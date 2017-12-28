package com.codekong.fileexplorer.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.CloudFileListAdapter;
import com.codekong.fileexplorer.base.BaseFragment;
import com.codekong.fileexplorer.bean.DirInfoModel;
import com.codekong.fileexplorer.bean.FileCard;
import com.codekong.fileexplorer.bean.ResponseModel;
import com.codekong.fileexplorer.config.Constant;
import com.codekong.fileexplorer.config.NetConstant;
import com.codekong.fileexplorer.listener.RequestCallback;
import com.codekong.fileexplorer.util.OkHttpUtil;
import com.codekong.fileexplorer.util.SfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 云端文件Fragment
 * Created by 尚振鸿 on 17-12-21. 20:57
 * mail:szh@codekong.cn
 */

public class CloudFileListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "CloudFileListFragment";

    //文件类型(文件[-1]/文件夹[1])
    private static final int TYPE_FILE = -1;
    private static final int TYPE_DRECTORY = 1;

    @BindView(R.id.id_cloud_file_current_path_tv)
    TextView mCurrentPathTv;
    @BindView(R.id.id_cloud_file_list_view)
    ListView mCloudFileListView;
    @BindView(R.id.id_cloud_file_pulltorefresh)
    PullToRefreshLayout mCloudFilePulltorefresh;
    @BindView(R.id.id_no_cloud_file_view)
    TextView mNoCloudFileView;
    @BindView(R.id.id_cloudfile_loading_framelayout)
    FrameLayout mCloudfileLoadingFramelayout;

    //ListView的Data
    private List<FileCard> mCloudFileCardList;
    //当前的dirId
    private String mCurDirId = "0";
    //当前父级目录的Id
    private String mCurParentDirId = "0";
    //当前文件路径
    private String mCurFilePath = "";
    //当前目录的层级
    private int mCurDirLevel = 1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_cloud_file;
    }


    @Override
    protected void initData() {
        super.initData();

        DirInfoModel model = new DirInfoModel(mCurDirId, "62d5e827-e5aa-457d-b42f-4fe25dcdc124");
        //获取根目录文件夹数据
        getNextDirInfoFromNetwork(model);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mCloudFileListView.setEmptyView(mNoCloudFileView);
        mCloudFileListView.setOnItemClickListener(this);
        mCloudFilePulltorefresh.setCanLoadMore(false);
        mCloudFilePulltorefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                initData();
            }

            @Override
            public void loadMore() {
            }
        });
    }

    /**
     * 从网络请求数据
     */
    public void getNextDirInfoFromNetwork(DirInfoModel model) {
        Gson gson = new Gson();
        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put(NetConstant.PARAMETER_TOKEN, getToken());
        //网络请求
        OkHttpUtil.getInstance().post(NetConstant.BASE_URL + NetConstant.GET_NEXT_DIR_LIST_URL, OkHttpUtil.MEDIA_TYPE_JSON, gson.toJson(model)
                , headerMap, new RequestCallback() {
                    @Override
                    public void onResponse(Response response) {
                        Gson gson = new Gson();
                        List<FileCard> fileCardList = new ArrayList<>();
                        String msg = "";
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                ResponseModel<List<FileCard>> responseModel = gson.fromJson(response.body().string()
                                        .replaceAll("\"", "'"), new TypeToken<ResponseModel<List<FileCard>>>() {
                                }.getType());

                                if (responseModel != null) {
                                    msg = responseModel.getMessage();
                                    if (responseModel.isSucceed()) {
                                        fileCardList = responseModel.getResult();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mCloudFileCardList = fileCardList;
                        setData(fileCardList, msg);
                    }

                    @Override
                    public void onFailure() {
                        //网络错误
                    }
                });
    }

    /**
     * 从SharePreference获取token
     *
     * @return
     */
    private String getToken() {
        final String sfTokenName = Constant.TOEKN_SF_FILE_NAME;
        String token = SfUtil.getFromSharepreference(getContext(), sfTokenName, NetConstant.PARAMETER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            token = "";
        }
        //TODO 获取token
        //return token;
        return "token";
    }

    private void setData(final List<FileCard> fileCardList, final String msg) {
        if (mCloudFileCardList != null && mCloudFileCardList.size() > 0){
            mCurDirLevel = mCloudFileCardList.get(0).getLevel();
            mCurParentDirId = mCloudFileCardList.get(0).getParentId();
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCloudFilePulltorefresh.finishRefresh();
                    mCloudfileLoadingFramelayout.setVisibility(View.GONE);
                    //设置数据更新UI
                    CloudFileListAdapter cloudFileListAdapter = new CloudFileListAdapter(getContext(), fileCardList);
                    mCloudFileListView.setAdapter(cloudFileListAdapter);
                    showToast(msg);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileCard fileCard = mCloudFileCardList.get(position);
        if (fileCard.getType() == TYPE_DRECTORY) {
            mCurFilePath += "/" + fileCard.getName();
            mCurrentPathTv.setText(mCurFilePath);

            DirInfoModel model = new DirInfoModel(fileCard.getId(), "62d5e827-e5aa-457d-b42f-4fe25dcdc124");
            mCurDirId = fileCard.getId();
            mCurParentDirId = fileCard.getParentId();
            mCurDirLevel = fileCard.getLevel();
            //获取下级目录信息
            getNextDirInfoFromNetwork(model);
        }
    }

    /**
     * 展示提示信息
     *
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.id_cloud_file_current_path_tv)
    public void onViewClicked() {
        if (mCurDirLevel <= 1){
            return;
        }
        DirInfoModel model = new DirInfoModel(mCurParentDirId, "62d5e827-e5aa-457d-b42f-4fe25dcdc124");
        //返回上级目录
        backToPreDir(model);
        mCurFilePath = mCurFilePath.substring(0, mCurFilePath.lastIndexOf("/"));
        mCurrentPathTv.setText(mCurFilePath);
    }

    /**
     * 返回上级目录
     */
    private void backToPreDir(DirInfoModel model) {
        Gson gson = new Gson();
        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put(NetConstant.PARAMETER_TOKEN, getToken());
        //网络请求
        OkHttpUtil.getInstance().post(NetConstant.BASE_URL + NetConstant.GET_PRE_DIR_LIST_URL, OkHttpUtil.MEDIA_TYPE_JSON, gson.toJson(model)
                , headerMap, new RequestCallback() {
                    @Override
                    public void onResponse(Response response) {
                        Gson gson = new Gson();
                        List<FileCard> fileCardList = new ArrayList<>();
                        String msg = "";
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                ResponseModel<List<FileCard>> responseModel = gson.fromJson(response.body().string()
                                        .replaceAll("\"", "'"), new TypeToken<ResponseModel<List<FileCard>>>() {
                                }.getType());

                                if (responseModel != null) {
                                    msg = responseModel.getMessage();
                                    if (responseModel.isSucceed()) {
                                        fileCardList = responseModel.getResult();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mCloudFileCardList = fileCardList;
                        setData(fileCardList, msg);
                    }

                    @Override
                    public void onFailure() {
                        //网络错误
                    }
                });
    }
}
