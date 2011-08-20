package eu.grigis.gaetan.rcweb.shared;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private String url;
	private String email;
	private String name;
	private String tokenChan;
	private boolean isLoggedIn;
	private boolean isAdmin;
	private static final long serialVersionUID = 1L;
	public UserInfo() {
		setUrl("");
		setEmail("");
		setName("");
		setTokenChan("");
		setLoggedIn(false);
		setAdmin(false);
	}
	public UserInfo(String n,String e,String u,String t,boolean i,boolean a)
	{
		url=u;name=n;email=e;isLoggedIn=i;isAdmin=a;tokenChan=t;
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
	}
	public void setTokenChan(String tokenChan) {
		this.tokenChan = tokenChan;
	}
	public String getTokenChan() {
		return tokenChan;
	};
}
