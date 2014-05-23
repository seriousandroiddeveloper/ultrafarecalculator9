package in.ultraneo.farecalculator9;







import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.ci.geo.route.Road;
import org.ci.geo.route.RoadProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapRouteActivity extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	private Road mRoad;
	 TextView txt1;
	 int width,height,x1,y1,screenHeight;
	 private ProgressDialog m_ProgressDialog;
	 String DISTANCE;
	 Button textView;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdirection2);
		mapView = (MapView) findViewById(R.id.mapview);
		
		
		mapView.setBuiltInZoomControls(true);
		
		Bundle bundle = getIntent().getExtras();
        final String stuff1 =bundle.getString("Lat1"); 
        final String stuff2= bundle.getString("Lon1"); 
        final String stuff3 = bundle.getString("Lat2"); 
        final String stuff4 = bundle.getString("Lon2");
        
        m_ProgressDialog = ProgressDialog.show(MapRouteActivity.this,null, "Please wait ...", true);
      /*  txt1 = new TextView(this);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        
        if(screenHeight==800)
        {
        	 x1 = 100; y1 = 270;	
        	  width = 180; height =70;
        	  txt1.setTextSize(43);
        }
        else if(screenHeight==480)
        {
        	 x1 = 63; y1 = 158;	
        	 width = 180; height =70;
          	  txt1.setTextSize(43);
        }
        else if(screenHeight==320){
        	 x1 = 50; y1 = 100;
        	 width = 180; height =70;
       	  txt1.setTextSize(43);
        	
        }
        myLayout.addView(txt1, new LayoutParams(width, height, x1, y1));*/
       
        
       
        
        
        
		new Thread() {
			

			@Override
			public void run() {
				// m_ProgressDialog = ProgressDialog.show(MapRouteActivity.this,"Please wait...", "Retrieving data ...", true);
				double fromLat = Double.valueOf(stuff1.trim()).doubleValue(), fromLon =Double.valueOf(stuff2.trim()).doubleValue(), toLat =Double.valueOf(stuff3.trim()).doubleValue(), toLon =Double.valueOf(stuff4.trim()).doubleValue();
				String url = RoadProvider
						.getUrl(fromLat, fromLon, toLat, toLon);
				InputStream is = getConnection(url);
				mRoad = RoadProvider.getRoute(is);
				mHandler.sendEmptyMessage(0);
				
			}
		}.start();
	}
	 public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.getdirectionmenu, menu);
	        return true;
	    }
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			m_ProgressDialog.dismiss();
			textView = (Button) findViewById(R.id.button1);
			textView.setTextSize(45);
			String temp =String.valueOf(mRoad.mdistance);//mDescription;
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapRouteActivity.this);
	        String default2 = sharedPrefs.getString("list_preference","taxi");
	        int distance = mRoad.mdistance;
	        float dis = (float)distance/1000;
	     //   Toast.makeText(MapRouteActivity.this, distance,Toast.LENGTH_SHORT).show();
	        String temp2 = String.valueOf(dis);//temp.substring(10,temp.indexOf("km"));
	        DISTANCE=temp2;
	        
	        FareCalculator ne = new FareCalculator();
			int yo = ne.filter(Float.valueOf(temp2));
	        
	        if(default2.equals("taxi")){
	        	textView.setTextSize(35);
	        	String start = sharedPrefs.getString("taxifirst","20");//getFloat("taxifirst", Float.valueOf("20.0"));
	        	String rest = sharedPrefs.getString("taxirest","11");//getFloat("taxirest", Float.valueOf("11.0"));
	        	float to = ne.getFaretaxi(yo,Float.valueOf(start),Float.valueOf(rest));
	        	textView.setText(String.valueOf("Rs."+to+"(taxi)"));
	        	
	        }else if(default2.equals("auto")){
	        	textView.setTextSize(35);
	        	String start = sharedPrefs.getString("autofirst","19");//getFloat("taxifirst", Float.valueOf("20.0"));
	        	String rest = sharedPrefs.getString("autorest","6.5");
	        	float to = ne.getFareauto(yo,Float.valueOf(start),Float.valueOf(rest));
	        	textView.setText("Rs."+to+"(auto)");
	        	
	        }else if(default2.equals("dist")){
	        	
	        	
	        	textView.setText(temp2+"Km");
	        	
	        }
	        
			 
			//textView.setText(mRoad.mName + " " + mRoad.mDescription);
			Toast.makeText(getApplicationContext(),mRoad.mDescription ,Toast.LENGTH_LONG).show();
			
			
			
			
			
			
			MapOverlay mapOverlay = new MapOverlay(mRoad, mapView,getApplicationContext());
			List<Overlay> listOfOverlays = mapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
			mapView.invalidate();
		};
	};

	private InputStream getConnection(String url) {
		InputStream is = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			is = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		 FareCalculator ne = new FareCalculator();
		 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapRouteActivity.this);
			int yo = ne.filter(Float.valueOf(DISTANCE));	
	        switch (item.getItemId()) {
	        case R.id.item1: 
	        {
	        	textView.setTextSize(35);
	        	String start = sharedPrefs.getString("autofirst","19");//getFloat("taxifirst", Float.valueOf("20.0"));
	        	String rest = sharedPrefs.getString("autorest","6.5");
	        	float to = ne.getFareauto(yo,Float.valueOf(start),Float.valueOf(rest));
	        	textView.setText("Rs."+to+"(auto)");	
	        }
	            
	            return true;
	        case R.id.item2: 
	        {
	        	textView.setTextSize(35);
	        	String start = sharedPrefs.getString("taxifirst","20");//getFloat("taxifirst", Float.valueOf("20.0"));
	        	String rest = sharedPrefs.getString("taxirest","11");//getFloat("taxirest", Float.valueOf("11.0"));
	        	float to = ne.getFaretaxi(yo,Float.valueOf(start),Float.valueOf(rest));
	        	textView.setText(String.valueOf("Rs."+to+"(taxi)"));//
	        	
	        	
	        }
	            
	            return true;
	        
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	public void onPause()
	{
		super.onPause(); 
	int zoomer = mapView.getZoomLevel();
	new UltraFileaccess().Load_Data("ZOOM",String.valueOf(zoomer), this);
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
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
      
       startActivity(new Intent(MapRouteActivity.this, Fareccalculator9Activity.class));

        return;
    }
	
	
}

