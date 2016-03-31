package app.appyourgoal.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.appyourgoal.R;

/**
 * Created by Dragisa on 10/19/2015.
 */
public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blank_fragment, container, false);
        return view;
    }
}
