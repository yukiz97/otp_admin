package ngn.otp_admin.models;

import java.io.Serializable;
import java.util.Date;

public class OTPModel implements Serializable {
	private String userId;
	private String phone1;
	private String phone2;
	private boolean enabled;
	private String code;
	private String privateKey;
	private String manualCode;
	private Date dateCreated;
	private Date dateModified;
	private String organization;
	private boolean isAdmin;
	private String password;
	
	//ngay 23/01/2019
	private boolean enableSMS;
	private boolean enableAppCode;
	
	public OTPModel(){
		userId="";
		phone1="";
		phone2="";
		enabled=false;
		code="";
		privateKey="";
		manualCode="";
		isAdmin = false;
		enableSMS = false;
		enableAppCode = false;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getManualCode() {
		return manualCode;
	}
	public void setManualCode(String manualCode) {
		this.manualCode = manualCode;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnableSMS() {
		return enableSMS;
	}

	public void setEnableSMS(boolean enableSMS) {
		this.enableSMS = enableSMS;
	}

	public boolean isEnableAppCode() {
		return enableAppCode;
	}

	public void setEnableAppCode(boolean enableAppCode) {
		this.enableAppCode = enableAppCode;
	}
	
	
	
	
	
}
