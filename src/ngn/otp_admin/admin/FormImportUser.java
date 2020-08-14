package ngn.otp_admin.admin;


import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ngn.otp_admin.beans.OTPBean;
import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.services.LDAPService;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.OTPProps;
import ngn.otp_admin.utils.TOTPUtil;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import com.vaadin.ui.TextField;

import com.vaadin.ui.VerticalLayout;


public class FormImportUser extends VerticalLayout{
	
	TextField txtFilter=this.getColumnFilterField();
	public Button cmdFilter=new Button("Lọc");
	Button cmdGetListUser=new Button("Lấy danh sách người dùng");
	Button cmdSelectAll=new Button("Chọn tất cả");
	Button cmdDeSelectALL=new Button("Bỏ chọn tất cả");
	Button cmdImport=new Button("Nhập khẩu");
	Grid<OTPBean> tblUser=new Grid<>();
	Label lblCount = new Label("",ContentMode.HTML);
	List<OTPBean> beans=new ArrayList<OTPBean>();
	ListDataProvider<OTPBean> dataProvider = new ListDataProvider<>(beans);
	
	
	public FormImportUser(){
		buildLayout();
		configComponents();
		
	}
	private void buildLayout(){
		tblUser.setDataProvider(dataProvider);
		//add controls
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.addComponent(cmdGetListUser);
		hlayout.addComponent(cmdSelectAll);
		hlayout.addComponent(cmdDeSelectALL);
		hlayout.addComponent(cmdImport);
		hlayout.addComponent(lblCount);
//		hlayout.setComponentAlignment(lblCount, Alignment.MIDDLE_RIGHT);
		lblCount.setWidth("95%");
		hlayout.setWidth("100%");
		hlayout.setExpandRatio(lblCount, 1f);
		
		this.addComponent(hlayout);
		//add filter
		HorizontalLayout hlayout1=new HorizontalLayout();
		hlayout1.setWidth("100%");
		hlayout1.addComponent(txtFilter);
		txtFilter.setWidth("100%");
		hlayout1.addComponent(cmdFilter);
		hlayout1.setExpandRatio(txtFilter, 1);
		this.addComponent(hlayout1);
		//add table
		this.addComponent(tblUser);
		tblUser.addComponentColumn(OTPBean::getUserId).setCaption("Tài khoản");
		tblUser.addComponentColumn(OTPBean::getOrganization).setCaption("Tổ chức");
		tblUser.addComponentColumn(OTPBean::getSelected).setCaption("Chọn");
		
		tblUser.setWidth("100%");
		tblUser.setHeight("100%");
		
		this.setMargin(new MarginInfo(true,false,false,false));
		this.setHeight("100%");
		this.setExpandRatio(tblUser, 1);
		
	}
	
	private void configComponents(){
//		cmdFilter.setClickShortcut(KeyCode.ENTER);
		cmdFilter.addClickListener(event->{
			doFilter();
		});
		cmdGetListUser.addClickListener(event->{
			processGetListClick();
		});
		cmdSelectAll.addClickListener(event->{
			processSelectAllClick();
		});
		
		cmdDeSelectALL.addClickListener(event->{
			processDeselecteAllClick();
		});
		
		cmdImport.addClickListener(event->{
			processImportClick();
		});
	}
	protected void processImportClick() {
		// TODO Auto-generated method stub
		
		if(beans.size()==0)return;
		ConfirmDialog.show(getUI(),"Xác nhận","Bạn có chắc muốn nhập khẩu những tài khoản được chọn?","Đồng ý","Hủy",new ConfirmDialog.Listener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(ConfirmDialog dialog) {
				// TODO Auto-generated method stub
				if(dialog.isConfirmed()){
					for(int i=0;i<beans.size();i++){
						OTPModel otp=new OTPModel();
						OTPBean bean=beans.get(i);
						if(bean.getSelected().getValue()==true){
							try {
								otp.setUserId(bean.getUserId().getValue().trim());
								otp.setOrganization(bean.getOrganization().getValue());
								otp.setCode(TOTPUtil.generateSecret());
								otp.setPrivateKey(TOTPUtil.generatePrivateKey(40));
								OTPLocalServiceUtil.insertOTP(otp);
								
							} catch ( Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					Notification.show("Import success");
				}
			}
		});
		
	}
	protected void processDeselecteAllClick() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		OTPBean bean=new OTPBean();
		for(int i=0;i<beans.size();i++){
			bean=beans.get(i);
			bean.getSelected().setValue(false);
		}
		
	}
	protected void processSelectAllClick() {
		// TODO Auto-generated method stub
		if(beans.size()==0)return;
		OTPBean bean=new OTPBean();
		for(int i=0;i<beans.size();i++){
			bean=beans.get(i);
			bean.getSelected().setValue(true);
		}
		
	}
	protected void processGetListClick() {
		// TODO Auto-generated method stub
		lblCount.setValue("");
		beans = new ArrayList();
//		OTPProps props=new OTPProps();
		String serverURL=OTPProps.getProperty("ldap.url");
		String securityBase=OTPProps.getProperty("ldap.base");
		String username=OTPProps.getProperty("ldap.principal");
		String password=OTPProps.getProperty("ldap.credential");
		String organization=securityBase.split(",")[0].split("=")[1].toString();
//		List<String> list=LDAPService.getListUser(serverURL, securityBase, username, password);
		List<String> list = LDAPService.getListUser(serverURL,securityBase, username, password);
		if(list.size()==0) return;
		for(String s:list){
			OTPBean bean=new OTPBean();
			bean.setUserId(new Label(s));
			CheckBox chkSelected=new CheckBox();
			bean.setSelected(chkSelected);
			bean.setOrganization(new Label(organization));
			beans.add(bean);
//			
		}
		lblCount.setValue("<p style=\"text-align:right; color:red;\">"+"Số mẫu tin: "+list.size()+"</p>");
		this.dataProvider = new ListDataProvider<>(beans);
		this.tblUser.setDataProvider(this.dataProvider);
	}
	protected void doFilter(){
		dataProvider.setFilter(OTPBean::getUserId, value ->{
			if(value == null)
				return false;
			String valueLower = value.toString().toLowerCase();
			String filterLower = txtFilter.getValue().toLowerCase();
			return valueLower.contains(filterLower);
		});
	}
	
	private TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.setPlaceholder("Lọc");
        return filter;
    }
	
}
