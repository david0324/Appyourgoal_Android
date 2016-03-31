package app.appyourgoal.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import app.appyourgoal.R;
import app.appyourgoal.utils.AutoFitTextureView;
import app.appyourgoal.utils.FontHelper;

/**
 * Created by Dragisa on 11/11/2015.
 */
public class Camera2Activity extends Activity {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private static final String TAG = "Camera2VideoFragment";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    private final static String  FILE_PATCH = "AppYourGoal";

    private String mFilename = "";
    private String mCameraId;

    private CountDownTimer mTimer;

    private boolean isInGalery = false;
    private boolean mMediaRecorderSet = true;
    private boolean mFlashOn = false;

    private static final int SELECT_VIDEO = 1;
    private String LOG = "djevticAPP";

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView;

    /**
     * Button to record video
     */
    private ImageView mRecordVideo;
    private TextView mCameraCancel;
    private TextView mCameraTime;
    private TextView mCameraTogle;
    private ImageView mCameraGalery;
    private ImageView mCameraRecordButton;
    private ImageView mCameraPaste;

    /**
     * A refernce to the opened {@link android.hardware.camera2.CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * A reference to the current {@link android.hardware.camera2.CameraCaptureSession} for
     * preview.
     */
    private CameraCaptureSession mPreviewSession;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * The {@link android.util.Size} of video recording.
     */
    private Size mVideoSize;

    /**
     * Camera preview.
     */
    private CaptureRequest.Builder mPreviewBuilder;

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Whether the app is recording video now
     */
    private boolean mIsRecordingVideo;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            Log.d(LOG,"Error log for camera: "+error);
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            if(error == 1){
                Intent main = new Intent(getApplication(), Camera2Activity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }else {
                finish();
            }
        }

    };



    public void cancelCamera(View v){
        Intent main = new Intent(getApplication(), MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
    }

    public static Camera2Activity newInstance() {
        return new Camera2Activity();
    }

    /**
     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        mTextureView = (AutoFitTextureView) findViewById(R.id.camera_preview);
        mRecordVideo = (ImageView) findViewById(R.id.camera_record_button);
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

        mRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRecordingVideo) {
                    stopRecordingVideo();
                } else {
                    startRecordingVideo();
                }
            }
        });
        setTumbnailImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Requests permissions needed for recording video.
     */
    private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            ActivityCompat.requestPermissions(getParent(), VIDEO_PERMISSIONS,
                    REQUEST_VIDEO_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                }
            } else {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    private void openCamera(int width, int height) {
        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }
        final Activity activity = this;
        if (null == activity || activity.isFinishing()) {
            return;
        }
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.d(TAG, "tryAcquire");
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mCameraId = manager.getCameraIdList()[0];

            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, mVideoSize);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            manager.openCamera(mCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (NullPointerException e) {
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     */
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<Surface>();

            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getParent();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize || null == this) {
            return;
        }
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder() throws IOException {
        if (null == this) {
            return;
        }
        if(mMediaRecorderSet) {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(getVideoFile(this));
            mMediaRecorder.setVideoEncodingBitRate(10000000);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
            mMediaRecorderSet = false;
        }
    }

    private String getVideoFile(Context context) {
        boolean success = true;
        File folder = new File(Environment.getExternalStorageDirectory() + "/"+FILE_PATCH);
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
        return mFilename;
    }

    private void startRecordingVideo() {
        try {
            // UI
            mRecordVideo.setImageResource(R.drawable.stop_record_button);
            mIsRecordingVideo = true;
            mTimer = getTimer().start();
            // Start recording
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordingVideo() {
        // UI
        mIsRecordingVideo = false;
        mTimer.cancel();
        mRecordVideo.setImageResource(R.drawable.start_record_button);
        // Stop recording
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Toast.makeText(this, "Video saved: " + getVideoFile(this),
                    Toast.LENGTH_SHORT).show();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mFilename);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        startPreview();
        setTumbnailImage();
    }

    private void setTumbnailImage() {
        Log.d("djevtic", "setTumbnailImage called");
        String lastFilename = "";
        File f = new File(Environment.getExternalStorageDirectory() + "/"+FILE_PATCH);
        f.mkdirs();
        File[] file = f.listFiles();
        lastFilename = getLastFilenameAdded(file);
        Log.d("djevtic", "Last date is: " + lastFilename);
        Log.d(LOG, "Full path: " + Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/" + lastFilename);
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/" + lastFilename, MediaStore.Video.Thumbnails.MINI_KIND);
        mCameraGalery.setImageBitmap(bm);
        startPreview();
    }

    /**
     *
     * @param files List of files
     * @return String with filename of last date
     */

    private String getLastFilenameAdded(File[] files){
        String result = "";
        ArrayList<Date> dates = new ArrayList<>();
        for (File file:files) {
            Log.d("djevtic","Get files: "+file.getName());
            dates.add(formDate(file.getName()));
        }
        Collections.sort(dates);
        if(dates.size()>0) {
            return dates.get(dates.size() - 1).getTime() + ".mp4";
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
        try {
            // When an Image is picked
            if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedVideo = data.getData();
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

                String filePath = "";

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
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
                Toast.makeText(this, "You haven't picked video",
                        Toast.LENGTH_LONG).show();
                //mRestart = true;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
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

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

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

    //Toggle flash light
    /*public void togleFlash(View v){
        mFlashOn = !mFlashOn;
        if(mFlashOn){
            mCameraTogle.setText(getText(R.string.flash_on));

            mParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameter);
            mCamera.startPreview();
        }else{
            mCameraTogle.setText(getText(R.string.flash_off));
            mParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameter);
            mCamera.startPreview();
        }
    }*/
}
