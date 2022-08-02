package vo;

public class LoginVO {
	String password;
	String username;
	public String getPassword() {
		return password;
	}
	public LoginVO(String password, String username) {
		super();
		this.password = password;
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
