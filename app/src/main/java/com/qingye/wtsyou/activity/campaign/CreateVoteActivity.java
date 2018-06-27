package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.gaode.activity.GaoDeAddressSelectActivity;
import com.qingye.wtsyou.activity.search.SelectOneStarsActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.fragment.campaign.AssociateConversationFragment;
import com.qingye.wtsyou.fragment.campaign.AssociateStarsFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityRule;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fj.edittextcount.lib.FJEditTextCount;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class CreateVoteActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvHead,tvRight;
    private FJEditTextCount edtName,edtContent;
    private RelativeLayout llAssociateAddress;
    private TextView associateAddress;
    private RelativeLayout llAssociationStars;
    private RelativeLayout llAssociateConversation;
    private Button btnCreate;
    private WebView webView;

    //关联明星
    private List<EntityStars> selectStars = new ArrayList<>();
    //关联聊天室
    private List<ChatingRoom> selectConversation = new ArrayList<>();

    //活动地点
    private POI selectedCity = new POI();

    private HttpModel<EntityBase> mEntityBaseHttpModel;
    private HttpModel<EntityRule> mEntityRuleHttpModel;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, CreateVoteActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vote,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        mEntityBaseHttpModel = new HttpModel<>(EntityBase.class);
        //规则
        mEntityRuleHttpModel = new HttpModel<>(EntityRule.class);
        ruleQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("发起投票");
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.black_text));
        tvRight.setText("取消");

        edtName = findView(R.id.edt_vote_name);
        edtContent = findView(R.id.edt_vote_content);

        llAssociationStars = findView(R.id.ll_associate_star);
        llAssociateAddress = findView(R.id.ll_associate_address);
        associateAddress = findView(R.id.tv_associate_address);
        llAssociateConversation = findView(R.id.ll_associate_conversation);

        btnCreate = findView(R.id.btn_create);
        webView = findView(R.id.webView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }

            progressBar = null;
        }
    }

    private void setProgressBar() {
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvRight.setOnClickListener(this);
        llAssociationStars.setOnClickListener(this);
        llAssociateAddress.setOnClickListener(this);
        llAssociateConversation.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_temp:
                finish();
                break;
            case R.id.ll_associate_star:
                toActivity(SelectOneStarsActivity.createIntent(context, selectStars), REQUEST_TO_SELECT_STARS);
                break;
            case R.id.ll_associate_address:
                toActivity(GaoDeAddressSelectActivity.createIntent(context), REQUEST_TO_SELECT_AREA);
                break;
            case R.id.ll_associate_conversation:
                if (selectStars.isEmpty()) {
                    showShortToast(R.string.associateStars);
                    return;
                }
                String[] relevanceStar = new String[selectStars.size()];
                for (int i = 0;i < selectStars.size();i ++) {
                    relevanceStar[i] = selectStars.get(i).getUuid();
                }
                toActivity(SelectStarsConversationActivity.createIntent(context, relevanceStar, selectConversation), REQUEST_TO_SELECT_CONVERSATION);
                break;
            case R.id.btn_create:
                //检查网络
                if (NetUtil.checkNetwork(context)) {
                    createVote();
                } else {
                    showShortToast(R.string.checkNetwork);
                }
            default:
                break;
        }
    }

    private static final int REQUEST_TO_SELECT_STARS = 1;
    private static final int REQUEST_TO_SELECT_AREA = 2;
    private static final int REQUEST_TO_SELECT_CONVERSATION = 3;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_STARS:
                if (data != null) {
                    selectStars = (List<EntityStars>) data.getExtras().getSerializable(Constant.SELECTED_STARS);

                    //关联明星
                    AssociateStarsFragment associateStarsFragment = new AssociateStarsFragment();
                    //注意这里是调用getChildFragmentManager()方法
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.SELECTED_STARS, (Serializable) selectStars);
                    associateStarsFragment.setArguments(bundle);
                    //把碎片添加到碎片中
                    transaction.replace(R.id.list_associate_stars,associateStarsFragment);
                    transaction.commit();
                }
                break;
            case REQUEST_TO_SELECT_AREA:
                if (data != null) {
                    selectedCity = (POI) data.getExtras().getSerializable(Constant.SELECTED_ADDRESS);
                    associateAddress.setText(selectedCity.getAddress().toString());
                }
                break;
            case REQUEST_TO_SELECT_CONVERSATION:
                if (data != null) {
                    selectConversation = (List<ChatingRoom>) data.getExtras().getSerializable(Constant.SELECTED_CONVERSATION);

                    //关联聊天室
                    AssociateConversationFragment associateConversationFragment = new AssociateConversationFragment();
                    //注意这里是调用getChildFragmentManager()方法
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.SELECTED_CONVERSATION, (Serializable) selectConversation);
                    associateConversationFragment.setArguments(bundle);
                    //把碎片添加到碎片中
                    transaction.replace(R.id.list_associate_conversation,associateConversationFragment);
                    transaction.commit();
                }
                break;
            default:
                break;
        }
    }

    public void createVote() {
        String campaignName = edtName.getText().toString().trim();
        String description = edtContent.getText().toString().trim();
        if(TextUtils.isEmpty(campaignName)){
            showShortToast(R.string.noName);
            return;
        }
        if(selectStars.size() == 0 ) {
            showShortToast(R.string.noStar);
            return;
        }
        if(TextUtils.isEmpty(description)){
            showShortToast(R.string.noContent);
            return;
        }

        String relevanceStar = selectStars.get(0).getUuid();

        String[] chatingRooms = new String[selectConversation.size()];
        if (selectConversation.size() > 0) {

            for (int i = 0;i < selectConversation.size();i ++) {
                chatingRooms[i] = selectConversation.get(i).getId();
            }
        } else {
            chatingRooms = null;
        }

        String request = HttpRequest.postCreateVote(campaignName, relevanceStar,
                selectedCity, description, chatingRooms);
        mEntityBaseHttpModel.post(request, URL_BASE + URLConstant.CREATEVOTE,1,this);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public void ruleQuery() {
        mEntityRuleHttpModel.get( URL_BASE + URLConstant.RULE + "vote",2,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                //成功
                showShortToast(R.string.createVoteSuccess);
                finish();
                break;
            case 2:
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                } else {
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                }

                settings.setDefaultFontSize(30);
                settings.setDefaultFixedFontSize(30);

                EntityRule entityRule = mEntityRuleHttpModel.getData();
                webView.setBackgroundColor(0); // 设置背景色
                webView.loadData(entityRule.getContent().getRuleDescription(), "text/html;charset=utf-8","utf-8");
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
