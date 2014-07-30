package com.face.facedetectionexample;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private Camera mCamera;
	private CameraPreview mPreview;
	TextView leftEye,rightEye,mouth,finalDistance;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("Is camera available : ",""+checkCameraHardware(this));
       // noOfFaces = (TextView) findViewById(R.id.textView1);
        leftEye = (TextView) findViewById(R.id.leftEye);
        rightEye = (TextView) findViewById(R.id.rightEye);
        mouth = (TextView) findViewById(R.id.mouth);
        finalDistance = (TextView)findViewById(R.id.distance);
        
        
     // Create an instance of Camera
        mCamera = getCameraInstance();
     // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
       
        preview.addView(mPreview);
       
      //  Camera.CameraInfo info = mCamera.getCameraInfo(Camera.getNumberOfCameras()-1, Camera.CameraInfo.CAMERA_FACING_FRONT);
          
          mCamera.setFaceDetectionListener(new FaceDetectionListener() {
			
			@Override
			public void onFaceDetection(Face[] faces, Camera camera) {
				DecimalFormat df = new DecimalFormat("####0.00");
				
				if (faces.length > 0){
					Matrix matrix = new Matrix();
					//Camera.CameraInfo info = new Camera.CameraInfo();
					//boolean mirror = true;
					//Log.v("Mirror", ""+mirror);
			          matrix.setScale(true ? -1 : 1, 1);
			          float[] pts_left = {faces[0].leftEye.x,faces[0].leftEye.y};
						float[] pts_right = {faces[0].rightEye.x,faces[0].rightEye.y};
						
					
						Log.v("Before :","" +faces[0].rect.left + " "+faces[0].rect.top);
						matrix.mapPoints(pts_left );
						matrix.mapPoints(pts_right );	
					
						matrix.postRotate(180);
			        
			          matrix.postScale(mPreview.getWidth() / 2000f, mPreview.getHeight()  / 2000f);
			          matrix.postTranslate(mPreview.getWidth() / 2f, mPreview.getHeight()  / 2f);
			        
			       /* double leftEye_X = pts_left[0];
					double leftEye_Y = pts_left[1];
					double rightEye_X = pts_right[0];
					double rightEye_Y = pts_right[1];*/
					
					
					Point newPoint_LeftEye = new Point((int)pts_left[0], (int)pts_left[1]);
					Point newPoint_RightEye = new Point((int)pts_right[0], (int)pts_right[1]);

					
					double leftEye_X = newPoint_LeftEye.x;
					double leftEye_Y = newPoint_LeftEye.y;
					double rightEye_X = newPoint_RightEye.x;
					double rightEye_Y = newPoint_RightEye.y;
					
					
				
					leftEye.setText(""+df.format(Math.pow((leftEye_X - rightEye_X),2)));
					rightEye.setText(""+df.format(Math.pow((leftEye_Y - rightEye_Y),2)));
					double distance_eyes_square = Math.pow((leftEye_X - rightEye_X),2) + Math.pow((leftEye_Y - rightEye_Y),2);
					
					mouth.setTextSize(30);
					mouth.setText(""+df.format(distance_eyes_square));
					double actual_distance = ((4361.8) * Math.pow(distance_eyes_square, -0.504));
					//double actual_distance = (-1.9074 * Math.pow(distance_eyes_square, 3)) + (140.53 * Math.pow(distance_eyes_square, 2)) - (3493.9 * distance_eyes_square) + 31098;
					finalDistance.setTextSize(40);
					finalDistance.setText(""+df.format(actual_distance));
					
					
					
			}
				
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
        	//noOfFaces.setText(""+mPreview.getNoOfFaces());
        //	noOfFaces.setText(mPreview.getNoOfFaces());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    
    @SuppressLint("NewApi")
	public static Camera getCameraInstance(){
        Camera c = null;
       // Context context = MainActivity.getApplicationContext();
        try {
        	//if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // this device has a camera
        
        		//c = Camera.open(); // attempt to get a Camera instance
        	int cameraId = Camera.getNumberOfCameras();
        	c = Camera.open(cameraId-1);
        	
        	
        		 //c = Camera.open(Camera.getNumberOfCameras()-1);
        	//Log.v("Camera Open", ""+c);
           // }
           
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	releaseCamera();
    }
}

class CameraFaceDetectionListener implements Camera.FaceDetectionListener {
	
	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		DecimalFormat df = new DecimalFormat("####0.00");
		//System.out.println("Value: " + df.format(value));
		// TODO Auto-generated method stub 
		if (faces.length > 0){
			Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
			double leftEye_X = faces[0].leftEye.x;
			double leftEye_Y = faces[0].leftEye.y;
			double rightEye_X = faces[0].rightEye.x;
			double rightEye_Y = faces[0].rightEye.y;
			double mouth_Y = faces[0].mouth.y;
			//faces[0].leftEye.
			//leftEye.
			double distance_eyes = Math.sqrt(Math.pow((leftEye_X - leftEye_Y),2) + Math.pow((rightEye_X - rightEye_Y),2));
			Log.v("distance between eyes :", ""+df.format(distance_eyes));
			Log.v("Focal length :",""+camera.getParameters().getFocalLength());
			float focusDistances[] = new float[3];
			   camera.getParameters().getFocusDistances(focusDistances);
			  Log.v("Optimal Focus Distance(meters): " 
			     ,""+focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);
			 
			
	}
    }
		
	}

	   
	


    



