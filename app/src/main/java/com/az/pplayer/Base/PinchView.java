package com.az.pplayer.Base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PinchView extends NestedScrollView {
    private boolean topZone = false;
    private IOnTouchListener listener;

    public PinchView(Context context) {
        super(context);
    }

    public PinchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PinchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
       boolean res =  super.onTouchEvent(ev);
        if (listener != null )
           return res && listener.onTouchDetected(ev);
        return res;
    }
    public void setOnTouchListener(IOnTouchListener listener){

        this.listener = listener;
    }

        public interface IOnTouchListener{
        boolean onTouchDetected(MotionEvent ev);
        }
}
