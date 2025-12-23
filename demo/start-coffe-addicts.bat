@echo off
:: ───────────────────────────────────────────────
::  COFFEE ADDICTS SERVER STARTER
:: ───────────────────────────────────────────────

title Coffee Addicts Starter
chcp 65001 >nul 2>&1
color 0B

echo ======================================================
echo              [ COFFEE ADDICTS SERVER STARTER ]
echo ======================================================
echo.
echo   1. Make sure JDK 17 is installed.
echo   2. Add your JDK path below if it's different.
echo   3. The application will open GraphiQL automatically.
echo   4. Execute the GraphQL request below in GraphiQL:
echo.
echo        {
echo.          getLocationsBasedOnCoordinates(x: 47.6, y: -122.4) {
echo.              name
echo.              x
echo.              y
echo.          }
echo.        }
echo.
echo ------------------------------------------------------
echo.

:: === CONFIGURABLE VARIABLES ===
set "JDK_PATH=C:\Program Files\OpenLogic\jdk-17.0.6.10-hotspot\bin\java.exe"
set "CSV_URL=https://raw.githubusercontent.com/EmanuelButoiGit/coffee-addicts-api/refs/heads/main/data/coffee_shops.csv"
set "JAR_NAME=coffee.addicts.jar"
set "LOG_FILE=coffee-addicts.log"

:: Add timestamp for log entries
echo ------------------------------------------------------ >> "%LOG_FILE%"
echo [%DATE% %TIME%] Starting Coffee Addicts... >> "%LOG_FILE%"
echo ------------------------------------------------------ >> "%LOG_FILE%"

:: === VALIDATE JAVA PATH ===
if not exist "%JDK_PATH%" (
    echo [ERROR] Could not find Java at:
    echo         %JDK_PATH%
    echo Please edit this batch file and set the correct JDK_PATH.
    echo.
    pause
    exit /b 1
)

:: === STARTUP ===
echo Launching GraphiQL in browser...
start "" http://localhost:8080/graphiql?path=/graphql

:: Small pause to ensure browser opens first
timeout /t 2 >nul

echo Starting %JAR_NAME% ...
echo (Logs will be written to %LOG_FILE%)
echo ------------------------------------------------------

"%JDK_PATH%" -jar "%JAR_NAME%" --csv.url="%CSV_URL%" >> "%LOG_FILE%" 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Something went wrong. Check "%LOG_FILE%" for details.
    echo.
    echo [OK] Coffee Addicts is running successfully!
)

echo ------------------------------------------------------
echo.
pause
