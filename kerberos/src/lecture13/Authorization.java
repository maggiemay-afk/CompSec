package lecture13;
 
import javax.security.auth.Subject; 
import javax.security.auth.login.*;

import lecture12.PlaintextCallbackHandler;

import java.security.PrivilegedAction;

/**
 * This class is adapted from 
 * 		https://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/tutorials/GeneralAcnAndAzn.html
 * 
 * This class attempts to authenticate a user using Kerberos authentication service.
 * If successful, it then run an Action object where access control checks for security-sensitive operations 
 *                will be based on the permissions of the subject that runs the code.
 */
public class Authorization {

	/*
	 * Set system properties for JAAS configuration file location, Kerberos realm, and KDC server.
	 */
	static void setProperties() {  
		System.setProperty("java.security.krb5.realm", "CS.UWM.EDU");
		System.setProperty("java.security.krb5.kdc", "localhost");
		System.setProperty("java.security.auth.login.config", "src/lecture13/JAAS.config");
	}
	
    public static void main(String[] args) {

        // 1. Obtain a LoginContext needed for authentication. 
    	//    Tell it to use the LoginModule implementation specified by the
        //    entry named "kerberos" in the JAAS login configuration file and 
    	//    to also use the PlaintextCallbackHandler from lecture 12
    	// 2. attempt authentication (you must stop the program if an exception is thrown)
    	
    	setProperties();
    	LoginContext lc = null;
    	
    	try {
			lc = new LoginContext("kerberos", new PlaintextCallbackHandler());
			lc.login();
			
    	} catch (LoginException le) {
			System.out.println("Login Exception - Authentication Failed\n" + le.getMessage());
		}
        System.out.println("Authentication succeeded!");

        
        // 3. now try to execute the Action on behalf of the authenticated Subject
        try {
        	
	        Subject subject = lc.getSubject();
	        PrivilegedAction <Object> action = new Action();
	        Subject.doAsPrivileged(subject, action, null);       	
        
        } catch(NullPointerException npe) {
        	System.out.println("Null Pointer Exception\n" + npe.getMessage());
        } catch(SecurityException se) {
        	System.out.println("Security Exception\n" + se.getMessage());
        }

    }
}
