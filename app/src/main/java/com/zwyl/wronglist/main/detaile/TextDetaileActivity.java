package com.zwyl.wronglist.main.detaile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mayigeek.frame.http.state.HttpSucess;
import com.zwyl.wronglist.App;
import com.zwyl.wronglist.R;
import com.zwyl.wronglist.base.BaseActivity;
import com.zwyl.wronglist.base.ComFlag;
import com.zwyl.wronglist.base.adapter.CommonAdapter;
import com.zwyl.wronglist.base.adapter.ViewHolder;
import com.zwyl.wronglist.dialog.AddNoteDialog;
import com.zwyl.wronglist.dialog.AnswerDialog;
import com.zwyl.wronglist.dialog.TitleDialog;
import com.zwyl.wronglist.dialog.bean.BeanAllYear;
import com.zwyl.wronglist.dialog.popwindow.PopupWindowA;
import com.zwyl.wronglist.dialog.popwindow.PopupWindowB;
import com.zwyl.wronglist.dialog.popwindow.PopupWindowC;
import com.zwyl.wronglist.http.ApiUtil;
import com.zwyl.wronglist.main.BeanHomeGrid;
import com.zwyl.wronglist.main.BeanTextBookGrid;
import com.zwyl.wronglist.main.subject.BeanSubject;
import com.zwyl.wronglist.service.UserService;
import com.zwyl.wronglist.util.DensityUtil;
import com.zwyl.wronglist.util.MyWebViewClient;
import com.zwyl.wronglist.util.Utils;
import com.zwyl.wronglist.viewstate.ViewControlUtil;
import com.zwyl.wronglist.viewstate.treelist.FileBean;
import com.zwyl.wronglist.viewstate.treelist.Node;
import com.zwyl.wronglist.viewstate.treelist.SimpleTreeAdapter;
import com.zwyl.wronglist.viewstate.treelist.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class TextDetaileActivity extends BaseActivity {

    @BindView(R.id.tv_textclass)
    TextView tvTextclass;

    @BindView(R.id.detaile_recyclerview)
    RecyclerView detaileRecyclerview;
    List mlist = new ArrayList<com.zwyl.wronglist.main.detaile.BeanCatelog>();
    @BindView(R.id.iv_detaile_select)
    ImageView ivDetaileSelect;
    @BindView(R.id.ll_detaile_select)
    LinearLayout llDetaileSelect;
    @BindView(R.id.catalog_lisitview)
    ListView catalogLisitview;
    private String textBookId;
    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private TreeListViewAdapter mAdapter;
    private UserService api;
    private List<BeanAllYear> years;
    private String timeasc = ComFlag.DESC;
    private int timeAscTag=0;//0代表降序,1代表升序
    private boolean catelogIsclick;
    private String clickId;//目录是否点击,有值点击.无值未点击,默认未点击(为了刷新详情时判断刷新默认数据还是选中后的数据 )
    private String schoolSubjectId, schoolTextBookName;
    private Activity context;
    private CommonAdapter mAdapterDetail;
    private String token;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_textdetaile;
    }

    @Override
    protected void initView() {
        context = TextDetaileActivity.this;
        super.initView();

        ivDetaileSelect.setImageResource(R.mipmap.selecte);
        textBookId = getIntent().getStringExtra("textBookId");
        schoolSubjectId = getIntent().getStringExtra("schoolSubjectId");
        token = getIntent().getStringExtra("token");
        schoolTextBookName = getIntent().getStringExtra("schoolTextBookName");
        setHeadView();
        //目录列表
        if (!TextUtils.isEmpty(schoolTextBookName))
            tvTextclass.setText(schoolTextBookName);
        else
            tvTextclass.setText("学科");
        try {
            mAdapter = new SimpleTreeAdapter<FileBean>(catalogLisitview, this, mDatas, 10);
            catalogLisitview.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //目录点击事件
        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
//                if (node.isLeaf()) {
                clickId = node.getClickId();
                TextDetaileActivity.this.getLeftItemclickData(clickId);
                catelogIsclick = true;
//                }
            }
        });
        //详情列表
        LinearLayoutManager linearLayoutManagerDetaile = new LinearLayoutManager(App.mContext, OrientationHelper.VERTICAL, false);
        detaileRecyclerview.setLayoutManager(linearLayoutManagerDetaile);
        linearLayoutManagerDetaile.setSmoothScrollbarEnabled(true);
        linearLayoutManagerDetaile.setAutoMeasureEnabled(true);
        detaileRecyclerview.setLayoutManager(linearLayoutManagerDetaile);
        detaileRecyclerview.setHasFixedSize(true);
        detaileRecyclerview.setNestedScrollingEnabled(false);
        detaileRecyclerview.setAdapter(mAdapterDetail = new CommonAdapter<BeanSubject>(App.mContext, R.layout.item_subject, mlist) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void convert(ViewHolder holder, BeanSubject beanSubject, int position) {
                holder.setText(R.id.tv_item_subject_type, Utils.getsubjectType(beanSubject.exerciseTypeCode));
//                int workType = beanSubject.workSupportType;
                int workType = beanSubject.workType;
                int exerciseTypeCode = beanSubject.exerciseTypeCode;
                String exerciseAnswer = beanSubject.exerciseAnswer;
                String exerciseAnalysis = beanSubject.exerciseAnalysis;
                String exerciseExplainFileUri = beanSubject.exerciseExplainFileUri;
                String exercisesTitle = beanSubject.exercisesTitle;
                TextView tv_item_subject_type = holder.getView(R.id.tv_item_subject_type);
                VideoView video_item_subject = holder.getView(R.id.video_item_subject);
                TextView tv_item_subject_text = holder.getView(R.id.tv_item_subject_text);
                holder.setText(R.id.tv_item_subject_time, beanSubject.createTime);
                LinearLayout ll_item_subject_listen = holder.getView(R.id.ll_item_subject_listen);
                LinearLayout ll_item_subject_answer = holder.getView(R.id.ll_item_subject_answer);
                LinearLayout ll_item_subject_look = holder.getView(R.id.ll_item_subject_look);
                LinearLayout ll_item_subject_addFalse = holder.getView(R.id.ll_item_subject_addFalse);
                LinearLayout ll_item_subject_shiftout = holder.getView(R.id.ll_item_subject_shiftout);
                WebView webView_participation_detail = holder.getView(R.id.webView_participation_detail);
                ll_item_subject_addFalse.setSelected(true);
                webView_participation_detail.setInitialScale(10);
                webView_participation_detail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webView_participation_detail.setHorizontalScrollBarEnabled(false);
                webView_participation_detail.setHorizontalScrollbarOverlay(true);

                WebSettings webSettings = webView_participation_detail.getSettings();
//                webSettings.setDefaultFontSize(16);
                webSettings.setSupportZoom(true);
                webSettings.setUseWideViewPort(true);
//                webSettings.setBuiltInZoomControls(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setBlockNetworkImage(false);
                webView_participation_detail.setWebViewClient(new MyWebViewClient(webView_participation_detail));

                //是否有听讲解内容
                if (TextUtils.isEmpty(exerciseExplainFileUri)) {
                    ll_item_subject_listen.setSelected(false);
                    ll_item_subject_listen.setClickable(false);
                } else {
                    ll_item_subject_listen.setClickable(true);
                    ll_item_subject_listen.setSelected(true);
                }
                //是否有正确答案
                if (TextUtils.isEmpty(exerciseAnswer)) {
                    ll_item_subject_answer.setSelected(false);
                    ll_item_subject_answer.setClickable(false);
                } else {
                    ll_item_subject_answer.setSelected(true);
                    ll_item_subject_answer.setClickable(true);
                }
                if (TextUtils.isEmpty(exerciseAnalysis)) {
                    ll_item_subject_look.setSelected(false);
                    ll_item_subject_look.setClickable(false);
                } else {
                    ll_item_subject_look.setClickable(true);
                    ll_item_subject_look.setSelected(true);
                }
                video_item_subject.setVisibility(View.GONE);
                webView_participation_detail.setVisibility(View.GONE);
                if (workType == ComFlag.NumFlag.WORKTYPE_BANK) {
                    webView_participation_detail.setVisibility(View.VISIBLE);
                    tv_item_subject_type.setText("网络作业:" + beanSubject.workName + "(题库作业) " + Utils.getsubjectType(exerciseTypeCode));
                    if (!TextUtils.isEmpty(beanSubject.fileWorkFileUri))
                        webView_participation_detail.loadUrl("http://ow365.cn/?i=18074&ssl=1&furl=" + beanSubject.fileWorkFileUri);
                    else
                        webView_participation_detail.loadDataWithBaseURL(null, exercisesTitle, "text/html", "UTF-8", null);
                } else if (workType == ComFlag.NumFlag.WORKTYPE_ADJUNCT) {
                    webView_participation_detail.setVisibility(View.VISIBLE);
                    tv_item_subject_type.setText("网络作业:" + beanSubject.workName + "(附件作业) " + Utils.getsubjectType(exerciseTypeCode));
                    if (!TextUtils.isEmpty(beanSubject.fileWorkFileUri))
                        webView_participation_detail.loadUrl("http://ow365.cn/?i=18074&ssl=1&furl=" + beanSubject.fileWorkFileUri);
                } else if (workType == ComFlag.NumFlag.WORKTYPE_COACH) {
                    if (beanSubject.workSupportType == ComFlag.NumFlag.WORK_SUPPORT_VEDIO) {
                        tv_item_subject_type.setText("作业辅导:" + beanSubject.workName + "(视频) " + Utils.getsubjectType(exerciseTypeCode));
                        video_item_subject.setVisibility(View.VISIBLE);
                        webView_participation_detail.setVisibility(View.GONE);
                        video_item_subject.setVideoPath(beanSubject.microCourseUri);
                        video_item_subject.requestFocus();
                        video_item_subject.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                video_item_subject.seekTo(1);
                                video_item_subject.pause();
                            }
                        });
                    } else if (beanSubject.workSupportType == ComFlag.NumFlag.WORK_SUPPORT_EXERCISE) {
                        webView_participation_detail.setVisibility(View.VISIBLE);
                        tv_item_subject_type.setText("作业辅导:" + beanSubject.workName + "(习题)" + Utils.getsubjectType(exerciseTypeCode));
//                        tv_item_subject_text.setVisibility(View.VISIBLE);
//                        tv_item_subject_text.setText(Html.fromHtml(exercisesTitle));
                        webView_participation_detail.loadDataWithBaseURL(null, exercisesTitle, "text/html", "UTF-8", null);
                    }
                }
                //答案
                ll_item_subject_answer.setOnClickListener((View v) -> {
                    if (!TextUtils.isEmpty(exerciseAnswer)) {
                        if (Utils.getsubjectType(exerciseTypeCode).equals("判断题")) {
                            new AnswerDialog(context, Utils.getJudge(beanSubject.studentAnswerOptionId), Utils.getJudge(exerciseAnswer), "答案").show();
                        } else
                            new AnswerDialog(context, beanSubject.studentAnswerOptionId, exerciseAnswer, "答案").show();
                    } else showToast("暂无答案");
                });
                //听讲解点击
                ll_item_subject_listen.setOnClickListener((View v) -> {
                    if (!TextUtils.isEmpty(beanSubject.exerciseExplainFileUri)) {
                        showToast("听讲解");
                        Intent intent = new Intent(TextDetaileActivity.this, VideoPlayActivity.class);
                        intent.putExtra("videoPath", beanSubject.exerciseExplainFileUri);
                        startActivity(intent);
                    } else {
                        showToast("无讲解视频");
                    }
                });
                //看解析
                ll_item_subject_look.setOnClickListener((View v) -> {
                    if (!TextUtils.isEmpty(exerciseAnalysis))
                        new AnswerDialog(context, exerciseAnalysis, "", "解析").show();
                    else showToast("暂无解析");
                });
                //加入错题集
                ll_item_subject_addFalse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddNoteDialog(context, beanSubject.reason, new AddNoteDialog.OnClickListener() {
                            @Override
                            public void onClick(String etString) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("workId", beanSubject.workId);
                                map.put("exerciseId", beanSubject.exerciseId);
                                map.put("reason", etString);
                                map.put("workType", beanSubject.workType + "");
                                ApiUtil.doDefaultApi(api.addMistakesCollection(map), new HttpSucess<String>() {
                                    @Override
                                    public void onSucess(String data) {
                                        showToast("加入成功");
                                        getCommonDate();
                                    }
                                });
                            }
                        }).show();
                    }
                });
                //移除错题集
                ll_item_subject_shiftout.setOnClickListener(v -> new TitleDialog(context, "确定讲题目移除错题集?", new TitleDialog.OnclickListener() {
                    @Override
                    public void OnSure() {
                        ApiUtil.doDefaultApi(api.deleteMistakesCollection(beanSubject.workId, beanSubject.exerciseId), new HttpSucess<String>() {
                            @Override
                            public void onSucess(String data) {
                                getCommonDate();
                            }
                        });
                    }

                    @Override
                    public void OnCancle() {

                    }
                }).show());
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        api = ApiUtil.createDefaultApi(UserService.class, token);
        //左边目录列表
        getcatelogList(textBookId);
        getDefaltDetaileData();


        //获取所有学年列表
        ApiUtil.doDefaultApi(api.allYearsByStudentId(null), new HttpSucess<List<BeanAllYear>>() {
            @Override
            public void onSucess(List<BeanAllYear> data) {
                years = data;
            }
        }, ViewControlUtil.create2Dialog(this));

    }

    private void getDefaltDetaileData() {
        //默认请求详情列表
        ApiUtil.doDefaultApi(api.selectExercisesCollectionByTextBookId(textBookId, timeasc), new HttpSucess<List<BeanSubject>>() {
            @Override
            public void onSucess(List<BeanSubject> data) {
                mAdapterDetail.setDatas(data);
                ivDetaileSelect.setImageResource(R.mipmap.selecte);
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    private void getcatelogList(String BookId) {
        //左边目录列表
        ApiUtil.doDefaultApi(api.selectTextBookChapter(textBookId), new HttpSucess<List<ResultInfoBean>>() {
            @Override
            public void onSucess(List<ResultInfoBean> data) {
                mDatas.clear();
                traveTree(data);
                mAdapter.refreshData(mDatas, 1);
                mAdapter.notifyDataSetChanged();
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    private void traveTree(List<ResultInfoBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            ResultInfoBean resultInfoBean = dataList.get(i);
            FileBean fileBean = new FileBean(resultInfoBean.getTextBookChapterId(), resultInfoBean.getTextBookChapterParentId(), resultInfoBean.getTextBookChapterName(), resultInfoBean.getTextBookChapterId() + "");
            mDatas.add(fileBean);
            traveTree(dataList.get(i).getChildrenList());
        }
    }

    //点击事件
    @OnClick({R.id.ll_detaile_select, R.id.detaile_recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_detaile_select:
                showPopWindow(ComFlag.PopFlag.TITLE);
                break;
        }
    }

    /**
     *
     */
    //设置顶部view显示及点击事件
    private void setHeadView() {
        setTitleCenter(schoolTextBookName);
        setShowLeftHead(true);//左边顶部按钮
        setShowRightHead(true);//右边顶部按钮
        setShowFilter(false);//日历筛选
        setShowLogo(false);//logo筛选
        setShowRefresh(true);//刷新
        setRefreshClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//刷新点击
                if (clickId != null)
                    getLeftItemclickData(clickId);
                else {
                    getDefaltDetaileData();
                }
            }
        });
        setTimeClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//时间点击
                if (timeAscTag == 0) {
                    head_time.setCompoundDrawablesWithIntrinsicBounds(TextDetaileActivity.this.getResources().getDrawable(R.mipmap.asc), null, null, null);
                    timeAscTag = 1;
                    timeasc = ComFlag.ASC;
                } else {
                    head_time.setCompoundDrawablesWithIntrinsicBounds(TextDetaileActivity.this.getResources().getDrawable(R.mipmap.desc), null, null, null);
                    timeAscTag = 0;
                    timeasc = ComFlag.DESC;
                }
                TextDetaileActivity.this.getCommonDate();
            }
        });
        setDownloadClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//全部下载
                TextDetaileActivity.this.showToast("全部下载");
            }
        });
    }

    private void getCommonDate() {
        if (clickId == null) {
            getDefaltDetaileData();
        } else {
            getLeftItemclickData(clickId);
        }
    }

    private PopupWindowA mWindowA;
    private PopupWindowB mWindowB;
    private PopupWindowC mWindowC;

    //显示popwindow
    private void showPopWindow(String tag) {
        if (mWindowC != null && mWindowC.isShowing()) {
            mWindowC.dismiss();
        }
        if (mWindowB != null && mWindowB.isShowing()) {
            mWindowB.dismiss();
        }
        if (mWindowA != null && mWindowA.isShowing()) {
            mWindowA.dismiss();
            if (ComFlag.PopFlag.TITLE.equals(tag)) {
                ivDetaileSelect.setImageResource(R.mipmap.selecte);
                return;
            }
        }
        ivDetaileSelect.setImageResource(R.mipmap.selectleft);
        this.mWindowA = new PopupWindowA<BeanAllYear>(this, years, new PopupWindowA.OnClickListener() {
            @Override
            public void onClick(int position1) {
                if (mWindowB != null && mWindowB.isShowing())
                    mWindowB.dismiss();
                if (mWindowC != null && mWindowC.isShowing())
                    mWindowC.dismiss();
                TextDetaileActivity.this.getSubject(years.get(position1).academicYearId);
            }
        });
        //根据指定View定位
        PopupWindowCompat.showAsDropDown(this.mWindowA, llDetaileSelect, 0, DensityUtil.dip2px(-37), Gravity.RIGHT);
    }

    //获取所有课本目录的数据
    private void getSubject(String academicYearId) {
        ApiUtil.doDefaultApi(api.selectGradeSubejectList(academicYearId), new HttpSucess<List<BeanHomeGrid>>() {
            @Override
            public void onSucess(List<BeanHomeGrid> data) {
                if (data.size() > 0) {
                    mWindowB = new PopupWindowB<BeanHomeGrid>(TextDetaileActivity.this, data, new PopupWindowB.OnClickListener() {
                        @Override
                        public void onClick(int position2) {

                            if (mWindowC != null && mWindowC.isShowing())
                                mWindowC.dismiss();
                            if (data.size() > 0) {
                                schoolSubjectId = data.get(position2).schoolSubjectId;
                                TextDetaileActivity.this.getTextBook(academicYearId, schoolSubjectId, data.get(position2).schoolSubjectName);
                            } else {
                                TextDetaileActivity.this.showToast("没有下一级了");
                            }

                        }
                    });
                    //根据指定View定位
                    PopupWindowCompat.showAsDropDown(mWindowB, llDetaileSelect, DensityUtil.dip2px(153), DensityUtil.dip2px(-37), Gravity.RIGHT);
                } else {
                    TextDetaileActivity.this.showToast("没有下一级了");
                }
            }
        }, ViewControlUtil.create2Dialog(this));

    }

    //获取上下册数据
    private void getTextBook(String academicYearId, String schoolSubjectId, String schoolSubjectName) {
        ApiUtil.doDefaultApi(api.selectTextBook(academicYearId, schoolSubjectId), new HttpSucess<List<BeanTextBookGrid>>() {
            @Override
            public void onSucess(List<BeanTextBookGrid> data) {
                if (data.size() > 0) {
                    mWindowC = new PopupWindowC<BeanTextBookGrid>(TextDetaileActivity.this, data, new PopupWindowC.OnClickListener() {
                        @Override
                        public void onClick(int position3) {
                            TextDetaileActivity.this.getcatelogList(data.get(position3).schoolTextBookId);
                            tvTextclass.setText(schoolSubjectName);
                            mWindowC.dismiss();
                            mWindowB.dismiss();
                            mWindowA.dismiss();
                            //选择上下册时也刷新详情列表
                            textBookId = data.get(position3).schoolTextBookId;
                            TextDetaileActivity.this.getDefaltDetaileData();
                        }
                    });
                    //根据指定View定位
                    PopupWindowCompat.showAsDropDown(mWindowC, llDetaileSelect, DensityUtil.dip2px(260), DensityUtil.dip2px(-37), Gravity.RIGHT);
                } else {
                    TextDetaileActivity.this.showToast("没有下一级了");
                }
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    //获取目录点击后的详情数据
    private void getLeftItemclickData(String clickId) {
//        ApiUtil.doDefaultApi(api.selectExercisesCollectionByChapterId(clickId, timeasc), new HttpSucess<List<com.zwyl.wronglist.main.detaile.BeanDetaile>>() {
//            @Override
//            public void onSucess(List<com.zwyl.wronglist.main.detaile.BeanDetaile> data) {
//                mAdapterDetail.setDatas(data);
//            }
//        }, ViewControlUtil.create2Dialog(this));
        ApiUtil.doDefaultApi(api.selectExercisesCollectionByChapterId(clickId, timeasc), new HttpSucess<List<BeanSubject>>() {
            @Override
            public void onSucess(List<BeanSubject> data) {
                mAdapterDetail.setDatas(data);
            }
        }, ViewControlUtil.create2Dialog(this));
    }

}
