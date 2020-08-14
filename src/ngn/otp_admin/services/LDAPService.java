package ngn.otp_admin.services;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.vaadin.ui.Label;

import ngn.otp_admin.beans.OTPBean;
import ngn.otp_admin.utils.OTPProps;

public class LDAPService{
	
	
	public static  boolean ldapAuthentication(String ldapURL,String username, String password){
		
//		OTPProps props=new OTPProps();
		String serverURL=OTPProps.getProperty("ldap.url");
		String securityBase=OTPProps.getProperty("ldap.base");
		username="uid="+username+","+securityBase;
		System.out.println("user name: "+username);
		DirContext ctx = null;
		boolean result = false;
		try {
			Hashtable<String,String> env=new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapURL);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put("com.sun.jndi.ldap.connect.timeout", "2000");
			env.put("com.sun.jndi.ldap.read.timeout", "1000");
			env.put(Context.SECURITY_PRINCIPAL,username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			//Create the initial context
			ctx=new InitialDirContext(env);
			result=ctx!=null;
			if(result) {
				try {
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		} catch (NamingException e) {
	
		} finally {
            if (ctx != null) {
                try {   
                    ctx.close();
                    System.out.println("Close InitialDirContext");
                }
                catch (NamingException ne) { }
            }
        }
		return result;

	}
	
	public static boolean ldapAuthenticationSample(String ldapURL, String username, String password) {
//		OTPProps props=new OTPProps();
		String serverURL=OTPProps.getProperty("ldap.url");
		String securityBase=OTPProps.getProperty("ldap.base");
		System.out.println("user name: "+username);
		DirContext ctx =null;
		boolean result = false;
		try {
			Hashtable<String,String> env=new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapURL);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put("com.sun.jndi.ldap.connect.timeout", "1000");
			env.put(Context.SECURITY_PRINCIPAL,username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			//Create the initial context
			ctx=new InitialDirContext(env);
			result=ctx!=null;
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
            if (ctx != null) {
                try {   
                    ctx.close();
                    System.out.println("Close InitialDirContext");
                }
                catch (NamingException ne) { }
            }
        }
		return result;

		
	}


	public static DirContext getDirContext(String serverURL,String securityBase, String username, String password){
		if(username.isEmpty()|| password.isEmpty())return null;
		try {
			String base =securityBase;
			//String dn = "cn=" + username + "," + base;
			Hashtable<String,String> env=new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, serverURL);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put(Context.SECURITY_PRINCIPAL,username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			
			//Create the initial context
			DirContext ctx=new InitialDirContext(env);
			return ctx;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static List<String> getListUser(String serverURL,String searchBase, String username, String password){
		System.out.println(searchBase);
		List<String> list=new ArrayList<String>();
		DirContext ctx=getDirContext(serverURL,searchBase,username,password);
		if(ctx==null)return list;
		NamingEnumeration result=null;
		SearchControls controls=new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setTimeLimit(0);
		controls.setCountLimit(0);
//		String filter ="(objectClass=*)";
		String filter ="(objectClass=zimbraAccount)";
		int count = 0;
		int i=0;
		try {
			System.out.println("searchBase: "+searchBase);
			System.out.println("filter: "+filter);
			result=ctx.search(searchBase, filter ,controls);
			
			while(result.hasMore()){
				count++;
				SearchResult searchResult=(SearchResult)result.next();
				Attributes attributes=searchResult.getAttributes();
				
				try{
//					System.out.println(attributes.toString());
//					System.out.println(attributes.get("organization").get().toString());
					list.add(attributes.get("uid").get().toString());
				}catch(Exception e){
					
				}
			}
			System.out.println("====number of record: "+count);
			
			ctx.close();
			return list;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				ctx.close();
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return list;

		}
	}
	
	public static List<OTPBean> getListUserBean(String serverURL, String searchBase, String username, String password){
		List<OTPBean> list = new ArrayList<OTPBean>();
		DirContext ctx=getDirContext(serverURL,searchBase,username,password);
		if(ctx==null)return list;
		NamingEnumeration result=null;
		SearchControls controls=new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//		String filter = "(objectClass=zimbraAccount)";
		String filter ="*";
		int i=0;
		try {
			result=ctx.search(searchBase, filter ,controls);
			while(result.hasMore()){
				OTPBean bean = new OTPBean();
				SearchResult searchResult=(SearchResult)result.next();
				Attributes attributes=searchResult.getAttributes();
				try{
					System.out.println(attributes.get("uid").get().toString());
					bean.setUserId(new Label(attributes.get("uid").get().toString()));
					list.add(bean);
						
				}catch(Exception e){
					
				}
			}
			
			ctx.close();
			return list;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				ctx.close();
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return list;

		}
		
	}
	
	public static String getUserIdByEmail(String email) {
		
		if(email.contains("@")) {
			email = email.split("@")[0];
		}
		email=email.concat("@*");
		DirContext ctx = null;
		String serverURL = OTPProps.getProperty("ldap.url");
		String searchBase = OTPProps.getProperty("ldap.base");
		String username = OTPProps.getProperty("ldap.principal");
		String password = OTPProps.getProperty("ldap.credential");
		String output ="";
		try {
			Hashtable<String,String> env=new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, serverURL);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put(Context.SECURITY_PRINCIPAL,username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			
			//Create the initial context
			ctx=new InitialDirContext(env);
			SearchControls controls=new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//			String filter ="(objectClass=zimbraAccount)";
			String filter = "(&(objectClass=zimbraAccount)(mail="+email+"))";
			NamingEnumeration result= ctx.search(searchBase, filter ,controls);
			while(result.hasMore()){
				SearchResult searchResult=(SearchResult)result.next();
				Attributes attributes=searchResult.getAttributes();
				try{
					System.out.println(attributes.toString());
					System.out.println(attributes.get("uid").get().toString());
					output= attributes.get("uid").get().toString();
				}catch(Exception e){
					
				}
			}
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
		
		}finally {
			if(ctx != null) {
				try {
					ctx.close();
					System.out.println("DirContext close!");
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return output;
	}
	
	public static String getUserInfo(String userid) {
		
		DirContext ctx = null;
		String serverURL = OTPProps.getProperty("ldap.url");
		String searchBase = OTPProps.getProperty("ldap.base");
		String adminUsername = "uid=zimbra,cn=admins,cn=zimbra";
		String adminPassword = OTPLocalServiceUtil.getZimbraLdapKey();
		String output ="";
		try {
			Hashtable<String,String> env=new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, serverURL);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put(Context.SECURITY_PRINCIPAL,adminUsername);
			env.put(Context.SECURITY_CREDENTIALS, adminPassword);
			
			//Create the initial context
			ctx=new InitialDirContext(env);
			SearchControls controls=new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//			String filter ="(objectClass=zimbraAccount)";
			String filter = "(&(objectClass=zimbraAccount)(uid="+userid+"))";
			NamingEnumeration result= ctx.search(searchBase, filter ,controls);
			while(result.hasMore()){
				SearchResult searchResult=(SearchResult)result.next();
				Attributes attributes=searchResult.getAttributes();
				try{
					System.out.println(attributes.toString());
					System.out.println(attributes.get("zimbraaccountstatus").get().toString());
					output= attributes.get("zimbraaccountstatus").get().toString();
				}catch(Exception e){
					
				}
			}
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
		
		}finally {
			if(ctx != null) {
				try {
					ctx.close();
					System.out.println("DirContext close!");
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return output;
	}
	
	public static boolean changePass(String user, String password) {
		String serverURL=OTPProps.getProperty("ldap.url");
		serverURL = serverURL.split("://")[1];
		System.out.println(serverURL);
		String adminUsername = "uid=zimbra,cn=admins,cn=zimbra";
		String adminPassword = OTPLocalServiceUtil.getZimbraLdapKey();
		String securityBase=OTPProps.getProperty("ldap.base");
		String serverName = serverURL.split(":")[0].toString();
		int port = Integer.valueOf(serverURL.split(":")[1].toString());
		System.out.println(serverName+" - "+port);
		LDAPConnection connection = null;
		final String userDN;
		try {
		    connection = new LDAPConnection(serverName, port);  
		    BindResult auth = connection.bind(adminUsername,adminPassword);
		    // The password is replaced with the new value
	        Modification modification = new Modification(
	                ModificationType.REPLACE,
	                "userPassword",
	                password
	        );
	        // The DN of the user
	        userDN = "uid="+user+","+securityBase;
	        // Build the modification request
	        ModifyRequest modifyRequest = new ModifyRequest(
	                userDN,
	                modification
	        );
	        // Execute the modification
	        connection.modify(modifyRequest);
	        connection.close();

		} catch (LDAPException e) {
		    System.out.println(e);
		    connection.close();
		    return false;
		} 
		return true;
	}


}
