package com.samuel.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
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
import com.samuel.common.Base64Util;
import com.samuel.common.http.HttpGetTask;
import com.samuel.common.worklog.WorklogManage;
import com.samuel.mytools.R;

/**
 * ͼ��ʶ�𹤾߽ӿ�
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

        // ���հ�ť
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

        // ͼ��ʶ�����
        Button buttonDetectPhoto = (Button) findViewById(R.id.button_check);
        buttonDetectPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAccessToken)) {
                    Toast.makeText(PhotoDetectActivity.this, "���Ȼ�ȡ��������", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mPhotoBitmap == null) {
                    return;
                }

                TextView tvResult = (TextView) findViewById(R.id.text_check_result);
                tvResult.setText("������ģ��ʶ���С���");

                double threshold = 0.7; // ������ֵ
                // ��ȡͼƬ��base64����
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

        // ��ȡͼƬ����
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
        textViewTitle.setText("ͼƬʶ��ӿ�");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetectActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("��������");
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                String url = "https://blog.csdn.net/samuelzhou99/article/details/80493640";
//                url = "https://pan.baidu.com/s/1BDzJWG7p3S1PfeebAJJbqA";
                // ��ȡAccess Token
                ProtocolHandler mHandler = new ProtocolHandler();
                HttpGetTask mHttpTask = new HttpGetTask(mHandler);
                mHttpTask.execute("AccessToken");
            }
        });
    }

    private void initData() {
        mSkuMaps.clear();
        mSkuMaps.put("moli_baicha", "��¶�ྡྷ������ײ�");
        mSkuMaps.put("longjin_bingshuan", "��¶���ˬ����(����)");
        mSkuMaps.put("bohe_bingshuan", "��¶���ˬ����");
        mSkuMaps.put("fangzhu_bohe", "��¶�೬ǿ����");
        mSkuMaps.put("guiyuan_lianzi", "���ع�Բ����");
        mSkuMaps.put("huasheng", "���ػ���ţ��");
        mSkuMaps.put("guiyuan", "���ع�Բ�˱���");
        mSkuMaps.put("ditang_lianzi", "���ص�������");
        mSkuMaps.put("hongdouzhou", "�����޲�ʺ춹��");
    }

    private void processPhoto(JSONArray array) {
        if (array == null || array.length() == 0) {
            TextView tvResult = (TextView) findViewById(R.id.text_check_result);
            tvResult.setText("���ݽ������, û����Ч����");
            return;
        }

        try {
            StringBuffer sb = new StringBuffer(100);
            ArrayList<ItemMark> itemList = new ArrayList<ItemMark>(array.length());
            // �������༭��ͼƬ�ͻ�������
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
                sb.append(String.format(", ���Ŷ�: %.3f", itemMark.getScore()));
                if (i < array.length() - 1) {
                    sb.append("\n");
                }

                // ��ͼƬ�Ͻ��б�ע, ���ƾ��ο��������ʾ
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

            // ��ʾ�ı�
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
                    Toast.makeText(PhotoDetectActivity.this, "��������: " + tokenStr, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class DetectionHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.obj != null) {
                // ��������Ӧ������ݼ�
                String result = msg.obj.toString();
                if (!TextUtils.isEmpty(result)) {
                    WorklogManage.saveWorklog(3, 0, result, 1);

                    // �������ݼ�, ��ʶ�𵽵ı�ǩ��ע��ͼƬ������λ������
                    JSONObject resultObj = (JSONObject) msg.obj;
                    try {
                        processPhoto(resultObj.getJSONArray("results"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    TextView tvResult = (TextView) findViewById(R.id.text_check_result);
                    tvResult.setText("���ݽ����쳣");
                }
            }
        }
    }
}
