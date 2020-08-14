package ngn.otp_admin.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import ngn.otp_admin.utils.OTPProps;

public class DBService {
	
	
	public DBService(){
		
	}
	
	public static Connection getConnection() throws Exception {
		Connection cn=null;
		cn=ConnectionService.pdsMySQL.getConnection();
		return cn;
		
	}
	
	
//	public static ResultSet executeQuery(String sql) throws Exception{
//		Connection cn=null;
//		cn=getConnection();
//		Statement st=cn.createStatement();
//		ResultSet rs;
//		rs=st.executeQuery(sql);
//		return rs;
//				
//	}
	public static int executeUpdate(String sql) throws Exception{
		Connection cn=null;
		cn=getConnection();
		Statement st=cn.createStatement();
		int num= st.executeUpdate(sql);
		st.close();
		st = null;
		cn.close();
		cn = null;
		
		return num;
	}
	public static void changeDatabase(String databaseName) throws Exception{
		Connection cn=null;
		cn=getConnection();
		cn.setCatalog(databaseName);
		cn.close();
		cn = null;
	}
}
