package cn.bassy.demo.opengles.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class ErrorChecker {

    private static final String TAG = "ErrorChecker";

    public static void check(String prompt) {
        int error = GLES20.glGetError();
        if (error != 0) {
            Log.e(TAG, String.format("checkGLError: find error 0x%s(%d) on %s", Integer.toHexString(error), error, prompt));
        }
    }
}
