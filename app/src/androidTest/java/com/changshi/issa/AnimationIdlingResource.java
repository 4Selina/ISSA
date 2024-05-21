package com.changshi.issa;

import androidx.test.espresso.IdlingResource;


public class AnimationIdlingResource implements IdlingResource {
    private String resourceName = null;

    // 构造函数中设置资源名称
    public AnimationIdlingResource() {
        this.resourceName = resourceName;
    }

    @Override
    public String getName() {
        return null;
    }



    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {

    }

    @Override
    public boolean isIdleNow() {
        return false;
    }

}
