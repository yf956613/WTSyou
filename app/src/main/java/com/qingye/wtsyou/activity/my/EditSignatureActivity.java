package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.utils.Constant;

import fj.edittextcount.lib.FJEditTextCount;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class EditSignatureActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvHead,tvLeft,tvRight;
    private FJEditTextCount edtSignature;
    private LinearLayout llNickname;
    private LinearLayout llSignature;

    private String signature = null;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String signature) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.SIGNATURE, signature);//放进数据流中
        return new Intent(context, EditSignatureActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname,this);


        intent = getIntent();
        signature = intent.getStringExtra(Constant.SIGNATURE);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("设置个性签名");
        tvLeft = findView(R.id.tv_left);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setTextColor(getResources().getColor(R.color.black_text));
        tvLeft.setText("取消");
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.black_text));
        tvRight.setText("保存");

        llNickname = findView(R.id.llNickname);
        llNickname.setVisibility(View.GONE);
        llSignature = findView(R.id.llSignature);
        llSignature.setVisibility(View.VISIBLE);

        edtSignature = findView(R.id.edt_signature);
    }

    @Override
    public void initData() {
        edtSignature.setText(signature);
    }

    @Override
    public void initEvent() {
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.SIGNATURE,edtSignature.getText().toString());//放进数据流中

                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
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
}
