package in.ultraneo.farecalculator9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class UltraFileaccess {
	@SuppressWarnings("static-access")
	public void Load_Data(String filename, String Datastring, Context context) {
		FileOutputStream fos;

		try {
			fos = context.openFileOutput(filename, context.MODE_PRIVATE);
			fos.write(Datastring.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

		} catch (IOException e) {
			// TODO Auto-generated catch block

		}

	}
	
	public String get_Data(String filename,Context context)
	{
		FileInputStream f = null;
		String temp="";
		try {
		f= context.openFileInput(filename);
		int n = f.available();
		for(int i=0;i<n;i++)
		{
			temp=temp+(char)f.read();
		}
		f.close();
		}catch (IOException e)
		{
			
		}
		return temp;
	}

}
