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
public class InitialFragmentTwo extends Fragment {

    public static InitialFragmentTwo newInstance() {
        InitialFragmentTwo fragment = new InitialFragmentTwo();
        return fragment;
    }

    public InitialFragmentTwo(){}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Font helper for setting custom fonts
        FontHelper fontHelper = new FontHelper();

        View rootView = inflater.inflate(R.layout.fragment_initial, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.initial_fragment_page_text);
        text.setText("Share the videos of the goals you scored (or by the youtube link) with a large community of people crazy about football like you. Every week the most voted goal will win a prize. Here there are no jury of experts, the best goal of the week will be determined exclusively by AppyourGoal’s users!");
        text.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
        fontHelper.setTextViewTypeface(text, getContext());
        return rootView;
    }
}