#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
	output_msg = last_locked ="";
	lockedout = false;
}

//--------------------------------------------------------------
void ofApp::update(){

	if (mylock.tryLock()) {
		last_count = jni_count;
		mylock.unlock();
		lockedout = false;
	}
	else {
		lockedout = true;
		last_locked = "locked@ " + ofToString(last_count);
	}

	output_msg = "jni calls = " + ofToString(last_count);
	output_msg += ";   call period(ms) = " + ofToString(ofGetElapsedTimeMillis()/(float)last_count) + "\n data = ";

	for (int i=0; i<data_len; i++) {
		output_msg += ofToString((int)recv_data[i])+ " | ";
	}
	output_msg+= "\n Sensor Data: ";
	for (int i=0; i<kNUM_ICUBEX_SENSORS; i++) {
		output_msg += ofToString(myICube.getSensorData(i)) + " ";
	}



}

//--------------------------------------------------------------
void ofApp::draw(){
	if (lockedout)
		ofBackground(255,0,0);
	else
		ofBackground(255,0,255);
	//ofDrawBitmapString("Hello World\n Hello world", 25, 25);
	ofDrawBitmapString(last_locked, 25, 25);
	//output_msg = "jni calls = " + ofToString(jni_count);
	ofDrawBitmapString(output_msg, 25, 55);
	float fr = ofGetFrameRate();
	ofDrawBitmapString(ofToString(fr), 145, 25);

}

//--------------------------------------------------------------
void ofApp::keyPressed  (int key){ 
	
}

//--------------------------------------------------------------
void ofApp::keyReleased(int key){ 
	
}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h){

}

//--------------------------------------------------------------
void ofApp::touchDown(int x, int y, int id){
	output_msg += ofToString(x)+":"+ofToString(y)+"\n";
}

//--------------------------------------------------------------
void ofApp::touchMoved(int x, int y, int id){
	output_msg += ofToString(x)+":"+ofToString(y)+"\n";
}

//--------------------------------------------------------------
void ofApp::touchUp(int x, int y, int id){

}

//--------------------------------------------------------------
void ofApp::touchDoubleTap(int x, int y, int id){

}

//--------------------------------------------------------------
void ofApp::touchCancelled(int x, int y, int id){

}

//--------------------------------------------------------------
void ofApp::swipe(ofxAndroidSwipeDir swipeDir, int id){
	output_msg+= "sw! \n";
}

bool ofApp::menuItemSelected(char* menu_id) {
	output_msg += "onMenu....\n";
	return true;
}

//--------------------------------------------------------------
void ofApp::pause(){
	output_msg +="pause\n";
}

//--------------------------------------------------------------
void ofApp::stop(){

}

//--------------------------------------------------------------
void ofApp::resume(){
	output_msg +="resume\n";
}

//--------------------------------------------------------------
void ofApp::reloadTextures(){

}

//--------------------------------------------------------------
bool ofApp::backPressed(){
	return false;
}

//--------------------------------------------------------------
void ofApp::okPressed(){

}

//--------------------------------------------------------------
void ofApp::cancelPressed(){

}

void ofApp::onCustom() {
	//output_msg+="onCustom\n";
	jni_count++;
}

void ofApp::onArray(char* data, int len) {
	//output_msg+="onArray: size = " + ofToString(len)+ "\n"; // len = " + ofToString(len) + "\n";
	//output_msg+="array contents = ";
	data_len = len;
	mylock.lock();
	if (recv_data != NULL)
		delete recv_data;
	recv_data = new char[data_len];
	memcpy(recv_data, data, len);
	//output_msg+="\n";
	jni_count++;
	mylock.unlock();
	myICube.newMidiMessage(data, len);

}


void ofApp::onInt(int i) {

	mylock.lock();
	jni_count = i;
	mylock.unlock();
}
