package com.codekong.fileexplorer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionBarContextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.codekong.fileexplorer.R;

/**
 * 自定义ActionMode的外部容器
 * Created by 尚振鸿 on 17-12-20. 18:19
 * mail:szh@codekong.cn
 */

public class CustomActionModeContainer extends RelativeLayout{
    private DisplayMetrics metric = new DisplayMetrics();
    private Context mContext;
    public CustomActionModeContainer(@NonNull Context context) {
        this(context, null);

    }

    public CustomActionModeContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomActionModeContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int customWidthMeasureSpec = widthMeasureSpec;
        if (getParent() instanceof ActionBarContextView) {
            ActionBarContextView actionBarContextView = (ActionBarContextView) getParent();
            View closeLayout = actionBarContextView.findViewById(R.id.action_mode_close_button);

            View actionMenuView = actionBarContextView.getChildAt(2);
            if (null != closeLayout) {
                customWidthMeasureSpec = MeasureSpec.makeMeasureSpec(closeLayout.getMeasuredWidth() + width, mode);
            }
        }

        super.onMeasure(customWidthMeasureSpec, heightMeasureSpec);
    }
}
