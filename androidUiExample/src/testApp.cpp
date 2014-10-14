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
}

//--------------------------------------------------------------
void testApp::update() {


			if (value == 0) {

				backColor.set( 255,0, 0);

			} else if (value == 1) {

				backColor.set(0, 255, 0);

			} else if (value == 2) {

				backColor.set(0, 0, 255);

			}
			JNIEnv *env = ofGetJNIEnv();
					jmethodID javaReturnMethod = env->GetMethodID(javaClass,"returnValue","()F");
					if(!javaReturnMethod){
						ofLog() << "javaReturnMethod not found!" << endl;
					}
					value=env->CallFloatMethod(javaObject,javaReturnMethod);
}

//--------------------------------------------------------------
void testApp::draw() {
ofBackground(backColor);
ofDrawBitmapString(ofToString(ofGetFrameRate(),2),10,10);
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
