#include "testApp.h"
#include <jni.h>
//--------------------------------------------------------------
void testApp::setup() {

	backColor.set(255, 0, 0);
	value = 0;

	JNIEnv *env = ofGetJNIEnv();
	jclass localClass = env->FindClass("cc/openframeworks/androidUiExample/OFActivity");
	javaClass = (jclass) env->NewGlobalRef(localClass);
	if (!javaClass) {
		ofLog() << "javaClass not found!" << endl;
	}

	javaObject = ofGetOFActivityObject();
	javaObject = (jobject) env->NewGlobalRef(javaObject);
	if (!javaObject) {
		ofLog() << "javaObject not found!" << endl;
	}

	outputstr = "";

	//player.loadMovie("data/hands.mp4");
}

//--------------------------------------------------------------
void testApp::update() {

	if (value == 0) {

		backColor.set( 128,0, 0);

	} else if (value == 1) {

		backColor.set(0, 128, 0);

	} else if (value == 2) {

		backColor.set(0, 0, 128);

	}
	JNIEnv *env = ofGetJNIEnv();
	jmethodID javaReturnMethod = env->GetMethodID(javaClass,"returnValue","()F");
	if(!javaReturnMethod){
		ofLog() << "javaReturnMethod not found!" << endl;
	}
	value=env->CallFloatMethod(javaObject,javaReturnMethod);
	javaReturnMethod = env->GetMethodID(javaClass,"getRawByteInt", "()I");
	if(!javaReturnMethod){
		ofLog() << "javaReturnMethod not found!" << endl;
		//outputstr += "javaReturnMethod not found!\n"
	}
	raw_byte=env->CallCharMethod(javaObject, javaReturnMethod);

	javaReturnMethod = env->GetMethodID(javaClass,"getLastInt", "()I");
	if(!javaReturnMethod){
		ofLog() << "javaReturnMethod not found!" << endl;
	}
	sensorValue=env->CallIntMethod(javaObject, javaReturnMethod);
	/*if (sensorValue != -1) {
		if (!player.isPlaying()) {
			player.play();
		}
	}
	if (player.isPlaying()) {
		player.update();
	}*/

	//outputstr+=(int)raw_byte+"__";
	if (raw_byte == 0xff) {
		//outputstr+="\n";
	}
	if (outputstr.length() > 100) {
		outputstr = "";
	}
}

//--------------------------------------------------------------
void testApp::draw() {
	ofBackground(backColor);
	ofSetColor(255,255,255);
	ofDrawBitmapString(ofToString(ofGetFrameRate(),2),10,10);
	//ofDrawBitmapString(outputstr, 10, 20);
	ofDrawBitmapString(ofToString(sensorValue), 10, 40);
	//player.draw(300,400);
	ofSetCircleResolution(100);
	ofFill();
	ofSetColor(0, sensorValue*2, 255-sensorValue*2);
	ofCircle(300+(float)sensorValue/2.0f, 300, 100+sensorValue/2);
}

//--------------------------------------------------------------
void testApp::keyPressed(int key) {

}

//--------------------------------------------------------------
void testApp::keyReleased(int key) {

}

//--------------------------------------------------------------
void testApp::windowResized(int w, int h) {

}

//--------------------------------------------------------------
void testApp::touchDown(int x, int y, int id) {

}

//--------------------------------------------------------------
void testApp::touchMoved(int x, int y, int id) {

}

//--------------------------------------------------------------
void testApp::touchUp(int x, int y, int id) {

}

//--------------------------------------------------------------
void testApp::touchDoubleTap(int x, int y, int id) {

}

//--------------------------------------------------------------
void testApp::touchCancelled(int x, int y, int id) {

}

//--------------------------------------------------------------
void testApp::swipe(ofxAndroidSwipeDir swipeDir, int id) {

}

//--------------------------------------------------------------
void testApp::pause() {

}

//--------------------------------------------------------------
void testApp::stop() {

}

//--------------------------------------------------------------
void testApp::resume() {

}

//--------------------------------------------------------------
void testApp::reloadTextures() {

}

//--------------------------------------------------------------
bool testApp::backPressed() {
	return false;
}

//--------------------------------------------------------------
void testApp::okPressed() {

}

//--------------------------------------------------------------
void testApp::cancelPressed() {

}
