package com.gtasatutoymas.txdw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.gtasatutoymas.txdw.txd.TXDWorkshop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import min3d.core.Color4BufferList;
import min3d.core.Object3dContainer;
import min3d.objectPrimitives.Box;
import min3d.objectPrimitives.Rectangle;
import min3d.objectPrimitives.Sphere;
import min3d.vos.Color4;
import min3d.vos.Light;

/* JADX INFO: loaded from: classes.dex */
public class RendererActivity extends min3d.core.RendererActivity {
    int mode;
    float ox;
    float oy;
    TXDWorkshop work;
    int instances = 0;
    ArrayList<Object3dContainer> objs = new ArrayList<>();
    float sensorDensity = 2.0f;
    String dataPath = new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/txdw/").toString();
    int UP = 0;
    int DOWN = 1;

    @Override // min3d.core.RendererActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.abc_renderer);
        ((LinearLayout) findViewById(R.id.renderview)).addView(_glSurfaceView);
        SeekBar seekBar = (SeekBar) findViewById(R.id.sbCameraGame);
        seekBar.setMax(200);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.gtasatutoymas.txdw.RendererActivity.100000000
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                scene.camera().position.z = (i - 100) / 1.5f;
            }
        });
    }

    @Override // min3d.core.RendererActivity, min3d.interfaces.ISceneController
    public void initScene() {
        work = new TXDWorkshop(new StringBuffer().append(dataPath).append("render.txd").toString(), getApplicationContext());
        work.PrepareToRender();
        loadScene();
        readMap();
        scene.lights().add(new Light());
        for (int i = 0; i < objs.size(); i++) {
            scene.addChild(objs.get(i));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        sensorDensity = displayMetrics.density;
    }

    public void loadScene() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(dataPath).append("scene.txt").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    if (!line.startsWith("//")) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        String strNextToken = stringTokenizer.nextToken();
                        if (strNextToken.contains("cam")) {
                            scene.camera().position.x = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().position.y = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().position.z = Float.parseFloat(stringTokenizer.nextToken()) / 4.0f;
                            scene.camera().target.x = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().target.y = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().target.z = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().upAxis.x = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().upAxis.y = Float.parseFloat(stringTokenizer.nextToken());
                            scene.camera().upAxis.z = Float.parseFloat(stringTokenizer.nextToken());
                        } else if (strNextToken.contains("bgs")) {
                            scene.backgroundColor().setAll(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                        } else if (strNextToken.contains("sen")) {
                            sensorDensity = Float.parseFloat(stringTokenizer.nextToken());
                        }
                    }
                } else {
                    bufferedReader.close();
                    return;
                }
            }
        } catch (IOException e) {
        }
    }

    public void readMap() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(dataPath).append("maps.txt").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    if (!line.startsWith("//")) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        String strNextToken = stringTokenizer.nextToken();
                        if (strNextToken.contains("box")) {
                            String strNextToken2 = stringTokenizer.nextToken();
                            Box box = new Box(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                            if (!strNextToken2.contains("null")) {
                                box.textures().addById(work.getTexID(strNextToken2));
                            }
                            box.position().x = Float.parseFloat(stringTokenizer.nextToken());
                            box.position().z = Float.parseFloat(stringTokenizer.nextToken());
                            box.position().y = Float.parseFloat(stringTokenizer.nextToken());
                            box.rotation().x = Float.parseFloat(stringTokenizer.nextToken());
                            box.rotation().z = Float.parseFloat(stringTokenizer.nextToken());
                            box.rotation().y = Float.parseFloat(stringTokenizer.nextToken());
                            if (Integer.parseInt(stringTokenizer.nextToken()) != 0) {
                                box.setAutoRot(true, Integer.parseInt(stringTokenizer.nextToken()));
                            } else {
                                box.setAutoRot(false, 0);
                            }
                            objs.add(box);
                        } else if (strNextToken.contains("sp")) {
                            String strNextToken3 = stringTokenizer.nextToken();
                            Sphere sphere = new Sphere(Float.parseFloat(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()) * 5, Integer.parseInt(stringTokenizer.nextToken()) * 5);
                            if (!strNextToken3.contains("null")) {
                                sphere.textures().addById(work.getTexID(strNextToken3));
                            }
                            sphere.position().x = Float.parseFloat(stringTokenizer.nextToken());
                            sphere.position().z = Float.parseFloat(stringTokenizer.nextToken());
                            sphere.position().y = Float.parseFloat(stringTokenizer.nextToken());
                            sphere.rotation().x = Float.parseFloat(stringTokenizer.nextToken());
                            sphere.rotation().z = Float.parseFloat(stringTokenizer.nextToken());
                            sphere.rotation().y = Float.parseFloat(stringTokenizer.nextToken());
                            if (Integer.parseInt(stringTokenizer.nextToken()) != 0) {
                                sphere.setAutoRot(true, Integer.parseInt(stringTokenizer.nextToken()));
                            } else {
                                sphere.setAutoRot(false, 0);
                            }
                            objs.add(sphere);
                        } else if (strNextToken.contains("ret")) {
                            String strNextToken4 = stringTokenizer.nextToken();
                            int i = Integer.parseInt(stringTokenizer.nextToken());
                            int i2 = Integer.parseInt(stringTokenizer.nextToken());
                            int i3 = Integer.parseInt(stringTokenizer.nextToken());
                            Rectangle rectangle = new Rectangle(i, i2, i3, i3, new Color4());
                            if (!strNextToken4.contains("null")) {
                                rectangle.textures().addById(work.getTexID(strNextToken4));
                            }
                            rectangle.position().x = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.position().z = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.position().y = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.rotation().x = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.rotation().z = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.rotation().y = Float.parseFloat(stringTokenizer.nextToken());
                            rectangle.doubleSidedEnabled(true);
                            rectangle.lightingEnabled(false);
                            rectangle.setAutoRot(false, 0);
                            objs.add(rectangle);
                        } else {
                            String strNextToken5 = stringTokenizer.nextToken();
                            String strNextToken6 = stringTokenizer.nextToken();
                            String strNextToken7 = "";
                            if (stringTokenizer.countTokens() == 11) {
                                strNextToken7 = stringTokenizer.nextToken();
                            }
                            OBJLoader oBJLoader = new OBJLoader(strNextToken5);
                            if (!strNextToken6.contains("null")) {
                                oBJLoader.setTexture(work.getTexID(strNextToken6));
                            }
                            if (stringTokenizer.countTokens() == 11) {
                                oBJLoader.setTexture(work.getTexID(strNextToken7));
                            }
                            Object3dContainer object = oBJLoader.getObject();
                            object.position().x = Float.parseFloat(stringTokenizer.nextToken());
                            object.position().z = Float.parseFloat(stringTokenizer.nextToken());
                            object.position().y = Float.parseFloat(stringTokenizer.nextToken());
                            object.rotation().x = Float.parseFloat(stringTokenizer.nextToken());
                            object.rotation().z = Float.parseFloat(stringTokenizer.nextToken());
                            object.rotation().y = Float.parseFloat(stringTokenizer.nextToken());
                            if (Integer.parseInt(stringTokenizer.nextToken()) != 0) {
                                object.setAutoRot(true, Integer.parseInt(stringTokenizer.nextToken()));
                            } else {
                                object.setAutoRot(false, 0);
                            }
                            objs.add(object);
                        }
                    }
                } else {
                    bufferedReader.close();
                    return;
                }
            }
        } catch (IOException e) {
        }
    }

    @Override // min3d.core.RendererActivity, min3d.interfaces.ISceneController
    public void updateScene() {
        for (int i = 0; i < objs.size(); i++) {
            if (objs.get(i).rotateAuto()) {
                switch (objs.get(i).rotValue()) {
                    case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                        objs.get(i).rotation().x += 0.3f;
                        break;
                    case 2:
                        objs.get(i).rotation().z += 0.3f;
                        break;
                    case 3:
                        objs.get(i).rotation().y += 0.3f;
                        break;
                }
            }
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_renderer, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.previewMenu) {
            try {
                startActivity(new Intent(getApplicationContext(), Class.forName("com.gtasatutoymas.txdw.PreviewActivity")));
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return super.onTouchEvent(motionEvent);
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (motionEvent.getAction() == 2) {
            if (scene.camera().position.x > 80 || scene.camera().position.x < -80) {
                if (scene.camera().position.x > 80) {
                    scene.camera().position.x -= 2.0f;
                }
                if (scene.camera().position.x < -80) {
                    scene.camera().position.x -= -2.0f;
                }
            } else {
                scene.camera().position.x += ((x - ox) / sensorDensity) / 2.0f;
            }
            if (scene.camera().position.y > 70 || scene.camera().position.y < -0.01d) {
                if (scene.camera().position.y > 70) {
                    scene.camera().position.y -= 2.0f;
                }
                if (scene.camera().position.y < -0.01d) {
                    scene.camera().position.y -= -2.0f;
                }
            } else {
                scene.camera().position.y += ((y - oy) / sensorDensity) / 2.0f;
            }
        }
        ox = x;
        oy = y;
        return true;
    }
}
