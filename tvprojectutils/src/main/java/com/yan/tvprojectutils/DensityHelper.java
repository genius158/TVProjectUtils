package com.yan.tvprojectutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by yan on 2017/8/4.
 */

public class DensityHelper {
    private final static int CONT_VALUE = 72;

    private DensityHelper() {
    }

    /**
     * 重新计算displayMetrics.xhdpi, 使单位pt重定义为设计稿的相对长度
     *
     * @param context
     * @param designWidth 设计稿的宽度
     */
    private static void resetDensity(Context context, float designWidth) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        context.getResources().getDisplayMetrics().xdpi = dm.widthPixels / designWidth * CONT_VALUE;
    }

    /**
     * 恢复displayMetrics为系统原生状态，单位pt恢复为长度单位磅
     *
     * @param context
     */
    private static void restoreDensity(Context context) {
        context.getResources().getDisplayMetrics().setToDefaults();
    }

    /**
     * 转换dp为px
     *
     * @param context
     * @param value   需要转换的dp值
     * @return
     */
    public static float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 转换pt为px
     *
     * @param context
     * @param value   需要转换的pt值，若context.resources.displayMetrics经过resetDensity()的修改则得到修正的相对长度，否则得到原生的磅
     * @return
     */
    public static float pt2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 激活本方案
     * <p>
     * the method may not work if you just call in application
     * than you can call it in your base activity
     */
    public static void activate(Context context, float designWidth) {
        resetDensity(context, designWidth);
    }

    /**
     * 恢复系统原生方案
     */
    public static void inactivate(Context context) {
        restoreDensity(context);
    }

}
