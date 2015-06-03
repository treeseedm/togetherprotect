package http;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import yiqihi.mobile.com.commonlib.CommonUtility;

/**
 * 图片加载工具
 * Created by qiudongchao on 2015/3/9.
 */
public class HttpImageUtils {
    private static final String TAG = "HttpImageUtils";
    public static final int TYPE_HTTP_CLIENT = 0;
    public static final int TYPE_URL_CON = 1;
    public static final int TYPE_OK_HTTP = 2;

    /**
     * 默认图片请求方式
     */
    public static int mDefault = TYPE_OK_HTTP;

    /**
     * 从网络下载图片【bitmap】
     * @param url 图片url
     * @return
     */
    public static Bitmap downloadNetworkBitmap(String url) {
        byte[] data = HttpImageUtils.downloadImageFromNetwork(url);
        if (data == null || data.length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;// 允许可清除
        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
        options.inJustDecodeBounds = true;// 获取图片宽高，该属性在decode的时候，仅返回图片的宽高，不生产bitmap对象
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        if (options.outHeight > 800) {
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 从网络下载图片【字节数组】
     * @param url
     * @return
     */
    public static byte[] downloadImageFromNetwork(String url){
        byte[] result = null;
        switch (mDefault){
            case TYPE_HTTP_CLIENT:
                result = downloadImageByHttpClient(url);
                break;
            case TYPE_URL_CON:
                result = downloadImageByURLCon(url);
                break;
            case TYPE_OK_HTTP:
                result = downloadImageByOkHttp(url);
                break;
            default:
                result = downloadImageByOkHttp(url);
                break;
        }
        return result;
    }

    /**
     * 从网络中获取 图片字节码
     *
     * @param url
     *            图片Url
     * @return
     */
    private static byte[] downloadImageByURLCon(String url) {
        InputStream is = null;
        byte[] data = null;
        Log.d(TAG, "downloadImage, url=" + url);
        try {
            URL parsedUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) parsedUrl.openConnection();
            connection.setConnectTimeout(HttpStack.TIMEOUT * 1000);
            connection.setReadTimeout(HttpStack.TIMEOUT * 1000);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                data = transStreamToBytes(is, 4096);
            }

        } catch (Exception e) {
            Log.w(TAG, "downloadImageFromNetwork error, url=" + url + "\nerror message=" + e.toString());
        }
        return data;
    }

    private static byte[] downloadImageByHttpClient(String url) {
        InputStream is = null;
        byte[] data = null;
        Log.d(TAG, "downloadImage, url=" + url);
        try {
            HttpGet httpGet = new HttpGet(url);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, HttpStack.TIMEOUT * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, HttpStack.TIMEOUT * 1000);
            HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpResponse.getEntity().getContent();
                data = transStreamToBytes(is, 4096);
                Log.d(TAG, "downloadImage  rspCode:" + responseCode);
            }
        } catch (Exception e) {
            Log.w(TAG, "downloadImageFromNetwork error, url=" + url + "\nerror message=" + e.toString());
        }
        return data;
    }

    private static byte[] downloadImageByOkHttp(String url) {
        InputStream is = null;
        byte[] data = null;
        Log.d(TAG, "downloadImage, url=" + url);
        try {
            Request request = new Request.Builder().url(url).get().build();
            Response response = OkHttpStack.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                is = response.body().byteStream();
                data = transStreamToBytes(is,4096);
            }
        } catch (Exception e) {
            Log.w(TAG, "downloadImageFromNetwork error, url=" + url + "\nerror message=" + e.toString());
        }
        return data;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 将输入流转化为字节数组
     *
     * @param is
     * @param buffSize
     * @return
     */
    private static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException("buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param localPath
     * @return
     */
    public static boolean bitmapToLocalFile(Bitmap bitmap, String localPath) {
        Log.d("bitmap", "" + bitmap);
        Log.d("localPath", localPath);
        boolean isLoaded = false;
        FileOutputStream fos = null;
        File f = new File(localPath);
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            isLoaded = true;
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isLoaded;
    }

    /**
     * 读取本地图片
     *
     * @param localPath
     * @return
     */
    public static Bitmap getBitMap(String localPath) {

        Log.d("localPath", localPath);
        Bitmap bitmap = null;
        try {
            File file = new File(localPath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(localPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 创建图片文件
     * @return
     * @throws IOException
     */
    public static File createImageFile() throws IOException {
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

    /**
     * 通过URI获取图片物理地址
     * @param _uri
     * @param context
     * @return
     */
    public static String getFilePath(Uri _uri , Context context) {
        String filePath ="";

        Log.d("","URI = "+ _uri);
        if (_uri != null && "content".equals(_uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        }
        else {
            filePath = _uri.getPath();
        }
        return filePath;
    }

    /**
     *将地址中重复的斜杠去掉
     * @param origPath
     * @return
     */
    public  static String fixSlashes(String origPath) {
        // Remove duplicate adjacent slashes.
        boolean lastWasSlash = false;
        char[] newPath = origPath.toCharArray();
        int length = newPath.length;
        int newLength = 0;
        for (int i = 0; i < length; ++i) {
            char ch = newPath[i];
            if (ch == '/') {
                if (!lastWasSlash) {
                    newPath[newLength++] = System.getProperty("file.separator", "/").charAt(0);
                    ;
                    lastWasSlash = true;
                }
            } else {
                newPath[newLength++] = ch;
                lastWasSlash = false;
            }
        }
        // Remove any trailing slash (unless this is the root of the file system).
        if (lastWasSlash && newLength > 1) {
            newLength--;
        }
        // Reuse the original string if possible.
        return (newLength != length) ? new String(newPath, 0, newLength) : origPath;
    }

    /**
     * 压缩图片
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {

        File file = new File(filePath);
        long originalSize = file.length();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize based on a preset ratio
        options.inSampleSize = getInSampleSize(options, 800, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap compressedImage = BitmapFactory.decodeFile(filePath, options);
        if(compressedImage!=null&&compressedImage.getRowBytes()*compressedImage.getHeight()/1024>200){
            compressedImage = compressImage(compressedImage);
        }
        return compressedImage;
    }
    private static int getInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (  width > reqWidth) {
            final int halfWidth = width / 2;
            while (  (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        inSampleSize*= 2;

        while(height/inSampleSize>reqHeight){
            inSampleSize *= 2;
        }
        return inSampleSize;
    }
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 60;
        while (options > 0 && baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
