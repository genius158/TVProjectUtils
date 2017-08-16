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
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by yan on 2017/8/4.
 */
public class FocusRecyclerView extends RecyclerView {
    private boolean needGetDownView;
    private boolean needGetUpView;
    private boolean needGetLeftView;
    private boolean needGetRightView;

    /**
     * isFocusOutAble = true
     * <p>
     * what is the effect ?
     * <p>
     * for example if the orientation in layoutManager is horizontal
     * when the recyclerView scroll to end that the focus could be out of recyclerView
     * just effect the direction that load more able to trigger
     */
    private boolean isFocusOutAble = true;

    public FocusRecyclerView(Context context) {
        this(context, null);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            resetValue();
        } else {
            return super.dispatchKeyEvent(event);
        }

        if (this.getChildAt(0) == null) {
            return super.dispatchKeyEvent(event);
        }

        LayoutParams layoutParams = (LayoutParams) this.getChildAt(0).getLayoutParams();
        int offsetY = this.getChildAt(0).getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        int offsetX = this.getChildAt(0).getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

        View focusView = this.getFocusedChild();

        int layoutDirection = getCurrentLayoutDirection();

        if (focusView != null) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                    if (layoutDirection == OrientationHelper.HORIZONTAL || (isFocusOutAble && downView == null && isRecyclerViewToBottom())) {
                        break;
                    }
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
                case KeyEvent.KEYCODE_DPAD_UP:
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
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                    if (layoutDirection == OrientationHelper.VERTICAL || (isFocusOutAble && rightView == null && isRecyclerViewToRight())) {
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
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                    if (layoutDirection == OrientationHelper.VERTICAL || (leftView == null && isRecyclerViewToLeft())) {
                        break;
                    }
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
        return super.dispatchKeyEvent(event);
    }

    private void resetValue() {
        needGetDownView = false;
        needGetUpView = false;
        needGetLeftView = false;
        needGetRightView = false;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        final View focusView = getFocusedChild();
        if (focusView != null) {
            if (needGetRightView) {
                View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                if (rightView != null) {
                    needGetRightView = false;
                    rightView.requestFocusFromTouch();
                    rightView.requestFocus();
                }
            } else if (needGetLeftView) {
                View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                if (leftView != null) {
                    needGetLeftView = false;
                    leftView.requestFocusFromTouch();
                    leftView.requestFocus();
                }
            } else if (needGetDownView) {
                View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                if (downView != null) {
                    needGetDownView = false;
                    downView.requestFocusFromTouch();
                    downView.requestFocus();
                }
            } else if (needGetUpView) {
                View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                if (upView != null) {
                    needGetUpView = false;
                    upView.requestFocusFromTouch();
                    upView.requestFocus();
                }
            }
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        final int tempFocusIndex = indexOfChild(getFocusedChild());
        if (tempFocusIndex == -1) {
            return i;
        }
        if (tempFocusIndex == i) {
            return childCount - 1;
        } else if (i == childCount - 1) {
            return tempFocusIndex;
        } else {
            return i;
        }
    }

    public void setFocusOutAble(boolean focusOutAble) {
        isFocusOutAble = focusOutAble;
    }

    public void setFocusFrontAble(boolean focusFront) {
        setChildrenDrawingOrderEnabled(focusFront);
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
            firstChildTop = firstChild.getTop() - layoutParams.topMargin - getRecyclerViewItemInset(layoutParams, 1) - this.getPaddingTop();
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
            firstChildLeft = firstChild.getLeft() - layoutParams.leftMargin - getRecyclerViewItemInset(layoutParams, 2) - this.getPaddingLeft();
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

    private int getRecyclerViewItemInset(LayoutParams layoutParams, int type) {
        try {
            Field field = LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            Rect decorInsets = (Rect) field.get(layoutParams);
            if (type == 1) {
                return decorInsets.top;
            } else if (type == 2) {
                return decorInsets.left;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
