package com.jws.pandahealth;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class Retrofit
{



////    Retrofit  都做了什么，
//    retrofit先校验是不是接口方法，
// 如果是，遍历api方法，把里面的方法存放到map<method,要访问的接口>中，
//    用Proxy代理把api接口给实现成class，为每一个接口实现的方法设置调用监听(
//            把要访问的接口从map中取出来，得到serviceMethod，
// 看serviceMethod源码发现，通过serviceMethod可以找到serviceMethod的属性，
//            包括 host interfaceName，请求方式（post,get,delete....等） header，conentType等，在把方法的请求参数传到serviceMethod中，这样，
//            serrvicMethod便可以模拟出http请求，返回相关参数
//    ),
//
//
//
//
//
//
//
//    Android Retrofit 2.0
//    /**
//     * 上传一张图片
//     * @param description
//     * @param imgs
//     * @return
//     */
//    @Multipart
//    @POST("/upload")
//    Call<String> uploadImage( @Part("file\"; filename=\"image.png\"")RequestBody imgs);
//
//
//
//
//
//     多张图片的上传
//    /**
//     * 上传三张图片
//     * @param description
//     * @param imgs
//     * @param imgs1
//     * @param imgs3
//     * @return
//     */
//    @Multipart
//    @POST("/upload")
//    Call<String> uploadImage(@Part("fileName") String description,
//                             @Part("file\"; filename=\"ima e1.png\"")RequestBody imgs,
//                             @Part("file\"; filename=\"image2.png\"")RequestBody img
//                             @Part("file\"; filename=\"image3.png\"")RequestBody imgs3);s1,
//
//
//    public static void upload(String path){
//         
//         String descriptionString = "hello, this is description speaking";
//         
//         String[] m = new String[2];
//         m[0]= "share.png";
//         m[1]= "Screenshot_20160128-140709.png";
//         File[] ssssss= new File[2];
//         File file1 = new File("/storage/emulated/0/sc/share.png");
//         File file = new File("/storage/emulated/0/Pictures/ScreenShots/Screenshot_20160128-140709.png");
//         ssssss[0]=file;
//         ssssss[0]=file1;
//         RequestBody requestBody[] = new RequestBody[3];
//         RequestBody requestBody1 =
//           RequestBody.create(MediaType.parse("multipart/form-data"), file);
//         RequestBody requestBody2 =
//           RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//         requestBody[0]=requestBody1;
//         requestBody[1]=requestBody2;
//         Call<String> call = apiManager.uploadImage( m[0],requestBody1,requestBody2,null);
//         call.enqueue(new Callback<String>() {
//              @Override
//              public void onResponse(Response<String> response, Retrofit retrofit) {
//                   Log.v("Upload", response.message());
//                   Log.v("Upload", "success");
//                  }
//             
//                      @Override
//              public void onFailure(Throwable t) {
//                   Log.e("Upload", t.toString());
//                  }
//             });
//         
//    }
//
////-----------------------------------------------------------------
//
//
//    @POST("/fileabout.php")
//    Call<string> upload_3(@Part("filedes") String des,@PartMap Map<string,requestbody> params);
//
//    Map<string,requestbody> params = new HashMap<string, requestbody="">();
//    params.put("file[]\"; filename=\""+file.getName()+"", requestBody);
//    params.put("file[]\"; filename=\""+file2.getName()+"", requestBody2);
//    params.put("file[]\"; filename=\""+file3.getName()+"", requestBody3);
//    Call<string> model = api.upload_3("hello",params);
//
//
//
//
//
//    Retrofit.Builder builder = new Retrofit.Builder()
//            .baseUrl("http://192.168.56.1");
//    File file = new File(Environment.getExternalStorageDirectory() + "/" + "text_img.png");
//    final RequestBody requestBody =
//            RequestBody.create(MediaType.parse("multipart/form-data"),file);
//    uploadfileApi api = builder.addConverterFactory(new ChunkingConverterFactory(requestBody, new ProgressListener() {
//        @Override
//        public void onProgress(long progress, long total, boolean done) {
//            Log.e(TAG, "onProgress: 这是上传的 " + progress + "total ---->"  + total );
//            Log.e(TAG, "onProgress: " + Looper.myLooper());
//        }
//    })).addConverterFactory(GsonConverterFactory.create()).build().create(uploadfileApi.class);
//    Call<string> model = api.upload("hh",requestBody);
//    model.enqueue(new Callback<string>() {
//        @Override
//        public void onResponse(Call<string> call, Response<string> response) {
//
//        }
//
//        @Override
//        public void onFailure(Call<string> call, Throwable t) {
//
//        }
//    });
//}
//
//
//



}
