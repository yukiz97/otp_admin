package ngn.otp_admin.beans;

import com.vaadin.ui.HorizontalLayout;

public class UserLockBean {
	private String userName = "";
	private int attemp = 0;
	private String date;
	private HorizontalLayout controls = new HorizontalLayout();
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
	public HorizontalLayout getControls() {
		return controls;
	}
	public void setControls(HorizontalLayout controls) {
		this.controls = controls;
	}
	
	

}
