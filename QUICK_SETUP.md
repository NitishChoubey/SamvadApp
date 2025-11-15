# Quick Setup Guide for Samvad Migration

## 🎯 IMPORTANT: First Steps After Migration

### 1. Sync Android Project (REQUIRED)

**Before running the app, you MUST sync Gradle:**

1. Open Android Studio
2. Open the Samvad project
3. You will see a banner saying "Gradle files have changed"
4. Click **"Sync Now"**
5. Wait for Gradle to download new dependencies (Retrofit, OkHttp, etc.)
6. This may take 2-5 minutes depending on your internet speed

**If you see compilation errors, they will disappear after syncing!**

---

## 🗄️ Database Setup (Choose One)

### Option A: PostgreSQL (Recommended)

1. **Install PostgreSQL** (if not installed):
   - Download from: https://www.postgresql.org/download/
   - Default port: 5432

2. **Create Database:**
   ```sql
   -- Open psql or pgAdmin
   CREATE DATABASE samvad_db;
   CREATE USER samvad_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE samvad_db TO samvad_user;
   ```

3. **Update application.properties:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db
   spring.datasource.username=samvad_user
   spring.datasource.password=your_password
   ```

### Option B: MySQL

1. **Install MySQL** (if not installed):
   - Download from: https://dev.mysql.com/downloads/
   - Default port: 3306

2. **Create Database:**
   ```sql
   -- Open MySQL Workbench or command line
   CREATE DATABASE samvad_db;
   CREATE USER 'samvad_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON samvad_db.* TO 'samvad_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Update application.properties:**
   ```properties
   # Comment out PostgreSQL line and uncomment these:
   spring.datasource.url=jdbc:mysql://localhost:3306/samvad_db
   spring.datasource.username=samvad_user
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

---

## 🔑 Configure JWT Secret

**IMPORTANT:** Change the JWT secret in `application.properties`:

```properties
# Replace with a secure random string (at least 256 bits / 32 characters)
jwt.secret=your-super-secret-key-change-this-make-it-long-and-random-at-least-32-chars
```

To generate a secure secret, you can use:
- Online: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
- Command line: `openssl rand -base64 32`

---

## 📱 SMS Configuration (Optional)

### For Testing WITHOUT SMS:
The backend will log the OTP to the console instead of sending SMS.
Just check the Spring Boot console output when you request an OTP.

### For Production WITH SMS:
1. Sign up for Twilio: https://www.twilio.com/
2. Get your Account SID, Auth Token, and Phone Number
3. Update `application.properties`:
   ```properties
   twilio.account-sid=your_twilio_account_sid
   twilio.auth-token=your_twilio_auth_token
   twilio.phone-number=+1234567890
   ```

---

## 🚀 Start the Backend

### Windows:
```bash
# Double-click or run from command prompt:
start-backend.bat
```

### Linux/Mac:
```bash
chmod +x start-backend.sh
./start-backend.sh
```

### Manual Start:
```bash
cd backend
mvn spring-boot:run
```

**Server will start at:** `http://localhost:8080`

---

## 📱 Run Android App

### For Emulator:
The app is already configured to use `http://10.0.2.2:8080/`
Just run the app!

### For Physical Device:
1. Find your computer's IP address:
   - Windows: `ipconfig` (look for IPv4 Address)
   - Linux/Mac: `ifconfig` or `ip addr`
   
2. Update `app/build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "BASE_URL", "\"http://YOUR_IP:8080/\"")
   ```
   Example: `"http://192.168.1.100:8080/"`

3. Make sure your phone and computer are on the same WiFi network

4. Sync Gradle and run the app

---

## ✅ Testing the Setup

### Test 1: Backend Health Check
Open browser or Postman:
```
GET http://localhost:8080/api/auth/send-otp
```
You should see a 400 error (expected - missing request body)
If you see this, the server is running!

### Test 2: Send OTP from Android
1. Open the app
2. Enter a phone number (use format: +1234567890)
3. Click "Get OTP"
4. Check Spring Boot console for OTP (if Twilio not configured)
5. Enter OTP and verify

### Test 3: Database Tables Created
After first run, check your database. These tables should exist:
- `users`
- `messages`
- `otp_verification`

---

## 🐛 Common Issues & Solutions

### Issue: "Cannot resolve symbol 'BuildConfig'"
**Solution:** Sync Gradle files in Android Studio

### Issue: "Unresolved reference: Retrofit"
**Solution:** Sync Gradle files and wait for dependencies to download

### Issue: Backend won't start - "Unable to create initial connections"
**Solution:** 
- Check if database is running
- Verify credentials in application.properties
- Check database port (5432 for PostgreSQL, 3306 for MySQL)

### Issue: Android app shows "Failed to send OTP"
**Solution:**
- Check if backend is running (http://localhost:8080)
- Check Android emulator can reach backend (use 10.0.2.2:8080)
- Check firewall settings
- Check backend console for errors

### Issue: OTP not received
**Solution:**
- Without Twilio: Check Spring Boot console logs for OTP
- With Twilio: Verify credentials and phone number format

### Issue: JWT token errors
**Solution:**
- Ensure jwt.secret is at least 32 characters
- Check token expiration time
- Clear app data and login again

---

## 📊 Verify Migration Success

Check these items:

- ✅ Backend starts without errors
- ✅ Database tables are created
- ✅ Android app builds without errors
- ✅ Can send OTP request
- ✅ Can verify OTP
- ✅ Can update user profile
- ✅ Can send messages (WebSocket)

---

## 📞 Getting Help

1. Check **MIGRATION_GUIDE.md** for detailed documentation
2. Check Spring Boot console logs for backend errors
3. Check Android Logcat for app errors
4. Verify database connection
5. Test API endpoints with Postman

---

## 🎉 Success Checklist

Before considering migration complete:

- [ ] Database is created and configured
- [ ] Backend starts successfully
- [ ] Android app syncs without errors
- [ ] Can authenticate with OTP
- [ ] Can create user profile
- [ ] Can upload profile image
- [ ] Can send messages
- [ ] Can receive messages in real-time

---

## 📈 Next Steps After Testing

1. **Add Search Functionality** - Implement user search API
2. **Add Chat List** - Implement chat list API
3. **Improve Error Handling** - Better user feedback
4. **Add Offline Support** - Cache messages locally
5. **Setup Production Environment** - Deploy to cloud
6. **Configure HTTPS** - SSL certificate
7. **Setup CI/CD** - Automated deployment
8. **Add Monitoring** - Application performance monitoring

---

**You're now ready to start testing! Good luck! 🚀**

