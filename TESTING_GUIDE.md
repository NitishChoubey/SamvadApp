# 🎯 READY TO TEST - Complete Testing Guide

## ✅ YES! You Can Now Test the App!

Here's the complete step-by-step testing procedure:

---

## 📋 PRE-TESTING CHECKLIST

Before you start testing, verify:

### Backend Status:
- [ ] ✅ JWT error is fixed (DONE!)
- [ ] ✅ Build is successful (DONE!)
- [ ] PostgreSQL service is running
- [ ] Database "Samvad" exists
- [ ] Backend server is started

### Android App Status:
- [ ] Android Studio is open
- [ ] Gradle files are synced
- [ ] No compilation errors
- [ ] Emulator or device is ready

---

## 🚀 STEP-BY-STEP TESTING PROCEDURE

### PHASE 1: Start Backend Server (5 minutes)

#### Step 1: Start PostgreSQL
```cmd
# Press Win + R → type: services.msc
# Find: postgresql-x64-16
# Status should be: Running
# If not → Right-click → Start
```

#### Step 2: Verify Database Exists
Open pgAdmin 4 or run:
```cmd
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -l
```
Password: `@Nitish@6250`

Look for "Samvad" in the list.

**If not found, create it:**
```cmd
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres
```
```sql
CREATE DATABASE "Samvad";
\q
```

#### Step 3: Start Backend
**Open CMD (not PowerShell):**
```cmd
cd D:\AndroidStudioProjects\Samvad\backend
mvn spring-boot:run
```

#### Step 4: Wait for Success Message
Look for:
```
Started SamvadBackendApplication in X.XXX seconds
```

#### Step 5: Verify Backend is Running
**Open browser:** http://localhost:8080

You should see:
```
Whitelabel Error Page
This application has no explicit mapping for /error
```
✅ **This is GOOD!** Server is running!

**Leave this CMD window open!** Backend must keep running.

---

### PHASE 2: Setup Android App (10 minutes)

#### Step 1: Open Android Studio
1. Open Android Studio
2. Open your Samvad project
3. Wait for indexing to complete

#### Step 2: Sync Gradle Files (CRITICAL!)
**This is MANDATORY!**

1. You'll see a banner: "Gradle files have changed since last sync"
2. Click **"Sync Now"**
3. Wait for sync to complete (2-5 minutes)
4. Download new dependencies (Retrofit, OkHttp, WebSocket)

**If you don't see the banner:**
- Go to: `File` → `Sync Project with Gradle Files`
- Or click the 🐘 (elephant) icon in toolbar

#### Step 3: Verify No Compilation Errors
After Gradle sync:
- Check the "Build" tab at bottom
- Should show: "BUILD SUCCESSFUL"
- No red errors in code editor

**If you see errors:**
- Clean Project: `Build` → `Clean Project`
- Rebuild: `Build` → `Rebuild Project`
- Sync Gradle again

#### Step 4: Check Build Configuration
Verify in `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
```

**For emulator:** Use `10.0.2.2:8080` ✅ (already set)
**For physical device:** Change to your PC's IP (e.g., `192.168.1.100:8080`)

---

### PHASE 3: Run and Test the App (20 minutes)

#### Step 1: Start Emulator or Connect Device

**Option A: Using Emulator**
1. Click the device dropdown in toolbar
2. Select an emulator (or create new one)
3. Click the ▶️ Run button
4. Wait for emulator to start

**Option B: Using Physical Device**
1. Enable Developer Options on your phone
2. Enable USB Debugging
3. Connect phone via USB
4. Allow USB debugging prompt
5. Select your device from dropdown
6. Click ▶️ Run button

**Important for Physical Device:**
If using a real phone, update `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "BASE_URL", "\"http://YOUR_PC_IP:8080/\"")
```

Find your PC IP:
```cmd
ipconfig
```
Look for "IPv4 Address" (e.g., 192.168.1.100)

Then sync Gradle again!

#### Step 2: App Launches
- App should install and launch
- You should see the Welcome/Splash screen
- Then the phone number entry screen

---

## 🧪 TESTING SCENARIOS

### TEST 1: Phone Authentication (OTP)

#### Step 1: Enter Phone Number
1. Enter phone number in format: `+1234567890`
2. Click "Get OTP" or "Send OTP" button

#### Step 2: Check Backend Console
**Switch to your CMD window running the backend**

