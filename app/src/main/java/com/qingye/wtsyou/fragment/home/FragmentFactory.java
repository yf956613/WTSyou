package com.qingye.wtsyou.fragment.home;

import android.support.v4.app.Fragment;

import com.qingye.wtsyou.fragment.fragmentFactory.HomeStarsOneFragment;
import com.qingye.wtsyou.fragment.fragmentFactory.HomeStarsThreeFragment;
import com.qingye.wtsyou.fragment.fragmentFactory.HomeStarsTwoFragment;

/**
 * Fragment工厂类
 */
public class FragmentFactory {
    /**
     * 根据position生产不同的fragment
     * @param position
     * @return
     */

    public static Fragment create(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeStarsOneFragment();
                break;
            case 1:
                fragment = new HomeStarsTwoFragment();
                break;
            case 2:
                fragment = new HomeStarsThreeFragment();
                break;
            case 3:
                fragment = new HomeMoreFragment();
                break;
        }
        return fragment;
    }
}