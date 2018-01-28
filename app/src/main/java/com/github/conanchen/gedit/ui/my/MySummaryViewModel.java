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
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payer.activeinapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.repository.HelloRepository;
import com.github.conanchen.gedit.repository.MySummaryRepository;
import com.github.conanchen.gedit.repository.PaymentRepository;
import com.github.conanchen.gedit.user.profile.grpc.UserProfileResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoAccessToken;

import javax.inject.Inject;

public class MySummaryViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<VoAccessToken> mySummaryMutableLiveData = new MutableLiveData<>();
    private final LiveData<UserProfileResponse> mySummaryLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MySummaryViewModel(MySummaryRepository mySummaryRepository) {
        mySummaryLiveData = Transformations.switchMap(mySummaryMutableLiveData, voAccessToken -> {
            if (voAccessToken == null) {
                return AbsentLiveData.create();
            } else {
                return mySummaryRepository.userProfile(voAccessToken);
            }
        });
    }

    @VisibleForTesting
    public LiveData<UserProfileResponse> getMySummaryLiveData() {
        return mySummaryLiveData;
    }

    public void userProfile(VoAccessToken voAccessToken) {
        mySummaryMutableLiveData.setValue(voAccessToken);
    }
}
