package com.takumi.wms.utils;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtil {

    public static void doInIO(final RxDo d) {
        Observable.just(null).observeOn(Schedulers.io()).subscribe(new Observer() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                d.doSomething();
            }
        });
    }

    public static void doInMain(final RxDo d) {
        Observable.just(null).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                d.doSomething();
            }
        });
    }

    public interface RxDo {
        void doSomething();
    }
}
