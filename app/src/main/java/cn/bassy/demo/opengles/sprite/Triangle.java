package cn.bassy.demo.opengles.sprite;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.util.ErrorChecker;
import cn.bassy.demo.opengles.util.ShaderHelper;

/**
 * A simple triangle.
 *
 * Created on 2018/7/12
 *
 * @author weitianpeng
 */
public class Triangle extends ISprite{

    private static final String TAG = "Triangle";
    private static final int BYTE_PER_FLOAT = 4;
    private static final int VERTEX_COMPONENT = 3; //X, Y, Z

    private FloatBuffer vertexBuffer;
    private float vertices[] = {
            // x  ,   y,   z
             0.0f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f
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

    public Triangle(Context context) {
        super(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //A vertex shader generates the final position of each vertex and is run once
        //per vertex. Once the final positions are known, OpenGL will take the visible
        //set of vertices and assemble them into points, lines, and triangles.
        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        ErrorChecker.check("loadVertexShader ");

        //A fragment shader generates the final color of each fragment of a point,
        //line, or triangle and is run once per fragment. A fragment is a small,
        //rectangular area of a single color, analogous to a pixel on a computer screen.
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        ErrorChecker.check("loadFragmentShader ");

        shaderProgram = GLES20.glCreateProgram(); //create a program object
        ErrorChecker.check("glCreateProgram");

        //An OpenGL program is simply one vertex shader and one fragment shader
        //linked together into a single object. Vertex shaders and fragment shaders
        //always go together.
        //Without a fragment shader, OpenGL wouldn’t know how
        //to draw the fragments that make up each point, line, and triangle; and without
        //a vertex shader, OpenGL wouldn’t know where to draw these fragments.
        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        ErrorChecker.check("glAttachVertexShader");

        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object
        ErrorChecker.check("glAttachFragmentShader");

        GLES20.glLinkProgram(shaderProgram); //join our shaders together.
        ErrorChecker.check("glLinkProgram");
    }

    @Override
    public void onSizeChanged(int width, int height) {

    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram); //install a program object as part of current rendering state
        ErrorChecker.check("glUseProgram");

        //Each variable of shader has a location, and OpenGL works with these
        //locations rather than with the name of the variable directly.
        //So we need to retrieve vertex position and color position.
        int positionAttr = GLES20.glGetAttribLocation(shaderProgram, "vPosition"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        ErrorChecker.check("glGetAttribLocation");

        //tell OpenGL that it can find the data for vPosition in the buffer vertexBuffer
        GLES20.glVertexAttribPointer(positionAttr, VERTEX_COMPONENT, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        ErrorChecker.check("glVertexAttribPointer");

        //enable the attribute
        GLES20.glEnableVertexAttribArray(positionAttr); //enable a generic vertex attribute array
        ErrorChecker.check("glEnableVertexAttribArray");

        int colorUniform = GLES20.glGetUniformLocation(shaderProgram, "vColor"); //get the location of a uniform variable, vColor is defined in the fragmentShaderCode
        ErrorChecker.check("glGetUniformLocation");

        // glUniform4f or glUniform4fv can be used to load a uniform variable array of type vec4
        GLES20.glUniform4f(colorUniform, 0.0f, 0.6f, 1.0f, 1.0f); //specify the value of a uniform variable for the current program object
        ErrorChecker.check("glUniform4f");

        //finally draw it to screen
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / VERTEX_COMPONENT);
        ErrorChecker.check("glDrawArrays");
    }

}
