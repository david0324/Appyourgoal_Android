package app.appyourgoal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import app.appyourgoal.fragments.initial.InitialFragmentOne;
import app.appyourgoal.fragments.initial.InitialFragmentTwo;

/**
 * Created by Dragisa on 9/21/2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position){
            case 0:
                return InitialFragmentOne.newInstance();
            case 1:
                return InitialFragmentTwo.newInstance();
            case 2:
                return InitialFragmentOne.newInstance();
            case 3:
                return InitialFragmentTwo.newInstance();
            default:
                return InitialFragmentTwo.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }
}
