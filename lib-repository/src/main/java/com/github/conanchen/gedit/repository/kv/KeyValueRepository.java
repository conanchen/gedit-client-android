package com.github.conanchen.gedit.repository.kv;

import android.arch.lifecycle.LiveData;

import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Repository that handles VoXYZ objects.
 */
@Singleton
public class KeyValueRepository {

    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public KeyValueRepository(RoomFascade roomFascade,GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<KeyValue> findOne(String key) {
        return roomFascade.daoKeyValue.loadLiveOne(key);
    }

    public Maybe<KeyValue> findMaybe(String key) {
        return roomFascade.daoKeyValue.loadMaybe(key);
    }

    public Observable<Long> save(KeyValue keyValue) {
        return Observable.fromCallable(
                () -> roomFascade.daoKeyValue.save(keyValue)
        ).subscribeOn(Schedulers.io());
    }

}