package com.invofreaks.www.boostup;

import android.support.test.uiautomator.UiAutomatorTestCase;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

/**
 * Created by goura on 01-10-2015.
 */
public class UIAutomateClick extends UiAutomatorTestCase {

    public void forceStopCLick() throws UiObjectNotFoundException {

        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

        UiObject forceStop = mDevice.findObject(new UiSelector().text("Force stop"));
        forceStop.click();
    }

}
