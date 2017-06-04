package com.jws.pandahealth.component.askdoctor.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.LogUtil;
import com.jws.pandahealth.base.util.SharedPrefUtils;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.askdoctor.model.bean.QuestionNewInputInfo;
import com.jws.pandahealth.component.askdoctor.presenter.QuestionNewPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.QuestionNewContract;
import com.jws.pandahealth.component.askdoctor.utils.StringUtils;
import com.jws.pandahealth.component.askdoctor.view.FixedGridView;
import com.jws.pandahealth.component.askdoctor.view.PayAskDialog;
import com.jws.pandahealth.component.askdoctor.view.photo.ImageGridActivity;
import com.jws.pandahealth.component.askdoctor.view.photo.PhotoActivity;
import com.jws.pandahealth.component.askdoctor.view.photo.PhotoGridAdapter;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.ui.activity.PayWebActivity;
import com.jws.pandahealth.component.services.util.DialogUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class QuestionNewActivity extends BaseActivity<QuestionNewPresenter> implements QuestionNewContract.View, PermissionListener {

    @BindView(R.id.imagegrid)
    FixedGridView imagegrid;
    @BindView(R.id.inputpingwrite)
    EditText inputpingwrite;
    @BindView(R.id.alert)
    TextView alert;
    private static final int MAX_PIC_COUNT = 4; // 最大图片数量
    private static final int SPEAK_SIZE = 500;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int CURRENT_PIC_POS = 0;
    PopupWindow pop;
    Bimp bimp;
    PhotoGridAdapter gridViewAdapter;
    LinearLayout ll_popup;
    private String mOrderId;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.question_new_ac;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.questionnew_title_text),getString(R.string.next));

        inputpingwrite.setFilters(new InputFilter[]{StringUtils.getInputFilter()});
        alert = (TextView) findViewById(R.id.alert);
        alert.setText(0 + "/" + SPEAK_SIZE);
        inputpingwrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                alert.setText(arg0.length() + "/" + SPEAK_SIZE);
                if (arg0.length() > SPEAK_SIZE) {
                    alert.setText(SPEAK_SIZE + "/"
                            + SPEAK_SIZE);
                    inputpingwrite.setText(arg0.toString().substring(0,
                            SPEAK_SIZE));
                    Selection.setSelection(inputpingwrite.getText(), inputpingwrite
                            .getText().length());
                }
            }
        });

        /*** 初始化pop ***/
        inputpingwrite.setFocusable(true);
        inputpingwrite.setFocusableInTouchMode(true);
        inputpingwrite.requestFocus();


/*** initCashHandler 职称图片 ***/
        bimp = Bimp.getInstance();
        mPresenter.addListener(this,bimp);
        bimp.max.add(0);
        QuestionNewInputInfo info = mPresenter.getUserInput(this);
//        JLog.e(info.toString());
//        if (info != null) {
//            for (ImageItem iv : info.items) {
//                iv.setBitmap(ImageUtils.getLoacalBitmap(iv.getImagePath()));
//            }
            String input = info.input;
            inputpingwrite.setText(input);
            alert.setText(input.length() + "/" + SPEAK_SIZE);
        if(info.items!=null && info.items.size()!=0) {
            bimp.selectBitmap.add(0,info.items);
            bimp.tempSelectBitmap.add(0,new ArrayList<ImageItem>());
        } else {
            bimp.selectBitmap.add(new ArrayList<ImageItem>());
            bimp.tempSelectBitmap.add(new ArrayList<ImageItem>());
        }
        gridViewAdapter = new PhotoGridAdapter(mContext, CURRENT_PIC_POS,
                MAX_PIC_COUNT);
        imagegrid.setAdapter(gridViewAdapter);
        // gridViewAdapter1.update();
        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                if (pos == bimp.selectBitmap.get(CURRENT_PIC_POS).size()) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus()
                                    .getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
