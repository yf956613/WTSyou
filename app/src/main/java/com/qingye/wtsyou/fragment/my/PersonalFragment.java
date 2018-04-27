package com.qingye.wtsyou.fragment.my;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.conversation.MyConversationActivity;
import com.qingye.wtsyou.activity.home.FocusStarsActivity;
import com.qingye.wtsyou.activity.my.AddressActivity;
import com.qingye.wtsyou.activity.my.CampaignActivity;
import com.qingye.wtsyou.activity.my.CardActivity;
import com.qingye.wtsyou.activity.my.CoinActivity;
import com.qingye.wtsyou.activity.my.DataActivity;
import com.qingye.wtsyou.activity.my.DiamondActivity;
import com.qingye.wtsyou.activity.my.FriendsActivity;
import com.qingye.wtsyou.activity.my.HeartActivity;
import com.qingye.wtsyou.activity.my.MessageActivity;
import com.qingye.wtsyou.activity.my.OrderActivity;
import com.qingye.wtsyou.activity.my.SettingActivity;
import com.qingye.wtsyou.activity.my.SignInActivity;
import com.qingye.wtsyou.activity.my.TicketActivity;

import org.w3c.dom.Text;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private ImageView ivLeft;
    private TextView tvRight;

    private RelativeLayout rlPersonalMessage;

    private LinearLayout llIdol;
    private LinearLayout llFriends;
    private LinearLayout llConversation;
    private LinearLayout llHeart;
    private LinearLayout llDiamonds;
    private LinearLayout llCoin;
    private RelativeLayout llCampaign;
    private RelativeLayout llOrder;
    private RelativeLayout llCard;
    private RelativeLayout llAddress;
    private RelativeLayout llTicket;
    private RelativeLayout llSet;

    public PersonalFragment() {
        // Required empty public constructor
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static PersonalFragment createInstance() {
        return new PersonalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_personal);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    @Override
    public void initView() {
        ivLeft = findViewById(R.id.iv_left);
        tvRight = findViewById(R.id.tv_right);

        rlPersonalMessage = findViewById(R.id.rlPersonalMessage);

        llIdol = findViewById(R.id.llIdol);
        llFriends = findViewById(R.id.llFriends);
        llConversation = findViewById(R.id.llConversation);
        llHeart = findViewById(R.id.llHeart);
        llDiamonds = findViewById(R.id.llDiamonds);
        llCoin = findViewById(R.id.llCoin);
        llCampaign = findViewById(R.id.llCampaign);
        llOrder = findViewById(R.id.llOrder);
        llCard = findViewById(R.id.llCard);
        llAddress = findViewById(R.id.llAddress);
        llTicket = findViewById(R.id.llTicket);
        llSet = findViewById(R.id.llSet);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        rlPersonalMessage.setOnClickListener(this);

        llIdol.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        llConversation.setOnClickListener(this);
        llHeart.setOnClickListener(this);
        llDiamonds.setOnClickListener(this);
        llCoin.setOnClickListener(this);
        llCampaign.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llCard.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llTicket.setOnClickListener(this);
        llSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlPersonalMessage:
                toActivity(DataActivity.createIntent(context));
                break;
            case R.id.iv_left:
                toActivity(MessageActivity.createIntent(context));
                break;
            case R.id.tv_right:
                toActivity(SignInActivity.createIntent(context));
                break;
            case R.id.llIdol:
                toActivity(FocusStarsActivity.createIntent(context));
                break;
            case R.id.llFriends:
                toActivity(FriendsActivity.createIntent(context));
                break;
            case R.id.llConversation:
                toActivity(MyConversationActivity.createIntent(context));
                break;
            case R.id.llHeart:
                toActivity(HeartActivity.createIntent(context));
                break;
            case R.id.llDiamonds:
                toActivity(DiamondActivity.createIntent(context));
                break;
            case R.id.llCoin:
                toActivity(CoinActivity.createIntent(context));
                break;
            case R.id.llCampaign:
                toActivity(CampaignActivity.createIntent(context));
                break;
            case R.id.llOrder:
                toActivity(OrderActivity.createIntent(context));
                break;
            case R.id.llCard:
                toActivity(CardActivity.createIntent(context));
                break;
            case R.id.llAddress:
                toActivity(AddressActivity.createIntent(context));
                break;
            case R.id.llTicket:
                toActivity(TicketActivity.createIntent(context));
                break;
            case R.id.llSet:
                toActivity(SettingActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

}
