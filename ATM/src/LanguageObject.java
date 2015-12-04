import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * A language object that contains  different messages based
 * on the type of message in a specific language.
 * TODO: ability to add one field only to the language file.
 * @author Artem Los
 * 
 */
public class LanguageObject {

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
	}
	
	
	/**
	 * Add a new language statement. For example,
	 * name = "welcome", statement = "Welcome to the Bank".
	 * @param name A short description of the statement
	 * @param statement The sentence associated with that description.
	 */
	public void AddStatement(String  name, String statement) {
		statements.put(name, statement);
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
		
		return array;
	}
}
