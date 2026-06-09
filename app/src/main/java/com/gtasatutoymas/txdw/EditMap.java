package com.gtasatutoymas.txdw;

import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.gtasatutoymas.txdw.txd.TXDWorkshop;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import min3d.core.Object3dContainer;
import min3d.objectPrimitives.Box;
import min3d.objectPrimitives.Rectangle;
import min3d.objectPrimitives.Sphere;
import min3d.vos.Color4;
import min3d.vos.Light;

/* JADX INFO: loaded from: classes.dex */
public class EditMap extends min3d.core.RendererActivity {
    ListView lv;
    float sensorDensity;
    TXDWorkshop work;
    String dataPath = new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/txdw/").toString();
    ArrayList<Object3dContainer> objs = new ArrayList<>();
    ArrayList<DataObj> data = new ArrayList<>();

    @Override // min3d.core.RendererActivity, min3d.interfaces.ISceneController
    public void updateScene() {
    }

    class DataObj {
        public boolean has2Tex;
        public String obj;
        public String tex2;
        public String texture;
        private final EditMap this$0;
        public int type = 0;

        public DataObj(EditMap editMap) {
            this.this$0 = editMap;
        }
    }

    @Override // min3d.core.RendererActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.abc_edit_map_layout);
        ((LinearLayout) findViewById(R.id.RenderViewEdit)).addView(this._glSurfaceView);
        this.lv = (ListView) findViewById(R.id.listItems);
    }

    public void loadScene() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(this.dataPath).append("scene.txt").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    if (!line.startsWith("//")) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        String strNextToken = stringTokenizer.nextToken();
                        if (strNextToken.contains("cam")) {
                            this.scene.camera().position.x = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().position.y = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().position.z = Float.parseFloat(stringTokenizer.nextToken()) / 4.0f;
                            this.scene.camera().target.x = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().target.y = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().target.z = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().upAxis.x = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().upAxis.y = Float.parseFloat(stringTokenizer.nextToken());
                            this.scene.camera().upAxis.z = Float.parseFloat(stringTokenizer.nextToken());
                        } else if (strNextToken.contains("bgs")) {
                            this.scene.backgroundColor().setAll(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                        } else if (strNextToken.contains("sen")) {
                            this.sensorDensity = Float.parseFloat(stringTokenizer.nextToken());
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(this.dataPath).append("maps.txt").toString()));
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
                                box.textures().addById(this.work.getTexID(strNextToken2));
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
                            DataObj dataObj = new DataObj(this);
                            dataObj.type = 0;
                            dataObj.texture = strNextToken2;
                            this.data.add(dataObj);
                            this.objs.add(box);
                        } else if (strNextToken.contains("sp")) {
                            String strNextToken3 = stringTokenizer.nextToken();
                            Sphere sphere = new Sphere(Float.parseFloat(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()) * 5, Integer.parseInt(stringTokenizer.nextToken()) * 5);
                            if (!strNextToken3.contains("null")) {
                                sphere.textures().addById(this.work.getTexID(strNextToken3));
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
                            DataObj dataObj2 = new DataObj(this);
                            dataObj2.type = 1;
                            dataObj2.texture = strNextToken3;
                            this.data.add(dataObj2);
                            this.objs.add(sphere);
                        } else if (strNextToken.contains("ret")) {
                            String strNextToken4 = stringTokenizer.nextToken();
                            int i = Integer.parseInt(stringTokenizer.nextToken());
                            int i2 = Integer.parseInt(stringTokenizer.nextToken());
                            int i3 = Integer.parseInt(stringTokenizer.nextToken());
                            Rectangle rectangle = new Rectangle(i, i2, i3, i3, new Color4());
                            if (!strNextToken4.contains("null")) {
                                rectangle.textures().addById(this.work.getTexID(strNextToken4));
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
                            DataObj dataObj3 = new DataObj(this);
                            dataObj3.type = 2;
                            dataObj3.texture = strNextToken4;
                            this.data.add(dataObj3);
                            this.objs.add(rectangle);
                        } else {
                            String strNextToken5 = stringTokenizer.nextToken();
                            String strNextToken6 = stringTokenizer.nextToken();
                            String strNextToken7 = "";
                            if (stringTokenizer.countTokens() == 11) {
                                strNextToken7 = stringTokenizer.nextToken();
                            }
                            OBJLoader oBJLoader = new OBJLoader(strNextToken5);
                            if (!strNextToken6.contains("null")) {
                                oBJLoader.setTexture(this.work.getTexID(strNextToken6));
                            }
                            if (stringTokenizer.countTokens() == 11) {
                                oBJLoader.setTexture(this.work.getTexID(strNextToken7));
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
                            DataObj dataObj4 = new DataObj(this);
                            dataObj4.type = 3;
                            dataObj4.obj = strNextToken5;
                            dataObj4.texture = strNextToken6;
                            if (stringTokenizer.countTokens() == 11) {
                                dataObj4.tex2 = strNextToken7;
                                dataObj4.has2Tex = true;
                            } else {
                                dataObj4.has2Tex = false;
                            }
                            this.data.add(dataObj4);
                            this.objs.add(object);
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

    public void SaveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new StringBuffer().append(this.dataPath).append("maps.txt").toString());
            boolean z = false;
            int i = 0;
            while (i < this.data.size()) {
                DataObj dataObj = this.data.get(i);
                Object3dContainer object3dContainer = this.objs.get(i);
                if (dataObj.type == 0) {
                    fileOutputStream.write(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("box,").append(dataObj.texture).toString()).append(",").toString()).append(object3dContainer.paramsf1).toString()).append(",").toString()).append(object3dContainer.paramsf2).toString()).append(",").toString()).append(object3dContainer.paramsf3).toString()).append(",").toString()).append(object3dContainer.position().x).toString()).append(",").toString()).append(object3dContainer.position().y).toString()).append(",").toString()).append(object3dContainer.position().z).toString()).append(",").toString()).append(object3dContainer.rotation().x).toString()).append(",").toString()).append(object3dContainer.rotation().y).toString()).append(",").toString()).append(object3dContainer.rotation().z).toString()).append(",").toString()).append(object3dContainer.rotateAuto() ? 1 : 0).toString()).append(",").toString()).append(object3dContainer.rotValue()).toString()).append(z ? "" : "\n").toString().getBytes());
                } else if (dataObj.type == 1) {
                    fileOutputStream.write(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("sp,").append(dataObj.texture).toString()).append(",").toString()).append(object3dContainer.paramsf1).toString()).append(",").toString()).append(object3dContainer.paramsf2).toString()).append(",").toString()).append(object3dContainer.paramsf3).toString()).append(",").toString()).append(object3dContainer.position().x).toString()).append(",").toString()).append(object3dContainer.position().y).toString()).append(",").toString()).append(object3dContainer.position().z).toString()).append(",").toString()).append(object3dContainer.rotation().x).toString()).append(",").toString()).append(object3dContainer.rotation().y).toString()).append(",").toString()).append(object3dContainer.rotation().z).toString()).append(",").toString()).append(object3dContainer.rotateAuto() ? 1 : 0).toString()).append(",").toString()).append(object3dContainer.rotValue()).toString()).append(z ? "" : "\n").toString().getBytes());
                } else if (dataObj.type == 2) {
                    fileOutputStream.write(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("ret,").append(dataObj.texture).toString()).append(",").toString()).append(object3dContainer.paramsf1).toString()).append(",").toString()).append(object3dContainer.paramsf2).toString()).append(",").toString()).append(object3dContainer.paramsf3).toString()).append(",").toString()).append(object3dContainer.position().x).toString()).append(",").toString()).append(object3dContainer.position().y).toString()).append(",").toString()).append(object3dContainer.position().z).toString()).append(",").toString()).append(object3dContainer.rotation().x).toString()).append(",").toString()).append(object3dContainer.rotation().y).toString()).append(",").toString()).append(object3dContainer.rotation().z).toString()).append(z ? "" : "\n").toString().getBytes());
                } else if (dataObj.type == 3) {
                    fileOutputStream.write(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("obj,").append(dataObj.obj).toString()).append(",").toString()).append(dataObj.texture).toString()).append(",").toString()).append(dataObj.has2Tex ? new StringBuffer().append(dataObj.tex2).append(",").toString() : ",").toString()).append(object3dContainer.position().x).toString()).append(",").toString()).append(object3dContainer.position().y).toString()).append(",").toString()).append(object3dContainer.position().z).toString()).append(",").toString()).append(object3dContainer.rotation().x).toString()).append(",").toString()).append(object3dContainer.rotation().y).toString()).append(",").toString()).append(object3dContainer.rotation().z).toString()).append(",").toString()).append(object3dContainer.rotateAuto() ? 1 : 0).toString()).append(",").toString()).append(object3dContainer.rotValue()).toString()).append(z ? "" : "\n").toString().getBytes());
                }
                z = i == this.objs.size() + (-1);
                i++;
            }
            fileOutputStream.close();
        } catch (IOException e) {
        }
    }

    @Override // min3d.core.RendererActivity, min3d.interfaces.ISceneController
    public void initScene() {
        this.work = new TXDWorkshop(new StringBuffer().append(this.dataPath).append("render.txd").toString(), getApplicationContext());
        this.work.PrepareToRender();
        loadScene();
        readMap();
        this.scene.lights().add(new Light());
        for (int i = 0; i < this.objs.size(); i++) {
            this.scene.addChild(this.objs.get(i));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.sensorDensity = displayMetrics.density;
    }
}
