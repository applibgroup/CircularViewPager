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
package com.sanyuzhang.circular.viewpager.sample;

import com.sanyuzhang.circular.viewpager.cvp.view.ViewConfig;
import junit.framework.Assert;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CircularViewPagerTest {
    @Test
    /**
     * testPackageName
     */
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.sanyuzhang.circular.viewpager.sample", actualBundleName);
    }

    /**
     * testUtilsIsNumeric
     */
    @Test
    public void testUtilsIsNumeric() {
        boolean isEmpty = ViewConfig.isNumbers("12367");
        Assert.assertNotNull(isEmpty);
        assertEquals(true, isEmpty);
    }

    /**
     * testUtilsIsHttpUrl
     */
    @Test
    public void testUtilsIsHttpUrl() {
        boolean isEmpty = ViewConfig.isHttpUrl("https://github.com/sanyuzhang/CircularViewPager/raw/master/Sample/demo/demo.gif");
        Assert.assertNotNull(isEmpty);
        assertEquals(true, isEmpty);
    }
}