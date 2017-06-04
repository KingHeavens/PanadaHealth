package com.jws.pandahealth.component;

import android.os.Environment;

import com.jws.pandahealth.BuildConfig;
import com.jws.pandahealth.base.util.DeviceIdUtil;
import com.jws.pandahealth.base.util.SDCardUtils;
import com.jws.pandahealth.base.util.SystemUtil;

import java.io.File;

/**
 * app 配置信息
 */
public class AppConfig {

    public static final boolean IS_RELEASE = !BuildConfig.DEBUG; // 是否发布:true

    /**
     * 服务器基地址
     */
    public static String getServerPath() {
        if (IS_RELEASE)
            return "https://api.pandahealth.cn";//正式网址
        else
            return "http://api.ph.job.zhumingkai.cn";//测试网址
    }

    public  static final String CHINESEMEDICALURL="https://api.pandahealth.cn/?s=Html/tourism";
    public  static final String TERMSOFUSEURL="https://api.pandahealth.cn/?s=Html/agreement";
    public  static final String PRIVACYURL="https://api.pandahealth.cn/?s=Html/agreement";
    public  static final String ABOUT="https://api.pandahealth.cn/?s=Html/about";
    public  static final String PANDAHEALTH_CHATID="hx_system_1";

    /**
     * 查找医生 类型
     */
    public static final   String[] departmentValue=new String[]{"Obstetrics","Pediatrics","Dermatology","Medicine","Andrology","Surgery","Chinese medicine","Orthopaedics","Psychology","Dental","Ophthalmology","Ear nose throat","Cancer and treatment","Plastic surgery","Report interpretation","Nutrition"};
    public static final   String[] levelValue=new String[]{"Whole","Chief Physician","Associate Chief Physician"};
    public static  final  String[] serviceValue=new String[]{"All Services","Consultation By Graphics And Text","Consultation By Telephone","Private Doctor"};

    /**
     * rxbusutil Filter
     */
    public static final String USER_UPDATE ="com.jws.pandahealth.Userupdate";
    public static final String LOGIN_SUCCESS ="com.jws.pandahealth.loginSuccess";
    public static final String LOGOUT_SUCCESS ="com.jws.pandahealth.logoutSuccess";

    /**
     * 服务器交互参数
     **/
    /**
     * android 平台标志
     **/
    public static final String DEVICE_TYPE = "Android";

    /**
     * android 设备ID
     **/
    public static final String DEVICE_CODE = DeviceIdUtil.getmDeviceID(MyApplication.getInstance());

    /**
     * 加密key
     */
    public static final String PUBLIC_KEY = "w@tedaF2jHzX6nL%b0rP";

    /**
     * 应用包名
     */
    public static final String PACKAGE = MyApplication.getInstance()
            .getPackageName();
    /**
     * 应用版本号
     */
    public static final int VERSION_CODE = SystemUtil.getVerCode(PACKAGE,
            MyApplication.getInstance());
    //================= TYPE ====================
    public static final int TYPE_BASE = 101;

    //================= KEY =====================
    public static final String UPLOAD_PIC_FAILE = "failed"; // 上传图片失败标志

    //================= PATH ====================
    public static final String PATH_DATA = MyApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    /***
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
    * 本地SD卡路径
    **/
    public static String SD_CACHE_PATH = SDCardUtils.getSDCachePath(MyApplication.getInstance()) + File.separator;// sdache 目录
    public static final String LOG_FILE_PATH = SDCardUtils.getSDFilePath(MyApplication.getInstance(), null) + File.separator;// sdfile 目录

    /**
     * 1.0
     **/
    public static final String DATA_FAILED = "000";// 获取数据失败
    public static final String DATA_SUCCESS = "1";// 获取数据成功
    public static final String DATA_EMPTY = "4001";// 数据为空
    public static final String ERROR_TOEKN = "20005";// token过期
    public static final String SEX_MEN_KEY = "1"; // 接口内容 -> 男
    public static final String SEX_WOMEN_KEY = "0"; // 接口内容 -> 女
    public static final String ACTION_USER_SEX_CHANGED = "action_user_sex_changed"; // 用户性别发生改变

    public static final boolean TEST_CHAT = false;//测试聊天
    public static final String TEST_CHAT_ACCOUNT = "hx_account_29";//测试聊天

}
