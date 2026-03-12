#!/bin/bash

# Development Control Script
# Usage: ./devctl.sh [command] [options]
# Commands: start-infra, stop-infra, build, clean, test, spotless-check, spotless-apply, package, install, run-service, dev-setup, test-all

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Project root directory
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INFRA_DIR="$PROJECT_ROOT/infra"

print_usage() {
    echo -e "${BLUE}Development Control Script${NC}"
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Infrastructure Commands:"
    echo "  start-infra    - Start Kafka infrastructure"
    echo "  start-devices  - Start device simulators (ONT devices)"
    echo "  start-all      - Start all infrastructure (Kafka + devices)"
    echo "  stop-infra     - Stop Kafka infrastructure"
    echo "  stop-devices   - Stop device simulators"
    echo "  stop-all       - Stop all infrastructure services"
    echo "  restart-infra  - Restart Kafka infrastructure"
    echo "  restart-devices - Restart device simulators"
    echo "  restart-all    - Restart all infrastructure services"
    echo ""
    echo "Build Commands:"
    echo "  build          - Build all modules"
    echo "  clean          - Clean all modules"
    echo "  test           - Run tests for all modules"
    echo "  spotless-check - Check code formatting"
    echo "  spotless-apply - Apply code formatting"
    echo "  package        - Package all modules"
    echo "  install        - Install all modules to local repo"
    echo ""
    echo "Development Commands:"
    echo "  dev-setup      - Complete development setup (infra + build + format)"
    echo "  test-all       - Run complete test suite (format + build + test)"
    echo "  run-service    - Run specific service in dev mode"
    echo "  status         - Show status of all services"
    echo ""
    echo "Examples:"
    echo "  $0 dev-setup                    # Complete setup for development"
    echo "  $0 start-all                    # Start all infrastructure"
    echo "  $0 start-infra                  # Start Kafka only"
    echo "  $0 start-devices                # Start device simulators only"
    echo "  $0 run-service device-service    # Run specific service"
    echo "  $0 test-all                      # Run all checks and tests"
}

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_header() {
    echo -e "${CYAN}=== $1 ===${NC}"
}

# Change to project root
cd "$PROJECT_ROOT"

check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        print_error "Docker daemon is not running"
        exit 1
    fi
}

check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        exit 1
    fi
}

start_infra() {
    print_header "Starting Kafka Infrastructure"
    check_docker
    
    if [ -f "$INFRA_DIR/docker-compose-kafka.yml" ]; then
        print_status "Starting Kafka infrastructure..."
        cd "$INFRA_DIR"
        docker-compose -f docker-compose-kafka.yml up -d
        print_success "Kafka infrastructure started successfully!"
        
        print_status "Waiting for services to be ready..."
        sleep 10
        
        print_status "Checking Kafka service health..."
        if docker-compose -f docker-compose-kafka.yml ps | grep -q "Up"; then
            print_success "Kafka services are running!"
        else
            print_warning "Kafka services may not be fully ready yet"
        fi
        cd "$PROJECT_ROOT"
    else
        print_error "Kafka compose file not found: $INFRA_DIR/docker-compose-kafka.yml"
        exit 1
    fi
}

start_devices() {
    print_header "Starting Device Simulators"
    check_docker
    
    if [ -f "$INFRA_DIR/docker-compose-devices.yml" ]; then
        print_status "Starting device simulators (ONT devices)..."
        cd "$INFRA_DIR"
        docker-compose -f docker-compose-devices.yml up -d
        print_success "Device simulators started successfully!"
        
        print_status "Waiting for devices to be ready..."
        sleep 5
        
        print_status "Checking device service health..."
        if docker-compose -f docker-compose-devices.yml ps | grep -q "Up"; then
            print_success "Device simulators are running!"
        else
            print_warning "Device simulators may not be fully ready yet"
        fi
        cd "$PROJECT_ROOT"
    else
        print_error "Devices compose file not found: $INFRA_DIR/docker-compose-devices.yml"
        exit 1
    fi
}

start_all() {
    print_header "Starting All Infrastructure Services"
    start_infra
    sleep 2
    start_devices
    print_success "All infrastructure services started!"
}

stop_infra() {
    print_header "Stopping Kafka Infrastructure"
    check_docker
    
    if [ -f "$INFRA_DIR/docker-compose-kafka.yml" ]; then
        print_status "Stopping Kafka infrastructure..."
        cd "$INFRA_DIR"
        docker-compose -f docker-compose-kafka.yml down
        print_success "Kafka infrastructure stopped successfully!"
        cd "$PROJECT_ROOT"
    else
        print_error "Kafka compose file not found: $INFRA_DIR/docker-compose-kafka.yml"
        exit 1
    fi
}

stop_devices() {
    print_header "Stopping Device Simulators"
    check_docker
    
    if [ -f "$INFRA_DIR/docker-compose-devices.yml" ]; then
        print_status "Stopping device simulators..."
        cd "$INFRA_DIR"
        docker-compose -f docker-compose-devices.yml down
        print_success "Device simulators stopped successfully!"
        cd "$PROJECT_ROOT"
    else
        print_error "Devices compose file not found: $INFRA_DIR/docker-compose-devices.yml"
        exit 1
    fi
}

