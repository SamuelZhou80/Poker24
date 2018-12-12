package com.samuel.main;

import java.util.ArrayList;

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

public class PacerCalculate extends Activity {
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pacer_calculate);

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
        EditText editSpeed = (EditText) findViewById(R.id.edit_speed);
        String speedStr = editSpeed.getEditableText().toString();
        if (TextUtils.isEmpty(speedStr)) {
            return;
        }
        if (mTableView != null) {
            getTableData(speedStr, 1);
            mTableView.refreshTableView();
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
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) PacerCalculate.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            EditText editSpeed = (EditText) findViewById(R.id.edit_speed);
            String speedStr = editSpeed.getEditableText().toString();
            if (TextUtils.isEmpty(speedStr)) {
                Toast.makeText(PacerCalculate.this, "�������벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTableView != null) {
                getTableData(speedStr, 1);
                mTableView.refreshTableView();
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

            EditText edit = (EditText) findViewById(R.id.edit_totaltime);
            String timeStr = edit.getEditableText().toString();
            if (TextUtils.isEmpty(timeStr)) {
                Toast.makeText(PacerCalculate.this, "ȫ����ʱ���벻��ȷ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTableView != null) {
                getTableData(timeStr, 2);
                mTableView.refreshTableView();
            }
        }
    };

    private void getTableData(String inputStr, int inputType) {
        mTableData.clear();
        int speedInSeconds = 0;
        // 1: �����ټ���ʱ��; 2: ��ʱ��������� */
        if (inputType == 1) {
            int speed = GpsUtils.strToInt(inputStr); // �����ٵ�λת��Ϊ����
            speedInSeconds = speed / 100 * 60 + speed % 100;

            // ����ȫ����ʱ
            int totalTime = (int) Math.round(speedInSeconds * 42.195);
            int usedHour = totalTime / 3600;
            int usedMinute = (totalTime % 3600) / 60;
            EditText edit = (EditText) findViewById(R.id.edit_totaltime);
            edit.setText(String.format("%d%02d", usedHour, usedMinute));
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
            EditText edit = (EditText) findViewById(R.id.edit_speed);
            int pacerMinute = speedInSeconds / 60;
            int pacerSecond = speedInSeconds % 60;
            edit.setText(String.format("%d%02d", pacerMinute, pacerSecond));
        }

        ArrayList<String> itemData = new ArrayList<String>();
        itemData.add("ʮ����");
        itemData.add(convertTime(speedInSeconds * 10));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("���");
        itemData.add(convertTime((int) Math.round(speedInSeconds * 21.0975)));
        mTableData.add(itemData);

        itemData = new ArrayList<String>();
        itemData.add("��ʮ����");
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
