package eu.grigis.gaetan.rcweb.shared;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private String url;
	private String email;
	private String name;
	private boolean isLoggedIn;
	private boolean isAdmin;
	private static final long serialVersionUID = 1L;
	public UserInfo() {
		setUrl("");
		setEmail("");
		setName("");
		setLoggedIn(false);
		setAdmin(false);
	}
	public UserInfo(String n,String e,String u,boolean i,boolean a)
	{
		url=u;name=n;email=e;isLoggedIn=i;isAdmin=a;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isAdmin() {
		return isAdmin;
	};
}
