CREATE DATABASE IF NOT EXISTS College;
USE College;

CREATE TABLE IF NOT EXISTS Departments (
depName VARCHAR(20), 
campus VARCHAR(20),
PRIMARY KEY(depName)
);

CREATE TABLE IF NOT EXISTS Students (
first_name VARCHAR(20), 
last_name VARCHAR(20), 
id char(9),
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Classes (
className VARCHAR(50),
credits char(1), 
PRIMARY KEY (className)
);

CREATE TABLE IF NOT EXISTS Majors (
sid char(9),
dname VARCHAR(20), 
FOREIGN KEY (sid) REFERENCES Students (id),
FOREIGN KEY (dname) REFERENCES Departments (depName)
);

CREATE TABLE IF NOT EXISTS Minors (
sid char(9),
dname VARCHAR(20), 
FOREIGN KEY (sid) REFERENCES Students (id),
FOREIGN KEY (dname) REFERENCES Departments (depName)
);

CREATE TABLE IF NOT EXISTS IsTaking (
sid char(9),
cname VARCHAR(50),
FOREIGN KEY (sid) REFERENCES Students (id),
FOREIGN KEY (cname) REFERENCES Classes (className)
);

CREATE TABLE IF NOT EXISTS HasTaken (
sid char(9),
cname VARCHAR(50),
grade char(1),
FOREIGN KEY (sid) REFERENCES Students (id),
FOREIGN KEY (cname) REFERENCES Classes (className)
);
