package com.jws.pandahealth.component.app.base;

import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRealmHelper;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.RealmHelper;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.base.di.ContextLife;
import com.jws.pandahealth.component.more.model.MoreRealmHelper;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 *
 */

@dagger.Module
public class AppModule {

    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    MyApplication provideApplicationContext() {
        return application;
    }

    /** 数据请求 **/
    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper() {
        return new RetrofitHelper();
    }

    @Provides
    @Singleton
    AppRetrofitHelper provideAppRetrofitHelper() {
        return new AppRetrofitHelper();
    }
    @Provides
    @Singleton
    AskDoctorsRetrofitHelper provideAskDoctorsRetrofitHelper() {
        return new AskDoctorsRetrofitHelper();
    }
    @Provides
    @Singleton
    AskDoctorsRealmHelper provideAskDoctorsRealmHelper() {
        return new AskDoctorsRealmHelper();
    }

    /** 数据库 **/
    @Provides
    @Singleton
    RealmHelper provideRealmHelper() {
        return new RealmHelper();
    }

    @Provides
    @Singleton
    AppRealmHelper provideAppRealmHelper() {
        return new AppRealmHelper();
    }

    @Provides
    @Singleton
    ServicesRealmHelper provideServicesRealmHelper() {
        return new ServicesRealmHelper();
    }

    @Provides
    @Singleton
    ServicesRetrofitHelper provideServicesRetrofitHelper() {
        return new ServicesRetrofitHelper();
    }

    @Provides
    @Singleton
    MoreRetrofitHelper provideMoreRetrofitHelper() {
        return new MoreRetrofitHelper();
    }
    @Provides
    @Singleton
    MoreRealmHelper provideMoreRealmHelper() {
        return new MoreRealmHelper();
    }
}
