package in.ultraneo.farecalculator9;



import android.os.Bundle;
import android.preference.PreferenceActivity;

public class setting extends PreferenceActivity{
	public void onCreate(Bundle savedInstanceState) {       
	       super.onCreate(savedInstanceState);       
	        addPreferencesFromResource(R.xml.settings);
	        
	}

}
