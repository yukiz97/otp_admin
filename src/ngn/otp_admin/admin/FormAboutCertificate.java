package ngn.otp_admin.admin;


import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FormAboutCertificate extends VerticalLayout{
	private Panel panel = new Panel();
	private Label html=new Label("",ContentMode.HTML);
	
	public FormAboutCertificate() {
		this.setSizeFull();
		this.setMargin(true);
		this.addComponent(panel);
		this.panel.setContent(html);
		this.panel.setSizeFull();
		this.html.setValue("<div style='padding:15px'><div style='padding:15px'> <div align='center' style='font-weight: bold; font-size: 20px; color: red'> <div>CHỨNG NHẬN BẢN QUYỀN</div> <div>LICENSE CERTIFICATE</div> </div> <br/> <div align='left'> <div style='color: #1c60b7; font-weight: bold'> Mã bản quyền / License ID: 07B6-181211-092133-034-868<br/>Thông tin khách hàng /Customer: </div><div style='text-transform: uppercase; font-weight: bold'> TRUNG TÂM TIN HỌC – CÔNG BÁO THÀNH PHỐ HÀ NỘI <br/>HANOI CENTER OF INFORMATION AND LEGAL DOCUMENTS </div> <div>Địa chỉ (Address): Số 12 Lê Lai, Quận Hoàn Kiếm, Thành phố Hà Nội </div> <table border='1' cellpadding='5' cellspacing='0' width='100%' style='border-collapse: collapse; border-color: #eaeaea;'> <tr> <td> Product Name<br/> Sản phẩm </td> <td> RealTech OTP user license<br/> Bản quyền xác thực mật khẩu một lần cho tài khoản người dùng </td> </tr> <tr> <td> License Volume<br/> Số lượng bản quyền </td> <td> 5000 users<br/> 5000 tài khoản người dùng </td> </tr> <tr> <td> Date of License<br/> Ngày cấp bản quyền </td> <td> December 31, 2018<br/> 31 tháng 12 năm 2018 </td> </tr> <tr> <td> License Type<br/> Kiểu bản quyền </td> <td> Permanent<br/> Vĩnh viễn </td> </tr> <tr> <td> Product Code<br/> Mã bản quyền </td> <td> RTOTPUL1218 </td> </tr> </table></div>");
	}
}
