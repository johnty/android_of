package cc.openframeworks.androidUiExample;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import cc.openframeworks.OFAndroid;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.noisepages.nettoyeur.bluetooth.util.*;
import com.noisepages.nettoyeur.bluetooth.BluetoothSppConnection;
import com.noisepages.nettoyeur.bluetooth.BluetoothSppObserver;
import com.noisepages.nettoyeur.bluetooth.midi.BluetoothMidiDevice;
import com.noisepages.nettoyeur.midi.MidiReceiver;


public class OFActivity extends cc.openframeworks.OFActivity implements OnClickListener {
	// the variable you want to share must be public
	private static final String TAG = "oF BT MIDI Test";
	
	private static final int CONNECT = 1;
	
	private Button connect;
	private Button reset;
	private Toast toast = null;
	
	private BluetoothMidiDevice midiService = null;
	
	static void post(String msg) {
		Log.v("BT", msg);
	}
	
	// TOAST for displaying stuff
	private void toast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
				}
				toast.setText(TAG + ": " + msg);
				toast.show();
			}
		});
	}
	
	//BT Observer for showing stuff
	
	private final BluetoothSppObserver observer = new BluetoothSppObserver() {
	    @Override
	    public void onDeviceConnected(BluetoothDevice device) {
	    	post("device connected: " + device);
	    }

	    @Override
	    public void onConnectionLost() {
	    	post("connection lost");
	    }

	    @Override
	    public void onConnectionFailed() {
	    	post("connection failed");
	    }

	  };
	  
	  
	  //BT Receiver Port
	
	  private final MidiReceiver receiver = new MidiReceiver() {
		  
		    @Override
		    public void onNoteOff(int channel, int key, int velocity) {
		      post("note off: " + channel + ", " + key + ", " + velocity);
		    }

		    @Override
		    public void onNoteOn(int channel, int key, int velocity) {
		      post("note on: " + channel + ", " + key + ", " + velocity);
		    }

		    @Override
		    public void onAftertouch(int channel, int velocity) {
		      post("aftertouch: " + channel + ", " + velocity);
		    }

		    @Override
		    public void onControlChange(int channel, int controller, int value) {
		      post("control change: " + channel + ", " + controller + ", " + value);
		    }

		    @Override
		    public void onPitchBend(int channel, int value) {
		      post("pitch bend: " + channel + ", " + value);
		    }

		    @Override
		    public void onPolyAftertouch(int channel, int key, int velocity) {
		      post("polyphonic aftertouch: " + channel + ", " + key + ", " + velocity);
		    }

		    @Override
		    public void onProgramChange(int channel, int program) {
		      post("program change: " + channel + ", " + program);
		    }

		    @Override
		    public void onRawByte(byte value) {
		      post("raw byte: " + Integer.toHexString(value));
		    }

		    @Override
		    public boolean beginBlock() {
		      return false;
		    }

		    @Override
		    public void endBlock() {}
		  };
		  
	
	public float value=0;
	@Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);
        String packageName = getPackageName();
        
        try {
        	midiService = new BluetoothMidiDevice(observer, receiver);
        }
        catch (IOException e) {
        	toast("MIDI not available!");
        	finish();
        }

        ofApp = new OFAndroid(packageName,this);
        
        
    }
        
    @Override
    public void finish() {
      cleanup();
      super.finish();
    }
	
    private void cleanup() {
    	if (midiService != null) {
    		midiService.close();
    		midiService = null;
    	}
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
    	// you inflate the menu
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.main_layout, menu);
    	
    	// Create settings menu options from here, one by one or infalting an xml
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// This passes the menu option string to OF
    	// you can add additional behavior from java modifying this method
    	// but keep the call to OFAndroid so OF is notified of menu events
    	if(OFAndroid.menuItemSelected(item.getItemId())){
    		
    		return true;
    	}
    	// you check which button of menu is pressed
    	switch(item.getItemId()){
    	case R.id.red :
    		value=0;
    		return true;
    	case R.id.green:
    		value=1;
    		return true;
    	case R.id.blue:
    		value=2;
    		return true;
    	case R.id.connect:
            if (midiService.getConnectionState() == BluetoothSppConnection.State.NONE) {
                startActivityForResult(new Intent(this, DeviceListActivity.class), CONNECT);
              } else {
                midiService.close();
              }
    		return true;
    	case R.id.reset:
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
    	// This method is called every time the menu is opened
    	//  you can add or remove menu options from here
    	return  super.onPrepareOptionsMenu(menu);
    }
    // the function which will be called into oF 
    public float returnValue(){
    	float floatValue=value;
    	return floatValue;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
   
	
}



