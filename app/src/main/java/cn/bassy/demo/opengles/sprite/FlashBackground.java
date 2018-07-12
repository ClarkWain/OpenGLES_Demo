package cn.bassy.demo.opengles.sprite;

import android.opengl.GLES20;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class FlashBackground {

    private float grey = 0;
    private boolean isReverse = false;

    public void draw() {
        if (!isReverse) {
            grey += 0.01f;
            if (grey > 1f) {
                grey = 1f;
                isReverse = true;
            }
        } else {
            grey -= 0.01f;
            if (grey < 0) {
                grey = 0;
                isReverse = false;
            }
        }

        //specify clear values for the color buffers
        GLES20.glClearColor(grey, grey, grey, 0f);

        //clear buffers to preset values
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }
}
