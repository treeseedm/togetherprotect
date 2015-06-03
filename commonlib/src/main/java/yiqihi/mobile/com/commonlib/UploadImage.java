package yiqihi.mobile.com.commonlib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import http.HttpImageUtils;

public class UploadImage {
    public void updateIcon(Intent data,Context context) {
        Bitmap photo = getBitmapFromUri(data.getData(), context);
        if (photo == null) { //
            CommonUtility.showToast(context, "无法获取头像");
            return;
        }
        Bitmap bitmapRound = HttpImageUtils.getRoundedBitmap(photo);
        if (bitmapRound == null) { //
            CommonUtility.showToast(context, "无法获取头像");
            return ;
        } else {
            uploadFile(bitmapRound,context);
        }
    }
    public static void uploadFile(final Bitmap bitmap,Context context) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Log.i("MyAcountActivity", byteArray.length + "");
        BaseTaskForFile task = new BaseTaskForFile(context, false, byteArray) {
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public String getServerUrl() {
//                return Constants.SERVER_URL + "/profile/userImageUploader.jsp";
                return "http://10.144.32.112:8080/Upload/UploadFile";
            }

            @Override
            public void onPost(boolean success, UploadPictureResult result, String errorMessage) {
//                if (success) {
//                    ivLogo.setImageBitmap(bitmap);
//                    ivLogo.invalidate();
//                } else {
//                    CommonUtility.showMiddleToast(MyAcountActivity.this, errorMessage);
//                }
            }
        };
        task.exec();
    }

    public Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
