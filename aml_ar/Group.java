package aml_ar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Group {
	
	private int id = 0;
	private String name;
	
	public Group(){}
	
	public Group(String name){
		super();
		setName(name);
	}
	
	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getId()).append(" ").append( this.getName());
		return sb.toString();
	}
	
	static public Group[] loadAll (Connection conn) throws SQLException {
		ArrayList<Group> groups = new ArrayList<>();
		
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery("Select * from user_group");
		
		while (res.next()){
			Group tmpGroup = new Group();
			tmpGroup.setId(res.getInt("id"));
			tmpGroup.setName(res.getString("name"));
			
			groups.add(tmpGroup);		
		}
		Group[] groupArr = new Group[groups.size()];
		groups.toArray(groupArr);
		return groupArr;
	}
	public Group loadById (Connection conn) throws SQLException {
		
		PreparedStatement pst = conn.prepareStatement("Select * from user_group WHERE id = ?");
		pst.setInt(1, this.getId() );
		ResultSet res = pst.executeQuery();
		
		Group tmpGroup = new Group();
		tmpGroup.setName(res.getString("name"));
		tmpGroup.setId(res.getInt("id"));
	
		return tmpGroup;
	}


	public Group saveToDB(Connection conn) throws SQLException {
		if (this.getId()==0){
			//add to db
			String[] generatedColumns = {"id"};
			PreparedStatement pst = conn.prepareStatement("Insert into user_group (name) Value (?)", generatedColumns);
			pst.setString(1, this.getName() );	
			pst.executeUpdate();
			
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()) {
				this.setId( rs.getInt(1) );
			}	
			
		} else {
			//update to db
			PreparedStatement pst = conn.prepareStatement("Update user_group Set name=? Where id = ?");
			pst.setString(1, this.getName() );
			pst.setInt(2, this.getId() );

			pst.executeUpdate();
		}
		return this;
	}
	
	public void delete (Connection conn) throws SQLException {
		
		PreparedStatement pst = conn.prepareStatement("DELETE from user_group WHERE id = ?");
		pst.setInt(1, this.getId() );
		pst.executeUpdate();
	}
}
