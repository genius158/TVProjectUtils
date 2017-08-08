package com.yan.tvprojectutils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by yan on 2017/7/26.
 * use MarqueeText make sure that you have set like below
 * android:layout_width="match_parent"
 */
public class MarqueeText extends AppCompatTextView implements Runnable {
    private final static String DISABLE = "null";

    private int pixelDuring = 25;

    private int pixelsPerMoving = 1;

    private boolean fadingEdgeEnabled = false;
    private boolean isScrolling;

    private int currentScrollX;

    private String content = "";

    private String textSpace = "            ";
    private String textEndStyle = "...";

    private int gravity = -1;

    public MarqueeText(Context context) {
        this(context, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MarqueeText(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setHorizontallyScrolling(true);
    }

    private void setSelfText(final String text) {
        if (getWidth() == 0) {
            setFinalText(text);
            setVisibility(INVISIBLE);
            if (loadFinalTextRunnable == null) {
                loadFinalTextRunnable = new LoadFinalTextRunnable(this);
            }
            post(loadFinalTextRunnable.setText(text));
        } else {
            setFinalText(text);
        }
    }

    @Override
    public void setGravity(int gravity) {
        this.gravity = gravity;
        if (!isScrolling) {
            setSupperGravity(gravity);
        }
    }

    public void setSupperGravity(int gravity) {
        super.setGravity(gravity);
    }

    @Override
    public void setText(final CharSequence text, final TextView.BufferType type) {
        if (text == null) {
            return;
        }
        content = text.toString();

        if (isScrolling) {
            stopScroll();
            setSelfText(content);
            startScroll();
            return;
        }
        setSelfText(content);
    }

    private void setFinalText(CharSequence text) {
        if (getWidth() != 0) {
            attributeDell(text.toString());
        }

        if (!isScrolling) {
            String resetStr = text.toString();
            if (getWidth() != 0) {
                resetStr = resetText(resetStr);
            }
            super.setText(resetStr, TextView.BufferType.NORMAL);
            return;
        }
        String finalText = text.toString();
        if (!DISABLE.equals(textSpace)) {
            finalText = finalText + textSpace + finalText;
        }
        super.setText(finalText, TextView.BufferType.NORMAL);
    }

    private void attributeDell(String str) {
        setHorizontalFadingEdgeEnabled(fadingEdgeEnabled && getTextWidth(str) >= getWidth());

        if (gravity == -1) {
            gravity = getGravity();
        }
        if (isScrolling) {
            if (getTextWidth(str) > getWidth()) {
                if (!(getGravity() == Gravity.START
                        || getGravity() == (Gravity.TOP | Gravity.START)
                        || getGravity() == (Gravity.BOTTOM | Gravity.START)
                        || getGravity() == (Gravity.CENTER_VERTICAL | Gravity.START)
                )) {
                    setSupperGravity(Gravity.START);
                }
            } else {
                setSupperGravity(gravity);
            }
        } else {
            if (getTextWidth(str) > getWidth()) {
                setSupperGravity(Gravity.START);
            } else if (getTextWidth(str) <= getWidth()) {
                setSupperGravity(gravity);
            }
        }
    }

    private String resetText(String content) {
        if (getTextWidth(content) <= getWidth() || DISABLE.equals(textEndStyle)) {
            return content;
        }

        int i = 0;
        while (i < content.length() && getTextWidth(content, i) < getWidth()) {
            i++;
        }
        String tempStr = content.substring(0, i);
        for (int j = 0; j <= i && getTextWidth(tempStr, tempStr.length()) > getWidth(); ++j) {
            tempStr = content.substring(0, i - j) + textEndStyle;
        }
        return tempStr;
    }

    private int getTextWidth(String text) {
        return getTextWidth(text, text.length());
    }

    private int getTextWidth(String text, int endIndex) {
        Rect rect = new Rect();
        getPaint().getTextBounds(text, 0, endIndex, rect);
        return rect.right - rect.left;
    }

    public void startScroll() {
        removeCallbacks(startRunnable);
        postDelayed(startRunnable, 150);
    }

    public void stopScroll() {
        isScrolling = false;
        if (getWidth() == 0 || getTextWidth(content) <= getWidth()) {
            return;
        }
        removeCallbacks(startRunnable);
        scrollTo(0, 0);
        currentScrollX = 0;
        setSelfText(content);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSelfText(content);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopScroll();
        super.onDetachedFromWindow();
    }

    public void setFadingEdgeEnabled(boolean fadingEdgeEnabled) {
        this.fadingEdgeEnabled = fadingEdgeEnabled;
    }

    public void setPixelDuring(int pixelDuring) {
        this.pixelDuring = pixelDuring;
    }

    public void setPixelsPerMoving(int pixelsPerMoving) {
        this.pixelsPerMoving = pixelsPerMoving;
    }

    /**
     * text scroll out complete than the space the next text scroll out
     *
     * @param space
     */
    private void setNextSpace(int space) {
        textSpace = "";
        while (getPaint().measureText(textSpace) < space) {
            textSpace += " ";
        }
    }

    /**
     * textEndStyle you can set like ...
     *
     * @param textEndStyle
     */
    public void setTextEndStyle(String textEndStyle) {
        this.textEndStyle = textEndStyle;
    }

    private final class LoadFinalTextRunnable implements Runnable {
        private String text;
        private WeakReference<MarqueeText> weakReference;

        private LoadFinalTextRunnable(MarqueeText marqueeText) {
            this.weakReference = new WeakReference<>(marqueeText);
        }

        public LoadFinalTextRunnable setText(String text) {
            this.text = text;
            return this;
        }

        @Override
        public void run() {
            MarqueeText mt = weakReference.get();
            if (mt != null) {
                mt.setFinalText(text);
                mt.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * some runnable
     */
    @Override
    public void run() {
        if (!isScrolling) {
            return;
        }
        currentScrollX += pixelsPerMoving;
        scrollTo(currentScrollX, 0);
        if (!DISABLE.equals(textSpace)) {
            if (getScrollX() >= getPaint().measureText(content + textSpace)) {
                currentScrollX = 0;
            }
        } else if (getScrollX() >= getTextWidth(content)) {
            currentScrollX = -(this.getWidth());
        }

        postDelayed(this, pixelDuring);
    }

    private LoadFinalTextRunnable loadFinalTextRunnable;

    private final Runnable startRunnable = new Runnable() {
        public void run() {
            if (getTextWidth(content) > getWidth()) {
                removeCallbacks(MarqueeText.this);
                isScrolling = true;
                setSelfText(content);
                post(MarqueeText.this);
            }
        }
    };
}