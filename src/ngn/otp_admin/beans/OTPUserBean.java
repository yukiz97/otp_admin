package ngn.otp_admin.beans;

import java.io.Serializable;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class OTPUserBean implements Serializable{
	String id;
	Label userId=new Label();
	TextField phone1=new TextField();
	TextField phone2=new TextField();
	CheckBox active=new CheckBox();
	HorizontalLayout controls=new HorizontalLayout();
	TextField code=new TextField();
	TextField privatekey=new TextField();
	CheckBox admin = new CheckBox();
	CheckBox enableSMS = new CheckBox();
	CheckBox enableAppCode = new CheckBox();
	
	public OTPUserBean() {
		
	}
	
	public TextField getCode() {
		return code;
	}
	public void setCode(TextField code) {
		this.code = code;
	}
	public TextField getPrivatekey() {
		return privatekey;
	}
	public void setPrivatekey(TextField privatekey) {
		this.privatekey = privatekey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public Label getUserId() {
		return userId;
	}
	public void setUserId(Label userId) {
		this.userId = userId;
		id=userId.getValue();
	}
	public TextField getPhone1() {
		return phone1;
	}
	public void setPhone1(TextField phone1) {
		this.phone1 = phone1;
	}
	public TextField getPhone2() {
		return phone2;
	}
	public void setPhone2(TextField phone2) {
		this.phone2 = phone2;
	}
	public CheckBox getActive() {
		return active;
	}
	public void setActive(CheckBox active) {
		this.active = active;
	}
	public HorizontalLayout getControls() {
		return controls;
	}
	public void setControls(HorizontalLayout controls) {
		this.controls = controls;
	}
	public CheckBox getAdmin() {
		return admin;
	}
	public void setAdmin(CheckBox admin) {
		this.admin = admin;
	}
	public CheckBox getEnableSMS() {
		return enableSMS;
	}
	public void setEnableSMS(CheckBox enableSMS) {
		this.enableSMS = enableSMS;
	}
	public CheckBox getEnableAppCode() {
		return enableAppCode;
	}
	public void setEnableAppCode(CheckBox enableAppCode) {
		this.enableAppCode = enableAppCode;
	}
	
	
	

}
