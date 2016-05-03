package org.android.widget.bigpicbyscale;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
    Context context;

    public HackyViewPager(Context context) {

        super(context);
        this.context = context;
    }


    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            /*Intent intent = new Intent("FRIENDNUM");
			intent.putExtra("NUMRESULT", "s");
			context.sendBroadcast(intent);*/
            //tvLetter.setText(s);
            //tvLetter.setVisibility(View.VISIBLE);
			/*handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 500);*/
            //System.out.println("111111");
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}
