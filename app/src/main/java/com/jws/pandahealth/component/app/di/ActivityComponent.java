package com.jws.pandahealth.component.app.di;

import android.app.Activity;

import com.jws.pandahealth.base.di.ActivityScope;
import com.jws.pandahealth.base.di.module.ActivityModule;
import com.jws.pandahealth.component.app.base.AppComponent;
import com.jws.pandahealth.component.app.ui.activity.AppGuideActivity;
import com.jws.pandahealth.component.app.ui.activity.AppLoadingActivity;
import com.jws.pandahealth.component.app.ui.activity.CheckEmailActivity;
import com.jws.pandahealth.component.app.ui.activity.ContinueUserMessageActivity;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.app.ui.activity.MainActivity;
import com.jws.pandahealth.component.app.ui.activity.UserFindPwdActivity;
import com.jws.pandahealth.component.app.ui.activity.UserRegActivity;
import com.jws.pandahealth.component.app.ui.activity.WebViewActivity;
import com.jws.pandahealth.component.app.ui.activity.WelcomeActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.DoctorActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.FindDoctorActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.FindDoctorListActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.QuestionNewActivity;
import com.jws.pandahealth.component.askdoctor.view.imagezoom.ImageViewShowActivity;
import com.jws.pandahealth.component.askdoctor.view.photo.ImageGridActivity;
import com.jws.pandahealth.component.askdoctor.view.photo.PhotoActivity;
import com.jws.pandahealth.component.more.ui.activity.NotificationsActivity;
import com.jws.pandahealth.component.more.ui.activity.SettingActivity;
import com.jws.pandahealth.component.more.ui.activity.UpdatePwdActivity;
import com.jws.pandahealth.component.more.ui.activity.UserCenterClipActivity;
import com.jws.pandahealth.component.more.ui.activity.UserInfoActivity;
import com.jws.pandahealth.component.more.ui.activity.UserUpdateInfoActivity;
import com.jws.pandahealth.component.more.ui.activity.UserUpdateRegionActivity;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.ui.activity.ChatDisplayActivity;
import com.jws.pandahealth.component.services.ui.activity.PayActivity;
import com.jws.pandahealth.component.services.ui.activity.PayWebActivity;
import com.jws.pandahealth.component.services.ui.activity.ServicesListActivity;

/**
 * Created by codeest on 16/8/7.
 *
 */

@ActivityScope
@dagger.Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

                void inject(WelcomeActivity welcomeActivity);

    void inject(LoginActivity loginActivity);

    void inject(AppGuideActivity appGuideActivity);

    void inject(UserRegActivity reg);

    void inject(UserFindPwdActivity reg);

    void inject(AppLoadingActivity activity);

    void inject(MainActivity activity);

    void inject(QuestionNewActivity activity);

    void inject(PhotoActivity activity);

    void inject(ImageGridActivity activity);

    void inject(ImageViewShowActivity activity);

    void inject(PayActivity activity);

    void inject(CheckEmailActivity activity);

    void inject(FindDoctorActivity activity);

    void inject(FindDoctorListActivity activity);

    void inject(DoctorActivity activity);

    void inject(ChatActivity activity);

    void inject(PayWebActivity activity);

    void inject(ServicesListActivity activity);

    void inject(UserInfoActivity activity);

    void inject(UserCenterClipActivity activity);

    void inject(UserUpdateInfoActivity activity);

    void inject(UserUpdateRegionActivity activity);

    void inject(ContinueUserMessageActivity activity);
    void inject(ChatDisplayActivity activity);
    void inject(SettingActivity activity);
    void inject(UpdatePwdActivity activity);

    void inject(WebViewActivity activity);
    void inject(NotificationsActivity activity);

//  void inject(MainActivity mainActivity);
//  void inject(ZhihuDetailActivity zhihuDetailActivity);
//
//  void inject(ThemeActivity themeActivity);
//
//  void inject(SectionActivity sectionActivity);

}
