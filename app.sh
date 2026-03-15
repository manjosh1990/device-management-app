#!/bin/bash
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INFRA_DIR="$PROJECT_ROOT/infra"
cd "$PROJECT_ROOT"

info()    { echo -e "${BLUE}[INFO]${NC} $1"; }
success() { echo -e "${GREEN}[OK]${NC} $1"; }
err()     { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

case "$1" in
  build)
    mvn clean compile
    success "Build complete"
    ;;
  format)
    mvn spotless:apply
    success "Formatting applied"
    ;;
  test)
    mvn test
    success "Tests passed"
    ;;
  start-infra)
    docker-compose -p kafka -f "$INFRA_DIR/docker-compose-kafka.yml" up -d --force-recreate
    docker-compose -p devices -f "$INFRA_DIR/docker-compose-devices.yml" up -d --force-recreate
    success "Infrastructure started"
    ;;
  stop-infra)
    docker-compose -p devices -f "$INFRA_DIR/docker-compose-devices.yml" down
    docker-compose -p kafka -f "$INFRA_DIR/docker-compose-kafka.yml" down
    success "Infrastructure stopped"
    ;;
  run)
    [ -z "$2" ] && err "Usage: $0 run <service-name>"
    [ ! -d "$2" ] && err "Service '$2' not found"
    cd "$2"
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ;;
  run-all)
    SERVICES=(device-service command-service device-gateway)
    PIDS=()
    for svc in "${SERVICES[@]}"; do
      info "Starting $svc..."
      (cd "$svc" && mvn spring-boot:run -Dspring-boot.run.profiles=dev) &
      PIDS+=("$!")
    done
    success "All services started (PIDs: ${PIDS[*]})"
    trap "kill ${PIDS[*]} 2>/dev/null" EXIT
    wait
    ;;
  "")
    info "Running clean, test, and build..."
    mvn clean test compile
    success "Clean, test, and build complete"
    ;;
  *)
    echo "Usage: $0 {build|format|test|start-infra|stop-infra|run <service>|run-all}"
    ;;
esac
