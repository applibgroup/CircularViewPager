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
package com.sanyuzhang.circular.viewpager.sample.viewpager;

import com.sanyuzhang.circular.viewpager.cvp.view.CircularViewPager;
import com.sanyuzhang.circular.viewpager.sample.ResourceTable;
import com.sanyuzhang.circular.viewpager.sample.slice.ViewPagerIndicator;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class CircularViewPagerFragment extends BaseFraction {
    private String[] titles = {"Page 1", "Page 2", "Page 3", "Page 4", "Page 5"};
    private int[] imgs = {ResourceTable.Media_bg_img1, ResourceTable.Media_bg_img2, ResourceTable.Media_bg_img3, ResourceTable.Media_bg_img4, ResourceTable.Media_bg_img5};
    private ToastDialog toast;
    private Context context;
    private DependentLayout toastView;
    private Text toastText;

    public CircularViewPagerFragment(Context context) {
        this.context = context;
    }

    @Override
    protected void initComponent(Component component) {
        CircularViewPager pager = (CircularViewPager) component.findComponentById(ResourceTable.Id_scroll_pager);
        final Text title = (Text) component.findComponentById(ResourceTable.Id_title);

        ViewPagerIndicator indicator = (ViewPagerIndicator) component.findComponentById(ResourceTable.Id_indicator);

        pager.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) throws UnsupportedOperationException {
                // Empty on purpose. Implement this method if required.
            }

            @Override
            public void onPageSlideStateChanged(int i) throws UnsupportedOperationException {
                // Empty on purpose. Implement this method if required.
            }

            @Override
            public void onPageChosen(int i) {
                title.setText(titles[i]);
            }
        });

        pager.setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int position) {
                Image view = new Image(componentContainer.getContext());
                ComponentContainer.LayoutConfig config = new ComponentContainer.LayoutConfig();
                config.width = ComponentContainer.LayoutConfig.MATCH_PARENT;
                config.height = ComponentContainer.LayoutConfig.MATCH_PARENT;
                view.setLayoutConfig(config);
                view.setScaleMode(Image.ScaleMode.CLIP_CENTER);
                view.setPixelMap(imgs[position]);
                componentContainer.addComponent(view);
                return view;
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
                componentContainer.removeComponent((Component) o);
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object o) {
                return component == o;
            }
        });
        pager.setScrollFactor(7);
        pager.setPageCacheSize(6);
        pager.setOnPageClickListener(new CircularViewPager.OnPageClickListener() {

            @Override
            public void onPageClick(CircularViewPager autoScrollPager, int position) {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
                toast = new ToastDialog(context);
                toastView = (DependentLayout) LayoutScatter.getInstance(getFractionAbility()).parse(ResourceTable.Layout_fraction_toast, null, false);
                toastText = (Text) toastView.findComponentById(ResourceTable.Id_text);
                toastText.setText("Clicked page: " + (position + 1));
                toast.setComponent(toastView);
                toast.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
                toast.setTransparent(true);
                toast.setOffset(0, 100);
                toast.show();
            }
        });
        indicator.setOnIndicatorClickListener(new ViewPagerIndicator.OnIndicatorClickListener() {
            @Override
            public void onClick(int pos) {
                pager.setCurrentPage(pos);
            }
        });
        indicator.setViewPager(pager);
    }

    @Override
    protected int getUIContent() {
        return ResourceTable.Layout_fraction_circular_view_pager;
    }
}
