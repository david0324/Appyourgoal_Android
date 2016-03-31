package app.appyourgoal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.File;

import app.appyourgoal.R;
import app.appyourgoal.activities.GaleryActivity;

/**
 * Created by Dragisa on 10/23/2015.
 */
public class ImageGaleryAdapter extends ArrayAdapter {

    private Context mContext;
    private File[] mFileList;
    private int mLayoutResourceId;
    private LayoutInflater mInflater;

    private final static String  FILE_PATCH = "AppYourGoal";

    public ImageGaleryAdapter(GaleryActivity galeryActivity, int layoutResourceId ,File[] fileList) {
        super(galeryActivity, layoutResourceId, fileList);
        mLayoutResourceId = layoutResourceId;
        mContext = galeryActivity;
        mFileList = fileList;
        mInflater = (LayoutInflater) galeryActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFileList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = mInflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.grid_layout_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        File item = mFileList[position];
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/" + item.getName(), MediaStore.Images.Thumbnails.MINI_KIND);
        holder.image.setImageBitmap(bm);
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}
