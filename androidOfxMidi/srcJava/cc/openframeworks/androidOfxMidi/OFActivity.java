package cc.openframeworks.androidOfxMidi;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import cc.openframeworks.OFAndroid;


public class OFActivity extends cc.openframeworks.OFActivity{
	
	private long jni_calls = 0;
	private int clickCount = 1;
	private Handler handler = new Handler();
	private final int SAMPLE_INTERVAL_MS = 25; // in ms
	private final int SAMPLE_DATA_SIZE = 8; //number of bytes in dummy data
	
	private Runnable runnable = new Runnable() {
		//this class deals with generating periodic events,
		// to emulate the incoming "midi messages" from device
		// at a given rate
		@Override
		public void run() {
			Random r = new Random();
			byte r_b = (byte) r.nextInt(127);
			/*
			byte data[] = new byte[SAMPLE_DATA_SIZE];
			for (int i=0; i<SAMPLE_DATA_SIZE-1; i++) {
				data[i] = (byte) (0xFF - i);
			}
			data[SAMPLE_DATA_SIZE-1] = r_b; //append random byte at end.
			data[2]+=clickCount;
			*/
			
			//what looks like sensor data here:
			// sensor 0, value = random (0-127)
			byte data[] = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte)0x00,
										(byte) 0x00, (byte) r_b, (byte) 0xF7};
	    	OFAndroid.passArray(data);
			
			
			//OFAndroid.onCustom();
			OFAndroid.passArray(data);
			//OFAndroid.passInt((int) jni_calls);
			jni_calls++;
			if (clickCount % 2 == 1) {
				//repeat if we have odd clickcount
				// silly way to trigger emulation of data streaming...
				handler.postDelayed(runnable, SAMPLE_INTERVAL_MS);
			}
			if (jni_calls % 1000 == 0) {
				String msg = "JniCnt=" + jni_calls;
				Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
				t.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
				t.show();
				Log.v("JNI_CNT", msg);
			}
		}
	};

	@Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);
        String packageName = getPackageName();

        ofApp = new OFAndroid(packageName,this);
        
        //we send dummy data to make interface think sensor 0 is on:
        
        byte data[] = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte)0x00,
				(byte) 0x01, (byte) 0x40, (byte) 0xF7};
        OFAndroid.passArray(data);
        
        //start sending immediately:
        //handler.postDelayed(runnable, SAMPLE_INTERVAL_MS);
        
    }
	
	@Override
	public void onDetachedFromWindow() {
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        ofApp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ofApp.resume();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (OFAndroid.keyDown(keyCode, event)) {
	    return true;
	} else {
	    return super.onKeyDown(keyCode, event);
	}
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (OFAndroid.keyUp(keyCode, event)) {
	    return true;
	} else {
	    return super.onKeyUp(keyCode, event);
	}
    }


	OFAndroid ofApp;
    
	
	
    // Menus
    // http://developer.android.com/guide/topics/ui/menus.html
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Create settings menu options from here, one by one or infalting an xml
    	MenuInflater inflator = getMenuInflater();
    	inflator.inflate(R.menu.main_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	
    	
    	//handle within java
		if (item.getItemId() == R.id.menu_test) {
			clickCount++;
			String msg = "click count = " + clickCount;
			Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
			t.show();
			
		}
		if (item.getItemId() == R.id.menu_settings) {
			// use this to send an stream echo message
			 byte data[] = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte)0x00,
						(byte) 0x01, (byte) 0x40, (byte) 0xF7};
		     OFAndroid.passArray(data);
		     Log.v("OFActivity", "sending stream echo for sensor 0");
		}
    	
		//give oF a chance
    	// This passes the menu option string to OF
    	// you can add additional behavior from java modifying this method
    	// but keep the call to OFAndroid so OF is notified of menu events
    	if(OFAndroid.menuItemSelected(item.getItemId())) {
    		String msg = "back from C++ code!";
			Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
			t.show();
    	}
    	//byte data[] = new byte[] { (byte) 0xF0, (byte) 0xFF, (byte)0x0A};
    	//data[2]+=clickCount;
    	//OFAndroid.passArray(data);
    	OFAndroid.onCustom();
    	OFAndroid.passInt(clickCount);
    	if (clickCount % 2 == 1) {
    		handler.postDelayed(runnable, SAMPLE_INTERVAL_MS);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
    	// This method is called every time the menu is opened
    	//  you can add or remove menu options from here
    	return  super.onPrepareOptionsMenu(menu);
    }
	
}



