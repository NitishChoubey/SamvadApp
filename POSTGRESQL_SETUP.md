# PostgreSQL Setup Guide for Samvad

## 📋 Complete Step-by-Step PostgreSQL Installation & Configuration

This guide will help you install PostgreSQL and set it up for the Samvad backend.

---

## 🪟 Windows Installation

### Step 1: Download PostgreSQL

1. Go to: https://www.postgresql.org/download/windows/
2. Click on "Download the installer"
3. This takes you to EnterpriseDB download page
4. Download the latest version (PostgreSQL 16.x recommended)
5. Choose Windows x86-64 version
6. File will be named something like: `postgresql-16.x-windows-x64.exe`

### Step 2: Install PostgreSQL

1. **Run the installer** (double-click the downloaded .exe file)

2. **Installation wizard opens:**
   - Click "Next"

3. **Installation Directory:**
   - Default: `C:\Program Files\PostgreSQL\16`
   - Click "Next" (or change if needed)

4. **Select Components:**
   - ✅ PostgreSQL Server (required)
   - ✅ pgAdmin 4 (GUI tool - recommended)
   - ✅ Stack Builder (optional)
   - ✅ Command Line Tools (required)
   - Click "Next"

5. **Data Directory:**
   - Default: `C:\Program Files\PostgreSQL\16\data`
   - Click "Next"

6. **Set Password:**
   - Enter a password for the database superuser (postgres)
   - **REMEMBER THIS PASSWORD!** You'll need it later
   - Example: `postgres123` (use a stronger password in production)
   - Re-enter password to confirm
   - Click "Next"

7. **Port:**
   - Default: `5432`
   - Keep this unless you have a conflict
   - Click "Next"

8. **Locale:**
   - Default locale is fine
   - Click "Next"

9. **Ready to Install:**
   - Review settings
   - Click "Next" to begin installation
   - Wait for installation to complete (2-5 minutes)

10. **Completing Setup:**
    - Uncheck "Launch Stack Builder at exit" (not needed now)
    - Click "Finish"

### Step 3: Verify Installation

1. **Open Command Prompt** (Win + R, type `cmd`, press Enter)

2. **Check PostgreSQL version:**
   ```cmd
   psql --version
   ```
   
   If you see: `psql (PostgreSQL) 16.x`, installation is successful!
   
   **If command not found:**
   - Add to PATH: `C:\Program Files\PostgreSQL\16\bin`
   - Restart command prompt

### Step 4: Create Database for Samvad

#### Option A: Using Command Line (psql)

1. **Open Command Prompt or PowerShell**

2. **Connect to PostgreSQL:**
   ```cmd
   psql -U postgres
   ```
   
3. **Enter password** (the one you set during installation)

4. **You should see:** `postgres=#`

5. **Create database:**
   ```sql
   CREATE DATABASE samvad_db;
   ```
   You should see: `CREATE DATABASE`

6. **Create user:**
   ```sql
   CREATE USER samvad_user WITH PASSWORD 'samvad123';
   ```
   You should see: `CREATE ROLE`

7. **Grant privileges:**
   ```sql
   GRANT ALL PRIVILEGES ON DATABASE samvad_db TO samvad_user;
   ```
   You should see: `GRANT`

8. **Grant schema privileges:**
   ```sql
   \c samvad_db
   GRANT ALL ON SCHEMA public TO samvad_user;
   ```

9. **Exit psql:**
   ```sql
   \q
   ```

10. **Test connection with new user:**
    ```cmd
    psql -U samvad_user -d samvad_db
    ```
    Enter password: `samvad123`
    
    If you see `samvad_db=>`, success! Type `\q` to exit.

#### Option B: Using pgAdmin 4 (GUI)

1. **Open pgAdmin 4** from Start Menu
   - Search for "pgAdmin 4"

2. **First time setup:**
   - Set a master password for pgAdmin (can be same as postgres password)
   - Click "OK"

3. **Connect to PostgreSQL Server:**
   - In left panel, expand "Servers"
   - Click on "PostgreSQL 16"
   - Enter the postgres password you set during installation
   - Check "Save password" if you want
   - Click "OK"

4. **Create Database:**
   - Right-click on "Databases"
   - Select "Create" → "Database..."
   - Database name: `samvad_db`
   - Owner: `postgres` (for now)
   - Click "Save"

