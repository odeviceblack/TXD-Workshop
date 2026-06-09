package com.gtasatutoymas.txdw.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class Light {
    FloatBuffer pos = createFloatBuffer(4);
    FloatBuffer direction = createFloatBuffer(3);
    FloatBuffer specular = createFloatBuffer(4);
    FloatBuffer ambient = createFloatBuffer(4);
    FloatBuffer diffuse = createFloatBuffer(4);
    FloatBuffer emissive = createFloatBuffer(4);
    int index = 0;

    public int getIndex() {
        return this.index;
    }

    public void position(float f, float f2, float f3) {
        this.pos.put(f);
        this.pos.put(f2);
        this.pos.put(f3);
        this.pos.put(0.0f);
        this.pos.flip();
    }

    public void direction(float f, float f2, float f3) {
        this.direction.put(f);
        this.direction.put(f2);
        this.direction.put(f3);
        this.direction.flip();
    }

    public void getSpecular(float f, float f2, float f3, float f4) {
        this.specular.put(f / 255.0f);
        this.specular.put(f2 / 255.0f);
        this.specular.put(f3 / 255.0f);
        this.specular.put(f4 / 255.0f);
        this.specular.flip();
    }

    public void getAmbient(float f, float f2, float f3, float f4) {
        this.ambient.put(f / 255.0f);
        this.ambient.put(f2 / 255.0f);
        this.ambient.put(f3 / 255.0f);
        this.ambient.put(f4 / 255.0f);
        this.ambient.flip();
    }

    public void getDiffuse(float f, float f2, float f3, float f4) {
        this.diffuse.put(f / 255.0f);
        this.diffuse.put(f2 / 255.0f);
        this.diffuse.put(f3 / 255.0f);
        this.diffuse.put(f4 / 255.0f);
        this.diffuse.flip();
    }

    public void getEmisive(float f, float f2, float f3, float f4) {
        this.emissive.put(f / 255.0f);
        this.emissive.put(f2 / 255.0f);
        this.emissive.put(f3 / 255.0f);
        this.emissive.put(f4 / 255.0f);
        this.emissive.flip();
    }

    private FloatBuffer createFloatBuffer(int i) {
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        return byteBufferAllocateDirect.asFloatBuffer();
    }
}
