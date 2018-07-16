package cn.bassy.demo.opengles.util;

import android.opengl.GLES20;

/**
 * Created on 2018/7/16
 *
 * @author weitianpeng
 */
public class ShaderHelper {

    /**
     * Create a Shader
     *
     * @param type       Must be either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderCode GLSL code
     * @return a non-zero value by which it can be referenced
     */
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type); //create a shader object
        ErrorChecker.check("glCreateShader");

        GLES20.glShaderSource(shader, shaderCode); //replace the source code in a shader object
        ErrorChecker.check("glShaderSource");

        GLES20.glCompileShader(shader); //compile a shader object
        ErrorChecker.check("glCompileShader");

        return shader;
    }
}
