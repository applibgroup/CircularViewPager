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
package com.sanyuzhang.circular.viewpager.cvp.adapter;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSliderProvider;

/**
 * Creates a circular view pager adapter
 */
public class CircularViewPagerAdapter extends PageSliderProvider {
    private PageSliderProvider wrappedAdapter;

    public CircularViewPagerAdapter(PageSliderProvider wrapped) {
        wrappedAdapter = wrapped;
    }

    @Override
    public int getCount() {
        if (wrappedAdapter == null) {
            return 0;
        } else if (wrappedAdapter.getCount() > 1) {
            return wrappedAdapter.getCount() + 2;
        } else {
            return wrappedAdapter.getCount();
        }
    }

    @Override
    public String getPageTitle(int position) {
        return wrappedAdapter != null
                ? wrappedAdapter.getPageTitle(getRealPosition(position))
                : super.getPageTitle(position);
    }

    @Override
    public int getPageIndex(Object object) {
        return wrappedAdapter != null ? wrappedAdapter.getPageIndex(object) : super.getPageIndex(object);
    }

    @Override
    public Object createPageInContainer(ComponentContainer container, int position) {
        return wrappedAdapter.createPageInContainer(container, getRealPosition(position));
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer container, int position, Object object) {
        wrappedAdapter.destroyPageFromContainer(container, getRealPosition(position), object);
    }

    @Override
    public boolean isPageMatchToObject(Component view, Object o) {
        return wrappedAdapter.isPageMatchToObject(view, o);
    }

    @Override
    public void startUpdate(ComponentContainer container) {
        super.startUpdate(container);
        if (wrappedAdapter != null) {
            wrappedAdapter.startUpdate(container);
        }
    }

    @Override
    public void onUpdateFinished(ComponentContainer container) {
        super.onUpdateFinished(container);
        if (wrappedAdapter != null) {
            wrappedAdapter.onUpdateFinished(container);
        }
    }

    private int getRealPosition(int position) {
        if (wrappedAdapter != null && wrappedAdapter.getCount() > 1) {
            if (position == 0) {
                return wrappedAdapter.getCount() - 1;
            } else if (position == wrappedAdapter.getCount() + 1) {
                return 0;
            } else {
                return position - 1;
            }
        }
        return position;
    }
}
