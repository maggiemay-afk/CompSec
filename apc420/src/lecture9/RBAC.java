package lecture9;
 
import java.util.ArrayList; 
import java.util.List;
import java.util.Scanner; 

/*
 * A role contains a list of permission objects
 *                 a list of subjects (as strings)
 *                 a list of parents (which roles does this role inherits from)
 *                 a role name
 */
class Role {
	
	List<Role> parents = new ArrayList<>();
	List<Permission> permissions = new ArrayList<>();
	List<String> subjects = new ArrayList<>();
	String name;
	
	// a role must have a name
	Role(String name) {
		this.name = name;
	}
	// add parent role and return 'this' object (to allow method chaining)
	Role addParent(Role parent) {
		parents.add(parent);
		return parent;
	}
	
	// add permission object and return 'this' object
	Role addPermission(Permission permission) {
		permissions.add(permission);
		
	}
	// add subject (so that the 'subject' will have this role) and return 'this' object
	Role addSubject(String subject) {
		subjects.add(subject);
	}
	
	// check whether this role has the permission that grants access 'rights' to 'object'
	boolean checkPermission(String object, String rights) {
		// TODO
	}
	
	// check whether 'subject' has the access 'rights' to 'objects'
	boolean checkPermission(String subject, String object, String rights) {
		// TODO
	}
	
	// return a string representation of this role (need to include all information)
	public String toString() {
		// TODO
	}
}

/*
 * permission object specifies the access 'rights' to an 'object'
 * both 'rights' and 'object' are strings
 */
class Permission {
	final String object;
	final String rights;
	
	Permission(String object, String rights) {
		this.object = object;
		this.rights = rights;
	}
	// return true if 'object' is the 'object' in this permission and 'rights' is a substring of the 'rights' in this permission
	boolean checkPermission(String object, String rights) {
		return this.object.equals(object) && this.rights.indexOf(rights) >= 0;
	}
	// return string representation of this permission object
	public String toString() {
		return rights + " " + object;
	}
}
  
/*
 * represents a RBAC policy 
 */
public class RBAC {
	// stores all roles in a list
	List<Role> roles = new ArrayList<Role>(); 
	
	// add a new role to the list
	RBAC addRole(Role role) {
		roles.add(role);
		return this;
	}
	// check whether 'subject' has the access 'rights' to 'object'
	boolean checkPermission(String subject, String object, String rights) {
		// TODO
	}
	// return string representation of the RBAC policy	
	public String toString() {
		String ret = "";
		for(Role r: roles) {
			ret = ret + r + "\n";
		}
		return ret;
	}
	
	public static void main(String[] args) {
		RBAC rbac = new RBAC();
		
		Role employee = new Role("employee")
			.addSubject("alice")
			.addSubject("bob")
			.addPermission(new Permission("public", "rw"))
			.addPermission(new Permission("secret", "r"));
		Role manager = new Role("manager")
			.addSubject("carol")
			.addPermission(new Permission("secret", "w"))
			.addPermission(new Permission("top-secret", "r"))
			.addParent(employee);
		Role ceo = new Role("ceo")
			.addSubject("eve")
			.addPermission(new Permission("top-secret", "w"))
			.addParent(manager);

		rbac.addRole(employee).addRole(manager).addRole(ceo);
		
		System.out.println(rbac);
		
		Scanner in = new Scanner(System.in);

		System.out.println("Enter your command (e.g. alice r secret):");
		String cmd = in.nextLine().trim(); 

		while(!cmd.equals("")) {
			String[] triple = cmd.split(" ");
			if(triple.length != 3) {
				System.out.println("Illegal command. Try again");
			}
			else {
				String subj = triple[0], op = triple[1], obj = triple[2];
				try {
					if(rbac.checkPermission(subj, obj, op)) {
						System.out.printf("%s is allowed to perform %s operation on %s\n", subj, op, obj);
					}
					else {
						System.out.printf("%s is NOT allowed to perform %s operation on %s\n", subj, op, obj);
					}
				} catch(Exception e) { System.out.println(e); }
			}
			System.out.println("\nEnter your command");
			cmd = in.nextLine().trim();
		}

		in.close();
	}
}






