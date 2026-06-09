package com.gtasatutoymas.txdw.txd;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.gtasatutoymas.txdw.R;
import com.rw.texture.DXTDecompress;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import min3d.Shared;
import min3d.core.Color4BufferList;

/* JADX INFO: loaded from: classes.dex */
public class TXDWorkshop {
    Context context;
    ArrayList<TexIndex> indexTextures;
    boolean isCorrupt;
    String path;
    int sizeTexture;
    ArrayList<TXDHeader> textures;

    public TXDWorkshop(String str, Context context) {
        this.textures = new ArrayList<>();
        this.sizeTexture = 0;
        this.isCorrupt = false;
        this.indexTextures = new ArrayList<>();
        try {
            init(new FileInputStream(str));
            this.context = context;
            this.path = str;
        } catch (Exception e) {
            this.isCorrupt = true;
            Toast.makeText(context, context.getResources().getString(R.string.corrupt), 1).show();
        }
    }

    public TXDWorkshop(String str) {
        this.textures = new ArrayList<>();
        this.sizeTexture = 0;
        this.isCorrupt = false;
        this.indexTextures = new ArrayList<>();
        this.path = str;
    }

    private void init(InputStream inputStream) throws IOException {
        readInt(inputStream);
        readInt(inputStream);
        readInt(inputStream);
        inputStream.skip(4);
        if (readInt(inputStream) != 4) {
            Toast.makeText(this.context, "Error Textura Corrupta", 0).show();
        }
        inputStream.skip(4);
        int i = readShort(inputStream);
        inputStream.skip(2);
        for (int i2 = 0; i2 < i; i2++) {
            TXDHeader tXDHeader = new TXDHeader();
            inputStream.skip(4);
            int i3 = readInt(inputStream);
            inputStream.skip(4);
            inputStream.skip(12);
            tXDHeader.structSize = i3 - 24;
            tXDHeader.platform = readInt(inputStream);
            tXDHeader.filterFlags = readShort(inputStream);
            tXDHeader.vWrap = readByte(inputStream);
            tXDHeader.uWrap = readByte(inputStream);
            tXDHeader.diffuseName = readDiffuse(inputStream);
            tXDHeader.alphaName = readDiffuse(inputStream);
            tXDHeader.rasterFormat = readInt(inputStream);
            String string = readString(inputStream);
            if (string.contains("DXT1")) {
                tXDHeader.Compression = 1;
            } else if (string.contains("DXT3")) {
                tXDHeader.Compression = 3;
            } else {
                tXDHeader.Compression = 0;
            }
            tXDHeader.width = readShort(inputStream);
            tXDHeader.height = readShort(inputStream);
            tXDHeader.bpp = readByte(inputStream);
            tXDHeader.mipmapCount = readByte(inputStream);
            tXDHeader.rasterType = readByte(inputStream);
            int i4 = readByte(inputStream);
            tXDHeader.alphaCompress = i4;
            tXDHeader.alphaUsed = i4 == 9 || i4 == 1;
            tXDHeader.dataSize = readInt(inputStream);
            byte[] bArr = new byte[tXDHeader.dataSize];
            inputStream.read(bArr, 0, tXDHeader.dataSize);
            tXDHeader.data = bArr;
            tXDHeader.nativeSize = i3;
            this.textures.add(tXDHeader);
            inputStream.skip((i3 - 104) - bArr.length);
        }
    }

