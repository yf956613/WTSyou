package com.qingye.wtsyou.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;

import static zuo.biao.library.util.CommonUtil.showShortToast;

/**
 * Created by pm89 on 2018/4/22.
 */

public class NumberCount extends LinearLayout implements View.OnClickListener{

    private int number = 1;

    private TextView tvReduce;
    private TextView tvAdd;
    private EditText edtNumber;

    public NumberCount(Context context) {
        this(context, null);
    }

    public NumberCount(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_number_count, this, true);

        tvReduce = (TextView) findViewById(R.id.tv_reduce);
        tvAdd = (TextView) findViewById(R.id.tv_add);

        edtNumber = (EditText) findViewById(R.id.edt_number_count);
        if (edtNumber.getTag() instanceof TextWatcher) {
            edtNumber.removeTextChangedListener((TextWatcher) edtNumber.getTag());
        }
        edtNumber.setText(String.valueOf(number));//变化监听
        TextWatcher watcher = new TextWatcher() {
            private CharSequence temp ;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (s.toString().equals("0")) {
                        showShortToast(getContext(),R.string.noLess);
                        number = 1;
                        edtNumber.setText(Integer.toString(number));
                    } else {
                        number = Integer.parseInt(s.toString());
                    }
                } else {
                    showShortToast(getContext(),"数量不能为空");
                }
            }
        };
        edtNumber.addTextChangedListener(watcher);
        edtNumber.setTag(watcher);
        //全选
        edtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNumber.selectAll();
            }
        });

        initEvent();
    }

    public void initEvent() {
        tvReduce.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reduce:
                number = number - 1;
                if (number == 0 ) {
                    showShortToast(getContext(),"数量不能为0");
                    number = 1;
                }
                edtNumber.setText(Integer.toString(number));
                break;
            case R.id.tv_add:
                number = number + 1;
                edtNumber.setText(Integer.toString(number));
                break;
            default:
                break;
        }
    }
}
