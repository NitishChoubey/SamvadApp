# ✅ BACKEND STARTUP - FIXED!

## 🎉 The Error Was Fixed!

The JWT compilation error has been **RESOLVED**! The issue was with the JWT library API compatibility.

### What Was Fixed:
- ✅ JWT `parserBuilder()` → Changed to `parser()`
- ✅ JWT `setSigningKey()` → Changed to `verifyWith()`
- ✅ JWT `parseClaimsJws()` → Changed to `parseSignedClaims()`
- ✅ JWT `getBody()` → Changed to `getPayload()`
- ✅ JWT Builder API updated to use new method names
- ✅ Build now completes successfully

---

## 🚀 HOW TO START THE BACKEND (3 Methods)

### ✅ METHOD 1: Using Command Prompt (RECOMMENDED)

1. **Open Command Prompt (CMD, not PowerShell)**
   - Press `Win + R`
   - Type: `cmd`
   - Press Enter

2. **Navigate to backend folder:**
   ```cmd
   cd D:\AndroidStudioProjects\Samvad\backend
   ```

3. **Start the application:**
   ```cmd
   mvn spring-boot:run
   ```

4. **Wait for:**
   ```
   Started SamvadBackendApplication in X.XXX seconds
   ```

5. **Server is running at:** http://localhost:8080

---

### ✅ METHOD 2: Using the Batch File

1. **Open File Explorer**

2. **Navigate to:**
   ```
   D:\AndroidStudioProjects\Samvad
   ```

3. **Double-click:** `start-backend.bat`

4. **A window will open and start the server**

---

### ✅ METHOD 3: Direct Java Execution

1. **First, build the project:**
   ```cmd
   cd D:\AndroidStudioProjects\Samvad\backend
   mvn clean package -DskipTests
   ```

2. **Run the JAR file:**
   ```cmd
   java -jar target\samvad-backend-1.0.0.jar
   ```

---

## ⚠️ IMPORTANT: Check Your Database Configuration

Your `application.properties` file shows:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Samvad
spring.datasource.username=postgres
spring.datasource.password=@Nitish@6250
```

### Make sure:
1. ✅ PostgreSQL is running
2. ✅ Database "Samvad" exists (or change to "samvad_db")
3. ✅ Username "postgres" has access
4. ✅ Password is correct

### To check if database exists:

**Option 1: Using pgAdmin**
- Open pgAdmin 4
- Connect to PostgreSQL
- Check if "Samvad" database exists in the left panel

**Option 2: Using Command Line**
```cmd
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -l
```
Enter password: `@Nitish@6250`

Look for "Samvad" in the list.

---

## 🔧 IF DATABASE DOESN'T EXIST

### Create the database:

```cmd
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres
```
Enter password: `@Nitish@6250`

Then type:
```sql
CREATE DATABASE "Samvad";
\q
```

---

## ✅ VERIFY BACKEND IS RUNNING

### Check 1: Look for this message in console:
```
Started SamvadBackendApplication in X.XXX seconds (JVM running for X.XXX)
```

### Check 2: Open browser and go to:
```
http://localhost:8080
```

You should see a 404 or Whitelabel Error page (this is normal - means server is running!)

### Check 3: Try an API endpoint:
```
http://localhost:8080/api/auth/send-otp
```

You should see an error about missing request body (this is expected - means API is working!)

---

## ❌ TROUBLESHOOTING COMMON ERRORS

### Error: "Cannot connect to database"

**Solution:**
1. Start PostgreSQL service:
   - Press `Win + R`
   - Type: `services.msc`
   - Find "postgresql-x64-16"
   - Right-click → Start

2. Verify database exists (see above)

3. Check credentials in `application.properties`

---

### Error: "Port 8080 already in use"

**Solution 1: Stop existing process**
```cmd
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

**Solution 2: Change port**
Edit `application.properties`:
```properties
server.port=8081
```

---

### Error: "Maven command not found"

**Solution:**
Make sure Maven is in PATH:
```cmd
mvn -version
```

