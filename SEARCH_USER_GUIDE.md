# 🔍 Search User & Start Chat - Complete Guide

## ✅ What I Fixed

### Backend Changes:
1. **Added Search Endpoint** in `UserController.java`:
   - `GET /api/users/search?phoneNumber={phoneNumber}`
   - Searches for users by phone number
   - Returns user details if found

### Android App Changes:
1. **Added API Method** in `ApiServices.kt`:
   - `searchByPhoneNumber(phoneNumber: String)`
   
2. **Implemented Search Logic** in `BaseViewModel.kt`:
   - Now actually calls the backend API
   - Returns user details when found
   - Shows proper error messages when not found

---

## 🧪 How to Test the Search Feature

### Step 1: Make Sure You Have 2 Registered Users

**User 1 (Already registered - YOU):**
- Phone: The number you registered with (e.g., `9876543210`)
- Status: Logged in

**User 2 (Need to register):**
1. Open a NEW emulator OR use your real phone
2. Install the app
3. Register with a DIFFERENT phone number (e.g., `9876543211`)
4. Complete OTP verification

### Step 2: Search for User 2 from User 1's App

1. **In User 1's app**, click the **"+"** button (Add new chat)
2. **Enter User 2's phone number** exactly as registered:
   - Example: `9876543211`
   - Or with country code: `+919876543211`
3. Click **"Search"** button
4. **Wait 2-3 seconds** for the API call

### Step 3: What Should Happen

✅ **If User Found:**
- User's name and phone number will appear
- Click on the user to start chatting

❌ **If User NOT Found:**
- You'll see "User not found" or no results
- **Common reasons:**
  - Phone number not registered in database
  - Wrong phone number format
  - Backend not running

---

## 🐛 Troubleshooting Search Issues

### Problem 1: Nothing shows after clicking Search

**Check these:**

1. **Is the backend running?**
   ```cmd
   netstat -ano | findstr :8080
   ```
   Should show: `LISTENING 12328` (or similar PID)

2. **Check Android Logcat** for errors:
   - Open Logcat in Android Studio
   - Filter by: `BaseViewModel`
   - Look for:
     - ✅ `Searching for user with phone: 9876543211`
     - ✅ `User found: userId123, John`
     - ❌ `User not found: 404`
     - ❌ `Error searching user: ...`

3. **Check Backend Logs**:
   - Look for:
     - ✅ `Searching for user with phone number: 9876543211`
     - ✅ `User found: userId123`
     - ❌ `User not found with phone number: 9876543211`

### Problem 2: "User not found" but user exists

**Possible causes:**

1. **Phone number format mismatch:**
   - Database has: `9876543211`
   - You searched: `+919876543211`
   - **Solution**: Try both formats

2. **User not actually registered:**
   - Check database:
     ```sql
     SELECT * FROM users WHERE phone_number LIKE '%9876543211%';
     ```

3. **Backend can't connect to database:**
   - Check backend logs for database connection errors

### Problem 3: App crashes when searching

**Check:**
1. **Internet permission** in `AndroidManifest.xml`
2. **Network connectivity** on emulator
3. **Logcat crash logs**

---

## 📱 Complete Test Scenario

### Scenario: Two Users Start Chatting

**Setup:**
1. ✅ Backend running on port 8080
2. ✅ PostgreSQL database running
3. ✅ Two emulators/devices with app installed

**Test Steps:**

1. **Emulator 1 - Register Alice:**
   - Phone: `1111111111`
   - Get OTP from backend logs
   - Login successfully

2. **Emulator 2 - Register Bob:**
   - Phone: `2222222222`
   - Get OTP from backend logs
   - Login successfully

3. **Alice Searches for Bob:**
   - Click "+" button
   - Enter: `2222222222`
   - Click "Search"
   - ✅ Bob's profile should appear

4. **Alice Starts Chat with Bob:**
   - Click on Bob's profile
   - Type message: "Hi Bob!"
   - Send message
   - ✅ Message should appear in chat

5. **Bob Receives Message:**
   - Check Bob's app
   - ✅ Should see chat from Alice
   - ✅ Should see "Hi Bob!" message

---

## 🔧 API Testing (Optional)

You can test the search endpoint directly:

### Using Browser:
```
http://localhost:8080/api/users/search?phoneNumber=9876543211
```

### Using curl:
```cmd
curl -X GET "http://localhost:8080/api/users/search?phoneNumber=9876543211" ^
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Expected Response (Success):
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "phoneNumber": "9876543211",
  "name": "John Doe",
  "status": "Hey there! I am using Samvad.",
  "profileImageUrl": null
}
```

### Expected Response (Not Found):
```json
{
  "error": "User not found"
}
```
Status: 404

---

## 📊 Database Check

To verify users in database:

### Connect to PostgreSQL:
```cmd
psql -U postgres -d Samvad
```

### Check all users:
```sql
SELECT user_id, phone_number, name, status, created_at 
FROM users 
ORDER BY created_at DESC;
```

### Search specific user:
```sql
SELECT * FROM users WHERE phone_number = '9876543211';
```

---

## 🎯 Quick Checklist

Before testing search:
- [ ] Backend is running (port 8080 in use)
- [ ] PostgreSQL is running (port 5432 in use)
- [ ] At least 2 users registered in database
- [ ] You know both phone numbers
- [ ] App is rebuilt with latest changes
- [ ] Emulator/device has internet connectivity

During search:
- [ ] Enter phone number exactly as registered
- [ ] Wait 2-3 seconds after clicking Search
- [ ] Check Logcat for errors if nothing happens
- [ ] Check backend logs for search requests

---

## 🚀 Next Steps After Search Works

1. **Start a chat** by clicking on found user
2. **Send messages** back and forth
3. **Test real-time messaging** (WebSocket)
4. **Update profiles** with names and photos
5. **Create group chats** (if implemented)

---

## 💡 Tips

1. **Use simple phone numbers for testing:**
   - User 1: `1111111111`
   - User 2: `2222222222`
   - User 3: `3333333333`

2. **Keep backend terminal visible** to see OTPs and search logs

3. **Keep Logcat open** to see Android app logs

4. **Test on emulator first** before testing on real devices

5. **Use different phone numbers** for each user (don't reuse)

---

## 📞 Need More Help?

If search still doesn't work:

1. **Share the Logcat output** (filter: BaseViewModel)
2. **Share the backend logs** (last 20 lines)
3. **Check if you can access** `http://10.0.2.2:8080/api/users/search?phoneNumber=1111111111` from emulator browser
4. **Verify database has users** with SQL query above

---

**Good luck! The search should now work. Just make sure both users are registered in the database first!** 🎉

