package com.gtasatutoymas.txdw;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import min3d.core.Object3dContainer;
import min3d.vos.Color4;
import min3d.vos.Number3d;
import min3d.vos.Uv;

/* JADX INFO: loaded from: classes.dex */
public class OBJLoader {
    Object3dContainer obj;

    public OBJLoader(String str) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/txdw/").toString()).append(str).toString()).append(".obj").toString()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (line.length() != 0) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
                    String strNextToken = stringTokenizer.nextToken();
                    if (strNextToken.equals("v")) {
                        Number3d number3d = new Number3d();
                        number3d.x = Float.parseFloat(stringTokenizer.nextToken());
                        number3d.y = Float.parseFloat(stringTokenizer.nextToken());
                        number3d.z = Float.parseFloat(stringTokenizer.nextToken());
                        arrayList.add(number3d);
                    } else if (strNextToken.equals("vt")) {
                        Uv uv = new Uv();
                        uv.u = Float.parseFloat(stringTokenizer.nextToken());
                        uv.v = Float.parseFloat(stringTokenizer.nextToken());
                        arrayList2.add(uv);
                    } else if (strNextToken.equals("vn")) {
                        z = true;
                        Number3d number3d2 = new Number3d();
                        number3d2.x = Float.parseFloat(stringTokenizer.nextToken());
                        number3d2.y = Float.parseFloat(stringTokenizer.nextToken());
                        number3d2.z = Float.parseFloat(stringTokenizer.nextToken());
                        arrayList3.add(number3d2);
                    } else if (strNextToken.equals("f")) {
                        z2 = stringTokenizer.countTokens() == 5;
                        StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().replace('/', ' '));
                        arrayList4.add(new Integer(Integer.parseInt(stringTokenizer2.nextToken())));
                        arrayList5.add(new Integer(Integer.parseInt(stringTokenizer2.nextToken())));
                        if (z) {
                            arrayList6.add(new Integer(Integer.parseInt(stringTokenizer2.nextToken())));
                        }
                        StringTokenizer stringTokenizer3 = new StringTokenizer(stringTokenizer.nextToken().replace('/', ' '));
                        arrayList4.add(new Integer(Integer.parseInt(stringTokenizer3.nextToken())));
                        arrayList5.add(new Integer(Integer.parseInt(stringTokenizer3.nextToken())));
                        if (z) {
                            arrayList6.add(new Integer(Integer.parseInt(stringTokenizer3.nextToken())));
                        }
                        StringTokenizer stringTokenizer4 = new StringTokenizer(stringTokenizer.nextToken().replace('/', ' '));
                        arrayList4.add(new Integer(Integer.parseInt(stringTokenizer4.nextToken())));
                        arrayList5.add(new Integer(Integer.parseInt(stringTokenizer4.nextToken())));
                        if (z) {
                            arrayList6.add(new Integer(Integer.parseInt(stringTokenizer4.nextToken())));
                        }
                        if (z2) {
                            StringTokenizer stringTokenizer5 = new StringTokenizer(stringTokenizer.nextToken().replace('/', ' '));
                            arrayList4.add(new Integer(Integer.parseInt(stringTokenizer5.nextToken())));
                            arrayList5.add(new Integer(Integer.parseInt(stringTokenizer5.nextToken())));
                            if (z) {
                                arrayList6.add(new Integer(Integer.parseInt(stringTokenizer5.nextToken())));
                            }
                        }
                    }
                }
            }
            this.obj = new Object3dContainer(arrayList4.size() * (z ? 3 : 2), arrayList4.size(), new Boolean(true), new Boolean(z), new Boolean(true));
            for (int i2 = 0; i2 < arrayList4.size(); i2++) {
                int iIntValue = ((Integer) arrayList4.get(i2)).intValue();
                int iIntValue2 = ((Integer) arrayList5.get(i2)).intValue();
                int iIntValue3 = z ? ((Integer) arrayList6.get(i2)).intValue() : 0;
                Number3d number3d3 = (Number3d) arrayList.get(iIntValue - 1);
                Number3d number3d4 = z ? (Number3d) arrayList3.get(iIntValue3 - 1) : new Number3d();
                Uv uv2 = (Uv) arrayList2.get(iIntValue2 - 1);
                Color4 color4 = new Color4();
                this.obj.vertices().addVertex(number3d3.x, number3d3.y, number3d3.z, uv2.u, uv2.v, number3d4.x, number3d4.y, number3d4.z, color4.r, color4.g, color4.b, color4.a);
                if (!z2) {
                    this.obj.faces().add((short) i, ((short) i) + 1, ((short) i) + 2);
                } else {
                    this.obj.faces().add((short) i, ((short) i) + 1, ((short) i) + 3);
                    this.obj.faces().add(((short) i) + 1, ((short) i) + 2, ((short) i) + 3);
                }
                i += z2 ? 4 : 3;
            }
        } catch (IOException e) {
        }
    }

    public void setTexture(String str) {
        this.obj.textures().addById(str);
    }

    public Object3dContainer getObject() {
        return this.obj;
    }
}
