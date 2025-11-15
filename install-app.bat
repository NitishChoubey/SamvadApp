@echo off
echo ================================================
echo   Installing Samvad App on Emulator
echo ================================================
echo.

set ADB="C:\Users\nitis\AppData\Local\Android\Sdk\platform-tools\adb.exe"
set APK="D:\AndroidStudioProjects\Samvad\app\build\outputs\apk\debug\app-debug.apk"

echo Checking ADB connection...
%ADB% devices
echo.

echo Installing APK...
%ADB% install -r %APK%

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo   SUCCESS! App installed successfully!
    echo ================================================
    echo.
    echo Now open the Samvad app on your emulator and:
    echo 1. Click on "Rahul" or "+916299530370"
    echo 2. Chat screen should open!
    echo.
    echo If it still doesn't work, check Logcat in Android Studio.
    echo.
) else (
    echo.
    echo ================================================
    echo   ERROR: Installation failed!
    echo ================================================
    echo.
    echo Possible solutions:
    echo 1. Make sure the emulator is running
    echo 2. Try: Run -^> Run 'app' from Android Studio
    echo 3. Or drag-drop the APK into emulator window
    echo.
    echo APK location: %APK%
    echo.
)

pause

