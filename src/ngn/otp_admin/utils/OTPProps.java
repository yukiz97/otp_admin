package ngn.otp_admin.utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.SystemUtils;
public class OTPProps implements ServletContextListener{
	static Properties prop=new Properties();
	static String fileName="/opt/otp/config/otp_auth.properties";
	
	public static String getProperty(String key){
		return prop.getProperty(key,"");
	}
	public static void setProperty(String key, String value){
		prop.setProperty(key, value);
	}
	
	public static boolean saveProperty(){
		
		try {
//			String path = this.getClass().getClassLoader().getResource("").getPath();
//			System.out.println(path);
			OutputStream out;
			out = new FileOutputStream(fileName);
			
			prop.store(out, "OTP configuration");
			out.close();
			System.out.println("ok");
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Start OTPProps");
		try {
			InputStream inputStream = null;
			if(SystemUtils.IS_OS_WINDOWS) {
				inputStream=getClass().getClassLoader().getResourceAsStream("otp_auth.properties");
			} else {
				inputStream = new FileInputStream(fileName);
			}
			prop.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
//			prop.load(inputStream);
			inputStream.close();
			System.out.println("OTPProps start OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
