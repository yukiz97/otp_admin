package ngn.otp_admin.models;

import java.io.Serializable;
import java.util.Date;

public class LoginModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName = "";
	private int attemp = 0;
	private String date;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAttemp() {
		return attemp;
	}
	public void setAttemp(int attemp) {
		this.attemp = attemp;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
