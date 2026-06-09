package com.gtasatutoymas.txdw;

import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import com.gtasatutoymas.txdw.txd.TXDWorkshop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import min3d.core.Object3dContainer;
import min3d.vos.Light;

/* JADX INFO: loaded from: classes.dex */
public class PreviewActivity extends min3d.core.RendererActivity {
    ListView lv;
    Object3dContainer objl;
    float sensorDensity;
    TXDWorkshop work;
    String dataPath = new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/txdw/").toString();
    ArrayList<Object3dContainer> objs = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    float py = 0.0f;
    boolean frist = true;

    @Override // min3d.core.RendererActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.edit_map);
        loadNames();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        sensorDensity = displayMetrics.density;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.preview);
        lv = (ListView) findViewById(R.id.objs);
        lv.setAdapter((android.widget.ListAdapter) new ListAdapter(getApplicationContext(), ids));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.gtasatutoymas.txdw.PreviewActivity.100000000
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                updateModel(i);
            }
        });
        linearLayout.addView(_glSurfaceView);
        linearLayout.setOnTouchListener(new View.OnTouchListener() { // from class: com.gtasatutoymas.txdw.PreviewActivity.100000001
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                if (motionEvent.getAction() == 2) {
                    if (scene.camera().position.y > 30 || scene.camera().position.y < -30) {
                        if (scene.camera().position.y > 30) {
                            scene.camera().position.y -= 2.0f;
                        }
                        if (scene.camera().position.y < -30) {
                            scene.camera().position.y -= -2.0f;
                        }
                    } else {
                        scene.camera().position.y += ((y - py) / sensorDensity) / 2.0f;
                    }
                }
                py = y;
                return true;
            }
        });
        SeekBar seekBar = (SeekBar) findViewById(R.id.sbCamera);
        seekBar.setMax(80);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.gtasatutoymas.txdw.PreviewActivity.100000002
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                scene.camera().position.z = i / 0.5f;
            }
        });
    }

    public void updateModel(int i) {
        objl = objs.get(i);
        if (frist) {
            scene.addChild(objl);
            frist = false;
        } else {
            scene.removeChildAt(0);
            scene.addChild(objl);
        }
    }

    public void loadModels() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(dataPath).append("maps.txt").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return;
                }
                if (!line.startsWith("//")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                    if (stringTokenizer.nextToken().contains("obj")) {
                        String strNextToken = stringTokenizer.nextToken();
                        String strNextToken2 = stringTokenizer.nextToken();
                        String strNextToken3 = "";
                        if (stringTokenizer.countTokens() == 11) {
                            strNextToken3 = stringTokenizer.nextToken();
                        }
                        OBJLoader oBJLoader = new OBJLoader(strNextToken);
                        if (!strNextToken2.contains("null")) {
                            oBJLoader.setTexture(work.getTexID(strNextToken2));
                        }
                        if (stringTokenizer.countTokens() == 11) {
                            oBJLoader.setTexture(work.getTexID(strNextToken3));
                        }
                        objs.add(oBJLoader.getObject());
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public void loadNames() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(dataPath).append("maps.txt").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    if (!line.startsWith("//")) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        if (stringTokenizer.nextToken().contains("obj")) {
                            ids.add(stringTokenizer.nextToken());
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
    public void initScene() {
        work = new TXDWorkshop(new StringBuffer().append(dataPath).append("render.txd").toString(), getApplicationContext());
        work.PrepareToRender();
        loadModels();
        scene.lights().add(new Light());
        scene.camera().position.z = 5;
        scene.camera().upAxis.y = 1;
        scene.backgroundColor().setAll(0, 130, 230, 255);
    }

    @Override // min3d.core.RendererActivity, min3d.interfaces.ISceneController
    public void updateScene() {
        if (frist) {
            return;
        }
        objl.rotation().y += 0.3f;
    }
}
