package com.jws.pandahealth.component.app.di;

import android.app.Activity;

import com.jws.pandahealth.base.di.FragmentScope;
import com.jws.pandahealth.base.di.module.FragmentModule;
import com.jws.pandahealth.component.askdoctor.ui.fragment.HomeFragment;
import com.jws.pandahealth.component.app.base.AppComponent;
import com.jws.pandahealth.component.more.ui.fragment.MoreFragment;
import com.jws.pandahealth.component.services.ui.fragment.HistoryFragment;
import com.jws.pandahealth.component.services.ui.fragment.MyDoctorFragment;
import com.jws.pandahealth.component.services.ui.fragment.NewChatFragment;
import com.jws.pandahealth.component.services.ui.fragment.ServicesFragment;

/**
 * Created by codeest on 16/8/7.
 *
 */

@FragmentScope
@dagger.Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(HomeFragment fragment);
    void inject(ServicesFragment fragment);
    void inject(NewChatFragment fragment);
    void inject(MyDoctorFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(MoreFragment fragment);
}
