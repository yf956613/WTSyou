package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.gaode.activity.GaoDeAddressSelectActivity;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.model.EntityQiniuToken;
import com.qingye.wtsyou.model.QiniuMessage;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDatePicker;
import zuo.biao.library.widget.CustomDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.CutPictureActivity;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.ui.SelectPictureActivity;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class DataActivity extends BaseActivity implements View.OnClickListener, OnBottomDragListener
        , AlertDialog.OnDialogButtonClickListener, ItemDialog.OnDialogItemClickListener, View.OnTouchListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private TextView tvRight;

    private RelativeLayout rlHead;
    private RelativeLayout rlNickName;
    private RelativeLayout rlSignature;
    private RelativeLayout rlSex;
    private RelativeLayout rlBirthday;
    private RelativeLayout rlBackground;
    private RelativeLayout rlArea;

    private ImageView ivHead;
    private ImageView ivBackground;
    private TextView tvNickName;
    private TextView tvSignature;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvArea;

    private int ivWhich = 0;
    private int uploadWhich = 0;

    private CustomDatePicker customDatePicker;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private String now = sdf.format(new Date());

    private EntityPersonalMessage entityPersonalMessage;

    private CustomDialog progressBar;

    private String oldNickname = null;
    private String oldSignature = null;

    private String pictureBackgroundPath;
    private String pictureHeadPath;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context,EntityPersonalMessage entityPersonalMessage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PERSONALMESSAGE, entityPersonalMessage);//放进数据流中
        return new Intent(context, DataActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data, this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        entityPersonalMessage = (EntityPersonalMessage) intent.getSerializableExtra(Constant.PERSONALMESSAGE);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("我的资料");
        tvRight = findViewById(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");

        //头像
        rlHead = findViewById(R.id.rlHead,this);
        ivHead = findViewById(R.id.iv_head);

        //背景
        rlBackground = findViewById(R.id.rlBackground,this);
        ivBackground = findViewById(R.id.iv_background);

        //昵称
        rlNickName = findViewById(R.id.rlNickname,this);
        tvNickName = findViewById(R.id.tv_nickname);

        //个性签名
        rlSignature = findViewById(R.id.rlSignature,this);
        tvSignature = findViewById(R.id.tv_signature);

        //性别
        rlSex = findViewById(R.id.rlSex,this);
        tvSex = findViewById(R.id.tv_sex);

        //生日
        rlBirthday = findViewById(R.id.rlBirthday,this);
        tvBirthday = findViewById(R.id.tv_birthday);

        //地区
        rlArea = findViewById(R.id.rlArea);
        tvArea = findViewById(R.id.tv_area);

        initDatePicker();
    }

    public void onResume() {

        super.onResume();
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

    //设置时间选择器相关属性
    private void initDatePicker() {
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvBirthday.setText(time.split(" ")[0]);
            }
        }, "1930-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动
    }

    private static final String[] SEX_ITEM = {"男", "女"};

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

        if (ivWhich < 1) {
            Glide.with(context).load(path).into(ivHead);
            pictureHeadPath = path;

            if (pictureHeadPath != null) {
                //检查网络
                if (NetUtil.checkNetwork(context)) {
                    getQiNiuToken(path);
                } else {
                    showShortToast(R.string.checkNetwork);
                    //progressBarDismiss();

                    return;
                }
            } else {
                showShortToast(R.string.noHead);
                return;
            }

        } else {
            Glide.with(context).load(path).into(ivBackground);
            pictureBackgroundPath = path;

            if (pictureHeadPath != null) {
                //检查网络
                if (NetUtil.checkNetwork(context)) {
                    getQiNiuToken(path);
                } else {
                    showShortToast(R.string.checkNetwork);
                    //progressBarDismiss();

                    return;
                }
            } else {
                showShortToast(R.string.noBackground);
                return;
            }

        }
    }

    @Override
    public void initData() {
        //头像
        if (entityPersonalMessage.getContent().getPhoto() != null) {
            pictureHeadPath = entityPersonalMessage.getContent().getPhoto();
            Glide.with(context)
                    .load(pictureHeadPath)
                    .into(ivHead);
        }
        //背景
        if (entityPersonalMessage.getContent().getPhoto() != null) {
            pictureBackgroundPath = entityPersonalMessage.getContent().getBackgruoud();
            Glide.with(context)
                    .load(pictureBackgroundPath)
                    .into(ivBackground);
        }
        //昵称
        tvNickName.setText(entityPersonalMessage.getContent().getNickname());
        oldNickname = entityPersonalMessage.getContent().getNickname();//第一次修改昵称的时候
        //签名
        if (entityPersonalMessage.getContent().getAutograph() != null) {
            tvSignature.setText(entityPersonalMessage.getContent().getAutograph());
            oldSignature = entityPersonalMessage.getContent().getAutograph();//第一次修改签名的时候
        }
        //性别
        if (entityPersonalMessage.getContent().getSex() != null) {
            tvSex.setText(entityPersonalMessage.getContent().getSex());
        }
        //生日
        if (entityPersonalMessage.getContent().getBirthday() != null) {
            tvBirthday.setText(entityPersonalMessage.getContent().getBirthday());
        }
        //地区
        if (entityPersonalMessage.getContent().getAreaName() != null) {
            tvArea.setText(entityPersonalMessage.getContent().getAreaName());
        }

        progressBarDismiss();
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        rlHead.setOnClickListener(this);
        rlBackground.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlSignature.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlBirthday.setOnClickListener(this);
        rlArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                modifyPersonalMessage();
                break;
            case R.id.rlHead:
                ivWhich = 0;
                selectPicture();
                break;
            case R.id.rlBackground:
                ivWhich = 1;
                selectPicture();
                break;
            case R.id.rlNickname:
                toActivity(EditNicknameActivity.createIntent(context, oldNickname),REQUEST_TO_NICKNAME);
                break;
            case R.id.rlSignature:
                toActivity(EditSignatureActivity.createIntent(context, oldSignature),REQUEST_TO_SIGNATURE);
                break;
            case R.id.rlSex:
                toActivity(BottomMenuWindow.createIntent(context, SEX_ITEM)
                        .putExtra(BottomMenuWindow.INTENT_TITLE, "选择性别"), REQUEST_TO_BOTTOM_MENU, false);
                break;
            case R.id.rlBirthday:
                // 日期格式为yyyy-MM-dd
                customDatePicker.show(now);
                break;
            case R.id.rlArea:
                toActivity(GaoDeAddressSelectActivity.createIntent(context),REQUEST_TO_SELECT_AREA);
                break;
            default:
                break;
        }
    }

    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static final int REQUEST_TO_SELECT_AREA = 1;
    private static final int REQUEST_TO_NICKNAME = 2;
    private static final int REQUEST_TO_SIGNATURE = 3;

    private static final int REQUEST_TO_SELECT_PICTURE = 20;
    private static final int REQUEST_TO_CUT_PICTURE = 21;
    private static final int REQUEST_TO_BOTTOM_MENU = 31;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            case REQUEST_TO_BOTTOM_MENU:
                if (data != null) {
                    int selectedPosition = data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1);
                    if (selectedPosition >= 0 && selectedPosition < SEX_ITEM.length) {
                        tvSex.setText(SEX_ITEM[selectedPosition]);
                    }
                }
                break;
            case REQUEST_TO_SELECT_AREA:
                if (data != null) {
                    POI selectedCity = (POI) data.getExtras().getSerializable(Constant.SELECTED_ADDRESS);
                    tvArea.setText(selectedCity.getPcdt().getProvince().toString() + "-" +
                            selectedCity.getPcdt().getCity().toString() + "-" + selectedCity.getPcdt().getDistrict());
                }
                break;
            case REQUEST_TO_NICKNAME:
                if (data != null) {
                    String newNickname = (String) data.getExtras().getSerializable(Constant.NICKNAME);
                    tvNickName.setText(newNickname);
                    oldNickname = newNickname;//第一次之后修改昵称
                }
                break;
            case REQUEST_TO_SIGNATURE:
                if (data != null) {
                    String newSignature = (String) data.getExtras().getSerializable(Constant.SIGNATURE);
                    tvSignature.setText(newSignature);
                    oldSignature = newSignature;//第一次之后修改签名
                }
                break;
            default:
                break;
        }
    }

    private String headQiniu = "";
    private String backgroundQiniu = "";

    public void getQiNiuToken(final String path) {

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
                        if (path != null) {
                            UploadManager uploadManager = new UploadManager();
                            uploadManager.put(path, key,
                                    token, new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject res) {
                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                                            if(info.isOK()) {
                                                Log.e("qiniu", "Upload Success");
                                                QiniuMessage qiniuMessage =  JSON.parseObject(res.toString(),QiniuMessage.class);
                                                String hash = qiniuMessage.getHash();
                                                String backKey = qiniuMessage.getKey();

                                                if (ivWhich < 1) {
                                                    headQiniu = url + "/"+ backKey;
                                                } else {
                                                    backgroundQiniu = url + "/" + backKey;
                                                }

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

    public void modifyPersonalMessage() {

        String nickname = tvNickName.getText().toString().trim();
        //昵称
        if (TextUtils.isEmpty(nickname)) {
            showShortToast(R.string.noNickName);
            return;
        }

        String signature = tvSignature.getText().toString().trim();
        //个性签名
        if (TextUtils.isEmpty(signature)) {
            showShortToast(R.string.noSignature);
            return;
        }

        String sex = tvSex.getText().toString().trim();
        //性别
        if (TextUtils.isEmpty(sex)) {
            showShortToast(R.string.noSex);
            return;
        }

        String birthday = tvBirthday.getText().toString().trim();
        //生日
        if (TextUtils.isEmpty(birthday)) {
            showShortToast(R.string.noBirthday);
            return;
        }

        String area = tvArea.getText().toString().trim();
        //地区
        if (TextUtils.isEmpty(area)) {
            showShortToast(R.string.noArea);
            return;
        }

        if (headQiniu.isEmpty()) {
            headQiniu = pictureHeadPath;
        }

        if (backgroundQiniu.isEmpty()) {
            backgroundQiniu = pictureBackgroundPath;
        }

        //检查网络
        if (NetUtil.checkNetwork(context)) {

            setProgressBar();
            progressBar.show();

            HttpRequest.postModifyPersonalMessage(0, nickname, nickname,
                    entityPersonalMessage.getContent().getMobile(), sex, headQiniu, signature,
                    birthday, backgroundQiniu, area, new OnHttpResponseListener() {

                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                            if(!StringUtil.isEmpty(resultJson)){
                                EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                                if(entityBase.isSuccess()){
                                    //成功
                                    showShortToast(R.string.modifyMessage);

                                    Intent intent = new Intent(
                                            BroadcastAction.ACTION_MESSAGE_REFRESH);

                                    // 发送广播
                                    sendBroadcast(intent);

                                    progressBarDismiss();

                                    finish();
                                }else {//显示失败信息
                                    if (entityBase.getCode().equals("401")) {
                                        showShortToast(R.string.tokenInvalid);
                                        toActivity(MainActivity.createIntent(context));
                                    } else {
                                        showShortToast(entityBase.getMessage());
                                    }

                                    progressBarDismiss();
                                }

                            }else{
                                showShortToast(R.string.noReturn);

                                progressBarDismiss();

                            }
                        }
                    });
        } else {
            showShortToast(R.string.checkNetwork);

            progressBarDismiss();
        }

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {

    }

}
