package com.jws.pandahealth.component.more.ui.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.LogUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.askdoctor.utils.ImageUtils;
import com.jws.pandahealth.component.more.presenter.UserInfoPresenter;
import com.jws.pandahealth.component.more.presenter.contract.UserInfoContract;
import com.jws.pandahealth.component.more.utils.MoreUtil;
import com.jws.pandahealth.component.more.view.DateSelectorWheelView;
import com.jws.pandahealth.component.services.util.JLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;

import butterknife.BindView;

import static com.jws.pandahealth.component.MyApplication.getCurrentUser;

/**
 * Created by Administrator on 2016/12/27.
 */
public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View, View.OnClickListener, PermissionListener {
    @BindView(R.id.melayout)
    RelativeLayout meLayout;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.namelayout)
    RelativeLayout namelayout;

    @BindView(R.id.tvname)
    TextView nametv;

    @BindView(R.id.genderlayout)
    RelativeLayout genderlayout;

    @BindView(R.id.tvgender)
    TextView gendertv;

    @BindView(R.id.regionlayout)
    RelativeLayout regionlayout;

    @BindView(R.id.tvregion)
    TextView regiontv;

    @BindView(R.id.bornlayout)
    RelativeLayout bornlayout;

    @BindView(R.id.tvborn)
    TextView borntv;

    UserInfo userInfo;
    LinearLayout picPopLayout;
    PopupWindow picPop;

    LinearLayout bornPopLayout;
    PopupWindow bornPop;

    private static final int PHOTOZOOM = 0;//修改头像 - 相框
    private static final int PHOTOTAKE = 1;//修改头像 - 拍照
    private static final int IMAGE_COMPLETE = 2;// 修改头像 - 截取图片
    private static final int UPDATE_NICK_NAME = 3;// 修改昵称
    private static final int UPDATE_GENDER = 4;//  修改性别
    private static final int UPDATE_REGION = 5;// 修改地区
//    private static final int UPDATE_BORN = 6;// 修改生日

    private static final int PERMISSION_STORGE = 0x001;
    private static final int PERMISSION_CAMERA = 0x002;
    String photoSaveName;
    String upLoadPath;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.user_info_title));

        setUserInfo();

        overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.melayout:
                changeUserImg();
                break;

            case R.id.namelayout:
                changeNickname();
                break;

            case R.id.genderlayout:
                changeGender();
                break;

            case R.id.regionlayout:
                changeRegion();
                break;

            case R.id.bornlayout:
                changeBorn();
                break;

            default:
                break;
        }
    }


    /***
     * 修改昵称
     */
    private void changeNickname() {
        Intent it = new Intent(mContext, UserUpdateInfoActivity.class);
        it.putExtra("type", UserUpdateInfoActivity.NAME_TYPE);
        it.putExtra("data", nametv.getText().toString());
        startActivityForResult(it, UPDATE_NICK_NAME);
    }

    /**
     * 修改性别
     */
    private void changeGender() {
        Intent it = new Intent(mContext, UserUpdateInfoActivity.class);
        it.putExtra("type", UserUpdateInfoActivity.GENDER_TYPE);
        it.putExtra("data", userInfo.gender);
        startActivityForResult(it, UPDATE_GENDER);
    }

    /***
     * 修改地区
     */
    private void changeRegion() {
        Intent it = new Intent(mContext, UserUpdateRegionActivity.class);
        it.putExtra("parentId", "1");
        startActivityForResult(it, UPDATE_REGION);
    }

    /***
     * 修改生日
     */
    private void changeBorn() {
        /*** 初始化pop ***/
        if (bornPop == null) {
            bornPop = new PopupWindow(this);
            View popView = getLayoutInflater().inflate(
                    R.layout.view_userinfo_born_popwindow, null);
            bornPopLayout = (LinearLayout) popView.findViewById(R.id.born_pop_ll);
            //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
//            bornPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//            bornPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bornPop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            bornPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            bornPop.setBackgroundDrawable(new BitmapDrawable());
            bornPop.setFocusable(true);
            bornPop.setOutsideTouchable(true);
            bornPop.setContentView(popView);
//            backgroundAlpha(1f);
            final DateSelectorWheelView mDatePicker = (DateSelectorWheelView) popView.findViewById(R.id.date_picker);
            TextView mOkButton = (TextView) popView.findViewById(R.id.ok);
            TextView mCancelButton = (TextView) popView.findViewById(R.id.cancel);
            RelativeLayout parent = (RelativeLayout) popView.findViewById(R.id.born_pop_parent);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bornPop.dismiss();
                    bornPopLayout.clearAnimation();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    bornPop.dismiss();
                    bornPopLayout.clearAnimation();
                }
            });

            mOkButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    JLog.e(mDatePicker.getSelectedDate());
                    mPresenter.updateBorn(mDatePicker.getSelectedDate());
                    setUserInfo();

                    bornPop.dismiss();
                    bornPopLayout.clearAnimation();
                }
            });

            bornPopLayout.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.activity_translate_in));
            bornPop.showAtLocation(photo, Gravity.BOTTOM, 0, 0);
        }

        bornPopLayout.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.activity_translate_in));
        bornPop.showAtLocation(photo, Gravity.BOTTOM, 0, 0);

    }


    /**
     * 修改用户头像
     **/
    private void changeUserImg() {
        /*** 初始化pop ***/
        if (picPop == null) {
            picPop = new PopupWindow(this);
            View popView = getLayoutInflater().inflate(
                    R.layout.view_userinfo_photo_popupwindow, null);
            picPopLayout = (LinearLayout) popView.findViewById(R.id.pic_pop_ll);

            picPop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            picPop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            picPop.setBackgroundDrawable(new BitmapDrawable());
            picPop.setFocusable(true);
            picPop.setOutsideTouchable(true);
            picPop.setContentView(popView);

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
                    picPop.dismiss();
                    picPopLayout.clearAnimation();
                }
            });
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    requestPermission(PERMISSION_CAMERA, Manifest.permission.CAMERA);
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    requestPermission(PERMISSION_STORGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    picPop.dismiss();
                    picPopLayout.clearAnimation();
                }
            });
        }

        picPopLayout.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.activity_translate_in));
        picPop.showAtLocation(photo, Gravity.BOTTOM, 0, 0);
    }

    private void requestPermission(int requestCode, String... permissions) {
        AndPermission.with(this)
                .requestCode(requestCode)
                .permission(permissions)
                .send();
    }


    @Override
    public void onSucceed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
