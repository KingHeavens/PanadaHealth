package com.jws.pandahealth.component.askdoctor.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRealmHelper;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.askdoctor.model.bean.QuestionNewInputInfo;
import com.jws.pandahealth.component.askdoctor.presenter.contract.QuestionNewContract;
import com.jws.pandahealth.component.askdoctor.utils.ImageUtils;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.OrderInfo;
import com.jws.pandahealth.component.services.util.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.WeakHashMap;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/21.
 */

public class QuestionNewPresenter extends RxPresenter<QuestionNewContract.View> implements QuestionNewContract.Presenter {
    private static final String TAG = "QuestionNewPresenter";
    private final ServicesRetrofitHelper mServicesRetrofitHelper;
    AskDoctorsRetrofitHelper askDoctorsRetrofitHelper;
    AppRetrofitHelper appRetrofitHelper;
    AppRealmHelper helper;
    AskDoctorsRealmHelper askDoctorsRealmHelper;
    private String mOrderId;

    @Inject
    public QuestionNewPresenter(AppRealmHelper helper,AppRetrofitHelper appRetrofitHelper, AskDoctorsRetrofitHelper askDoctorsRetrofitHelper, AskDoctorsRealmHelper askDoctorsRealmHelper, ServicesRetrofitHelper servicesRetrofitHelper) {
        this.helper=helper;
        this.askDoctorsRetrofitHelper = askDoctorsRetrofitHelper;
        this.askDoctorsRealmHelper=askDoctorsRealmHelper;
        this.appRetrofitHelper=appRetrofitHelper;
        this.mServicesRetrofitHelper = servicesRetrofitHelper;
        initListener();
    }

    private void initListener() {
        Subscription subscribe = RxBusUtil.getDefault().toObservable(ServicesConstant.class)
                .compose(RxUtil.<ServicesConstant>rxSchedulerHelper())
                .subscribe(new Subscriber<ServicesConstant>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(ServicesConstant servicesConstant) {
                        String orderId = servicesConstant.getData(ServicesConstant.PAY_ORDER_ID);
                        if(TextUtils.isEmpty(mOrderId) || !mOrderId.equals(orderId))
                            return;
                        JLog.e(TAG + " pay success");
                        String hxId = servicesConstant.getData(ServicesConstant.PAY_DOCTOR_HXID);
                        if(servicesConstant.getPostType() == ServicesConstant.PAY_SUCCESS_BACK)
                            mView.onPaySuccess(hxId);
                        else if(servicesConstant.getPostType() == ServicesConstant.PAY_FAILED_BACK)
                            mView.onPayFailed();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void upLoadImg(String upFile, final String picPos, final String localPath) {
        File file = new File(upFile);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        Map<String,RequestBody> value=new HashMap<>();
//        value.put("file[]\"; filename=\""+"img"+"",requestBody);
        appRetrofitHelper.upload(requestBody, MyApplication.getHttpDataMap()).compose(RxUtil.<BaseInfo<String>>rxSchedulerHelper()).compose(RxUtil.<String>handleMyResult())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        mView.updateGridView(Integer.parseInt(picPos), localPath,
                                url);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        mView.updateGridView(Integer.parseInt(picPos), localPath,
                                AppConfig.UPLOAD_PIC_FAILE);
                        handlerErrorException(e);

                    }
                });
    }


    @Override
    public void saveUserInput(Context context,ArrayList<ImageItem> items, String input) {
         askDoctorsRealmHelper.saveQuestionNewInputInfo(context,input,items);
    }

    @Override
    public QuestionNewInputInfo getUserInput(Context context){
        QuestionNewInputInfo info=askDoctorsRealmHelper.getQuestionNewInputInfo(context);

        return info;
    }

//    @Override
//    public RealmList<ImageItem> getImageItem() {
//        return askDoctorsRealmHelper.getImageItems();
//    }
//
//    @Override
//    public void saveImageItems(RealmList<ImageItem> items) {
//        askDoctorsRealmHelper.saveImageItems(items);
//    }


    @Override
    public void getOrder(String type) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("type",type);
        Subscription subscribe = mServicesRetrofitHelper.getOrder(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<OrderInfo>>rxSchedulerHelper())
                .compose(RxUtil.<OrderInfo>handleMyResult())
                .subscribe(new Subscriber<OrderInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(OrderInfo orderInfo) {
                        mOrderId = orderInfo.orderId;
                        mView.toPayWebPage(orderInfo.orderId,orderInfo.paypalUrl);
                    }
                });
        addSubscrebe(subscribe);
    }


    @Override
    public void addListener(final Context mContext ,final Bimp bimp) {
        addSubscrebe(RxBusUtil.getDefault().toObservable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if(s.equals("addImageItem")){
                    if (bimp != null && bimp.selectBitmap != null
                            && bimp.selectBitmap.size() == 1) {
                        ArrayList<ImageItem> imageList = bimp.selectBitmap
                                .get(0);
                        if (imageList != null) {
                            for (ImageItem item : imageList) {
                                if (TextUtils.isEmpty(item.upLoadPath)) {
                                    String compressPath = ImageUtils.compressImage(
                                            mContext, item.getImagePath());

                                    if (!TextUtils.isEmpty(compressPath)) {
                                        upLoadImg(compressPath, String.valueOf(0), item.getImagePath());
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }));
    }


    @Override
    public void questionNew(final String token, final String content, final String url) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("token",token);
        httpDataMap.put("content",content);
        httpDataMap.put("arrImgUrl",url);
        Subscription subscribe = askDoctorsRetrofitHelper.questionNew(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        JLog.e(baseInfo.toString());
                        if(baseInfo.getStatus().equals(AppConfig.DATA_SUCCESS)){
                            mView.questionNewSuccess();

                        }else{
                            mView.questionNewerror(token, content,  url);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.questionNewerror(token, content,  url);
                    }
                });

        addSubscrebe(subscribe);
    }

    @Override
    public InitInfo getAskPrice() {
        return helper.askprice();
    }
}
