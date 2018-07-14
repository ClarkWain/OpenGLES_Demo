package cn.bassy.demo.opengles.sprite;

import android.graphics.Color;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.util.ErrorChecker;

/**
 * four triangle with blend color, look like this:
 * ┏━━━━━━━━━━━┓
 * ┃ *       * ┃
 * ┃   *   *   ┃
 * ┃     *     ┃
 * ┃   *   *   ┃
 * ┃ *       * ┃
 * ┗━━━━━━━━━━━┛
 *
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class BlendTriangleFan {

    private static final String TAG = "Triangle";

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

    private static final String vertexShaderCode =
                    "attribute vec4 vPosition;\n" +
                    "attribute vec4 aColor;\n" +
                    "varying vec4 vColor;\n" +
                    "void main() {" +
                    "   vColor = aColor;\n" +
                    "   gl_Position = vPosition;\n" +
                    "}\n";

    private static final String fragmentShaderCode =
                    "precision mediump float;\n" +
                    "varying vec4 vColor;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = vColor;\n" +
                    "}\n";

    private int shaderProgram;

    /**
     * Create a Shader
     *
     * @param type       Must be either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderCode GLSL code
     * @return a non-zero value by which it can be referenced
     */
    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type); //create a shader object
        ErrorChecker.check("glCreateShader");

        GLES20.glShaderSource(shader, shaderCode); //replace the source code in a shader object
        ErrorChecker.check("glShaderSource");

        GLES20.glCompileShader(shader); //compile a shader object
        ErrorChecker.check("glCompileShader");

        return shader;
    }

    public BlendTriangleFan() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES20.glCreateProgram(); //create a program object

        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object

        GLES20.glLinkProgram(shaderProgram); //join our shaders together.
    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram); //install a program object as part of current rendering state

        int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(positionAttrib); //enable a generic vertex attribute array

        int colorPositionAttrib = GLES20.glGetAttribLocation(shaderProgram, "aColor");
        GLES20.glVertexAttribPointer(colorPositionAttrib, 3, GLES20.GL_FLOAT, false, 0, colorBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(colorPositionAttrib); //enable a generic vertex attribute array

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }

}
