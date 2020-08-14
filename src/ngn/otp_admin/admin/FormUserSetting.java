package ngn.otp_admin.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.dialogs.ConfirmDialog;

import ngn.otp_admin.beans.OTPUserBean;
import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.OTPProps;
import ngn.otp_admin.utils.TOTPUtil;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class FormUserSetting extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FormUserSetting.class);
	
	public boolean loaded = false;
	Grid<OTPUserBean> tblUser=new Grid();
	TextField txtFilter=new TextField();
	public Button cmdFilter1=new Button("Lọc");
	Button cmdAddNew=new Button("Thêm tài khoản",VaadinIcons.PLUS);
	Button cmdSelectAll=new Button("Chọn tất cả", VaadinIcons.CHECK_SQUARE_O);
	Button cmdDeSelectAll=new Button("Bỏ chọn tất cả",VaadinIcons.SQUARE_SHADOW);
	Button cmdActive = new Button("Kích hoạt", VaadinIcons.USER_CHECK);
	Button cmdUnActive = new Button("Ngừng kích hoạt");
	Button cmdDelete=new Button("Xóa",VaadinIcons.TRASH);
	Button cmdImport = new Button("Import điện thoại từ excel", VaadinIcons.DOWNLOAD);
	ComboBox<String> cmbOrganization=new ComboBox();
	List<OTPUserBean> beans=new ArrayList<OTPUserBean>();
	ListDataProvider<OTPUserBean> listDataProvider = new ListDataProvider<>(beans);
	
	String adminUser = (String) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("adminUser");
	
	public FormUserSetting(){
		buildLayout();
		configComponents();
		loadData();
//		loaded =false;
		
	}
	
	public void initialize() {
//		buildLayout();
//		configComponents();
//		loadData();
	}
	public void loadData(){
		initComboBox();
		initTableData();
	}
	private void buildLayout(){
//		OTPProps props=new OTPProps();
		boolean phoneEnable = true;
		try {
			phoneEnable = Boolean.valueOf(OTPProps.getProperty("phone.enable"));
		}catch(Exception e) {
			
		}
		
		if(phoneEnable) {
			tblUser.addComponentColumn(OTPUserBean::getUserId).setId("taikhoan").setCaption("Tài khoản").setWidthUndefined();
			tblUser.addComponentColumn(OTPUserBean::getPhone1).setId("dienthoai1").setCaption("Điện thoại 1");
			tblUser.addComponentColumn(OTPUserBean::getPhone2).setId("dienthoai2").setCaption("Điện thoại 2");
			tblUser.addComponentColumn(OTPUserBean::getPrivatekey).setId("khoariengtu").setCaption("Khóa riêng tư");
			tblUser.addComponentColumn(OTPUserBean::getCode).setId("maungdung").setCaption("Mã ứng dụng");
			tblUser.addComponentColumn(OTPUserBean::getActive).setId("kichhoat").setCaption("Kích hoạt").setStyleGenerator(item -> "v-align-center");
			tblUser.addComponentColumn(OTPUserBean::getEnableSMS).setId("kichhoatsms").setCaption("Kích hoạt SMS").setStyleGenerator(item -> "v-align-center");
			tblUser.addComponentColumn(OTPUserBean::getEnableAppCode).setId("kichhoatappcode").setCaption("Kích hoạt AppCode").setStyleGenerator(item -> "v-align-center");
		
			tblUser.addComponentColumn(OTPUserBean::getAdmin).setId("laquantri").setCaption("Là quản trị").setStyleGenerator(item -> "v-align-center");
			tblUser.addComponentColumn(otpUserBean ->{
				return otpUserBean.getControls();
			}).setId("thaotac");
			tblUser.getDefaultHeaderRow().getCell("taikhoan").setHtml("<strong>Tài khoản</strong>");
			tblUser.getDefaultHeaderRow().getCell("dienthoai1").setHtml("<strong>Điện thoại 1</strong>");
			tblUser.getDefaultHeaderRow().getCell("dienthoai2").setHtml("<strong>Điện thoại 2</strong>");
			tblUser.getDefaultHeaderRow().getCell("khoariengtu").setHtml("<strong>Khóa riêng tư</strong>");
			tblUser.getDefaultHeaderRow().getCell("maungdung").setHtml("<strong>Mã ứng dụng</strong>");
			tblUser.getDefaultHeaderRow().getCell("kichhoat").setHtml("<strong>Kích hoạt</strong>");
			tblUser.getDefaultHeaderRow().getCell("kichhoatsms").setHtml("<strong>Kích hoạt SMS</strong>");
			tblUser.getDefaultHeaderRow().getCell("kichhoatappcode").setHtml("<strong>Kích hoạt appcode</strong>");
			tblUser.getDefaultHeaderRow().getCell("laquantri").setHtml("<strong>Là quản trị</strong>");
			tblUser.getDefaultHeaderRow().getCell("thaotac").setHtml("<strong>Thao tác</strong>");
			
			
		}else {
			tblUser.addComponentColumn(OTPUserBean::getUserId).setId("taikhoan");
			tblUser.addComponentColumn(OTPUserBean::getPrivatekey).setId("khoariengtu");
			tblUser.addComponentColumn(OTPUserBean::getActive).setId("kichhoat");
			tblUser.addComponentColumn(OTPUserBean::getAdmin).setId("laquantri");
			tblUser.addComponentColumn(OTPUserBean::getControls).setId("thaotac");
			tblUser.getDefaultHeaderRow().getCell("taikhoan").setHtml("<strong>Tài khoản</strong>");
			tblUser.getDefaultHeaderRow().getCell("khoariengtu").setHtml("<strong>Khóa riêng tư</strong>");
			tblUser.getDefaultHeaderRow().getCell("kichhoat").setHtml("<strong>Kích hoạt</strong>");
			tblUser.getDefaultHeaderRow().getCell("laquantri").setHtml("<strong>Là quản trị</strong>");
			tblUser.getDefaultHeaderRow().getCell("thaotac").setHtml("<strong>Thao tác</strong>");
		}
		
		
		
		
		tblUser.setWidth("100%");
		tblUser.setHeight("100%");
		//command
		HorizontalLayout hlayout1=new HorizontalLayout();
		hlayout1.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		cmbOrganization.setPlaceholder("Organization");
		cmbOrganization.setEmptySelectionAllowed(false);
		hlayout1.addComponent(cmbOrganization);
		hlayout1.addComponent(cmdAddNew);
		hlayout1.addComponent(cmdSelectAll);
		hlayout1.addComponent(cmdDeSelectAll);
		hlayout1.addComponent(cmdActive);
		hlayout1.addComponent(cmdUnActive);
		hlayout1.addComponent(cmdDelete);
//		hlayout1.addComponent(cmdImport);
		this.addComponent(hlayout1);
		//filter
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.setWidth("100%");
		hlayout.addComponent(txtFilter);
		txtFilter.setWidth("100%");
		hlayout.addComponent(cmdFilter1);
		hlayout.setExpandRatio(txtFilter, 1);
		this.addComponent(hlayout);
		this.addComponent(tblUser);
		this.setMargin(new MarginInfo(true,false,false,false));
		this.setSizeFull();
		this.setExpandRatio(tblUser, 1);
	}
	
	private HorizontalLayout generateControls(OTPUserBean otpUserBean) {
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.setSpacing(false);
		CheckBox chkSelect=new CheckBox("");
		
		Button cmdGenerateKey = new Button("", VaadinIcons.REFRESH);
		cmdGenerateKey.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cmdGenerateKey.setDescription("Phát sinh khóa riêng tư");
		cmdGenerateKey.addClickListener(event->{
			otpUserBean.getPrivatekey().setValue(TOTPUtil.generatePrivateKey(40));
		});
		
		
		Button cmdDelete=new Button("",VaadinIcons.TRASH);
		cmdDelete.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cmdDelete.setData(otpUserBean);
		cmdDelete.addClickListener(event->{
			processDeleteClick(event);
		});
	
		Button cmdListDevices=new Button("",VaadinIcons.SITEMAP);
		cmdListDevices.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cmdListDevices.addClickListener(event->{
			processListDeviceClick(otpUserBean.getUserId().getValue());
		});
			
		hlayout.addComponents(cmdGenerateKey,cmdDelete,cmdListDevices,chkSelect);
		hlayout.setComponentAlignment(chkSelect, Alignment.MIDDLE_CENTER);
		return hlayout;
	}
	
	private void configComponents(){
		cmdFilter1.setClickShortcut(KeyCode.ENTER);
		cmdSelectAll.addClickListener(event->{
			processSelectAllClick();
		});
		
		cmdDeSelectAll.addClickListener(event->{
			processDeselecteAllClick();
		});
		cmdDelete.addClickListener(event->{
			processDeleteClick1();
		});
		cmdFilter1.addClickListener(event->{
			doFilter();
		});
		cmdAddNew.addClickListener(event->{
			processAddNewClick();
		});
		
		cmbOrganization.addValueChangeListener(event->{
			processOrganizationChangeValue();
		});
		cmdActive.addClickListener(event->{
			processActiveClick1();
		});
		
		cmdUnActive.addClickListener(event->{
			processUnActiveClic();
		});
		
		cmdImport.addClickListener(event->{
			Window window = new Window();
//			ImportExcel frm = new ImportExcel();
//			frm.window = window;
//			window.setContent(frm);
//			window.center();
//			getUI().addWindow(window);
//			window.setVisible(true);
//			window.addCloseListener(new CloseListener() {
//				private static final long serialVersionUID = 1L;
//				@Override
//				public void windowClose(CloseEvent e) {
//					// TODO Auto-generated method stub
//					initTableData();
//				}
//			});
		});
		
	}
	
	protected void processUnActiveClic() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có chắc muốn ngừng kích hoạt các tài khoản được chọn?","Đồng ý","Hủy",new ConfirmDialog.Listener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(ConfirmDialog arg0) {
				// TODO Auto-generated method stub
				if(arg0.isConfirmed()){
					OTPUserBean bean=new OTPUserBean();
					for(int i=0;i<beans.size();i++){
						bean=beans.get(i);
						HorizontalLayout hlayout=bean.getControls();
						CheckBox chk=(CheckBox)hlayout.getComponent(3);
						if(chk.getValue()==true){
							String userId=bean.getUserId().getValue();
							try {
								OTPLocalServiceUtil.updateEnable(userId,false);
								bean.getActive().setValue(false);
								logger.info("Unenable OTP     "+adminUser+"     "+userId+"     SUCCESS");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			}
		});
		
		
	}
	protected void processActiveClick1() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có chắc muốn kích hoạt các tài khoản được chọn?","Đồng ý","Hủy",new ConfirmDialog.Listener() {
			
			@Override
			public void onClose(ConfirmDialog arg0) {
				// TODO Auto-generated method stub
				if(arg0.isConfirmed()){
					OTPUserBean bean=new OTPUserBean();
					System.out.println("bean size: "+beans.size());
					for(int i=0;i<beans.size();i++){
						bean=beans.get(i);
						HorizontalLayout hlayout=bean.getControls();
						CheckBox chk=(CheckBox)hlayout.getComponent(3);
						if(chk.getValue()==true){
							String userId=bean.getUserId().getValue();
							try {
								OTPLocalServiceUtil.updateEnable(userId,true);
								bean.getActive().setValue(true);
								logger.info("Enable OTP     "+adminUser+"     "+userId+"     SUCCESS");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			}
		});
		
	}
	protected void processOrganizationChangeValue() {
		// TODO Auto-generated method stub
		initTableData();
	}
	protected void processAddNewClick() {
		// TODO Auto-generated method stub
		FormAddUser frm=new FormAddUser();
		Window win=new Window("Thêm tài khoản mới");
		win.setWidth("30%");
		win.setContent(frm);
		frm.win=win;
		win.setWindowMode(WindowMode.NORMAL);
		win.setModal(true);
		
		getUI().addWindow(win);
		win.addCloseListener(new Window.CloseListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				initComboBox();
				initTableData();
				
			}
		});
		
	}
	private void initTableData(){
		beans= new ArrayList();
		List<OTPModel> list=new ArrayList<OTPModel>();
		try {
			list=OTPLocalServiceUtil.getListByOrganization(cmbOrganization.getValue().toString());
			if(list.size()==0)return;
			
			OTPUserBean bean;
			for(OTPModel otp:list){
				try {
					//list variables
					CheckBox chkEnableSMS = new CheckBox("", otp.isEnableSMS());
					chkEnableSMS.setEnabled(otp.isEnabled());
		
					bean=new OTPUserBean();
					bean.setUserId(new Label(otp.getUserId().trim()));
									
					TextField txtcode=new TextField();
					txtcode.setWidth("100%");
					txtcode.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					if(otp.getCode()!=null && !otp.getCode().equalsIgnoreCase("null")){
						txtcode.setValue(otp.getCode().trim());
					}
					bean.setCode(txtcode);
					txtcode.addBlurListener(event->{
						processCodeValueChange(otp.getUserId(),txtcode);
					});
					
					TextField txtprivatekey=new TextField();
					txtprivatekey.setWidth("100%");
					txtprivatekey.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					if(otp.getPrivateKey()!=null && !otp.getPrivateKey().equalsIgnoreCase("null")){
						txtprivatekey.setValue(otp.getPrivateKey());
						txtprivatekey.setData(new String(otp.getPrivateKey().trim()));
					}		
					bean.setPrivatekey(txtprivatekey);
					txtprivatekey.addBlurListener(event->{
						processPrivateKeyValueChange(otp.getUserId(),txtprivatekey);
					});
					txtprivatekey.addFocusListener(event->{
						txtprivatekey.selectAll();
					});
					
					TextField txtphone1=new TextField();
					txtphone1.setWidth("100%");
					txtphone1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					if(otp.getPhone1()!=null && !otp.getPhone1().equalsIgnoreCase("null")){
						txtphone1.setValue(otp.getPhone1().trim());
						txtphone1.setData(new String(otp.getPhone1().trim()));
						bean.setPhone1(txtphone1);
					}else{
						bean.setPhone1(txtphone1);
						txtphone1.setData(new String(otp.getPhone1().trim()));
					}
					
					TextField txtphone2=new TextField();	
					txtphone2.setWidth("100%");
					txtphone2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					if(txtphone1.getValue().trim().equalsIgnoreCase("")){
						txtphone2.setEnabled(false);
						
					}
					if(otp.getPhone2()!=null && !otp.getPhone2().equalsIgnoreCase("null")){
						txtphone2.setValue(otp.getPhone2().trim());
						txtphone2.setData(new String(otp.getPhone2().trim()));
						bean.setPhone2(txtphone2);
						
					}else{
						bean.setPhone2(txtphone2);
						txtphone2.setData(new String(otp.getPhone2().trim()));
					}
					
					txtphone1.addBlurListener(event->{
						processPhone1ValueChange(otp.getUserId(),txtphone1,txtphone2);
					});

					txtphone2.addBlurListener(event->{
						processPhone2ValueChange(otp.getUserId(),txtphone1,txtphone2);
					});
					
					CheckBox chkActive=new CheckBox("",otp.isEnabled());
					chkActive.setData(otp.getUserId());
					bean.setActive(chkActive);
					
					chkActive.addValueChangeListener(event->{
						
						if(!processActiveClick(event,otp.getUserId(), chkEnableSMS)) {
							chkActive.setValue(false);
						}
					});
						
					chkEnableSMS.setData(otp.getUserId());
					bean.setEnableSMS(chkEnableSMS);
					
					chkEnableSMS.addValueChangeListener(event->{
						processEnableSMSClick(event, otp.getUserId());
					});
					
					CheckBox chkEnableAppCode = new CheckBox("", otp.isEnableAppCode());
					chkEnableAppCode.setData(otp.getUserId());
					bean.setEnableAppCode(chkEnableAppCode);
					
					chkEnableAppCode.addValueChangeListener(event->{
						processEnableAppCodeClick(event, otp.getUserId());
					});
					
					CheckBox chkAdmin = new CheckBox("",otp.isAdmin());
					chkAdmin.setData(otp.getUserId());
					bean.setAdmin(chkAdmin);
					chkAdmin.addValueChangeListener(event->{
						processAdminClick(event, otp.getUserId());
					});
					bean.setControls(this.generateControls(bean));
					beans.add(bean);
				}catch(Exception ee) {
					ee.printStackTrace();
				}
			}
			this.listDataProvider = new ListDataProvider<>(beans);
			this.tblUser.setDataProvider(this.listDataProvider);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	protected void processEnableSMSClick(ValueChangeEvent<Boolean> event, String userId) {
		// TODO Auto-generated method stub
		try {
			OTPLocalServiceUtil.updateEnableSMS(userId, event.getValue());
			logger.info("Enable SMS     "+adminUser+"     "+userId+"     "+(Boolean)event.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void processEnableAppCodeClick(ValueChangeEvent<Boolean>  event, String userId) {
		try {
			OTPLocalServiceUtil.updateEnableAppCode(userId, event.getValue());
			logger.info("Enable Appcode     "+adminUser+"     "+userId+"     "+event.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void processListDeviceClick(String userId) {
		// TODO Auto-generated method stub
		FormEditDevices frm=new FormEditDevices(userId);
		Window win=new Window("Danh sách thiết bị");
		win.setWidth("40%");
		win.setContent(frm);
		win.setWindowMode(WindowMode.NORMAL);
		win.setModal(true);
		
		getUI().addWindow(win);
		
	}
	protected void processPrivateKeyValueChange(String userId, TextField txtprivatekey) {
		String oldValue = (String)txtprivatekey.getData();
		String newValue = txtprivatekey.getValue();
		if(oldValue.equals(newValue)) {
			return;
		}
		// TODO Auto-generated method stub
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có muốn lưu lại sự thay đổi khóa riêng tư?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

			@Override
			public void onClose(ConfirmDialog arg0) {
				// TODO Auto-generated method stub
				if(arg0.isConfirmed()) {
					try {
						OTPLocalServiceUtil.updatePrivateKey(userId, txtprivatekey.getValue());
						txtprivatekey.setData(new String(txtprivatekey.getValue()));
						logger.info("Change privatekey     "+adminUser+"     "+userId+"     ");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					txtprivatekey.setValue((String) txtprivatekey.getData());
				}
			}
			
		});
		
		
	}
	protected void processCodeValueChange(String userId, TextField txtcode) {
		try {
			OTPLocalServiceUtil.updateCode(userId, txtcode.getValue());
			logger.info("Change Appcode     "+adminUser+"     "+userId+"     "+txtcode.getValue());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	protected void processPhone1ValueChange(String userId,TextField phone1, TextField phone2) {
		// TODO Auto-generated method stub
		String oldValue = (String)phone1.getData();
		String newValue = phone1.getValue();
		if(oldValue.equals(newValue)) {
			return;
		}
		
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có muốn lưu lại sự thay đổi số điện thoại 1 tư?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

			@Override
			public void onClose(ConfirmDialog arg0) {
				// TODO Auto-generated method stub
				if(arg0.isConfirmed()) {
					if(phone1.getValue().trim().equalsIgnoreCase("")){
						phone2.setValue("");
						try {
							OTPLocalServiceUtil.updatePhone(userId,phone1.getValue(),phone2.getValue());
							logger.info("Delete Phone 1     "+adminUser+"     "+userId+"     "+phone1.getValue()+"     SUCCESS");
							return;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return;
						}		
					}
					if(isValidPhoneNumber(phone1.getValue())){
						try {
							OTPLocalServiceUtil.updatePhone(userId,phone1.getValue(),phone2.getValue());
							logger.info("Change Phone 1     "+adminUser+"     "+userId+"     "+phone1.getValue()+"     SUCCESS");
							phone2.setEnabled(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							phone2.setEnabled(false);
							e.printStackTrace();
							
						}
					}else{
						logger.info("Change Phone 1     "+adminUser+"     "+userId+"     "+phone1.getValue()+"     FAIL");
						phone2.setEnabled(false);
						phone1.setValue((String) phone1.getData());
//						phone1.setValue("");
						Notification.show("Số điện thoại không đúng",Type.ERROR_MESSAGE);
					}
				}else {
					phone1.setValue((String) phone1.getData());
				}
				
			}
			
		});
		
		
	}
	protected void processPhone2ValueChange(String userId,TextField phone1, TextField phone2) {
		// TODO Auto-generated method stub
		
		String oldValue = (String)phone2.getData();
		String newValue = phone2.getValue();
		if(oldValue.equals(newValue)) {
			return;
		}
		
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có muốn lưu lại sự thay đổi số điện thoại 2 tư?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

			@Override
			public void onClose(ConfirmDialog arg0) {
				if(arg0.isConfirmed()) {
					if(phone2.getValue().trim().equalsIgnoreCase("")){
						try {
							OTPLocalServiceUtil.updatePhone(userId,phone1.getValue(),phone2.getValue());
							logger.info("Delete Phone 2     "+adminUser+"     "+userId+"     "+phone2.getValue()+"     SUCCESS");
							return;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return;
						}
					}
					if(isValidPhoneNumber(phone2.getValue())){
						try {
							OTPLocalServiceUtil.updatePhone(userId,phone1.getValue(),phone2.getValue());
							logger.info("Change Phone 2     "+adminUser+"     "+userId+"     "+phone2.getValue()+"     SUCCESS");
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.info("Change Phone 2     "+adminUser+"     "+userId+"     "+phone2.getValue()+"     FAIL");
							e.printStackTrace();
							
						}
					}else{
						Notification.show("Số điện thoại không đúng",Type.ERROR_MESSAGE);
						phone2.setValue((String) phone2.getData());
						logger.info("Change Phone 2     "+adminUser+"     "+userId+"     "+phone2.getValue()+"     FAIL");
					}
				}else {
					phone2.setValue((String) phone2.getData());
				}
				
			}
			
		});
		
	}
	
	protected boolean processActiveClick(ValueChangeEvent<Boolean> event,String userId, CheckBox chk) {
		// TODO Auto-generated method stub
		try {
			if(event.getValue()==true) {
				if(!OTPLocalServiceUtil.checkLicence()) {
					Notification.show("Thông báo: số người dùng đã đạt đến giới hạn",Type.WARNING_MESSAGE);
					return false;
				}
					
			}
			OTPLocalServiceUtil.updateEnable(userId,event.getValue());
			logger.info("Enable OTP     "+adminUser+"     "+userId+"     "+event.getValue());
			OTPLocalServiceUtil.updateEnableSMS(userId, event.getValue());
			chk.setValue(event.getValue());
			chk.setEnabled(event.getValue());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
		
	}
	
	protected void processAdminClick(ValueChangeEvent<Boolean> event, String userId) {
		try {
			OTPLocalServiceUtil.updateAdmin(userId, event.getValue());
			logger.info("Change admin user     "+adminUser+"     "+userId+"     "+event.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void processDeleteClick(ClickEvent e) {
		// TODO Auto-generated method stub
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có chắc muốn xóa tài khoản này?","Đồng ý","Hủy",new ConfirmDialog.Listener() {
			
			@Override
			public void onClose(ConfirmDialog dialog) {
				// TODO Auto-generated method stub
				if(dialog.isConfirmed()){
					//process delete
					OTPUserBean bean=new OTPUserBean();
					bean=(OTPUserBean)e.getButton().getData();
					String userId=bean.getUserId().getValue();
					try {
						OTPLocalServiceUtil.deleteOTP(userId);
						beans.remove(bean);
						logger.info("Delete user     "+adminUser+"     "+userId+"     SUCCESS");
						listDataProvider.refreshAll();
						tblUser.getDataProvider().refreshAll();
						//them vao ngay 27/02/2019
						if(beans.size() == 0 ) {
							initComboBox();
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});

	}
	protected void processEditClick(ClickEvent e) {
		// TODO Auto-generated method stub
		OTPUserBean bean=new OTPUserBean();
		bean=(OTPUserBean)e.getButton().getData();
		String userId=bean.getUserId().getValue();
		Notification.show(userId);
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
	
	protected void processDeselecteAllClick() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		OTPUserBean bean=new OTPUserBean();
		for(int i=0;i<beans.size();i++){
			bean=beans.get(i);
			HorizontalLayout hlayout=bean.getControls();
			CheckBox chk=(CheckBox)hlayout.getComponent(3);
			chk.setValue(false);
			
		}
		
	}
	protected void processSelectAllClick() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		OTPUserBean bean=new OTPUserBean();
		for(int i=0;i<beans.size();i++){
			bean=beans.get(i);
			HorizontalLayout hlayout=bean.getControls();
			CheckBox chk=(CheckBox)hlayout.getComponent(3);
			chk.setValue(true);
			
		}
		this.listDataProvider.refreshAll();;
		this.tblUser.getDataProvider().refreshAll();
		
	}
	
	
	protected void processDeleteClick1(){
		if(beans.size()==0)return;
		
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có chắc muốn xóa?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

			@Override
			public void onClose(ConfirmDialog arg0) {
				// TODO Auto-generated method stub
				if(arg0.isConfirmed()){
					OTPUserBean bean=new OTPUserBean();
					for(int i=0;i<beans.size();i++){
						bean=beans.get(i);
						HorizontalLayout hlayout=bean.getControls();
						CheckBox chk=(CheckBox)hlayout.getComponent(3);
						if(chk.getValue()==true){
							String userId=bean.getUserId().getValue();
							try {
								OTPLocalServiceUtil.deleteOTP(userId);
								logger.info("Delete user     "+adminUser+"     "+userId+"     SUCCESS");
								beans.remove(bean);
								i--;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					//them vao ngay 27/02/2019
					if(beans.size() == 0 ) {
						initComboBox();
					}
					listDataProvider.refreshAll();
					tblUser.getDataProvider().refreshAll();
				}
				
			}
		});
			
				
	}
	
	protected void doFilter(){
		listDataProvider.setFilter(OTPUserBean::getUserId, value ->{
			if(value == null)
				return false;
			String valueLower = value.getValue().toLowerCase();
			String filterLower = txtFilter.getValue().toLowerCase();
			return valueLower.contains(filterLower);
		});
	}
	
	protected void initComboBox(){
		List<String> lst=new ArrayList<String>();
		
		String organization = (String)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("organization");
		if(!organization.equalsIgnoreCase("root")) {
			lst.add(organization);
			cmbOrganization.setItems(lst);
			cmbOrganization.setValue(organization);
			return;
		}
		try {
			lst=OTPLocalServiceUtil.getOrganization();
			System.out.println(lst.size());
			if(lst.size()>0) {
				cmbOrganization.setItems(lst);
				cmbOrganization.setValue(lst.get(0));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
