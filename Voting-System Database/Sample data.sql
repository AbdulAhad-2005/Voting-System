# this is just a sample data to conduct elections in Pakistan, if you wanna conduct real elections, replace this data with actual data if that time.

-- Inserting Election Types
INSERT INTO Election_Type (ElectionId, Title, Date, time, VoterRequirements, CandidateRequirements)
VALUES 
    (1, 'National Assembly Election', '2024-07-25', '08:00 AM - 05:00 PM', 'CNIC', 'Party affiliation'),
    (2, 'Provincial Assembly Election', '2024-07-25', '08:00 AM - 05:00 PM', 'CNIC', 'Party affiliation'),
    (3, 'Senate Election', '2024-07-25', '08:00 AM - 05:00 PM', 'Senate', 'Party affiliation');    
    -- inserting voters
INSERT INTO Voter (VoterId, CNIC, Name, FatherName, Age, Gender, ElectionId)
VALUES 
    (1, 1234567890123, 'Abdul Ahad', 'Akram Anwar', 35, 'Male', 1),
    (2, 2345678901234, 'Abdullah Saleh', 'Abdul Jabbar', 28, 'Female', 1),
    (3, 3456789012345, 'Mudassir Ahmad', 'Mahmood Ahmed', 45, 'Male', 1),
    (4, 4567890123456, 'Ali Khan', 'Abdullah Khan', 30, 'Female', 1),
    (5, 5678901234567, 'Mohammad Ali', 'Ahmed Khan', 40, 'Male', 1),
    (6, 6789012345678, 'Sana Khan', 'Irfan Khan', 25, 'Female', 1),
    (7, 7890123456789, 'Abdul Basit', 'Saeed Ahmed', 50, 'Male', 1),
    (8, 8901234567890, 'Hassan Ali', 'Akbar Khan', 32, 'Male', 1),
    (9, 9012345678901, 'Safia Bibi', 'Rashid Ahmed', 38, 'Female', 1),
    (10, 1234567890124, 'Usman Ahmed', 'Ali Khan', 26, 'Male', 1),
    (11, 2345678901235, 'Ayesha Khan', 'Naveed Ahmed', 29, 'Female', 1),
    (12, 3456789012346, 'Hamza Malik', 'Nasir Malik', 43, 'Male', 1),
    (13, 4567890123457, 'Saima Akhtar', 'Rafique Akhtar', 35, 'Female', 1),
    (14, 5678901234568, 'Faisal Shahzad', 'Zahid Shahzad', 31, 'Male', 1),
    (15, 6789012345679, 'Sadia Batool', 'Ali Raza', 27, 'Female', 1),
    (16, 7890123456790, 'Zainab Khan', 'Asad Khan', 29, 'Female', 1),
    (17, 8901234567891, 'Tariq Mehmood', 'Abdul Jabbar', 45, 'Male', 1),
    (18, 9012345678902, 'Nida Fatima', 'Shahid Ali', 33, 'Female', 1),
    (19, 1234567890125, 'Ahmed Ali', 'Khalid Ahmed', 38, 'Male', 1),
    (20, 2345678901236, 'Rabia Akhtar', 'Nasir Akhtar', 27, 'Female', 1),
    (21, 3456789012347, 'Kamran Khan', 'Imran Khan', 41, 'Male', 2),
    (22, 4567890123458, 'Sana Ali', 'Rashid Ali', 36, 'Female', 2),
    (23, 5678901234569, 'Hassan Mahmood', 'Ahmed Mahmood', 32, 'Male', 2),
    (24, 6789012345680, 'Sadia Khan', 'Naveed Khan', 28, 'Female', 2),
    (25, 7890123456791, 'Ali Raza', 'Mujahid Raza', 39, 'Male', 2),
    (26, 9012345678903, 'Sara Akram', 'Akram Khan', 31, 'Female', 2),
    (27, 1234567890126, 'Aamir Sohail', 'Sohail Ahmed', 47, 'Male', 2),
    (28, 2345678901237, 'Fatima Shah', 'Asad Shah', 30, 'Female', 2),
    (29, 3456789012348, 'Waqar Ahmed', 'Rafique Ahmed', 42, 'Male', 2),
    (30, 4567890123459, 'Neha Malik', 'Nasir Malik', 34, 'Female', 2),
    (31, 5678901234570, 'Imran Ali', 'Akram Ali', 37, 'Male', 2),
    (32, 6789012345681, 'Ayesha Khan', 'Rashid Khan', 26, 'Female', 2),
    (33, 7890123456792, 'Zubair Khan', 'Naveed Khan', 44, 'Male', 2),
    (34, 8901234567893, 'Bushra Ahmed', 'Mujahid Ahmed', 29, 'Female', 2),
    (35, 9012345678904, 'Usman Ali', 'Akram Ali', 40, 'Male', 2),
    (36, 1234567890127, 'Saima Bibi', 'Rafique Bibi', 35, 'Female', 2),
    (37, 2345678901238, 'Khalid Khan', 'Asad Khan', 48, 'Male', 2),
    (38, 3456789012349, 'Ayesha Ali', 'Rashid Ali', 27, 'Female', 2),
    (39, 4567890123460, 'Asif Khan', 'Imran Khan', 39, 'Male', 2),
    (40, 5678901234571, 'Maria Akhtar', 'Nasir Akhtar', 32, 'Female', 2);
    
    -- inserting regions
