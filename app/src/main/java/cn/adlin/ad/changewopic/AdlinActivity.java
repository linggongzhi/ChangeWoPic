package cn.adlin.ad.changewopic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.adlin.ad.tools.EditTextWithAdd;
import cn.adlin.ad.tools.EditTextWithAddInterFace;

public class AdlinActivity extends AppCompatActivity implements View.OnClickListener, EditTextWithAddInterFace {
    protected static final String MTAG = "ImageAccording";
    private String path1 = Environment.getExternalStorageDirectory()
            + File.separator + "client/tile/1.jpg";
    private Button showPicInf;
    private Button showPicInfBlu;
    private Button showPicInfSqu;
    private Button showPicInfRad;
    String exifPicPath = null;
    private EditTextWithAdd selectValues;// 选
    private String pathCode;
    private  Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adlin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        showPicInf = (Button) findViewById(R.id.jpgBtn);
        showPicInf.setOnClickListener(this);
        showPicInfBlu = (Button) findViewById(R.id.jpgBluBtn);
        showPicInfBlu.setOnClickListener(this);
        showPicInfSqu = (Button) findViewById(R.id.jpgSquBtn);
        showPicInfSqu.setOnClickListener(this);
        showPicInfRad = (Button) findViewById(R.id.jpgRadBtn);
        showPicInfRad.setOnClickListener(this);

        selectValues = (EditTextWithAdd) findViewById(R.id.editTextWan);
        selectValues.setActivity(this);

        List<String> onPaths = getExtSDCardPaths();
        for (int i = 0; i < onPaths.size(); i++) {
            Log.i(MTAG, "sd卡路径：" + onPaths.toString());
        }

//        Resources res = getResources();
//        Drawable drawable = res.getDrawable(R.mipmap.gg);
//        this.getWindow().setBackgroundDrawable(drawable);

