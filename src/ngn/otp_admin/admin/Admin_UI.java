package ngn.otp_admin.admin;

import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;

import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.OTPProps;
import ngn.otp_admin.utils.TOTPUtil;
@SuppressWarnings("serial")
@Theme("otpagenttheme")
@StyleSheet("styles.css")
//@Widgetset("DefaultWidgetSet")
public class Admin_UI extends UI {
	
	private static final Logger logger = LogManager.getLogger(Admin_UI.class);
	MainLayout mLayout=null;
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = Admin_UI.class)
	
	
	public static class Servlet extends VaadinServlet {
	}

	VerticalLayout layout=new VerticalLayout();
	FormLayout formLayout=new FormLayout();
	TextField txtUserName = new TextField("TÃªn tÃ i khoáº£n");
	TextField txtPassword=new TextField("MÃ£ OTP");
	Button cmdLogin=new Button("Ä�Äƒng nháº­p");
	
	Label errMessage = new Label("");

	
	
	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("isAdminLogin", false);
		if(isLogin())
			buildLayout();
		else{
			buildLoginLayout();
			configComponents();
		}
		
	}
	
	public void buildLayout(){
		
		if(!isLogin()) {
			System.out.println("not loggin");
			return;
		}
		
		MainLayout mLayout=new MainLayout();
//		FormSystemSetting mLayout = new FormSystemSetting();
		setContent(mLayout);
		this.setSizeFull();
		this.setHeight("100%");
	}
	public MainLayout getMainLayout(){
		return mLayout;
	}
	
	private void buildLoginLayout(){
		VerticalLayout vlayout=new VerticalLayout();
		Panel panel=new Panel("ThÃ´ng tin Ä‘Äƒng nháº­p");
		formLayout.addComponents(txtUserName,txtPassword,cmdLogin);
		formLayout.setMargin(true);
		txtPassword.setSizeFull();
		txtUserName.setSizeFull();
		
		cmdLogin.setWidth("100%");
		cmdLogin.setStyleName(ValoTheme.BUTTON_SMALL);
		cmdLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		vlayout.addComponent(formLayout);
		vlayout.addComponent(errMessage);
		
		
		panel.setWidth("30%");
		panel.setContent(vlayout);
		layout.setMargin(true);
		layout.addComponent(panel);
		layout.setComponentAlignment(panel, Alignment.TOP_CENTER);
		setContent(layout);
		
//		formLayout.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		
	}
	
	
	private void configComponents(){
		cmdLogin.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				processLogon();
			}
		});
		cmdLogin.setClickShortcut(com.vaadin.event.ShortcutAction.KeyCode.ENTER);
	}
	protected void processLogon() {
		// TODO Auto-generated method stub
		if(txtUserName.getValue().trim().isEmpty() || txtPassword.getValue().trim().isEmpty()) {
//			Notification.show("Vui lÃ²ng nháº­p tÃ i khoáº£n vÃ  mÃ£ OTP!",Type.WARNING_MESSAGE);
			errMessage.setValue("Vui lÃ²ng nháº­p tÃ i khoáº£n vÃ  mÃ£ OTP!");
			errMessage.setStyleName(ValoTheme.LABEL_FAILURE);
			return;
		}
		try{
			if(checkAdminOTPCode()==true){
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute("isAdminLogin", true);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute("adminUser", txtUserName.getValue());
				logger.info("Login admin     "+txtUserName.getValue()+"     SUCCESS");
				buildLayout();
			}else{
//				Notification.show("TÃªn tÃ i khoáº£n hoáº·c mÃ£ OTP khÃ´ng Ä‘Ãºng, hoáº·c khÃ´ng cÃ³ quyá»�n quáº£n trá»‹!",Type.WARNING_MESSAGE);
				errMessage.setValue("TÃªn tÃ i khoáº£n hoáº·c mÃ£ OTP khÃ´ng Ä‘Ãºng, hoáº·c khÃ´ng cÃ³ quyá»�n quáº£n trá»‹!");
				errMessage.setStyleName(ValoTheme.LABEL_FAILURE);
				txtPassword.focus();
				logger.info("Login admin     "+txtUserName.getValue()+"     FAIL");
			}
		}catch(Exception e){
			
		}
	}
	
	private boolean checkAdminOTPCode(){
		if(txtPassword.isEmpty() || txtUserName.isEmpty()) return false;
		
//		if(txtPassword.getValue().equalsIgnoreCase("aquarium") && txtUserName.getValue().equals("root")) {
//			VaadinService.getCurrentRequest().getWrappedSession().setAttribute("organization", "root");
//			return true;
//		}
		

		String privateKey="", otpCode = txtPassword.getValue();
		if(txtUserName.getValue().trim().equals("root")) {
			privateKey=(String)OTPProps.getProperty("admin.privatekey");
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute("organization", "root");
			return TOTPUtil.checkOTP(privateKey, otpCode);
		}
		
		
		OTPModel otp=new OTPModel();
		try {
			otp=OTPLocalServiceUtil.getByUserId(txtUserName.getValue().trim());
			System.out.println(otp.isAdmin());
			if(otp.isAdmin() == false) {
				return false;
			}	
			privateKey = otp.getPrivateKey();
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute("organization", otp.getOrganization());
			return TOTPUtil.checkOTP(privateKey, otpCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}

	private boolean isLogin(){
		try{
			boolean isLogin=(Boolean)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("isAdminLogin");
			return isLogin;
		}catch(Exception e){
			return false;
		}

	}


}
