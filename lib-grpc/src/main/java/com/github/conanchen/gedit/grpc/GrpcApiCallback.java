package com.github.conanchen.gedit.grpc;

import com.github.conanchen.gedit.common.grpc.Status;

public interface GrpcApiCallback {
    void onGrpcApiError(Status status);

    void onGrpcApiCompleted();
}
