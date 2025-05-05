import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 7409858457123385676L;
	private String fullName;
	private String employeeID;
	private String username;
	private String password;
	private Role role;
	
	User(String fullName, String employeeID, String username,String password, Role role){
		this.fullName = fullName;
		this.employeeID = employeeID;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public Role getRole(){
		return this.role;
	}
	
	public String getFullName() {
		return this.fullName;
	}
	
	public String getEmployeeID() {
		return this.employeeID;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
}
