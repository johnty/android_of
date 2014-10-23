package cc.openframeworks.androidUiExample;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.noisepages.nettoyeur.midi.util.SystemMessageDecoder;
import com.noisepages.nettoyeur.midi.util.SystemMessageReceiver;


public class OFActivity extends cc.openframeworks.OFActivity implements OnClickListener {
	// the variable you want to share must be public
	private static final String TAG = "oF BT MIDI Test";
	
	private static final int CONNECT = 1;
	
	private Button connect;
	private Button reset;
	private Toast toast = null;
	
	private BluetoothMidiDevice midiService = null;
	private SystemMessageDecoder midiSysDecoder;
	
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
	  
	  //sys ex receiver
	  
	  private final SystemMessageReceiver midiSysExReceiver = new SystemMessageReceiver() {

			@Override
			public void onSystemExclusive(byte[] sysex) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
			    for (byte b : sysex) {
			        sb.append(String.format("%02X ", b));
			    }
				post("sysex: " + sb);
				//Log.v("USBMIDI", "sysex");
			}

			@Override
			public void onTimeCode(int value) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSongPosition(int pointer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSongSelect(int index) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTuneRequest() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTimingClock() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onContinue() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStop() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActiveSensing() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSystemReset() {
				// TODO Auto-generated method stub

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
			  if (midiSysDecoder != null) {
				  midiSysDecoder.decodeByte(value);
			  }
			  if (value == (byte)0xF7 ) { //last value was "data", for the one sensor case
				  preVal = (int)raw_byte;
			  }
			  //post("raw byte: " + Integer.toHexString(value));
			  raw_byte = (char) value;
		  }

		  @Override
		  public boolean beginBlock() {
			  return false;
		  }

		  @Override
		  public void endBlock() {}
	  };

	
	public float value=0;
	public char raw_byte=0;
	public int preVal = -1;
	@Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);
        String packageName = getPackageName();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        try {
        	midiService = new BluetoothMidiDevice(observer, receiver);
        }
        catch (IOException e) {
        	toast("MIDI not available!");
        	finish();
        }
        midiSysDecoder = new SystemMessageDecoder(midiSysExReceiver);

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
    		Log.v("BT:", "switching to host mode and turning on s1");
    		//byte data[] = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte) 0x00, (byte) 0x22, (byte) 0xF7 };
    		//host mode
    		byte data[] = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte) 0x00, (byte) 0x5A, (byte) 0x00, (byte) 0xF7 };
    		midiService.getMidiOut().beginBlock();

    		for (int i=0; i<data.length; i++) {
    			midiService.getMidiOut().onRawByte(data[i]);
    		}
    		midiService.getMidiOut().endBlock();
    		//set interval to 50ms
    		data = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte) 0x00, (byte) 0x03, (byte) 0x00,(byte) 0x32, (byte) 0xF7 };
    		midiService.getMidiOut().beginBlock();
    		for (int i=0; i<data.length; i++) {
    			midiService.getMidiOut().onRawByte(data[i]);
    		}
    		midiService.getMidiOut().endBlock();
    		//start stream port 0 (0x40 == on, port 0)
    		data = new byte[] { (byte) 0xF0, (byte) 0x7D, (byte) 0x00, (byte) 0x01, (byte) 0x40, (byte) 0xF7 };
    		midiService.getMidiOut().beginBlock();
    		for (int i=0; i<data.length; i++) {
    			midiService.getMidiOut().onRawByte(data[i]);
    		}
    		midiService.getMidiOut().endBlock();
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
    public int getRawByteInt() {
    	int charValue= (int)raw_byte;
    	return charValue;
    }
    public int getLastInt() {
    	int intVal = preVal;
    	return intVal;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECT:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(DeviceListActivity.DEVICE_ADDRESS);
				try {
					midiService.connect(address);
				} catch (IOException e) {
					toast(e.getMessage());
				}
	        }
	        break;
	        }
	}
   
	
}



