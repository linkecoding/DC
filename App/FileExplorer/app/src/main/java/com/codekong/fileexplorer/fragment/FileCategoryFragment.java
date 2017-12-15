package com.codekong.fileexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.CommonAdapter;
import com.codekong.fileexplorer.adapter.ViewHolder;
import com.codekong.fileexplorer.bean.Category;
import com.codekong.fileexplorer.config.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by szh on 2017/2/9.
 * 文件类别Fragment
 */

public class FileCategoryFragment extends Fragment {
    @BindView(R.id.id_start_search)
    TextView mStartSearchBtn;
    @BindView(R.id.id_category_grid_view)
    GridView mCategoryGridView;
    //存放分类数据
    private List<Category> mCategoryData = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setData();
    }

    /**
     * 初始化一些数据(模拟)
     */
    private void initData() {
        for (int i = 0; i < Constant.FILE_CATEGORY_ICON.length; i++) {
            Category category = new Category();
            category.setCategoryIcon(Constant.FILE_CATEGORY_ICON[i]);
            category.setCategoryName(Constant.FILE_CATEGORY_NAME[i]);
            category.setCategoryNums(1 + "项");
            mCategoryData.add(category);
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
       CommonAdapter<Category> mAdapter = new CommonAdapter<Category>(this.getContext(), mCategoryData, R.layout.category_item) {
            @Override
            public void convert(ViewHolder helper, Category item) {
                helper.setImageResource(R.id.id_category_icon, getResId(item.getCategoryIcon()));
                helper.setText(R.id.id_category_name, item.getCategoryName());
                helper.setText(R.id.id_category_nums, item.getCategoryNums());
            }
        };
        mCategoryGridView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public int getResId(String iconName){
        return getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
    }
}
