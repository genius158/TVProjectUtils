package com.yan.tvprojectutils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by yan on 2017/8/4.
 */
public class FocusRecyclerView extends RecyclerView {
    private boolean keyEventDown;
    private boolean keyEventUp;
    private boolean keyEventLeft;
    private boolean keyEventRight;
    private boolean isActionDown;
    private boolean isMoving;

    private boolean needGetDownView;
    private boolean needGetUpView;
    private boolean needGetLeftView;
    private boolean needGetRightView;

    private boolean isAbleFocusOut = true;

    public FocusRecyclerView(Context context) {
        super(context);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isMoving = (ev.getAction() == MotionEvent.ACTION_MOVE)
                || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int layoutDirection = getCurrentLayoutDirection();

        LayoutParams layoutParams = (LayoutParams) this.getChildAt(0).getLayoutParams();
        int offsetY = this.getChildAt(0).getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        int offsetX = this.getChildAt(0).getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

        View focusView = this.getFocusedChild();
        if (focusView != null) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    keyEventDown = true;
                    keyEventUp = false;
                    View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                    if (layoutDirection == OrientationHelper.HORIZONTAL || (isAbleFocusOut && downView == null && isRecyclerViewToBottom())) {
                        break;
                    }
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        isActionDown = false;
                        return true;
                    } else {
                        needGetDownView = false;
                        isActionDown = true;
                        if (downView != null) {
                            downView.requestFocusFromTouch();
                            downView.requestFocus();
                            return true;
                        } else {
                            if (!isRecyclerViewToBottom()) {
                                needGetDownView = true;
                            }
                            this.smoothScrollBy(0, offsetY);
                            return true;
                        }
                    }
                case KeyEvent.KEYCODE_DPAD_UP:
                    keyEventUp = true;
                    keyEventDown = false;
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        isActionDown = false;
                        return true;
                    } else {
                        isActionDown = true;
                        needGetUpView = false;
                        View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                        if (layoutDirection == OrientationHelper.HORIZONTAL || (upView == null && isRecyclerViewToTop())) {
                            break;
                        }
                        if (upView != null) {
                            upView.requestFocusFromTouch();
                            upView.requestFocus();
                            return true;
                        } else {
                            if (!isRecyclerViewToTop()) {
                                needGetUpView = true;
                            }
                            this.smoothScrollBy(0, -offsetY);
                            return true;
                        }
                    }
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    keyEventRight = true;
                    keyEventLeft = false;
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        isActionDown = false;
                        return true;
                    } else {
                        needGetRightView = false;
                        isActionDown = true;
                        View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                        if (layoutDirection == OrientationHelper.VERTICAL || (isAbleFocusOut && rightView == null && isRecyclerViewToRight())) {
                            break;
                        }
                        if (rightView != null) {
                            rightView.requestFocusFromTouch();
                            rightView.requestFocus();
                            return true;
                        } else {
                            if (!isRecyclerViewToRight()) {
                                needGetRightView = true;
                            }
                            this.smoothScrollBy(offsetX, 0);
                            return true;
                        }
                    }
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    keyEventRight = false;
                    keyEventLeft = true;
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                    if (layoutDirection == OrientationHelper.VERTICAL || (leftView == null && isRecyclerViewToLeft())) {
                        break;
                    }
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        isActionDown = false;
                        return true;
                    } else {
                        needGetLeftView = false;
                        isActionDown = true;
                        if (leftView != null) {
                            leftView.requestFocusFromTouch();
                            leftView.requestFocus();
                            return true;
                        } else {
                            if (!isRecyclerViewToLeft()) {
                                needGetLeftView = true;
                            }
                            this.smoothScrollBy(-offsetX, 0);
                            return true;
                        }
                    }
            }
        }

        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        final View focusView = getFocusedChild();
        if (focusView != null) {
            if ((dx > 0 && keyEventRight && isMoving && isActionDown) || (needGetRightView)) {
                View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                if (rightView != null) {
                    needGetLeftView = false;
                    needGetRightView = false;
                    rightView.requestFocusFromTouch();
                    rightView.requestFocus();
                }
            } else if ((dx < 0 && keyEventLeft && isMoving && isActionDown) || needGetLeftView) {
                View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                if (leftView != null) {
                    needGetLeftView = false;
                    needGetRightView = false;
                    leftView.requestFocusFromTouch();
                    leftView.requestFocus();
                }
            } else if ((dy > 0 && keyEventDown && isMoving && isActionDown) || (needGetDownView)) {
                View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                if (downView != null) {
                    needGetUpView = false;
                    needGetDownView = false;
                    downView.requestFocusFromTouch();
                    downView.requestFocus();
                }
            } else if ((dy < 0 && keyEventUp && isMoving && isActionDown) || needGetUpView) {
                View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                if (upView != null) {
                    needGetUpView = false;
                    needGetDownView = false;
                    upView.requestFocusFromTouch();
                    upView.requestFocus();
                }
            }
        }
    }

    public void setAbleFocusOut(boolean ableFocusOut) {
        isAbleFocusOut = ableFocusOut;
    }

    private int getCurrentLayoutDirection() {
        int layoutDirection = 1;
        if (getLayoutManager() != null) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                layoutDirection = ((LinearLayoutManager) getLayoutManager()).getOrientation();
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                layoutDirection = ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation();
            }
        }
        return layoutDirection;
    }

    /**
     * ---------------------- recyclerView can scroll -----------------------
     *
     * @return
     */

    public boolean isRecyclerViewToTop() {
        LayoutManager manager = this.getLayoutManager();
        if (manager == null) {
            return true;
        }
        if (manager.getItemCount() == 0) {
            return true;
        }

        int firstChildTop = 0;
        if (this.getChildCount() > 0) {
            View firstVisibleChild = this.getChildAt(0);
            if (firstVisibleChild != null && firstVisibleChild.getMeasuredHeight() >= this.getMeasuredHeight()) {
                if (Build.VERSION.SDK_INT < 14) {
                    return !(ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0);
                } else {
                    return !ViewCompat.canScrollVertically(this, -1);
                }
            }

            View firstChild = this.getChildAt(0);
            LayoutParams layoutParams = (LayoutParams) firstChild.getLayoutParams();
            firstChildTop = firstChild.getTop() - layoutParams.topMargin - getRecyclerViewItemTopInset(layoutParams) - this.getPaddingTop();
        }
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
            if (out[0] < 1 && firstChildTop == 0) {
                return true;
            }
        }
        return false;
    }

    private int getRecyclerViewItemTopInset(LayoutParams layoutParams) {
        try {
            Field field = LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            Rect decorInsets = (Rect) field.get(layoutParams);
            return decorInsets.top;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isRecyclerViewToBottom() {
        LayoutManager manager = this.getLayoutManager();
        if (manager == null || manager.getItemCount() == 0) {
            return false;
        }

        if (manager instanceof LinearLayoutManager) {
            View lastVisibleChild = this.getChildAt(this.getChildCount() - 1);
            if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= this.getMeasuredHeight()) {
                if (Build.VERSION.SDK_INT < 14) {
                    return !(ViewCompat.canScrollVertically(this, 1) || this.getScrollY() < 0);
                } else {
                    return !ViewCompat.canScrollVertically(this, 1);
                }
            }

            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

            int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
            int lastPosition = layoutManager.getItemCount() - 1;
            for (int position : out) {
                if (position == lastPosition) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRecyclerViewToLeft() {
        LayoutManager manager = this.getLayoutManager();
        if (manager == null) {
            return true;
        }
        if (manager.getItemCount() == 0) {
            return true;
        }

        int firstChildLeft = 0;
        if (this.getChildCount() > 0) {
            View firstVisibleChild = this.getChildAt(0);
            if (firstVisibleChild != null && firstVisibleChild.getMeasuredWidth() >= this.getMeasuredWidth()) {
                if (Build.VERSION.SDK_INT < 14) {
                    return !(ViewCompat.canScrollHorizontally(this, -1) || this.getScrollX() > 0);
                } else {
                    return !ViewCompat.canScrollHorizontally(this, -1);
                }
            }

            View firstChild = this.getChildAt(0);
            LayoutParams layoutParams = (LayoutParams) firstChild.getLayoutParams();
            firstChildLeft = firstChild.getLeft() - layoutParams.leftMargin - getRecyclerViewItemLeftInset(layoutParams) - this.getPaddingLeft();
        }
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildLeft == 0) {
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
            if (out[0] < 1 && firstChildLeft == 0) {
                return true;
            }
        }
        return false;
    }

    private int getRecyclerViewItemLeftInset(LayoutParams layoutParams) {
        try {
            Field field = LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            Rect decorInsets = (Rect) field.get(layoutParams);
            return decorInsets.left;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isRecyclerViewToRight() {
        LayoutManager manager = this.getLayoutManager();
        if (manager == null || manager.getItemCount() == 0) {
            return false;
        }

        if (manager instanceof LinearLayoutManager) {
            View lastVisibleChild = this.getChildAt(this.getChildCount() - 1);
            if (lastVisibleChild != null && lastVisibleChild.getMeasuredWidth() >= this.getMeasuredWidth()) {
                if (Build.VERSION.SDK_INT < 14) {
                    return !(ViewCompat.canScrollHorizontally(this, 1) || this.getScrollX() < 0);
                } else {
                    return !ViewCompat.canScrollHorizontally(this, 1);
                }
            }

            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

            int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
            int lastPosition = layoutManager.getItemCount() - 1;
            for (int position : out) {
                if (position == lastPosition) {
                    return true;
                }
            }
        }
        return false;
    }
}
