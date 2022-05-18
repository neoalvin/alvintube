package com.neoalvin.alvintube.model

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.neoalvin.alvintube.R
import com.neoalvin.alvintube.constants.Constants
import com.neoalvin.alvintube.program.BallShaderProgram
import com.neoalvin.alvintube.utils.TextureHelper


class Ball(context: Context) {
    private val POSITION_COORDIANTE_COMPONENT_COUNT = 3 // 每个顶点的坐标数 x y z

    private val TEXTURE_COORDIANTE_COMPONENT_COUNT = 2 // 每个顶点的坐标数 s t

    private val STRIDE: Int = ((POSITION_COORDIANTE_COMPONENT_COUNT
            + TEXTURE_COORDIANTE_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT)

    private var context = context

    var indexBuffer // 顶点数据缓存对象
            : IndexBuffer? = null
    var vertexBuffer // 索引缓存对象
            : VertexBuffer? = null
    private lateinit var ballShaderProgram : BallShaderProgram // 着色器程序

    var modelMatrix = FloatArray(16) // 模型矩阵

    private var textureId = 0


    init {
        Matrix.setIdentityM(modelMatrix, 0)
        initVertexData()
        buildProgram()
        // setAttributeStatus();
    }

    private var numElements: Int = 0

    private fun initVertexData() {
        val angleSpan = 5 // 将球进行单位切分的角度，此数值越小划分矩形越多，球面越趋近平滑
        val radius = 1.0f // 球体半径
        var offset: Short = 0
        val vertexList: ArrayList<Float> = ArrayList() // 使用list存放顶点数据
        val indexList: ArrayList<Short> = ArrayList() // 顶点索引数组
        var vAngle = 0
        while (vAngle < 180) {
            var hAngle = 0
            while (hAngle <= 360) {

                // st纹理坐标
                val s0 = hAngle / 360.0f //左上角 s
                val t0 = vAngle / 180.0f //左上角 t
                val s1 = (hAngle + angleSpan) / 360.0f //右下角s
                val t1 = (vAngle + angleSpan) / 180.0f //右下角t
                // 左上角 0
                val x0 = (radius * Math.sin(Math.toRadians(vAngle.toDouble())) * Math.cos(
                    Math
                        .toRadians(hAngle.toDouble())
                )).toFloat()
                val y0 = (radius * Math.sin(Math.toRadians(vAngle.toDouble())) * Math.sin(
                    Math
                        .toRadians(hAngle.toDouble())
                )).toFloat()
                val z0 = (radius * Math.cos(Math.toRadians(vAngle.toDouble()))).toFloat()
                vertexList.add(x0)
                vertexList.add(y0)
                vertexList.add(z0)
                vertexList.add(s0)
                vertexList.add(t0)
                // 右上角 1
                val x1 = (radius * Math.sin(Math.toRadians(vAngle.toDouble())) * Math.cos(
                    Math
                        .toRadians((hAngle + angleSpan).toDouble())
                )).toFloat()
                val y1 = (radius * Math.sin(Math.toRadians(vAngle.toDouble())) * Math.sin(
                    Math
                        .toRadians((hAngle + angleSpan).toDouble())
                )).toFloat()
                val z1 = (radius * Math.cos(Math.toRadians(vAngle.toDouble()))).toFloat()
                vertexList.add(x1)
                vertexList.add(y1)
                vertexList.add(z1)
                vertexList.add(s1)
                vertexList.add(t0)
                // 右下角 2
                val x2 = (radius * Math.sin(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                    .cos(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y2 = (radius * Math.sin(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                    .sin(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z2 = (radius * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble()))).toFloat()
                vertexList.add(x2)
                vertexList.add(y2)
                vertexList.add(z2)
                vertexList.add(s1)
                vertexList.add(t1)
                // 左下角 3
                val x3 = (radius * Math.sin(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                    .cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y3 = (radius * Math.sin(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                    .sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z3 = (radius * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble()))).toFloat()
                vertexList.add(x3)
                vertexList.add(y3)
                vertexList.add(z3)
                vertexList.add(s0)
                vertexList.add(t1)
                indexList.add((offset + 0).toShort())
                indexList.add((offset + 3).toShort())
                indexList.add((offset + 2).toShort())
                indexList.add((offset + 0).toShort())
                indexList.add((offset + 2).toShort())
                indexList.add((offset + 1).toShort())
                offset = (offset + 4).toShort() // 4个顶点的偏移
                hAngle = hAngle + angleSpan
            }
            vAngle = vAngle + angleSpan
        }
        numElements = indexList.size // 记录有多少个索引点
        val data_vertex = FloatArray(vertexList.size)
        for (i in 0 until vertexList.size) {
            data_vertex[i] = vertexList[i]
        }
        vertexBuffer = VertexBuffer(data_vertex)
        val data_index = ShortArray(indexList.size)
        for (i in 0 until indexList.size) {
            data_index[i] = indexList[i]
        }
        indexBuffer = IndexBuffer(data_index)
    }

    private fun buildProgram() {
        ballShaderProgram = BallShaderProgram(context)
        ballShaderProgram.userProgram()
    }

    private fun setAttributeStatus() {
        // 每一组完整的顶点数据间隔STRIDE个字节，请注意
        vertexBuffer!!.setVertexAttributePointer(
            ballShaderProgram.aPositionLocation,
            POSITION_COORDIANTE_COMPONENT_COUNT,
            STRIDE, 0
        )
        // 每一组完整的顶点数据由  x y z s t 排列组成，所以纹理数据要先偏移正确的字节数，才能载入。
        vertexBuffer!!.setVertexAttributePointer(
            ballShaderProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDIANTE_COMPONENT_COUNT,
            STRIDE,
            POSITION_COORDIANTE_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT
        )
    }

    fun draw(modelViewProjectionMatrix: FloatArray?) {
        ballShaderProgram.userProgram()
        setAttributeStatus()

        // 将最终变换矩阵写入
        ballShaderProgram.setUniforms(modelViewProjectionMatrix, textureId)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer!!.indexBufferId)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_SHORT, 0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }


    private fun initTexture() {
        textureId = TextureHelper.loadTexture(context, R.mipmap.world)
    }
}