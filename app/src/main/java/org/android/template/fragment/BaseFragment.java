package org.android.template.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    protected View mainView;

    protected BaseFragmentActivity getBaseFragmentActivity() {
        return (BaseFragmentActivity) getActivity();
    }

    protected void setBackArguments(Bundle bundle) {
        getBaseFragmentActivity().setBackArguments(bundle);
    }

    protected void finish() {
        getBaseFragmentActivity().backFragment();
    }

    protected void onFragmentArguments(Bundle bundle) {
    }

    protected FragmentTransaction startFragmentByArgument(Fragment fragment, String tag, Bundle bundle) {
        return getBaseFragmentActivity().startFragmentByArgument(fragment, tag, bundle);
    }

    protected FragmentTransaction startFragmentByArgument(Fragment fragment, Bundle bundle) {
        return getBaseFragmentActivity().startFragmentByArgument(fragment, "", bundle);
    }

    protected FragmentTransaction startFragment(Fragment fragment, String tag) {
        return getBaseFragmentActivity().startFragmentByArgument(fragment, tag, null);
    }

    protected FragmentTransaction startFragment(Fragment fragment) {
        return getBaseFragmentActivity().startFragmentByArgument(fragment, "", null);
    }

    protected boolean onBackKeyDown() {
        return false;
    }
}
