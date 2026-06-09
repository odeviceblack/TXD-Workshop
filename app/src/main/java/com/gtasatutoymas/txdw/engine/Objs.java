package com.gtasatutoymas.txdw.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class Objs {
    short[] indices;
    ShortBuffer indicesBuffer;
    FloatBuffer normals;
    float[] nors;
    FloatBuffer texCoord;
    float[] uvs;
    float[] vertexs;
    FloatBuffer vertices;
    int offsetV = 0;
    int ofsuv = 0;
    int ofsnor = 0;
    int indexOfs = 0;
    boolean normalsE = false;
    int numElements = 0;
    Vector3 pos = new Vector3(0.0f, 0.0f, 0.0f);
    Vector3 rot = new Vector3(0.0f, 0.0f, 0.0f);
    Vector3 scl = new Vector3(0.0f, 0.0f, 0.0f);

    public Objs(int i, int i2) {
        this.vertexs = new float[i * 3];
        this.uvs = new float[i * 2];
        this.nors = new float[i * 3];
        this.indices = new short[i2 * 3];
    }

    public int setVertex(float f, float f2, float f3) {
        this.vertexs[this.offsetV] = f;
        this.vertexs[this.offsetV + 1] = f2;
        this.vertexs[this.offsetV + 2] = f3;
        this.offsetV += 3;
        this.numElements++;
        return this.numElements - 1;
    }

    public void setindices(short s, short s2, short s3) {
        this.indices[this.indexOfs] = s;
        this.indices[this.indexOfs + 1] = s2;
        this.indices[this.indexOfs + 2] = s3;
        this.indexOfs += 3;
    }

    public void setUV(float f, float f2) {
        this.uvs[this.ofsuv] = f;
        this.uvs[this.ofsuv + 1] = f2;
        this.ofsuv += 2;
    }

    public void setNormals(float f, float f2, float f3) {
        if (!this.normalsE) {
            this.normalsE = true;
        }
        this.nors[this.ofsnor] = f;
        this.nors[this.ofsnor + 1] = f2;
        this.nors[this.ofsnor + 2] = f3;
        this.ofsnor += 3;
    }

    public void ToBuffers() {
        this.vertices = createFloatBuffer(this.vertexs.length);
        this.vertices.put(this.vertexs);
        this.vertices.flip();
        this.indicesBuffer = createShortBuffer(this.indices.length);
        this.indicesBuffer.put(this.indices);
        this.indicesBuffer.flip();
        if (this.normalsE) {
            this.normals = createFloatBuffer(this.nors.length);
            this.normals.put(this.nors);
            this.normals.flip();
        }
        this.texCoord = createFloatBuffer(this.uvs.length);
        this.texCoord.put(this.uvs);
        this.texCoord.flip();
    }

    public boolean hasNormals() {
        return this.normalsE;
    }

    public FloatBuffer toVertexBuffer() {
        return this.vertices;
    }

    public ShortBuffer toIndexBuffer() {
        return this.indicesBuffer;
    }

    public FloatBuffer toNormalsBuffer() {
        return this.normals;
    }

    public FloatBuffer toUVBuffer() {
        return this.texCoord;
    }

    private ShortBuffer createShortBuffer(int i) {
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i * 2);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        return byteBufferAllocateDirect.asShortBuffer();
    }

    private FloatBuffer createFloatBuffer(int i) {
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        return byteBufferAllocateDirect.asFloatBuffer();
    }
}
