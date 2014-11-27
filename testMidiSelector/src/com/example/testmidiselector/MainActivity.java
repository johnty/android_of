package com.example.testmidiselector;

import java.util.List;

import com.noisepages.nettoyeur.midi.MidiReceiver;
import com.noisepages.nettoyeur.usb.ConnectionFailedException;
import com.noisepages.nettoyeur.usb.DeviceNotConnectedException;
import com.noisepages.nettoyeur.usb.InterfaceNotAvailableException;
import com.noisepages.nettoyeur.usb.UsbBroadcastHandler;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice.UsbMidiInput;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice.UsbMidiOutput;
import com.noisepages.nettoyeur.usb.midi.util.UsbMidiInputSelector;
import com.noisepages.nettoyeur.usb.midi.util.UsbMidiOutputSelector;
import com.noisepages.nettoyeur.usb.util.AsyncDeviceInfoLookup;
import com.noisepages.nettoyeur.usb.util.UsbDeviceSelector;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private UsbMidiDevice usbMIDI = null;
	private UsbMidiInput usbMidiInput = null;
	private MidiReceiver usbMidiOutput = null;

	private Toast toast = null;

	private void toast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
				}
				toast.setText("MIDISelectorTest: " + msg);
				toast.show();
			}
		});
	}

	private void chooseMidiDevice() {
		final List<UsbMidiDevice> devices = UsbMidiDevice.getMidiDevices(this);
		new AsyncDeviceInfoLookup() {

			@Override
			protected void onLookupComplete() {
				new UsbDeviceSelector<UsbMidiDevice>(devices) {

					@Override
					protected void onDeviceSelected(UsbMidiDevice device) {
						usbMIDI = device;
						usbMIDI.requestPermission(MainActivity.this);
						toast("USB device selected!");
					}

					@Override
					protected void onNoSelection() {
						toast("No device selected");
					}
				}.show(getFragmentManager(), null);
			}
		}.execute(devices.toArray(new UsbMidiDevice[devices.size()]));
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//add permission handler
		UsbMidiDevice.installBroadcastHandler(this, new UsbBroadcastHandler() {

			@Override
			public void onPermissionGranted(UsbDevice device) {
				// TODO Auto-generated method stub
				toast("USB permission granted!");
				if (usbMIDI == null || !usbMIDI.matches(device)) {
					return;
				}
				try {
					usbMIDI.open(MainActivity.this);

				} catch (ConnectionFailedException e) {
					toast("USB connection failed"); 
					usbMIDI = null;
					return;
				}
				new UsbMidiInputSelector(usbMIDI) {

					@Override
					protected void onInputSelected(UsbMidiInput input, UsbMidiDevice device, int iface,
							int index) {
						toast("Input selection: Interface " + iface + ", Input " + index);
						//input.setReceiver(receiver);
						try {
							input.start();
						} catch (DeviceNotConnectedException e) {
							toast("MIDI device has been disconnected");
							return;
						} catch (InterfaceNotAvailableException e) {
							toast("MIDI interface is unavailable");
							return;
						}
						//outputSelector.show(getFragmentManager(), null);
					}

					@Override
					protected void onNoSelection(UsbMidiDevice device) {
						// TODO Auto-generated method stub
						toast("No input selected");
					}
				}.show(getFragmentManager(), null);

				new UsbMidiOutputSelector(usbMIDI) {

					@Override
					protected void onOutputSelected(UsbMidiOutput output, UsbMidiDevice device, int iface,
							int index) {
						toast("Output selection: Interface " + iface + ", Output " + index);
						try {
							usbMidiOutput = output.getMidiOut();
						} catch (DeviceNotConnectedException e) {
							toast("MIDI device has been disconnected");
						} catch (InterfaceNotAvailableException e) {
							toast("MIDI interface is unavailable");
						}
					}

					@Override
					protected void onNoSelection(UsbMidiDevice device) {
						toast("No output selected");
					}
				}.show(getFragmentManager(), null);

			}

			@Override
			public void onPermissionDenied(UsbDevice device) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeviceDetached(UsbDevice device) {
				// TODO Auto-generated method stub

			}
		});




	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.connect_usb) {
			chooseMidiDevice();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
