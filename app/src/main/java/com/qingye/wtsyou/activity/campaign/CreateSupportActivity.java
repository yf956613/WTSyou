package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.search.SelectThreeStarsActivity;
import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.fragment.campaign.AssociateConversationFragment;
import com.qingye.wtsyou.fragment.campaign.AssociateStarsFragment;
import com.qingye.wtsyou.modle.EntityQiniuToken;
import com.qingye.wtsyou.modle.EntityStars;
import com.qingye.wtsyou.modle.EntityContent;
import com.qingye.wtsyou.modle.QiniuMessage;
import com.qingye.wtsyou.modle.SupportType;
import com.qingye.wtsyou.modle.SupportTypeItem;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.AutoLineFeedLayout;
import com.qingye.wtsyou.widget.CustomDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
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

public class CreateSupportActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvHead,tvRight;
    private FJEditTextCount edtName,edtContent;
    private EditText edtTarget;
    private TextView tvCount;
    /*private RadioGroup rgType;
    private RadioButton rbtScreen,rbtCarousel,rbtArticle,rbtRaise;*/
    private RelativeLayout llAssociationStars;
    private RelativeLayout llAssociateConversation;
    private LinearLayout llTargetValue;
    private Button btnCreate;

    private RelativeLayout rlPicture;
    private ImageView ivSelectPicture;

    //应援类型
    List<SupportTypeItem> supportTypeItem = new ArrayList<>();
    private AutoLineFeedLayout layout;
    private int WHICH = 0;
    private String name;//选择类型
    private BigDecimal bigTarget = BigDecimal.ZERO;//目标金额

    //关联明星
    private List<EntityStars> selectStars = new ArrayList<>();

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, CreateSupportActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_support,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("发起应援");
        tvRight = findViewById(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.black_text));
        tvRight.setText("取消");

        edtName = findViewById(R.id.edt_support_name);
        edtContent = findViewById(R.id.edt_support_content);
        edtTarget = findViewById(R.id.edt_target_value);

        layout = findViewById(R.id.al);

        //关联聊天室
        AssociateConversationFragment associateConversationFragment = new AssociateConversationFragment();
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_associate_conversation,associateConversationFragment);
        transaction.commit();

        llAssociateConversation = findViewById(R.id.ll_associate_conversation);
        llAssociationStars = findViewById(R.id.ll_associate_star);
        llTargetValue = findViewById(R.id.ll_target_value);

        //添加图片
        rlPicture = findViewById(R.id.rl_picture);
        ivSelectPicture = findViewById(R.id.iv_select_img);

        btnCreate = findViewById(R.id.btn_create);
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
        //检查网络
        if (NetUtil.checkNetwork(context)) {
            getSupportType();
        } else {
            showShortToast(R.string.checkNetwork);
        }
    }

    @Override
    public void initEvent() {
        tvRight.setOnClickListener(this);
        llAssociationStars.setOnClickListener(this);
        llAssociateConversation.setOnClickListener(this);
        rlPicture.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
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
                break;case R.id.ll_associate_star:
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

    public void getSupportType() {
        String type = Constant.SUPPORT;
        HttpRequest.getSupportType(0, type, new OnHttpResponseListener() {
            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                if(!StringUtil.isEmpty(resultJson)){
                    EntityContent entityContent =  JSON.parseObject(resultJson,EntityContent.class);
                    if(entityContent.isSuccess()){
                        //成功//showShortToast(R.string.getSuccess);
                        List<SupportType> supportTypes = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityContent.getContent())
                                ,new TypeToken<List<SupportType>>(){}.getType());
                        for (SupportType entitySupportType : supportTypes) {
                            SupportTypeItem entitySupportTypeItem = new SupportTypeItem();
                            entitySupportTypeItem.setSupportType(entitySupportType);
                            entitySupportTypeItem.setSelector(false);
                            supportTypeItem.add(entitySupportTypeItem);
                        }
                        supportTypeItem.get(0).setSelector(true);
                        selectSupportType();
                    }else{//显示失败信息
                        if (entityContent.getCode().equals("401")) {
                            showShortToast(R.string.tokenInvalid);
                            toActivity(MainActivity.createIntent(context));
                        } else {
                            showShortToast(entityContent.getMessage());
                        }

                        progressBarDismiss();
                    }
                }else{
                    showShortToast(R.string.noReturn);
                }
            }
        });
    }

    public void selectSupportType() {
        View view;
        final List<Button> buttonList = new ArrayList<>();
        for (int i = 0; i < supportTypeItem.size(); i ++) {
            view = LayoutInflater.from(CreateSupportActivity.this).inflate(R.layout.list_radiobutton, null);
            final Button button = view.findViewById(R.id.btn);
            button.setTag(i);
            button.setText(supportTypeItem.get(i).getSupportType().getName());
            boolean isSelect = supportTypeItem.get(i).getSelector();
            if (isSelect) {
                button.setSelected(true);
            }
            buttonList.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0;j < supportTypeItem.size();j ++) {
                        if (j == (int) button.getTag()) {
                            buttonList.get(j).setSelected(true);
                            supportTypeItem.get(j).setSelector(true);
                            //目标金额
                            if (supportTypeItem.get(j).getSupportType().getName().equals("集资")) {
                                llTargetValue.setVisibility(View.VISIBLE);
                                WHICH = 1;
                            } else {
                                llTargetValue.setVisibility(View.GONE);
                                WHICH = 0;
                            }
                        } else {
                            buttonList.get(j).setSelected(false);
                            supportTypeItem.get(j).setSelector(false);
                        }
                    }
                }
            });

            layout.addView(view, i);
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
        if (WHICH == 1) {
            String target = edtTarget.getText().toString().trim();
            if (TextUtils.isEmpty(target)) {
                showShortToast(R.string.noTarget);
                return;
            }
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

        for (int i = 0;i < supportTypeItem.size();i ++) {
            if (supportTypeItem.get(i).getSelector()) {
                name = supportTypeItem.get(i).getName();
            }
        }
        String detail = edtContent.getText().toString().trim();
        String relevanceStar = selectStars.get(0).getUuid();
        if (WHICH == 1) {
            String allTarget = edtTarget.getText().toString().trim();
            bigTarget = new BigDecimal(allTarget);
        } else {
            bigTarget = BigDecimal.ZERO;
        }

        //检查网络
        if (NetUtil.checkNetwork(this)) {

            setProgressBar();
            progressBar.show();

            HttpRequest.postCreateSupport(0, activityName, activityIcon,
                    name, detail, relevanceStar, bigTarget, WHICH,new OnHttpResponseListener() {

                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                            if(!StringUtil.isEmpty(resultJson)){
                                EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                                if(entityBase.isSuccess()){
                                    //成功
                                    showShortToast(R.string.createSuppoertSuccess);
                                    finish();

                                    progressBarDismiss();
                                }else{//显示失败信息
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