INSERT INTO region (regionId, VotingRegion, ElectionId)
VALUES (1, 'NA-123', 1),
(2, 'NA-456', 1),
(3, 'NA-789', 1),
(4, 'PP-123', 2),
(5, 'PP-456', 2),
(6, 'Senate Islamabad', 3),
(7, 'Senate Punjab', 3),
(8, 'Senate Sindh', 3),
(9, 'Senate KP', 3),
(10, 'Senate Baluchistan', 3);
    
    -- inseting voting address
INSERT INTO Voting_Address (VoterId, PollingBoothNo, RegionId, BoothName, City, Province)
VALUES 
    (1, 101, 1, 'ABC School', 'Islamabad', 'Islamabad Capital Territory'),
    (2, 202, 1, 'XYZ College', 'Islamabad', 'Islamabad Capital Territory'),
    (3, 303, 1, 'DEF University', 'Islamabad', 'Islamabad Capital Territory'),
    (4, 404, 1, 'GHI School', 'Islamabad', 'Islamabad Capital Territory'),
    (5, 505, 1, 'LMN College', 'Islamabad', 'Islamabad Capital Territory'),
    (6, 606, 1, 'JKL University', 'Islamabad', 'Islamabad Capital Territory'),
    (7, 707, 1, 'OPQ School', 'Islamabad', 'Islamabad Capital Territory'),
    (8, 808, 1, 'STU School', 'Islamabad', 'Islamabad Capital Territory'),
    (9, 909, 1, 'VWX College', 'Islamabad', 'Islamabad Capital Territory'),
    (10, 1010, 1, 'PQR University', 'Islamabad', 'Islamabad Capital Territory'),
    (11, 1111, 1, 'RST School', 'Islamabad', 'Islamabad Capital Territory'),
    (12, 1212, 1, 'XYZ College', 'Islamabad', 'Islamabad Capital Territory'),
    (13, 1313, 1, 'LMN University', 'Islamabad', 'Islamabad Capital Territory'),
    (14, 1414, 1, 'JKL School', 'Islamabad', 'Islamabad Capital Territory'),
    (15, 1515, 1, 'OPQ College', 'Islamabad', 'Islamabad Capital Territory'),
    (16, 1616, 1, 'ABC School', 'Islamabad', 'Islamabad Capital Territory'),
    (17, 1717, 1, 'DEF College', 'Islamabad', 'Islamabad Capital Territory'),
    (18, 1818, 1, 'GHI University', 'Islamabad', 'Islamabad Capital Territory'),
    (19, 1919, 1, 'JKL School', 'Islamabad', 'Islamabad Capital Territory'),
    (20, 2020, 1, 'MNO College', 'Islamabad', 'Islamabad Capital Territory'),
    (21, 2121, 2, 'PQR University', 'Lahore', 'Punjab'),
    (22, 2222, 2, 'STU School', 'Lahore', 'Punjab'),
    (23, 2323, 2, 'VWX College', 'Lahore', 'Punjab'),
    (24, 2424, 2, 'YZA University', 'Lahore', 'Punjab'),
    (25, 2525, 2, 'BCD School', 'Lahore', 'Punjab'),
    (26, 2626, 2, 'EFG College', 'Lahore', 'Punjab'),
    (27, 2727, 2, 'HIJ University', 'Lahore', 'Punjab'),
    (28, 2828, 2, 'KLM School', 'Lahore', 'Punjab'),
    (29, 2929, 2, 'NOP College', 'Lahore', 'Punjab'),
    (30, 3030, 2, 'QRS University', 'Lahore', 'Punjab'),
    (31, 3131, 2, 'STU School', 'Lahore', 'Punjab'),
    (32, 3232, 2, 'VWX College', 'Lahore', 'Punjab'),
    (33, 3333, 2, 'YZA University', 'Lahore', 'Punjab'),
    (34, 3434, 2, 'BCD School', 'Lahore', 'Punjab'),
    (35, 3535, 2, 'EFG College', 'Lahore', 'Punjab'),
    (36, 3636, 2, 'HIJ University', 'Lahore', 'Punjab'),
    (37, 3737, 2, 'KLM School', 'Lahore', 'Punjab'),
    (38, 3838, 2, 'NOP College', 'Lahore', 'Punjab'),
    (39, 3939, 2, 'QRS University', 'Lahore', 'Punjab'),
    (40, 4040, 2, 'STU School', 'Lahore', 'Punjab');


