package com.jws.pandahealth.base.util;


import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.AppApiException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jws on 2016/8/3.
 */
public class RxUtil {

    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseInfo<T>, T> handleMyResult() {   //compose判断结果
        return new Observable.Transformer<BaseInfo<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseInfo<T>> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<BaseInfo<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseInfo<T> tMyHttpResponse) {
                        LogUtil.e(tMyHttpResponse.toString());
                        if(tMyHttpResponse.getStatus().equals(AppConfig.DATA_SUCCESS)) {
                            return createData(tMyHttpResponse.getObject());
                        } else {
                            AppApiException appApiException = new AppApiException(tMyHttpResponse.getErrmsg());
                            appApiException.setErrorCode(tMyHttpResponse.getErrcode());
                            return Observable.error(appApiException);
                        }
                    }
                });
            }
        };
    }

    /**
     * 返回基本处理
     * @return
     */
    public static  Observable.Transformer<BaseInfo, String> handleBaseResult() {
        return new Observable.Transformer<BaseInfo, String>() {
            @Override
            public Observable<String> call(Observable<BaseInfo> baseInfoObservable) {
                return baseInfoObservable.flatMap(new Func1<BaseInfo, Observable<String>>() {
                    @Override
                    public Observable<String> call(final BaseInfo baseInfo) {
                        LogUtil.e(baseInfo.toString());
                        if(AppConfig.DATA_SUCCESS.equals(baseInfo.getStatus()))
                            return createData(baseInfo.getStatus());
                        else
                            return Observable.error(new AppApiException(baseInfo.getErrmsg(),baseInfo.getErrcode()));
                    }
                });
            }
        };
    }

    /**
     * 生成Observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
