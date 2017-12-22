package com.codekong.fileexplorer.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.SwitchViewPagerAdapter;
import com.codekong.fileexplorer.base.BaseActivity;
import com.codekong.fileexplorer.fragment.CloudFileFragment;
import com.codekong.fileexplorer.fragment.FileCategoryFragment;
import com.codekong.fileexplorer.fragment.FileListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.id_switch_view_pager)
    ViewPager mSwitchViewPager;
    //更多操作按钮
    private ImageView mMoreOperation;

    @Override
    protected void initWindows() {
        super.initWindows();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        ActionBar actionBar = getSupportActionBar();
        View actionBarView = null;
        if (actionBar == null){
            return;
        }
        actionBarView = LayoutInflater.from(this).inflate(R.layout.custom_actionbar_layout, null);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        //设置自定义的布局
        actionBar.setCustomView(actionBarView, layoutParams);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> tabNameList = new ArrayList<>();
        tabNameList.add("分类");
        tabNameList.add("手机");
        tabNameList.add("网盘");
        fragmentList.add(new FileCategoryFragment());
        fragmentList.add(new FileListFragment());
        fragmentList.add(new CloudFileFragment());
        SwitchViewPagerAdapter switchViewPagerAdapter = new SwitchViewPagerAdapter(getSupportFragmentManager(), fragmentList, tabNameList);
        mSwitchViewPager.setAdapter(switchViewPagerAdapter);
        mSwitchViewPager.setOffscreenPageLimit(2);
        TabLayout switchTabLayout = actionBarView.findViewById(R.id.id_switch_tab_layout);
        switchTabLayout.setupWithViewPager(mSwitchViewPager);

        mMoreOperation = actionBarView.findViewById(R.id.id_more_operation);
        mSwitchViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    //只在文件列表页面显示按钮
                    mMoreOperation.setVisibility(View.VISIBLE);
                } else {
                    mMoreOperation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
