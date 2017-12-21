package com.codekong.fileexplorer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.fragment.CloudFileFragment;
import com.codekong.fileexplorer.fragment.FileCategoryFragment;
import com.codekong.fileexplorer.fragment.FileListFragment;

/**
 * Created by szh on 2017/2/9.
 */

public class SwitchViewPagerAdapter extends FragmentPagerAdapter {
    private String[] mTabName = new String[3];
    public SwitchViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mTabName[0] = context.getString(R.string.str_category);
        mTabName[1] = context.getString(R.string.str_phone);
        mTabName[2] = context.getString(R.string.str_network_disk);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return new FileListFragment();
        }
        if (position == 2){
            return new CloudFileFragment();
        }
        if (position == 0){
            return new FileCategoryFragment();
        }
        return new FileCategoryFragment();
    }

    @Override
    public int getCount() {
        return mTabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabName[position];
    }
}
