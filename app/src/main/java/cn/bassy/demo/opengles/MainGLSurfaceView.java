package cn.bassy.demo.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class MainGLSurfaceView extends GLSurfaceView {
    public MainGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public MainGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }
}