class MapOverlay extends com.google.android.maps.Overlay {
	Road mRoad;
	Context context;
	ArrayList<GeoPoint> mPoints;
	static MapController mapController;

	public MapOverlay(Road road, MapView mv,Context context) {
		mRoad = road;
		this.context = context;
		if (road.mRoute.length > 0) {
			mPoints = new ArrayList<GeoPoint>();
			for (int i = 0; i < road.mRoute.length; i++) {
				mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),
						(int) (road.mRoute[i][0] * 1000000)));
			}
			
			int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(
					mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)
					.getLatitudeE6()) / 2);
			int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(
					mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)
					.getLongitudeE6()) / 2);
			GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);
			
			
			mapController = mv.getController();
			mapController.animateTo(new GeoPoint((int) (mRoad.mRoute[0][1] * 1000000),
					(int) (mRoad.mRoute[0][0] * 1000000)));
			String temp = new UltraFileaccess().get_Data("ZOOM", context);
			mapController.setZoom(Integer.parseInt(temp));
			
			
		}
	}
	
	 
	@Override
	public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
		super.draw(canvas, mv, shadow);
		 Point screenPts = new Point();
		 Point screenPts2 = new Point();
		mv.getProjection().toPixels(new GeoPoint((int) (mRoad.mRoute[0][1] * 1000000),
				(int) (mRoad.mRoute[0][0] * 1000000)), screenPts);
		mv.getProjection().toPixels(new GeoPoint((int) (mRoad.mRoute[mRoad.mRoute.length-1][1] * 1000000),
				(int) (mRoad.mRoute[mRoad.mRoute.length-1][0] * 1000000)), screenPts2);
		
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bluedot);
		Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.pinicon32);		
		
		drawPath(mv, canvas,bmp,screenPts,screenPts2,bmp2);
		return true;
	}

	public void drawPath(MapView mv, Canvas canvas,Bitmap bmp,Point screenPts,Point screenPts2,Bitmap bmp2) {
		int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(3);
		canvas.drawBitmap(bmp, screenPts.x-5, screenPts.y-2, null);
		canvas.drawBitmap(bmp2, screenPts2.x-15, screenPts2.y-32, null);
		for (int i = 0; i < mPoints.size(); i++) {
			Point point = new Point();
			mv.getProjection().toPixels(mPoints.get(i), point);
			x2 = point.x;
			y2 = point.y;
			if (i > 0) {
				canvas.drawLine(x1, y1, x2, y2, paint);
				
			}
			x1 = x2;
			y1 = y2;
		}
	}
}