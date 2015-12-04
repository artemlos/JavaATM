package Tests;
import static org.junit.Assert.*;
import org.junit.Test;
import ATM.*;
import ATM.LanguageObject.LanguageType;

public class TestLanguageObject {

	@Test
	public void Serialize() {
		LanguageObject lang = new LanguageObject(LanguageType.English, 1);
		lang.AddStatement("welcome","Welcome to the Bank!");
		
		byte[] serialized = lang.Serialize();
		
		LanguageObject lang2 = new LanguageObject(serialized);
		
		assertEquals(lang.getVersion(), lang2.getVersion());
		assertEquals(lang.getLanguage(), lang2.getLanguage());
		
		assertTrue(lang.GetStatement("welcome").equals("Welcome to the Bank!"));
		
		assertEquals(lang.GetStatement("welcome"), lang2.GetStatement("welcome"));
	}

}