//            showToast(R.string.camer_permission_toast);
            photoSaveName = System.currentTimeMillis() + ".jpg";
            Uri imageUri = Uri.fromFile(new File(AppConfig.SD_CACHE_PATH,
                    photoSaveName));
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.putExtra(
                    MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, PHOTOTAKE);
        } else if (requestCode == PERMISSION_STORGE) {
//            showToast(R.string.storg_permission_toast);
            Intent openAlbumIntent = new Intent();
            openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(openAlbumIntent, PHOTOZOOM);
        }
        picPop.dismiss();
        picPopLayout.clearAnimation();
    }

    @Override
    public void onFailed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
            if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.CAMERA)) {
                showToast("Failed to get camera permissions");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Prompt")
                        .setMessage("Permissions you have rejected the camera, and no longer prompt next time, if you want to continue to use this feature, please for our authorized camera and storage in the set permissions. !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } else if (requestCode == PERMISSION_STORGE) {
            if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("Failed to get photos, media content and file permissions");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Prompt")
                        .setMessage("You have been refused access to photos, media content and file permissions, and no longer prompt next time, if you want to continue to use this feature, please for our authorized camera and storage in the set permissions. !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
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
            case UPDATE_NICK_NAME:// 修改用户昵称
                setUserInfo();
                break;

            case UPDATE_GENDER:// 修改用户性别
                setUserInfo();
                mPresenter.updateGender();
                break;

            case UPDATE_REGION:// 修改地区
                setUserInfo();
                mPresenter.updateRegion();
                break;

            case PHOTOTAKE://拍照
                Intent intent2 = new Intent(this,
                        UserCenterClipActivity.class);
                intent2.putExtra("path", AppConfig.SD_CACHE_PATH + photoSaveName);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;

            case PHOTOZOOM://相册
                if (data == null) {
                    return;
                }
                String path = ImageUtils.getPath(mContext, data.getData());
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Intent intent3 = new Intent(this,
                        UserCenterClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;

            case IMAGE_COMPLETE://截取图
                upLoadPath = data.getStringExtra("path");
                if (!TextUtils.isEmpty(upLoadPath)) {
                    showDialog();
                    mPresenter.upLoadImg(upLoadPath);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void setUserInfo() {
        userInfo = getCurrentUser();
        nametv.setText(userInfo.userName);
        gendertv.setText((AppConfig.SEX_WOMEN_KEY.equals(userInfo.gender) ? getString(R.string.user_info_gender_female) : getString(R.string.user_info_gender_male)));
        regiontv.setText(userInfo.regionName);
        borntv.setText(userInfo.born);
        MoreUtil.loadUserImage(this, photo, userInfo.userIcon);
        RxBusUtil.getDefault().post(AppConfig.USER_UPDATE);
        meLayout.setOnClickListener(this);
        namelayout.setOnClickListener(this);
        genderlayout.setOnClickListener(this);
        regionlayout.setOnClickListener(this);
        bornlayout.setOnClickListener(this);
    }

    @Override
    public void updateUserIconSuccess() {
        RxBusUtil.getDefault().post(AppConfig.USER_UPDATE);
        LogUtil.e("上传头像成功");
    }

    @Override
    public void updateGenderSuccess() {
        LogUtil.e("上传性别成功");
    }

    @Override
    public void updateRegionSuccess() {
        LogUtil.e("上传城市成功");
    }

    @Override
    public void updateBornSuccess() {
        LogUtil.e("上传生日成功");
    }

    @Override
    public void uploadImgSuccess() {
        JLog.e("上传图片成功");
        closeDialog();
        setUserInfo();
        mPresenter.updateUserIcon();

    }


    @Override
    public void noHttpError() {
        closeDialog();
        showToast(getString(R.string.http_no_net_tip));
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        showToast(msg);
    }

}
