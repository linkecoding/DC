package com.codekong.fileexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codekong.fileexplorer.R;
import com.codekong.fileexplorer.bean.FileCard;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 尚振鸿 on 17-12-26. 22:03
 * mail:szh@codekong.cn
 */

public class CloudFileListAdapter extends BaseAdapter {

    //文件类型(文件[-1]/文件夹[1])
    private static final int TYPE_FILE = -1;
    private static final int TYPE_DRECTORY = 1;

    private Context mContext;

    private List<FileCard> mFileCardList;

    public CloudFileListAdapter(Context context, List<FileCard> filCardList){
        this.mContext = context;
        this.mFileCardList = filCardList;
    }

    @Override
    public int getCount() {
        return mFileCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileCard fileCard = mFileCardList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.fileName.setText(fileCard.getName());
        if (fileCard.getType() == TYPE_FILE){
            viewHolder.fileSize.setText(fileCard.getSize());
            viewHolder.fileIcon.setImageResource(R.drawable.ic_file);
            viewHolder.nextDir.setVisibility(View.GONE);
        }else {
            //当前item是目录
            viewHolder.fileSize.setText(R.string.str_folder);
            viewHolder.fileIcon.setImageResource(R.drawable.ic_folder);
            viewHolder.nextDir.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.id_file_icon)
        ImageView fileIcon;
        @BindView(R.id.id_file_name)
        TextView fileName;
        @BindView(R.id.id_file_size)
        TextView fileSize;
        @BindView(R.id.id_next_dir)
        ImageButton nextDir;
        @BindView(R.id.id_select_file_cb)
        CheckBox selectFileCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
