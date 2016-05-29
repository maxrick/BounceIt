package com.max.jumpingapp.views;

import android.content.Context;
import android.util.AttributeSet;

import com.daimajia.slider.library.SliderLayout;

/**
 * Created by max on 5/29/2016.
 */
public class CustomSliderLayout extends SliderLayout {//@// TODO: 5/29/2016 delete class
    public CustomSliderLayout(Context context) {
        super(context);
        setWillNotDraw(false);
        System.out.println("set will not draw1");
    }

    public CustomSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        System.out.println("set will not draw2");
    }

    public CustomSliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        System.out.println("set will not draw3");
    }

}
