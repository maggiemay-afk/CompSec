package lecture11;
 
import java.io.File;
import org.apache.kerby.kerberos.kerb.client.KrbClient;
import org.apache.kerby.kerberos.kerb.type.ticket.SgtTicket;
import org.apache.kerby.kerberos.kerb.type.ticket.TgtTicket;

/*
 * Kerberos client. Run it after the KDC server is started.
 */
public class Client {
	public static void main (String[] args) { 
		try {
			String basedir = System.getProperty("basedir");
			if (basedir == null) {
				basedir = new File(".").getCanonicalPath();
			}
			
			// create a Kerberos client using the saved configuration file.
			KrbClient client = new KrbClient(new File(basedir + "/target/krb5.conf"));
 
			/*
			 * Get a Ticket Granting Ticket (TGT) from the server and 
			 *    print the client principal and the realm in the ticket
			 *
			 * Get a service ticket from the server using the TGT and 
			 *    print the service principal and the realm within the ticket
			 *
			 */
			client.init();
			
			TgtTicket tgt = client.requestTgt("alice@CS.UWM.EDU", "Alice's password");
			System.out.println(tgt.getClientPrincipal());
			System.out.println(tgt.getRealm());
			
			SgtTicket tkt = client.requestSgt(tgt, "bob/SERVICE@CS.UWM.EDU");
			System.out.println("bob/SERVICE"); //Couldn't find a getServerPrincipal or equivalent method
			System.out.println(tkt.getRealm());
			
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
