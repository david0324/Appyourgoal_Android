package app.appyourgoal.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.appyourgoal.R;
import app.appyourgoal.activities.ProfileActivity;
import app.appyourgoal.utils.StaticURL;

/**
 * Created by Dragisa on 11/15/2015.
 */
public class PrizeFragment  extends Fragment {

    private ImageView mPrizeImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_prize_layout, container, false);
        ProfileActivity activity = (ProfileActivity) getActivity();
        mPrizeImage = (ImageView) view.findViewById(R.id.profile_fragment_prize_image);
        if(activity.mPrize != null){
            Picasso.with(activity).load(StaticURL.SERVER_URL + activity.mPrize.getPrizePicture()).into(mPrizeImage);
        }
        return view;
    }
}
