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

import com.sanyuzhang.circular.viewpager.sample.viewpager.CircularViewPagerFragment;
import com.sanyuzhang.circular.viewpager.sample.viewpager.FragmentPageAdapter;
import com.sanyuzhang.circular.viewpager.sample.ResourceTable;
import com.sanyuzhang.circular.viewpager.sample.viewpager.TextFragment;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {
    private PageSlider contentPager;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        contentPager = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        contentPager.setPageCacheSize(5);
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter((FractionAbility) getAbility(), getData());
        contentPager.setProvider(fragmentPageAdapter);
        contentPager.setCurrentPage(0);
        contentPager.addPageChangedListener(new PageSlider.PageChangedListener() {
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
                contentPager.setCurrentPage(i);
            }
        });
    }

    private ArrayList<Fraction> getData() {
        ArrayList<Fraction> dataItem = new ArrayList<>();
        dataItem.add(new CircularViewPagerFragment(this));
        dataItem.add(new TextFragment("Fragment 1"));
        dataItem.add(new TextFragment("Fragment 2"));

        return dataItem;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
