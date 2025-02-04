# Voting System

A **Java-based Voting System** utilizing **Swing for GUI** and **MySQL Workbench for database management**.  
This project was developed as a **semester project**, inspired by the democratic system of Pakistan.

## 🛠 Features

- 🔐 **User Authentication**: Login for voters.
- 🏛 **Party Management**: Add and manage political parties and their symbols.
- 🏅 **Candidate Management**: Add and manage candidates associated with parties.
- 🗳 **Voting Process**: Cast votes and store them in the database.
- 📊 **Result Compilation**: Calculate and display election results based on votes.

## 📂 Project Structure

The project is organized into the following directories:

- **Voting-System Database**: Contains the SQL scripts for setting up the database.
- **VotingSystem**: Contains the Java source code for the application.

### Voting-System Database

- `Database schema.sql`: SQL script to create the necessary tables for the `Voting-System` database.
- `Sample data.sql`: SQL script to insert sample data into the `Voting-System` database.

### Voting System (Java Code)
#### Authentication & User Management
- **AuthenticationGui.java** - Handles user authentication, login, and validation.
- **VoterCredentials.java** - Checks voter login credentials from database.

#### Election & Voting Process
- **GiveVote.java** - Allows a voter to cast a vote for a selected candidate.
- **EndElection.java** - Ends the election process and prevents further voting.
- **ElectionTypeSelection.java** - Allows selection of the type of election (e.g., Federal, Provincial or Senate).

#### Candidate & Party Management
- **AddCandidate.java** - Enables the admin to add candidates to the election.
- **RemoveCandidate.java** - Allows the admin to remove a candidate from the election.
- **AddParty.java** - Enables the admin to register new political parties.
- **RemoveParty.java** - Allows the admin to delete a registered political party.
- **AddingPartyImages.java** - Uploads party symbols to the database of the initial parties which are already included in sample data of database and have not been added through application gui.

#### Voter Management
- **AddVoter.java** - Registers a new voter to the system.
- **RemoveVoter.java** - Removes an existing voter from the system.

#### Others
- **GUI.java** - Acts as the main window in which there are buttons to start election or to add election credentials.
- **VotingInterface.java** - Provides the voting interface where users can select candidates.
- **ElectionResultWindow.java** - Displays the election results after voting ends.

## Setup Instructions
1. **Clone the Repository**
   ```sh
   git clone https://github.com/AbdulAhad-2005/Voting-System.git
   cd Voting-System

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
1. **Upload Background Images**:
   - Upload the path to beautiful background images that match to the template of the tab in almost every file at the
   ```backgroundImage = ImageIO.read(new File("Add path to the background image."));```
   line of code at the place of "Add path to the background image."
   
2. **Upload Party Images**:
   - Run the `addPartyImages.java` file to store party symbols in the database.

3. **Launch Voting System**:
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