    public String getpath() {
        return this.path;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getRaster(int i) {
        String str = "Unknown";
        switch (this.textures.get(i).rasterFormat) {
            case 512:
                str = "RGB565";
                break;
            case 768:
                str = "RGBA4444";
                break;
            case 1280:
                str = "BGRA8888";
                break;
            case 1536:
                str = "BGR888";
                break;
            case 3840:
                str = "RGBA8888";
                break;
        }
        return str;
    }

    public String getName(int i) {
        return cortarnombre(this.textures.get(i).diffuseName);
    }

    public void PrepareToRender() {
        for (int i = 0; i < getNumTextures(); i++) {
            String string = new StringBuffer().append("tex").append(i).toString();
            Shared.textureManager().addTextureId(getImageDecompress(i), string);
            this.indexTextures.add(new TexIndex(this, string, getName(i)));
        }
    }

    public String getTexID(String str) {
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= this.indexTextures.size()) {
                break;
            }
            if (!this.indexTextures.get(i2).name.equals(str)) {
                i2++;
            } else {
                i = i2;
                break;
            }
        }
        return this.indexTextures.get(i).id;
    }

    public Bitmap getImageDecompress(int i) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        TXDHeader tXDHeader = this.textures.get(i);
        switch (tXDHeader.Compression) {
            case 0:
                switch (tXDHeader.rasterFormat) {
                    case 512:
                        bitmapCreateBitmap = UncompressedRasters.ConvertRGB565(tXDHeader);
                        break;
                    case 768:
                        bitmapCreateBitmap = UncompressedRasters.convertRGBA4444(tXDHeader);
                        break;
                    case 1280:
                        bitmapCreateBitmap = UncompressedRasters.convertBGRA8888(tXDHeader);
                        break;
                    case 1536:
                        bitmapCreateBitmap = UncompressedRasters.convertBGR888(tXDHeader);
                        break;
                    case 3840:
                        bitmapCreateBitmap = UncompressedRasters.convertRGBA8888(tXDHeader);
                        break;
                }
                break;
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                bitmapCreateBitmap = convertDXT1toRGBA8888(tXDHeader);
                break;
            case 3:
                bitmapCreateBitmap = convertDXT3toRGBA8888(tXDHeader);
                break;
        }
        return bitmapCreateBitmap;
    }

    public String getDimens(int i) {
        TXDHeader tXDHeader = this.textures.get(i);
        return new StringBuffer().append(new StringBuffer().append(tXDHeader.width).append("X").toString()).append(tXDHeader.height).toString();
    }

    public String cortarnombre(String str) {
        int iIndexOf = str.indexOf(0);
        return iIndexOf > 0 ? str.substring(0, iIndexOf) : str;
    }

    public int getAlphaIV(int i) {
        int i2 = 0;
        if (this.textures.get(i).alphaUsed) {
            i2 = 2;
        }
        return i2;
    }

    public int getNumTextures() {
        return this.textures.size();
    }

    public String getCompression(int i) {
        String string = "Unknown";
        switch (this.textures.get(i).Compression) {
            case 0:
                string = this.context.getResources().getString(R.string.nocompressed);
                break;
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                string = new StringBuffer().append(this.context.getResources().getString(R.string.compressed)).append(" DXT1").toString();
                break;
            case 3:
                string = new StringBuffer().append(this.context.getResources().getString(R.string.compressed)).append(" DXT3").toString();
                break;
        }
        return string;
    }

    public void SaveTXD() {
        if (!isWritable()) {
            Toast.makeText(this.context, this.context.getResources().getString(R.string.nw), 1).show();
            return;
        }
        try {
            this.sizeTexture = 0;
            this.sizeTexture += 28;
            this.sizeTexture += this.textures.size() * 12;
            for (int i = 0; i < this.textures.size(); i++) {
                this.sizeTexture += this.textures.get(i).nativeSize;
            }
            this.sizeTexture += 12;
            FileOutputStream fileOutputStream = new FileOutputStream(this.path);
            byte[] bArr = new byte[28];
            writeInt(bArr, 0, 22);
            writeInt(bArr, 4, this.sizeTexture);
            writeInt(bArr, 8, 402915327);
            writeInt(bArr, 12, 1);
            writeInt(bArr, 16, 4);
            writeInt(bArr, 20, 402915327);
            writeShort(bArr, 24, this.textures.size());
            writeShort(bArr, 26, 2);
            fileOutputStream.write(bArr);
            for (int i2 = 0; i2 < this.textures.size(); i2++) {
                TXDHeader tXDHeader = this.textures.get(i2);
                byte[] bArr2 = new byte[24];
                writeInt(bArr2, 0, 21);
                writeInt(bArr2, 4, tXDHeader.nativeSize);
                writeInt(bArr2, 8, 402915327);
                writeInt(bArr2, 12, 1);
                writeInt(bArr2, 16, tXDHeader.structSize);
                writeInt(bArr2, 20, 402915327);
                fileOutputStream.write(bArr2);
                byte[] bArr3 = new byte[8];
                writeInt(bArr3, 0, tXDHeader.platform);
                writeShort(bArr3, 4, tXDHeader.filterFlags);
                writeByte(bArr3, 6, tXDHeader.vWrap);
                writeByte(bArr3, 7, tXDHeader.uWrap);
                fileOutputStream.write(bArr3);
                byte[] bArr4 = new byte[32];
                byte[] bytes = tXDHeader.diffuseName.getBytes();
                System.arraycopy(bytes, 0, bArr4, 0, bytes.length);
                byte[] bArr5 = new byte[32];
                byte[] bytes2 = tXDHeader.alphaName.getBytes();
                System.arraycopy(bytes2, 0, bArr5, 0, bytes2.length);
                fileOutputStream.write(bArr4);
                fileOutputStream.write(bArr5);
                byte[] bArr6 = new byte[4];
                writeInt(bArr6, 0, tXDHeader.rasterFormat);
                fileOutputStream.write(bArr6);
                if (tXDHeader.Compression == 1) {
                    fileOutputStream.write("DXT1".getBytes());
                } else if (tXDHeader.Compression == 3) {
                    fileOutputStream.write("DXT3".getBytes());
                } else {
                    fileOutputStream.write(new byte[4]);
                }
                byte[] bArr7 = new byte[12];
                writeShort(bArr7, 0, tXDHeader.width);
                writeShort(bArr7, 2, tXDHeader.height);
                writeByte(bArr7, 4, tXDHeader.bpp);
                writeByte(bArr7, 5, tXDHeader.mipmapCount);
                writeByte(bArr7, 6, tXDHeader.rasterType);
                writeByte(bArr7, 7, tXDHeader.alphaCompress);
                writeInt(bArr7, 8, tXDHeader.dataSize);
                fileOutputStream.write(bArr7);
                fileOutputStream.write(tXDHeader.data);
                byte[] bArr8 = new byte[12];
                writeInt(bArr8, 0, 3);
                writeInt(bArr8, 4, 0);
                writeInt(bArr8, 8, 402915327);
                fileOutputStream.write(bArr8);
            }
            byte[] bArr9 = new byte[12];
            writeInt(bArr9, 0, 3);
            writeInt(bArr9, 4, 0);
            writeInt(bArr9, 8, 402915327);
            fileOutputStream.write(bArr9);
            fileOutputStream.close();
        } catch (IOException e) {
            Toast.makeText(this.context, "Error Saved", 1).show();
        }
    }

    public boolean isWritable() {
        boolean z = true;
        int i = 0;
        while (true) {
            if (i >= this.textures.size()) {
                break;
            }
            if (!getRaster(i).contains("Unknown")) {
                i++;
            } else {
                z = false;
                break;
            }
        }
        return z;
    }

    public boolean isCorrupt() {
        return this.isCorrupt;
    }

    public void NewTXD(String str) {
        this.textures.clear();
        this.path = str;
        try {
            this.sizeTexture = 0;
            this.sizeTexture += 28;
            this.sizeTexture += 12;
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            byte[] bArr = new byte[28];
            writeInt(bArr, 0, 22);
            writeInt(bArr, 4, this.sizeTexture);
            writeInt(bArr, 8, 402915327);
            writeInt(bArr, 12, 1);
            writeInt(bArr, 16, 4);
            writeInt(bArr, 20, 402915327);
            writeShort(bArr, 24, 0);
            writeShort(bArr, 26, 2);
            fileOutputStream.write(bArr);
            byte[] bArr2 = new byte[12];
            writeInt(bArr2, 0, 3);
            writeInt(bArr2, 4, 0);
            writeInt(bArr2, 8, 402915327);
            fileOutputStream.write(bArr2);
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(this.context, new StringBuffer().append("Error created: ").append(e.getMessage()).toString(), 1).show();
        }
    }

    public void addTexture(TXDHeader tXDHeader) {
        if (isWritable()) {
            this.textures.add(tXDHeader);
            SaveTXD();
        }
    }

    public void editTexture(String str, int i) {
        TXDHeader tXDHeader = this.textures.get(i);
        tXDHeader.diffuseName = str;
        this.textures.remove(i);
        this.textures.add(i, tXDHeader);
        SaveTXD();
    }

    public void removeTexture(int i) {
        if (!isWritable()) {
            Toast.makeText(this.context, this.context.getResources().getString(R.string.nw), 1).show();
        } else {
            this.textures.remove(i);
            SaveTXD();
        }
    }

    public String[] getStringTextures() {
        String[] strArr = new String[this.textures.size()];
        for (int i = 0; i < this.textures.size(); i++) {
            strArr[i] = cortarnombre(this.textures.get(i).diffuseName);
        }
        return strArr;
    }

    private static void writeInt(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 & 255);
        bArr[i + 1] = (byte) ((i2 >> 8) & 255);
        bArr[i + 2] = (byte) ((i2 >> 16) & 255);
        bArr[i + 3] = (byte) ((i2 >> 24) & 255);
    }

    private static void writeShort(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 & 255);
        bArr[i + 1] = (byte) ((i2 >> 8) & 255);
    }

    private static void writeByte(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 & 255);
    }

    private static int readInt(InputStream inputStream) throws IOException {
        return inputStream.read() | (inputStream.read() << 8) | (inputStream.read() << 16) | (inputStream.read() << 24);
    }

    private static int readShort(InputStream inputStream) throws IOException {
        return inputStream.read() | (inputStream.read() << 8);
    }

    private static int readByte(InputStream inputStream) throws IOException {
        return inputStream.read();
    }

    private static String readDiffuse(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[32];
        inputStream.read(bArr, 0, 32);
        return new String(bArr);
    }

    private static String readString(InputStream inputStream) throws IOException {
        return new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("").append((char) ((byte) inputStream.read())).toString()).append((char) ((byte) inputStream.read())).toString()).append((char) ((byte) inputStream.read())).toString()).append((char) ((byte) inputStream.read())).toString();
    }

    private Bitmap convertDXT1toRGBA8888(TXDHeader tXDHeader) {
        DXTDecompress dXTDecompress = new DXTDecompress(tXDHeader.data, tXDHeader.width, tXDHeader.height, 1);
        return Bitmap.createBitmap(dXTDecompress.getImagePixels(), dXTDecompress.getWidth(), dXTDecompress.getHeight(), Bitmap.Config.ARGB_8888);
    }

    private Bitmap convertDXT3toRGBA8888(TXDHeader tXDHeader) {
        DXTDecompress dXTDecompress = new DXTDecompress(tXDHeader.data, tXDHeader.width, tXDHeader.height, 3);
        return Bitmap.createBitmap(dXTDecompress.getImagePixels(), dXTDecompress.getWidth(), dXTDecompress.getHeight(), Bitmap.Config.ARGB_8888);
    }

    public class TexIndex {
        public String id;
        public String name;
        private final TXDWorkshop this$0;

        public TexIndex(TXDWorkshop tXDWorkshop, String str, String str2) {
            this.this$0 = tXDWorkshop;
            this.id = str;
            this.name = str2;
        }
    }
}
