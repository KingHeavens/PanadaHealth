package com.jws.pandahealth.base.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jws.pandahealth.base.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 *
 */
@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment. getActivity();
    }
}
