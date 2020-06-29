package com.zwyl.wronglist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.VideoView;

import com.zwyl.wronglist.R;

/**
 * 头像选择对话框 拍照 or 相册
 */
public class AnswerDialog extends Dialog {
    private Activity activity;

    public AnswerDialog(Activity activity, String mAnswer, String exerciseAnswer, String title) {
        super(activity, R.style.dialog);
        this.activity = activity;
        View view = View.inflate(activity, R.layout.dialog_analysis, null);
        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        WebView webView_participation_detail = (WebView) view.findViewById(R.id.webView_participation_detail);
        WebView webView_participation_answer = (WebView) view.findViewById(R.id.webView_participation_answer);
        if (title.equals("答案")) {
            if (TextUtils.isEmpty(mAnswer) || TextUtils.isEmpty(exerciseAnswer)) {
                webView_participation_answer.loadDataWithBaseURL("", exerciseAnswer, "text/html", "UTF-8", "");
            } else {
                webView_participation_answer.loadDataWithBaseURL(null, "我的答案 : " + mAnswer, "text/html", "UTF-8", null);
                webView_participation_detail.loadDataWithBaseURL(null, "正确答案 : " + exerciseAnswer, "text/html", "UTF-8", null);
            }
        } else {
            webView_participation_answer.loadDataWithBaseURL("", mAnswer, "text/html", "UTF-8", "");
        }
//        WebSettings settings = webView_participation_detail.getSettings();
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setJavaScriptEnabled(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setBlockNetworkImage(true);
//        webView_participation_detail.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    webView_participation_detail.getSettings().setBlockNetworkImage(false);
//                }
//            }
//        });
        tv_dialog_title.setText(title);

        setContentView(view);

        //底部关闭按钮
        view.findViewById(R.id.tv_dialog_cancle).setOnClickListener(v -> {
            dismiss();
        });
    }
}

