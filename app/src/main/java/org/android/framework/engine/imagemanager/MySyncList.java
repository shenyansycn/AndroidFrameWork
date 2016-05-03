package org.android.framework.engine.imagemanager;

import java.util.ArrayList;

/**
 * Created by ShenYan on 14-1-26.
 */
 class MySyncList<E> {
    private ArrayList<E> mArrayList;
    private Object mArrayListLock = new Object();

    public MySyncList(int size) {
        mArrayList = new ArrayList<E>(size);
    }

    public E get(int index) {
        synchronized (mArrayListLock) {
            return mArrayList.get(index);
        }
    }

    public void set(int index, E e) {
        synchronized (mArrayListLock) {
            mArrayList.add(index, e);
        }
    }

    public void set(E e) {
        synchronized (mArrayListLock) {
            mArrayList.add(e);
        }
    }

    public void remove(int index) {
        synchronized (mArrayListLock) {
            mArrayList.remove(index);
        }
    }

    public void clear() {
        synchronized (mArrayListLock) {
            mArrayList.clear();
        }
    }

}
