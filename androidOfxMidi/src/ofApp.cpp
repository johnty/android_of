#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
	output_msg = "";
}

//--------------------------------------------------------------
void ofApp::update(){

}

//--------------------------------------------------------------
void ofApp::draw(){
	ofBackground(255,0,255);
	ofDrawBitmapString("Hello World\n Hello world", 25, 25);
	ofDrawBitmapString(output_msg, 25, 55);

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
	output_msg+="onCustom\n";
}

void ofApp::onArray(char* data, int len) {
	output_msg+="onArray: size = " + ofToString(len)+ "\n"; // len = " + ofToString(len) + "\n";
}


void ofApp::onInt(int i) {
	output_msg+="i= " + ofToString(i) + "!\n";
}
