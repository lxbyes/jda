package me.leckie.jda.moudle;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 6672747371885386511L;
	
	private long id;
	private String username;
	private String password;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "{id:" + getId() + ", username:" + getUsername() + ", password:" + getPassword() + "}";
	}

}