        init();
    }

    private void init() {
        exif(null, false);

        ImageView picView1 = (ImageView) findViewById(R.id.imageview);
//        String myPicPath1 = "/storage/sdcard1/gg.png";
//        String appPicPath = "/assets/gg.png";
//        File mFile = new File(path1);
//        File mFile1 = new File(myPicPath1);
//        File mFile2 = new File(appPicPath);
//        Bitmap bitmap = null;
//        // 若该文件存在
//        if (mFile.exists()) {
//            bitmap = BitmapFactory.decodeFile(path1);
//            Log.i(MTAG, "sd卡图片路径path1：" + mFile.exists());
//        }else if(mFile1.exists()){
//            bitmap = BitmapFactory.decodeFile(myPicPath1);
//            Log.i(MTAG, "sd卡图片路径myPicPath1：" + mFile1.exists());
//        }
//        else if(mFile2.exists()){
//            bitmap = BitmapFactory.decodeFile(appPicPath);
//            Log.i(MTAG, "sd卡图片路径myPicPath1：" + mFile2.exists());
//        }
//        // 为Bitmap添加水印
//        Bitmap bitmap1 = createWatermark(bitmap, "@adlin");
//        picView1.setImageBitmap(bitmap1);
        //        获取assets下的文件，apk安装以后放在/data/app/**.apk，以apk形式存在，asset/res和apk在一起，
//        并不会解压到/data/data/YourApp目录下去，所以你无法直接获取到assets的绝对路径，因为他根本木有，
//        只能以流的形式读取。
        AssetManager am = getAssets();
        InputStream is = null;
        Bitmap B = null;
        try {
            is = am.open("gg2.jpg");
            Drawable d =Drawable.createFromStream(is, null);
            B = ((BitmapDrawable) d).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        exifPicPath = "/storage/sdcard1/gg2.jpg";
        File file = new File(exifPicPath);
        if (!file.exists())
        {
            exifPicPath = "/storage/emulated/0/gg2.jpg";
            file = new File(exifPicPath);
            {
                if (!file.exists()){
                    try {
                        saveFile(B, "gg2.jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    exifPicPath = ALBUM_PATH + "gg2.jpg";
                    selectValues.setText(exifPicPath);
                    picView1.setImageBitmap(B);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adlin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * @return List<String>
     * @throws IOException
     * @Title: getExtSDCardPaths
     * @Description: to obtain storage paths, the first path is theoretically
     * the returned value of
     * Environment.getExternalStorageDirectory(), namely the
     * primary external storage. It can be the storage of internal
     * device, or that of external sdcard. If paths.size() >1,
     * basically, the current device contains two type of storage:
     * one is the storage of the device itself, one is that of
     * external sdcard. Additionally, the paths is directory.
     */
    public static List<String> getExtSDCardPaths() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.endsWith(Environment.MEDIA_UNMOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of ‘mount‘, to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paths;
    }

    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/changepic/";

    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    private void exif(String casually, boolean cb) {
        /*
         * 目前Android SDK定义的Tag有: TAG_DATETIME 时间日期 TAG_FLASH 闪光灯
		 * TAG_GPS_LATITUDE 纬度 TAG_GPS_LATITUDE_REF 纬度参考 TAG_GPS_LONGITUDE 经度
		 * TAG_GPS_LONGITUDE_REF 经度参考 TAG_IMAGE_LENGTH 图片长 TAG_IMAGE_WIDTH 图片宽
		 * TAG_MAKE 设备制造商 TAG_MODEL 设备型号 TAG_ORIENTATION 方向 TAG_WHITE_BALANCE
		 * 白平衡
		 */

        try {
            // android读取图片EXIF信息
            ExifInterface exifInterface = new ExifInterface(exifPicPath);
            String smodel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            String width = exifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String height = exifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            if (cb == false) {
                Toast.makeText(AdlinActivity.this,
                        smodel + "  " + width + "*" + height, Toast.LENGTH_LONG)
                        .show();
            }
            Log.i(MTAG, "sd卡图片TAG_MODEL：" + smodel);

            if (casually != null) {
                // 设置信息 tag可以自定义
                exifInterface.setAttribute(ExifInterface.TAG_MODEL, casually);
                exifInterface.saveAttributes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 为图片target添加水印文字
    // Bitmap target：被添加水印的图片
    // String mark：水印文章
    private Bitmap createWatermark(Bitmap target, String mark) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.RED);

        // 水印的字体大小
        p.setTextSize(50);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在左边的中间位置开始添加水印
        canvas.drawText(mark, 0, h/2, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.jpgBtn) {
            exif(null, false);
            return;
        }
        if (v.getId() == R.id.jpgBluBtn) {
            exif("Canon", true);
            return;
        }
        if (v.getId() == R.id.jpgSquBtn) {
            exif("Panasonic", true);
            return;
        }
        if (v.getId() == R.id.jpgRadBtn) {
            exif("NIKON D5300", true);
            return;
        }
    }

    @Override
    public void addButtonListener() {
        // TODO Auto-generated method stub
        Toast.makeText(AdlinActivity.this, "点击了加号来也……", Toast.LENGTH_LONG).show();
        selectValues.setText("巡逻……");
        chooseFile();
    }

    private static final int FILE_SELECT_CODE = 0;

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub.
        if (resultCode != Activity.RESULT_OK) {
            Log.e(MTAG, "onActivityResult() error, resultCode: " + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            Log.i(MTAG, "------->" + uri.getPath());
            pathCode = uri.getPath();
            if (pathCode != null) {
                pathCode = getRealPathFromURI(uri);
                selectValues.setText(pathCode);

                ImageView picView1 = (ImageView) findViewById(R.id.imageview);
                exifPicPath = pathCode;
                File f = new File(exifPicPath);
                if (f.exists()) { /* 产生Bitmap对象，并放入mImageView中 */
                    Bitmap bm = BitmapFactory.decodeFile(exifPicPath);
                    picView1.setImageBitmap(bm);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(AdlinActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
