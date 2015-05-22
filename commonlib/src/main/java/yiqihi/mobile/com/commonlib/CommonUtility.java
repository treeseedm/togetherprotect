package yiqihi.mobile.com.commonlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yiqihi.mobile.com.commonlib.customview.ListViewEmptyListener;
import yiqihi.mobile.com.commonlib.customview.LoadingDialog;
import yiqihi.mobile.com.commonlib.customview.NoResultView;

/**
 * 常用工具类【通用方法】
 */
public class CommonUtility {
    private static final String LOGFOLDER="\\yiqihi\\log";
    private static final String TAG = "CommonUtility";
    private static Thread myStartThread;

    /**
     * 获取对话框
     * 
     * @param context
     * @param title
     *            对话框标题
     * @param message
     *            对话框消息
     * @param leftLabel
     *            对话框左侧按钮文字
     * @param leftListener
     *            对话框左侧按钮监听
     * @param rightLabel
     *            对话框右侧按钮文字
     * @param rightListener
     *            对话框右侧按钮监听
     * @return 对话框
     */
    public static AlertDialog showConfirmDialog(Context context, String title, String message, String leftLabel,
            OnClickListener leftListener, String rightLabel, OnClickListener rightListener) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, leftLabel, leftListener);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, rightLabel, rightListener);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, CharSequence message, String btnLabel,
            OnClickListener btnListener) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, btnLabel, btnListener);
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 废除此接口，显示加载进度框全部改为下面的showLoadingDialog方法
     *
     * @param context
     * @param message
     * @param cancelable
     * @param dismissListener
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable,
            OnDismissListener dismissListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(message);
        progressDialog.setOnDismissListener(dismissListener);
        return progressDialog;
    }

    /**
     * 单选列表对话框
     *
     * @param context
     * @param title
     *            对话框标题
     * @param itemLabels
     *            列表数组
     * @param checkedIndex
     *            默认选择项
     * @param checkListener
     * @param leftLabel
     * @param leftListener
     * @param rightLabel
     * @param rightListener
     * @return
     */
    public static AlertDialog showSingleChioceDialog(Context context, String title, String[] itemLabels,
            int checkedIndex, OnClickListener checkListener, String leftLabel, OnClickListener leftListener,
            String rightLabel, OnClickListener rightListener) {
        AlertDialog alertDialog = new Builder(context).setTitle(title)
                .setSingleChoiceItems(itemLabels, checkedIndex, checkListener).create();
        if (leftLabel != null) {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, leftLabel, leftListener);
        }
        if (rightLabel != null) {
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, rightLabel, rightListener);
        }
        alertDialog.show();
        return alertDialog;
    }
    /**
     * 显示进度框
     *
     * @param context
     * @param message
     * @param cancelable
     * @param cancelListener
     * @return
     */
    public static LoadingDialog showLoadingDialog(Context context, String message, boolean cancelable,
                                                  DialogInterface.OnCancelListener cancelListener) {
        return LoadingDialog.show(context, message, cancelable, cancelListener);
    }



    /**
     * 显示提示
     *
     * @param context
     * @param msg
     *            提示内容
     */
    public static void showToast(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }


    /**
     * 拼凑文字颜色html
     *
     * @param text
     *            要修饰的文字
     * @param colorValue
     *            颜色值
     * @return
     */
    public static String getColorText(String text, String colorValue) {
        StringBuffer sb = new StringBuffer("<font color=\"#");
        return sb.append(colorValue).append("\">").append(text).append("</font>").toString();
    }

    /**
     * 半角转全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if(TextUtils.isEmpty(input)){
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 设置 EditText="" , 清除选中的焦点 隐藏键盘
     *
     * @param context
     * @param editText
     */
    public static void hideSoftKeyboard(Context context, EditText editText) {
        // editText.setText("");
        editText.clearFocus();
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 清除选中的焦点 隐藏键盘 但不清除文字
     *
     * @param context
     * @param editText
     */
    public static void hideSoftKeyboardNotClear(Context context, EditText editText) {
        editText.clearFocus();
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 未使用
     *
     * @param msg
     * @return
     */
    public static String getDigestCode(String msg) {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            digest = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    /**
     * 将文件解码为图片
     *
     * @param filePath
     *            文件路径
     * @param destSize
     * @return
     */
    public static Bitmap decodeBitmap(String filePath, int destSize) {
        if (filePath == null || filePath.length() == 0) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        Bitmap dest = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int bitmapHeight = options.outHeight;
        int bitmapWidth = options.outWidth;
        if (bitmapHeight == -1 || bitmapWidth == -1) {
            return null;
        }
        int widthRate = bitmapWidth / destSize;
        int heightRate = bitmapHeight / destSize;
        float scaleRate = widthRate > heightRate ? heightRate : widthRate;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            options.inDither = false;
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inSampleSize = (int) scaleRate;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            if (bitmap != null) {
                int destWidth = (int) ((float) bitmapWidth / scaleRate);
                int destHieght = (int) ((float) bitmapHeight / scaleRate);
                dest = Bitmap.createScaledBitmap(bitmap, destWidth, destHieght, false);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dest;
    }

    /**
     * 是否开启定位服务
     *
     * @param context
     * @return
     */
    public static boolean isShowOpenLocate(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean NETWORK_status = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (GPS_status || NETWORK_status) {
            return true;
        } else {
            return false;
        }
    }

    // 通过经纬度获取城市
    /**
     * 通过经纬度获取城市 google接口
     *
     * @param lat
     *            纬度
     * @param log
     *            经度
     */
    public static void reverseGeocode(double lat, double log) {
        HttpRequestBase httpRequest = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            httpRequest = new HttpGet("http://maps.google.cn/maps/geo?key=abcdefg&q=" + Double.toString(lat) + ","
                    + Double.toString(log));
            defaultHttpClient = new DefaultHttpClient();
            HttpResponse response = defaultHttpClient.execute(httpRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String charSet = EntityUtils.getContentCharSet(httpEntity);
                if (null == charSet) {
                    charSet = "UTF-8";
                }
                String str = new String(EntityUtils.toByteArray(httpEntity), charSet);
                httpRequest.abort();
                defaultHttpClient.getConnectionManager().shutdown();
                if (!str.equals("")) {
                    JSONObject jsonobject = new JSONObject(str);
                    JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                    for (int i = 0, length = jsonArray.length(); i < length; i++) {
                        JSONObject itemObj = jsonArray.getJSONObject(i);
                        JSONObject addressDetailObj = itemObj.optJSONObject("AddressDetails");
                        JSONObject countryObj = addressDetailObj.optJSONObject("Country");
                        JSONObject AdministrAreaObj = countryObj.optJSONObject("AdministrativeArea");
                        JSONObject localityJsonObj = AdministrAreaObj.optJSONObject("Locality");
                        // 城市名称
                        String LocalityName = localityJsonObj.optString("LocalityName");
                        JSONObject DependentLocalityObj = localityJsonObj.optJSONObject("DependentLocality");
                        // 区名称
                        String DependentLocalityName = DependentLocalityObj.optString("DependentLocalityName");

                    }
                }
            }

        } catch (Exception e) {
            httpRequest.abort();
            defaultHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    /**
     * 通过经纬度获取城市--百度接口
     *
     * @param lat
     *            纬度
     * @param log
     *            经度
     */
    public static void reverseGeocodeBaidu(double lat, double log) {
        HttpRequestBase httpRequest = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            httpRequest = new HttpGet("http://api.map.baidu.com/geocoder?location=" + Double.toString(lat) + ","
                    + Double.toString(log) + "&coord_type=gcj02&output=json");
            defaultHttpClient = new DefaultHttpClient();
            HttpResponse response = defaultHttpClient.execute(httpRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String charSet = EntityUtils.getContentCharSet(httpEntity);
                if (null == charSet) {
                    charSet = "UTF-8";
                }
                String str = new String(EntityUtils.toByteArray(httpEntity), charSet);
                httpRequest.abort();
                defaultHttpClient.getConnectionManager().shutdown();
                if (!str.equals("")) {
                    JSONObject jsonobject = new JSONObject(str);
                    String success = jsonobject.optString("status");
                    if ("OK".equalsIgnoreCase(success)) {
                        JSONObject result = jsonobject.optJSONObject("result");
                        if (result != null) {
                            // 此处为详细地址
                            JSONObject addressComponent = result.optJSONObject("addressComponent");
                            if (addressComponent != null) {
                                String city = addressComponent.optString("city");
                                if (!TextUtils.isEmpty(city)) {
//                                    GlobalConfig.getInstance().cityName=city;
                                }
                                String district = addressComponent.optString("district");
                                if (!TextUtils.isEmpty(district)) {
//                                    GlobalConfig.getInstance().dependentLocalityName=district;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            httpRequest.abort();
            defaultHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }


    /**
     * 加载商品详情网页时把里面的最外层table的宽度替换成100%
     *
     * @param mes
     * @return
     */
    public static String getWidthValue(String mes) {
        String reg1 = "<table\\s*(.*?)\\s*width=\"(.*?)\"\\s+(.*?)>";
        Pattern pat = Pattern.compile(reg1);
        Matcher mat = pat.matcher(mes);
        String str1 = "";
        String str2 = "";
        if (mat.find()) {
            str1 = mat.group();
        }
        Pattern pa = Pattern.compile("width=\"(.*?)\"");
        Matcher m = pa.matcher(str1);
        if (m.find()) {
            str2 = m.group();
        }
        if (str2 != null && str2 != "") {
            str2 = str2.replaceAll("width=", "").replaceAll("\"", "");
        } else {
            str2 = "";
        }
        return str2;
    }
    /**
     * 判断价钱是否为空或者为0
     *
     * @param price
     * @return
     */
    public static boolean isOrNoZero(String price, boolean isSubstring) {
        boolean isOrNo = false;

        if (TextUtils.isEmpty(price)) {
            isOrNo = true;
        } else {
            try {
                String subStringStr = price;
                if (isSubstring) {
                    subStringStr = price.substring(1);
                }
                if (TextUtils.isEmpty(subStringStr)) {
                    isOrNo = true;
                } else {
                    double parseResult = Double.parseDouble(subStringStr);
                    if (0 == parseResult) {
                        isOrNo = true;
                    }
                }

            } catch (Exception e) {
            }
        }
        return isOrNo;
    }

    /**
     * 判断价钱是否为空或者小于0
     *
     * @param price
     * @return
     */
    public static boolean isOrNoLessThanZero(String price, boolean isSubstring) {
        boolean isOrNo = false;

        if (TextUtils.isEmpty(price)) {
            isOrNo = true;
        } else {
            try {
                String subStringStr = price;
                if (isSubstring) {
                    subStringStr = price.substring(1);
                }
                if (TextUtils.isEmpty(subStringStr)) {
                    isOrNo = true;
                } else {
                    double parseResult = Double.parseDouble(subStringStr);
                    if (0 > parseResult) {
                        isOrNo = true;
                    }
                }

            } catch (Exception e) {
            }
        }
        return isOrNo;
    }

    /**
     * 为ListView设置空View显示
     *
     * @param list
     */
    public static void setEmptyViewToListView(Context context, ListView list, int icon) {
        NoResultView emptyView = new NoResultView(context, icon, "", false);
        ((ViewGroup) list.getParent()).addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        list.setEmptyView(emptyView);
    }

    /**
     * 为ListView设置空View显示
     *
     * @param list
     */
    public static void setEmptyViewToListViewSearch(Context context, ListView list, String message) {
        NoResultView emptyView = new NoResultView(context, NoResultView.SOSUO, message, true);
        ((ViewGroup) list.getParent()).addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        list.setEmptyView(emptyView);
    }

    /**
     * 为ListView设置空View根据icon message
     *
     * @param list
     */
    public static void setEmptyViewToListViewSearch(Context context, ListView list, int icon, String message) {
        NoResultView emptyView = new NoResultView(context, icon, message);
        ((ViewGroup) list.getParent()).addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        list.setEmptyView(emptyView);
    }

    /**
     * 为ListView设置空View根据icon message
     *
     * @param list
     */
    public static TextView setEmptyViewToListViewPing(Context context, ListView list, int icon, String message) {
        NoResultView emptyView = new NoResultView(context, icon, message);
        ((ViewGroup) list.getParent()).addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        list.setEmptyView(emptyView);
        return emptyView.tvMessage;
    }

    /**
     * 为ListView设置空View根据icon message
     *
     * @param list
     */
    public static void setEmptyViewToListViewSearchAndListener(Context context, ListView list, int icon,
            String message, final ListViewEmptyListener myCouponListViewIsEmptyListener) {
        NoResultView emptyView = new NoResultView(context, icon, message);
        ((ViewGroup) list.getParent()).addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myCouponListViewIsEmptyListener.HideView();
            }

        });
        list.setEmptyView(emptyView);
    }



    /**
     * 关键代码 执行序列化和反序列化 进行深度拷贝
     *
     * @param src
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy2(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * 深层拷贝对象
     *
     * @param src
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    public static List deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }

    public static double formateFileSize(long fileSize) {
        return (double) fileSize / (1024 * 1024);
    }

    /**
     * 转换文件大小
     *
     * @param fileSize
     * @return
     */
    public static String FormetFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            if (fileSize == 0) {
                fileSizeString = "0.00B";
            } else {
                fileSizeString = df.format((double) fileSize) + "B";
            }
        } else if (fileSize < 1024 * 1024) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) fileSize / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

    /**
     * 将图片截取为圆角图片
     *
     * @param bitmap
     *            原图片片
     * @return 圆角矩形图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } else {
            return null;
        }

    }

    // /**
    // * 过滤英文括号为中文括号
    // * @param goodsName
    // * @return
    // */
    // public static String filterChar(String goodsName) {
    // return TextUtils.isEmpty(goodsName) ? goodsName : goodsName.replace("(", "（").replace(")", "）");
    // }

    /**
     * 去除字符串中的html标签
     *
     * @param HTMLStr
     * @return
     */
    public static String getonerow(String HTMLStr) {
        String htmlStr = HTMLStr;
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>";
            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>";
            String regEx_html = "<[^>]+>";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("");
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("");
            textStr = htmlStr.replaceAll("&nbsp;", "");
            textStr = textStr.replaceAll("<", "<");
            textStr = textStr.replaceAll(">", ">");
            textStr = textStr.replaceAll("®", "®");
            textStr = textStr.replaceAll("&", "&");
        } catch (Exception e) {
            textStr = HTMLStr;
        }
        return textStr;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Activity context) {
        // Rect frame = new Rect();
        // context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // BDebug.d("qius", ""+frame.top);
        // return frame.top;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {

            e1.printStackTrace();
        }

        return sbar;
    }


    /**
     * 内存卡空间
     *
     * @return
     */
    public static long displaySdcardMemory() {
        String state = Environment.getExternalStorageState();
        long availCount = 0;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            availCount = sf.getAvailableBlocks() * blockSize;
        } else {
            File root = Environment.getDataDirectory();
            StatFs sf = new StatFs(root.getPath());
            long blockSize = sf.getBlockSize();
            availCount = sf.getAvailableBlocks() * blockSize;
        }
        return availCount;
    }

    public static boolean createFile(String filepath, boolean recursion) throws IOException {
        boolean result = false;
        File f = new File(filepath);
        if (!f.exists()) {
            try {
                result = f.createNewFile();
            } catch (IOException e) {
                if (!recursion) {
                    throw e;
                }
                File parent = f.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                try {
                    result = f.createNewFile();
                } catch (IOException e1) {
                    throw e1;
                }
            }
        }
        return result;
    }

    /**
     * 判断sdcard是否可用
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取下载文件目录
     *
     * @param context
     * @return
     */
    public static String getAppDownPath(Context context) {
        if (isSdcardExist()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/GomeEShopGame/";
        } else {
            return context.getFilesDir().getAbsolutePath() + "/GomeEShopGame/";
        }

    }

    /**
     *
     * @param context
     * @param appName
     *            文件名称，传带后缀名 xxx.apk
     */
    public static boolean installApp(Context context, String appName) {
        // 获取下载目录
        // File fileDownloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String downDir = getAppDownPath(context);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(downDir, appName);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static Spanned textStyleRed(String txt1, String txt2) {
        if (TextUtils.isEmpty(txt1)) {
            txt1 = "";
        }
        if (TextUtils.isEmpty(txt2)) {
            txt2 = "";
        }
        return Html.fromHtml("<font color=\"#ef3131\">" + txt1 + "&nbsp;</font>"
                + "<font color=\"#999999\">" + txt2 + "</font>");
    }

    /**
     * 设置Listview的高度
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 判断当前系统是否为 message 系统 【比如判断当前系统是否为小米系统】 demo:isOSFromMessage("xiaomi") -- miui系统
     *
     * @param message
     *            系统标识
     * @return
     */
    public static boolean isOSFromMessage(String message) {
        String brand = android.os.Build.BRAND;
        String fingerPrint = android.os.Build.FINGERPRINT;
        String manuFacture = android.os.Build.MANUFACTURER;
        boolean b = false, f = false, m = false;
        try {
            if (!TextUtils.isEmpty(brand) && brand.toLowerCase().contains(message)) {
                b = true;
            }
            if (!TextUtils.isEmpty(fingerPrint) && fingerPrint.toLowerCase().contains(message)) {
                f = true;
            }
            if (!TextUtils.isEmpty(manuFacture) && manuFacture.toLowerCase().contains(message)) {
                m = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b || f || m;
    }


    /**
     * 将返回数据为字符串格式的转化为两位小数z金钱
     *
     * @param moneyString
     * @return
     */
    public static String getMoneyFromString(String moneyString) {
        String money = "0.00";
        try {
            money = new DecimalFormat("#0.00").format(Float.valueOf(moneyString));
        } catch (Exception e) {
        }
        return "￥" + money;
    }

    /**
     * @Description: 将返回数据为字符串格式的转化为两位小数z金钱
     * @author biaofeng.hou
     * @param moneyAmount
     * @return
     * @date 2014-11-24 下午2:17:05
     */
    public static String getMoneyFromInt(int moneyAmount) {
        String money = "0.00";
        if (moneyAmount > 0) {
            try {
                money = new DecimalFormat("#0.00").format(moneyAmount);
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    public static String getMoneyFromInt(double moneyAmount) {
        String money = "0.00";
        if (moneyAmount > 0) {
            try {
                money = new DecimalFormat("#0.00").format(moneyAmount);
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    /**
     * @Description: 将返回数据为字符串格式的转化为两位小数z金钱
     * @author biaofeng.hou
     * @param moneyAmount
     * @return
     * @date 2014-11-24 下午2:17:05
     */
    public static String getMoneyInt(int moneyAmount) {
        String money = "0";
        if (moneyAmount > 0) {
            try {
                money = new DecimalFormat("#0").format(moneyAmount);
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    public static String getMoneyInt(double moneyAmount) {
        String money = "0";
        if (moneyAmount > 0) {
            try {
                money = new DecimalFormat("#0").format(moneyAmount);
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    /**
     * @Description: 将返回数据为字符串格式的转化为金钱
     * @author biaofeng.hou
     * @param moneyAmount
     * @return
     * @date 2014-11-24 下午2:17:05
     */
    public static String getMoneyInt(String moneyAmount) {
        String money = "0";
        if (!TextUtils.isEmpty(moneyAmount) && !moneyAmount.startsWith("-")) {
            try {
                money = new DecimalFormat("#0").format(Float.valueOf(moneyAmount));
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    /**
     * @Description: 将返回数据为字符串格式的转化为金钱，若为空则返回空
     * @author biaofeng.hou
     * @param moneyAmount
     * @return
     * @date 2014-11-25 上午9:46:17
     */
    public static String getMoneyIntEmpty(String moneyAmount) {
        if (TextUtils.isEmpty(moneyAmount)) {
            return "";
        }
        String money = "0";
        if (!moneyAmount.startsWith("-")) {
            try {
                money = new DecimalFormat("#0").format(Float.valueOf(moneyAmount));
            } catch (Exception e) {
            }
        }
        return "￥" + money;
    }

    /**
     * @Description: 获取联系人电话
     * @author biaofeng.hou
     * @param cursor
     * @return
     * @date 2014-11-25 下午12:07:45
     */
    public static ArrayList<String> getContactList(Context context, Cursor cursor) {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        ArrayList<String> allPhoneNum = new ArrayList<String>();
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = phones.getString(index);
                    String phone = phoneNumber.replaceAll(" ", "").replaceAll("-", "");
                    if (phone.length() == 11 && checkPhone(phone)) {
                        allPhoneNum.add(phone);
                    }
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return allPhoneNum;
    }

    /**
     * @Description: 校验手机号码
     * @author biaofeng.hou
     * @param num
     * @return
     * @date 2014-11-25 下午12:08:15
     */
    public static boolean checkPhone(String num) {
        String mobile = num;
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        String exp = "(1)[0-9]{10}$";
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(mobile);
        boolean isPhoneNum = m.matches();
        return isPhoneNum;
    }

    /**
     * 价格数据是否为0
     * 
     * @param money
     * @return
     */
    public static boolean isNotNullOrZero(String money) {
        return !TextUtils.isEmpty(money) && !"0".equalsIgnoreCase(money) && !"0.0".equalsIgnoreCase(money)
                && !"0.00".equalsIgnoreCase(money);
    }

    /**
     * 判断app是否已安装
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            // 获取packagemanager
            PackageManager packageManager = context.getPackageManager();
            // 获取所有已安装程序的包信息
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            // 用于存储所有已安装程序的包名
            List<String> packageNames = new ArrayList<String>();
            // 从pinfo中将包名字逐一取出，压入pName list中
            if (packageInfos != null) {
                for (int i = 0; i < packageInfos.size(); i++) {
                    String packName = packageInfos.get(i).packageName;
                    packageNames.add(packName);
                }
            }
            // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
            return packageNames.contains(packageName);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断服务器返回的boolean字符串是否为true【Y:TRUE--N:FALSE】
     * 
     * @param stringBoolean
     * @return
     */
    public static boolean isTrue(String stringBoolean) {
        boolean flag = false;
        if (!TextUtils.isEmpty(stringBoolean)) {
            flag = "Y".equalsIgnoreCase(stringBoolean) || "true".equalsIgnoreCase(stringBoolean);
        }
        return flag;
    }

    /**
     * 设置intent跳转参数,注意只保留string类型与Serializable类型
     * 
     * 
     * @param intent
     * @param params
     */
    public static void setIntentParams(Intent intent, Map<String, Object> params) {
        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                Object value = params.get(key);
                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (Integer) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (Double) value);
                } else if (value instanceof Long) {
                    intent.putExtra(key, (Long) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (Boolean) value);
                } else {
                    intent.putExtra(key, (Serializable) value);
                }
            }
        }
    }

    public static String getFormateLabel(String format, int width, TextView shopping_cart_has_prom) {
        StringBuilder sb = new StringBuilder();
        Paint paint = shopping_cart_has_prom.getPaint();
        String dot = "          ";
        int firstIndex = 0;
        if(!TextUtils.isEmpty(format)){
            for (int i = 0; i < format.length(); i++) {
                float tempLength = paint.measureText(format, 0, i);
                if(tempLength > width){
                    firstIndex = i - 1;
                    break;
                }
            }
            if(firstIndex == 0){
                sb.append(format);
            }else{
                sb.append(format.substring(0, firstIndex));
                String secString = format.substring(firstIndex, format.length());
                secString = dot + secString;
                firstIndex = 0;
                for (int i = 0; i < secString.length(); i++) {
                    float tempLength = paint.measureText(secString, 0, i);
                    if(tempLength > width){
                        firstIndex = i - 1;
                        break;
                    }
                }
                sb.append("\n");
                if(firstIndex == 0){
                    sb.append(secString);
                }else{
                    sb.append(secString.substring(0, firstIndex));
                    String thString = secString.substring(firstIndex, secString.length());
                    thString = dot + thString;
                    firstIndex = 0;
                    for (int i = 0; i < thString.length(); i++) {
                        float tempLength = paint.measureText(thString, 0, i);
                        if(tempLength > width){
                            firstIndex = i - 1;
                            break;
                        }
                    }
                    sb.append("\n");
                    if(firstIndex == 0){
                        sb.append(thString);
                    }else{
                        sb.append(thString.substring(0, firstIndex-2)+"...");
                    }
                }
            }
            return sb.toString();
        }else{
            return "";
        }
    }

    /**
     * 替换中文字符为英文字符【优化列表加载性能】
     * @param title
     * @return
     */
    public static String getText(String title){
        title = title.replaceAll("（", "(").replaceAll("）", ")").replaceAll("-", "-");
        return title;
    }

    /**
     * 获取底部导航栏高度
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        try {
            Resources resources = context.getResources();
            int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid > 0) {//导航栏展示
                int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = context.getResources().getDimensionPixelSize(resourceId);
                }

            }
        } catch (Exception e) {

        }
        return result;
    }
    public static void showSoft(final EditText editText) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 500);
    }
    public static void writeLog(String content){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        sdDir=new File(sdDir.getPath()+LOGFOLDER,"log"+System.currentTimeMillis()+".txt");
        try{
            FileOutputStream stream=new FileOutputStream(sdDir);
            stream.write(content.getBytes());
            stream.flush();;
            stream.close();
        }catch(Exception e){

        }

    }
//    private void showDateTimePicker(boolean showTimer) {
//        if (showTimer) {
//            if (!isTimer) {
//                Calendar calendar = Calendar.getInstance();
//                final int year = calendar.get(Calendar.YEAR);
//                final int month = calendar.get(Calendar.MONTH);
//                final int day = calendar.get(Calendar.DATE);
//                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                final int minute = calendar.get(Calendar.MINUTE);
//                // 添加大小月月份并将其转换为list,方便之后的判断
//                String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
//                String[] months_little = { "4", "6", "9", "11" };
//
//                final List<String> list_big = Arrays.asList(months_big);
//                final List<String> list_little = Arrays.asList(months_little);
//                // 找到dialog的布局文件
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                View view = inflater.inflate(R.layout.timer_choose_include, null);
//
//                // 年
//                final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
//                wv_year.setAdapter(new NumericWheelAdapter(year, 2100));// 设置"年"的显示数据
//                wv_year.setCyclic(true);// 可循环滚动
//                wv_year.setLabel("年");// 添加文字
//                wv_year.setCurrentItem(0);// 初始化时显示的数据
//                // 月
//                final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
//                wv_month.setAdapter(new NumericWheelAdapter(1, 12));
//                wv_month.setCyclic(true);
//                wv_month.setLabel("月");
//                wv_month.setCurrentItem(month);
//
//                // 日
//                final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
//                wv_day.setCyclic(true);
//                // 判断大小月及是否闰年,用来确定"日"的数据
//                if (list_big.contains(String.valueOf(month + 1))) {
//                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//                } else if (list_little.contains(String.valueOf(month + 1))) {
//                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//                } else {
//                    // 闰年
//                    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//                    else
//                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//                }
//                wv_day.setLabel("日");
//                wv_day.setCurrentItem(day - 1);
//
//                // 时
//                final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
//                wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
//                wv_hours.setCyclic(true);
//                wv_hours.setLabel("时");
//                wv_hours.setCurrentItem(hour);
//
//                // 分
//                final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
//                wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
//                wv_mins.setCyclic(true);
//                wv_mins.setLabel("分");
//                wv_mins.setCurrentItem(minute);
//
//                // 添加"年"监听
//                OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
//                    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                        int year_num = newValue + 2013;
//                        // 判断大小月及是否闰年,用来确定"日"的数据
//                        if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
//                            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//                        } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
//                            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//                        } else {
//                            if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
//                                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//                            else
//                                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//                        }
//                    }
//                };
//                // 添加"月"监听
//                OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
//                    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                        int month_num = newValue + 1;
//                        // 判断大小月及是否闰年,用来确定"日"的数据
//                        if (list_big.contains(String.valueOf(month_num))) {
//                            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//                        } else if (list_little.contains(String.valueOf(month_num))) {
//                            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//                        } else {
//                            if (((wv_year.getCurrentItem() + 2013) % 4 == 0 && (wv_year.getCurrentItem() + 2013) % 100 != 0)
//                                    || (wv_year.getCurrentItem() + 2013) % 400 == 0)
//                                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//                            else
//                                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//                        }
//                    }
//                };
//                wv_year.addChangingListener(wheelListener_year);
//                wv_month.addChangingListener(wheelListener_month);
//
//                // 根据屏幕密度来指定选择器字体的大小
//                int textSize = 0;
//                DisplayMetrics dm = this.getResources().getDisplayMetrics();
//                textSize = (int) (12 * dm.density);
//                // textSize = 12;
//                //
//                wv_day.TEXT_SIZE = textSize;
//                wv_hours.TEXT_SIZE = textSize;
//                wv_mins.TEXT_SIZE = textSize;
//                wv_month.TEXT_SIZE = textSize;
//                wv_year.TEXT_SIZE = textSize;
//
//                Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
//                Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
//                // 确定
//                btn_sure.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        String dateStr = (wv_month.getCurrentItem() + 1) + "/" + (wv_day.getCurrentItem() + 1) + "/"
//                                + (year+wv_year.getCurrentItem()) + " "+ wv_hours.getCurrentItem() + ":"
//                                + wv_mins.getCurrentItem();
//                        Date sendTimeDate = TimeUtils.convertStr2Date(dateStr, TimeUtils.DATE_TIME_FORMAT);
//                        sendTime = sendTimeDate.getTime();
//                        isTimer = true;
//                        String message = String.format(getString(R.string.timer_success_tip),
//                                TimeUtils.toString(sendTimeDate, TimeUtils.DATE_TIME_C_FORMAT));
//                        Utils.showToast(context, message);
//                        TypedArray typedArray = context
//                                .obtainStyledAttributes(new int[] { R.attr.btn_insert_timer_res });
//                        timer_iv.setImageResource(typedArray.getResourceId(0, 0));
//                        bottom_contain_lt.setVisibility(View.GONE);// 隐藏
//                        timer_contain_lt.setVisibility(View.GONE);
//
//                    }
//                });
//                // 取消
//                btn_cancel.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        // 关闭 如果已经设置 那么取消
//                        if (isTimer) {
//                            isTimer = false;
//                            Utils.showToast(context, R.string.timer_cancel);
//                        }
//                        TypedArray typedArray = context.obtainStyledAttributes(new int[] { R.attr.btn_insert_timer });
//                        timer_iv.setImageResource(typedArray.getResourceId(0, 0));
//                        bottom_contain_lt.setVisibility(View.GONE);
//                        timer_contain_lt.setVisibility(View.GONE);
//
//                    }
//                });
//                bottom_contain_lt.setVisibility(View.VISIBLE);
//                hiddenBottomView(bottom_contain_lt);
//                timer_contain_lt.setVisibility(View.VISIBLE);
//                timer_contain_lt.removeAllViews();
//                timer_contain_lt.addView(view);
//            } else {
//                bottom_contain_lt.setVisibility(View.VISIBLE);
//                hiddenBottomView(bottom_contain_lt);
//                timer_contain_lt.setVisibility(View.VISIBLE);
//            }
//
//        } else {
//            bottom_contain_lt.setVisibility(View.GONE);
//            timer_contain_lt.setVisibility(View.GONE);
//        }
//    }
}
