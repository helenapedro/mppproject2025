SET NAMES utf8mb4;
SET time_zone = '+00:00';

CREATE TABLE IF NOT EXISTS department (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(100) NOT NULL UNIQUE,
  location  VARCHAR(100) NOT NULL,
  budget    DECIMAL(12,2) NOT NULL CHECK (budget >= 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS employee (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(120) NOT NULL,
  title      VARCHAR(120) NOT NULL,
  hire_date  DATE NOT NULL,
  salary     DECIMAL(12,2) NOT NULL,
  dept_id    INT NOT NULL,
  CONSTRAINT fk_emp_dept FOREIGN KEY(dept_id) REFERENCES department(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE INDEX idx_employee_dept ON employee(dept_id);

-- Use ENUM for simpler constraint enforcement
CREATE TABLE IF NOT EXISTS project (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(150) NOT NULL,
  description TEXT,
  start_date  DATE NOT NULL,
  end_date    DATE NULL,
  budget      DECIMAL(12,2) NOT NULL,
  status      ENUM('PLANNED','ACTIVE','ON_HOLD','COMPLETED','CANCELLED') NOT NULL,
  dept_id     INT NOT NULL,
  CONSTRAINT fk_proj_dept FOREIGN KEY(dept_id) REFERENCES department(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE INDEX idx_project_dept ON project(dept_id);
CREATE INDEX idx_project_status ON project(status);
CREATE INDEX idx_project_enddate ON project(end_date);

CREATE TABLE IF NOT EXISTS client (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  name     VARCHAR(150) NOT NULL,
  industry VARCHAR(120),
  email    VARCHAR(150),
  phone    VARCHAR(50)
) ENGINE=InnoDB;

-- Association classes
CREATE TABLE IF NOT EXISTS employee_project (
  employee_id   INT NOT NULL,
  project_id    INT NOT NULL,
  allocation_pct DECIMAL(5,2) NOT NULL CHECK (allocation_pct >= 0 AND allocation_pct <= 100),
  PRIMARY KEY(employee_id, project_id),
  CONSTRAINT fk_ep_emp FOREIGN KEY(employee_id) REFERENCES employee(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_ep_prj FOREIGN KEY(project_id) REFERENCES project(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;
CREATE INDEX idx_ep_proj ON employee_project(project_id);

CREATE TABLE IF NOT EXISTS client_project (
  client_id  INT NOT NULL,
  project_id INT NOT NULL,
  PRIMARY KEY(client_id, project_id),
  CONSTRAINT fk_cp_client FOREIGN KEY(client_id) REFERENCES client(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_cp_proj FOREIGN KEY(project_id) REFERENCES project(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Seed data
INSERT INTO department(id, name, location, budget) VALUES
  (1,'Engineering','HQ',1500000.00),
  (2,'Operations','HQ', 900000.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), location=VALUES(location), budget=VALUES(budget);

INSERT INTO employee(id,name,title,hire_date,salary,dept_id) VALUES
  (1,'Alice Johnson','Senior Engineer','2023-06-01',120000.00,1),
  (2,'Bob Smith','Engineer','2024-02-15', 90000.00,1),
  (3,'Carol Davis','Ops Analyst','2022-11-20', 80000.00,2)
ON DUPLICATE KEY UPDATE name=VALUES(name), title=VALUES(title), hire_date=VALUES(hire_date), salary=VALUES(salary), dept_id=VALUES(dept_id);

INSERT INTO project(id,name,description,start_date,end_date,budget,status,dept_id) VALUES
  (10,'Phoenix','Core platform upgrade','2025-01-01','2025-12-31',700000.00,'ACTIVE',1),
  (11,'Mercury','Observability rollout','2025-03-01','2025-09-30',250000.00,'ACTIVE',1),
  (12,'Atlas','Warehouse automation','2025-02-01','2026-01-31',500000.00,'PLANNED',2)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), start_date=VALUES(start_date), end_date=VALUES(end_date), budget=VALUES(budget), status=VALUES(status), dept_id=VALUES(dept_id);

INSERT INTO client(id,name,industry,email,phone) VALUES
  (100,'Globex','Finance','cto@globex.com','+1-555-1000'),
  (101,'Initech','SaaS','it@initech.com','+1-555-1010')
ON DUPLICATE KEY UPDATE name=VALUES(name), industry=VALUES(industry), email=VALUES(email), phone=VALUES(phone);

INSERT INTO client_project(client_id, project_id) VALUES
  (100,10),(101,11),(101,12)
ON DUPLICATE KEY UPDATE client_id=VALUES(client_id), project_id=VALUES(project_id);

INSERT INTO employee_project(employee_id, project_id, allocation_pct) VALUES
  (1,10,60.00),(2,10,40.00),(1,11,30.00),(3,12,50.00)
ON DUPLICATE KEY UPDATE allocation_pct=VALUES(allocation_pct);