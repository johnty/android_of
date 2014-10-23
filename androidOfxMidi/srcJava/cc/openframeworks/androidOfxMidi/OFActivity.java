package cc.openframeworks.androidOfxMidi;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import cc.openframeworks.OFAndroid;


public class OFActivity extends cc.openframeworks.OFActivity{
	
	private int clickCount = 0;

	@Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);
        String packageName = getPackageName();

        ofApp = new OFAndroid(packageName,this);
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
    	byte data[] = new byte[] { (byte) 0xF0, (byte) 0xFF, (byte)0x0A};
    	OFAndroid.passArray(data);
    	OFAndroid.onCustom();
    	OFAndroid.passInt(clickCount);
    	
    	return super.onOptionsItemSelected(item);
    }
    

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
    	// This method is called every time the menu is opened
    	//  you can add or remove menu options from here
    	return  super.onPrepareOptionsMenu(menu);
    }
	
}



