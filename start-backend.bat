@echo off
echo ================================================
echo   Samvad Backend - Quick Start Script
echo ================================================
echo.

cd /d "%~dp0backend"

echo Checking if Maven is installed...
call mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)

echo.
echo Maven found! Building the project...
echo.

call mvn clean install
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed! Check the errors above.
    pause
    exit /b 1
)

echo.
echo ================================================
echo   Build Successful! Starting the server...
echo ================================================
echo.
echo Server will start at: http://localhost:8080
echo.
echo IMPORTANT: Make sure your database is running!
echo - PostgreSQL: localhost:5432/samvad_db
echo   OR
echo - MySQL: localhost:3306/samvad_db
echo.
echo Press Ctrl+C to stop the server
echo ================================================
echo.

call mvn spring-boot:run

pause

