package cn.bassy.demo.opengles;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.bassy.demo.opengles.sprite.BlendTriangleFan;
import cn.bassy.demo.opengles.sprite.FlashBackground;
import cn.bassy.demo.opengles.sprite.Square;
import cn.bassy.demo.opengles.sprite.Triangle;
import cn.bassy.demo.opengles.sprite.TriangleFan;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
class MainRender implements GLSurfaceView.Renderer {

    FlashBackground background;
    Triangle triangle;
    TriangleFan triangleFan;
    BlendTriangleFan colorfulTriangleFan;
    Square square;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        background = new FlashBackground();
        triangle = new Triangle();
        triangleFan = new TriangleFan();
        colorfulTriangleFan = new BlendTriangleFan();
        square = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        square.onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        background.draw();
        //triangle.draw();
        //triangleFan.draw();
        //colorfulTriangleFan.draw();
        square.draw();
    }
}
