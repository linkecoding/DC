package com.codekong.fileexplorer.fragment;

import android.widget.ListView;
import android.widget.TextView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.base.BaseFragment;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import butterknife.BindView;

/**
 * 云端文件Fragment
 * Created by 尚振鸿 on 17-12-21. 20:57
 * mail:szh@codekong.cn
 */

public class CloudFileListFragment extends BaseFragment {

    @BindView(R.id.id_cloud_file_current_path_tv)
    TextView mCurrentPathTv;
    @BindView(R.id.id_cloud_file_list_view)
    ListView mCloudFileListView;
    @BindView(R.id.id_cloud_file_pulltorefresh)
    PullToRefreshLayout mCloudFilePulltorefresh;
    @BindView(R.id.id_no_cloud_file_view)
    TextView mNoCloudFileView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_cloud_file;
    }


    @Override
    protected void initData() {
        super.initData();
        //网络请求
        //OkHttpUtil.getInstance().post();
        mCloudFileListView.setEmptyView(mNoCloudFileView);
    }
}
