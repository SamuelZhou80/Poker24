package com.samuel.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.FileManager;
import com.framework.http.HttpGetTask;
import com.samuel.common.Constant;
import com.samuel.debug.worklog.WorklogManage;
import com.samuel.mytools.R;
import com.samuel.utils.Base64Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 图像识别工具接口
 * 
 * @author Administrator
 *
 */
public class PhotoDetectActivity extends Activity {
    private static final int TAKE_PICTURE = 100;
    private static final String PHOTO_FILE_PATH = Constant.FILE_IMAGE_DIR + "photo.jpg";
    private long mPhotoSize;
    private String mAccessToken = "";
    // private String mPhotoBase64Str = "";
    private Map<String, String> mSkuMaps = new HashMap<String, String>();
    private Bitmap mPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detect_layout);

        initTitleView();
        initData();

        // 拍照按钮
        Button buttonTakePhoto = (Button) findViewById(R.id.button_takephoto);
        buttonTakePhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPhotoSize = FileManager.getFileSize(PHOTO_FILE_PATH);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_FILE_PATH)));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        // 图像识别入口
        Button buttonDetectPhoto = (Button) findViewById(R.id.button_check);
        buttonDetectPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAccessToken)) {
                    Toast.makeText(PhotoDetectActivity.this, "请先获取访问令牌", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mPhotoBitmap == null) {
                    return;
                }

                TextView tvResult = (TextView) findViewById(R.id.text_check_result);
                tvResult.setText("物体检测模型识别中……");

                double threshold = 0.7; // 检测的阈值
                // 获取图片的base64数据
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                mPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
                String photoBase64Str = Base64Util.encode(baos.toByteArray());
                DetectionHandler mHandler = new DetectionHandler();
                HttpGetTask mHttpTask = new HttpGetTask(mHandler);
                mHttpTask.execute(HttpGetTask.GET_OBJECT_DETECTION, mAccessToken, photoBase64Str, threshold);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhotoBitmap != null) {
            mPhotoBitmap.recycle();
            mPhotoBitmap = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filePath = PHOTO_FILE_PATH;
        if (mPhotoSize == FileManager.getFileSize(filePath)) {
            return;
        }

        // 获取图片数据
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 2;
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inJustDecodeBounds = false;
        mPhotoBitmap = BitmapFactory.decodeFile(filePath, op);
        if (mPhotoBitmap == null) {
            return;
        }

        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    getContentResolver().delete(uri, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ImageView imageView = (ImageView) findViewById(R.id.image_photo);
        if (mPhotoBitmap != null && !mPhotoBitmap.isRecycled()) {
            imageView.setImageBitmap(mPhotoBitmap);
        }
    }

    private void initTitleView() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("图片识别接口");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetectActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("访问令牌");
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                String url = "https://blog.csdn.net/samuelzhou99/article/details/80493640";
//                url = "https://pan.baidu.com/s/1BDzJWG7p3S1PfeebAJJbqA";
                // 获取Access Token
                ProtocolHandler mHandler = new ProtocolHandler();
                HttpGetTask mHttpTask = new HttpGetTask(mHandler);
                mHttpTask.execute("AccessToken");
            }
        });
    }

    private void initData() {
        mSkuMaps.clear();
        mSkuMaps.put("moli_baicha", "高露洁劲白茉莉白茶");
        mSkuMaps.put("longjin_bingshuan", "高露洁冰爽茶香(龙井)");
        mSkuMaps.put("bohe_bingshuan", "高露洁冰爽薄荷");
        mSkuMaps.put("fangzhu_bohe", "高露洁超强牙膏");
        mSkuMaps.put("guiyuan_lianzi", "银鹭桂圆莲子");
        mSkuMaps.put("huasheng", "银鹭花生牛奶");
        mSkuMaps.put("guiyuan", "银鹭桂圆八宝粥");
        mSkuMaps.put("ditang_lianzi", "银鹭低糖莲子");
        mSkuMaps.put("hongdouzhou", "好粥道薏仁红豆粥");
    }

    private void processPhoto(JSONArray array) {
        if (array == null || array.length() == 0) {
            TextView tvResult = (TextView) findViewById(R.id.text_check_result);
            tvResult.setText("数据解析完毕, 没有有效数据");
            return;
        }

        try {
            StringBuffer sb = new StringBuffer(100);
            ArrayList<ItemMark> itemList = new ArrayList<ItemMark>(array.length());
            // 创建待编辑的图片和画布对象
            Bitmap resizeBitmap = mPhotoBitmap.copy(Config.RGB_565, true);
            Canvas mCanvas = new Canvas(resizeBitmap);
            Paint paint = new Paint();
            paint.setStrokeWidth(5);

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                JSONObject locationObj = item.optJSONObject("location");
                ItemMark itemMark = new ItemMark();
                itemMark.setTag(item.optString("name"));
                itemMark.setScore(item.optDouble("score"));
                int left = locationObj.getInt("left");
                int top = locationObj.getInt("top");
                int width = locationObj.getInt("width");
                int height = locationObj.getInt("height");
                String name = mSkuMaps.get(itemMark.getTag());
                if (name != null) {
                    itemMark.setName(name);
                }
                itemList.add(itemMark);
                sb.append(i + 1);
                sb.append(". ");
                sb.append(itemMark.getName());
                sb.append(String.format(", 置信度: %.3f", itemMark.getScore()));
                if (i < array.length() - 1) {
                    sb.append("\n");
                }

                // 在图片上进行标注, 绘制矩形框和名称提示
                mCanvas.drawLine(left, top, left + width, top, paint);
                mCanvas.drawLine(left + width, top, left + width, top + height, paint);
                mCanvas.drawLine(left, top, left, top + height, paint);
                mCanvas.drawLine(left, top + height, left + width, top + height, paint);
                
                TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
                textPaint.setTextSize(25 * mPhotoBitmap.getWidth() / 480);
                textPaint.setTypeface(Typeface.DEFAULT);
                textPaint.setColor(Color.RED);
                mCanvas.drawText(itemMark.getName(), left, top, textPaint);
            }
            mCanvas.save();
            mCanvas.restore();
            if (mCanvas != null) {
                mCanvas = null;
            }
            mPhotoBitmap.recycle();
            mPhotoBitmap = resizeBitmap;
            ImageView imageView = (ImageView) findViewById(R.id.image_photo);
            imageView.setImageBitmap(mPhotoBitmap);

            // 显示文本
            TextView tvResult = (TextView) findViewById(R.id.text_check_result);
            tvResult.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ProtocolHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.obj != null) {
                String tokenStr = (String) msg.obj;
                if (!TextUtils.isEmpty(tokenStr)) {
                    mAccessToken = tokenStr;
                    Toast.makeText(PhotoDetectActivity.this, "访问令牌: " + tokenStr, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class DetectionHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.obj != null) {
                // 保存中心应答的数据集
                String result = msg.obj.toString();
                if (!TextUtils.isEmpty(result)) {
                    WorklogManage.saveWorklog(3, 0, result, 1);

                    // 解析数据集, 将识别到的标签标注在图片上描绘出位置轮廓
                    JSONObject resultObj = (JSONObject) msg.obj;
                    try {
                        processPhoto(resultObj.getJSONArray("results"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    TextView tvResult = (TextView) findViewById(R.id.text_check_result);
                    tvResult.setText("数据解析异常");
                }
            }
        }
    }
}
