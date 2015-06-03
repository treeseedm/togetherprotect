package com.myproduct.freetogether;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yiqihi.mobile.com.commonlib.CommonUtility;

/**
 * @author xingchao.song
 * @Description 头像拍照或者从相册选择
 * @date 2014-11-27 下午3:56:30
 */

public class TakePhotoActivity extends Activity implements OnClickListener {

    private static final String TAG = "TakePhotoActivity";


    static final int REQUEST_TAKE_PHOTO = 1;// 去照相
    static final int SELECT_PHOTO = 2; // 去选择照片
    static final int PHOTO_REQUEST_CUT = 3;// 去裁剪照片

    private Button selectFromGallery;
    private Button takePicture;
    private Button takeCancle;
    private File photoFile = null;
    private ImageView img;
    private TextView text;
    public static final int TAKE_PIC_RESULT_CODE = 1001;

    public File croppedFile = null;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.take_pop);
        initParams();
        initView();
        addListener();
    }

    private void initParams() {

    }

    private void initView() {
        takePicture = (Button) this.findViewById(R.id.start_camera);
        selectFromGallery = (Button) this.findViewById(R.id.select_from_gallery);
        takeCancle = (Button) findViewById(R.id.btn_takecancle);
    }

    private void addListener() {
        takePicture.setOnClickListener(this);
        selectFromGallery.setOnClickListener(this);
        takeCancle.setOnClickListener(this);
    }

    private void pickFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == REQUEST_TAKE_PHOTO && mFromPage == 0) { // 来自头像页面 从照相机返回，去剪裁页面
//            cropPicture(Uri.fromFile(photoFile));
//            return;
//        }
        if (requestCode == REQUEST_TAKE_PHOTO) {// 来自评价晒单页面， 从照相机返回，直接回到评价晒单页面
            data = new Intent();
            Uri uri = Uri.fromFile(photoFile);
            data.setData(uri);
            setResult(Activity.RESULT_OK, data);
            this.finish();
            return;
        }
//        if (requestCode == SELECT_PHOTO && mFromPage == 0) {// 来自头像页面 从相册返回，去剪裁页面
//            if (data == null) {
//                CommonUtility.showToast(TakePhotoActivity.this, "你没有选择图片");
//                finish();
//                return;
//            } else {
//                Uri selectedImage = data.getData();
//                cropPicture(selectedImage);
//                return;
//            }
//        }
        if (requestCode == SELECT_PHOTO) {// 来自评价晒单页面， 从相册返回，直接回到评价晒单页面
            setResult(Activity.RESULT_OK, data);

            this.finish();
            return;
        }
//        if (requestCode == PHOTO_REQUEST_CUT) {// 从剪裁页面返回
//            if (data != null) {
//                data.setData(Uri.fromFile(croppedFile));
//            }else{
//                CommonUtility.showToast(this, "无法获取头像");
//                this.finish();
//                return;
//            }
//            dealWithCrop(data);
//            return;
//        }

    }

//    public void dealWithCrop(Intent picdata) {//剪裁之后的图片
//        if (picdata != null) {
//            setResult(TAKE_PIC_RESULT_CODE, picdata);
//        }
//        this.finish();
//
//    }

    // 去剪裁图片
//    public void cropPicture(Uri input) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(input, "image/*");
//        // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true");
//
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", 250);
//        intent.putExtra("outputY", 250);
//        intent.putExtra("return-data", false);
//        intent.putExtra("noFaceDetection", true);
//
//        try {
//            croppedFile = createImageFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(croppedFile));
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }

    public void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                CommonUtility.showToast(this, "create file failed");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.i(TAG, "storageDir:" + storageDir.getPath());
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // / mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_camera:
                startCamera();
                break;
            case R.id.select_from_gallery:
                pickFromGallery();
                break;
            case R.id.btn_takecancle:
                finish();
                break;
            default:
                break;
        }

    }

}
