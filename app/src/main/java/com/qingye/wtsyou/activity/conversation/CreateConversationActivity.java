package com.qingye.wtsyou.activity.conversation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.SelectStarsConversationActivity;
import com.qingye.wtsyou.activity.search.SelectThreeStarsActivity;
import com.qingye.wtsyou.fragment.campaign.AssociateConversationFragment;
import com.qingye.wtsyou.fragment.campaign.AssociateStarsFragment;
import com.qingye.wtsyou.model.EntityQiniuToken;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.model.QiniuMessage;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import zuo.biao.library.widget.CustomDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fj.edittextcount.lib.FJEditTextCount;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.CutPictureActivity;
import zuo.biao.library.ui.SelectPictureActivity;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class CreateConversationActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvLeft,tvRight,tvHead;
    private FJEditTextCount edtName,edtContent;
    private RelativeLayout llAssociationStars;

    private RelativeLayout rlPicture;
    private ImageView ivSelectPicture;

    //关联明星
    private List<EntityStars> selectStars = new ArrayList<>();

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, CreateConversationActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        tvLeft = findViewById(R.id.tv_left);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText("取消");
        tvRight = findViewById(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("发起");
        tvRight.setTextColor(getResources().getColor(R.color.orange_text2));
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("创建聊天室");

        edtName = findViewById(R.id.edt_support_name);
        edtContent = findViewById(R.id.edt_support_content);

        //关联聊天室
        AssociateConversationFragment associateConversationFragment = new AssociateConversationFragment();
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_associate_conversation,associateConversationFragment);
        transaction.commit();

        llAssociationStars = findViewById(R.id.ll_associate_star);

        //添加图片
        rlPicture = findViewById(R.id.rl_picture);
        ivSelectPicture = findViewById(R.id.iv_select_img);
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
        rlPicture.setOnClickListener(this);
    }

    private String picturePath;

    /**选择图片
     */
    private void selectPicture() {
        toActivity(SelectPictureActivity.createIntent(context), REQUEST_TO_SELECT_PICTURE, false);
    }

    /**裁剪图片
     * @param path
     */
    private void cutPicture(String path) {
        if (StringUtil.isFilePath(path) == false) {
            showShortToast("找不到图片");
            return;
        }
        this.picturePath = path;

        toActivity(CutPictureActivity.createIntent(context, path
                , DataKeeper.imagePath, "photo" + System.currentTimeMillis(), 200)
                , REQUEST_TO_CUT_PICTURE);
    }

    /**显示图片
     * @param path
     */
    private void setPicture(String path) {
        if (StringUtil.isFilePath(path) == false) {
            showShortToast("找不到图片");
            return;
        }
        this.picturePath = path;

        Glide.with(context).load(path).into(ivSelectPicture);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_temp:
                finish();
                break;
            case R.id.ll_associate_star:
                toActivity(SelectThreeStarsActivity.createIntent(context,selectStars),REQUEST_TO_SELECT_STARS);
                break;
            case R.id.ll_associate_conversation:
                toActivity(SelectStarsConversationActivity.createIntent(context));
                break;
            case R.id.rl_picture:
                selectPicture();
                break;
            case R.id.btn_create:
                check();
                break;
            default:
                break;
        }

    }

    private static final int REQUEST_TO_SELECT_STARS = 1;
    private static final int REQUEST_TO_SELECT_PICTURE = 20;
    private static final int REQUEST_TO_CUT_PICTURE = 21;

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
            case REQUEST_TO_SELECT_PICTURE:
                if (data != null) {
                    cutPicture(data.getStringExtra(SelectPictureActivity.RESULT_PICTURE_PATH));
                }
                break;
            case REQUEST_TO_CUT_PICTURE:
                if (data != null) {
                    setPicture(data.getStringExtra(CutPictureActivity.RESULT_PICTURE_PATH));
                }
                break;
            default:
                break;
        }
    }

    public void check() {
        String name = edtName.getText().toString().trim();
        String content = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            showShortToast(R.string.noName);
            return;
        }
        if (TextUtils.isEmpty(content)){
            showShortToast(R.string.noContent);
            return;
        }
        if(selectStars.size() == 0 ) {
            showShortToast(R.string.noStar);
            return;
        }
        if (picturePath != null) {
            //检查网络
            if (NetUtil.checkNetwork(this)) {
                getQiNiuToken();
            } else {
                showShortToast(R.string.checkNetwork);
                progressBarDismiss();
            }
        } else {
            showShortToast(R.string.noPicture);
            return;
        }

    }

    public void getQiNiuToken() {

        //setProgressBar();
        //progressBar.show();

        HttpRequest.getQiNiuToken(0, new OnHttpResponseListener() {

            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                if(!StringUtil.isEmpty(resultJson)){
                    EntityQiniuToken entityQiniuToken =  JSON.parseObject(resultJson,EntityQiniuToken.class);
                    if(entityQiniuToken.isSuccess()){
                        //成功
                        //token
                        String token = entityQiniuToken.getContent().getUptoken();
                        //url
                        final String url = entityQiniuToken.getContent().getUrl();
                        long currentTime = System.currentTimeMillis();
                        String key = String.valueOf(currentTime);
                        if (picturePath != null) {
                            UploadManager uploadManager = new UploadManager();
                            uploadManager.put(picturePath, key,
                                    token, new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject res) {
                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                                            if(info.isOK()) {
                                                Log.e("qiniu", "Upload Success");
                                                QiniuMessage qiniuMessage =  JSON.parseObject(res.toString(),QiniuMessage.class);
                                                String hash = qiniuMessage.getHash();
                                                String backKey = qiniuMessage.getKey();
                                                createSupport( url, hash, backKey);

                                                //progressBarDismiss();
                                            } else {
                                                Log.e("qiniu", "Upload Fail");
                                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                                showShortToast(R.string.uploadFailed);

                                                //progressBarDismiss();
                                            }
                                            Log.e("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                        }
                                    }, null);
                        } else {
                            if (entityQiniuToken.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityQiniuToken.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        //显示失败信息
                        showShortToast(entityQiniuToken.getMessage());

                        //progressBarDismiss();
                    }
                }
            }
        });
    }

    public void createSupport(String url, String hash, String key) {
        String activityName = edtName.getText().toString().trim();
        String activityIcon = url + "/" + key;


        //检查网络
        if (NetUtil.checkNetwork(this)) {

            setProgressBar();
            progressBar.show();

        } else {
            showShortToast(R.string.checkNetwork);

            progressBarDismiss();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        finish();
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
}
