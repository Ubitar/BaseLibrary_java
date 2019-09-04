package com.common.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.common.R;
import com.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoadingDialog extends DialogFragment {

    private static final String CANCELABLE = "CANCELABLE";
    private static final String DIM_AMOUNT = "DIM_AMOUNT";
    private static final String TEXT = "TEXT";

    public static final String TAG = "LOADING_DIALOG";

    @BindView(R2.id.txt_tips_loading_msg)
    TextView txt_tips_loading_msg;

    private float dimAmount = 0.5f;
    private boolean cancelable = true;
    private String text = "加载中";
    private DialogInterface.OnDismissListener onDismissListener;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogTheme);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CANCELABLE))
                cancelable = bundle.getBoolean(CANCELABLE, true);
            if (bundle.containsKey(DIM_AMOUNT))
                dimAmount = bundle.getFloat(DIM_AMOUNT, dimAmount);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_base_loading, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(TEXT)) setText(bundle.getString(TEXT));
        }
    }

    public void setText(String text) {
        this.text = text;
        if (txt_tips_loading_msg != null) txt_tips_loading_msg.setText(this.text);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.getWindow().setDimAmount(dimAmount);

        //点击返回键不消失，需要监听OnKeyListener:
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return !cancelable;
                }
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (onDismissListener != null) onDismissListener.onDismiss(dialog);
        super.onCancel(dialog);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void show(FragmentManager manager) {
        if (manager.isStateSaved() || manager.isDestroyed()) return;
        LoadingDialogManager.getInstance().show(this, manager);
    }

    public int show(FragmentTransaction transaction) {
        return super.show(transaction, TAG);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public static class LoadingDialogManager {

        private static LoadingDialogManager manager;

        public static LoadingDialogManager getInstance() {
            if (manager == null) {
                synchronized (LoadingDialogManager.class) {
                    if (manager == null) {
                        manager = new LoadingDialogManager();
                    }
                }
            }
            return manager;
        }

        private LoadingDialogManager() {

        }

        public void show(LoadingDialog dialog, FragmentManager fragmentManager) {
            Fragment fragment = fragmentManager.findFragmentByTag(LoadingDialog.TAG);
            if (fragment == null) {
                dialog.showNow(fragmentManager, LoadingDialog.TAG);
            }
        }

        public void cancel(FragmentManager fragmentManager) {
            Fragment fragment = fragmentManager.findFragmentByTag(LoadingDialog.TAG);
            if (fragment != null && fragment instanceof LoadingDialog) {
                ((LoadingDialog) fragment).dismissAllowingStateLoss();
            }
        }

    }

    public static class Builder {
        private final Bundle bundle;
        private DialogInterface.OnDismissListener onDismissListener;

        public Builder() {
            bundle = new Bundle();
        }

        public Builder setCancelable(boolean cancelable) {
            bundle.putBoolean(CANCELABLE, cancelable);
            return this;
        }

        public Builder setDimAount(float dimAount) {
            bundle.putFloat(DIM_AMOUNT, dimAount);
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setText(String text) {
            bundle.putString(TEXT, text);
            return this;
        }

        public LoadingDialog build() {
            LoadingDialog dialog = new LoadingDialog();
            dialog.setOnDismissListener(onDismissListener);
            dialog.setArguments(bundle);
            return dialog;
        }
    }

}
