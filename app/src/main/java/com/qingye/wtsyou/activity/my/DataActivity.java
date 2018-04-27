package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.search.SearchAreaActivity;
import com.qingye.wtsyou.modle.City;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.CutPictureActivity;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.ui.SelectPictureActivity;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.StringUtil;

public class DataActivity extends BaseActivity implements View.OnClickListener, OnBottomDragListener
        , AlertDialog.OnDialogButtonClickListener, ItemDialog.OnDialogItemClickListener, View.OnTouchListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private TextView tvRight;

    private RelativeLayout rlHead;
    private RelativeLayout rlBirthday;
    private RelativeLayout rlSex;
    private RelativeLayout rlBackground;
    private RelativeLayout rlArea;

    private ImageView ivHead;
    private ImageView ivBackground;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvArea;

    private int ivWhich = 0;

    private CustomDatePicker customDatePicker;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private String now = sdf.format(new Date());

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, DataActivity.class);
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
        } else {
            Glide.with(context).load(path).into(ivBackground);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        rlHead.setOnClickListener(this);
        rlBackground.setOnClickListener(this);
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
                finish();
                break;
            case R.id.rlHead:
                ivWhich = 0;
                selectPicture();
                break;
            case R.id.rlBackground:
                ivWhich = 1;
                selectPicture();
                break;
            case R.id.rlSex:
                toActivity(BottomMenuWindow.createIntent(context, SEX_ITEM)
                        .putExtra(BottomMenuWindow.INTENT_TITLE, "选择性别"), REQUEST_TO_BOTTOM_MENU, false);
                break;
            case R.id.rlBirthday:
                // 日期格式为yyyy-MM-dd
                customDatePicker.show(now);
                tvBirthday.setVisibility(View.VISIBLE);
                break;
            case R.id.rlArea:
                toActivity(SearchAreaActivity.createIntent(context),REQUEST_TO_SELECT_AREA);
                break;
            default:
                break;
        }
    }

    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static final int REQUEST_TO_SELECT_AREA = 1;

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
                    City selectedCity = (City) data.getExtras().getSerializable(Constant.SELECTED_CITY);
                    tvArea.setText(selectedCity.name);
                }
                break;
            default:
                break;
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
