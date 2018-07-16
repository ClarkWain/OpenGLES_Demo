package cn.bassy.demo.opengles.sprite;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.util.ShaderHelper;

/**
 * this is a square, no matter how you rotate your device, it will not be squashed.
 *
 * Created on 2018/7/14
 *
 * @author weitianpeng
 */
public class Square extends ISprite{

    private static final String TAG = "Square";
    private static final int BYTE_PER_FLOAT = 4;
    private static final int VERTEX_COMPONENTS = 3; //X, Y, Z
    private static final int COLOR_COMPONENTS = 3; //R, G, B

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

    private FloatBuffer colorBuffer;
    private float colors[] = {
            // R,   G,    B
             1.0f,  1.0f, 1.0f,
             1.0f,  0.0f, 0.0f,
             1.0f,  0.8f, 0.0f,
             0.1f,  0.7f, 0.0f,
             0.0f,  0.1f, 0.7f,
             1.0f,  0.0f, 0.0f
    };

    private float[] projectionMatrix = new float[4*4];

    private static final String vertexShaderCode =
                    "uniform mat4 uMatrix;" +   //add a matrix variable here
                    "attribute vec4 vPosition;" +
                    "attribute vec4 aColor;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "   vColor = aColor;" +
                    "   gl_Position = uMatrix * vPosition;" +
                    "}";

    private static final String fragmentShaderCode =
                    "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int shaderProgram;


    public Square(Context context) {
        super(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(colors.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES20.glCreateProgram(); //create a program object

        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object

        GLES20.glLinkProgram(shaderProgram); //join our shaders together.
    }

    public void onSizeChanged(int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram); //install a program object as part of current rendering state

        int matrixPositionUniform = GLES20.glGetUniformLocation(shaderProgram, "uMatrix");
        GLES20.glUniformMatrix4fv(matrixPositionUniform, 1, false, projectionMatrix, 0);

        int positionAttr = GLES20.glGetAttribLocation(shaderProgram, "vPosition"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        GLES20.glVertexAttribPointer(positionAttr, VERTEX_COMPONENTS, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(positionAttr); //enable a generic vertex attribute array

        int colorAttr = GLES20.glGetAttribLocation(shaderProgram, "aColor");
        GLES20.glVertexAttribPointer(colorAttr, COLOR_COMPONENTS, GLES20.GL_FLOAT, false, 0, colorBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(colorAttr); //enable a generic vertex attribute array

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertices.length / VERTEX_COMPONENTS);
    }

}
