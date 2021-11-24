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

import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.agp.components.*;

import java.util.List;

public class FragmentPageAdapter extends PageSliderProvider {
    private FractionAbility context;
    private List<Fraction> fractionList;

    public FragmentPageAdapter(FractionAbility context, List<Fraction> fractionList) {
        this.fractionList = fractionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fractionList == null ? 0 : fractionList.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int position) {
        StackLayout parse = null;
        for (int i = 0; i < fractionList.size(); i++) {
            if (position == i) {
                parse = new StackLayout(context);
                StackLayout.LayoutConfig layoutConfig = new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT);
                parse.setLayoutConfig(layoutConfig);
                parse.setId(10000 + i);
                componentContainer.removeAllComponents();
                componentContainer.addComponent(parse);
                getFractionManager()
                        .startFractionScheduler()
                        .remove(fractionList.get(i))
                        .submit();

                getFractionManager()
                        .startFractionScheduler()
                        .add(parse.getId(), fractionList.get(i))
                        .show(fractionList.get(i))
                        .submit();
                break;
            }

        }
        return parse;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object obj) {
        if (componentContainer == null) {
            return;
        }
        if (obj instanceof Component) {
            componentContainer.removeComponent((Component) obj);
        }
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }

    public FractionManager getFractionManager() {
        if (this.context instanceof FractionAbility) {
            FractionAbility fractionAbility = (FractionAbility) this.context;
            return fractionAbility.getFractionManager();
        }
        return null;
    }
}
