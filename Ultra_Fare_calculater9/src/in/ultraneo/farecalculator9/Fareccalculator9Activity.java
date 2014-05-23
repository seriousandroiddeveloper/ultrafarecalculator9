package in.ultraneo.farecalculator9;








import java.net.URL;
import java.net.URLConnection;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fareccalculator9Activity extends Activity {
	Button btn1;
	
	EditText edittxt1,edittxt2;
	CustomizeDialog custo1;
	Location CURRENT_LOCATION;
	Boolean current_location=false,SOURCE=false,DESTINATION=false;
	String LAT1="",LON1,LAT2="",LON2;
	

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Debug.startMethodTracing("traceName");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn1=(Button)findViewById(R.id.button1);
       // btn2 = (ImageButton)findViewById(R.id.imageButton1);
        //btn3 =(ImageButton)findViewById(R.id.imageButton2);
        edittxt1 = (EditText)findViewById(R.id.editText1);
        edittxt2 = (EditText)findViewById(R.id.editText2);
        edittxt1.setKeyListener(null);
        edittxt1.setLongClickable(false);
        edittxt2.setKeyListener(null);
        edittxt2.setLongClickable(false);
        String [] filelist = fileList();
		int nooffile = filelist.length;
		if(nooffile==0)
		{
			new UltraFileaccess().Load_Data("FIRSTTIME","0",this);
			new UltraFileaccess().Load_Data("ZOOM","12",this);	
       new UltraFileaccess().Load_Data("edittxt1a","0",this);
       new UltraFileaccess().Load_Data("edittxt1l","0",this);
       
        new UltraFileaccess().Load_Data("edittxt2a","0",this);
        new UltraFileaccess().Load_Data("edittxt2l","0",this);
		}
		 String firsttime = new UltraFileaccess().get_Data("FIRSTTIME", this);
	        if(firsttime.equals("0"))
	        {
	        	startActivity(new Intent(this, SwipeTest.class));
	        	
	        }
		String temp= new UltraFileaccess().get_Data("edittxt1a", this);
		String temp2 = new UltraFileaccess().get_Data("edittxt1l", this);
		if(temp.equals("0"))
		{
			
		}else {
		edittxt1.setText(temp+","+temp2);
		LAT1=temp;
		LON1=temp2;
		//new UltraFileaccess().Load_Data("edittxt1a","0",this);
		//new UltraFileaccess().Load_Data("edittxt1l","0",this);
		}
		
		
		String temp3= new UltraFileaccess().get_Data("edittxt2a", this);
		String temp4 = new UltraFileaccess().get_Data("edittxt2l", this);
		if(temp3.equals("0"))
		{
			
		}else {
		edittxt2.setText(temp3+","+temp4);

		LAT2=temp3;
		LON2=temp4;
		//new UltraFileaccess().Load_Data("edittxt2a","0",this);
		//new UltraFileaccess().Load_Data("edittxt2l","0",this);
		}
        
		
//		if(LAT1.equals(""))
//		{
//			
//		}
//		else
//		{
//			edittxt1.setText(LAT1+","+LON1);
//		}
//		if(LAT2.equals(""))
//		{
//			
//		}
//		else
//		{
//			edittxt2.setText(LAT2+","+LON2);
//		}
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            	
				CURRENT_LOCATION = location;//String.valueOf(Latitude);
				current_location=true;
				
            
             
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {
            	current_location= false;
            	
            }
            
			
			
          };
        
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        
        
        edittxt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final CharSequence[] items = {"Current Location", "Point on Map", "Search place","Clear"};

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle(null);
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
				        if(item==0)
				        {
				        	if(current_location)
				        	{
				        		double Latitude = CURRENT_LOCATION.getLatitude();
								double Longitude = CURRENT_LOCATION.getLongitude();
								LAT1= Double.toString(Latitude);
								LON1=Double.toString(Longitude);
								SOURCE= true;
								new UltraFileaccess().Load_Data("edittxt1a",LAT1,Fareccalculator9Activity.this);
							       new UltraFileaccess().Load_Data("edittxt1l",LON1,Fareccalculator9Activity.this);
								
				        	edittxt1.setText("Current Location");
				        	}else
				        	{
				        		

				    			new AlertDialog.Builder(Fareccalculator9Activity.this)
				    			.setTitle("Enable Wireless Location Provider").setMessage("Please enable Wireless networks provider to get approx current location, Please Yes to continue").setIcon(android.R.drawable.ic_menu_compass)
				    			.setPositiveButton("Yes",
				    					          new DialogInterface.OnClickListener() {
				    					          public void onClick(DialogInterface dialog, int which) {
				    					        	  Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				    									startActivity(i);  
				    					           }
				    					           })
				    					           .setNegativeButton("No",
				    					          new DialogInterface.OnClickListener() {
				    					          public void onClick(DialogInterface dialog, int which) {
				    					        		
				    					        	  
				    					           }
				    					           }).show();
				        		
				        		
				        	}
				        	
				        }
				        if(item==1){
				        	
							
					      	
					      		
							startActivity(new Intent(getApplicationContext(),UltralocatorActivity.class).putExtra("pos","0"));
					      	
				        	
				        }
				        if(item==2) {
				        	Bundle bundle = new Bundle();
							
					      	 bundle.putString("search","#");
					      	bundle.putString("pos","0");
					      	 
					      	Intent i = new Intent(getApplicationContext(),searchlisters.class);
							 i.putExtras(bundle);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							startActivity(i);
				        	
				        	
				        }
				        if(item==3) {
				        	new UltraFileaccess().Load_Data("edittxt1a","0",Fareccalculator9Activity.this);
						       new UltraFileaccess().Load_Data("edittxt1l","0",Fareccalculator9Activity.this);
						       edittxt1.setText("");
				        }
				        
				    }
				});
				AlertDialog alert = builder.create();	
				alert.show();
			}
			});
        edittxt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final CharSequence[] items = {"Point on Map", "Search place","Clear"};

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle(null);
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				       // Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
				        if(item==0)
				        {

				        	startActivity(new Intent(getApplicationContext(),UltralocatorActivity.class).putExtra("pos","1"));	
				        	
				        	
				        }
				        if(item==1) {
				        	Bundle bundle = new Bundle();
							
					      	 bundle.putString("search","#");
					      	bundle.putString("pos","1");
					      	 
					      	Intent i = new Intent(getApplicationContext(),searchlisters.class);
							 i.putExtras(bundle);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							startActivity(i);
				        	
				        	
				        }
				        
				        if(item==2) {
				        	new UltraFileaccess().Load_Data("edittxt2a","0",Fareccalculator9Activity.this);
						       new UltraFileaccess().Load_Data("edittxt2l","0",Fareccalculator9Activity.this);
						       edittxt2.setText("");
				        }
				        
				        
				        
				    }
				});
				AlertDialog alert = builder.create();	
				alert.show();
				
			}
		});
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//////////////////
				
			
			if(edittxt1.getText().toString().equals(""))
			{
				if(edittxt2.getText().toString().equals(""))
				{
					Toast.makeText(Fareccalculator9Activity.this,"You forget to set source and destination",Toast.LENGTH_SHORT).show();	
				}else {
					Toast.makeText(Fareccalculator9Activity.this,"You forget to set source",Toast.LENGTH_SHORT).show();	
				}
				
			}else
			{
				if(edittxt2.getText().toString().equals(""))
				{
					Toast.makeText(Fareccalculator9Activity.this,"You forget to set destination",Toast.LENGTH_SHORT).show();	
				}
				else
					
				{
					btn1.setEnabled(false);
					boolean connectivity;
					try {
						
					    URL url = new URL("http://www.google.com/");
					    URLConnection conn = url.openConnection();
					    conn.connect();
					    connectivity = true;
					} catch (Exception e) {
					    connectivity = false;
					}
					if(connectivity) {
					String coordinates[] = {LAT1, LON1};
			    	double lat = Double.parseDouble(coordinates[0]);
			    	double lng = Double.parseDouble(coordinates[1]);
			    	GeoPoint p1 = new GeoPoint(
			    	(int) (lat * 1E6),
			    	(int) (lng * 1E6));
			    	
			    	String coordinates2[] = {LAT2, LON2};
			    	double lat2 = Double.parseDouble(coordinates2[0]);
			    	double lng2 = Double.parseDouble(coordinates2[1]);
			    	GeoPoint p2 = new GeoPoint(
			    	(int) (lat2 * 1E6),
			    	(int) (lng2 * 1E6));
					
			    	Double g = CalculationByDistance(p1, p2);
			    	
			    	
			    	
			    	//Toast.makeText(getApplicationContext(), String.valueOf(g),Toast.LENGTH_SHORT).show();
					if(g<400.00)
					{
						
						
						
						
						Bundle bundle = new Bundle();
				       	 bundle.putString("Lat1",new UltraFileaccess().get_Data("edittxt1a", Fareccalculator9Activity.this));
				       	 bundle.putString("Lon1",new UltraFileaccess().get_Data("edittxt1l", Fareccalculator9Activity.this));
				       	 bundle.putString("Lat2",new UltraFileaccess().get_Data("edittxt2a", Fareccalculator9Activity.this));
				       	 bundle.putString("Lon2",new UltraFileaccess().get_Data("edittxt2l", Fareccalculator9Activity.this));
				       	 
				       	new UltraFileaccess().Load_Data("edittxt1a","0",Fareccalculator9Activity.this);
				         new UltraFileaccess().Load_Data("edittxt1l","0",Fareccalculator9Activity.this);
				         
				          new UltraFileaccess().Load_Data("edittxt2a","0",Fareccalculator9Activity.this);
				          new UltraFileaccess().Load_Data("edittxt2l","0",Fareccalculator9Activity.this);
				      
				       	 
						Intent i = new Intent(v.getContext(),MapRouteActivity.class);

					 i.putExtras(bundle);
						startActivity(i);
						
						
					}
					else{
						
						new AlertDialog.Builder(v.getContext())
						.setTitle("Lol...").setMessage("You need train or Aeroplane for such Long distance ("+String.format("%.2f",g)+"Km)").setIcon(R.drawable.fareicon)
						
								           .setNegativeButton("OK",
								          new DialogInterface.OnClickListener() {
								          public void onClick(DialogInterface dialog, int which) {
								        		
								        	  
								           }
								           }).show();

						new UltraFileaccess().Load_Data("edittxt1a","0",Fareccalculator9Activity.this);
				         new UltraFileaccess().Load_Data("edittxt1l","0",Fareccalculator9Activity.this);
				         
				          new UltraFileaccess().Load_Data("edittxt2a","0",Fareccalculator9Activity.this);
				          new UltraFileaccess().Load_Data("edittxt2l","0",Fareccalculator9Activity.this);
				          edittxt1.setText("");
				          edittxt2.setText("");
						
					}
					
					
					
					
				}else
				{
					Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
				}
					
					
				}
			
				
			
			}
			
							
			}
		});
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.item1: 
        {
        	startActivity(new Intent(Fareccalculator9Activity.this, setting.class));
        }
            
            return true;
        case R.id.item2: 
        {
        	startActivity(new Intent(Fareccalculator9Activity.this, SwipeTest.class));
        }
                    return true;
        case R.id.item3: 
        {
        	in.ultraneo.farecalculator9.CustomizeDialog about = new CustomizeDialog(Fareccalculator9Activity.this,R.layout.about9);
            TextView abouttxt = (TextView)about.findViewById(R.id.widget66);
           // aboutbtn = (Button)about.findViewById(R.id.widget44);
            abouttxt.setText(R.string.about);
            about.show();

        	
        	
        }
                    return true;            
                    
        
    default:
        return super.onOptionsItemSelected(item);
    }
}
    
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent); 

        return;
    } 
    public double CalculationByDistance(GeoPoint StartP, GeoPoint EndP) {
        double lat1 = StartP.getLatitudeE6()/1E6;
        double lat2 = EndP.getLatitudeE6()/1E6;
        double lon1 = StartP.getLongitudeE6()/1E6;
        double lon2 = EndP.getLongitudeE6()/1E6;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
           Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6371 * c;
     }
	@Override
	protected void onDestroy() {
		Debug.stopMethodTracing();
		super.onDestroy();
	}
    
    
}