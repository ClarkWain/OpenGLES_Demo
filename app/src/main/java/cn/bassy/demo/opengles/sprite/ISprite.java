package cn.bassy.demo.opengles.sprite;

import android.content.Context;

/**
 * Created on 2018/7/16
 *
 * @author weitianpeng
 */
public abstract class ISprite {

    Context mContext;

    public ISprite(Context context) {
        this.mContext = context;
    }

    public abstract void onSizeChanged(int width, int height);

    public abstract void draw();
}
