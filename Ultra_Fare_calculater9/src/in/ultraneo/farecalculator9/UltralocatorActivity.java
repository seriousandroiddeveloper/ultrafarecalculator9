package in.ultraneo.farecalculator9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;




	import java.util.List;

	import com.google.android.maps.GeoPoint;
	import com.google.android.maps.MapActivity;
	import com.google.android.maps.MapController;
	import com.google.android.maps.MapView;
	import com.google.android.maps.Overlay;




	
	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
	import android.graphics.Canvas;
	import android.graphics.Point;
	import android.os.Bundle;
	import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

	public class UltralocatorActivity extends MapActivity {
		MapView mapView;
		MapController mc;
		GeoPoint p1;
		Boolean status=false;
		Button btn1;
		
		LocationManager mlocManager;
		LocationListener mlocListener;
		String POS;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map9);
	        mapView = (MapView) findViewById(R.id.mapview1);  
	        btn1 = (Button)findViewById(R.id.button1);
	        
	       Bundle bundle = getIntent().getExtras();

	        
	       POS = bundle.getString("pos"); 
	    	//mapView.setBuiltInZoomControls(true);  
	    	//mapView.setSatellite(true);
	    	//mapView.setStreetView(true);
	    	//mapView.setTraffic(true);
	    	mc = mapView.getController();
	    	String coordinates[] ={"28.49766", "76.77246"};
	    	double lat = Double.parseDouble(coordinates[0]);
	    	double lng = Double.parseDouble(coordinates[1]);
	    	p1 = new GeoPoint(
	    	(int) (lat * 1E6),
	    	(int) (lng * 1E6));
	    	mc.animateTo(p1);
	    	String temp = new UltraFileaccess().get_Data("ZOOM",this);
	    	mc.setZoom(Integer.parseInt(temp));
	    	
	    	mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

	        mlocListener = new getLocationfromGPSin();
	        mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000,10, mlocListener);
	    	
	    	MapOverlay mapOverlay = new MapOverlay(p1);
	    	List<Overlay> listOfOverlays = mapView.getOverlays();
	    	
	    	listOfOverlays.clear();
	    	listOfOverlays.add(mapOverlay);
	    	mapView.invalidate();
	    	
	    	btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(status)
					{
						if(POS.equals("0"))
						{
							String lat = Double.toString(p1.getLatitudeE6()/1E6);
							String lon = Double.toString(p1.getLongitudeE6()/1E6);
							
							//Fareccalculator9Activity.LAT1=lat;
							//Fareccalculator9Activity.LON1=lon;
							
							
							
							//Toast.makeText(UltralocatorActivity.this,String.valueOf(p1.getLatitudeE6()/1E6),Toast.LENGTH_SHORT).show();
							new UltraFileaccess().Load_Data("edittxt1a",lat,v.getContext());
							new UltraFileaccess().Load_Data("edittxt1l",lon,v.getContext());	
						}
						if(POS.equals("1"))
						{
							String lat = Double.toString(p1.getLatitudeE6()/1E6);
							String lon = Double.toString(p1.getLongitudeE6()/1E6);
							
							//Fareccalculator9Activity.LAT2=lat;
							//Fareccalculator9Activity.LON2=lon;
							new UltraFileaccess().Load_Data("edittxt2a",lat,v.getContext());
							new UltraFileaccess().Load_Data("edittxt2l",lon,v.getContext());
							
						}
						Intent i = new Intent(v.getContext(), Fareccalculator9Activity.class);
						startActivity(i);
					}
					else
					{
						Toast.makeText(v.getContext(),"Please select any valid pointer", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	    	
	    }
	     
	    protected void onPause() {   
	        super.onPause(); 
	        mlocManager.removeUpdates(mlocListener);
	    }
	    public class getLocationfromGPSin implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {

				double Latitude = location.getLatitude()*1E6;
				double Longitude = location.getLongitude()*1E6;
				GeoPoint ptemp = new GeoPoint((int)Latitude, (int)Longitude);
				mc.animateTo(ptemp);
				//MapOverlay mapOverlay = new MapOverlay(ptemp);
				

			}

			@Override
			public void onProviderDisabled(String provider) {
				//btn1.setEnabled(true);
				Toast.makeText(getApplicationContext(),
						"GPS is Disabled Please Enable it", Toast.LENGTH_SHORT)
						.show();
				String temp= new UltraFileaccess().get_Data("edittxt1a", getApplicationContext());
				String temp3= new UltraFileaccess().get_Data("edittxt2a", getApplicationContext());
				if(temp.equals("0"))
				{
					if(temp3.equals("0")) {
						
					}else
					{
						String temp4 = new UltraFileaccess().get_Data("edittxt2l", getApplicationContext());
						String coordinates[] = {temp3,temp4};
				    	double lat = Double.parseDouble(coordinates[0]);
				    	double lng = Double.parseDouble(coordinates[1]);
				    	p1 = new GeoPoint(
				    	(int) (lat * 1E6),
				    	(int) (lng * 1E6));
				    	mc.animateTo(p1);
					}
				}else {
					String temp2 = new UltraFileaccess().get_Data("edittxt1l", getApplicationContext());
					String coordinates[] = {temp, temp2};
			    	double lat = Double.parseDouble(coordinates[0]);
			    	double lng = Double.parseDouble(coordinates[1]);
			    	p1 = new GeoPoint(
			    	(int) (lat * 1E6),
			    	(int) (lng * 1E6));
			    	mc.animateTo(p1);
				}
				
				
				

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

		}
	    
		@Override
		protected boolean isRouteDisplayed() {
			
			return false;
		}
		class MapOverlay extends com.google.android.maps.Overlay
		{
			public MapOverlay(GeoPoint temp_p1) {
				p1 = temp_p1;
				
			}
			
		@Override
		public boolean draw(Canvas canvas, MapView mapView,
				boolean shadow, long when)
				{
				super.draw(canvas, mapView, shadow);
				//---translate the GeoPoint to screen pixels---
				Point screenPts = new Point();
				mapView.getProjection().toPixels(p1, screenPts);
				//---add the marker---
				Bitmap bmp = BitmapFactory.decodeResource(
				getResources(), R.drawable.pinicon32);
				canvas.drawBitmap(bmp, screenPts.x-10, screenPts.y-50, null);
				return true;
				}
		
		@Override
		public boolean onTouchEvent(MotionEvent event,
				MapView mapView)
		{
		//---when user lifts his finger---
		if (event.getAction() == 1) {
			status=true;
		GeoPoint p = mapView.getProjection().fromPixels(
		(int) event.getX(),
		(int) event.getY());
		/*Toast.makeText(getBaseContext(),"Location: "+
		p.getLatitudeE6() / 1E6 + "," +
		p.getLongitudeE6() /1E6 ,
		Toast.LENGTH_SHORT).show();*/
		p1 = p;
		MapOverlay mapOverlay = new MapOverlay(p1);
		
		}
		return false;
		}
				}
		
		
		
	}