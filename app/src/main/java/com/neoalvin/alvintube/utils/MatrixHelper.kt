package com.neoalvin.alvintube.utils

import kotlin.math.tan

open class MatrixHelper {
    companion object {
        fun perspectiveM(m : FloatArray, yFovInDegrees: Float, aspect: Float, n: Float, f: Float) {
            var angleInRadians = yFovInDegrees * Math.PI.toFloat() / 180.0f
            var a = 1.0f / tan(angleInRadians / 2.0).toFloat()

            // Android数组是列阵
            m[0] = a / aspect; m[4] = 0f; m[8] = 0f; m[12] = 0f;
            m[1] = 0f; m[5] = a; m[9] = 0f; m[13] = 0f;
            m[2] = 0f; m[6] = 0f; m[10] = -((f + n) / (f - n)); m[14] = -((2f * f * n) / (f - n));
            m[3] = 0f; m[7] = 0f; m[11] = -1f; m[15] = 0f;
        }
    }
}