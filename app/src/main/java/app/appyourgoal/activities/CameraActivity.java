package app.appyourgoal.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import app.appyourgoal.R;
import app.appyourgoal.utils.CameraPreview;
import app.appyourgoal.utils.FontHelper;

/**
 * Created by Dragisa on 10/19/2015.
 */
public class CameraActivity extends Activity{

    private TextView mCameraCancel;
    private TextView mCameraTime;
    private TextView mCameraTogle;
    private ImageView mCameraGalery;
    private ImageView mCameraRecordButton;
    private ImageView mCameraPaste;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Context myContext;
    private LinearLayout cameraPreview;

    private boolean mRestart = false;

    private static final int SELECT_VIDEO = 1;
    private String LOG = "djevticAPP";

    private boolean mFlashOn = false;
    private boolean mRecordingGoing = false;

    private boolean isInGalery = false;

    private Camera mCamera;

    private String mFilename;

    private CountDownTimer mTimer;

    private Camera.Parameters mParameter;

    private int mOrientation;

    private int mCameraId;

    private final static String  FILE_PATCH = "AppYourGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Font helper for setting custom fonts
        FontHelper fontHelper = new FontHelper();

        //initializing components
        mCameraCancel = (TextView) findViewById(R.id.camera_cancel);
        mCameraTime = (TextView) findViewById(R.id.camera_time);
        mCameraTogle = (TextView) findViewById(R.id.camera_flash_togle);
        mCameraGalery = (ImageView) findViewById(R.id.camera_galery);
        mCameraRecordButton = (ImageView) findViewById(R.id.camera_record_button);
        mCameraPaste = (ImageView) findViewById(R.id.camera_paste);

        //Setting font type
        fontHelper.setTextViewTypeface(mCameraCancel, getApplicationContext());
        fontHelper.setTextViewTypeface(mCameraTime, getApplicationContext());
        fontHelper.setTextViewTypeface(mCameraTogle, getApplicationContext());

        mCameraPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplication(), PostYourPastedGoalActivity.class);
                startActivity(main);
            }
        });

    }

    public void togleRecoding(View v){
        if (mRecordingGoing) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            // stop recording and release camera
            mCameraRecordButton.setImageResource(R.drawable.start_record_button);
            mediaRecorder.stop(); // stop the recording
            mTimer.cancel();
            releaseMediaRecorder(); // release the MediaRecorder object
            Toast.makeText(CameraActivity.this, "Video captured!", Toast.LENGTH_LONG).show();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mFilename);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            mRecordingGoing = false;
            setTumbnailImage();
        } else {
            if (!prepareMediaRecorder()) {
                Log.d(LOG,"CameraActivity FAILED TO PREPARE MEDIA RECORDER");
                Toast.makeText(CameraActivity.this, "Sorry but we failed in preparing camera for recording!\n - Ended -", Toast.LENGTH_LONG).show();
                finish();
            }
            mOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            mCameraRecordButton.setImageResource(R.drawable.stop_record_button);
            // work on UiThread for better performance
            runOnUiThread(new Runnable() {
                public void run() {
                    // If there are stories, add them to the table

                    try {
                        mediaRecorder.start();
                        mTimer = getTimer().start();
                    } catch (final Exception ex) {
                        Log.d("---", "Exception in thread");
                        Toast.makeText(CameraActivity.this, "Sorry but we failed in preparing camera for recording!\n - Ended -", Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });

            mRecordingGoing = true;
        }
    }

    public void cancelCamera(View v){
        Intent main = new Intent(getApplication(), MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
    }

    //Toggle flash light
    public void togleFlash(View v){
        mFlashOn = !mFlashOn;
        List<String> supportedFlashModes = mParameter.getSupportedFlashModes();
        if(!mRecordingGoing && (supportedFlashModes!=null )) {
            if (mFlashOn) {
                mCameraTogle.setText(getText(R.string.flash_on));
                mParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(mParameter);
                mCamera.startPreview();
            } else {
                mCameraTogle.setText(getText(R.string.flash_off));
                mParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(mParameter);
                mCamera.startPreview();
            }
        }
    }

    private int findBackFacingCamera() {
        mCameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = i;
                break;
            }else if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
                mCameraId = i;
            }
        }
        return mCameraId;
    }

    public void onResume() {
        super.onResume();
        myContext = this;
        mediaRecorder = new MediaRecorder();
        initialize();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your device does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            mCamera = Camera.open(findBackFacingCamera());
            Log.d(LOG,"Camera is: "+mCamera);
            mPreview.refreshCamera(mCamera);
            mParameter = mCamera.getParameters();
        }
        if(mRestart){
            Intent main = new Intent(getApplication(), CameraActivity.class);
            startActivity(main);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);

        mPreview = new CameraPreview(myContext, mCamera, mediaRecorder);
        cameraPreview.addView(mPreview);

        setTumbnailImage();
    }

    private CountDownTimer getTimer(){
        CountDownTimer timer = new CountDownTimer(Long.MAX_VALUE , 1000) {
            int counter = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                counter++;
                String time = new Integer(counter).toString();
                long millis = counter;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds     = seconds % 60;
                mCameraTime.setText(String.format("%d:%02d:%02d", minutes, seconds,millis));
            }

            @Override
            public void onFinish() {
                counter = 0;
            }
        };
        return timer;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        /*if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(LOG, "Device HAVE have camera!!!!!");
            return true;
        } else {
            Log.d(LOG, "Device DON'T have camera!!!!!");
            return false;
        }*/
        Log.d(LOG,"Number of cameras: "+ Camera.getNumberOfCameras());
        if(Camera.getNumberOfCameras()!=0){
            return true;
        }else{
            return false;
        }
    }

    private void releaseMediaRecorder() {
        Log.d(LOG,"releaseMediaRecorder called");
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    private boolean prepareMediaRecorder() {
        Log.d(LOG,"prepareMediaRecorder called");
        mCamera.unlock();
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mPreview.setMediaRecorder(mediaRecorder);
            mPreview.setRotation();
            //mPreview.prepareVideoOrientation();
        }
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(mCameraId,CamcorderProfile.QUALITY_480P));

        File folder = new File(Environment.getExternalStorageDirectory() + "/"+FILE_PATCH);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.d("djevtic","Folder created");
        } else {
            Log.d("djevtic","Folder already exist");
            // Do something else on failure
        }

        Date date = new Date();
        mFilename = Environment.getExternalStorageDirectory() + "/"+FILE_PATCH+"/"+ date.getTime()+".mp4";
        mediaRecorder.setOutputFile(mFilename);
        //mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
        //mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(LOG,"Camera Activity  IllegalStateException while preparing mediaRecorder");
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(LOG,"Camera Activity  IOException while preparing mediaRecorder");
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mPreview.getHolder().removeCallback(mPreview);
            mCamera = null;
        }
    }

    private void setTumbnailImage() {
        Log.d("djevtic", "setTumbnailImage called");
        String lastFilename = "";
        File f = new File(Environment.getExternalStorageDirectory() + "/"+FILE_PATCH);
        f.mkdirs();
        File[] file = f.listFiles();
        if(file != null) {
            lastFilename = getLastFilenameAdded(file);
        }else{
            lastFilename = null;
        }
        Log.d("djevtic", "Last date is: " + lastFilename);
        Log.d(LOG,"Full path: "+Environment.getExternalStorageDirectory() + "/"+FILE_PATCH+"/"+lastFilename);
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/" + lastFilename, MediaStore.Video.Thumbnails.MINI_KIND);
        mCameraGalery.setImageBitmap(bm);
    }

    public static Bitmap getThumbnail(ContentResolver cr) throws Exception {

        Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", null, MediaStore.Video.VideoColumns.DATE_TAKEN);
        if (ca != null && ca.moveToFirst()) {
            int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
            ca.close();
            return MediaStore.Video.Thumbnails.getThumbnail(cr, id, MediaStore.Video.Thumbnails.MINI_KIND, null );
        }

        ca.close();
        return null;

    }

    /**
     *
     * @param files List of files
     * @return String with filename of last date
     */

    private String getLastFilenameAdded(File[] files){
        String result = "";
        if(files.length > 0) {
            ArrayList<Date> dates = new ArrayList<>();
            for (File file : files) {
                Log.d("djevtic", "Get files: " + file.getName());
                dates.add(formDate(file.getName()));
            }
            Collections.sort(dates);
            if (dates.size() > 0) {
                return dates.get(dates.size() - 1).getTime() + ".mp4";
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     *
     * @param filename String value of filename
     * @return Date when filename is created
     */
    private Date formDate(String filename){

        String dateList = filename.substring(0, filename.length()-4);;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(dateList));
        return cal.getTime();
    }

    public void openGalery(View v){
        isInGalery = true;
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Video"), SELECT_VIDEO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isInGalery = false;
        String imgDecodableString;
        String filePath = "";
        try {
            // When an Image is picked
            if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedVideo = data.getData();
                Log.d(LOG,"CameraActivity Build.VERSION.SDK_INT: "+Build.VERSION.SDK_INT);
                Log.d(LOG,"CameraActivity selectedVideo URI: "+selectedVideo);
                //if(Build.VERSION.SDK_INT <19){
                if(!String.valueOf(selectedVideo).contains("documents")){
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedVideo,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();
                }else{
                    String wholeID = DocumentsContract.getDocumentId(selectedVideo);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    //String[] column = { MediaStore.Images.Media.DATA };
                    String[] column = { MediaStore.Video.Media.DATA };

                    // where id is equal to
                    String sel = MediaStore.Video.Media._ID + "=?";

                    Cursor cursor = getContentResolver().
                            query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }

                Log.d(LOG, "File path:"+filePath);


                File videoFile = new File(filePath);

                if(videoFile.exists()){

                    Log.d(LOG, "Galery selected video:"+videoFile.getAbsolutePath());
                    Log.d(LOG, "Environment.getExternalStorageDirectory():"+Environment.getExternalStorageDirectory());
                    Intent postGoal = new Intent(getApplication(), PostYourGoalActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("file", videoFile.getAbsolutePath());
                    postGoal.putExtras(mBundle);
                    startActivity(postGoal);

                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
                mRestart = true;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong while we where picking video", Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG, "CameraActivity ERROR WHILE PICKING VIDEO " + e.getMessage());
            Log.d(LOG, "CameraActivity STACK TRACE START");
            e.printStackTrace();
            Log.d(LOG, "CameraActivity STACK TRACE END");
        }
    }

    @Override
    public void onBackPressed() {
        if(!isInGalery) {
            Intent main = new Intent(getApplication(), MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);
        }
    }
}

