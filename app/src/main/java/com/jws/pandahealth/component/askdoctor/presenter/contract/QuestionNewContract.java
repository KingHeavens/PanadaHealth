package com.jws.pandahealth.component.askdoctor.presenter.contract;

import android.content.Context;

import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.askdoctor.model.bean.QuestionNewInputInfo;
import com.jws.pandahealth.component.askdoctor.model.bean.UploadImageInfo;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;
import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface QuestionNewContract {
    interface View extends BaseView{
        void showPop();
        /**
         * 跳转到支付Web页面
         */
        void toPayWebPage(String orderId,String url);
        void questionNewSuccess();
        void questionNewerror(String token,String content,String url);
        /**
         *支付成功
         */
        void onPaySuccess(String hxId);

        /**
         * 支付失败
         */
        void onPayFailed();

        void updateGridView(int picPos, String localPath, String upLoadPath);
    }
    interface Presenter extends BasePresenter<View>{
        void upLoadImg(String upFile, String picPos, String localPath);
       void saveUserInput(Context context,ArrayList<ImageItem> items, String input);
        QuestionNewInputInfo getUserInput(Context context);
//        void saveImageItems(RealmList<ImageItem> items);
//        RealmList<ImageItem> getImageItem();

        void questionNew(String token,String content,String url);

        void getOrder(String type);
        InitInfo getAskPrice();
        void addListener( Context mContext , Bimp bimp);
    }
}
