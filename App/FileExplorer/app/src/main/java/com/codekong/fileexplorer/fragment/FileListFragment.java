package com.codekong.fileexplorer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.FileListAdapter;
import com.codekong.fileexplorer.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by szh on 2017/2/9.
 * 文件列表Fragment
 */

public class FileListFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.id_start_search)
    TextView mStartSearch;
    @BindView(R.id.id_now_file_path_tv)
    TextView mNowFilePathTv;
    @BindView(R.id.id_file_list_view)
    ListView mFileListView;
    @BindView(R.id.id_empty_view)
    TextView mEmptyView;
    private Unbinder mUnbinder;
    //文件列表数组
    private File[] mFilesArray;
    //文件列表List
    private ArrayList<File> mFileList = new ArrayList<>();
    //文件ListView适配器
    private FileListAdapter mFileListAdapter;
    //文件根路径
    private String mRootPath;
    //当前的文件路径堆栈
    public static Stack<String> mNowPathStack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据并显示
     */
    private void initData() {
        String fileNowPath = "";
        //对文件进行过滤和排序
        mFilesArray = FileUtils.filterSortFileByName(Environment.getExternalStorageDirectory().getPath(), true);
        mFileList.addAll(Arrays.asList(mFilesArray));
        mRootPath = Environment.getExternalStorageDirectory().getPath();
        mNowPathStack = new Stack<>();
        mNowPathStack.push(mRootPath);
        fileNowPath = FileUtils.getNowStackPathString(mNowPathStack);
        //设置文件路径显示
        mNowFilePathTv.setText(fileNowPath);
        mFileListAdapter = new FileListAdapter(this.getContext(), mFileList);
        mFileListView.setAdapter(mFileListAdapter);
        mFileListView.setOnItemClickListener(this);
        //设置没有item显示时默认显示的图标
        mFileListView.setEmptyView(mEmptyView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        File file = mFileList.get(position);
        String fileName = file.getName();
        if (file.isFile()) {
            //如果是File则打开
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);
            int index = fileName.lastIndexOf(".");
            String suffix = fileName.substring(index + 1);
            String type = MimeTypeMap.getSingleton().getExtensionFromMimeType(suffix);
            if (type == null) {
                Toast.makeText(getContext(), R.string.str_no_program_can_open_the_file, Toast.LENGTH_SHORT).show();
                return;
            }
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
}
