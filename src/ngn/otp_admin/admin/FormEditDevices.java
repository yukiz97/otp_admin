package ngn.otp_admin.admin;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ngn.otp_admin.beans.OTPDevicesBean;
import ngn.otp_admin.models.OTPUserDevicesModel;
import ngn.otp_admin.services.OTPLocalServiceUtil;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FormEditDevices extends VerticalLayout {

	private String userId="";
	public Grid<OTPDevicesBean> tblDevices=new Grid<>();
	List<OTPDevicesBean> beans=new ArrayList<OTPDevicesBean>();
	ListDataProvider<OTPDevicesBean> listDataProvider = new ListDataProvider<>(beans);
	
	public FormEditDevices(String userid){
		this.userId=userid;
		buildLayout();
		initTable();
	}
	
	private void buildLayout(){
		tblDevices.setItems(beans);
		tblDevices.addComponentColumn(OTPDevicesBean::getDeviceName).setCaption("Device name");
		tblDevices.addComponentColumn(OTPDevicesBean::getCode).setCaption("code");
		tblDevices.addComponentColumn(OTPDevicesBean::getCreateDate).setCaption("Create date");
		tblDevices.addComponentColumn(OTPDevicesBean::getControls).setCaption("Controls");
		tblDevices.setWidth("100%");
		this.addComponent(tblDevices);
		
	}
	
	private void initTable(){
		beans = new ArrayList();
		List<OTPUserDevicesModel> list=new ArrayList();
		try {
			list=OTPLocalServiceUtil.getListDevicesByUserId(userId);
			if(list.size()==0) return;
			//OTPDevicesBean bean;
			for(OTPUserDevicesModel model: list){
				OTPDevicesBean bean=new OTPDevicesBean();
				bean.setCode(new Label(model.getCode()));
				bean.setDeviceName(new Label(model.getDeviceName()));
				bean.setCreateDate(new Label(model.getCreateDate()));
				HorizontalLayout vLayout=new HorizontalLayout();
				Button cmdDelete=new Button("",VaadinIcons.TRASH);
				cmdDelete.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				vLayout.addComponent(cmdDelete);
				vLayout.setComponentAlignment(cmdDelete, Alignment.MIDDLE_CENTER);
				cmdDelete.addClickListener(event->{
					processDeleteButtonClick(bean);
				});
				
				bean.setControls(vLayout);
				System.out.println(bean.getDeviceName());
				beans.add(bean);
			}
			this.listDataProvider = new ListDataProvider<>(beans);
			tblDevices.setDataProvider(listDataProvider);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void processDeleteButtonClick(OTPDevicesBean bean) {
		// TODO Auto-generated method stub
		ConfirmDialog.show(getUI(),"Confirm","Are you sure to delete this user?","Ok","Cancel",new ConfirmDialog.Listener(){
			@Override
			public void onClose(ConfirmDialog dialog) {
				if(dialog.isConfirmed()){
					try {
						OTPLocalServiceUtil.deleteUserDevice(userId,bean.getCode().getValue());
						beans.remove(bean);
						listDataProvider.refreshAll();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// TODO Auto-generated method stub
				
			}
			
		});	
	}
}
