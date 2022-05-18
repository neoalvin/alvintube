package com.neoalvin.alvintube.model

import android.opengl.GLES20
import android.opengl.Matrix
import com.neoalvin.alvintube.constants.Constants
import com.neoalvin.alvintube.program.CubeShaderProgram
import java.nio.ByteBuffer


class Cube {
    init {
        vertexArray = VertexArray(CUBE_DATA)
        Matrix.setIdentityM(modelMatrix,0)
    }

    fun bindData(shaderProgram: CubeShaderProgram){
        vertexArray.setVertexAttributePointer(
            shaderProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE,
            0);

        vertexArray.setVertexAttributePointer(
            shaderProgram.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE,
            POSITION_COMPONENT_COUNT
        )
    }

    fun draw() {
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6 * 2 * 3)
        indexArray.position(0)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6 * 2 * 3, GLES20.GL_UNSIGNED_BYTE, indexArray)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val  COLOR_COMPONENT_COUNT = 3
        private val  STRIDE = (POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)* Constants.BYTES_PER_FLOAT

        private lateinit var vertexArray: VertexArray

        var modelMatrix = FloatArray(16)

        private val CUBE_DATA = floatArrayOf(
            // x,   y,    z     R,  G,  B
            -1f, 1f, 1f, 1f, 0f, 0f,  // 0 left top near
            1f, 1f, 1f, 1f, 0f, 1f,  // 1 right top near
            -1f, -1f, 1f, 0f, 0f, 1f,  // 2 left bottom near
            1f, -1f, 1f, 0f, 1f, 0f,  // 3 right bottom near
            -1f, 1f, -1f, 0f, 1f, 0f,  // 4 left top far
            1f, 1f, -1f, 0f, 0f, 1f,  // 5 right top far
            -1f, -1f, -1f, 1f, 0f, 1f,  // 6 left bottom far
            1f, -1f, -1f, 1f, 0f, 0f
        )

        var indexArray: ByteBuffer = ByteBuffer.allocateDirect(6 * 2 * 3).put(
            byteArrayOf( //front
                1, 0, 2,
                1, 2, 3,  //back
                5, 4, 6,
                5, 6, 7,  //left
                4, 0, 2,
                4, 2, 6,  //right
                5, 1, 3,
                5, 3, 7,  //top
                5, 4, 0,
                5, 0, 1,  //bottom
                7, 6, 2,
                7, 2, 3
            )
        )

//        private val CUBE_DATA = floatArrayOf( //x,    y,    z     R, G, B
//            1f, 1f, 1f, 1f, 0f, 1f,  //近平面第一个三角形
//            -1f, 1f, 1f, 1f, 0f, 0f,
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            1f, 1f, 1f, 1f, 0f, 1f,  //近平面第二个三角形
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            1f, -1f, 1f, 0f, 1f, 0f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //远平面第一个三角形
//            -1f, 1f, -1f, 0f, 1f, 0f,
//            -1f, -1f, -1f, 1f, 0f, 1f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //远平面第二个三角形
//            -1f, -1f, -1f, 1f, 0f, 1f,
//            1f, -1f, -1f, 1f, 0f, 0f,
//            -1f, 1f, -1f, 0f, 1f, 0f,  //左平面第一个三角形
//            -1f, 1f, 1f, 1f, 0f, 0f,
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            -1f, 1f, -1f, 0f, 1f, 0f,  //左平面第二个三角形
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            -1f, -1f, -1f, 1f, 0f, 1f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //右平面第一个三角形
//            1f, 1f, 1f, 1f, 0f, 1f,
//            1f, -1f, 1f, 0f, 1f, 0f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //右平面第二个三角形
//            1f, -1f, 1f, 0f, 1f, 0f,
//            1f, -1f, -1f, 1f, 0f, 0f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //上平面第一个三角形
//            -1f, 1f, -1f, 0f, 1f, 0f,
//            -1f, 1f, 1f, 1f, 0f, 0f,
//            1f, 1f, -1f, 0f, 0f, 1f,  //上平面第二个三角形
//            -1f, 1f, 1f, 1f, 0f, 0f,
//            1f, 1f, 1f, 1f, 0f, 1f,
//            1f, -1f, -1f, 1f, 0f, 0f,  //下平面第一个三角形
//            -1f, -1f, -1f, 1f, 0f, 1f,
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            1f, -1f, -1f, 1f, 0f, 0f,  //下平面第二个三角形
//            -1f, -1f, 1f, 0f, 0f, 1f,
//            1f, -1f, 1f, 0f, 1f, 0f
//        )
    }
}