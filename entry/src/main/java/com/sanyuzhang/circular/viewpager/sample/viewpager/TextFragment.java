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

import com.sanyuzhang.circular.viewpager.sample.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

public class TextFragment extends BaseFraction {
    private String params;

    @Override
    protected void initComponent(Component component) {
        Text tv = (Text) component.findComponentById(ResourceTable.Id_text);
        tv.setText(params);
    }

    @Override
    protected int getUIContent() {
        return ResourceTable.Layout_fraction_text;
    }

    public TextFragment(String params) {
        this.params = params;
    }
}
