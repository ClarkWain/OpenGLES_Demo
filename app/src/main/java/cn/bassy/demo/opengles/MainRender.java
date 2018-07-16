package cn.bassy.demo.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.bassy.demo.opengles.sprite.BlendTriangleFan;
import cn.bassy.demo.opengles.sprite.FlashBackground;
import cn.bassy.demo.opengles.sprite.ISprite;
import cn.bassy.demo.opengles.sprite.Square;
import cn.bassy.demo.opengles.sprite.TextureRectangle;
import cn.bassy.demo.opengles.sprite.Triangle;
import cn.bassy.demo.opengles.sprite.TriangleFan;
import cn.bassy.demo.opengles.sprite.TriangleFan3D;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
class MainRender implements GLSurfaceView.Renderer {

    private ISprite background;
    private ISprite triangle;
    private ISprite triangleFan;
    private ISprite blendTriangleFan;
    private ISprite square;
    private ISprite triangleFan3D;
    private ISprite textureRectangle;

    private Context context;

    public MainRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        background = new FlashBackground(context);
        triangle = new Triangle(context);
        triangleFan = new TriangleFan(context);
        blendTriangleFan = new BlendTriangleFan(context);
        square = new Square(context);
        triangleFan3D = new TriangleFan3D(context);
        textureRectangle = new TextureRectangle(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        background.onSizeChanged(width, height);
        triangle.onSizeChanged(width, height);
        triangleFan.onSizeChanged(width, height);
        blendTriangleFan.onSizeChanged(width, height);
        square.onSizeChanged(width, height);
        triangleFan3D.onSizeChanged(width, height);
        textureRectangle.onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        background.draw();
        triangle.draw();
//        triangleFan.draw();
//        blendTriangleFan.draw();
//        square.draw();
//        triangleFan3D.draw();
//        textureRectangle.draw();
    }
}
