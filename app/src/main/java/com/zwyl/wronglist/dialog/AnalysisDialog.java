package com.zwyl.wronglist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.zwyl.wronglist.R;

/**
 * 头像选择对话框 拍照 or 相册
 */
public class AnalysisDialog extends Dialog {
    private Activity activity;

    public AnalysisDialog(Activity activity, String data) {
        super(activity, R.style.dialog);
        this.activity = activity;
        View view = View.inflate(activity, R.layout.dialog_analysis, null);

        setContentView(view);

        //底部关闭按钮
        view.findViewById(R.id.tv_dialog_cancle).setOnClickListener(v -> {
            dismiss();
        });
    }
}

