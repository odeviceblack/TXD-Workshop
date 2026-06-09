package com.gtasatutoymas.txdw.engine;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Environment;
import com.gtasatutoymas.txdw.TextureGL;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class Render implements GLSurfaceView.Renderer {
    Objs[] abj;
    Objs b;
    Camera cam;
    int glTex;
    Light lights;

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.cam = new Camera(3, 3, 3);
        this.cam.upAxis.y = 1;
        this.lights = new Light();
        this.lights.position(1.0f, 1.0f, 1.0f);
        this.lights.getDiffuse(10, 0, 0, 255);
        this.lights.getAmbient(0, 0, 0, 255);
        this.lights.getSpecular(255, 0, 0, 255);
        this.lights.getEmisive(255, 0, 0, 255);
        this.lights.direction(3.0f, 3.0f, 3.0f);
        this.b = new Box(4, 4, 4);
        this.glTex = TextureGL.getID(gl10, new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/tmp.png").toString());
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        gl10.glViewport(0, 0, i, i2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45, i / i2, 1, 10);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        gl10.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl10.glClear(16640);
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10, this.cam.pos.x, this.cam.pos.y, this.cam.pos.z, this.cam.target.x, this.cam.target.y, this.cam.target.z, this.cam.upAxis.x, this.cam.upAxis.y, this.cam.upAxis.z);
        drawLight(this.lights, gl10);
        drawObjects(this.b, gl10);
    }

    public void drawLight(Light light, GL10 gl10) {
        if (light != null) {
            gl10.glEnable(16384 + light.getIndex());
            int index = 16384 + light.getIndex();
            gl10.glLightfv(index, 4611, light.pos);
            gl10.glLightfv(index, 4608, light.ambient);
            gl10.glLightfv(index, 4609, light.diffuse);
            gl10.glLightfv(index, 5632, light.emissive);
            gl10.glLightfv(index, 4610, light.specular);
            gl10.glLightfv(index, 4612, light.direction);
            gl10.glLightf(index, 4615, 1.0f);
            gl10.glLightf(index, 4616, 0.0f);
            gl10.glLightf(index, 4617, 0.0f);
        }
    }

    public void drawObjects(Objs objs, GL10 gl10) {
        objs.ToBuffers();
        gl10.glEnableClientState(32884);
        gl10.glVertexPointer(3, 5126, 0, objs.toVertexBuffer());
        gl10.glEnableClientState(32888);
        gl10.glTexCoordPointer(2, 5126, 0, objs.toUVBuffer());
        gl10.glPushMatrix();
        gl10.glTranslatef(objs.pos.x, objs.pos.y, objs.pos.z);
        gl10.glRotatef(objs.rot.x, 1, 0, 0);
        gl10.glRotatef(objs.rot.y, 0, 1, 0);
        gl10.glRotatef(objs.rot.z, 0, 0, 1);
        gl10.glScalef(objs.scl.x, objs.scl.y, objs.scl.z);
        TextureGL.draw(gl10, this.glTex, objs);
        gl10.glPopMatrix();
    }
}
