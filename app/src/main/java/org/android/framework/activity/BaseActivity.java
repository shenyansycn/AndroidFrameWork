package org.android.framework.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;


public abstract class BaseActivity extends Activity {

    protected Context context;
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // initView();
    }

    // private void initView() {
    // View backbtn = findViewById(R.id.backbtn);
    // if (backbtn != null) {
    // backbtn.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // finish();
    // }
    // });
    // }
    // }

    // public void showEmptyView(boolean b, String s, OnClickListener listener)
    // {
    // if (emptyView == null) {
    // emptyView = findViewById(R.id.emptyview);
    // }
    // if (emptyView != null) {
    // emptyView.setVisibility(b ? View.VISIBLE : View.GONE);
    // if (s != null && !s.equals("")) {
    // ((TextView) emptyView.findViewById(R.id.tv)).setText(s);
    // }
    // if (listener != null) {
    // emptyView.setOnClickListener(listener);
    // }
    // }
    // }

    // public void showEmptyView(boolean b) {
    // showEmptyView(b, null, null);
    // }
}