stop_all() {
    print_header "Stopping All Infrastructure Services"
    stop_devices
    stop_infra
    print_success "All infrastructure services stopped!"
}

restart_infra() {
    print_header "Restarting Kafka Infrastructure"
    stop_infra
    sleep 2
    start_infra
}

restart_devices() {
    print_header "Restarting Device Simulators"
    stop_devices
    sleep 2
    start_devices
}

restart_all() {
    print_header "Restarting All Infrastructure Services"
    stop_all
    sleep 3
    start_all
}

build_project() {
    print_header "Building Project"
    check_maven
    mvn clean compile
    print_success "Build completed successfully!"
}

clean_project() {
    print_header "Cleaning Project"
    check_maven
    mvn clean
    print_success "Clean completed successfully!"
}

run_tests() {
    print_header "Running Tests"
    check_maven
    mvn test
    print_success "Tests completed successfully!"
}

check_formatting() {
    print_header "Checking Code Formatting"
    check_maven
    mvn spotless:check
    print_success "Code formatting check passed!"
}

apply_formatting() {
    print_header "Applying Code Formatting"
    check_maven
    mvn spotless:apply
    print_success "Code formatting applied successfully!"
}

package_project() {
    print_header "Packaging Project"
    check_maven
    mvn clean package
    print_success "Packaging completed successfully!"
}

install_project() {
    print_header "Installing Project"
    check_maven
    mvn clean install
    print_success "Install completed successfully!"
}

run_service() {
    SERVICE="$2"
    if [ -z "$SERVICE" ]; then
        print_error "Please specify a service to run"
        echo "Available services: device-service, command-service"
        echo "Usage: $0 run-service [service-name]"
        exit 1
    fi
    
    if [ ! -d "$SERVICE" ]; then
        print_error "Service '$SERVICE' not found!"
        echo "Available services: device-service, command-service"
        exit 1
    fi
    
    print_header "Running $SERVICE in Development Mode"
    check_maven
    cd "$SERVICE"
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
}

dev_setup() {
    print_header "Complete Development Setup"
    
    print_status "Step 1: Starting all infrastructure..."
    start_all
    
    print_status "Step 2: Applying code formatting..."
    apply_formatting
    
    print_status "Step 3: Building project..."
    build_project
    
    print_status "Step 4: Running tests..."
    run_tests
    
    print_success "Development setup completed successfully!"
    print_status "You can now run individual services with: $0 run-service [service-name]"
}

test_all() {
    print_header "Running Complete Test Suite"
    
    print_status "Step 1: Checking code formatting..."
    check_formatting
    
    print_status "Step 2: Building project..."
    build_project
    
    print_status "Step 3: Running tests..."
    run_tests
    
    print_success "All tests and checks passed!"
}

show_status() {
    print_header "Service Status"
    
    print_status "Kafka Infrastructure:"
    if [ -f "$INFRA_DIR/docker-compose-kafka.yml" ]; then
        cd "$INFRA_DIR"
        echo "Kafka Services:"
        docker-compose -f docker-compose-kafka.yml ps
        cd "$PROJECT_ROOT"
    else
        print_warning "Kafka compose file not found"
    fi
    
    echo ""
    print_status "Device Simulators:"
    if [ -f "$INFRA_DIR/docker-compose-devices.yml" ]; then
        cd "$INFRA_DIR"
        echo "Device Services:"
        docker-compose -f docker-compose-devices.yml ps
        cd "$PROJECT_ROOT"
    else
        print_warning "Devices compose file not found"
    fi
    
    echo ""
    print_status "Project Modules:"
    if [ -d "device-service" ]; then
        echo "  ✓ device-service"
    fi
    if [ -d "command-service" ]; then
        echo "  ✓ command-service"
    fi
}

case "$1" in
    "start-infra")
        start_infra
        ;;
    
    "start-devices")
        start_devices
        ;;
    
    "start-all")
        start_all
        ;;
    
    "stop-infra")
        stop_infra
        ;;
    
    "stop-devices")
        stop_devices
        ;;
    
    "stop-all")
        stop_all
        ;;
    
    "restart-infra")
        restart_infra
        ;;
    
    "restart-devices")
        restart_devices
        ;;
    
    "restart-all")
        restart_all
        ;;
    
    "build")
        build_project
        ;;
    
    "clean")
        clean_project
        ;;
    
    "test")
        run_tests
        ;;
    
    "spotless-check")
        check_formatting
        ;;
    
    "spotless-apply")
        apply_formatting
        ;;
    
    "package")
        package_project
        ;;
    
    "install")
        install_project
        ;;
    
    "run-service")
        run_service "$@"
        ;;
    
    "dev-setup")
        dev_setup
        ;;
    
    "test-all")
        test_all
        ;;
    
    "status")
        show_status
        ;;
    
    "help"|"--help"|"-h")
        print_usage
        ;;
    
    *)
        print_error "Unknown command: $1"
        print_usage
        exit 1
        ;;
esac
