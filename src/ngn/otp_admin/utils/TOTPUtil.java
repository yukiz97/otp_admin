package ngn.otp_admin.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ngn.otp_admin.models.OTPModel;
import ngn.otp_admin.services.OTPLocalServiceUtil;
import ngn.otp_admin.utils.OTPProps;

import org.apache.commons.codec.binary.Base32;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.vaadin.server.VaadinService;

public class TOTPUtil {

	public static String getQRBarcodeURL(
			String user,
			String host,
			String secret) {
		String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
		return String.format(format, user, host, secret);
	}

	public static String generateSecret() {
		byte[] buffer = new byte[10];
		new SecureRandom().nextBytes(buffer);
		return new String(new Base32().encode(buffer));
		//return new String(buffer);
	}
	public static long getTimeIndex() {
		return System.currentTimeMillis()/1000/60;
		//return System.currentTimeMillis();
	}

	//get generated code 
	public static String getCode(String seed, String timeIndex)
			throws NoSuchAlgorithmException, InvalidKeyException {

		byte[] msg=hexStr2Bytes(seed);
		byte[] timeBytes=hexStr2Bytes(timeIndex);
		SecretKeySpec signKey = new SecretKeySpec(msg, "HmacSHA1");
		//		byte[] timeBytes=String.valueOf(timeIndex).getBytes();

		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(timeBytes);
		int offset = hash[hash.length-1] & 0xf;
		int binary =
				((hash[offset] & 0x7f) << 24) |
				((hash[offset + 1] & 0xff) << 16) |
				((hash[offset + 2] & 0xff) << 8) |
				(hash[offset + 3] & 0xff);

		int otp = binary % 1000000;
		String result = Integer.toString(otp);
		while (result.length() < 6) {
			result = "0" + result;
		}
		return result;
	}
	public static byte[] hexStr2Bytes(String hex){
		// Adding one byte to get the right conversion
		// Values starting with "0" can be converted
		byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

		// Copy all the REAL bytes, not the "first"
		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = bArray[i+1];
		return ret;
	}

	public static String getSteps(int delay){
		String steps = "0";
		Calendar m = Calendar.getInstance(); 
		m.setTime(new Date());   
		m.add(Calendar.MINUTE, delay); 

		long T1= m.getTimeInMillis()/1000;
		long T = T1 / 60;
		steps = Long.toHexString(T).toUpperCase();
		//System.out.println(steps);
		while (steps.length() < 16)
			steps = "0" + steps;
		return steps;
	}
	public static String getUserPrivateKey(String userId){
		System.out.println("get user private key for: "+userId);
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
	public static String getDeviceName(String s){
		String deviceName=s;
		int start=deviceName.indexOf('(')+1;
		int end=deviceName.indexOf(')');
		deviceName=deviceName.substring(start,end);
		deviceName=deviceName.replace(';', ' ');
		return deviceName;
	}
	public static void sendingEmail(String userId){
		String header=VaadinService.getCurrentRequest().getHeader("User-Agent");
		String deviceName=getDeviceName(header);
		//		OTPProps props=new OTPProps();
		String mailTo=userId;
		mailTo=mailTo.split("@")[0];
		mailTo+="@"+OTPProps.getProperty("mail.smtp.domain");
		String content="<table><tr><td><b>User</b></td><td><b>Device<b></td><td><b>Timestamp</b></td></tr>";
		content+="<tr><td>"+userId.split("@")[0]+"</td><td>"+deviceName+"</td><td>"+new Date()+"</td></tr></table>";
		//		SendEMail.sendEMail(mailTo, content);
	}


	public static boolean checkOTP(String privateKey, String otpCode) {

		boolean result = checkOTP_1(privateKey, otpCode);
		if(result == true) {
			return result;
		}
		else {
			result = checkOTP_2(privateKey, otpCode);
		}
		return result;
	}

	public static boolean checkOTP_1(String privateKey, String otpCode) {

		if(privateKey.trim().isEmpty() || otpCode.trim().isEmpty()) return false;

		try {
			//check for totp code
			String t;
			int window = 1;
			String code;
			boolean match=false;
			for (int i = -window; i <= window; ++i) {
				System.out.println(i);
				code=TOTPUtil.getCode(privateKey,TOTPUtil.getSteps(i));
				System.out.println(code);
				if(code.equalsIgnoreCase(otpCode)){
					match=true;
					break;
				}
			}
			if(match==true){
				return true;
			}else return false;
		}catch(Exception e) {
			return false;
		}
	}
	
	public static boolean checkOTP_2(String privateKey, String otpCode) {
		
		if(privateKey.trim().isEmpty()) return false;
		
		TimeBasedOneTimePasswordGenerator totp;
		SecretKey secretKey;
		int codeLenght = 6;
		try {
			privateKey = privateKey.substring(0,32).toUpperCase();
		}catch(Exception e) {
			
		}
		
		privateKey = privateKey.replace('1', 'A');
		privateKey = privateKey.replace('2', 'B');
		privateKey = privateKey.replace('3', 'C');
		privateKey = privateKey.replace('4', 'D');
		privateKey = privateKey.replace('5', 'E');
		privateKey = privateKey.replace('6', 'F');
		privateKey = privateKey.replace('7', 'G');
		privateKey = privateKey.replace('8', 'H');
		privateKey = privateKey.replace('9', 'I');
		privateKey = privateKey.replace('0', 'J');
		
		
		System.out.println("Check OTP_2 - "+privateKey);
		try {
			totp = new TimeBasedOneTimePasswordGenerator(30,TimeUnit.SECONDS, codeLenght);
			byte[] decodedKey  = new Base32().decode(privateKey);
			while(decodedKey.length < 20) {
				decodedKey = concat(decodedKey,decodedKey);
			}
			secretKey = new SecretKeySpec(decodedKey, 0, 20 , totp.getAlgorithm()); 
			final Date now = new Date();
			int password = totp.generateOneTimePassword(secretKey, now);
			System.out.println("original password: "+password);
			System.out.format("Current password: %06d\n", password);
			if(Integer.valueOf(otpCode)==password) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return false;
	}
	
	private static byte[] concat(byte[]...arrays)
	{
	    // Determine the length of the result array
	    int totalLength = 0;
	    for (int i = 0; i < arrays.length; i++)
	    {
	        totalLength += arrays[i].length;
	    }

	    // create the result array
	    byte[] result = new byte[totalLength];

	    // copy the source arrays into the result array
	    int currentIndex = 0;
	    for (int i = 0; i < arrays.length; i++)
	    {
	        System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
	        currentIndex += arrays[i].length;
	    }

	    return result;
	}

	public static String generatePrivateKey(int length) {
		Random RANDOM = new SecureRandom();
		String ALPHABET = "0123456789ABCDEF";
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}

}