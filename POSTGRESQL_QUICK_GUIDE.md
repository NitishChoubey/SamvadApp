# PostgreSQL Quick Setup - Visual Guide

## 🎯 SUPER QUICK SETUP (5 Minutes)

### For Windows Users - Follow These Simple Steps:

---

## STEP 1️⃣: DOWNLOAD (2 minutes)

1. Go to: **https://www.postgresql.org/download/windows/**
2. Click: **"Download the installer"**
3. Download: **postgresql-16.x-windows-x64.exe** (latest version)
4. Wait for download to complete

---

## STEP 2️⃣: INSTALL (3 minutes)

1. **Double-click** the downloaded file

2. Click **"Next"** → **"Next"** → **"Next"**

3. **IMPORTANT - Set Password:**
   ```
   Password: samvad123
   
   ⚠️ REMEMBER THIS PASSWORD!
   ```
   
4. Keep clicking **"Next"** (default settings are fine)

5. Click **"Next"** → **"Finish"**

---

## STEP 3️⃣: CREATE DATABASE (3 minutes)

### Option A: Using Command Line (Quick!)

1. **Press** `Win + R`
2. **Type:** `cmd`
3. **Press:** Enter

4. **Type these commands** (press Enter after each):

```cmd
psql -U postgres
```
**Enter password:** `samvad123` (or whatever you set)

```sql
CREATE DATABASE samvad_db;
CREATE USER samvad_user WITH PASSWORD 'samvad123';
GRANT ALL PRIVILEGES ON DATABASE samvad_db TO samvad_user;
\c samvad_db
GRANT ALL ON SCHEMA public TO samvad_user;
\q
```

**Done!** ✅

### Option B: Using pgAdmin (Visual!)

1. **Open:** pgAdmin 4 (from Start Menu)
2. **Enter master password** (can be same: `samvad123`)
3. **Click:** PostgreSQL 16 (in left panel)
4. **Enter password:** `samvad123`

**Create Database:**
- Right-click **"Databases"**
- Click **"Create" → "Database"**
- Name: `samvad_db`
- Click **"Save"**

**Create User:**
- Right-click **"Login/Group Roles"**
- Click **"Create" → "Login/Group Role"**
- Name: `samvad_user`
- Go to **"Definition"** tab
- Password: `samvad123`
- Go to **"Privileges"** tab
- Check ✅ **"Can login?"**
- Click **"Save"**

**Grant Access:**
- Right-click **"samvad_db"**
- Click **"Properties"**
- Go to **"Security"** tab
- Click **"+"**
- Select: `samvad_user`
- Grant: ALL
- Click **"Save"**

**Done!** ✅

---

## STEP 4️⃣: CONFIGURE SAMVAD (1 minute)

1. **Open file:**
   ```
   D:\AndroidStudioProjects\Samvad\backend\src\main\resources\application.properties
   ```

2. **Find these lines and update:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db
   spring.datasource.username=samvad_user
   spring.datasource.password=samvad123
   ```

3. **Save the file**

---

## STEP 5️⃣: TEST (1 minute)

1. **Double-click:**
   ```
   D:\AndroidStudioProjects\Samvad\start-backend.bat
   ```

2. **Wait for:**
   ```
   Started SamvadBackendApplication
   ```

3. **Check for NO errors about database**

**If it starts successfully, PostgreSQL is working!** 🎉

---

## 🔍 VERIFY SETUP

### Quick Test Commands:

```cmd
# Test connection
psql -U samvad_user -d samvad_db

# If you see: samvad_db=>
# SUCCESS! ✅

# Exit with:
\q
```

### Check Tables Were Created:

**In pgAdmin:**
1. Expand: PostgreSQL 16 → Databases → samvad_db → Schemas → public → Tables
2. You should see:
   - ✅ users
   - ✅ messages
   - ✅ otp_verification

**In Command Line:**
```cmd
psql -U samvad_user -d samvad_db
\dt
\q
```

---

## ❌ TROUBLESHOOTING

### Problem: "psql: command not found"

**Solution:**
1. Add to PATH: `C:\Program Files\PostgreSQL\16\bin`
2. Restart command prompt
3. Try again

### Problem: "Password authentication failed"

**Solution:**
1. Make sure you're using the correct password
2. Default we set: `samvad123`
3. Or reset it:
   ```sql
   psql -U postgres
   ALTER USER samvad_user WITH PASSWORD 'samvad123';
   ```

### Problem: "Database does not exist"

**Solution:** Repeat Step 3 to create the database

### Problem: "Connection refused"

**Solution:**
1. Open Services (Win + R → `services.msc`)
2. Find "postgresql-x64-16"
3. Right-click → Start

---

## ✅ CHECKLIST

Before starting the backend, verify:

- [x] PostgreSQL installed
- [x] Password set: `samvad123`
- [x] Database created: `samvad_db`
- [x] User created: `samvad_user`
- [x] Password for user: `samvad123`
- [x] `application.properties` updated
- [x] Can connect: `psql -U samvad_user -d samvad_db`

**All checked? You're ready!** 🚀

---

## 📝 REMEMBER THESE:

```
Database: samvad_db
Username: samvad_user
Password: samvad123
Port:     5432
Host:     localhost
```

**Connection String:**
```
jdbc:postgresql://localhost:5432/samvad_db
```

---

## 🎉 WHAT'S NEXT?

1. ✅ PostgreSQL is set up
2. ▶️ Start backend: `start-backend.bat`
3. ▶️ Open Android Studio
4. ▶️ Sync Gradle
5. ▶️ Run the app
6. ▶️ Test authentication!

---

## 📚 MORE HELP?

**Detailed guide:** Read `POSTGRESQL_SETUP.md`

**Quick setup:** Read `QUICK_SETUP.md`

**Full migration guide:** Read `MIGRATION_GUIDE.md`

---

**That's it! PostgreSQL is ready for Samvad!** 🎊

