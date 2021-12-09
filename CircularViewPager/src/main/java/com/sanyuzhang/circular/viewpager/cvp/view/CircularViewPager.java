package com.sanyuzhang.circular.viewpager.cvp.view;

import com.sanyuzhang.circular.viewpager.cvp.adapter.CircularViewPagerAdapter;
import ohos.agp.components.*;
import ohos.agp.database.DataSetSubscriber;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.multimodalinput.event.TouchEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by j_cho on 2017/10/03.
 */

public class CircularViewPager extends PageSlider implements Component.TouchEventListener, Component.BindStateChangedListener, ComponentTreeObserver.WindowFocusUpdatedListener {
    private PageSliderProvider wrappedPagerAdapter;
    private PageSliderProvider wrapperAdapter;
    private PageChangedListener mOnPageChangeListener;
    private CircularScroll scroller;
    private H handler;
    private boolean autoScroll = false;
    private int intervalInMillis;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int touchSlop;
    private OnPageClickListener onPageClickListener;

    private static final int MSG_AUTO_SCROLL = 0;
    private static final int DEFAULT_INTERNAL_IM_MILLIS = 2000;

    private List<PageChangedListener> mOnPageChangeListeners = new LinkedList<>();
    private InnerDataSetObserver mObserver = new InnerDataSetObserver();
    private InnerOnPageChangeListener innerOnPageChangeListener = new InnerOnPageChangeListener();
    private EventRunner eventRunner = EventRunner.getMainEventRunner();

    public interface OnPageClickListener {
        void onPageClick(CircularViewPager pager, int position);
    }

