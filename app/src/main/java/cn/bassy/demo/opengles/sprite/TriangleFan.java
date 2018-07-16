package cn.bassy.demo.opengles.sprite;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.util.ShaderHelper;

/**
 * four triangle, look like this:
 * ┏━━━━━━┓
 * ┃ *       * ┃
 * ┃   *   *   ┃
 * ┃     *     ┃
 * ┃   *   *   ┃
 * ┃ *       * ┃
 * ┗━━━━━━┛
 *
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class TriangleFan extends ISprite{

    private static final String TAG = "TriangleFan";
    private static final int BYTE_PER_FLOAT = 4;
    private static final int VERTEX_COMPONENTS = 3; //X, Y, Z

    private FloatBuffer vertexBuffer;
    private float vertices[] = {
            // x,   y,    z
             0.0f,  0.0f, 0.0f, //center position
            -0.5f, -0.5f, 0.0f, //first  (0,0) (-0.5, -0.5) ( 0.5, -0.5)
             0.5f, -0.5f, 0.0f, //second (0,0) ( 0.5, -0.5) ( 0.5,  0.5)
             0.5f,  0.5f, 0.0f, //third  (0,0) ( 0.5,  0.5) (-0.5,  0.5)
            -0.5f,  0.5f, 0.0f, //forth  (0,0) (-0.5,  0.5) (-0.5, -0.5)
            -0.5f, -0.5f, 0.0f  //the repeat position
    };

    private static final String vertexShaderCode =
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "   gl_Position = vPosition;" +
                    "}";

    private static final String fragmentShaderCode =
                    "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int shaderProgram;


    public TriangleFan(Context context) {
        super(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES20.glCreateProgram(); //create a program object

        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object

        GLES20.glLinkProgram(shaderProgram); //join our shaders together.
    }

    @Override
    public void onSizeChanged(int width, int height) {

    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram); //install a program object as part of current rendering state

        int positionAttr = GLES20.glGetAttribLocation(shaderProgram, "vPosition"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        GLES20.glVertexAttribPointer(positionAttr, VERTEX_COMPONENTS, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(positionAttr); //enable a generic vertex attribute array

        int colorUniform = GLES20.glGetUniformLocation(shaderProgram, "vColor"); //get the location of a uniform variable, vColor is defined in the fragmentShaderCode
        GLES20.glUniform4f(colorUniform, 0.0f, 0.6f, 1.0f, 1.0f); //specify the value of a uniform variable for the current program object

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertices.length / VERTEX_COMPONENTS);//use GL_TRIANGLE_FAN to draw
    }

}