-- Inserting Parties
INSERT INTO Party (PartyId, Name, Abbreviation, Symbol, Leader)
VALUES 
    (1, 'Pakistan Tehreek-e-Insaf', 'PTI', NULL, 'Imran Khan'),
    (2, 'Pakistan Muslim League-Nawaz', 'PML-N', NULL, 'Shehbaz Sharif'),
    (3, 'Pakistan Peoples Party', 'PPP', NULL, 'Bilawal Bhutto Zardari'),
    (4, 'Jamiat Ulema-e-Islam (F)', 'JUI-F', NULL, 'Maulana Fazlur Rehman'),
    (5, 'Muttahida Qaumi Movement', 'MQM', NULL, 'Khalid Maqbool Siddiqui'),
    (6, 'Jamat-e-Islami', 'JI', NULL, 'Siraj ul Haq');

-- Inserting Candidates
INSERT INTO Candidate (CandidateId, Name, PartyId, ElectionId, RegionId)
VALUES 
    (1, 'Imran Khan', 1, 1, 1),
    (2, 'Nawaz Sharif', 2, 1, 1),
    (3, 'Bilawal Bhutto Zardari', 3, 1, 1),
    (4, 'Maulana Fazlur Rehman', 4, 1, 1),
    (5, 'Khalid Maqbool Siddiqui', 5, 1, 1),
    (6, 'Asfandyar Wali Khan', 6, 1, 1),
    (7, 'Shah Mehmood Qureshi', 1, 1, 2),
    (8, 'Shahbaz Sharif', 2, 1, 2),
    (9, 'Asif Ali Zardari', 3, 1, 2),
    (10, 'Zahid Akram Khan Durrani', 4, 1, 2),
    (11, 'Farooq Sattar', 5, 1, 2),
    (12, 'Siraj-ul-Haq', 6, 1, 2),
    
    (13, 'Ali Amin Gandapur', 1, 2, 4),
    (14, 'Maryam Nawaz', 2, 2, 4),
    (15, 'Murad Ali Shah', 3, 2, 4),
    (16, 'Maryam Nawaz', 4, 2, 4),
    (17, 'Mulana Fazlur Rehman', 5, 2, 4),
    (18, 'Siraj-ul-Haq', 5, 2, 4),
    
    (19, 'Raza Rabbani', 3, 3, 5),
    (20, 'Shahzad Waseem', 1, 3, 6),
    (21, 'Raza Rabbani', 3, 3, 7),
    (22, 'Shahzad Waseem', 1, 3, 8);


-- Inserting Votes

