package cn.bassy.demo.opengles.sprite;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.util.ErrorChecker;

/**
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class Triangle {

    private static final String TAG = "Triangle";

    private FloatBuffer vertexBuffer;
    private float vertices[] = {
             0.0f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f
    };

    private float color[] = {0.0f, 0.6f, 1.0f, 1.0f};

    private final String vertexShaderCode =
                    "attribute vec4 vPosition;\n" +
                    "void main() {" +
                    "   gl_Position = vPosition;\n" +
                    "}\n";

    private final String fragmentShaderCode =
                    "precision mediump float;\n" +
                    "uniform vec4 vColor;\n" +
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

    public Triangle() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        ErrorChecker.check("loadVertexShader ");

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        ErrorChecker.check("loadFragmentShader ");

        shaderProgram = GLES20.glCreateProgram(); //create a program object
        ErrorChecker.check("glCreateProgram");

        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        ErrorChecker.check("glAttachVertexShader");

        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object
        ErrorChecker.check("glAttachFragmentShader");

        GLES20.glLinkProgram(shaderProgram); //link a program object
        ErrorChecker.check("glLinkProgram");
    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram); //install a program object as part of current rendering state
        ErrorChecker.check("glUseProgram");

        int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        ErrorChecker.check("glGetAttribLocation");

        GLES20.glEnableVertexAttribArray(positionAttrib); //enable a generic vertex attribute array
        ErrorChecker.check("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        ErrorChecker.check("glVertexAttribPointer");

        int colorUniform = GLES20.glGetUniformLocation(shaderProgram, "vColor"); //get the location of a uniform variable, vColor is defined in the fragmentShaderCode
        ErrorChecker.check("glGetUniformLocation");

        GLES20.glUniform4fv(colorUniform, 1, color, 0); //specify the value of a uniform variable for the current program object
        ErrorChecker.check("glUniform4fv");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);
        ErrorChecker.check("glDrawArrays");
    }

}
