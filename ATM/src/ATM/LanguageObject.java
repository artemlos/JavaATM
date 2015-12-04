package ATM;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * A language object that contains  different messages based
 * on the type of message in a specific language.
 * TODO: ability to add one field only to the language file.
 * @author Artem Los
 * 
 */
public class LanguageObject implements Serializable{

	public enum LanguageType {
		English, Swedish, Russian, German 
	}
	
	LanguageType language;
	int version;
	
	HashMap<String, String> statements;  
	
	public LanguageType getLanguage() {
		return language;
	}
	
	/**
	 * The version of the current language file.
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Creates a new instance of the object from parameters.
	 */
	public LanguageObject (LanguageType language, int version) {
		this.language = language;
		this.version = version;
		this.statements = new HashMap<>();
	}
	
	/**
	 * Creates a new instance of the object from a byte array.
	 * @param array
	 */
	public LanguageObject (byte[] array) {
		ByteArrayInputStream out = new ByteArrayInputStream(array);
		ObjectInputStream os = null;
		
		LanguageObject obj = null;
		
		try{
			os = new ObjectInputStream(out);
			obj = (LanguageObject)os.readObject();
		}
		catch (Exception e)
		{}
		finally
		{
			try {
				if(os != null)
					os.close();
			} catch (IOException e){}
			
			try {
				out.close();
			} catch(IOException e) {}
		}
		
		this.language = obj.language;
		this.version = obj.version;
		this.statements = obj.statements;
		
		if (statements == null) {
			this.statements = new HashMap<>();
		}
	}
	
	
	/**
	 * Add a new language statement. For example,
	 * name = "welcome", statement = "Welcome to the Bank".
	 * @param name A short description of the statement
	 * @param statement The sentence associated with that description.
	 */
	public void AddStatement(String name, String statement) {
		statements.put(name, statement);
	}
	
	/**
	 * Gets a statement given the short description.
	 * name = "welcome" returns "Welcome to the Bank".
	 * @param name A short description of the statement
	 */
	public String GetStatement(String name) {
		if (statements.containsKey(name))
			return statements.get(name);
		return null;
	}
	
	/**
	 * Converts a Language object to a byte array.
	 * @return A byte array reprensenting the object or null (in case of error)
	 */
	public byte[] Serialize() {
		byte[] array = null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try{
			os = new ObjectOutputStream(out);
			os.writeObject(this);
			array = out.toByteArray();
		}
		catch (Exception e)
		{System.out.println(e);}
		finally
		{
			try {
				if(os != null)
					os.close();
			} catch (IOException e){}
			
			try {
				out.close();
			} catch(IOException e) {}
		}
		
		return array;
	}
}
