#!/bin/bash

echo "================================================"
echo "  Samvad Backend - Quick Start Script"
echo "================================================"
echo ""

cd "$(dirname "$0")/backend"

echo "Checking if Maven is installed..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/"
    exit 1
fi

echo ""
echo "Maven found! Building the project..."
echo ""

mvn clean install
if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Build failed! Check the errors above."
    exit 1
fi

echo ""
echo "================================================"
echo "  Build Successful! Starting the server..."
echo "================================================"
echo ""
echo "Server will start at: http://localhost:8080"
echo ""
echo "IMPORTANT: Make sure your database is running!"
echo "- PostgreSQL: localhost:5432/samvad_db"
echo "  OR"
echo "- MySQL: localhost:3306/samvad_db"
echo ""
echo "Press Ctrl+C to stop the server"
echo "================================================"
echo ""

mvn spring-boot:run

