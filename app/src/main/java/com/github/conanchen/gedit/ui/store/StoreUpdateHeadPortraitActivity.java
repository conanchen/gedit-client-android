package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.store.StoreUpdateInfo;
import com.github.conanchen.gedit.ui.common.Constant;
import com.github.conanchen.gedit.ui.common.FullyGridLayoutManager;
import com.github.conanchen.gedit.util.ChoosePictureOrVideo;
import com.github.conanchen.gedit.util.PictureUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改头像的界面
 */
@Route(path = "/app/StoreUpdateHeadPortraitActivity")
public class StoreUpdateHeadPortraitActivity extends BaseActivity {
    private String TAG = StoreUpdateActivity.class.getSimpleName();

    @BindView(R.id.updatebutton)
    AppCompatButton updatebutton;
    @BindView(R.id.show)
    AppCompatTextView show;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private static final Gson gson = new Gson();

    private List<LocalMedia> selectMedia = new ArrayList<>();//已选择图片数据
    private List<String> sharePathList = new ArrayList<>();//分享图片未经过的路径
    private List<String> shareCompressList = new ArrayList<>();//分享图片经过压缩的的路径
    private GridImageAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    StoreUpdateHeadPortraitViewModel storeUpdateHeadPortraitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update_head_portrait);
        ButterKnife.bind(this);
        setupViewModel();
        setExhibition();
    }

    private void setupViewModel() {
        storeUpdateHeadPortraitViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateHeadPortraitViewModel.class);
        storeUpdateHeadPortraitViewModel.getStoreUpdateResponseLiveData()
                .observe(this, storeUpdateResponse -> {
                    show.setText("result:" + gson.toJson(storeUpdateResponse));
                });

    }


    @OnClick({R.id.updatebutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updatebutton:
                if (!selectMedia.isEmpty()) {
                    for (int i = 0; i < selectMedia.size(); i++) {
                        String path = selectMedia.get(i).getPath();
                        Log.i(TAG, "picture path :" + path);
                    }
                }
                StoreUpdateInfo storeUpdateInfo = StoreUpdateInfo.builder()
                        .setName(StoreUpdateInfo.Field.LOGO)
                        .setUuid("111")
                        .setValue("http://baidu.com")
                        .build();
//                storeUpdateHeadPortraitViewModel.updateHeadPortrait(storeUpdateInfo);
                break;
        }
    }

    //设置门店展示
    private void setExhibition() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        //删除图片回调接口
        adapter = new GridImageAdapter(this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                ChoosePictureOrVideo.getInstance().ChoosePictureOrVideo(StoreUpdateHeadPortraitActivity.this, PictureMimeType.ofImage(), true, selectMedia);
            }
        });

        //删除选中的图片回调
        adapter.setOnItemDeleteListener(new GridImageAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                if (!selectMedia.isEmpty()) {
                    selectMedia.remove(position);
                }

                if (adapter != null) {
                    adapter.notifyItemRemoved(position);
                }

                if (!shareCompressList.isEmpty()) {
                    shareCompressList.remove(position);
                }

                if (!sharePathList.isEmpty()) {
                    sharePathList.remove(position);
                }
            }
        });

        adapter.setList(selectMedia);
        adapter.setSelectMax(Constant.MAX_SELECT_NUM_NINE);//设置最多显示的张数
        recyclerView.setAdapter(adapter);

        //点击图片预览图片
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectMedia.size() > 0) {
                    LocalMedia media = selectMedia.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(StoreUpdateHeadPortraitActivity.this).externalPicturePreview(position, selectMedia);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(StoreUpdateHeadPortraitActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(StoreUpdateHeadPortraitActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectMedia.clear();
                    selectMedia = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    if (PictureMimeType.ofImage() == Constant.selectType) {
                        //选择图片成功回调
                        adapter.setSelectMax(Constant.MAX_SELECT_NUM_NINE);
                        adapter.notifyDataSetChanged();

                        //清除之前的数据
                        shareCompressList.clear();
                        sharePathList.clear();

                        for (int i = 0; i < selectMedia.size(); i++) {
                            LocalMedia media = selectMedia.get(i);
                            if (media.isCut() && !media.isCompressed()) {
                                // 裁剪过
                                String path = media.getCutPath();
                            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                                final String path = media.getCompressPath();
                                //添加图片地址
                                if (!TextUtils.isEmpty(path)) {
                                    sharePathList.add(path);
                                    try {
                                        String url = PictureUtil.bitmapToPath(path);
                                        shareCompressList.add(url);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        //图片压缩失败  添加原图
                                        shareCompressList.add(path);
                                    }
                                }
                            } else {
                                // 原图地址
                                String path = media.getPath();
                            }
                        }
                        if (selectMedia != null) {
                            adapter.setList(selectMedia);
                            adapter.notifyDataSetChanged();

                            if (!selectMedia.isEmpty()) {
                                for (int i = 0; i < selectMedia.size(); i++) {
                                    String path = selectMedia.get(i).getPath();
                                    Log.i(TAG, "picture path :" + path);
                                }
                            }
                            StoreUpdateInfo storeUpdateInfo = StoreUpdateInfo.builder()
                                    .setName(StoreUpdateInfo.Field.LOGO)
                                    .setUuid("111")
                                    .setValue(selectMedia.get(0).getPath())
                                    .build();
                            storeUpdateHeadPortraitViewModel.updateHeadPortrait(storeUpdateInfo);
                        }
                    }

                    break;
            }

        }
    }

}
