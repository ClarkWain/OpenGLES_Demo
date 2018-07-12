package cn.bassy.demo.opengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.bassy.demo.opengles.sprite.FlashBackground;
import cn.bassy.demo.opengles.sprite.Triangle;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
class MainRender implements GLSurfaceView.Renderer {

    FlashBackground background;
    Triangle triangle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        background = new FlashBackground();
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        background.draw();
        triangle.draw();
    }
}
