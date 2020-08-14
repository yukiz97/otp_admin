package ngn.otp_admin.services;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.MetaBroadcaster.ThirtySecondsCache;

import ngn.otp_admin.beans.OTPUserBean;
import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.models.OTPUserDevicesModel;
import ngn.otp_admin.models.OTPUserLogModel;

public class OTPLocalServiceUtil {
	
	
	private static final String salt ="U4VRHKAC26YRDV5U";
	public static OTPModel addOTP(OTPModel otp) throws Exception{
		if(otp.getUserId().equalsIgnoreCase("")){
			return null;
		}
		String sql="insert into otp (userid,phone1,phone2,organization,enable,code,privatekey,manualcode,datecreated,datemodified)";
		sql+="values(";
		sql+="'"+otp.getUserId()+"',";
		sql+="'"+otp.getPhone1()+"',";
		sql+="'"+otp.getPhone2()+"',";
		sql+="'"+otp.getOrganization()+"',";
		sql+=""+otp.isEnabled()+",";
		sql+="'"+otp.getCode()+"',";
		sql+="'"+otp.getPrivateKey()+"',";
		sql+="'"+otp.getManualCode()+"',";
		sql+="now(),";
		sql+="now())";
//		sql+=" on duplicate key update ";
//		sql+=" phone1='"+otp.getPhone1()+"',";
//		sql+=" phone2='"+otp.getPhone2()+"',";
//		sql+=" organization='"+otp.getOrganization()+"',";
//		sql+=" enable="+otp.isEnabled()+",";
//		sql+=" code='"+otp.getCode()+"',";
//		sql+=" privatekey='"+otp.getPrivateKey()+"',";
//		sql+=" manualcode='"+otp.getManualCode()+"',";
//		sql+=" datemodified=now()";
		//System.out.println(sql);
		DBService.executeUpdate(sql);
		return otp;
	}
	public static OTPModel getByUserId(String userId) throws Exception{
		OTPModel otp=new OTPModel();
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		ResultSet rs;
		String sql="select * from otp where userid='"+userId+"'";
		rs=st.executeQuery(sql);
		while(rs.next()){
			otp.setUserId(rs.getString("userid"));
			otp.setPhone1(rs.getString("phone1"));
			otp.setPhone2(rs.getString("phone2"));
			otp.setEnabled(rs.getBoolean("enable"));
			otp.setCode(rs.getString("code"));
			otp.setPrivateKey(rs.getString("privatekey"));
			otp.setManualCode(rs.getString("manualcode"));
			otp.setDateCreated(rs.getDate("datecreated"));
			otp.setDateModified(rs.getDate("datemodified"));
			otp.setOrganization(rs.getString("organization"));
			otp.setAdmin(rs.getBoolean("isadmin"));
			otp.setPassword(rs.getString("password"));
			otp.setEnableSMS(rs.getBoolean("enablesms"));
			otp.setEnableAppCode(rs.getBoolean("enableappcode"));
		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return otp;
		
	}
	public static List<OTPModel> getList() throws Exception{
		List<OTPModel> list=new ArrayList();
		OTPModel otp=new OTPModel();
		String sql="select * from otp";
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		ResultSet rs;
		rs=st.executeQuery(sql);
		while(rs.next()){
			otp=new OTPModel();
			otp.setUserId(rs.getString("userid"));
			otp.setPhone1(rs.getString("phone1"));
			otp.setPhone2(rs.getString("phone2"));
			otp.setEnabled(rs.getBoolean("enable"));
			otp.setCode(rs.getString("code"));
			otp.setPrivateKey(rs.getString("privatekey"));
			otp.setManualCode(rs.getString("manualcode"));
			otp.setDateCreated(rs.getDate("datecreated"));
			otp.setDateModified(rs.getDate("datemodified"));
			otp.setOrganization(rs.getString("organization"));
			list.add(otp);

		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return list;
	}
	public static List<OTPModel> getListByOrganization(String organization) throws Exception{
		List<OTPModel> list=new ArrayList();
		OTPModel otp=new OTPModel();
		String sql="select * from otp where organization like '"+organization+"'";
		ResultSet rs;
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		rs=st.executeQuery(sql);
		while(rs.next()){
			otp=new OTPModel();
			otp.setUserId(rs.getString("userid"));
			otp.setPhone1(rs.getString("phone1"));
			if(otp.getPhone1() == null) {
				otp.setPhone1("");
			}
			otp.setPhone2(rs.getString("phone2"));
			if(otp.getPhone2()==null) {
				otp.setPhone2("");
			}
			otp.setEnabled(rs.getBoolean("enable"));
			otp.setCode(rs.getString("code"));
			otp.setPrivateKey(rs.getString("privatekey"));
			otp.setManualCode(rs.getString("manualcode"));
			otp.setDateCreated(rs.getDate("datecreated"));
			otp.setDateModified(rs.getDate("datemodified"));
			otp.setOrganization(rs.getString("organization"));
			otp.setAdmin(rs.getBoolean("isadmin"));
			otp.setEnableSMS(rs.getBoolean("enablesms"));
			otp.setEnableAppCode(rs.getBoolean("enableappcode"));
			list.add(otp);

		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return list;
	}
	
	public static OTPModel insertOTP(OTPModel otp)throws Exception{
		if(otp.getUserId().equalsIgnoreCase("")){
			return null;
		}
		String sql="insert into otp (userid,phone1,phone2,organization,enable,code,privatekey,manualcode,datecreated,datemodified)";
		sql+="values(";
		sql+="'"+otp.getUserId()+"',";
		sql+="'"+otp.getPhone1()+"',";
		sql+="'"+otp.getPhone2()+"',";
		sql+="'"+otp.getOrganization()+"',";
		sql+=""+otp.isEnabled()+",";
		sql+="'"+otp.getCode()+"',";
		sql+="'"+otp.getPrivateKey()+"',";
		sql+="'"+otp.getManualCode()+"',";
		sql+="now(),";
		sql+="now())";
		DBService.executeUpdate(sql);
		return otp;
	}
	public static int deleteOTP(String id) throws Exception{
		String sql="delete from otp where userid='"+id+"'";
		return DBService.executeUpdate(sql);
	}
	public static void updateEnable(String userId,Boolean enabled) throws Exception {
		// TODO Auto-generated method stub
//		if(checkLicence()) {
			String sql="update otp set enable="+enabled.toString()+" where userid='"+userId+"'";
			DBService.executeUpdate(sql);
		
//		}
		
	}
	
	public static boolean checkLicence() {
		String sql ="SELECT count(userid) from otp where enable = 1";
		int count = 0;
		ResultSet rs;
		Connection cn=null;
		
		try {
			cn=DBService.getConnection();
			Statement st=cn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				count = rs.getInt(1);
			}
			st.close();
			st = null;
			rs.close();
			rs=null;
			cn.close();
			cn = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		if(count == 5000) return false;
		else return true;
		
	}
	
	public static void updateEnableSMS(String userId, Boolean isEnableSMS) throws Exception {
		// TODO Auto-generated method stub
		String sql = "update otp set enablesms = "+isEnableSMS.toString()+" where userid = '"+userId+"'";
		DBService.executeUpdate(sql);
	}
	
	public static void updateEnableAppCode(String userId, Boolean isEnableAppCode) throws Exception {
		String sql = "update otp set enableappcode = "+isEnableAppCode.toString()+" where userid = '"+userId+"'";
		DBService.executeUpdate(sql);
	}
	
	public static void updateAdmin(String userId, Boolean isAdmin) throws Exception {
		String sql = "update otp set isadmin = "+isAdmin.toString()+" where userid = '"+userId+"'";
		DBService.executeUpdate(sql);
	}
	public static void updatePhone(String userId, String phone1, String phone2) throws Exception{
		String sql="update otp set phone1='"+phone1+"',phone2='"+phone2+"' where userid='"+userId+"'";
		DBService.executeUpdate(sql);
	}
	public static void updateCode(String userId, String code) throws Exception{
		String sql="update otp set code='"+code+"' where userid='"+userId+"'";
		DBService.executeUpdate(sql);
	}
	public static void updatePrivateKey(String userId, String privateKey) throws Exception{
		String sql="update otp set privatekey='"+privateKey+"' where userid='"+userId+"'";
		DBService.executeUpdate(sql);
	}
	
	public static void updatePassword(String userId, String password) throws Exception {
		if(userId.trim()=="" || password.trim()=="")return;
		String sql = "update otp set password = AES_ENCRYPT('"+password.trim()+"','"+salt+"') where userid = '"+userId.trim()+"'";
//		String sql = "update otp set password = '"+password.trim()+"' where userid = '"+userId.trim()+"'";
		DBService.executeUpdate(sql);
	}
	
	public static String getPassword(String userId) throws Exception {
		String result ="";
		if(userId.trim()=="") return "";
		
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		
		String sql = "select AES_DECRYPT(password,'"+ salt +"') as pass from otp where userid = '"+userId.trim()+"'";
//		String sql = "select password as pass from otp where userid = '"+userId.trim()+"'";
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
//			result = rs.getString("pass");
			//get blog
			Blob blob = rs.getBlob("pass");
			result = new String(blob.getBytes(1, (int)blob.length()));
			break;
		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return result;
	}
	
	public static List<String> getOrganization() throws Exception{
		List<String> lst=new ArrayList<String>();
		String sql="SELECT distinct organization from otp";
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		ResultSet rs=st.executeQuery(sql);
		String s;
		while(rs.next()){
			s=rs.getString("organization");
			if(s==null)
				s="";
			lst.add(s);
		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return lst;
		
	}
	
	//for user devices table
	public static OTPUserDevicesModel insertUserDevice(OTPUserDevicesModel model) throws Exception{
		String sql="insert into user_devices(userid,devicename,code,createDate)values(";
		sql+="'"+model.getUserId()+"',";
		sql+="'"+model.getDeviceName()+"',";
		sql+="'"+model.getCode()+"',";
		sql+="'"+model.getCreateDate().toString()+"')";
		DBService.executeUpdate(sql);
		
		return model;
	}
	public static List<OTPUserDevicesModel> getListDevicesByUserId(String userId) throws Exception{
		List<OTPUserDevicesModel> list=new ArrayList();
		OTPUserDevicesModel model=new OTPUserDevicesModel();
		String sql="select * from user_devices where userid='"+userId+"'";
		ResultSet rs;
		Connection cn=null;
		cn=DBService.getConnection();
		Statement st=cn.createStatement();
		rs=st.executeQuery(sql);
		while(rs.next()){
			model=new OTPUserDevicesModel();
			model.setUserId(userId);
			model.setDeviceName(rs.getString("deviceName"));
			model.setCode(rs.getString("code"));
			model.setCreateDate(rs.getTimestamp("createDate"));
			list.add(model);
		}
		st.close();
		st = null;
		rs.close();
		rs=null;
		cn.close();
		cn = null;
		return list;
		
		
	}
	
	public static void deleteUserDevice(String userId, String code) throws Exception{
		String sql="delete from user_devices where userId='"+userId+"' and code='"+code+"'";
		DBService.executeUpdate(sql);
	}
	
	public static OTPUserLogModel insertUserLog(OTPUserLogModel model) throws Exception{
		String sql="insert into user_log(userId,timestamp,device,ipAddress,longitude,latitude)value('";
		sql+=model.getUserId()+"','"+model.getTimestamp().toString()+"','"+model.getDeviceName()+"',inet_aton('"+model.getIpAddress()+"')";
		sql+=","+model.getLongitude()+","+model.getLatitude()+")";
		DBService.executeUpdate(sql);
		return model;
	}
	
	public static String getUserAppcode(String userId){
		OTPModel otp=new OTPModel();
		try {
			otp=OTPLocalServiceUtil.getByUserId(userId);
			return otp.getCode().trim();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getUserPrivateKey(String userId){
		OTPModel otp=new OTPModel();
		try {
			otp=OTPLocalServiceUtil.getByUserId(userId);
			return otp.getPrivateKey().trim();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getUserPhone1(String userId) {
		String result = "";
		OTPModel otp = new OTPModel();
		try {
			otp = OTPLocalServiceUtil.getByUserId(userId);
			result = otp.getPhone1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String getUserPhone2(String userId) {
		String result = "";
		OTPModel otp = new OTPModel();
		try {
			otp = OTPLocalServiceUtil.getByUserId(userId);
			result = otp.getPhone2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getZimbraPreAuthKey() {
		String domainKey = "";
		String sql = "select AES_DECRYPT(preauthkey,'"+ salt +"') as 'key' from zimbrapreauthkey";
		try {
			Connection cn=null;
			cn=DBService.getConnection();
			Statement st=cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Blob blob = rs.getBlob("key");
				domainKey = new String(blob.getBytes(1, (int)blob.length()));
			}
			st.close();
			st = null;
			rs.close();
			rs=null;
			cn.close();
			cn = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return domainKey;
		
	}
	public static String getZimbraLdapKey() {
		String ldapkey = "";
		String sql = "select AES_DECRYPT(ldapkey,'"+ salt +"') as 'key' from zimbraldapkey";
		try {
			Connection cn=null;
			cn=DBService.getConnection();
			Statement st=cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Blob blob = rs.getBlob("key");
				ldapkey = new String(blob.getBytes(1, (int)blob.length()));
			}
			st.close();
			st = null;
			rs.close();
			rs=null;
			cn.close();
			cn = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ldapkey;
		
	}
	
	public static void insertZimbraPreAuthKey(String key) {
		String sqlDelete = "delete from zimbrapreauthkey where id = 1";
		String sqlInsert = "insert into zimbrapreauthkey(id,preauthkey)values(1,AES_ENCRYPT('"+key.trim()+"','"+salt+"'))";
		try {
			DBService.executeUpdate(sqlDelete);
			DBService.executeUpdate(sqlInsert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insertZimbraLdapKey(String key) {
		String sqlDelete = "delete from zimbraldapkey where id = 1";
		String sqlInsert = "insert into zimbraldapkey(id,ldapkey)values(1,AES_ENCRYPT('"+key.trim()+"','"+salt+"'))";
		try {
			DBService.executeUpdate(sqlDelete);
			DBService.executeUpdate(sqlInsert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
