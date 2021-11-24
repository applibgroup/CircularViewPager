/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sanyuzhang.circular.viewpager.sample.slice;

import ohos.agp.components.*;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

public class ViewPagerIndicator extends DependentLayout implements Component.DrawTask, Component.EstimateSizeListener, Component.TouchEventListener {
    private int count = 5;
    private Paint paint;
    private float radius = 10;
    private float circlePadding = 8;
    private int selectPos = 0;

    public ViewPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public ViewPagerIndicator(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context);
    }

    public ViewPagerIndicator(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        setEstimateSizeListener(this);
        setAlignment(LayoutAlignment.CENTER);
        addDrawTask(this);
        setTouchEventListener(this);
    }

    public void setViewPager(PageSlider pageSlider) {
        PageSliderProvider provider = pageSlider.getProvider();
        if (provider == null || provider.getCount() == 0) {
            return;
        }
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {
                selectPos = i;
                invalidate();
            }
        });
        count = provider.getCount();
        invalidate();
    }


    @Override
    public void onDraw(Component component, Canvas canvas) {
        for (int i = 0; i < count; i++) {
            float x = (float) (getPaddingLeft() + (double)radius + ((double)circlePadding + (double)radius * 2) * i);
            float y = radius * 2;
            paint.reset();
            if (selectPos == i) {
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL_STYLE);
            } else {
                paint.setStrokeWidth(4);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE_STYLE);
            }
            canvas.drawCircle(x, y, radius, paint);
        }
    }

    @Override
    public boolean onEstimateSize(int i, int i1) {
        int newWidth = (int) (getPaddingLeft() + (double)circlePadding * (count - 1) + ((double)radius * 2) * count + getPaddingRight());
        int newHeight = (int) (getPaddingTop() + (double)radius + getPaddingBottom());
        setEstimatedSize(newWidth, newHeight);
        return true;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        int action = touchEvent.getAction();
        switch (action) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                float x = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
                float y = touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
                int pos = getClickPos(x, y);
                if (pos > -1) {
                    if (listener != null) {
                        listener.onClick(pos);
                    }
                }
                break;
        }
        return false;
    }

    private int getClickPos(float x, float y) {
        for (int i = 0; i < count; i++) {
            int pos = i;
            if (x >= getPaddingLeft() + (double)circlePadding * pos &&
                    x <= getPaddingLeft() + (double)radius * 2 * (pos + 1) + (double)circlePadding * pos &&
                    y >= getPaddingTop() &&
                    y <= getPaddingTop() + (double)radius * 2) {
                return pos;
            }
        }
        return -1;
    }

    private OnIndicatorClickListener listener;

    public interface OnIndicatorClickListener {
        void onClick(int pos);
    }

    public void setOnIndicatorClickListener(OnIndicatorClickListener listener) {
        this.listener = listener;
    }
}
