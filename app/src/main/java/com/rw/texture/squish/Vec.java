package com.rw.texture.squish;

/* JADX INFO: loaded from: classes.dex */
final class Vec {
    private float x;
    private float y;
    private float z;

    Vec() {
    }

    Vec(float f) {
        this(f, f, f);
    }

    Vec(Vec vec) {
        this(vec.x, vec.y, vec.z);
    }

    Vec(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    float x() {
        return this.x;
    }

    float y() {
        return this.y;
    }

    float z() {
        return this.z;
    }

    Vec set(float f) {
        this.x = f;
        this.y = f;
        this.z = f;
        return this;
    }

    Vec set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
        return this;
    }

    Vec set(Vec vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    Vec add(Vec vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    Vec add(float f, float f2, float f3) {
        this.x += f;
        this.y += f2;
        this.z += f3;
        return this;
    }

    Vec sub(Vec vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    Vec mul(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    Vec mul(Vec vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    Vec div(float f) {
        float f2 = 1.0f / f;
        this.x *= f2;
        this.y *= f2;
        this.z *= f2;
        return this;
    }

    float lengthSQ() {
        return dot(this);
    }

    float dot(Vec vec) {
        return (this.x * vec.x) + (this.y * vec.y) + (this.z * vec.z);
    }
}
