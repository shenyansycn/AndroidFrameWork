package org.android.framework.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;

public class MyProgressDialog {
    public MyProgressDialog(Context context) {
        super();
        this.context = context;
    }

    public MyProgressDialog(Context context, String loadText) {
        super();
        this.context = context;
        this.message = loadText;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;
    private String message;

    public void setMessage(String message) {
        this.message = message;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }
    }

    private boolean isCancelable = true;

    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setCancelable(isCancelable);
        }
    }

    /**
     * 显示精度条对话框
     */
    public void showProgressDialog(AsyncTask<?, ?, ?> asyncTask) {
        if (mProgressDialog == null) {
            mProgressDialog = new MProgressDialog(context, asyncTask);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(isCancelable);
            mProgressDialog.setOnCancelListener(cancelListener);
            mProgressDialog.show();
        } else if (mProgressDialog.isShowing()) {
            mProgressDialog = null;
            mProgressDialog = new MProgressDialog(context, asyncTask);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(isCancelable);
            mProgressDialog.setOnCancelListener(cancelListener);
            mProgressDialog.show();
        }
    }

    /**
     * 显示精度条对话框
     */
    public void showProgressDialog() {
        mProgressDialog = null;
        mProgressDialog = new MProgressDialog(context, null);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setOnCancelListener(cancelListener);
        mProgressDialog.show();
    }

    private OnCancelListener cancelListener;

    public void setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public boolean isShowLoading() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        } else {
            return false;
        }
    }

    public synchronized void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    private ProgressDialog mProgressDialog = null;

    /**
     * 进度对话框
     */
    private class MProgressDialog extends ProgressDialog {
        private AsyncTask<?, ?, ?> asyncTask;

        public MProgressDialog(Context context, AsyncTask<?, ?, ?> asyncTask) {
            super(context);
            this.asyncTask = asyncTask;
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (asyncTask != null && asyncTask.getStatus() != Status.FINISHED) {
                asyncTask.cancel(true);
                if (cancelListener != null) {
                    cancelListener.onCancel(mProgressDialog);
                }
            }
        }
    }

}
