package ngn.otp_admin.admin;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ngn.otp_admin.admin.FormSystemSetting;

public class MainLayout extends VerticalLayout{

	private static final long serialVersionUID = 1L;
	TabSheet tabs=new TabSheet();
	FormSystemSetting frmSystemSetting=new FormSystemSetting();
	FormImportUser frmImportUser=new FormImportUser();
	FormUserSetting frmUserSetting=new FormUserSetting();
	FormAboutCertificate frmAbout = new FormAboutCertificate();
	FormUserLock frmUserLock = new FormUserLock();
	//header
	HorizontalLayout headerLayout=new HorizontalLayout();
	//content
	VerticalLayout contentLayout=new VerticalLayout();
	//footer
//	VerticalLayout footerLayout=new VerticalLayout();
	//logout button
	Button cmdLogout = new Button("Đăng xuất",VaadinIcons.SIGN_OUT);

	public MainLayout(){
		buildLayout();
		configComponents();
	}

	public void buildLayout(){
		String organization = (String)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("organization");

		frmSystemSetting.setCaption("Cài đặt hệ thống");
		frmSystemSetting.setIcon(VaadinIcons.TOOLS);
		frmUserSetting.setCaption("Cài đặt người dùng");
		frmUserSetting.setIcon(VaadinIcons.USER);
		frmImportUser.setCaption("Nhập khẩu người dùng");
		frmAbout.setCaption("Thông tin bản quyền");
		frmImportUser.setIcon(VaadinIcons.USERS);
		frmUserLock.setCaption("Tài khoản tạm khóa");
		frmUserLock.setIcon(VaadinIcons.LOCK);

		//check if root login
		if(organization.equals("root")) {
			tabs.addTab(frmSystemSetting);
		}
		tabs.addTab(frmImportUser);
		tabs.addTab(frmUserSetting);
		tabs.addTab(frmUserLock);
		tabs.addTab(frmAbout);
		
		tabs.setSizeFull();
		
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		contentLayout.addComponent(tabs);
		contentLayout.setMargin(new MarginInfo(false,true,false,true));
		headerLayout.setMargin(new MarginInfo(false,true,false,true));
//		footerLayout.setMargin(true);
		this.addComponent(headerLayout);
		this.addComponent(contentLayout);
//		this.addComponent(footerLayout);
//		contentLayout.setSizeFull();
//		contentLayout.setHeight("100%");
//		this.setComponentAlignment(headerLayout, Alignment.TOP_CENTER);
//		this.setComponentAlignment(contentLayout, Alignment.TOP_CENTER);
//		this.setComponentAlignment(footerLayout, Alignment.BOTTOM_CENTER);
//		this.setSizeFull();
		this.setExpandRatio(contentLayout, 1);

		headerLayout.addComponent(new Label("<h2><b>CÀI ĐẶT OTP</b></h2>",ContentMode.HTML));
		headerLayout.addComponent(cmdLogout);
		headerLayout.setComponentAlignment(cmdLogout, Alignment.MIDDLE_RIGHT);
		headerLayout.setWidth("100%");
//		headerLayout.setMargin(true);

		contentLayout.setHeight("100%");
		this.setHeight("100%");

	}
	public void configComponents(){
		
		tabs.addSelectedTabChangeListener(event->{
			String caption=tabs.getSelectedTab().getCaption();
			if(caption.equalsIgnoreCase("import user")){
				frmImportUser.cmdFilter.setClickShortcut(KeyCode.ENTER);
				frmUserSetting.cmdFilter1.setClickShortcut(-1);

				return;
			}
			if(caption.equalsIgnoreCase("user setting")){
				frmUserSetting.cmdFilter1.setClickShortcut(KeyCode.ENTER);
				frmImportUser.cmdFilter.setClickShortcut(-1);
				frmUserSetting.loadData();
			}
			
			if(tabs.getSelectedTab().getCaption().equalsIgnoreCase("Cài đặt người dùng")) {
				if(!frmUserSetting.loaded) {
					frmUserSetting.initialize();
					frmUserSetting.loaded = true;
				}
				frmUserSetting.loadData();
			}
			
			if(tabs.getSelectedTab().getCaption().equalsIgnoreCase("Tài khoản tạm khóa")) {
				if(!frmUserLock.loaded) {
					frmUserLock.initialize();
					frmUserLock.loaded = true;
				}
				frmUserLock.loadData();

			}
			
		});
		cmdLogout.addClickListener(event->{
			processLogout();
		});

	}

	private void processLogout() {
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("isAdminLogin", false);
		UI.getCurrent().getPage().setLocation("./admin");
	}

	public FormUserSetting getFrmUserSetting() {
		return frmUserSetting;

	}


}
