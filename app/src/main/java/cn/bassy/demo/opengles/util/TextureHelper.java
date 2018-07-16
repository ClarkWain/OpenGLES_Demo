package cn.bassy.demo.opengles.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;
import android.util.Log;

public class TextureHelper {

    private static final String TAG = "TextureHelper";

    /**
     * load a bitmap into texture
     * @param context context
     * @param drawableId res id
     * @return return the texture id, return 0 if failed.
     */
    public static int loadTexture(Context context, @DrawableRes int drawableId){
        int[] textures = new int[1];
        GLES20.glGenTextures(textures.length, textures, 0); //generate on texture

        if(textures[0] == 0){
            ErrorChecker.check("loadTexture failed.");
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // get raw data.
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, options);

        if(bitmap == null){
            GLES20.glDeleteTextures(textures.length, textures, 0);
            Log.e(TAG, "load bitmap failed");
            return 0;
        }

        //we Need to tell OpenGL that future texture calls should be applied to this texture object.
        //The first parameter,  GL_TEXTURE_2D , tells OpenGL that this should be treated
        //as a two-dimensional texture, and the second parameter tells OpenGL which
        //texture object ID to bind to.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);


        //We set each filter with a call to  glTexParameteri() :  GL_TEXTURE_MIN_FILTER refers to
        //minification, while  GL_TEXTURE_MAG_FILTER refers to magnification. For minifica-
        //tion, we select  GL_LINEAR_MIPMAP_LINEAR , which tells OpenGL to use trilinear
        //filtering. We set the magnification filter to  GL_LINEAR , which tells OpenGL to
        //use bilinear filtering.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);


        //now load the bitmap data into OpenGL
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        //release this bitmap data immediately
        bitmap.recycle();

        //tell OpenGL to generate all of the necessary levels
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        //Now that we’ve finished loading the texture, a good practice is to then unbind from the texture
        //Passing 0 to  glBindTexture() unbinds from the current texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textures[0];
    }

}