5. **Create User:**
   - Expand "PostgreSQL 16"
   - Right-click on "Login/Group Roles"
   - Select "Create" → "Login/Group Role..."
   - **General tab:**
     - Name: `samvad_user`
   - **Definition tab:**
     - Password: `samvad123`
     - Confirm password: `samvad123`
   - **Privileges tab:**
     - ✅ Can login?
     - ✅ Create databases? (optional)
   - Click "Save"

6. **Grant Privileges:**
   - Right-click on "samvad_db"
   - Select "Properties"
   - Go to "Security" tab
   - Click "+" to add
   - Select "samvad_user"
   - Grant all privileges
   - Click "Save"

### Step 5: Configure Samvad Backend

1. **Open your project:** `D:\AndroidStudioProjects\Samvad\backend`

2. **Edit `application.properties`:**
   - Location: `src\main\resources\application.properties`

3. **Update database configuration:**
   ```properties
   # PostgreSQL Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db
   spring.datasource.username=samvad_user
   spring.datasource.password=samvad123
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   # JPA/Hibernate Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.properties.hibernate.format_sql=true
   ```

4. **Save the file**

### Step 6: Test Connection

1. **Open Command Prompt** in your Samvad folder

2. **Navigate to backend:**
   ```cmd
   cd D:\AndroidStudioProjects\Samvad\backend
   ```

3. **Start the backend:**
   ```cmd
   mvn spring-boot:run
   ```
   
   OR double-click: `start-backend.bat` in the root folder

4. **Check console output:**
   - Look for: "Hikari CP" connection pool messages
   - Look for: "Started SamvadBackendApplication"
   - No errors about database connection
   
5. **Check database tables created:**
   - Open pgAdmin 4
   - Navigate to: PostgreSQL 16 → Databases → samvad_db → Schemas → public → Tables
   - You should see:
     - `users`
     - `messages`
     - `otp_verification`

**If you see these tables, PostgreSQL is successfully configured!** ✅

---

## 🐧 Linux Installation (Ubuntu/Debian)

### Step 1: Install PostgreSQL

1. **Update package list:**
   ```bash
   sudo apt update
   ```

2. **Install PostgreSQL:**
   ```bash
   sudo apt install postgresql postgresql-contrib
   ```

3. **Start PostgreSQL service:**
   ```bash
   sudo systemctl start postgresql
   sudo systemctl enable postgresql
   ```

4. **Verify installation:**
   ```bash
   psql --version
   ```

### Step 2: Create Database

1. **Switch to postgres user:**
   ```bash
   sudo -i -u postgres
   ```

2. **Open PostgreSQL prompt:**
   ```bash
   psql
   ```

3. **Create database:**
   ```sql
   CREATE DATABASE samvad_db;
   ```

4. **Create user:**
   ```sql
   CREATE USER samvad_user WITH PASSWORD 'samvad123';
   ```

5. **Grant privileges:**
   ```sql
   GRANT ALL PRIVILEGES ON DATABASE samvad_db TO samvad_user;
   ```

6. **Connect to database and grant schema access:**
   ```sql
   \c samvad_db
   GRANT ALL ON SCHEMA public TO samvad_user;
   ```

7. **Exit:**
   ```sql
   \q
   exit
   ```

8. **Test connection:**
   ```bash
   psql -U samvad_user -d samvad_db -h localhost
   ```

### Step 3: Configure Backend

Same as Windows Step 5 above.

---

## 🍎 macOS Installation

### Step 1: Install PostgreSQL

#### Option A: Using Homebrew (Recommended)

1. **Install Homebrew** (if not installed):
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```

2. **Install PostgreSQL:**
   ```bash
   brew install postgresql@16
   ```

3. **Start PostgreSQL:**
   ```bash
   brew services start postgresql@16
   ```

#### Option B: Using Postgres.app

1. Download from: https://postgresapp.com/
2. Drag to Applications
3. Open Postgres.app
4. Click "Initialize" on the default server

### Step 2: Create Database

Same as Linux steps above.

### Step 3: Configure Backend

Same as Windows Step 5 above.

---

## 🔧 Common Issues & Solutions

### Issue 1: "psql: command not found"

**Windows:**
```cmd
# Add to PATH:
# 1. Open System Properties → Environment Variables
# 2. Edit "Path" variable
# 3. Add: C:\Program Files\PostgreSQL\16\bin
# 4. Restart command prompt
```

**Linux/Mac:**
```bash
# Usually auto-added, if not:
export PATH=/usr/lib/postgresql/16/bin:$PATH
# Add to ~/.bashrc or ~/.zshrc
```

### Issue 2: "Connection refused"

**Check if PostgreSQL is running:**
```cmd
# Windows - Check Services
services.msc
# Look for "postgresql-x64-16"

