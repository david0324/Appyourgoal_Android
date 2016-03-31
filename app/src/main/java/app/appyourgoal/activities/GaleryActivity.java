package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;

import app.appyourgoal.R;
import app.appyourgoal.adapters.ImageGaleryAdapter;
import app.appyourgoal.utils.FontHelper;

/**
 * Created by Dragisa on 10/23/2015.
 */
public class GaleryActivity extends Activity {

    private final static String  FILE_PATCH = "AppYourGoal";
    private File mFile = null;
    private File[] mFileList;

    private TextView mCancel;
    private TextView mVideo;
    private TextView mDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galery_grid_layout);

        //Font helper for setting custom fonts
        FontHelper fontHelper = new FontHelper();

        //Initialize fields
        mCancel = (TextView) findViewById(R.id.galery_cancel);
        mVideo = (TextView) findViewById(R.id.galery_video);
        mDone = (TextView) findViewById(R.id.galery_done);

        //Setting font type
        fontHelper.setTextViewTypeface(mCancel, getApplicationContext());
        fontHelper.setTextViewTypeface(mVideo, getApplicationContext());
        fontHelper.setTextViewTypeface(mDone, getApplicationContext());

        GridView gridView = (GridView) findViewById(R.id.galery_grid_view);

        mFileList = getFileList();
        // Instance of ImageGaleryAdapter Class
        gridView.setAdapter(new ImageGaleryAdapter(this,R.layout.galery_single_layout ,mFileList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("djevtic","Item clicked position: "+position);
                File item = mFileList[position];
                //Create intent
                Log.d("djevtic","Item clicked: "+item.getName());
                mFile = item;
            }
        });
    }

    private File[] getFileList(){
        File f = new File(Environment.getExternalStorageDirectory() + "/"+FILE_PATCH);
        f.mkdirs();
        return f.listFiles();
    }

    public void onDone(View v){
        if(mFile != null){
            Intent i = new Intent(getApplication(), PostYourGoalActivity.class);
            i.putExtra("file", mFile.getName());
            startActivity(i);
        }
    }

    public void onCancelGalery(View v){
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }
}
