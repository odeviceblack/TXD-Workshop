package com.gtasatutoymas.txdw.engine;

/* JADX INFO: loaded from: classes.dex */
public class Box extends Objs {
    public Box(float f, float f2, float f3) {
        super(24, 12);
        build(f, f2, f3);
    }

    private void build(float f, float f2, float f3) {
        float f4 = f / 2.0f;
        float f5 = f2 / 2.0f;
        float f6 = f3 / 2.0f;
        int vertex = setVertex(-f4, f5, f6);
        setUV(0.0f, 0.0f);
        setNormals(0, 0, 1);
        int vertex2 = setVertex(f4, f5, f6);
        setUV(1.0f, 0.0f);
        setNormals(0, 0, 1);
        int vertex3 = setVertex(f4, -f5, f6);
        setUV(1.0f, 1.0f);
        setNormals(0, 0, 1);
        int vertex4 = setVertex(-f4, -f5, f6);
        setUV(0.0f, 1.0f);
        setNormals(0, 0, 1);
        setindices((short) vertex, (short) vertex3, (short) vertex2);
        setindices((short) vertex, (short) vertex4, (short) vertex3);
        int vertex5 = setVertex(f4, f5, f6);
        setUV(0.0f, 0.0f);
        setNormals(1, 0, 0);
        int vertex6 = setVertex(f4, f5, -f6);
        setUV(1.0f, 0.0f);
        setNormals(1, 0, 0);
        int vertex7 = setVertex(f4, -f5, -f6);
        setUV(1.0f, 1.0f);
        setNormals(1, 0, 0);
        int vertex8 = setVertex(f4, -f5, f6);
        setUV(0.0f, 1.0f);
        setNormals(1, 0, 0);
        setindices((short) vertex5, (short) vertex7, (short) vertex6);
        setindices((short) vertex5, (short) vertex8, (short) vertex7);
        int vertex9 = setVertex(f4, f5, -f6);
        setUV(0.0f, 0.0f);
        setNormals(0, 0, -1);
        int vertex10 = setVertex(-f4, f5, -f6);
        setUV(1.0f, 0.0f);
        setNormals(0, 0, -1);
        int vertex11 = setVertex(-f4, -f5, -f6);
        setUV(1.0f, 1.0f);
        setNormals(0, 0, -1);
        int vertex12 = setVertex(f4, -f5, -f6);
        setUV(0.0f, 1.0f);
        setNormals(0, 0, -1);
        setindices((short) vertex9, (short) vertex11, (short) vertex10);
        setindices((short) vertex9, (short) vertex12, (short) vertex11);
        int vertex13 = setVertex(-f4, f5, -f6);
        setUV(0.0f, 0.0f);
        setNormals(-1, 0, 0);
        int vertex14 = setVertex(-f4, f5, f6);
        setUV(1.0f, 0.0f);
        setNormals(-1, 0, 0);
        int vertex15 = setVertex(-f4, -f5, f6);
        setUV(1.0f, 1.0f);
        setNormals(-1, 0, 0);
        int vertex16 = setVertex(-f4, -f5, -f6);
        setUV(0.0f, 1.0f);
        setNormals(-1, 0, 0);
        setindices((short) vertex13, (short) vertex15, (short) vertex14);
        setindices((short) vertex13, (short) vertex16, (short) vertex15);
        int vertex17 = setVertex(-f4, f5, -f6);
        setUV(0.0f, 0.0f);
        setNormals(0, 1, 0);
        int vertex18 = setVertex(f4, f5, -f6);
        setUV(1.0f, 0.0f);
        setNormals(0, 1, 0);
        int vertex19 = setVertex(f4, f5, f6);
        setUV(1.0f, 1.0f);
        setNormals(0, 1, 0);
        int vertex20 = setVertex(-f4, f5, f6);
        setUV(0.0f, 1.0f);
        setNormals(0, 1, 0);
        setindices((short) vertex17, (short) vertex19, (short) vertex18);
        setindices((short) vertex17, (short) vertex20, (short) vertex19);
        int vertex21 = setVertex(-f4, -f5, f6);
        setUV(0.0f, 0.0f);
        setNormals(0, -1, 0);
        int vertex22 = setVertex(f4, -f5, f6);
        setUV(1.0f, 0.0f);
        setNormals(0, -1, 0);
        int vertex23 = setVertex(f4, -f5, -f6);
        setUV(1.0f, 1.0f);
        setNormals(0, -1, 0);
        int vertex24 = setVertex(-f4, -f5, -f6);
        setUV(0.0f, 1.0f);
        setNormals(0, -1, 0);
        setindices((short) vertex21, (short) vertex23, (short) vertex22);
        setindices((short) vertex21, (short) vertex24, (short) vertex23);
    }
}
