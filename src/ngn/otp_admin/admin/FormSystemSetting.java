package ngn.otp_admin.admin;


import ngn.otp_admin.services.LDAPService;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.OTPProps;

import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FormSystemSetting extends VerticalLayout {

	FormLayout formLayout=new FormLayout();
	TextField txtServerName=new TextField("LDAP Server name");
	TextField txtLDAPUrl=new TextField("LDAP Url");
	TextField txtBase=new TextField("String Base");
	TextField txtPrincipal=new TextField("Principal");
	TextField txtSessionTimeOut=new TextField("Session time out (minute)");
	PasswordField txtCredentials=new PasswordField("Credentials");
	Button cmdTestServer=new Button("Test server");
	Button cmdSave=new Button("Save setting");
	Button cmdSaveSession=new Button("Save setting");
	Button cmdSaveOwaUrl=new Button("Save setting");
	
	TextField txtZimbraPreAuthKey = new TextField("Zimbra preauth domain key");
	Button cmdSaveZimbraPreAuthKey =new Button("Save setting");
	
	
	TextField txtZimbraLdapKey = new TextField("Zimbra ldap key");
	Button cmdSaveZimbraLdapKey = new Button("Save setting");
	public FormSystemSetting(){
		buildLayout();
		configComponents();
		initComponents();
		
	
	}
	
	private void buildLayout(){
		VerticalLayout vlayout=new VerticalLayout();
		vlayout.setMargin(new MarginInfo(true,false,false,true));
		vlayout.addComponent(new Label("<b>Ldap setting</b>",ContentMode.HTML));
		this.addComponent(vlayout);
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addComponent(txtServerName);
		formLayout.addComponent(txtLDAPUrl);
		formLayout.addComponent(txtBase);
		formLayout.addComponent(txtPrincipal);
		formLayout.addComponent(txtCredentials);
		txtBase.setWidth("100%");
		txtLDAPUrl.setWidth("100%");
		txtServerName.setWidth("100%");
		txtPrincipal.setWidth("100%");
		txtCredentials.setWidth("100%");
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.addComponent(cmdTestServer);
		hlayout.addComponent(cmdSave);
		hlayout.setSpacing(true);
		formLayout.addComponent(hlayout);
		this.addComponent(formLayout);
		VerticalLayout vlayout1=new VerticalLayout();
		vlayout1.setMargin(new MarginInfo(false,true,false,true));
		vlayout1.addComponent(new Label("<b><hr/>Session setting</b>",ContentMode.HTML));
		HorizontalLayout frm=new HorizontalLayout();
		frm.setSpacing(true);
		frm.setMargin(new MarginInfo(false, false, true, false));
		frm.setWidth("100%");
		//frm.addComponent(new Label("Session time out(second)"));
		frm.addComponent(txtSessionTimeOut);
		txtSessionTimeOut.setWidth("100%");
		frm.addComponent(cmdSaveSession);
		frm.setComponentAlignment(cmdSaveSession, Alignment.BOTTOM_RIGHT);
		frm.setExpandRatio(txtSessionTimeOut, 1);
		vlayout1.addComponent(frm);
		this.addComponent(vlayout1);

		
		//add zimbra preauth key
		vlayout1.addComponent(new Label("<b><hr/>Zimbra setting</b>",ContentMode.HTML));
		HorizontalLayout hlayout2 = new HorizontalLayout();
		hlayout2.setWidth("100%");
		hlayout2.addComponents(txtZimbraPreAuthKey,cmdSaveZimbraPreAuthKey);
		txtZimbraPreAuthKey.setWidth("100%");
		hlayout2.setExpandRatio(txtZimbraPreAuthKey, 1);
		hlayout2.setComponentAlignment(cmdSaveZimbraPreAuthKey, Alignment.BOTTOM_RIGHT);
		hlayout2.setMargin(new MarginInfo(false,false,true,false));
		hlayout2.setSpacing(true);
		vlayout1.addComponent(hlayout2);

		//add zimbra ldap key
		vlayout1.addComponent(new Label("<b><hr/>Zimbra ldap setting</b>",ContentMode.HTML));
		HorizontalLayout hlayout3 = new HorizontalLayout();
		hlayout3.setWidth("100%");
		hlayout3.addComponents(txtZimbraLdapKey,cmdSaveZimbraLdapKey);
		txtZimbraLdapKey.setWidth("100%");
		hlayout3.setExpandRatio(txtZimbraLdapKey, 1);
		hlayout3.setComponentAlignment(cmdSaveZimbraLdapKey, Alignment.BOTTOM_RIGHT);
		hlayout3.setMargin(new MarginInfo(false,false,true,false));
		hlayout3.setSpacing(true);
		vlayout1.addComponent(hlayout3);
		
	}
	private void configComponents(){
		txtSessionTimeOut.setRequiredIndicatorVisible(true);
		txtBase.setRequiredIndicatorVisible(true);
		txtCredentials.setRequiredIndicatorVisible(true);
		txtLDAPUrl.setRequiredIndicatorVisible(true);
		txtPrincipal.setRequiredIndicatorVisible(true);
		txtServerName.setRequiredIndicatorVisible(true);
		
		cmdTestServer.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				processTestServerClick();
			}
		});
		cmdSave.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				processSaveClick();
			}
		});
		
		txtSessionTimeOut.addValueChangeListener(event->{
			if(!isNumber(event.getValue())){
				txtSessionTimeOut.setValue("");
			}
		});
		cmdSaveSession.addClickListener(event->{
			processSaveSessionClick();
		});
		
		cmdSaveZimbraPreAuthKey.addClickListener(event->{
			processSaveZimbraPreAuthKey();
		});
		
		cmdSaveZimbraLdapKey.addClickListener(event->{
			processSaveZimbraLdapKey();
		});
	}
	
	private void processSaveZimbraLdapKey() {
		// TODO Auto-generated method stub
		if(txtZimbraLdapKey.getValue().trim().isEmpty()) return;
		OTPLocalServiceUtil.insertZimbraLdapKey(txtZimbraLdapKey.getValue().trim());
		System.out.println(OTPLocalServiceUtil.getZimbraLdapKey());
		txtZimbraLdapKey.setValue("");
		Notification.show("Lưu key thành công!");
	}

	protected void processSaveZimbraPreAuthKey() {
		// TODO Auto-generated method stub
		if(txtZimbraPreAuthKey.getValue().trim().isEmpty()) return;
		OTPLocalServiceUtil.insertZimbraPreAuthKey(txtZimbraPreAuthKey.getValue().trim());
		System.out.println(OTPLocalServiceUtil.getZimbraPreAuthKey());
		txtZimbraPreAuthKey.setValue("");
		Notification.show("Lưu key thành công!");
	}


	protected void processSaveSessionClick() {
		// TODO Auto-generated method stub
		if(txtSessionTimeOut.getValue().isEmpty()){
			txtSessionTimeOut.setComponentError(new UserError("Input session timeout"));
			txtSessionTimeOut.focus();
			return;
		}
//		OTPProps props=new OTPProps();
		OTPProps.setProperty("session.timeout", txtSessionTimeOut.getValue());
		if(OTPProps.saveProperty()){
			Notification.show("Saved success");
			txtSessionTimeOut.setComponentError(null);
		}else{
			Notification.show("Saved fail", Type.ERROR_MESSAGE);
		}
		
	}

	private boolean isNumber(String value){
		try{
			int n=Integer.parseInt(value);
			if(n>0 && n<100){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		
	}
	protected void processTestServerClick() {
		// TODO Auto-generated method stub
		String serverName=txtServerName.getValue();
		String url=txtLDAPUrl.getValue();
		String base=txtBase.getValue();
		String user=txtPrincipal.getValue();
		String pass=txtCredentials.getValue();
		if(LDAPService.ldapAuthenticationSample(url,user,pass)){
			Notification.show("Connected to server successful");
		}else{
			Notification.show("Connected fail",Type.ERROR_MESSAGE);
		}
			
				
	}

	private void initComponents(){
		
		txtServerName.setValue(OTPProps.getProperty("ldap.server"));
		txtPrincipal.setValue(OTPProps.getProperty("ldap.principal"));
		txtCredentials.setValue(OTPProps.getProperty("ldap.credential"));
		txtBase.setValue(OTPProps.getProperty("ldap.base"));
		txtLDAPUrl.setValue(OTPProps.getProperty("ldap.url"));
		txtSessionTimeOut.setValue(OTPProps.getProperty("session.timeout"));

	}
	protected void processSaveClick() {
		// TODO Auto-generated method stub
//		OTPProps props=new OTPProps();
		OTPProps.setProperty("ldap.server", txtServerName.getValue());
		OTPProps.setProperty("ldap.url", txtLDAPUrl.getValue());
		OTPProps.setProperty("ldap.base", txtBase.getValue());
		OTPProps.setProperty("ldap.principal",txtPrincipal.getValue());
		OTPProps.setProperty("ldap.credential", txtCredentials.getValue());
		if(OTPProps.saveProperty()){
			Notification.show("Saved success");
		}else{
			Notification.show("Saved fail", Type.ERROR_MESSAGE);
		}
			
	}
}
