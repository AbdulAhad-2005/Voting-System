# SQL code for the voting system project database
# this code is for MySQL server, check if it works on other sql servers.

DROP DATABASE IF EXISTS Voting_System;
CREATE DATABASE Voting_System;
Use Voting_System;

CREATE TABLE Election_Type (
ElectionId INT PRIMARY KEY auto_increment,
Title VARCHAR(30),
Date DATE,
time VARCHAR(20),
VoterRequirements VARCHAR(100),
CandidateRequirements VARCHAR(100)
);
CREATE TABLE Voter (
VoterId INT PRIMARY KEY auto_increment,
CNIC BIGINT UNIQUE,
Name VARCHAR(50),
FatherName VARCHAR(50),
Age INT,
Gender VARCHAR(11),
ElectionId INT,
    FOREIGN KEY (ElectionId) REFERENCES Election_Type(ElectionId)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);
CREATE TABLE Region (
regionId int primary key,
VotingRegion varchar(25),
ElectionId int,
FOREIGN KEY (ElectionId) REFERENCES election_type(ElectionId)
on update restrict
on delete restrict
);
CREATE TABLE Voting_Address (
VoterId INT auto_increment,
PollingBoothNo INT,
RegionId int,
BoothName VARCHAR (100),
City VARCHAR(15),
Province VARCHAR(30),
FOREIGN KEY (VoterId) REFERENCES Voter(VoterId)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (RegionId) REFERENCES Region(regionId)
on update cascade
on delete cascade
);
CREATE TABLE Party (
PartyId INT PRIMARY KEY auto_increment,
Name VARCHAR(50),
Abbreviation VARCHAR(5),
Symbol LONGBLOB,
Leader VARCHAR(30)
);
CREATE TABLE Candidate (
CandidateId INT PRIMARY KEY auto_increment,
Name VARCHAR(30),
PartyId INT,
ElectionId INT,
RegionId int,
FOREIGN KEY (PartyId) REFERENCES Party(PartyId)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (ElectionId) REFERENCES Election_Type(ElectionId)
ON UPDATE CASCADE
ON DELETE CASCADE,
FOREIGN KEY (RegionId) REFERENCES Region(regionId)
on update cascade
on delete cascade
);
CREATE TABLE Vote (
    VoterId INT primary key,
    CandidateId INT,
    PartyId int,
    ElectionId INT,
    FOREIGN KEY (VoterId) REFERENCES Voter(VoterId)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (CandidateId) REFERENCES Candidate(CandidateId)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
	FOREIGN KEY (PartyId) REFERENCES Party(PartyId)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY (ElectionId) REFERENCES Election_Type(ElectionId)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

