package aml_ar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;


public class User {

	private int id = 0;
	private String username;
	private String password;
	private String email;
	
	public User(){}
	
	public User(String username, String password, String email){
		super();
		setUsername(username);
		setPassword(password);
		setEmail(email);
		
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public User setPassword(String password) {
		
		this.password = BCrypt.hashpw(password, BCrypt.gensalt() );
		return this;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	
	private void setId(int id) {
		this.id = id;
	}

	static public User[] loadAll (Connection conn) throws SQLException {
		
		ArrayList<User> users = new ArrayList<>();
		
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery("Select * from user");
		
		while (res.next()){
			User tmpUsr = new User();
			tmpUsr.setUsername(res.getString("username"));
			tmpUsr.setEmail(res.getString("email"));
			tmpUsr.password = res.getString("password");
			tmpUsr.setId(res.getInt("id"));
			
			users.add(tmpUsr);		
		}
		User[] userArr = new User[users.size()];
		users.toArray(userArr);
		return userArr;
		
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getUsername() ).append(" ").append( this.getEmail() );
		return sb.toString();
	}
	
	public User saveToDB(Connection conn) throws SQLException {
		if (this.getId()==0){
			//add to db
			String[] generatedColumns = {"id"};
			PreparedStatement pst = conn.prepareStatement("Insert into user (username, password, email) Value (?,?,?)", generatedColumns);
			pst.setString(1, this.getUsername() );
			pst.setString(2, this.getPassword() );
			pst.setString(3, this.getEmail() );		
			pst.executeUpdate();
			
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()) {
				this.setId( rs.getInt(1) );
			}	
			
		} else {
			//update to db
			PreparedStatement pst = conn.prepareStatement("Update user Set username=?, password=?, email=? Where id = ?");
			pst.setString(1, this.getUsername() );
			pst.setString(2, this.getPassword() );
			pst.setString(3, this.getEmail() );
			pst.setInt(4, this.getId() );

			pst.executeUpdate();
		}
		
		return this;
	}
	
}