If not found, add to PATH:
```
D:\Maven\apache-maven-3.9.11\bin
```

---

### Error: "JAVA_HOME not set"

**Solution:**
Set JAVA_HOME environment variable:
```cmd
setx JAVA_HOME "C:\Program Files\Java\jdk-22"
```

Restart command prompt.

---

## 📋 STARTUP CHECKLIST

Before starting backend:

- [ ] PostgreSQL service is running
- [ ] Database "Samvad" exists (or "samvad_db")
- [ ] User "postgres" can connect
- [ ] Maven is installed and in PATH
- [ ] Java 17+ is installed
- [ ] No other service using port 8080
- [ ] `application.properties` has correct credentials

---

## 🎯 COMPLETE STARTUP PROCEDURE

### Step 1: Start PostgreSQL
```cmd
# Check if running
services.msc
# Look for "postgresql-x64-16" - should be "Running"
```

### Step 2: Verify Database
```cmd
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -d Samvad
# Enter password: @Nitish@6250
# If you can connect, database exists!
\q
```

### Step 3: Start Backend
```cmd
cd D:\AndroidStudioProjects\Samvad\backend
mvn spring-boot:run
```

### Step 4: Wait for Success Message
```
Started SamvadBackendApplication
```

### Step 5: Test in Browser
```
http://localhost:8080
```

---

## 📊 WHAT YOU SHOULD SEE

### Successful Startup Log:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

... Samvad Backend ...
... Hikari CP ...
... HikariPool-1 - Start completed.
... Started SamvadBackendApplication in 5.123 seconds
```

### Look for:
- ✅ "HikariPool" - Database connection successful
- ✅ "Started SamvadBackendApplication" - Server is running
- ✅ "Tomcat started on port(s): 8080" - Web server ready
- ❌ NO "connection refused" errors
- ❌ NO "database does not exist" errors

---

## 🎉 SUCCESS INDICATORS

### ✅ Backend Started Successfully When:

1. **Console shows:**
   ```
   Started SamvadBackendApplication in X seconds
   ```

2. **Browser shows (http://localhost:8080):**
   ```
   Whitelabel Error Page
   This application has no explicit mapping for /error
   ```
   (This is GOOD - means server is running!)

3. **Database tables created:**
   - Open pgAdmin
   - Navigate to: Samvad → Schemas → public → Tables
   - You should see: `users`, `messages`, `otp_verification`

---

## 🚀 NEXT STEPS

After backend starts successfully:

1. ✅ Backend is running on port 8080
2. ▶️ Open Android Studio
3. ▶️ Sync Gradle files (IMPORTANT!)
4. ▶️ Run the Android app
5. ▶️ Test authentication with OTP

---

## 💡 QUICK COMMANDS REFERENCE

```cmd
# Navigate to backend
cd D:\AndroidStudioProjects\Samvad\backend

# Build project
mvn clean install -DskipTests

# Start application
mvn spring-boot:run

# Stop application
Ctrl + C

# Check if port 8080 is in use
netstat -ano | findstr :8080

# Check PostgreSQL service
services.msc

# Connect to database
"C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -d Samvad
```

---

## 📞 STILL HAVING ISSUES?

### Get detailed error information:

1. **Run with debug logging:**
   ```cmd
   mvn spring-boot:run -X
   ```

2. **Check the full error message** in the console

3. **Common issues:**
   - Database connection errors → Check PostgreSQL service and credentials
   - Port already in use → Change port or stop existing process
   - Build failures → Run `mvn clean install` first

---

## ✅ SUMMARY

**The compilation error is FIXED!**

The backend will now build and start successfully if:
1. PostgreSQL is running
2. Database "Samvad" exists
3. Credentials in `application.properties` are correct

**To start:**
```cmd
cd D:\AndroidStudioProjects\Samvad\backend
mvn spring-boot:run
```

**Look for:** `Started SamvadBackendApplication`

**That's it! The backend is ready!** 🎉

---

**Your backend is now working! The error was in the JWT code, which has been fixed!**

