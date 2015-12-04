package ATM;

/**
 * Some helper methods.
 * @author Artem Los
 *
 */
public class HelperMethods {
	
	public static int GetPortFromArgs (String[] args, int arg) {
    	if (args.length > 0) {
    		try {
    			return Integer.parseInt(args[arg]);
    		} catch (Exception e){
    			System.out.println("The port has to be a number, eg. 4711.");
    		}
    	} else {
    		System.out.println("The port has to be specified, eg. 4711.");
    	}
    	
    	return -1;
	}
}
