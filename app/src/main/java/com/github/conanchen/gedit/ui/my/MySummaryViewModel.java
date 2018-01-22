/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.conanchen.gedit.ui.my;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.HelloRepository;
import com.github.conanchen.gedit.room.hello.Hello;
import com.github.conanchen.gedit.util.AbsentLiveData;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MySummaryViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Long> helloTime = new MutableLiveData<>();
    private final LiveData<PagedList<Hello>> helloPagedListLiveData;
    private HelloRepository helloRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public MySummaryViewModel(HelloRepository helloRepository) {
        this.helloRepository = helloRepository;
        helloPagedListLiveData = Transformations.switchMap(helloTime, time -> {
            if (time == null) {
                return AbsentLiveData.create();
            } else {

                return helloRepository.loadHellos(time);
            }
        });

    }

    @VisibleForTesting
    public void setHelloName(String helloName) {
        Observable.just(true).subscribeOn(Schedulers.computation()).observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    helloRepository.sayHello(helloName);
                });
    }

    @VisibleForTesting
    public LiveData<PagedList<Hello>> getHelloPagedListLiveData() {
        return helloPagedListLiveData;
    }

    @VisibleForTesting
    public void reloadHellos() {
        this.helloTime.setValue(System.currentTimeMillis());
    }
}
