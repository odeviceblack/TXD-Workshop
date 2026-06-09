package com.gtasatutoymas.txdw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gtasatutoymas.txdw.txd.TXDHeader;
import com.gtasatutoymas.txdw.txd.TXDListAdapter;
import com.gtasatutoymas.txdw.txd.TXDWorkshop;
import com.rw.texture.DXTCompress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    Bitmap bmp;
    ImageView ivTexture;
    ListView lvTextures;
    TXDWorkshop txd;

    public final int result = 0;
    boolean isOpen = false;
    int position = 0;
    String nameTexture = "";
    File current = new File("/");

    // -------------------------------------------------------------------------
    // Activity lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);

        File txdwDir = new File(Environment.getExternalStorageDirectory() + "/txdw/");
        if (!txdwDir.exists()) {
            ExtractResources();
            Options(1, true);
        }

        ivTexture = (ImageView) findViewById(R.id.ivTexture);
        lvTextures = (ListView) findViewById(R.id.textures);

        setupButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == 0) {
            if (intent != null) {
                Uri data = intent.getData();
                if (data != null) {
                    loadTexture(data.getPath());
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Button setup
    // -------------------------------------------------------------------------

    private void setupButtons() {
        ((Button) findViewById(R.id.newtxdbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileDialogTXDNew();
            }
        });

        ((Button) findViewById(R.id.open)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(
                            new Intent(getApplicationContext(), Class.forName("com.gtasatutoymas.txdw.File_Browser")),
                            0);
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        });

        ((Button) findViewById(R.id.exportar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noOpen), Toast.LENGTH_SHORT).show();
                } else {
                    FileDialogTXDExport();
                }
            }
        });

        ((Button) findViewById(R.id.importar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noOpen), Toast.LENGTH_SHORT).show();
                } else {
                    FileDialogTXDImport();
                }
            }
        });

        ((Button) findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noOpen), Toast.LENGTH_SHORT).show();
                } else {
                    txd.SaveTXD();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((Button) findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((Button) findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Class.forName("com.gtasatutoymas.txdw.HelpActivity"));
                    intent.putExtra("result", "h");
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        });

        ((Button) findViewById(R.id.about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Class.forName("com.gtasatutoymas.txdw.HelpActivity"));
                    intent.putExtra("result", "a");
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // Public methods – same names as original (public API preserved)
    // -------------------------------------------------------------------------

    public void runGame(View view) {
        try {
            startActivity(getApplication().getPackageManager().getLaunchIntentForPackage("com.rockstargames.gtasa"));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Game not found", Toast.LENGTH_LONG).show();
        }
    }

    public void RenderView(View view) {
        try {
            startActivity(new Intent(getApplicationContext(), Class.forName("com.gtasatutoymas.txdw.RendererActivity")));
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public void reload() {
        loadTexture(txd.getpath());
    }

    public void loadTexture(final String path) {
        isOpen = true;
        txd = new TXDWorkshop(path, getApplicationContext());

        if (txd.getNumTextures() != 0) {
            if (txd.isCorrupt()) return;

            lvTextures.setAdapter((android.widget.ListAdapter)
                    new TXDListAdapter(getApplicationContext(), txd.getStringTextures(), path));

            bmp = txd.getImageDecompress(0);
            ivTexture.setImageBitmap(bmp);
            nameTexture = txd.getName(0);
            setTitle("TXD Workshop - " + new File(path).getName() + " -> " + nameTexture);

            lvTextures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    nameTexture = txd.getName(i);
                    position = i;
                    setTitle("TXD Workshop - " + new File(path).getName() + " -> " + nameTexture);
                    bmp = txd.getImageDecompress(i);
                    ivTexture.setImageBitmap(bmp);
                }
            });

            lvTextures.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (!txd.isWritable()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.nw), Toast.LENGTH_LONG).show();
                    } else {
                        Options(i, false);
                    }
                    return true;
                }
            });

        } else {
            if (txd.isCorrupt()) {
                isOpen = false;
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noTextures), Toast.LENGTH_LONG).show();
            }

            lvTextures.setAdapter((android.widget.ListAdapter) new ArrayAdapter<>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    new String[]{getResources().getString(R.string.importtex)}));

            lvTextures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    Toast.makeText(getApplicationContext(), "Importa una textura no seas ...", Toast.LENGTH_SHORT).show();
                }
            });

            lvTextures.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                    Toast.makeText(getApplicationContext(), "Por que presionas si Esta vacio?", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

    public boolean isConected() {
        boolean connected = false;
        NetworkInfo[] allNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public void ExtractResources() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/txdw/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            AssetManager assets = getResources().getAssets();
            String[] list = assets.list("render");
            for (int i = 0; i < list.length; i++) {
                CopyToFolder(assets.open("render/" + list[i]), list[i]);
            }
        } catch (IOException e) {
            // silently ignore
        }
    }

    public void CopyToFolder(InputStream inputStream, String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(
                    Environment.getExternalStorageDirectory() + "/txdw/" + fileName);
            byte[] buf = new byte[inputStream.available()];
            inputStream.read(buf);
            out.write(buf);
            inputStream.close();
            out.close();
        } catch (IOException e) {
            // silently ignore
        }
    }

    public void Options(final int pos, boolean isWelcome) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        if (isWelcome) {
            dialog.setTitle(getResources().getString(R.string.welcome));
            dialog.setMessage(getResources().getString(R.string.msg1) + "\n" + getResources().getString(R.string.msg2));
            dialog.setButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.list_options, (ViewGroup) null);
            dialog.setView(viewInflate);
            dialog.setTitle(txd.getName(pos));

            ListOptions listOptions = new ListOptions(getApplicationContext(), new String[]{
                    getResources().getString(R.string.edit),
                    getResources().getString(R.string.rename),
                    getResources().getString(R.string.delete)
            });

            ListView listView = (ListView) viewInflate.findViewById(R.id.list_options);
            listView.setAdapter((android.widget.ListAdapter) listOptions);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (i == 0) {
                        editar(pos);
                    } else if (i == 1) {
                        renombrar(pos, false);
                    } else if (i == 2) {
                        txd.removeTexture(pos);
                        reload();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }

        dialog.show();
    }

    public void editar(final int pos) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.edit_dialog, (ViewGroup) null);
        dialog.setView(viewInflate);
        dialog.setTitle(getResources().getString(R.string.selectparams));

        final EditText editText = (EditText) viewInflate.findViewById(R.id.editdialogname);
        editText.setText(txd.getName(pos));

        ((TextView) viewInflate.findViewById(R.id.edit_dialogdimens))
                .setText(getResources().getString(R.string.size) + " " + txd.getDimens(pos));
        ((TextView) viewInflate.findViewById(R.id.edit_dialograster))
                .setText("Raster: " + txd.getRaster(pos));
        ((TextView) viewInflate.findViewById(R.id.edit_dialogcomp))
                .setText(txd.getCompression(pos));

        TextView alphaView = (TextView) viewInflate.findViewById(R.id.edit_dialogalpha);
        if (txd.getAlphaIV(pos) != 0) {
            alphaView.setTextColor(-65536);
            alphaView.setText(getResources().getString(R.string.a));
        } else {
            alphaView.setText(getResources().getString(R.string.wa));
        }

        dialog.setButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                String newName = editText.getText().toString();
                if (newName.length() <= 32) {
                    txd.editTexture(newName, pos);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.edited), Toast.LENGTH_LONG).show();
                    reload();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.only_chr), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void renombrar(final int pos, boolean showAbout) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        if (!showAbout) {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.rename, (ViewGroup) null);
            dialog.setView(viewInflate);
            dialog.setTitle(getResources().getString(R.string.rename));

            final EditText editText = (EditText) viewInflate.findViewById(R.id.renameEditText1);
            editText.setText(txd.getName(pos));

            dialog.setButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    String newName = editText.getText().toString();
                    if (newName.length() <= 32) {
                        txd.editTexture(newName, pos);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.renamed), Toast.LENGTH_LONG).show();
                        reload();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.only_chr), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.about, (ViewGroup) null);
            WebView webView = (WebView) viewInflate.findViewById(R.id.aboutWebView);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("file:///android_asset/" + getResources().getString(R.string.filea));
            dialog.setView(viewInflate);
        }

        dialog.show();
    }

    public void Iniciate(String imagePath, String fileName) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.add_dialog, (ViewGroup) null);
        dialog.setView(viewInflate);
        dialog.setTitle(getResources().getString(R.string.selectparams));

        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.addiv);
        final Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmapDecodeFile);

        final CheckBox isAlpha = (CheckBox) viewInflate.findViewById(R.id.isAlpha);
        final CheckBox isCompressed = (CheckBox) viewInflate.findViewById(R.id.compressEnabled);

        final String baseName = fileName.substring(0, fileName.indexOf("."));
        ((TextView) viewInflate.findViewById(R.id.addname))
                .setText(baseName + " " + bitmapDecodeFile.getWidth() + "X" + bitmapDecodeFile.getHeight());

        dialog.setButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                if (!txd.isWritable()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.nw), Toast.LENGTH_LONG).show();
                    return;
                }

                int width = bitmapDecodeFile.getWidth();
                int height = bitmapDecodeFile.getHeight();

                if (width > 1024 || height > 1024) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.outrange), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidTextureDimension(width, height)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.failimg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (baseName.length() > 32) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.only_chr), Toast.LENGTH_SHORT).show();
                    return;
                }

                TXDHeader header = new TXDHeader();
                header.filterFlags = 4358;
                header.uWrap = 0;
                header.vWrap = 0;
                header.platform = 9;
                header.width = width;
                header.height = height;
                header.mipmapCount = 1;
                header.diffuseName = baseName;

                if (isCompressed.isChecked()) {
                    header.bpp = 16;
                    header.rasterType = 4;
                    int[] pixels = new int[width * height];
                    bitmapDecodeFile.getPixels(pixels, 0, width, 0, 0, width, height);

                    if (isAlpha.isChecked()) {
                        header.alphaUsed = true;
                        header.alphaCompress = 9;
                        header.alphaName = baseName + "a";
                        header.Compression = 3;
                        header.rasterFormat = 768;
                        DXTCompress dxt = new DXTCompress(pixels, width, height, 3);
                        dxt.Compress();
                        header.data = dxt.getCompressedData();
                        header.dataSize = dxt.getCompressedData().length;
                    } else {
                        header.alphaUsed = false;
                        header.alphaCompress = 8;
                        header.alphaName = "";
                        header.Compression = 1;
                        header.rasterFormat = 512;
                        DXTCompress dxt = new DXTCompress(pixels, width, height, 1);
                        dxt.Compress();
                        header.data = dxt.getCompressedData();
                        header.dataSize = dxt.getCompressedData().length;
                    }
                    header.nativeSize = 116 + header.dataSize;
                    header.structSize = header.nativeSize - 24;

                } else {
                    header.bpp = 32;
                    header.rasterType = 4;
                    int[] pixels = new int[width * height];
                    bitmapDecodeFile.getPixels(pixels, 0, width, 0, 0, width, height);

                    if (isAlpha.isChecked()) {
                        header.alphaUsed = true;
                        header.alphaCompress = 1;
                        header.alphaName = baseName + "a";
                        header.Compression = 0;
                        header.rasterFormat = 3840;
                        byte[] buf = new byte[width * height * 4];
                        int idx = 0;
                        for (int px : pixels) {
                            buf[idx++] = (byte) Color.red(px);
                            buf[idx++] = (byte) Color.green(px);
                            buf[idx++] = (byte) Color.blue(px);
                            buf[idx++] = (byte) Color.alpha(px);
                        }
                        header.data = buf;
                        header.dataSize = buf.length;
                    } else {
                        header.alphaUsed = false;
                        header.alphaCompress = 0;
                        header.alphaName = "";
                        header.Compression = 0;
                        header.rasterFormat = 1536;
                        byte[] buf = new byte[width * height * 4];
                        int idx = 0;
                        for (int px : pixels) {
                            buf[idx++] = (byte) Color.blue(px);
                            buf[idx++] = (byte) Color.green(px);
                            buf[idx++] = (byte) Color.red(px);
                            buf[idx++] = (byte) 0xFF;
                        }
                        header.data = buf;
                        header.dataSize = buf.length;
                    }
                    header.nativeSize = 116 + header.dataSize;
                    header.structSize = header.nativeSize - 24;
                }

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.imported), Toast.LENGTH_SHORT).show();
                txd.addTexture(header);
                reload();
            }
        });

        dialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void SavePNG(String dir, boolean temp) {
        try {
            File file;
            if (temp) {
                file = new File(dir + "/tmp.png");
            } else {
                file = new File(dir + "/" + nameTexture + ".png");
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exported), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.noexported) + " Exception: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void NewTXD(final String path) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.new_dialog, (ViewGroup) null);
        dialog.setView(viewInflate);
        dialog.setTitle(getResources().getString(R.string.selectname));

        ((TextView) viewInflate.findViewById(R.id.new_dialogpath))
                .setText(getResources().getString(R.string.path) + " " + path);

        final EditText et = (EditText) viewInflate.findViewById(R.id.new_dialogname);

        dialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                String fullPath = path + "/" + et.getText().toString() + ".txd";
                txd = new TXDWorkshop(fullPath);
                txd.setContext(getApplicationContext());
                txd.NewTXD(fullPath);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.created), Toast.LENGTH_SHORT).show();
                reload();
            }
        });

        dialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void FileDialogTXDNew() {
        showFileDialog(
                getResources().getString(R.string.selectdirnew),
                false,
                new FileDialogCallback() {
                    @Override
                    public void onDirectorySelected(String path) {
                        NewTXD(path);
                    }
                    @Override
                    public void onFileSelected(String path, String name) { /* unused */ }
                });
    }

    public void FileDialogTXDImport() {
        showFileDialog(
                getResources().getString(R.string.selectimg),
                true,
                new FileDialogCallback() {
                    @Override
                    public void onDirectorySelected(String path) { /* unused */ }
                    @Override
                    public void onFileSelected(String path, String name) {
                        Iniciate(path, name);
                    }
                });
    }

    public void FileDialogTXDExport() {
        showFileDialog(
                getResources().getString(R.string.selectdir),
                false,
                new FileDialogCallback() {
                    @Override
                    public void onDirectorySelected(String path) {
                        SavePNG(path, false);
                    }
                    @Override
                    public void onFileSelected(String path, String name) { /* unused */ }
                });
    }

    // -------------------------------------------------------------------------
    // Inner class – file browser list adapter
    // -------------------------------------------------------------------------

    public class ListLines2 extends ArrayAdapter {
        Context c;
        File dirs;

        public ListLines2(MainActivity mainActivity, Context context, String[] strArr, File file) {
            super(context, R.layout.list_item, strArr);
            this.c = context;
            this.dirs = file;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.list_item, (ViewGroup) null);
            String str = (String) getItem(i);
            TextView textView = (TextView) viewInflate.findViewById(R.id.listitemTextView);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.listitemImageView1);
            File file = new File(dirs + "/" + str);

            if (file.isDirectory()) {
                imageView.setImageResource(R.drawable.fsm_folder);
            } else if (str.endsWith(".img")) {
                imageView.setImageResource(R.drawable.img_img);
            } else if (str.endsWith(".dff")) {
                imageView.setImageResource(R.drawable.img_dff);
            } else if (str.endsWith(".ifp")) {
                imageView.setImageResource(R.drawable.img_ifp);
            } else if (str.endsWith(".fxp")) {
                imageView.setImageResource(R.drawable.img_fxp);
            } else if (str.endsWith(".gxt")) {
                imageView.setImageResource(R.drawable.img_gxt);
            } else if (str.endsWith(".txd")) {
                imageView.setImageResource(R.drawable.img_txd);
            } else if (str.endsWith(".ipl")) {
                imageView.setImageResource(R.drawable.img_ipl);
            } else if (str.endsWith(".col")) {
                imageView.setImageResource(R.drawable.img_col);
            } else if (str.endsWith("png") || str.endsWith("jpg")) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                imageView.setImageResource(R.drawable.fsm_unknown);
            }

            textView.setText(str);
            return viewInflate;
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true if (w x h) is a valid power-of-two texture size accepted by
     * GTA SA (square or 2:1 / 1:2 aspect ratios up to 1024).
     */
    private boolean isValidTextureDimension(int w, int h) {
        int size = 1024;
        for (int i = 0; i < 10; i++) {
            if (w == size && h == size)         return true;  // square
            if (w == size && h == size / 2)     return true;  // landscape 2:1
            if (w == size / 2 && h == size)     return true;  // portrait 1:2
            size /= 2;
        }
        return false;
    }

    /** Callback interface used by the unified file-dialog helper. */
    private interface FileDialogCallback {
        void onDirectorySelected(String path);
        void onFileSelected(String path, String name);
    }

    /**
     * Generic file-browser dialog.
     *
     * @param title      Dialog title string.
     * @param pickFiles  When true the dialog lets the user pick PNG/JPG files;
     *                   when false only directories can be selected via the OK button.
     * @param callback   Called when the user confirms their selection.
     */
    private void showFileDialog(String title, final boolean pickFiles, final FileDialogCallback callback) {
        final File root = new File(Environment.getExternalStorageDirectory() + "/");
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.file_dialog, (ViewGroup) null);
        dialog.setView(viewInflate);
        dialog.setTitle(title);

        current = root;
        final ListView listView = (ListView) viewInflate.findViewById(R.id.filedialog);
        listView.setAdapter((android.widget.ListAdapter)
                new ListLines2(this, getApplicationContext(), root.list(), root));

        ((Button) viewInflate.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File parent = new File(current.getParent());
                    current = parent;
                    listView.setAdapter((android.widget.ListAdapter)
                            new ListLines2(MainActivity.this, getApplicationContext(), parent.list(), parent));
                } catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });

        // OK button – only meaningful for directory-picking dialogs
        dialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                try {
                    if (current.isDirectory()) {
                        callback.onDirectorySelected(current.getAbsolutePath());
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                File selected = new File(current + "/" + (String) adapterView.getItemAtPosition(i));
                if (selected.isDirectory()) {
                    current = selected;
                    listView.setAdapter((android.widget.ListAdapter)
                            new ListLines2(MainActivity.this, getApplicationContext(), selected.list(), selected));
                } else if (pickFiles) {
                    String name = selected.getName();
                    if (name.endsWith("png") || name.endsWith("jpg")) {
                        callback.onFileSelected(selected.getAbsolutePath(), name);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_png), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }
}