//                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
//                            mContext, R.anim.activity_translate_in));
                    showPop();

                } else {
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putExtra("pos", CURRENT_PIC_POS);
                    intent.putExtra("ID", pos);
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public void showPop() {
        if (pop == null) {
            pop = new PopupWindow(this);
            View popView = getLayoutInflater().inflate(R.layout.view_userinfo_photo_popupwindow,
                    null);
            ll_popup = (LinearLayout) popView.findViewById(R.id.pic_pop_ll);

            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
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
                    ll_popup.clearAnimation();
                }
            });
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//
                    requestPermission(PERMISSION_CAMERA, Manifest.permission.CAMERA);
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    requestPermission(PERMISSION_STORGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
            });
        }
        pop.showAtLocation(imagegrid, Gravity.BOTTOM, 0, 0);
    }

    private void requestPermission(int requestCode, String... permissions) {
        AndPermission.with(this)
                .requestCode(requestCode)
                .permission(permissions)
                .send();
    }

    private static final int PERMISSION_STORGE = 0x001;
    private static final int PERMISSION_CAMERA = 0x002;

    @Override
    public void onSucceed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
            photo();
        } else if (requestCode == PERMISSION_STORGE) {
            Intent intent = new Intent(mContext, ImageGridActivity.class);
            intent.putExtra("pos", CURRENT_PIC_POS);
            intent.putExtra("maxCount", MAX_PIC_COUNT);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_translate_in,
                    R.anim.activity_translate_out);
        }
    }

    @Override
    public void onFailed(int requestCode) {
        if (requestCode == PERMISSION_CAMERA) {
            if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.CAMERA)) {
                showToast("获取相机权限失败");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("友好提醒")
                        .setMessage("您已拒绝了相机权限，并且下次不再提示，如果你要继续使用此功能，请在设置中为我们授权相机和存储权限。！")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
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
                showToast("获取照片、媒体内容和文件权限失败");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("友好提醒")
                        .setMessage("您已拒绝了获取照片、媒体内容和文件权限，并且下次不再提示，如果你要继续使用此功能，请在设置中为我们授权相机和存储权限。！")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
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


    void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 修改图片上传状态
     **/
    @Override
    public void updateGridView(int picPos, String localPath, String upLoadPath) {
        if (bimp.selectBitmap != null && bimp.selectBitmap.size() != 0) {
            ArrayList<ImageItem> imageList = bimp.selectBitmap.get(picPos);
            if (imageList != null && imageList.size() > 0) {
                for (ImageItem item : imageList) {
                    if (item.getImagePath().equals(localPath)) {
                        item.upLoadPath = upLoadPath;
                    }
                }
            }

            if (gridViewAdapter != null)
                gridViewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        if (gridViewAdapter != null) {
            gridViewAdapter.notifyDataSetChanged();
        }

//        new UpLoadPicTask().execute();

        super.onResume();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.shortShow(msg);
    }

    @Override
    public void noHttpError() {
        LogUtil.e(getString(R.string.http_no_net_tip));
    }


    public void saveAskContent() {
        mPresenter.saveUserInput(this,Bimp.getInstance().selectBitmap.get(CURRENT_PIC_POS), inputpingwrite.getText().toString().trim());
//        RealmList<ImageItem> is=mPresenter.saveUserInput(Bimp.getInstance().selectBitmap.get(CURRENT_PIC_POS), inputpingwrite.getText().toString().trim());
//        bimp.selectBitmap.clear();
//        bimp.tempSelectBitmap.clear();
//        bimp.selectBitmap.add(is);
//        bimp.tempSelectBitmap.add(is);
    }


    public void clickTitleRight(View view) {

        UserInfo u=MyApplication.getCurrentUser();
        if(u==null){
            Intent it=new Intent(this, LoginActivity.class);
            startActivity(it);
            return;
        }
        if(inputpingwrite.getText().toString().trim().equals("")){
            showToast(getString(R.string.pleaseenterthecontent));
            return;
        }

        InitInfo info= mPresenter.getAskPrice();
        if(info!=null) {
            DialogUtil.showPayDialog(this, info.quickAskPrice, info.quickAskTime, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PayAskDialog dialog=(PayAskDialog)v.getTag();
                    dialog.dismiss();

                    showDialog();
                    saveAskContent();
                    mPresenter.getOrder("4");
                }
            });
        }else{
            ToastUtil.shortShow(R.string.server_date_error);
        }



    }


    @Override
    public void toPayWebPage(String orderId, String url) {
        mOrderId = orderId;
        Intent it = new Intent(mContext, PayWebActivity.class);
        it.putExtra(ServicesConstant.PAY_URL, url);
        startActivityForResult(it, ServicesConstant.REQUEST_PAY);
    }

    @Override
    public void onPaySuccess(final String hxId) {
        closeDialog();
        DialogUtil.showNoTitleDialog(this, getString(R.string.pay_success), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付成功操作
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();

                showDialog();
                String url="";
                for(ImageItem ii:bimp.selectBitmap.get(0))
                {
                    if(!TextUtils.isEmpty(ii.upLoadPath)) {
                        url = url + ii.upLoadPath+",";
                    }
                }
                mPresenter.questionNew(MyApplication.getCurrentUser().token,inputpingwrite.getText().toString(),url.equals("")?"":url.substring(0,url.length()-1));

            }
        });
    }

    @Override
    public void onPayFailed() {
        closeDialog();
        DialogUtil.showNoTitleDialog(this, getString(R.string.pay_fail), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_SUCCESS) {
            String orderId = data.getStringExtra(ServicesConstant.PAY_ORDER_ID);
            if (orderId == null || !orderId.equals(mOrderId))
                return;
            onPaySuccess("");
        } else if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_FAILED) {
            String orderId = data.getStringExtra(ServicesConstant.PAY_ORDER_ID);
            if (orderId == null || !orderId.equals(mOrderId))
                return;
            onPayFailed();
        } else if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_CANCEL) {
            DialogUtil.showNoTitleDialog(this, getString(R.string.pay_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = (Dialog) v.getTag();
                    dialog.dismiss();
                }
            });
        }
    }


    @Override
    public void questionNewSuccess() {
        closeDialog();
        ToastUtil.shortShow("timely medical consultation success");
        SharedPrefUtils.saveString(this,"inputQuestionText","");
        SharedPrefUtils.setDataList(this,"inputQuestionImageItems",new ArrayList());
        this.finish();
    }
        int fieldCount=0;
    @Override
    public void questionNewerror(String token, String content, String url) {
        fieldCount++;
        if(fieldCount>=3){
            closeDialog();
            ToastUtil.shortShow(getString(R.string.pay_fail));
        } else{
            mPresenter.questionNew(token,content,url);
        }
    }
}
