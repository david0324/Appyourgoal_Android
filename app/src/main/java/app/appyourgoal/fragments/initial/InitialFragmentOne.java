package app.appyourgoal.fragments.initial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.appyourgoal.R;
import app.appyourgoal.utils.FontHelper;

/**
 * Created by Dragisa on 9/21/2015.
 */
public class InitialFragmentOne extends Fragment {

    public static InitialFragmentOne newInstance() {
        InitialFragmentOne fragment = new InitialFragmentOne();
        return fragment;
    }

    public InitialFragmentOne(){}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Font helper for setting custom fonts
        FontHelper fontHelper = new FontHelper();


        View rootView = inflater.inflate(R.layout.fragment_initial_first, container, false);
        TextView textOne = (TextView) rootView.findViewById(R.id.initial_fragment_page_text_one);
        TextView textTwo = (TextView) rootView.findViewById(R.id.initial_fragment_page_text_two);
        TextView textThre = (TextView) rootView.findViewById(R.id.initial_fragment_page_text_three);
        textOne.setText("YOU PLAY");
        textOne.setTextSize(55);
        textTwo.setText("YOU SHARE");
        textTwo.setTextSize(55);
        textThre.setText("YOU WIN");
        textThre.setTextSize(55);
        fontHelper.setTextViewTypeface(textOne, getContext());
        fontHelper.setTextViewTypeface(textTwo,getContext());
        fontHelper.setTextViewTypeface(textThre,getContext());
        return rootView;
    }
}
