package com.samuel.main.eco;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.main.Constant;
import com.samuel.main.FileManager;
import com.samuel.main.GpsUtils;
import com.samuel.mytools.R;
import com.samuel.utils.OpenFileUtil;

public class EcoAnalyzerActivity extends Activity {
    private Button mButtonCalc;
    private TextView mResultDetail;
    /**
     * �ٶ���������ֵ
     */
    public static final int SPEED_RANGE_MAX = 16;
    private static int roadTypeNum = 12;// ·����������
    private static int speedRangeNum = SPEED_RANGE_MAX + 1;// �ٶ���������
    private double[][] mDistanceArray = new double[roadTypeNum][speedRangeNum];
    private double[][] mSelfDistanceArray = new double[roadTypeNum][speedRangeNum];
    private double[][] mFuelArray = new double[roadTypeNum][speedRangeNum];
    private double[][] mSelfFuelArray = new double[roadTypeNum][speedRangeNum];
    private double[][] mAverAppArray = new double[roadTypeNum][speedRangeNum];
    private int[][] mDriveTimeArray = new int[roadTypeNum][speedRangeNum];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_analyzer);

        initTitle();

        mButtonCalc = (Button) findViewById(R.id.button_check);
        mButtonCalc.setOnClickListener(checkClickListener);
        mButtonCalc.setText("���������");
        Button buttonReset = (Button) findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(fuelStatListener);
        buttonReset.setText("�����ͺ���־");

        mResultDetail = (TextView) findViewById(R.id.text_result_detail);
        mResultDetail.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                String path = OpenFileUtil.getPath(EcoAnalyzerActivity.this, uri);
                String logStr;
                if (path == null) {
                    path = Constant.FILE_ECOLOG_DIR + "applog.txt";
                }
                File file = new File(path);
                logStr = FileManager.readFile(file, "UTF-16");
                if (TextUtils.isEmpty(logStr)) {
                    Toast.makeText(EcoAnalyzerActivity.this, path + " ��ʧ��", Toast.LENGTH_SHORT).show();
                    return;
                }
                parserLog(logStr);
            } catch (Exception e) {
                Toast.makeText(EcoAnalyzerActivity.this, "�����ļ��쳣", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("Eco���ݷ���");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EcoAnalyzerActivity.this.finish();
            }
        });

        Button rightBtn = (Button) findViewById(R.id.common_btn_right);
        rightBtn.setText("ѡ����־");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// ����������
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * �����������
     */
    private OnClickListener checkClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ʡ������
            double savedFuel = 0;
            double ecoTotalDistance = 0;
            double ecoDriveFuelTotal = 0; // �żݵ����ͺ�
            double selfDriveDistTotal = 0; // �Լݵ������
            double selfDriveFuelTotal = 0; // �Լݵ����ͺ�
            for (int i = 0; i < roadTypeNum; i++) {
                for (int j = 0; j < speedRangeNum; j++) {
                    // ȥ��û�н����ż�ģʽ��·��
                    if (mDistanceArray[i][j] <= mSelfDistanceArray[i][j]) {
                        continue;
                    }
                    selfDriveDistTotal += mSelfDistanceArray[i][j];
                    selfDriveFuelTotal += mSelfFuelArray[i][j];
                    ecoTotalDistance += mDistanceArray[i][j];
                    ecoDriveFuelTotal += mFuelArray[i][j];
                }
            }

            ecoTotalDistance = ecoTotalDistance - selfDriveDistTotal;
            ecoDriveFuelTotal = ecoDriveFuelTotal - selfDriveFuelTotal;

            // �����Լݺ��żݽ׶εİٹ����ͺ�
            double selfFuelHkm = 0;
            double ecoFuelHkm = 0;
            if (selfDriveDistTotal > 0) {
                selfFuelHkm = selfDriveFuelTotal * 100 / selfDriveDistTotal;
            }
            if (ecoTotalDistance > 0) {
                ecoFuelHkm = ecoDriveFuelTotal * 100 / ecoTotalDistance;
            }
            // ����ʡ������=[ͬ�ȹ���(ͬһ·�����͡�ͬһ�ٶ�����)���Լ��ڼ�ƽ���ٹ����ͺ�-�ż��ڼ��ƽ���ٹ����ͺ�]���ż���ʻ���
            savedFuel = (selfFuelHkm - ecoFuelHkm) * ecoTotalDistance / 100;
            String resultStr = "";
            resultStr += String.format("�Լ����%.2f,�ͺ�%.2f��,��ʻ�ͺ�%.2fL/hkm\n", selfDriveDistTotal, selfDriveFuelTotal, selfFuelHkm);
            resultStr += String.format("�ż����%.2f,�ͺ�%.2f��,��ʻ�ͺ�%.2fL/hkm\n", ecoTotalDistance, ecoDriveFuelTotal, ecoFuelHkm);
            resultStr += String.format("������=%.2f��", savedFuel);
            mResultDetail.setText(resultStr);
        }
    };

    /**
     * ��̺��ͺĵı��ͳ�ƽӿ�
     */
    private OnClickListener fuelStatListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // parserAppLog();

            String distanceLog = "";
            String fuelLog = "";
            String selfDistLog = "";
            String selfFuelLog = "";
            String timeLog = "";
            double selfDistTotal = 0, selfFuelTotal = 0;
            for (int i = 0; i < roadTypeNum; i++) {
                for (int j = 0; j < speedRangeNum; j++) {
                    distanceLog += String.format("%.6f,", mDistanceArray[i][j]);
                    fuelLog += String.format("%.6f,", mFuelArray[i][j]);
                    selfDistLog += String.format("%.6f,", mSelfDistanceArray[i][j]);
                    selfFuelLog += String.format("%.6f,", mSelfFuelArray[i][j]);
                    selfDistTotal += mSelfDistanceArray[i][j];
                    selfFuelTotal += mSelfFuelArray[i][j];
                    timeLog += String.valueOf(mDriveTimeArray[i][j]) + ',';
                }
                distanceLog += "\n";
                fuelLog += "\n";
                selfDistLog += "\n";
                selfFuelLog += "\n";
                timeLog += "\n";
            }
            // ���浽�ļ���
            FileManager.createDir(Constant.FILE_ECOLOG_DIR);
            String fileName = Constant.FILE_ECOLOG_DIR + "distance.csv";
            FileManager.writeFile(fileName, distanceLog);
            fileName = Constant.FILE_ECOLOG_DIR + "totalFuel.csv";
            FileManager.writeFile(fileName, fuelLog);
            fileName = Constant.FILE_ECOLOG_DIR + "selfDistance.csv";
            FileManager.writeFile(fileName, selfDistLog);
            fileName = Constant.FILE_ECOLOG_DIR + "selfFuel.csv";
            FileManager.writeFile(fileName, selfFuelLog);
            fileName = Constant.FILE_ECOLOG_DIR + "drivetime.csv";
            FileManager.writeFile(fileName, timeLog);
            Toast.makeText(EcoAnalyzerActivity.this, "��̺��ͺ�ͳ�����" + selfDistTotal + ",fuel=" + selfFuelTotal, Toast.LENGTH_LONG).show();
        }
    };

    private void parserLog(String logStr) {
        try {
            JSONArray array = new JSONArray(logStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int roadType = obj.getInt("roadType");
                int speedRange = obj.getInt("speedRange");
                double distance = obj.getDouble("distance");
                double selfDist = obj.getDouble("selfDist");
                // double oilHkm = obj.getDouble("hundredFuel");
                double fuel = obj.getDouble("totalFuel");
                double selfFuel = obj.getDouble("selfFuel");
                mDistanceArray[roadType][speedRange] = distance;
                mSelfDistanceArray[roadType][speedRange] = selfDist;
                mFuelArray[roadType][speedRange] = fuel;
                mSelfFuelArray[roadType][speedRange] = selfFuel;
                mDriveTimeArray[roadType][speedRange] = obj.optInt("usedTime");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void parserAppLog() {
        String path = Constant.FILE_ECOLOG_DIR + "applog.txt";
        File file = new File(path);
        String logStr = FileManager.readFile(file, "UTF-16");
        if (TextUtils.isEmpty(logStr)) {
            Toast.makeText(EcoAnalyzerActivity.this, path + " ��ʧ��", Toast.LENGTH_SHORT).show();
            return;
        }
        int startPos = 0;
        String resultLog = "";
        JSONArray logArr = new JSONArray();
        try {
            while (startPos < logStr.length() - 1) {
                int roadEndIndex = 0;
                int roadIndex = logStr.indexOf("=", startPos);
                if (roadIndex != -1) {
                    startPos = roadIndex;
                    roadEndIndex = logStr.indexOf(",�ٶ�����=", startPos);
                }
                int speedIndex = logStr.indexOf("=", roadEndIndex);
                if (speedIndex > 0) {
                    startPos = speedIndex;
                }
                int appStart = logStr.indexOf(":", startPos);
                if (appStart > 0) {
                    startPos = appStart;
                }
                int appEndIndex = logStr.indexOf(";", startPos);
                if (appEndIndex == -1) {
                    break;
                }
                startPos = appEndIndex + 1;

                int roadType = GpsUtils.strToInt(logStr.substring(roadIndex + 1, roadEndIndex));
                int speedType = GpsUtils.strToInt(logStr.substring(speedIndex + 1, appStart));
                String appLogStr = logStr.substring(appStart + 1, appEndIndex);

                double appTotalValue = 0;
                int appCount = 0;
                int start = 0;
                int prevValue = 0;
                for (int i = 0; i < 1000; i++) {
                    int nextIndex = appLogStr.indexOf(",", start);
                    if (nextIndex == -1) {
                        nextIndex = appLogStr.length();
                    }
                    String tmpStr = appLogStr.substring(start, nextIndex);
                    int tmpValue = GpsUtils.strToInt(tmpStr);
                    int curValue = tmpValue - prevValue;
                    if (curValue > 0) {
                        appTotalValue += ((double) curValue) * i / 10;
                        appCount = tmpValue;
                        prevValue = tmpValue;
                    }
                    start = nextIndex + 1;
                }
                JSONObject ob = new JSONObject();
                ob.put("roadType", roadType);
                ob.put("speedRange", speedType);
                ob.put("appCount", appCount);
                double aver = 0;
                if (appCount > 0) {
                    aver = appTotalValue / appCount;
                }
                ob.put("appAver", aver);
                logArr.put(ob);
            }

            for (int i = 0; i < logArr.length(); i++) {
                JSONObject obj = logArr.getJSONObject(i);
                int roadType = obj.getInt("roadType");
                int speedRange = obj.getInt("speedRange");
                double averApp = obj.getDouble("appAver");
                mAverAppArray[roadType][speedRange] = averApp;
            }
            for (int i = 0; i < roadTypeNum; i++) {
                for (int j = 0; j < speedRangeNum; j++) {
                    resultLog += String.format("%.3f,", mAverAppArray[i][j]);
                }
                resultLog += "\n";
            }
            // ���浽�ļ���
            FileManager.createDir(Constant.FILE_ECOLOG_DIR);
            String fileName = Constant.FILE_ECOLOG_DIR + "app_log.csv";
            FileManager.writeFile(fileName, resultLog);
            Toast.makeText(EcoAnalyzerActivity.this, "�������ſ������!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(EcoAnalyzerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
