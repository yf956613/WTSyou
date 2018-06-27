package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.EntityIdentify;
import com.qingye.wtsyou.utils.AliyunUtils;
import com.qingye.wtsyou.utils.Base64Util;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.TakePictureManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.CutPictureActivity;
import zuo.biao.library.ui.SelectPictureActivity;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.StringUtil;

;

public class IdentifyActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener ,TakePictureManager.takePictureCallBackListener{

    private TextView tvRight,tvHead;
    private ImageView ivImg;
    private Button btnIdentity;
    private TakePictureManager mTakePictureManager;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, IdentifyActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("取消");
        tvRight.setTextColor(getResources().getColor(R.color.gray_text10));
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("实名认证");

        ivImg = findView(R.id.iv_img);
        btnIdentity = findView(R.id.btn_identify);


        /*mTakePictureManager = new TakePictureManager(this);
        mTakePictureManager.setTailor(0,0,0,0);
        mTakePictureManager.setTakePictureCallBackListener(this);*/
    }
    @Override
    public void successful(boolean isTailor, File outFile, Uri filePath) {

    }

    @Override
    public void failed(int errorCode, List<String> deniedPermissions) {

    }
    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvRight.setOnClickListener(this);
        btnIdentity.setOnClickListener(this);
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
                , DataKeeper.imagePath, "photo" + System.currentTimeMillis(), 600, 450, 0, 0)
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

        Glide.with(context).load(path).into(ivImg);

        //第二步:将图片转换为Base64字符串
        String base64 = Base64Util.imageToBase64(path);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("DemoActivity", "onFailure: 请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("DemoActivity", "onResponse: 请求成功：" + response.body().string());

                EntityIdentify identify = GsonUtil.getGson().fromJson(response.body().string(), EntityIdentify.class);
            }
        };
        //第三步：构建请求，
        AliyunUtils.aliyun(callback,"face", base64);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_temp:
                finish();
                break;
            case R.id.btn_identify:
                /*mTakePictureManager.startTakeWayByAlbum();*/
                selectPicture();
                break;
            default:
                break;
        }
    }

    private static final int REQUEST_TO_SELECT_PICTURE = 20;
    private static final int REQUEST_TO_CUT_PICTURE = 21;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*mTakePictureManager.attachToActivityForResult(requestCode,resultCode,data);*/

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTakePictureManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }*/

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
}
