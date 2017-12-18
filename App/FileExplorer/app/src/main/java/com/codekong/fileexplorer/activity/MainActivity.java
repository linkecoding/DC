package com.codekong.fileexplorer.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.SwitchViewPagerAdapter;
import com.codekong.fileexplorer.base.BaseActivity;
import com.codekong.fileexplorer.util.ViewUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.id_more_operation)
    ImageView mMoreOperation;
    @BindView(R.id.id_switch_tab_layout)
    TabLayout mSwitchTabLayout;
    @BindView(R.id.id_switch_view_pager)
    ViewPager mSwitchViewPager;
    //当前被选中的Fragment的position
    private int mCurrentFragmentPosition = 0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //设置状态栏的白底黑字
        ViewUtils.miuiSetStatusBarLightMode(getWindow(), true);
        SwitchViewPagerAdapter switchViewPagerAdapter = new SwitchViewPagerAdapter(this, getSupportFragmentManager());
        mSwitchViewPager.setAdapter(
                switchViewPagerAdapter);
        mSwitchTabLayout.setupWithViewPager(mSwitchViewPager);

        mSwitchViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentFragmentPosition = position;
                if (position == 1) {
                    //只在文件列表页面显示按钮
                    mMoreOperation.setVisibility(View.VISIBLE);
                } else {
                    mMoreOperation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
