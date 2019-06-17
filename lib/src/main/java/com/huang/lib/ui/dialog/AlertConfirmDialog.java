package com.huang.lib.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.huang.lib.R;
import com.huang.lib.R2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AlertConfirmDialog extends DialogFragment {

    public static final String CANCELABLE = "CANCELABLE";
    public static final String DIM_AMOUNT = "DIM_AMOUNT";
    public static final String CANCEL_TEXT = "CANCEL_TEXT";
    public static final String HIDE_CANCEL = "HIDE_CANCEL";
    public static final String SUBMIT_TEXT = "SUBMIT_TEXT";
    public static final String HIDE_SUBMIT = "HIDE_SUBMIT";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String INTERCEPT_CLOSE = "INTERCEPT_CLOSE";

    public static final String TAG = "ALERT_CONFIRM_DIALOG";


    private float dimAmount = 0.5f;
    private boolean cancelable = true, hideCancel, hideSubmit, interceptClose;
    private String title, content, cancelText, submitText;

    @BindView(R2.id.txtTitle)
    TextView txtTitle;
    @BindView(R2.id.txtContent)
    TextView txtContent;
    @BindView(R2.id.txtCancel)
    TextView txtCancel;
    @BindView(R2.id.viewSep2)
    View viewSep2;
    @BindView(R2.id.txtSubmit)
    TextView txtSubmit;

    private Unbinder unbinder;

    private OnClickSubmitListener onClickSubmitListener;
    private OnClickCancelListener onClickCancelListener;

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
            if (bundle.containsKey(CANCEL_TEXT))
                cancelText = bundle.getString(CANCEL_TEXT);
            if (bundle.containsKey(SUBMIT_TEXT))
                submitText = bundle.getString(SUBMIT_TEXT);
            if (bundle.containsKey(HIDE_CANCEL))
                hideCancel = bundle.getBoolean(HIDE_CANCEL, false);
            if (bundle.containsKey(HIDE_SUBMIT))
                hideSubmit = bundle.getBoolean(HIDE_SUBMIT, false);
            if (bundle.containsKey(TITLE))
                title = bundle.getString(TITLE);
            if (bundle.containsKey(CONTENT))
                content = bundle.getString(CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_base_alert_confirm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hideCancel) txtCancel.setVisibility(View.GONE);
        if (hideSubmit) txtSubmit.setVisibility(View.GONE);
        if (hideCancel || hideSubmit) viewSep2.setVisibility(View.GONE);
        if (cancelText != null) txtCancel.setText(cancelText);
        if (submitText != null) txtSubmit.setText(submitText);
        if (title != null) {
            txtTitle.setText(title);
            txtTitle.setVisibility(View.VISIBLE);
        }
        if (content != null) {
            txtContent.setText(content);
            txtContent.setVisibility(View.VISIBLE);
        }
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickSubmitListener != null)
                    onClickSubmitListener.onClick(AlertConfirmDialog.this);
                if (!interceptClose) dismiss();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCancelListener != null)
                    onClickCancelListener.onClick(AlertConfirmDialog.this);
                if (!interceptClose) dismiss();
            }
        });
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
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public static class Builder {
        private final Bundle bundle;
        private OnClickSubmitListener onClickSubmitListener;
        private OnClickCancelListener onClickCancelListener;

        public Builder() {
            bundle = new Bundle();
        }

        public AlertConfirmDialog.Builder setCancelable(boolean cancelable) {
            bundle.putBoolean(CANCELABLE, cancelable);
            return this;
        }

        public AlertConfirmDialog.Builder setDimAount(float dimAount) {
            bundle.putFloat(DIM_AMOUNT, dimAount);
            return this;
        }

        public AlertConfirmDialog.Builder setOnClickSubmitListener(OnClickSubmitListener onClickSubmitListener) {
            this.onClickSubmitListener = onClickSubmitListener;
            return this;
        }

        public AlertConfirmDialog.Builder setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
            this.onClickCancelListener = onClickCancelListener;
            return this;
        }

        public AlertConfirmDialog.Builder setTitle(String title) {
            bundle.putString(TITLE, title);
            return this;
        }

        public AlertConfirmDialog.Builder setContent(String content) {
            bundle.putString(CONTENT, content);
            return this;
        }

        public AlertConfirmDialog.Builder setSubmitText(String text) {
            bundle.putString(SUBMIT_TEXT, text);
            return this;
        }

        public AlertConfirmDialog.Builder setCancelText(String text) {
            bundle.putString(CANCEL_TEXT, text);
            return this;
        }

        public AlertConfirmDialog.Builder setHideCancel(boolean hide) {
            bundle.putBoolean(HIDE_CANCEL, hide);
            return this;
        }

        public AlertConfirmDialog.Builder setHideSubmit(boolean hide) {
            bundle.putBoolean(HIDE_SUBMIT, hide);
            return this;
        }

        public AlertConfirmDialog.Builder setInterceptClose(boolean interceptClose) {
            bundle.putBoolean(INTERCEPT_CLOSE, interceptClose);
            return this;
        }


        public AlertConfirmDialog build() {
            AlertConfirmDialog dialog = new AlertConfirmDialog();
            dialog.setOnClickSubmitListener(onClickSubmitListener);
            dialog.setOnClickCancelListener(onClickCancelListener);
            dialog.setArguments(bundle);
            return dialog;
        }
    }

    public interface OnClickSubmitListener {
        void onClick(AlertConfirmDialog dialog);
    }

    public interface OnClickCancelListener {
        void onClick(AlertConfirmDialog dialog);
    }

    public void setOnClickSubmitListener(OnClickSubmitListener onClickSubmitListener) {
        this.onClickSubmitListener = onClickSubmitListener;
    }

    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
    }
}
