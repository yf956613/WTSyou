package com.qingye.wtsyou.fragment.home;

import android.support.v4.app.Fragment;

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
                fragment = new HomeStarsFragment();
                break;
            case 1:
                fragment = new HomeStarsFragment();
                break;
            case 2:
                fragment = new HomeStarsFragment();
                break;
            case 3:
                fragment = new HomeStarsFragment();
                break;
            case 4:
                fragment = new HomeMoreFragment();
                break;
        }
        return fragment;
    }
}