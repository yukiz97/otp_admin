package ngn.otp_admin.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

import ngn.otp_admin.beans.UserLockBean;
import ngn.otp_admin.models.LoginModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormUserLock extends VerticalLayout {
	private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private static final Logger logger = LogManager.getLogger(FormUserLock.class);
	public boolean loaded = false;
	Grid<UserLockBean> tblUser=new Grid();
	List<UserLockBean> beans=new ArrayList();
	ListDataProvider<UserLockBean> listDataProvider = new ListDataProvider<>(beans);
	String adminUser = (String) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("adminUser");
	private TextField txtFilter=new TextField();
	private Button cmdFilter1=new Button("Lọc");
	private Button cmdRefresh = new Button("",VaadinIcons.REFRESH);
	
	
	public FormUserLock(){
		loaded =false;
	}
	
	public void initialize() {
		buildLayout();
		configComponents();
		loadData();
	}

	private void buildLayout() {
		tblUser.setWidth("100%");
		tblUser.setHeight("100%");
		
		HorizontalLayout hlayout=new HorizontalLayout();
		hlayout.setWidth("100%");
		txtFilter.setWidth("100%");
		hlayout.addComponents(txtFilter,cmdFilter1,cmdRefresh);
		hlayout.setExpandRatio(txtFilter, 1);
		this.addComponent(hlayout);
		this.addComponent(tblUser);
		
		this.setMargin(new MarginInfo(true,false,false,false));
		this.setSizeFull();
		this.setExpandRatio(tblUser, 1);
		
	}

	private void configComponents() {
		configTable();
		cmdFilter1.setClickShortcut(KeyCode.ENTER);
		cmdFilter1.addClickListener(event->{
			doFilter();
		});
		tblUser.setDataProvider(this.listDataProvider);
		cmdRefresh.addClickListener(event->{
			loadData();
		});
		
	}

	protected void doFilter() {
		// TODO Auto-generated method stub
		listDataProvider.setFilter(UserLockBean::getUserName, value ->{
			if(value == null)
				return false;
			String valueLower = value.toString().toLowerCase();
			String filterLower = txtFilter.getValue().toLowerCase();
			return valueLower.contains(filterLower);
		});
	}

	private Map<String, LoginModel> getListLog() {
		Map<String, LoginModel> hashLog = new ConcurrentHashMap<String, LoginModel>();
		String result="";
		String url = "http://localhost:8080/otp_auth/LogAttempService";
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, "");
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.addHeader("OTPLogAttemp-Command", "GetListLogAttemp")
				.addHeader("cache-control", "no-cache")
				.build();
		try {
			Response response = client.newCall(request).execute();
			result =response.headers().get("OTPLogAttemp-Status");
			response.body().close();
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result == null || result.isEmpty()) return hashLog;
		
		String strarr[] = result.split(";");
		for(int i=0;i<strarr.length; i++) {
			LoginModel model = new LoginModel();
			String str[] = strarr[i].split(":");
			model.setUserName(str[0]);
			model.setAttemp(Integer.valueOf(str[1]));
			model.setDate(str[2]);
			hashLog.put(model.getUserName(), model);
		}
		
		return hashLog;
		
	}
	public void loadData() {
		// TODO Auto-generated method stub
		beans = new ArrayList();
		listDataProvider = new ListDataProvider<>(beans);
		txtFilter.setValue("");
		Map<String, LoginModel> hashLog = new ConcurrentHashMap<String, LoginModel>();
//		hashLog = LogAttempService.getListLog();
		//get list log
		hashLog = getListLog();
		
		System.out.println("size: "+hashLog.size());
		UserLockBean bean = new UserLockBean();

		for(String key:hashLog.keySet()) {
			LoginModel model = hashLog.get(key);
			System.out.println("user: "+model.getUserName());
			bean = new UserLockBean();
			bean.setUserName(model.getUserName());
			bean.setAttemp(model.getAttemp());
			bean.setDate(model.getDate());
			
			Button cmdDelete=new Button("",VaadinIcons.TRASH);
			cmdDelete.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			cmdDelete.setData(bean);
			
			cmdDelete.addClickListener(event->{
				processDelete(event);
			});
			
			HorizontalLayout vlayout = new HorizontalLayout();
			vlayout.addComponent(cmdDelete);
			bean.setControls(vlayout);
			
			beans.add(bean);
		}
		this.listDataProvider = new ListDataProvider<>(beans);
		tblUser.setDataProvider(listDataProvider);
		
		
	}
	protected void processDelete(ClickEvent e) {
		// TODO Auto-generated method stub
		UserLockBean bean=new UserLockBean();
		bean=(UserLockBean)e.getButton().getData();
		String url = "http://localhost:8080/otp_auth/LogAttempService";
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, "");
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.addHeader("OTPLogAttemp-Command", "DeleteLog")
				.addHeader("userName",bean.getUserName())
				.addHeader("cache-control", "no-cache")
				.build();
		try {
			Response response = client.newCall(request).execute();
			loadData();
			response.body().close();
			response.close();
		} catch (IOException ee) {
			// TODO Auto-generated catch block
			ee.printStackTrace();
		}
		
	}

	private void configTable() {
		tblUser.addColumn(UserLockBean::getUserName).setId("taikhoan");
		tblUser.addColumn(UserLockBean::getAttemp).setId("solan");
		tblUser.addColumn(UserLockBean::getDate).setId("thoigian");
		tblUser.addComponentColumn(UserLockBean::getControls).setId("thaotac");
		
		tblUser.getDefaultHeaderRow().getCell("taikhoan").setHtml("<strong>Tài khoản</strong>");
		tblUser.getDefaultHeaderRow().getCell("solan").setHtml("<strong>Số lần</strong>");
		tblUser.getDefaultHeaderRow().getCell("thoigian").setHtml("<strong>Thời gian</strong>");
		tblUser.getDefaultHeaderRow().getCell("thaotac").setHtml("<strong>Thao tác</strong>");
		
	}

}
