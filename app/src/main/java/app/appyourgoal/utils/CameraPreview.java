package app.appyourgoal.utils;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context mContext;
    private MediaRecorder mRecorder;
    private int mRotation;
    private boolean mCameraRelesed = false;

    private String LOG = "djevticAPP";

    public CameraPreview(Context context, Camera camera, MediaRecorder recorder) {
        super(context);
        mContext = context;
        mCamera = camera;
        mRecorder = recorder;
        setmHolder(getHolder());
        getmHolder().addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        getmHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setMediaRecorder(MediaRecorder recorder){
        mRecorder = recorder;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(LOG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (getmHolder().getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(getmHolder());
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(LOG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void prepareVideoOrientation(){
        mCamera.stopPreview();


        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation() == Surface.ROTATION_0) {
            Log.d(LOG,"ORIENTATION SET "+0);
            //parameters.setPreviewSize(h, w);
            mCamera.setDisplayOrientation(90);
            if (mRecorder != null) {
                mRecorder.setOrientationHint(90);
                mRotation = 90;
            }
        }

        if (display.getRotation() == Surface.ROTATION_90) {
            Log.d(LOG,"ORIENTATION SET "+90);
            //parameters.setPreviewSize(w, h);
        }

        if (display.getRotation() == Surface.ROTATION_180) {
            Log.d(LOG,"ORIENTATION SET "+180);
            //parameters.setPreviewSize(h, w);
        }

        if (display.getRotation() == Surface.ROTATION_270) {
            Log.d(LOG,"ORIENTATION SET "+270);
            //parameters.setPreviewSize(w, h);
            mCamera.setDisplayOrientation(180);
            if (mRecorder != null) {
                mRecorder.setOrientationHint(180);
                mRotation = 180;
            }
        }

       if(parameters != null){
            if(mCamera != null){
                mCamera.setParameters(parameters);
                refreshCamera(mCamera);
            }else{
                Log.d(LOG, "Camera is NULL");
            }

        }else{
            Log.d(LOG, "Parameters are NULL");
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Log.d(LOG,"surfaceChanged called");
            mCamera.stopPreview();


            Camera.Parameters parameters = mCamera.getParameters();
            Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();

            if (display.getRotation() == Surface.ROTATION_0) {
                Log.d(LOG, "ORIENTATION SET " + 0);
                //parameters.setPreviewSize(h, w);
                mCamera.setDisplayOrientation(90);
                if (mRecorder != null) {
                    mRecorder.setOrientationHint(90);
                    mRotation = 90;
                }
            }

            if (display.getRotation() == Surface.ROTATION_90) {
                Log.d(LOG, "ORIENTATION SET " + 90);
                //parameters.setPreviewSize(w, h);
            }

            if (display.getRotation() == Surface.ROTATION_180) {
                Log.d(LOG, "ORIENTATION SET " + 180);
                //parameters.setPreviewSize(h, w);
            }

            if (display.getRotation() == Surface.ROTATION_270) {
                Log.d(LOG, "ORIENTATION SET " + 270);
                //parameters.setPreviewSize(w, h);
                mCamera.setDisplayOrientation(180);
                if (mRecorder != null) {
                    mRecorder.setOrientationHint(180);
                    mRotation = 180;
                }
            }

        if(parameters != null){
            if(mCamera != null){
                mCamera.setParameters(parameters);
                refreshCamera(mCamera);
            }else{
                Log.d(LOG, "Camera is NULL");
            }

        }else{
            Log.d(LOG, "Parameters are NULL");
        }
    }

    public void setRotation(){
        Log.d(LOG,"setRotation is:"+mRotation);
        Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            mRotation = 90;
        }
        if (display.getRotation() == Surface.ROTATION_90) {
            mRotation = 180;
        }
        if (display.getRotation() == Surface.ROTATION_180) {
            mRotation = 270;
        }
        if (display.getRotation() == Surface.ROTATION_270) {
            mRotation = 0;
        }
        Log.d(LOG,"setRotation is:"+mRotation);
        mRecorder.setOrientationHint(mRotation);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.d(LOG, "Camera.release");
         //mCamera.release();
        //mHolder.removeCallback(this);
    }

    public SurfaceHolder getmHolder() {
        return mHolder;
    }

    public void setmHolder(SurfaceHolder mHolder) {
        this.mHolder = mHolder;
    }
}