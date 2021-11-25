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
package com.sanyuzhang.circular.viewpager.cvp.view;

import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.regex.Pattern;

/**
 * Contains methods to standard constants used in the UI for timeouts, sizes, and distances.
 */
public class ViewConfig {
    /**
     * The coefficient of friction applied to flings/scrolls.
     */
    private static final float SCROLL_FRICTION = 0.015f;

    /**
     * Earth's gravity in SI units (m/s^2)
     */
    public static final float GRAVITY_EARTH = 9.80665f;

    /**
     * Defines the default duration in milliseconds before a press turns into
     * a long press
     */
    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds we will wait to see if a touch event
     * is a tap or a scroll. If the user does not move within this interval, it is
     * considered to be a tap.
     */
    private static final int TAP_TIMEOUT = 100;

    /**
     * Defines the duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_TIMEOUT = 300;

    /**
     * Defines the minimum duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    /**
     * Distance a touch can wander before we think the user is scrolling in dips.
     * Note that this value defined here is only used as a fallback by legacy/misbehaving
     * applications that do not provide a Context for determining density/configuration-dependent
     * values.
     * <p>
     * To alter this value, see the configuration resource config_viewConfigurationTouchSlop
     * in frameworks/base/core/res/res/values/config.xml or the appropriate device resource overlay.
     * It may be appropriate to tweak this on a device-specific basis in an overlay based on
     * the characteristics of the touch panel and firmware.
     */
    private static final int TOUCH_SLOP = 8;

    /**
     * Distance in dips between the first touch and second touch to still be considered a double tap
     */
    private static final int DOUBLE_TAP_SLOP = 100;

    /**
     * Minimum velocity to initiate a fling, as measured in dips per second
     */
    private static final int MINIMUM_FLING_VELOCITY = 50;

    /**
     * Maximum velocity to initiate a fling, as measured in dips per second
     */
    private static final int MAXIMUM_FLING_VELOCITY = 8000;

    private static final int MINIMUM_SCALING_SPAN = 145;

    /**
     * The amount of friction applied to scrolls and flings.
     *
     * @return A scalar dimensionless value representing the coefficient of
     * friction.
     */
    public static float getScrollFriction() {
        return SCROLL_FRICTION;
    }

    /**
     * getLongPressTimeout
     *
     * @return the duration in milliseconds before a press turns into
     * a long press
     */
    public static int getLongPressTimeout() {
        return DEFAULT_LONG_PRESS_TIMEOUT;
    }

    /**
     * getDoubleTapTimeout
     *
     * @return the duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    public static int getDoubleTapTimeout() {
        return DOUBLE_TAP_TIMEOUT;
    }

    /**
     * getTapTimeout
     *
     * @return the duration in milliseconds we will wait to see if a touch event
     * is a tap or a scroll. If the user does not move within this interval, it is
     * considered to be a tap.
     */
    public static int getTapTimeout() {
        return TAP_TIMEOUT;
    }

    /**
     * getDoubleTapMinTime
     *
     * @return the minimum duration in milliseconds between the first tap's
     * up event and the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    public static int getDoubleTapMinTime() {
        return DOUBLE_TAP_MIN_TIME;
    }

    /**
     * getTouchSlop
     *
     * @return Distance in dips a touch can wander before we think the user is scrolling
     */
    public static int getTouchSlop() {
        return TOUCH_SLOP;
    }

    /**
     * getDoubleTapSlop
     *
     * @return Distance in dips between the first touch and second touch to still be
     * considered a double tap
     */
    public static int getDoubleTapSlop() {
        return DOUBLE_TAP_SLOP;
    }

    /**
     * getMinimumFlingVelocity
     *
     * @return Minimum velocity to initiate a fling, as measured in dips per second.
     */
    public static int getMinimumFlingVelocity() {
        return MINIMUM_FLING_VELOCITY;
    }

    /**
     * getMaximumFlingVelocity
     *
     * @return Maximum velocity to initiate a fling, as measured in dips per second.
     */
    public static int getMaximumFlingVelocity() {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * Retrieves the distance in pixels between touches that must be reached for a gesture to be
     * interpreted as scaling.
     * <p>
     * In general, scaling shouldn't start until this distance has been met or surpassed, and
     * scaling should end when the distance in pixels between touches drops below this distance.
     *
     * @param context context
     * @return The distance in pixels
     * @throws IllegalStateException if this method is called on a ViewConfiguration that was
     *                               instantiated using a constructor with no Context parameter.
     */
    public static int getScaledMinimumScalingSpan(Context context) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (MINIMUM_SCALING_SPAN * attributes.densityPixels);
    }

    /**
     * isNumbers
     *
     * @param str
     * @return true false
     */
    public static boolean isNumbers(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * isHttpUrl
     *
     * @param url
     * @return isHttpUrl
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }
}
