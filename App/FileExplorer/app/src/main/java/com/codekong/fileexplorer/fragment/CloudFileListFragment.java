package com.codekong.fileexplorer.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.adapter.CloudFileListAdapter;
import com.codekong.fileexplorer.base.BaseFragment;
import com.codekong.fileexplorer.bean.FileCard;
import com.codekong.fileexplorer.bean.ResponseModel;
import com.codekong.fileexplorer.config.NetConstant;
import com.codekong.fileexplorer.listener.RequestCallback;
import com.codekong.fileexplorer.util.OkHttpUtil;
import com.codekong.fileexplorer.util.SfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Response;

/**
 * 云端文件Fragment
 * Created by 尚振鸿 on 17-12-21. 20:57
 * mail:szh@codekong.cn
 */

public class CloudFileListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "CloudFileListFragment";
    
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
        getNextDirInfoFromNetwork("0");
        mCloudFileListView.setEmptyView(mNoCloudFileView);
        mCloudFileListView.setOnItemClickListener(this);
    }


    /**
     * 从网络请求数据
     */
    public void getNextDirInfoFromNetwork(String id) {

        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put("token", getToken());
        //网络请求
        OkHttpUtil.getInstance().post(NetConstant.BASE_URL + NetConstant.GET_NEXT_DIR_LIST_URL, OkHttpUtil.MEDIA_TYPE_JSON,
                id, headerMap, new RequestCallback() {
                    @Override
                    public void onResponse(Response response) {
                        Gson gson = new Gson();
                        List<FileCard> fileCardList = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null){
                            try {
                                ResponseModel<List<FileCard>> responseModel = gson.fromJson(response.body().string().replaceAll("\"", "'"), new TypeToken<ResponseModel<List<FileCard>>>(){}.getType());
                                if (responseModel != null){
                                    fileCardList = responseModel.getResult();
                                }else{
                                    Log.e(TAG, "onResponse:空 ");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setData(fileCardList);
                    }

                    @Override
                    public void onFailure() {
                        //网络错误
                    }
                });
    }

    /**
     * 从SharePreference获取token
     * @return
     */
    private String getToken(){
        final String sfTokenName = "sf_token";
        String token = SfUtil.getFromSharepreference(getContext(), sfTokenName, "token");
        if (TextUtils.isEmpty(token)){
            token = "";
        }
        //return token;
        return "token";
    }

    private void setData(final List<FileCard> fileCardList){
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //设置数据更新UI
                    CloudFileListAdapter cloudFileListAdapter = new CloudFileListAdapter(getContext(), fileCardList);
                    mCloudFileListView.setAdapter(cloudFileListAdapter);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
