# Employment & Engagement Management System (EEMS)

A **fully modular Java 17 project** using **JDBC + Streams**, connected to **MySQL on Hostinger**.
---

## 🧩 Overview
EEMS (Employment & Engagement Management System) demonstrates solid OOP design, multi-layered architecture, functional programming with streams, and clean data access through the DAO pattern. It’s runnable directly from `App.java` — **no Postman needed**.

---

## 🏗️ Architecture
```
Domain → Repository (DAO-backed) → Service → CLI (App)
```
Each layer is isolated for modularity and testability.

| Layer | Description |
|--------|--------------|
| **Domain** | Immutable records for `Department`, `Employee`, `Project`, `Client`, etc. |
| **Repository** | CRUD interfaces and JDBC implementations for MySQL. |
| **Service Layer** | Functional operations using Java Streams. |
| **CLI (App)** | Entry point to run and demonstrate all required tasks. |

---

## 📦 Domain Model & Multiplicities

- Department 1 — employs — * Employee  
- Department 1 — hosts — * Project  
- Employee * — works on — * Project (`EmployeeProject` association with allocation%)  
- Client * — sponsors — * Project (`ClientProject` association)

**Enum:** `ProjectStatus { PLANNED, ACTIVE, ON_HOLD, COMPLETED, CANCELLED }`

---

## ⚙️ Technology Stack
- **Language:** Java 17  
- **Database:** MySQL (Hostinger)  
- **Build Tool:** Maven or manual `javac`  
- **JDBC Driver:** `mysql-connector-j-8.3.0.jar`

---

## 📂 Project Structure
```
src/edu/miu/eems/
├── db/                 # Database connection + schema
│   ├── DB.java
│   └── Schema-mysql.sql
├── domain/             # Domain entities (records)
├── repo/               # Repository interfaces
│   └── jdbc/           # JDBC implementations
├── service/            # Business logic (streams + transactions)
└── App.java        # CLI entry point
```

---

## 🗃️ Database Setup (phpMyAdmin / Hostinger)

1. Log into **phpMyAdmin** on your Hostinger panel.
2. Create a new database (e.g., `eems_db`).
3. Open the **SQL** tab.
4. Paste and execute the contents of `Schema-mysql.sql`.

This will create all tables (`department`, `employee`, `project`, `client`, `employee_project`, `client_project`) and seed data.

---

## 🔐 Database Configuration
Edit the file `src/edu/miu/eems/db/DB.java`:
```java
private static final String DB_URL  = "jdbc:mysql://<HOST>:3306/<DBNAME>?useSSL=false&serverTimezone=UTC";
private static final String DB_USER = "<USERNAME>";
private static final String DB_PASS = "<PASSWORD>";
```
Alternatively, use config file (resources/config.properties):
```
DB_URL=jdbc:mysql://<HOST>:3306/<DBNAME>?useSSL=false&serverTimezone=UTC
DB_USER=<USERNAME>
DB_PASS=<PASSWORD>
```

---

## 🧱 Build & Run

### Option 1: Without Maven
```bash
# Place MySQL driver JAR under lib/
javac -cp .:lib/mysql-connector-j-8.3.0.jar $(find src -name "*.java")
java  -cp .:lib/mysql-connector-j-8.3.0.jar edu.miu.eems.App
```

### Option 2: With Maven
```xml
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.3.0</version>
</dependency>
```
Then run:
```bash
mvn compile exec:java -Dexec.mainClass=edu.miu.eems.App
```

---

## 🧪 Demo Tasks Implemented
1. **calculateProjectHRCost(int projectId)** → Aggregates salary × months × allocation% via streams.
2. **getProjectsByDepartment(int deptId, String sortBy)** → Filters and sorts active projects.
3. **findClientsByUpcomingProjectDeadline(int days)** → Joins client–project tables, filters by end date.
4. **transferEmployeeToDepartment(int empId, int newDeptId)** → Transactional update across tables.

All these are accessible through `App` for direct demonstration.

---

## 🧠 Key Queries Professor Will Review
- `SELECT * FROM project WHERE dept_id=? AND status='ACTIVE' ORDER BY ...`
- `SELECT DISTINCT c.* FROM client c JOIN client_project cp ON c.id=cp.client_id JOIN project p ON p.id=cp.project_id WHERE p.end_date <= DATE_ADD(CURDATE(), INTERVAL ? DAY)`
- `UPDATE employee SET dept_id=? WHERE id=?` (transactional)

---

## 🧩 Implementation Highlights
- **Modular**: Each layer has a clear responsibility.
- **Functional**: Business logic leverages Java Streams.
- **Transactional**: Multi-step operations wrapped with `DataAccessSystem.inTransaction()`.
- **Reusable**: DAO classes can be tested independently.
- **Portable**: Works with any MySQL server by changing credentials.

---

## 🧰 Troubleshooting
| Issue | Cause / Fix |
|--------|--------------|
| Cannot connect to DB | Check Hostinger credentials or allow remote access. |
| SQL syntax error | Ensure you used the MySQL schema (not SQLite). |
| Driver not found | Add `mysql-connector-j` JAR to classpath or Maven dependency. |
| Timezone/SSL warnings | Append `?useSSL=false&serverTimezone=UTC` to JDBC URL. |

---

## 🧭 Submission Notes
✅ Professor can run `App` directly to verify:  
- Database connectivity  
- CRUD and business tasks  
- Proper stream usage  
- DAO-based modular design  
- Queries visible and maintainable

---

## 📜 License
For academic and educational use. Attribution required for redistribution.

---
