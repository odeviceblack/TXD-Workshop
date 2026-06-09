package com.gtasatutoymas.txdw;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Environment;
import com.gtasatutoymas.txdw.engine.Objs;
import com.gtasatutoymas.txdw.engine.Render;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLView extends GLSurfaceView {
    public OpenGLView(Context context) {
        super(context);
        setRenderer(new Render());
    }

    public class RendererGL implements GLSurfaceView.Renderer {
        TextureGL gltex;

        // Quad: 4 vértices, 2 triângulos (6 indices)
        float[] POSITIONS = {
            -1.5f,  1.5f, 0.0f,
            -1.5f, -1.5f, 0.0f,
             1.5f,  1.5f, 0.0f,
             1.5f, -1.5f, 0.0f
        };
        float[] TEXCOORDS = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
        };
        // Dois triângulos formando o quad
        short[] INDICES = { 0, 1, 2, 1, 3, 2 };

        int texture = 0;
        Objs quad = null;

        private FloatBuffer createFloatBuffer(float[] arr) {
            ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(arr);
            fb.position(0);
            return fb;
        }

        private Objs buildQuad() {
            // 4 vértices, 2 triângulos
            Objs obj = new Objs(4, 2);
            for (int i = 0; i < POSITIONS.length; i += 3)
                obj.setVertex(POSITIONS[i], POSITIONS[i+1], POSITIONS[i+2]);
            for (int i = 0; i < TEXCOORDS.length; i += 2)
                obj.setUV(TEXCOORDS[i], TEXCOORDS[i+1]);
            for (int i = 0; i < INDICES.length; i += 3)
                obj.setindices(INDICES[i], INDICES[i+1], INDICES[i+2]);
            obj.ToBuffers();
            return obj;
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            this.texture = TextureGL.getID(gl10,
                    Environment.getExternalStorageDirectory() + "/txdw/tmp.png");
            this.quad = buildQuad();
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0.27450982f, 0.27450982f, 0.27450982f, 1.0f);
            gl10.glClear(16640);

            gl10.glEnableClientState(32884); // GL_VERTEX_ARRAY
            gl10.glVertexPointer(3, 5126, 0, quad.toVertexBuffer());
            gl10.glEnableClientState(32888); // GL_TEXTURE_COORD_ARRAY
            gl10.glTexCoordPointer(2, 5126, 0, quad.toUVBuffer());

            gl10.glEnable(2884);   // GL_CULL_FACE
            gl10.glCullFace(1029); // GL_BACK

            gl10.glMatrixMode(5888); // GL_MODELVIEW
            gl10.glLoadIdentity();
            GLU.gluLookAt(gl10, 4, 4, 5.5f, 0, 0, 0, 0, 1, 0);

            FloatBuffer lightColor    = createFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
            FloatBuffer lightSpecular = createFloatBuffer(new float[]{3.0f, 3.0f, 3.0f});
            FloatBuffer lightPos      = createFloatBuffer(new float[]{3.0f, 3.0f, 3.0f, 0.0f});

            gl10.glEnable(2896);  // GL_LIGHTING
            gl10.glLightfv(16385, 4611, lightPos);
            gl10.glLightfv(16385, 4609, lightColor);
            gl10.glLightfv(16385, 4612, lightSpecular);
            gl10.glEnable(16385); // GL_LIGHT0

            // Face 1
            gl10.glTranslatef(-2.0f, 1.0f, 0.0f);
            gl10.glRotatef(0.1f, 1, 0, 0);
            TextureGL.draw(gl10, texture, quad);

            // Face 2
            gl10.glPushMatrix();
            gl10.glRotatef(90, 0, 1, 0);
            TextureGL.draw(gl10, texture, quad);
            gl10.glPopMatrix();

            // Face 3
            gl10.glPushMatrix();
            gl10.glRotatef(-90, 1, 0, 0);
            TextureGL.draw(gl10, texture, quad);
            gl10.glPopMatrix();

            gl10.glTranslatef(5.0f, 0.5f, 0.0f);

            // Face 4
            TextureGL.draw(gl10, texture, quad);

            // Face 5
            gl10.glPushMatrix();
            gl10.glRotatef(90, 0, 1, 0);
            TextureGL.draw(gl10, texture, quad);
            gl10.glPopMatrix();

            // Face 6
            gl10.glPushMatrix();
            gl10.glRotatef(-90, 1, 0, 0);
            TextureGL.draw(gl10, texture, quad);
            gl10.glPopMatrix();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int w, int h) {
            gl10.glViewport(0, 0, w, h);
            gl10.glMatrixMode(5889); // GL_PROJECTION
            gl10.glLoadIdentity();
            GLU.gluPerspective(gl10, 45, (float) w / h, 1, 10);
        }
    }
}