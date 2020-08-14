package ngn.otp_admin.beans;

import java.io.Serializable;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class OTPDevicesBean implements Serializable{
	Label deviceName=new Label();
	Label code=new Label();
	Label createDate=new Label();
	HorizontalLayout controls=new HorizontalLayout();
	public Label getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(Label deviceName) {
		this.deviceName = deviceName;
	}
	public Label getCode() {
		return code;
	}
	public void setCode(Label code) {
		this.code = code;
	}
	public HorizontalLayout getControls() {
		return controls;
	}
	public void setControls(HorizontalLayout controls) {
		this.controls = controls;
	}
	public Label getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Label createDate) {
		this.createDate = createDate;
	}
	
	
}