    private class H extends EventHandler {
        private H(EventRunner runner) {
            super(runner);
        }

        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            int eventId = event.eventId;
            if (eventId == MSG_AUTO_SCROLL) {
                setCurrentPage(getCurrentPage() + 1);
                sendEvent(MSG_AUTO_SCROLL, intervalInMillis);
            }
        }
    }

    public CircularViewPager(Context context) {
        super(context);
        init();
    }

    public CircularViewPager(Context context, AttrSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.addPageChangedListener(innerOnPageChangeListener);
        setTouchEventListener(this);
        handler = new H(eventRunner);
        getComponentTreeObserver().addWindowFocusUpdatedListener(this);
        setBindStateChangedListener(this);
        touchSlop = ViewConfig.getTouchSlop();
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        if (autoScroll) {
            startAutoScroll();
        }
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        pauseAutoScroll();
    }

    public void startAutoScroll() {
        startAutoScroll(intervalInMillis != 0 ? intervalInMillis : DEFAULT_INTERNAL_IM_MILLIS);
    }

    public void startAutoScroll(int intervalInMillis) {
        // Only post scroll message when necessary.
        if (getCount() > 1) {
            this.intervalInMillis = intervalInMillis;
            autoScroll = true;
            pauseAutoScroll();
            handler.sendEvent(MSG_AUTO_SCROLL, intervalInMillis);
        }
    }

    public void stopAutoScroll() {
        autoScroll = false;
        pauseAutoScroll();
    }

    public void setInterval(int intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    public void setScrollFactor(double factor) {
        setScrollerIfNeeded();
        scroller.setFactor(factor);
    }

    public OnPageClickListener getOnPageClickListener() {
        return onPageClickListener;
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    @Override
    public void onWindowFocusUpdated(boolean hasWindowFocus) {
        if (autoScroll) {
            if (hasWindowFocus) {
                startAutoScroll();
            } else {
                pauseAutoScroll();
            }
        }
    }

    public void setOnPageChangeListener(PageChangedListener listener) {
        mOnPageChangeListener = listener;
    }

    @Override
    public void addPageChangedListener(PageChangedListener listener) {
        mOnPageChangeListeners.add(listener);
    }


    public void clearOnPageChangeListeners() {
        mOnPageChangeListeners.clear();
        super.addPageChangedListener(innerOnPageChangeListener);
    }

    @Override
    public void setProvider(PageSliderProvider adapter) {
        if (wrappedPagerAdapter != null && mObserver != null) {
            wrappedPagerAdapter.removeDataSubscriber(mObserver);
        }
        wrappedPagerAdapter = adapter;
        if (wrappedPagerAdapter != null && mObserver != null) {
            wrappedPagerAdapter.addDataSubscriber(mObserver);
        }
        wrapperAdapter = (wrappedPagerAdapter == null) ? null : new CircularViewPagerAdapter(adapter);
        super.setProvider(wrapperAdapter);

        setCurrentPage(0, false);
    }

    @Override
    public PageSliderProvider getProvider() {
        // In order to be compatible with ViewPagerIndicator
        return wrappedPagerAdapter;
    }

    @Override
    public void setCurrentPage(int item) {
        super.setCurrentPage(item + 1);
    }

    @Override
    public void setCurrentPage(int item, boolean smoothScroll) {
        super.setCurrentPage(item + 1, smoothScroll);
    }

    @Override
    public int getCurrentPage() {
        int curr = super.getCurrentPage();
        if (wrappedPagerAdapter != null && wrappedPagerAdapter.getCount() > 1) {
            if (curr == 0) {
                curr = wrappedPagerAdapter.getCount() - 1;
            } else if (curr == wrapperAdapter.getCount() - 1) {
                curr = 0;
            } else {
                curr = curr - 1;
            }
        }
        return curr;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                setPage();
                pauseAutoScroll();
                mInitialMotionX = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX();
                mInitialMotionY = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY();
                break;
            case TouchEvent.POINT_MOVE:
                float lastMotionX = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX();
                float lastMotionY = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY();
                resetInitialMotion(lastMotionX, lastMotionY);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                if (autoScroll) {
                    startAutoScroll();
                }
                lastMotionX = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getX();
                lastMotionY = touchEvent.getPointerScreenPosition(touchEvent.getIndex()).getY();
                onPageClick(lastMotionX, lastMotionY);
                break;
        }
        return true;
    }

    /**
     * Get current item of the outer wrapper adapter.
     *
     * @return CurrentItem
     */
    private int getCurrentItemOfWrapper() {
        return super.getCurrentPage();
    }

    /**
     * Get item count of the outer wrapper adapter.
     *
     * @return CurrentItem
     */
    private int getCountOfWrapper() {
        if (wrapperAdapter != null) {
            return wrapperAdapter.getCount();
        }
        return 0;
    }

    /**
     * Get item count of the adapter which is set by user
     *
     * @return CurrentItem
     */
    private int getCount() {
        if (wrappedPagerAdapter != null) {
            return wrappedPagerAdapter.getCount();
        }
        return 0;
    }

    private void setScrollerIfNeeded() {
        if (scroller != null) {
            return;
        }
        scroller = new CircularScroll();
    }

    private void pauseAutoScroll() {
        handler.removeEvent(MSG_AUTO_SCROLL);
    }

    private void setPage() {
        if (getCurrentItemOfWrapper() + 1 == getCountOfWrapper()) {
            setCurrentPage(0, false);
        } else if (getCurrentItemOfWrapper() == 0) {
            setCurrentPage(getCount() - 1, false);
        }
    }

    private void resetInitialMotion(float lastMotionX, float lastMotionY) {
        if ((int) Math.abs((double)lastMotionX - (double)mInitialMotionX) > touchSlop
                || (int) Math.abs((double)lastMotionY - (double)mInitialMotionY) > touchSlop) {
            mInitialMotionX = 0.0f;
            mInitialMotionY = 0.0f;
        }
    }

    private void onPageClick(float lastMotionX, float lastMotionY) {
        if (scroller != null) {
            final double lastFactor = scroller.getFactor();
            scroller.setFactor(1);
            new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                @Override
                public void run() {
                    // Manually swipe not affected by scroll factor.
                    scroller.setFactor(lastFactor);
                }
            });
            if ((int) mInitialMotionX != 0 && (int) mInitialMotionY != 0) {
                if ((int) Math.abs((double) lastMotionX - (double) mInitialMotionX) < touchSlop
                        && (int) Math.abs((double) lastMotionY - (double) mInitialMotionY) < touchSlop) {
                    mInitialMotionX = 0.0f;
                    mInitialMotionY = 0.0f;
                    if (onPageClickListener != null) {
                        onPageClickListener.onPageClick(CircularViewPager.this, getCurrentPage());
                    }
                }
            }
        }
    }

    private class InnerOnPageChangeListener implements PageChangedListener {
        private int mLastSelectedPage = -1;

        private InnerOnPageChangeListener() {
        }

        @Override
        public void onPageSliding(int position, float positionOffset, int positionOffsetPixels) {
            final int pos;
            // Fix position
            if (position == 0) {
                pos = getCount() - 1;
            } else if (position == getCountOfWrapper() - 1) {
                pos = 0;
            } else {
                pos = position - 1;
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSliding(pos, positionOffset, positionOffsetPixels);
            }
            for (PageChangedListener onPageChangeListener : mOnPageChangeListeners) {
                onPageChangeListener.onPageSliding(pos, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSlideStateChanged(int state) {
            if (state == SLIDING_STATE_IDLE && getCount() > 1) {
                if (getCurrentItemOfWrapper() == 0) {
                    // scroll to the last page
                    setCurrentPage(getCount() - 1, false);
                } else if (getCurrentItemOfWrapper() == getCountOfWrapper() - 1) {
                    // scroll to the first page
                    setCurrentPage(0, false);
                }
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSlideStateChanged(state);
            }
            for (PageChangedListener onPageChangeListener : mOnPageChangeListeners) {
                onPageChangeListener.onPageSlideStateChanged(state);
            }
        }

        @Override
        public void onPageChosen(int position) {
            if (mOnPageChangeListener != null || mOnPageChangeListeners.size() > 0) {
                final int pos;
                // Fix position
                if (position == 0) {
                    pos = getCount() - 1;
                } else if (position == getCountOfWrapper() - 1) {
                    pos = 0;
                } else {
                    pos = position - 1;
                }

                if (mLastSelectedPage != pos) {
                    mLastSelectedPage = pos;
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener.onPageChosen(pos);
                    }
                    for (PageChangedListener onPageChangeListener : mOnPageChangeListeners) {
                        onPageChangeListener.onPageChosen(pos);
                    }
                }
            }
        }
    }

    private class InnerDataSetObserver extends DataSetSubscriber {
        @Override
        public void onChanged() {
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyDataChanged();
            }
        }

        @Override
        public void onInvalidated() {
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyDataChanged();
            }
        }
    }
}
