# Device Management Application

A multi-module Spring Boot application for managing network devices (ONTs) and executing NETCONF commands on them via Kafka-driven async processing.

## Architecture

```
                   ┌─────────────────┐
              ┌───▶│ device-service  │
              │    │   (port 8080)   │
              │    └─────────────────┘
              │             ▲
┌──────────┐  │             │ (lookup device info)
│  Client  │──┤             │
│  (curl)  │  │    ┌────────┴───────┐       ┌───────────┐       ┌────────────────┐
└──────────┘  └───▶│ command-service│──────▶│   Kafka   │──────▶│ device-gateway │
                   │   (port 8081)  │       │           │       │  (port 8082)   │
                   └────────────────┘       └───────────┘       └───────┬────────┘
                                                                        │
                                                                        │ NETCONF/SSH
                                                                        │
                                                                ┌───────▼────────┐
                                                                │  ONT Devices   │
                                                                │ (ont1, ont2...)│
                                                                └────────────────┘
```

### Request Flow

1. Client registers devices via **device-service** REST API
2. Client sends a command via **command-service** REST API
3. Command is persisted and published to **Kafka** (`device-commands` topic)
4. **device-gateway** consumes the event, looks up the device from **device-service**
5. Gateway establishes a NETCONF/SSH session and executes the command on the device
6. Gateway updates command status back in **command-service** (`PENDING → IN_PROGRESS → SUCCESS / FAILED`)
7. Client can query command status via **command-service**

## Modules

### device-contracts

Shared library containing DTOs and models used across services.

| Model | Description |
|-------|-------------|
| `DeviceResponse` | Device info returned by device-service |
| `CommandEvent` | Kafka event published when a command is created |
| `CommandResponse` | Command info returned by command-service |
| `CommandType` | `GET_CONFIG`, `SET_CONFIG`, `REBOOT` |
| `CommandStatus` | `PENDING`, `IN_PROGRESS`, `SUCCESS`, `FAILED` |
| `DeviceStatus` | `REGISTERED`, `ONLINE`, `OFFLINE` |

### device-service (port 8080)

Device inventory service. Manages device registration and lookup.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/devices` | Register a new device |
| `GET` | `/devices` | List all devices |
| `GET` | `/devices/{deviceId}` | Get device by ID |

### command-service (port 8081)

Command management service. Accepts commands, persists them, and publishes to Kafka. Auto-resolves NETCONF RPC payloads for known command types when `payload` is not provided.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/commands` | Create and dispatch a command |
| `GET` | `/commands/{id}` | Get command by ID |
| `PATCH` | `/commands/{id}/status` | Update command status |

**Auto-resolved payloads:**

| Command Type | Generated RPC |
|-------------|---------------|
| `GET_CONFIG` | `<get-config><source><running/></source></get-config>` |
| `REBOOT` | `<system-restart xmlns="urn:ietf:params:xml:ns:yang:ietf-system"/>` |
| `SET_CONFIG` | Requires `payload` in the request |

### device-gateway (port 8082)

Kafka consumer and NETCONF execution engine. Consumes command events, connects to devices via NETCONF/SSH (JNC library), executes RPCs, and reports status back to command-service.

### Infrastructure

| Component | Description |
|-----------|-------------|
| Kafka (KRaft) | Message broker on port `9092` |
| ont1 | Simulated NETCONF device (sysrepo/netopeer2) on port `8301` |
| ont2 | Simulated NETCONF device (sysrepo/netopeer2) on port `8302` |

## Prerequisites

- Java 21
- Maven 3.9+
- Docker & docker-compose

## Getting Started

### Using app.sh

| Command | Description |
|---------|-------------|
| `./app.sh` | Clean, test, and build (default) |
| `./app.sh build` | Clean compile |
| `./app.sh test` | Run tests |
| `./app.sh format` | Apply Spotless code formatting |
| `./app.sh start-infra` | Start Kafka + ONT device simulators |
| `./app.sh stop-infra` | Stop all infrastructure |
| `./app.sh run <service>` | Run a single service (e.g. `device-service`) |
| `./app.sh run-all` | Run all services in parallel |

### Quick Start

```bash
# 1. Build the project
./app.sh

# 2. Start infrastructure (Kafka + simulated devices)
./app.sh start-infra

# 3. Run all services
./app.sh run-all
```

## Sample cURL Commands

### 1. Register devices

```bash
# Register ONT1
curl -s -X POST http://localhost:8080/devices \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "ONT1",
    "name": "ONT Device 1",
    "ipAddress": "localhost",
    "port": 8301,
    "vendor": "Sysrepo",
    "model": "Netopeer2"
  }' | jq

# Register ONT2
curl -s -X POST http://localhost:8080/devices \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "ONT2",
    "name": "ONT Device 2",
    "ipAddress": "localhost",
    "port": 8302,
    "vendor": "Sysrepo",
    "model": "Netopeer2"
  }' | jq
```

### 2. List all devices

```bash
curl -s http://localhost:8080/devices | jq
```

### 3. Get a specific device

```bash
curl -s http://localhost:8080/devices/ONT1 | jq
```

### 4. Send a GET_CONFIG command

```bash
curl -s -X POST http://localhost:8081/commands \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "ONT1",
    "commandType": "GET_CONFIG"
  }' | jq
```

### 5. Send a REBOOT command

```bash
curl -s -X POST http://localhost:8081/commands \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "ONT1",
    "commandType": "REBOOT"
  }' | jq
```

### 6. Send a SET_CONFIG command (with custom payload)

```bash
curl -s -X POST http://localhost:8081/commands \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "ONT1",
    "commandType": "SET_CONFIG",
    "payload": "<edit-config><target><running/></target><config><interfaces xmlns=\"urn:ietf:params:xml:ns:yang:ietf-interfaces\"><interface><name>eth0</name><type xmlns:ianaift=\"urn:ietf:params:xml:ns:yang:iana-if-type\">ianaift:ethernetCsmacd</type></interface></interfaces></config></edit-config>"
  }' | jq
```

### 7. Check command status

```bash
# Replace 1 with the command ID from the create response
curl -s http://localhost:8081/commands/1 | jq
```

## Logs

All service logs are written to the `logs/` directory at the project root:

```
logs/
├── device-service.log
├── command-service.log
└── device-gateway.log
```

Logs rotate daily with 30-day retention (1GB max total size).
