package com.jws.pandahealth.component.app.base;

import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRealmHelper;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.base.di.ContextLife;
import com.jws.pandahealth.component.more.model.MoreRealmHelper;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;

import javax.inject.Singleton;

/**
 * Created by codeest on 16/8/7.
 *
 */
@Singleton
@dagger.Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife("Application")
    MyApplication getContext();  //提供App的Context

    RetrofitHelper retrofitHelper();  //提供http的帮助类

    AppRealmHelper appRealmHelper(); //提供数据库帮助类

    AppRetrofitHelper appRetrofitHelper();
    AskDoctorsRetrofitHelper askDoctorsRetrofitHelper();
    AskDoctorsRealmHelper askDoctorsRealmHelper();

    ServicesRealmHelper servicesRealmHelper();
    ServicesRetrofitHelper servicesRetrofitHelper();

    MoreRealmHelper moreRealmHelper();
    MoreRetrofitHelper moreRetrofitHelper();

}
