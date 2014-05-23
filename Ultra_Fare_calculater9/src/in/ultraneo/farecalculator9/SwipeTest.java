package in.ultraneo.farecalculator9;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

/* The following example is based on the following blog entry
 * http://www.codeshogun.com/blog/2009/04/16/how-to-implement-swipe-action-in-android/
 */

public class SwipeTest extends Activity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut; 
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;
    int count=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_right);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_in_left); 
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_left);

		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//below code is good
					//viewFlipper.setInAnimation(slideLeftIn);
					//viewFlipper.setOutAnimation(slideLeftOut);
					if(count>=5){
						new AlertDialog.Builder(SwipeTest.this)
						.setTitle("Got It?").setMessage("Do you want to read it again ?").setIcon(R.drawable.handicon)
						.setPositiveButton("No! I got it",
								          new DialogInterface.OnClickListener() {
								          public void onClick(DialogInterface dialog, int which) {
								        	  new UltraFileaccess().Load_Data("FIRSTTIME","1",SwipeTest.this);
												finish(); 
								        	  
								        	  
								           }
								           })
								           .setNegativeButton("Read this again",
								          new DialogInterface.OnClickListener() {
								          public void onClick(DialogInterface dialog, int which) {
								        	  viewFlipper.setInAnimation(slideLeftIn);
												viewFlipper.setOutAnimation(slideLeftOut);
												viewFlipper.showNext();
												
												count=0;
												
								        	  
								           }
								           }).show();
						
						
					//Toast.makeText(SwipeTest.this,"No Next Value",Toast.LENGTH_SHORT).show();
					}else {
						viewFlipper.setInAnimation(slideRightIn);
						viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showNext();
					
					
					count++; }
					//viewFlipper.showPrevious();
				}
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//below code is good
					if(count<=0){
					
					}else {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showPrevious();
					count--;
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
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
    	
    	String check = new UltraFileaccess().get_Data("FIRSTTIME", this);
    	if(check.equals("0"))
    	{
    		Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent); 	
    	}
    	else
    	{
    		startActivity(new Intent(this,Fareccalculator9Activity.class));
    	}

        return;
    }
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}
}
