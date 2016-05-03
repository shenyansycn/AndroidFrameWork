package org.android.template.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import org.android.R;

import java.util.ArrayList;

/**
 * 基于FragmentActivity中的Fragment管理类，所有已经加入的Fragment都是hide状态，只有显示的Fragment才是show状态，用其他replace或者重新加载的都会重新初始化当前Fragment内容。
 * fragmentTagList保存Fragment的tag序列，以模拟实现堆栈的操作。
 */
public class BaseFragmentActivity extends FragmentActivity {
    protected Context context;
    /**
     * 保存fragment的tag容器
     */
    private static ArrayList<String> fragmentTagList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.main_fragment);
        context = this;
    }

    /**
     * 重载back按键功能，并反馈给当前显示的Fragment中，用于处理返回按键功能
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentTagList.size() <= 1) {
                exit();
                return true;
            } else {
                BaseFragment fragment = (BaseFragment) getBaseFragmentManager().findFragmentByTag(fragmentTagList.get(fragmentTagList.size() - 1));
                if (fragment != null) {
                    if (!fragment.onBackKeyDown()) {
                        backFragment();
                    }
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private int exitTipsTime = 3000;
    private Long beforeTime = 0l;

    /**
     * 退出提示功能
     */
    private void exit() {
        Long nowTime = System.currentTimeMillis();
        if (nowTime - beforeTime > exitTipsTime) {
            beforeTime = nowTime;
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    /**
     * 移除栈内顶部的fragment并显示栈内顶部fragment。
     */
    protected void backFragment() {
        //移除当前Fragment
        BaseFragment beforeFragment = (BaseFragment) getBaseFragmentManager().findFragmentByTag(fragmentTagList.get(fragmentTagList.size() - 1));
        if (beforeFragment != null) {
            getBaseFragmentTransaction().remove(beforeFragment).commit();
            fragmentTagList.remove(fragmentTagList.size() - 1);
        }

        BaseFragment fragment = (BaseFragment) getBaseFragmentManager().findFragmentByTag(fragmentTagList.get(fragmentTagList.size() - 1));
        if (fragment != null) {
            //设置回调函数
            if (getBackArguments() != null) {
                fragment.onFragmentArguments(getBackArguments());
            }
            //显示次级Fragment，并传递参数
            getBaseFragmentTransaction().show(fragment).commit();
            setBackArguments(null);
        }
    }

    private void backFragment(String tag) {
        //查找是否存在tag
        ArrayList<String> removeList = new ArrayList<String>();
        int existTag = -1;
        int size = fragmentTagList.size();
        for (int i = size - 1; i > 0; i--) {
            BaseFragment fragment = (BaseFragment) getBaseFragmentManager().findFragmentByTag(fragmentTagList.get(i));
            if (tag.equals(fragment.getTag())) {
                existTag = i;
                break;
            } else {
                removeList.add(fragment.getTag());
            }
        }

    }


    /**
     * fragment间传递消息所用参数
     */
    private Bundle args = null;

    /**
     * 设置参数
     *
     * @param args
     * @return
     */
    protected BaseFragmentActivity setBackArguments(Bundle args) {
        this.args = args;
        return this;
    }

    /**
     * 获得参数
     *
     * @return
     */
    protected Bundle getBackArguments() {
        return args;
    }


    protected FragmentTransaction startFragment(Fragment fragment, String tag) {
        return startFragmentByArgument(fragment, tag, null);
    }

    protected FragmentTransaction startFragment(Fragment fragment) {
        return startFragmentByArgument(fragment, "", null);
    }

    protected FragmentTransaction startFragmentByArgument(Fragment fragment, Bundle bundle) {
        return startFragmentByArgument(fragment, "", bundle);
    }

    protected FragmentTransaction startFragmentByArgument(Fragment fragment, String tag, Bundle bundle) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        //隐藏最新的Fragment
        if (fragmentTagList.size() > 0) {
            Fragment fragment1 = getBaseFragmentManager().findFragmentByTag(fragmentTagList.get(fragmentTagList.size() - 1));
            if (fragment1 != null) {
                ft.hide(fragment1).commit();
            }
        }
        ft = this.getSupportFragmentManager().beginTransaction();
        //如果有参数传递就设置参数
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        //如果定了tag，就用定义的tag，如果没有则按照数字创建
        if (tag == null || "".equals(tag)) {
            //        fragmentTag++;
            int size = fragmentTagList.size();
            fragmentTagList.add(String.valueOf(size + 1));
        } else {
            fragmentTagList.add(tag);
        }
        ft.add(R.id.main_fragment, fragment, fragmentTagList.get(fragmentTagList.size() - 1));
        ft.commit();
        return ft;
    }

    public FragmentManager getBaseFragmentManager() {
        return this.getSupportFragmentManager();
    }


    public FragmentTransaction getBaseFragmentTransaction() {
        return this.getSupportFragmentManager().beginTransaction();
    }

}
