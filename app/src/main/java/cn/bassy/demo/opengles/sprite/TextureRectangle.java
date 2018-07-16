package cn.bassy.demo.opengles.sprite;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.bassy.demo.opengles.R;
import cn.bassy.demo.opengles.util.ShaderHelper;
import cn.bassy.demo.opengles.util.TextureHelper;

/**
 * a rectangle with texture 2d texture.
 *
 * Created on 2018/7/15
 *
 * @author weitianpeng
 */
public class TextureRectangle extends ISprite {

    private static final String TAG = "TextureRectangle";

    private static final int BYTE_PER_FLOAT = 4;
    private static final int VERTEX_COMPONENTS = 4; //X, Y, Z, W
    private static final int TEXTURE_COMPONENTS = 2; //S, T

    private FloatBuffer vertexBuffer;
    private float vertices[] = {
            // x,   y,    z     w     //setup w value, the default is 1.0f, w<1.0 looks far away from perspective center, w>1.0 looks near
             0.0f,  0.0f, 0.0f, 1.0f, //center position
            -0.5f, -0.5f, 0.0f, 0.7f, //first  (0,0) (-0.5, -0.5) ( 0.5, -0.5)
             0.5f, -0.5f, 0.0f, 0.7f, //second (0,0) ( 0.5, -0.5) ( 0.5,  0.5)
             0.5f,  0.5f, 0.0f, 1.0f, //third  (0,0) ( 0.5,  0.5) (-0.5,  0.5)
            -0.5f,  0.5f, 0.0f, 1.0f, //forth  (0,0) (-0.5,  0.5) (-0.5, -0.5)
            -0.5f, -0.5f, 0.0f, 0.7f  //the repeat position
    };

    private FloatBuffer textureBuffer;
    private float texture[] = {
            //tex.S  tex.T
             0.5f, 0.5f, //center position
             0.0f, 1.0f, //first  (0.5f, 0.5f) (0.0f, 1.0f) (1.0f, 1.0f)
             1.0f, 1.0f, //second (0.5f, 0.5f) (1.0f, 1.0f) (1.0f, 0.0f)
             1.0f, 0.0f, //third  (0.5f, 0.5f) (1.0f, 0.0f) (0.0f, 0.0f)
             0.0f, 0.0f, //forth  (0.5f, 0.5f) (0.0f, 0.0f) (0.0f, 1.0f)
             0.0f, 1.0f  //the repeat position
    };

    private static final String textureVertexShaderCode =
                    "uniform mat4 u_Matrix;" +
                    "attribute vec4 a_Position;  " +
                    "attribute vec2 a_TextureCoordinates;" +
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                    "    v_TextureCoordinates = a_TextureCoordinates;" +
                    "    gl_Position = u_Matrix * a_Position;" +
                    "}";

    private static final String textureFragmentShaderCode =
                    "precision mediump float; " +
                    "uniform sampler2D u_TextureUnit;" +
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
                    "}";

    private float[] projectionMatrix = new float[4*4];

    private int shaderProgram;
    private int textureId;

    public TextureRectangle(Context context) {
        super(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(texture.length * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, textureVertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, textureFragmentShaderCode);

        shaderProgram = GLES20.glCreateProgram(); //create a program object

        GLES20.glAttachShader(shaderProgram, vertexShader); //attach vertexShader to a program object
        GLES20.glAttachShader(shaderProgram, fragmentShader);//attach fragmentShader to a program object

        GLES20.glLinkProgram(shaderProgram); //join our shaders together.

        textureId = TextureHelper.loadTexture(context, R.drawable.texture); //load image into texture.
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

        int matrixPositionUniform = GLES20.glGetUniformLocation(shaderProgram, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrixPositionUniform, 1, false, projectionMatrix, 0);

        int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "a_Position"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        GLES20.glVertexAttribPointer(positionAttrib, VERTEX_COMPONENTS, GLES20.GL_FLOAT, false, 0, vertexBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(positionAttrib); //enable a generic vertex attribute array

        int textureAttrib = GLES20.glGetAttribLocation(shaderProgram, "a_TextureCoordinates"); //get the location of an attribute variable, vPosition is defined in the vertexShaderCode
        GLES20.glVertexAttribPointer(textureAttrib, TEXTURE_COMPONENTS, GLES20.GL_FLOAT, false, 0, textureBuffer); //define an array of generic vertex attribute data
        GLES20.glEnableVertexAttribArray(textureAttrib); //enable a generic vertex attribute array


        //When we draw using textures in OpenGL, we don’t pass the texture directly in to the shader.
        //Instead, we use a texture unit to hold the texture.
        //We do this because a GPU can only draw so many textures at the same time. It uses these
        // texture units to represent the active textures currently being drawn.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0); // Set the active texture unit to texture unit 0.

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);// Bind the texture to this unit.

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        int uTextureUnitLocation = GLES20.glGetUniformLocation(shaderProgram, "u_TextureUnit");
        GLES20.glUniform1i(uTextureUnitLocation, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertices.length / VERTEX_COMPONENTS);
    }

}
