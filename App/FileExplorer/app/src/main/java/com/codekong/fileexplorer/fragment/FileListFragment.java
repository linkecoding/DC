package com.codekong.fileexplorer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.FileListAdapter;
import com.codekong.fileexplorer.base.BaseFragment;
import com.codekong.fileexplorer.util.FileUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by szh on 2017/2/9.
 * 文件列表Fragment
 */

public class FileListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "FileListFragment";
    
    @BindView(R.id.id_start_search)
    TextView mStartSearch;
    @BindView(R.id.id_now_file_path_tv)
    TextView mNowFilePathTv;
    @BindView(R.id.id_file_list_view)
    ListView mFileListView;
    @BindView(R.id.id_empty_view)
    TextView mEmptyView;
    @BindView(R.id.id_file_list_pulltorefresh)
    PullToRefreshLayout mPullToRefreshLayout;

    //文件列表数组
    private File[] mFilesArray;
    //文件列表List
    private ArrayList<File> mFileList = new ArrayList<>();
    //文件ListView适配器
    private FileListAdapter mFileListAdapter;
    //当前的文件路径堆栈
    private Stack<String> mNowPathStack;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_file_list;
    }

    @Override
    protected void initData() {
        String fileNowPath = "";
        //对文件进行过滤和排序
        mFilesArray = FileUtils.filterSortFileByName(Environment.getExternalStorageDirectory().getPath(), true);
        mFileList.clear();
        mFileList.addAll(Arrays.asList(mFilesArray));
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        mNowPathStack = new Stack<>();
        mNowPathStack.push(rootPath);
        fileNowPath = FileUtils.getNowStackPathString(mNowPathStack);
        //设置文件路径显示
        mNowFilePathTv.setText(fileNowPath);
        mFileListAdapter = new FileListAdapter(this.getContext(), mFileList);
        mFileListView.setAdapter(mFileListAdapter);
        mFileListView.setOnItemClickListener(this);
        //设置没有item显示时默认显示的图标
        mFileListView.setEmptyView(mEmptyView);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mFileListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem != 0){
                    disablePullToRefresh(mPullToRefreshLayout, false);
                }else{
                    disablePullToRefresh(mPullToRefreshLayout, true);
                }
            }
        });

        mPullToRefreshLayout.setCanLoadMore(false);
        mPullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                initData();
                mPullToRefreshLayout.finishRefresh();
            }

            @Override
            public void loadMore() {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        File file = mFileList.get(position);
        String fileName = file.getName();
        if (file.isFile()) {
            //如果是File则打开
            int index = fileName.lastIndexOf(".");
            String suffix = fileName.subSequence(index +1, fileName.length()).toString().toLowerCase();
            String type = null;
            if (MimeTypeMap.getSingleton().hasExtension(suffix)){
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
            }
            if (type == null) {
                Toast.makeText(getContext(), R.string.str_no_program_can_open_the_file, Toast.LENGTH_SHORT).show();
                return;
            }
            //打开文件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, type);
            startActivity(intent);
        } else {
            //是目录则进入下级目录
            mNowPathStack.push("/" + fileName);
            showChange(FileUtils.getNowStackPathString(mNowPathStack));
        }
    }

    /**
     * 改变数据源，刷新列表
     */
    public void showChange(String path) {
        mNowFilePathTv.setText(path);
        mFilesArray = FileUtils.filterSortFileByName(path, true);
        mFileList.clear();
        mFileList.addAll(Arrays.asList(mFilesArray));
        mFileListAdapter.updateFileList(mFileList);
    }

    /**
     * 改变数据源，刷新列表
     */
    public void showChange(File[] fileArray) {
        mFilesArray = fileArray;
        mFileList.clear();
        mFileList.addAll(Arrays.asList(mFilesArray));
        mFileListAdapter.updateFileList(mFileList);
    }

    @OnClick(R.id.id_now_file_path_tv)
    public void onViewClicked() {
        //点击返回上级目录
        if (!mNowPathStack.empty()){
            if (mNowPathStack.peek().equals(Environment.getExternalStorageDirectory().getPath())){
                return;
            }
            mNowPathStack.pop();
        }
        showChange(FileUtils.getNowStackPathString(mNowPathStack));
    }

    /**
     * 通过反射禁用下拉刷新
     * @param disable
     */
    private void disablePullToRefresh(PullToRefreshLayout pullToRefreshLayout, boolean disable){
        Class<PullToRefreshLayout> pullToRefreshClass = PullToRefreshLayout.class;
        try {
            Field canRefreshField = pullToRefreshClass.getDeclaredField("canRefresh");
            canRefreshField.setAccessible(true);
            canRefreshField.set(pullToRefreshLayout, disable);
            canRefreshField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {
        //当前在根目录
        if ((Environment.getExternalStorageDirectory().getAbsolutePath()).equals(mNowFilePathTv.getText().toString())){
            //不处理返回键
            return false;
        }else{
            //退到上级目录
            mNowPathStack.pop();
            //刷新目录
            showChange(FileUtils.getNowStackPathString(mNowPathStack));
            return true;
        }
    }
}
