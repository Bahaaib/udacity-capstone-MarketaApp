package com.example.bahaa.marketa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bahaa.marketa.Books.BookFragment;
import com.example.bahaa.marketa.Games.GameFragment;
import com.example.bahaa.marketa.Movies.MovieFragment;

/**
 * Created by Bahaa on 12/16/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    //Setting up the View Pager with tabs
    private int mTabsNum;
    public PagerAdapter(FragmentManager fm, int tabsNum) {
        super(fm);
        this.mTabsNum = tabsNum;
    }

    //Here we control the flow of the pager, What Fragment to go on clicking to which Tab..
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GameFragment();
            case 1:
                return new MovieFragment();
            case 2:
                return new BookFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabsNum;
    }
}
