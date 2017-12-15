package aml_ar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/aml_ar?useSSL=false", "root", "coderslab")){
			
			User[] users = User.loadAll(conn);
			for (User u : users){
				System.out.println(u.toString());
			}
			
			users[1].setEmail("nowy@mail.com");
			users[1].saveToDB(conn);

			
			User user = new User("test127","12345","mail127@mail.pl");
			user.saveToDB(conn);

			User user1 = new User("test128","12345","mail128@mail.pl");
			user1.saveToDB(conn);
			
			User user2 = new User("test129","12345","mail129@mail.pl");
			user2.saveToDB(conn);
			
			System.out.println( user.toString() );
			System.out.println( user1.toString() );
			System.out.println( user2.toString() );
			
		} catch (SQLException e){
    		System.out.println(e);
    	}	

	}

}