# Linux
sudo systemctl status postgresql

# macOS
brew services list
```

**Start the service:**
```cmd
# Windows - In Services, right-click → Start

# Linux
sudo systemctl start postgresql

# macOS
brew services start postgresql@16
```

### Issue 3: "password authentication failed"

**Reset password:**
```bash
# Connect as postgres superuser
psql -U postgres

# Change password
ALTER USER samvad_user WITH PASSWORD 'newpassword';
```

**Update application.properties** with new password.

### Issue 4: "database does not exist"

**Create the database:**
```sql
psql -U postgres
CREATE DATABASE samvad_db;
\q
```

### Issue 5: "permission denied for schema public"

**Grant schema access:**
```sql
psql -U postgres -d samvad_db
GRANT ALL ON SCHEMA public TO samvad_user;
\q
```

### Issue 6: Port 5432 already in use

**Change port in PostgreSQL:**
1. Find `postgresql.conf` file
2. Change `port = 5432` to `port = 5433`
3. Restart PostgreSQL
4. Update `application.properties`: `jdbc:postgresql://localhost:5433/samvad_db`

---

## ✅ Verification Checklist

After setup, verify these:

- [ ] PostgreSQL service is running
- [ ] Can connect with: `psql -U postgres`
- [ ] Database `samvad_db` exists
- [ ] User `samvad_user` exists
- [ ] Can connect with: `psql -U samvad_user -d samvad_db`
- [ ] `application.properties` has correct credentials
- [ ] Backend starts without database errors
- [ ] Tables (users, messages, otp_verification) are created automatically

---

## 📊 Quick Reference

### Connection Details:
```
Host: localhost
Port: 5432
Database: samvad_db
Username: samvad_user
Password: samvad123  (change this!)
```

### Useful PostgreSQL Commands:
```sql
\l                  -- List all databases
\c samvad_db        -- Connect to database
\dt                 -- List tables in current database
\du                 -- List users
\q                  -- Quit psql

-- Show table structure
\d users
\d messages
\d otp_verification

-- Check data
SELECT * FROM users;
SELECT * FROM messages;
SELECT * FROM otp_verification;
```

### Connection String for application.properties:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db
spring.datasource.username=samvad_user
spring.datasource.password=samvad123
```

---

## 🔐 Security Best Practices

### For Development:
- Current setup is fine
- Simple passwords are OK
- localhost connection only

### For Production:
1. **Use strong passwords:**
   ```sql
   ALTER USER samvad_user WITH PASSWORD 'Str0ng_P@ssw0rd_H3r3!';
   ```

2. **Enable SSL:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db?ssl=true
   ```

3. **Restrict connections:**
   Edit `pg_hba.conf` to allow only specific IPs

4. **Regular backups:**
   ```bash
   pg_dump -U samvad_user samvad_db > backup.sql
   ```

5. **Use environment variables:**
   Don't hardcode passwords in `application.properties`

---

## 🎯 Next Steps

After PostgreSQL is set up:

1. ✅ PostgreSQL is installed
2. ✅ Database `samvad_db` is created
3. ✅ User `samvad_user` is created
4. ✅ `application.properties` is configured
5. ▶️ **Start the backend:** Run `start-backend.bat`
6. ▶️ **Verify tables created:** Check pgAdmin or use `\dt` command
7. ▶️ **Test API:** Run Android app and try authentication

---

## 📞 Need Help?

### Check PostgreSQL logs:
```
Windows: C:\Program Files\PostgreSQL\16\data\log\
Linux: /var/log/postgresql/
macOS: ~/Library/Application Support/Postgres/var-16/
```

### Test connection:
```bash
# Should succeed if everything is configured correctly
psql -U samvad_user -d samvad_db -h localhost -W
```

### Check if service is running:
```bash
# Windows
services.msc  # Look for postgresql-x64-16

# Linux
sudo systemctl status postgresql

# macOS
brew services list | grep postgresql
```

---

## 🎉 Success!

If you can:
1. ✅ Connect to PostgreSQL
2. ✅ See the `samvad_db` database
3. ✅ Connect with `samvad_user`
4. ✅ Start backend without errors
5. ✅ See tables created automatically

**Then PostgreSQL is successfully set up for Samvad!** 🎊

**Now you can start the backend and test your app!**

---

**Remember:**
- Default password: `samvad123`
- Default port: `5432`
- Database name: `samvad_db`
- Username: `samvad_user`

**Change the password in production!**

