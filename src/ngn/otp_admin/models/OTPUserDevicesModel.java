package ngn.otp_admin.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OTPUserDevicesModel implements Serializable{
	private String userId;
	private String deviceName;
	private String code;
	private Date createDate;
	
	public OTPUserDevicesModel(){
		userId="";
		deviceName="";
		code="";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreateDate() {
		try{
		SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String shortdate=df.format(createDate);
		return shortdate;
		}catch(Exception e){
			return "";
		}
		
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
