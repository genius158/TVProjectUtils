package com.yan.tvprojectutilstest;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by yan on 2017/7/21.
 */

public class AnimationHelper {
    private View largeView;
    private View smallView;

    private float ratioX=1.08f;
    private float ratioY=1.08f;

    public AnimationHelper() {
        valueLargeAnimator.setDuration(250);
        valueSmallAnimator.setDuration(250);

    }

    public AnimationHelper setLargeView(View largeView) {
        this.largeView = largeView;
        return this;
    }

    public AnimationHelper setSmallView(View smallView) {
        this.smallView = smallView;
        return this;
    }

    public AnimationHelper setRatioX(float ratioX) {
        this.ratioX = ratioX;
        return this;
    }

    public AnimationHelper setRatioY(float ratioY) {
        this.ratioY = ratioY;
        return this;
    }

    public void starLargeAnimation(View largeView,int ratioX,int ratioY) {
        setRatioX(ratioX);
        setRatioY(ratioY);
      starLargeAnimation(largeView);
    }

    public void starSmallAnimation(View smallView,int ratioX,int ratioY) {
        setRatioX(ratioX);
        setRatioY(ratioY);
        starSmallAnimation(smallView);
    }

    public void starLargeAnimation(View largeView) {
        this.largeView = largeView;
        if (this.largeView != null) {
            this.largeView.startAnimation(valueLargeAnimator);
        }
    }

    public void starSmallAnimation(View smallView) {
        this.smallView = smallView;
        if (this.smallView != null) {
            this.smallView.startAnimation(valueSmallAnimator);
        }
    }

    private Animation valueLargeAnimator = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            largeView.setScaleX(1 + interpolatedTime * (ratioX - 1));
            largeView.setScaleY(1 + interpolatedTime * (ratioY - 1));
        }
    };
    private Animation valueSmallAnimator = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            smallView.setScaleX(ratioX - (ratioX - 1) * interpolatedTime);
            smallView.setScaleY(ratioY - (ratioY - 1) * interpolatedTime);
        }
    };
}
