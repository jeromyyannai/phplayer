package com.az.pplayer.Base;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

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
