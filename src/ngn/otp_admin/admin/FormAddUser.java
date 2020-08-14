package ngn.otp_admin.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.TOTPUtil;

public class FormAddUser extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(FormAddUser.class);
	String adminUser = (String) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("adminUser");
	//Components
	public Window win;
	TextField txtUserId=new TextField("Tài khoản");
	CheckBox chkEnableOTP=new CheckBox("Kích hoạt",true);
	TextField txtPhone1=new TextField("Điện thoại 1");
	TextField txtPhone2=new TextField("Điện thoại 2");
	ComboBox<String> cmbOrganization =new ComboBox<>("Tổ chức");
	Button cmdSave=new Button("Lưu",VaadinIcons.DOWNLOAD);
	Button cmdCancel=new Button("Hủy");

	FormAddUser(){
		buildLayout();
		configComponents();
		initComboBox();

	}
	
	private void buildLayout(){
		this.setMargin(true);
		FormLayout form=new FormLayout();
		txtUserId.setRequiredIndicatorVisible(true);
		txtPhone1.setRequiredIndicatorVisible(true);
		cmbOrganization.setEmptySelectionAllowed(false);
		cmbOrganization.setRequiredIndicatorVisible(true);
		cmbOrganization.setTextInputAllowed(false);
		
		
		txtUserId.setWidth("100%");
		txtPhone1.setWidth("100%");
		txtPhone2.setWidth("100%");
		cmbOrganization.setWidth("100%");
		cmbOrganization.setEmptySelectionAllowed(false);
		form.addComponent(txtUserId);
		form.addComponent(txtPhone1);
		form.addComponent(txtPhone2);
		form.addComponent(cmbOrganization);
		form.addComponent(chkEnableOTP);
		
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.setSpacing(true);
		hlayout.addComponent(cmdSave);
		hlayout.addComponent(cmdCancel);
		//form.addComponent(hlayout);
		
		this.addComponent(form);
		this.addComponent(hlayout);
		this.setComponentAlignment(hlayout, Alignment.MIDDLE_RIGHT);
	}

	private void configComponents(){
//		txtUserId.setRequired(true);
		txtUserId.focus();
		txtPhone2.setEnabled(false);
		cmdSave.setClickShortcut(KeyCode.ENTER);
		cmdSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
		txtPhone1.setIcon(VaadinIcons.MOBILE);
		txtPhone2.setIcon(VaadinIcons.MOBILE);
		txtUserId.setIcon(VaadinIcons.USER);
		cmbOrganization.setTextInputAllowed(true);
		
		cmdSave.addClickListener(event-> {
			save();
		});
		
		
		txtPhone1.addBlurListener(event->{
			if(isValidPhoneNumber(txtPhone1.getValue())){
				txtPhone2.setEnabled(true);
			}else{
				txtPhone2.setEnabled(false);
				txtPhone2.setValue("");
			}
		});
		
		cmdCancel.addClickListener(event->{
			win.close();
		});
	}

	protected void initComboBox(){
		cmbOrganization.clear();
		List<String> lst=new ArrayList<String>();
		String organization = (String)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("organization");
		if(!organization.equalsIgnoreCase("root")) {
			lst.add(organization);
			cmbOrganization.setItems(lst);
			cmbOrganization.setValue(organization);
			cmbOrganization.setEnabled(false);
			return;
		}
		lst.add("Peoples");
		cmbOrganization.setItems(lst);
		cmbOrganization.setSelectedItem(lst.get(0));
	}
	private void save(){
		if(isValid()==false)return;
		try {
			OTPModel otp=new OTPModel();
			otp.setUserId(txtUserId.getValue().trim());
			otp.setPhone1(txtPhone1.getValue().trim());
			otp.setPhone2(txtPhone2.getValue().trim());
			otp.setEnabled(chkEnableOTP.getValue());
			otp.setOrganization(cmbOrganization.getValue().toString());
			otp.setCode(TOTPUtil.generateSecret());
			otp.setPrivateKey(TOTPUtil.generatePrivateKey(40));
			//String username=(String)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("username");
			OTPLocalServiceUtil.addOTP(otp);
			logger.info("Add User     "+adminUser+"     "+txtUserId.getValue()+"     SUCCESS");
			Notification.show("Insert successded");
			clearControl();
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Insert failured");
		}
	}

	private boolean isValid(){
		txtPhone1.setComponentError(null);
		txtPhone2.setComponentError(null);
		txtUserId.setComponentError(null);
		
		
		if(txtUserId.getValue().isEmpty()){
			txtUserId.setComponentError(new UserError("Input user id"));
			txtUserId.focus();
			return false;
		}
		
		if(isValidPhoneNumber(txtPhone1.getValue())==false && txtPhone1.getValue().isEmpty()==false){
			txtPhone1.setComponentError(new UserError("Invalid phone number"));
			txtPhone1.focus();
			return false;
		}
		if(txtPhone2.isEnabled()==true && txtPhone2.getValue().isEmpty()==false){
			if(isValidPhoneNumber(txtPhone2.getValue().trim())==false){
				txtPhone2.setComponentError(new UserError("Invalid phone number"));
				txtPhone2.focus();
				return false;
			}
		}

		return true;
	}
	private boolean isValidPhoneNumber(String phoneNumber){
		try{
			Double.parseDouble(phoneNumber);
		}catch(Exception e){
			return false;
		}
		PhoneNumberUtil phoneUtil=PhoneNumberUtil.getInstance();
		try {
			PhoneNumber pn=phoneUtil.parse(phoneNumber,"VN");
			System.out.println(pn.getCountryCode());
			System.out.println(pn.toString());
			System.out.println(pn.getRawInput());
			boolean valid=phoneUtil.isValidNumber(pn);
			return valid;
		} catch (NumberParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	protected void clearControl(){
		txtUserId.setValue("");
		txtPhone1.setValue("");
		txtPhone2.setValue("");
		chkEnableOTP.setValue(true);
		txtUserId.focus();
	}

}
