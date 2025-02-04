# Voting System

A **Java-based Voting System** utilizing **Swing for GUI** and **MySQL Workbench for database management**.  
This project was developed as a **semester project**, inspired by the democratic system of Pakistan.

## 🛠 Features

- 🔐 **User Authentication**: Login for voters.
- 🏛 **Party Management**: Add and manage political parties and their symbols.
- 🏅 **Candidate Management**: Add and manage candidates associated with parties.
- 🗳 **Voting Process**: Cast votes and store them in the database.
- 📊 **Result Compilation**: Calculate and display election results based on votes.

## 📌 Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK)**
- **Eclipse IDE** (or any Java IDE)
- **MySQL Server** (for database management)
- **JDBC Driver** (for Java-MySQL connectivity)

## ⚙️ Setup Instructions

### 🏛 Database Setup

1. Create a MySQL database named **`Voting-System`**.
2. Execute the provided SQL script (`Database schema.sql`) to set up tables and design of database.
3. Also execute the provided SQL script (`Sample data.sql`) to insert sample data(optional: you can use your own sample data).

### 🔗 JDBC Driver Configuration

1. Download the **JDBC Driver for MySQL**.
2. In **Eclipse IDE**:
   - Right-click on your project → **Build Path** → **Configure Build Path**.
   - Under **Libraries**, click **Add External JARs** and select the downloaded **JDBC driver**.
   - Apply and Close.

### 🚀 Running the Application

1. **Upload Party Images**:
   - Run the `addPartyImages.java` file to store party symbols in the database.

2. **Launch Voting System**:
   - Run `authenticateGUI.java` to start the application.

## 📖 Usage Guide

1. **Login**:
   - Users must log in through the authentication interface.

2. **Cast Vote**:
   - After authentication, users can vote for their preferred candidates.

3. **Admin Features**:
   - Admins can add or manage parties and candidates through the provided UI.

## ⚠️ Important Notes

- This project is a **basic implementation** and **lacks any types of security measures**.
- It is intended **for educational purposes only**.
- Not suitable for real-world elections **without significant improvements**.

---

💡 **Feel free to contribute, modify, or enhance this project!**  
📩 **For any queries, reach out via GitHub Issues.**  

---
