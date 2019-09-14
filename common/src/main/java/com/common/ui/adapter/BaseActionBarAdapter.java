package com.common.ui.adapter;

import android.view.ViewGroup;

public interface BaseActionBarAdapter {

    void injectView(ViewGroup viewGroup);

    void showActionBar();

    void hideActionBar();

    void release();

    void setOnClickLeftListener(OnClickLeftListener listener);

    void setOnClickRightListener(OnClickRightListener listener);

    interface OnClickLeftListener {
        void onClick();
    }

    interface OnClickRightListener {
        void onClick();
    }

}
