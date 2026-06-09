package com.gtasatutoymas.txdw.engine;

/* JADX INFO: loaded from: classes.dex */
public class Camera {
    public Vector3 pos;
    public Vector3 target = new Vector3(0.0f, 0.0f, 0.0f);
    public Vector3 upAxis = new Vector3(0.0f, 0.0f, 0.0f);

    public Camera(float f, float f2, float f3) {
        this.pos = new Vector3(f, f2, f3);
    }
}
