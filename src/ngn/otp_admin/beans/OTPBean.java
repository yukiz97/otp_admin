package ngn.otp_admin.beans;

import java.io.Serializable;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;

public class OTPBean  implements Serializable{
	String id;
	Label organization=new Label();
	Label userId=new Label();
	CheckBox selected=new CheckBox();
	
	
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
	public CheckBox getSelected() {
		return selected;
	}
	public void setSelected(CheckBox selected) {
		this.selected = selected;
	}
	public Label getOrganization() {
		return organization;
	}
	public void setOrganization(Label organization) {
		this.organization = organization;
	}
	
	
}
