package com.neoalvin.alvintube.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

class TextureHelper {
    companion object {
        private const val TAG = "TextureHelper";

        fun loadTexture(context: Context, resourceId: Int): Int {
            // 创建纹理对象
            val textureObjectIds = intArrayOf()
            GLES20.glGenTextures(1, textureObjectIds, 0)

            if (textureObjectIds[0] == 0) {
                Log.e(TAG, "Could not generate a new OpenGL texture object!");
                return 0;
            }

            var options = BitmapFactory.Options()
            options.inScaled = false   //指定需要的是原始数据，非压缩数据
            var bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
            if (bitmap == null) {
                Log.e(TAG, "Resource ID " + resourceId + "could not be decode");
                GLES20.glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }

            // 告诉OpenGL后面纹理调用应该是应用于哪个纹理对象
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

            // 设置缩小的时候（GL_TEXTURE_MIN_FILTER）使用mipmap三线程过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
            // 设置放大的时候（GL_TEXTURE_MAG_FILTER）使用双线程过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // 设置位图数据到纹理中
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // 回收位图数据
            bitmap.recycle();

            // 快速生成mipmap贴图
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

            // 解除纹理操作的绑定
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            return textureObjectIds[0];
        }
    }
}