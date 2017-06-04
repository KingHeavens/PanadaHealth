package com.jws.pandahealth.component.app.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.presenter.ContinuePresenter;
import com.jws.pandahealth.component.app.presenter.contract.ContinueContract;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.askdoctor.model.bean.UploadImageInfo;
import com.jws.pandahealth.component.askdoctor.utils.ImageUtils;
import com.jws.pandahealth.component.more.ui.activity.UserCenterClipActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/31.
 */

public class ContinueUserMessageActivity extends BaseActivity<ContinuePresenter> implements ContinueContract.View,PermissionListener, View.OnClickListener {

    @BindView(R.id.camrea)
    ImageView camrea;

    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.female)
    TextView female;
    @BindView(R.id.male)
    TextView male;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.skip)
    Button skip;



    /**
     * 相框获取图
     **/
    private static final int PHOTOZOOM = 0;

    /**
     * 拍照
     **/
    private static final int PHOTOTAKE = 1;

    /**
     * 昵称
     **/
    private static final int UPDATE_NICK_NAME = 3;
    /**
     * 截取图片
     **/
    private static final int IMAGE_COMPLETE = 2;

    String upLoadPath;;
    LinearLayout popLayout;
    PopupWindow pop;

    String uploadurl="";
    int gendertype=0;//0 nv  1 nan
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.female:
                female.setSelected(true);
                male.setSelected(false);
                gendertype=0;
                break;
            case R.id.male:
                female.setSelected(false);
                male.setSelected(true);
                gendertype=1;
                break;
            case R.id.submit:
                String name=et_name.getText().toString().trim();
                if(name.length()==0){
                    ToastUtil.show(R.string.placeinputname);
                    return;
                }
                showDialog();
                mPresenter.continueUser(getIntent().getStringExtra("token"),uploadurl,name,gendertype+"");

                break;
            case R.id.skip:
                finish();
                break;
            case R.id.camrea:
                /**
                 * 修改用户头像
                 **/
                /*** 初始化pop ***/
                if (pop == null) {
                    pop = new PopupWindow(this);
                    View popView = getLayoutInflater().inflate(
                            R.layout.view_userinfo_photo_popupwindow, null);
                    popLayout = (LinearLayout) popView.findViewById(R.id.pic_pop_ll);

                    pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                    pop.setBackgroundDrawable(new BitmapDrawable());
                    pop.setFocusable(true);
                    pop.setOutsideTouchable(true);
                    pop.setContentView(popView);

                    RelativeLayout parent = (RelativeLayout) popView
                            .findViewById(R.id.pic_pop_parent);
                    Button bt1 = (Button) popView
                            .findViewById(R.id.pic_pop_camera_btn);
                    Button bt2 = (Button) popView
                            .findViewById(R.id.pic_pop_photo_btn);
                    Button bt3 = (Button) popView
                            .findViewById(R.id.pic_pop_cancel_btn);
                    parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pop.dismiss();
                            popLayout.clearAnimation();
                        }
                    });
                    bt1.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View v) {
                            requestCameraPermission();
                        }
                    });
                    bt2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            requestStorgePermission();
                        }
                    });
                    bt3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            pop.dismiss();
                            popLayout.clearAnimation();
                        }
                    });
                }

                popLayout.startAnimation(AnimationUtils.loadAnimation(mContext,
                        R.anim.activity_translate_in));
                pop.showAtLocation(photo, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_userreg_continueuser;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.perfectinformation));
        female.setSelected(true);
        male.setSelected(false);
        female.setOnClickListener(this);
        male.setOnClickListener(this);
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);
        camrea.setOnClickListener(this);
    }










    /**
     * 申请存储权限
     */
    private void requestStorgePermission() {
        AndPermission.with(this)
                .requestCode(PERMISSION_STORGE)
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .send();
    }
    /**
     * 申请相机权限。
     */
    private void requestCameraPermission() {
        AndPermission.with(this)
                .requestCode(PERMISSION_CAMERA)
                .permission(Manifest.permission.CAMERA)
                .send();
    }


    private void onAlbumBtnClicked() {
        Intent openAlbumIntent = new Intent();
        openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(openAlbumIntent, PHOTOZOOM);
        pop.dismiss();
        popLayout.clearAnimation();
    }
    //========================================================================
    //==============================权限相关操作===============================
    //========================================================================
    private static final int PERMISSION_STORGE = 998;
    private static final int PERMISSION_CAMERA = 999;
    String photoSaveName;
    private void onCameraBtnClicked() {
        photoSaveName = System.currentTimeMillis() + ".png";
        Uri imageUri = Uri.fromFile(new File(AppConfig.SD_CACHE_PATH,
                photoSaveName));
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(
                MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent
                .putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, PHOTOTAKE);
        pop.dismiss();
        popLayout.clearAnimation();
    }

    @Override
    public void onSucceed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
            Toast.makeText(this, "获取相机权限成功", Toast.LENGTH_SHORT).show();
            onCameraBtnClicked();
        } else if (requestCode == PERMISSION_STORGE) {
            Toast.makeText(this, "获取照片、媒体内容和文件权限成功", Toast.LENGTH_SHORT).show();
            onAlbumBtnClicked();
        }
    }

    @Override
    public void onFailed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
            if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("友好提醒")
                        .setMessage("您已拒绝了相机权限，并且下次不再提示，如果你要继续使用此功能，请在设置中为我们授权相机和存储权限。！")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        } else if (requestCode == PERMISSION_STORGE) {
            if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "获取照片、媒体内容和文件权限失败", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("友好提醒")
                        .setMessage("您已拒绝了获取照片、媒体内容和文件权限，并且下次不再提示，如果你要继续使用此功能，请在设置中为我们授权相机和存储权限。！")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 这个Activity中没有Fragment，这句话可以注释。
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 没有Listener，最后的PermissionListener参数不写。
        //AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        // 有Listener，最后需要写PermissionListener参数。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }



     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTOZOOM://获取相册图片
                if (data == null) {
                    return;
                }
                String path = ImageUtils.getPath(mContext, data.getData());
                if (TextUtils.isEmpty(path)) {
                    showToast("请开启手机相册权限");
                    return;
                }
                Intent intent3 = new Intent(this,
                        UserCenterClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;

            case PHOTOTAKE://拍照
                Intent intent2 = new Intent(this,
                        UserCenterClipActivity.class);
                intent2.putExtra("path", AppConfig.SD_CACHE_PATH + photoSaveName);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;

            case IMAGE_COMPLETE://截取图
                upLoadPath = data.getStringExtra("path");
                if (!TextUtils.isEmpty(upLoadPath)) {
                    mPresenter.upload(upLoadPath);
                }
                break;

            default:
                break;
        }

    }



    @Override
    public void uploadSuccess(String url) {
        closeDialog();
        uploadurl=url;
        ImageLoaderUtil.loadContinueImage(this,
                photo, uploadurl);
    }

    @Override
    public void continueSuccess(BaseInfo baseInfo) {
        closeDialog();
        ToastUtil.show(R.string.continuesuccess);
        finish();
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        ToastUtil.shortShow(msg);
    }

    @Override
    public void noHttpError() {
        closeDialog();
        ToastUtil.show(R.string.http_no_net_tip);
    }




}
