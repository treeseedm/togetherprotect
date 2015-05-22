package http;

import android.os.AsyncTask;
import android.os.Build;

/**
 * 异步操作工具类
 * 
 * @author qiudongchao
 * 
 */
public class AsyncUtils {

    /**
     * 异步栈统一执行方法【根据不同版本执行不同的方法】
     * 
     * @param task
     *            异步栈
     * @param args
     *            异步栈参数
     */
    public static <T> void execute(AsyncTask<T, ?, ?> task, T... args) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT) {
            throw new UnsupportedOperationException("This class can only be used on API 4 and newer.");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            task.execute(args);
        } else {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        }
    }
}
