package tm.shker.mohamed.chickengrill.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by mohamed on 07/10/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public static int pos = 0;
    private ArrayList<Fragment> myFragments;

    public ViewPagerAdapter(FragmentManager fm , ArrayList<Fragment> myFragments) {
        super(fm);
        this.myFragments = myFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        String pageTitle = "";
            switch(pos)
            {
                case 0:
                    pageTitle = "עסקיות בורגרים";
                    break;
                case 1:
                    pageTitle = "עסקיות";
                    break;
                case 2:
                    pageTitle = "קומבינציות";
                    break;
                case 3:
                    pageTitle = "מנות בג'בטה";
                    break;
                case 4:
                    pageTitle = "מנות בבגט";
                    break;
                case 5:
                    pageTitle = "תוספות";
                    break;
                case 6:
                    pageTitle = "שתיה קלה";
                    break;
            }
            return pageTitle;
    }


    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        ViewPagerAdapter.pos = pos;
    }
}