Look for logs like:
```
OTP sent successfully to +1234567890
OTP for +1234567890: 123456
```

**Copy the 6-digit OTP from console!**

#### Step 3: Enter OTP
1. Go back to the app
2. You should see OTP entry screen
3. Enter the OTP from backend console
4. Click "Verify" button

#### Step 4: Expected Result
✅ OTP verified successfully
✅ JWT token received and stored
✅ Navigate to profile setup screen

**Check backend logs:**
```
OTP verified successfully
User authenticated: +1234567890
```

---

### TEST 2: User Profile Setup

#### Step 1: Enter Profile Details
1. Enter your name: "Your Name"
2. Enter status: "Hey there! I am using Samvad."
3. (Optional) Upload profile picture

#### Step 2: Click Save/Continue

#### Step 3: Expected Result
✅ Profile saved to database
✅ Navigate to home screen / chat list

**Check backend logs:**
```
Profile updated successfully
Profile image uploaded
```

**Verify in pgAdmin:**
1. Open pgAdmin 4
2. Navigate to: Samvad → Schemas → public → Tables → users
3. Right-click → View/Edit Data → All Rows
4. You should see your user record!

---

### TEST 3: Home Screen / Chat List

#### Step 1: Navigate to Home
You should see:
- ✅ App home screen
- ✅ Bottom navigation bar
- ✅ Empty chat list (first time)

#### Step 2: Try to Search Users
**Note:** User search endpoint needs to be implemented in backend
- This feature may not work yet
- Check backend console for "TODO: implement search" messages

---

### TEST 4: Send a Message (if available)

#### Step 1: Start a Chat
1. Try to open a chat
2. Enter a message
3. Send it

#### Step 2: Check Backend Console
Look for:
```
Message sent from <userId> to <receiverId>
WebSocket message received
```

#### Step 3: Expected Result
✅ Message sent via WebSocket
✅ Message saved to database

**Verify in pgAdmin:**
1. Open messages table
2. You should see your message record!

---

## 📊 WHAT TO CHECK IN EACH SCREEN

### Splash Screen
- [ ] Shows for 2-3 seconds
- [ ] Auto-navigates to next screen

### Welcome Screen
- [ ] Shows app logo/name
- [ ] "Get Started" button works

### Phone Entry Screen
- [ ] Can enter phone number
- [ ] "Send OTP" button works
- [ ] Shows loading indicator
- [ ] Navigates to OTP screen

### OTP Entry Screen
- [ ] Shows 6 input boxes for OTP
- [ ] Can enter digits
- [ ] "Verify" button works
- [ ] Shows loading indicator
- [ ] Navigates to profile setup

### Profile Setup Screen
- [ ] Can enter name
- [ ] Can enter status
- [ ] Can upload profile picture
- [ ] "Save" button works
- [ ] Navigates to home screen

### Home Screen
- [ ] Shows app bar/toolbar
- [ ] Shows bottom navigation
- [ ] Shows chat list (empty or with chats)
- [ ] Can navigate between tabs

---

## ✅ SUCCESS CRITERIA

Your testing is successful if:

### Backend:
- [x] ✅ Backend starts without errors
- [ ] ✅ Database connection successful
- [ ] ✅ OTP is logged to console
- [ ] ✅ API endpoints respond
- [ ] ✅ WebSocket connection works

### Android App:
- [ ] ✅ App builds without errors
- [ ] ✅ App installs on device/emulator
- [ ] ✅ App launches successfully
- [ ] ✅ Can send OTP request
- [ ] ✅ Can verify OTP
- [ ] ✅ Can create user profile
- [ ] ✅ Can navigate to home screen

### Database:
- [ ] ✅ Tables auto-created (users, messages, otp_verification)
- [ ] ✅ User record is inserted
- [ ] ✅ OTP records are created
- [ ] ✅ Messages are stored (if tested)

---

## 🐛 TROUBLESHOOTING DURING TESTING

### App Issue: "Failed to send OTP"

**Possible Causes:**
1. Backend not running
2. Wrong BASE_URL
3. Network issue

**Solutions:**
1. Check backend CMD window - should show "Started..."
2. For emulator: Use `10.0.2.2:8080`
3. For device: Use your PC IP + make sure same WiFi
4. Check Android Logcat for errors

**Check Logcat:**
```
View → Tool Windows → Logcat
Filter: "Samvad" or "PhoneAuth"
```

---

