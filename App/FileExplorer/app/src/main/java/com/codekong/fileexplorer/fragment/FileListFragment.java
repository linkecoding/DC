package com.codekong.fileexplorer.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.activity.MainActivity;
import com.codekong.fileexplorer.adapter.FileListAdapter;
import com.codekong.fileexplorer.base.BaseFragment;
import com.codekong.fileexplorer.util.FileUtils;
import com.codekong.fileexplorer.util.ViewUtils;
import com.codekong.fileexplorer.view.OperationMenuPopupWindow;
import com.codekong.fileexplorer.view.SortMenuPopupWindow;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by szh on 2017/2/9.
 * 文件列表Fragment
 */

public class FileListFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.MultiChoiceModeListener, View.OnClickListener {
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

    //当前选中的数量
    private TextView mSelectedCountTv;
    //ActionMode多选模式
    private ActionMode mSelectFileActionMode;
    //选中的position集合
    private Set<Integer> mCheckedPos = new HashSet<>();
    //全选按钮
    private Button mSelectAllBtn;
    //取消按钮
    private Button mSelectCancelBtn;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_file_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        ActionBar actionBar = null;
        if (getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            actionBar = mainActivity.getSupportActionBar();
        }
        if (actionBar == null){
            return;
        }
        final View actionBarView = actionBar.getCustomView();
        ImageView moreOperationView = actionBarView.findViewById(R.id.id_more_operation);
        moreOperationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OperationMenuPopupWindow operationMenuPopupWindow = new OperationMenuPopupWindow(FileListFragment.this);
                operationMenuPopupWindow.showAtLocation(actionBarView, Gravity.TOP, 0, 0);
                operationMenuPopupWindow.setOnWindowItemClickListener(new OperationMenuPopupWindow.OnWindowItemClickListener() {
                    @Override
                    public void closeMenu() {
                        FileListFragment.this.closeMenu(operationMenuPopupWindow);
                    }

                    @Override
                    public void sort(String path) {
                        FileListFragment.this.closeMenu(operationMenuPopupWindow);
                        showSortMethodMenu();
                    }

                    @Override
                    public void newFolder(final String path) {
                        FileListFragment.this.newFolder(path, operationMenuPopupWindow);
                    }

                    @Override
                    public void showHideFolder(String path, boolean showHideFile) {
                        FileListFragment.this.closeMenu(operationMenuPopupWindow);
                        notifyDataChange(FileUtils.filterSortFileByName(path, !showHideFile));
                    }
                });

            }
        });
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
        mFileListView.setOnItemLongClickListener(this);
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
                    ViewUtils.disablePullToRefresh(mPullToRefreshLayout, false);
                }else{
                    ViewUtils.disablePullToRefresh(mPullToRefreshLayout, true);
                }
            }
        });
        //禁用上拉加载
        mPullToRefreshLayout.setCanLoadMore(false);
        mPullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                //刷新当前目录
                notifyDataChange(mNowFilePathTv.getText().toString());
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
            notifyDataChange(FileUtils.getNowStackPathString(mNowPathStack));
        }
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
        //刷新文件列表显示
        notifyDataChange(FileUtils.getNowStackPathString(mNowPathStack));
    }

    /**
     * 改变数据源，刷新列表
     */
    public void notifyDataChange(final String path) {
        mNowFilePathTv.setText(path);
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                mFilesArray = FileUtils.filterSortFileByName(path, true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFileList.clear();
                        mFileList.addAll(Arrays.asList(mFilesArray));
                        mFileListAdapter.updateFileList(mFileList);
                        //刷新完成
                        mPullToRefreshLayout.finishRefresh();
                    }
                });
            }
        });
    }

    /**
     * 改变数据源，刷新列表
     */
    public void notifyDataChange(File[] fileArray) {
        mFilesArray = fileArray;
        mFileList.clear();
        mFileList.addAll(Arrays.asList(mFilesArray));
        mFileListAdapter.updateFileList(mFileList);
    }

    /**
     * 弹出新建文件夹对话框
     * @param path
     * @param operationMenuPopupWindow
     */
    private void newFolder(final String path, OperationMenuPopupWindow operationMenuPopupWindow) {
        FileListFragment.this.closeMenu(operationMenuPopupWindow);
        final View view = (LinearLayout) getLayoutInflater().inflate(R.layout.input_layout, null);
        //新文件名输入框
        final EditText et = view.findViewById(R.id.id_input_ed);
        //自定义弹出框标题
        final TextView titleTv = new TextView(FileListFragment.this.getContext());
        titleTv.setText(FileListFragment.this.getString(R.string.str_new_folder));
        titleTv.setTextSize(16);
        titleTv.setGravity(Gravity.CENTER_HORIZONTAL);
        new AlertDialog.Builder(FileListFragment.this.getActivity())
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(FileListFragment.this.getString(R.string.str_new_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(et.getText())) {
                            File file = new File(path, et.getText().toString());
                            if (!file.exists()) {
                                if (file.mkdirs()) {
                                    //创建文件夹成功,刷新目录显示
                                    notifyDataChange(FileUtils.filterSortFileByName(path, true));
                                    showToast(getString(R.string.str_folder_create_success));
                                } else {
                                    showToast(getString(R.string.str_folder_create_failed));
                                }
                            } else {
                                //文件夹已经存在
                                showToast(getString(R.string.str_folder_exist));
                            }
                        }
                    }
                })
                .setNegativeButton(FileListFragment.this.getString(R.string.str_cancel), null)
                .show();
    }

    /**
     * 展示排序方式菜单
     */
    private void showSortMethodMenu() {
        if (getActivity().getWindow() == null){
            return;
        }
        final View mainActivityView = getActivity().getWindow().getDecorView().getRootView();
        final SortMenuPopupWindow sortMenuPopupWindow = new SortMenuPopupWindow(this);
        sortMenuPopupWindow.showAtLocation(mainActivityView.findViewById(R.id.id_main_activity), Gravity.TOP, 0, 0);
        sortMenuPopupWindow.setOnSortItemClickListener(new SortMenuPopupWindow.OnSortItemClickListener() {
            @Override
            public void closeMenu() {
                FileListFragment.this.closeMenu(sortMenuPopupWindow);
            }

            @Override
            public void sortByName(String path) {
                FileListFragment.this.closeMenu(sortMenuPopupWindow);
                notifyDataChange(FileUtils.filterSortFileByName(path, true));
            }

            @Override
            public void sortBySizeDesc(String path) {
                FileListFragment.this.closeMenu(sortMenuPopupWindow);
                //从大到小排序
                notifyDataChange(FileUtils.filterSortFileBySize(path, true));
            }

            @Override
            public void sortBySizeAsc(String path) {
                FileListFragment.this.closeMenu(sortMenuPopupWindow);
                //从大到小排序
                notifyDataChange(FileUtils.filterSortFileBySize(path, false));
            }

            @Override
            public void sortByModifyDate(String path) {
                FileListFragment.this.closeMenu(sortMenuPopupWindow);
                //从大到小排序
                notifyDataChange(FileUtils.filterSortFileByLastModifiedTime(path));
            }
        });
    }

    /**
     * 关闭隐藏下拉菜单
     */
    private void closeMenu(PopupWindow popupWindow) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
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
            notifyDataChange(FileUtils.getNowStackPathString(mNowPathStack));
            return true;
        }
    }

    /**
     * 展示提示信息
     * @param msg
     */
    private void showToast(String msg){
        Toast.makeText(FileListFragment.this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //开启ListView的多选模式(该种多选模式下自动屏蔽onClick)
        mFileListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        //设置监听事件
        mFileListView.setMultiChoiceModeListener(this);
        //不再调用click
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mSelectFileActionMode = mode;
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_select_action_mode_layout, null);
        mSelectAllBtn = customView.findViewById(R.id.id_select_all_btn);
        mSelectCancelBtn = customView.findViewById(R.id.id_cancel_select_btn);
        mSelectAllBtn.setOnClickListener(this);
        mSelectCancelBtn.setOnClickListener(this);

        mSelectedCountTv = customView.findViewById(R.id.id_selected_count_tv);
        mode.setCustomView(customView);

        //显示每个item的多选的小圆点
        mFileListAdapter.updateFileList(mFileList, true, null);
        return true;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (mCheckedPos.contains(position)){
            mCheckedPos.remove(position);
        }else{
            mCheckedPos.add(position);
        }
        if (mCheckedPos.size() == mFileListAdapter.getCount()){
            //全选模式下改为全不选
            mSelectAllBtn.setText(R.string.str_unchecked_all);
        }else{
            mSelectAllBtn.setText(R.string.str_check_all);
        }
        mFileListAdapter.updateFileList(mFileList, true, mCheckedPos);
        mSelectedCountTv.setText(String.format(getString(R.string.str_checked_count), mCheckedPos.size()));
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        //当菜单项被点击时
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //清除多选状态
        mFileListView.clearChoices();
        mCheckedPos.clear();
        mFileListAdapter.updateFileList(mFileList, false, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_select_all_btn:
                Button button = (Button)v;
                if (mCheckedPos.size() == mFileListAdapter.getCount()){
                    //当前处于全选状态,点击之后应该取消全选状态
                    mFileListAdapter.updateFileList(mFileList, true, null);
                    mCheckedPos.clear();
                    button.setText(R.string.str_check_all);
                }else{
                    mCheckedPos.clear();
                    for (int i = 0; i < mFileListAdapter.getCount(); i++) {
                        //mFileListView.setItemChecked(i, true);
                        mCheckedPos.add(i);
                    }
                    mFileListAdapter.updateFileList(mFileList, true, mCheckedPos);
                    button.setText(R.string.str_unchecked_all);
                }
                mSelectedCountTv.setText(String.format(getString(R.string.str_checked_count), mCheckedPos.size()));
                break;
            case R.id.id_cancel_select_btn:
                //关闭ActionMode
                mSelectFileActionMode.finish();
                break;
            default:

        }
    }
}
