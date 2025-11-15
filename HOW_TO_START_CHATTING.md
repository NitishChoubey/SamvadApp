# 🚀 How to Start Chatting in Samvad App

## ✅ Prerequisites (Already Done)
- ✅ Backend server is running on port 8080
- ✅ PostgreSQL database is running
- ✅ Android app is installed on emulator
- ✅ App is configured to use `http://10.0.2.2:8080/`

---

## 📱 Step-by-Step Guide to Start Chatting

### **Step 1: Open the App**
The app should already be open in your emulator.

### **Step 2: Register with Phone Number**

1. **Enter a phone number** (use any format, e.g., `+919876543210` or `9876543210`)
2. Click **"Send OTP"** button

### **Step 3: Get the OTP from Backend Logs**

Since Twilio is not configured, the OTP won't be sent via SMS. Instead:

1. **Open the terminal/command prompt where the backend is running**
2. **Look for a log message** that says:
   ```
   OTP for +919876543210: 123456
   ```
3. **Copy the 6-digit OTP code**

### **Step 4: Enter OTP and Login**

1. Enter the OTP code you got from the backend logs
2. Click **"Verify OTP"** button
3. The app will:
   - Create a new user account (if first time)
   - Generate a JWT token
   - Log you in automatically

### **Step 5: Start Chatting**

Once logged in, you should see:
- **Contacts/Chats list screen** - showing your conversations
- **Bottom navigation** or **FAB button** to start a new chat

To start a chat:
1. Click the **"New Chat"** or **"+"** button
2. Select a contact or enter a phone number
3. Start typing and send messages!

---

## 🔧 Testing with Multiple Users

To test chatting between two users:

### **Option 1: Two Emulators**
1. Start another Android emulator
2. Install the app on it
3. Register with a different phone number
4. Chat between the two emulators

### **Option 2: Emulator + Real Device**
1. Find your computer's IP address (e.g., `192.168.1.100`)
2. In `app/build.gradle.kts`, change:
   ```kotlin
   buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
   ```
   to:
   ```kotlin
   buildConfigField("String", "BASE_URL", "\"http://YOUR_IP:8080/\"")
   ```
3. Rebuild the app and install on your phone
4. Register with a different phone number
5. Chat between emulator and phone

---

## 🐛 Quick Troubleshooting

### **Problem: Can't send OTP**
- **Solution**: Check backend logs - OTP will be printed there (since Twilio is not configured)

### **Problem: OTP verification fails**
- **Solution**: Make sure you're entering the exact OTP from backend logs
- Check that the OTP hasn't expired (valid for 5 minutes)

### **Problem: Can't connect to backend**
- **Solution**: Check if backend is running on port 8080:
  ```cmd
  netstat -ano | findstr :8080
  ```
- Check that emulator is using `http://10.0.2.2:8080/`

### **Problem: Messages not sending**
- **Solution**: 
  - Check WebSocket connection in Logcat
  - Check backend logs for WebSocket connection messages
  - Make sure JWT token is being sent with requests

### **Problem: Can't see other user's messages**
- **Solution**: 
  - Make sure both users are connected to the same backend
  - Check WebSocket connection status
  - Verify both users have valid JWT tokens

---

## 📝 Backend Endpoints Being Used

- `POST /api/auth/send-otp` - Send OTP to phone number
- `POST /api/auth/verify-otp` - Verify OTP and login
- `POST /api/auth/refresh` - Refresh JWT token
- `GET /api/users` - Get all users/contacts
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update user profile
- `GET /api/messages/chat/{userId}` - Get chat history
- `WebSocket /ws` - Real-time messaging

---

## 🎯 Quick Test Scenario

1. **Register User 1**: `+919876543210` → Get OTP from logs → Login
2. **Update Profile**: Set name to "Alice" (if app has profile screen)
3. **Register User 2**: Use another emulator/device with `+919876543211`
4. **Start Chat**: User 1 searches for User 2's phone number
5. **Send Message**: Type "Hello!" and send
6. **Verify**: User 2 should receive the message in real-time

---

## 📞 Need Help?

- Check **Logcat** in Android Studio for app logs
- Check **backend console** for server logs
- Look for ERROR or WARN messages
- Common issues are usually:
  - Backend not running
  - Wrong BASE_URL configuration
  - Database connection issues
  - WebSocket connection failures

---

**Happy Chatting! 💬**