### App Issue: "Invalid OTP"

**Possible Causes:**
1. Wrong OTP entered
2. OTP expired (5 minutes)
3. Backend issue

**Solutions:**
1. Check backend console for correct OTP
2. Request new OTP if expired
3. Check backend logs for verification errors

---

### App Issue: "CLEARTEXT communication not permitted" ⚠️

**Error Message:**
```
CLEARTEXT communication to 10.0.2.2 not permitted by network security policy
```

**Cause:** Android 9+ blocks HTTP traffic by default.

**Solution:** ✅ **ALREADY FIXED!**
- Network security config created: `app/src/main/res/xml/network_security_config.xml`
- AndroidManifest updated to allow HTTP for development

**If you still see this error:**
1. **Rebuild the app** (mandatory!):
   - `Build` → `Clean Project`
   - `Build` → `Rebuild Project`
2. **Reinstall**: Click ▶️ Run button
3. Test again

---

### App Issue: Network error / Connection refused

**For Emulator:**
```kotlin
// In app/build.gradle.kts - should be:
buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
```

**For Physical Device:**
1. Find your PC IP: `ipconfig`
2. Update BASE_URL: `"http://192.168.1.XXX:8080/"`
3. Ensure phone and PC on same WiFi
4. Sync Gradle
5. Rebuild app

---

### Backend Issue: Database connection error

**Check:**
1. PostgreSQL service running
2. Database "Samvad" exists
3. Credentials correct in `application.properties`

**Fix:**
```cmd
services.msc
# Start postgresql-x64-16

# Create database if needed
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres
CREATE DATABASE "Samvad";
\q
```

---

### Backend Issue: Port 8080 already in use

**Solution:**
```cmd
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8081
```

---

## 📝 TESTING LOGS TO COLLECT

### Backend Console Logs:
Save logs showing:
- Application startup
- Database connection
- OTP generation
- API requests/responses
- WebSocket messages

### Android Logcat:
Filter for:
- `PhoneAuthViewModel`
- `BaseViewModel`
- `ApiServices`
- `WebSocketManager`
- `Retrofit`

Look for:
- API calls
- Responses
- Errors
- Token storage

### Database Records:
Take screenshots of:
- users table with your record
- otp_verification table with OTP records
- messages table with test messages

---

## 🎯 COMPLETE TESTING WORKFLOW

```
1. Start PostgreSQL        ✓
2. Start Backend           ✓
3. Verify Backend Running  ✓
4. Open Android Studio     →
5. Sync Gradle Files       → (CRITICAL!)
6. Run App                 →
7. Test Authentication     →
8. Test Profile Setup      →
9. Test Home Screen        →
10. Test Messaging         →
```

---

## 🎉 EXPECTED RESULTS

### First Time User Flow:
```
Splash → Welcome → Phone Entry → OTP Entry → Profile Setup → Home
```

### Returning User Flow:
```
Splash → Home (auto-login with stored token)
```

---

## 📞 QUICK REFERENCE

### Backend Commands:
```cmd
# Start backend
cd D:\AndroidStudioProjects\Samvad\backend
mvn spring-boot:run

# Stop backend
Ctrl + C
```

### Check Backend Status:
```
Browser: http://localhost:8080
Expected: Whitelabel Error Page (means it's running!)
```

### Android Studio:
```
Sync Gradle: File → Sync Project with Gradle Files
Clean: Build → Clean Project
Rebuild: Build → Rebuild Project
Run: Click ▶️ button or Shift+F10
```

### Find PC IP (for physical device):
```cmd
ipconfig
# Look for IPv4 Address
```

---

## ✅ YOU'RE READY TO TEST!

### Final Checklist:
- [ ] Backend is running (check CMD window)
- [ ] Android Studio is open
- [ ] Gradle is synced
- [ ] Emulator/device is ready
- [ ] You understand the test flow

### Start Testing:
1. Click the ▶️ Run button in Android Studio
2. Wait for app to launch
3. Follow the test scenarios above
4. Check logs in both backend and Logcat

---

## 🎊 GOOD LUCK!

**Everything is ready for testing!**

**The migration is complete - now it's time to see it in action!**

**If you encounter any issues, check the troubleshooting sections above!**

---

**Remember:**
- Backend MUST be running during testing
- Use `10.0.2.2:8080` for emulator
- Check OTP in backend console
- Sync Gradle before first run

**Happy Testing! 🚀**

