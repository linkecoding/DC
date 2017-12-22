package com.codekong.fileexplorer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by szh on 2017/2/9.
 */

public class SwitchViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "SwitchViewPagerAdapter";

    private List<String> mTabNameList;
    private List<Fragment> mFragmentList;
    public SwitchViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabNameList) {
        super(fm);
        this.mTabNameList = tabNameList;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e(TAG, "getItem: " + position);
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNameList.get(position);
    }
}
