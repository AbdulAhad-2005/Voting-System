package VotingSystem;

import java.io.*;
import java.sql.*;

public class AddingPartyImages {

    public static void main(String[] args) {
        File imageFile1 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\pti.png");
        File imageFile2 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\plmn.png");
        File imageFile3 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\ppp.png");
        File imageFile4 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\juif.png");
        File imageFile5 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\mqm.png");
        File imageFile6 = new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\jamat-e-islami.png");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
            insertImage(connection, imageFile1, "Pakistan Tehreek-e-Insaf");
            insertImage(connection, imageFile2, "Pakistan Muslim League-Nawaz");
            insertImage(connection, imageFile3, "Pakistan Peoples Party");
            insertImage(connection, imageFile4, "Jamiat Ulema-e-Islam (F)");
            insertImage(connection, imageFile5, "Muttahida Qaumi Movement");
            insertImage(connection, imageFile6, "Jamat-e-Islami");

            System.out.println("Images inserted successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertImage(Connection connection, File imageFile, String partyName) throws SQLException, IOException {
        try (FileInputStream fis = new FileInputStream(imageFile);
             PreparedStatement statement = connection.prepareStatement("UPDATE Party SET Symbol = ? WHERE Name = ?")) {
            statement.setBinaryStream(1, fis, (int) imageFile.length());
            statement.setString(2, partyName);
            statement.executeUpdate();
        }
    }
}

