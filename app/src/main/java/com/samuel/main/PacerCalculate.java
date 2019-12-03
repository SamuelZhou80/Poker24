package com.samuel.main;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.samuel.common.TableView;
import com.samuel.mytools.R;

@SuppressLint("DefaultLocale")
public class PacerCalculate extends Activity {
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();
    private EditText mEditSpeed;
    private EditText mEditTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pacer_calculate);

        mEditSpeed = (EditText) findViewById(R.id.edit_speed);
        mEditTime = (EditText) findViewById(R.id.edit_totaltime);
        initTitle();
        // ������
        initTableView();

        Button mButtonCalc = (Button) findViewById(R.id.button_1);
        mButtonCalc.setOnClickListener(calcListener);
        mButtonCalc.setText("����ʱ��");
        Button buttonReset = (Button) findViewById(R.id.button_2);
        buttonReset.setOnClickListener(calcSpeedListener);
        buttonReset.setText("��������");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("���ټ�����");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PacerCalculate.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("����");
        btnRight.setOnClickListener(calcListener);
        btnRight.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("unused")
    private void refreshLoanTable() {
        String speedStr = mEditSpeed.getEditableText().toString();
        if (TextUtils.isEmpty(speedStr)) {
            return;
        }
        if (mTableView != null) {
            getTableData(speedStr, 1);
            mTableView.refreshTableView();
            mTableView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ��ʼ�����
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(PacerCalculate.this) / 2;
        int[] columnwidth = { itemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "���", "ʱ��" };

        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(title);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
        mTableView.setVisibility(View.GONE);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) PacerCalculate.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String speedStr = mEditSpeed.getEditableText().toString();
            if (TextUtils.isEmpty(speedStr) || speedStr.length() < 3) {
                Toast.makeText(PacerCalculate.this, "�������벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTableView != null) {
                getTableData(speedStr, 1);
                mTableView.refreshTableView();
                mTableView.setVisibility(View.VISIBLE);
            }
        }
    };

    private OnClickListener calcSpeedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) PacerCalculate.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String timeStr = mEditTime.getEditableText().toString();
            if (TextUtils.isEmpty(timeStr)) {
                Toast.makeText(PacerCalculate.this, "ȫ����ʱ���벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTableView != null) {
                getTableData(timeStr, 2);
                mTableView.refreshTableView();
                mTableView.setVisibility(View.VISIBLE);
            }
        }
    };

    private void getTableData(String inputStr, int inputType) {
        mTableData.clear();
        int speedInSeconds = 0;
        // 1: �����ټ���ʱ��; 2: ��ʱ��������� */
        if (inputType == 1) {
            int speed = GpsUtils.strToInt(inputStr); // �����ٵ�λת��Ϊ����
            if (speed < 100) {
                Toast.makeText(PacerCalculate.this, "�������벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }

            // ����ȫ����ʱ
            speedInSeconds = speed / 100 * 60 + speed % 100;
            int totalTime = (int) Math.round(speedInSeconds * 42.195);
            int usedHour = totalTime / 3600;
            int usedMinute = (totalTime % 3600) / 60;
            mEditTime.setText(String.format("%d%02d", usedHour, usedMinute));
        } else {
            int input = GpsUtils.strToInt(inputStr);
            int hour = input / 100;
            int minute = input % 100;
            if (hour == 0) {
                Toast.makeText(PacerCalculate.this, "ȫ����ʱ���벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }
            int totalSeconds = hour * 3600 + minute * 60;
            speedInSeconds = (int) Math.round(totalSeconds / 42.195);

            // ��������
            int pacerMinute = speedInSeconds / 60;
            int pacerSecond = speedInSeconds % 60;
            mEditSpeed.setText(String.format("%d%02d", pacerMinute, pacerSecond));
        }

        ArrayList<String> itemData = new ArrayList<String>();
        itemData.add("800��");
        itemData.add(convertTime(speedInSeconds * 4 / 5));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("1����");
        itemData.add(convertTime(speedInSeconds));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("5����");
        itemData.add(convertTime(speedInSeconds * 5));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("10����");
        itemData.add(convertTime(speedInSeconds * 10));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("���");
        itemData.add(convertTime((int) Math.round(speedInSeconds * 21.0975)));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("30����");
        itemData.add(convertTime(speedInSeconds * 30));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("ȫ��");
        itemData.add(convertTime((int) Math.round(speedInSeconds * 42.195)));
        mTableData.add(itemData);
    }

    private String convertTime(int seconds) {
        if (seconds == 0) {
            return "";
        }
        int hour = seconds / 3600;
        int remained = seconds % 3600;
        int minute = remained / 60;
        remained = remained % 60;

        if (hour > 0) {
            return String.format("%d:%02d:%02d", hour, minute, remained);
        } else {
            return String.format("%d:%02d", minute, remained);
        }
    }
}
