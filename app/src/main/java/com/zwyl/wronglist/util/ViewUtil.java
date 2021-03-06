package com.zwyl.wronglist.util;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zwyl.wronglist.App;
import com.zwyl.wronglist.R;
import com.zwyl.wronglist.main.subject.BeanAbc;
import com.zwyl.wronglist.main.subject.BeanSubject;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by BinBinWang on 2018/1/19.
 */

public class ViewUtil {

    public static final int dp9 = DensityUtil.dip2px(App.getContext(), 9);
    public static final int dp19 = DensityUtil.dip2px(App.getContext(), 19);
    public static final int dp420 = DensityUtil.dip2px(App.getContext(), 420);


    //单列文字
    public static View getTextView(CharSequence text) {
        TextView tv = new TextView(App.getContext());
        tv.setText(text);
        tv.setTextSize(27);
        tv.setTextColor(App.getContext().getResources().getColor(R.color.gray_3333));
        return tv;
    }

    //两列文字
    public static View getTextViewTwo(String str1, String str2) {
        LinearLayout linearLayout = new LinearLayout(App.mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(App.getContext());
        tv.setText(str1);
        tv.setTextSize(18);
        tv.setPadding(19, 0, 0, 0);
        tv.setTextColor(App.getContext().getResources().getColor(R.color.gray_3333));
        TextView tvStr = new TextView(App.getContext());
        tvStr.setPadding(19, 0, 0, 0);
        tvStr.setText(str2);
        tvStr.setTextSize(18);
        tvStr.setTextColor(App.getContext().getResources().getColor(R.color.gray_8888));
        // tvStr.setOnClickListener(listener);
        linearLayout.addView(tv);
        linearLayout.addView(tvStr);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    //横向线性布局
    public static LinearLayout get_ll_horizontal() {
        LinearLayout linearLayout = new LinearLayout(App.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp420);
        //        params.topMargin = ViewUtil.dp15;
        //        params.bottomMargin = ViewUtil.dp18;
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }



    public interface OncheckedClick {
        void OnChecked(int indexNum, String optionId);

    }
    public interface OnMultipleClick {
        void OnChecked(boolean isChecked,int indexNum, String optionId);
    }

}
