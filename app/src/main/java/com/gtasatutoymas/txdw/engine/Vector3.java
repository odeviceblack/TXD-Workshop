package com.gtasatutoymas.txdw.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class Vector3 {
    float x;
    float y;
    float z;

    public Vector3(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public FloatBuffer toBuffer() {
        FloatBuffer floatBufferAsFloatBuffer = ByteBuffer.allocateDirect(12).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferAsFloatBuffer.put(this.x);
        floatBufferAsFloatBuffer.put(this.y);
        floatBufferAsFloatBuffer.put(this.z);
        return floatBufferAsFloatBuffer;
    }
}
