package com.samuel.main;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.samuel.common.TableView;
import com.samuel.mytools.R;

/**
 * ��Ͷ���������
 * 
 * @author Administrator
 *
 */
public class CalcInvestActivity extends Activity {
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_invest_layout);

        initTitle();

        // ��ʼ�����
        int firstItemWidth = GpsUtils.dip2px(CalcInvestActivity.this, 60);
        int itemWidth = (GpsUtils.getScreenWidth(CalcInvestActivity.this) - firstItemWidth) / 3 - 2;
        int[] columnwidth = { firstItemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "����", "�ۼ�Ͷ��(Ԫ)", "�ۼ�����(Ԫ)", "�ܽ��(Ԫ)" };
        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(title);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
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
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("Ͷ�ʼ�����");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcInvestActivity.this.finish();
            }
        });

        Button btnCalc = (Button) findViewById(R.id.common_btn_right);
        btnCalc.setText("����");
        btnCalc.setOnClickListener(calcListener);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) CalcInvestActivity.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            // ��Ͷ���
            EditText editMoney = (EditText) findViewById(R.id.edit_money);
            // ����
            EditText editYear = (EditText) findViewById(R.id.edit_year);
            EditText editTotalYear = (EditText) findViewById(R.id.edit_total_year);
            // �ر���
            EditText editRate = (EditText) findViewById(R.id.edit_rate);
            int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
            int yearNum = GpsUtils.strToInt(editYear.getText().toString());
            double rate = GpsUtils.strToFloat(editRate.getText().toString());
            if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
                Toast.makeText(CalcInvestActivity.this, "������Ͷ�ʽ��������ر��ʵ�!", Toast.LENGTH_SHORT).show();
                return;
            }

            double prevYearMoney = 0;
            mTableData.clear();
            for (int i = 1; i < yearNum + 1; i++) {
                // �ܽ��=(���ڽ��+����Ͷ��)*(1+ÿ��������)
                int totalMoney = (int) ((prevYearMoney + initMoney) * (1 + rate / 100));
                ArrayList<String> itemData = new ArrayList<String>();
                itemData.add("��" + i + "��");
                itemData.add(String.valueOf(initMoney * i)); // �ۼ�Ͷ�뱾��
                itemData.add(String.valueOf(totalMoney - initMoney * i)); // �ۼ�����
                itemData.add(String.valueOf(totalMoney)); // �ܽ��
                mTableData.add(itemData);
                prevYearMoney = totalMoney;
            }
            int totalYearNum = GpsUtils.strToInt(editTotalYear.getText().toString());
            if (totalYearNum < yearNum) {
                totalYearNum = yearNum;
            }
            // ����ֹͣ��Ͷ��Ͷ����ĩ������
            initMoney = initMoney * yearNum;
            for (int i = yearNum + 1; i < totalYearNum + 1; i++) {
                // �ܽ��=(���ڽ��)*(1+ÿ��������)
                int totalMoney = (int) (prevYearMoney * (1 + rate / 100));
                ArrayList<String> itemData = new ArrayList<String>();
                itemData.add("��" + i + "��");
                itemData.add(String.valueOf(initMoney)); // �ۼ�Ͷ�뱾��
                itemData.add(String.valueOf(totalMoney - initMoney)); // �ۼ�����
                itemData.add(String.valueOf(totalMoney)); // �ܽ��
                mTableData.add(itemData);
                prevYearMoney = totalMoney;
            }

            mTableView.refreshTableView();

            StringBuffer sb = new StringBuffer(200);
            sb.append(String.format("��Ͷ�ʶ�: %.2f��Ԫ, ", (float) (initMoney) / 10000L));
            sb.append("�ۼ�����: ");
            sb.append(String.format("%.2f��Ԫ, ", (prevYearMoney - initMoney) / 10000L));
            sb.append(String.format("��ĩ�ܽ��: %.2f��Ԫ", prevYearMoney / 10000L));
            TextView tvResult = (TextView) findViewById(R.id.text_summary_result);
            tvResult.setText(sb.toString());
            tvResult.setVisibility(View.VISIBLE);
        }
    };

    /**
     * ������ǰ���������һ�οո�, ���һ�ζ������ַ���
     * 
     * @param number
     *            ���������
     * @param fiexedLength
     *            �����ַ����ĳ���
     * @param isPrefix
     *            ��ѡ����, Ĭ��Ϊ0, 1��ʾ�ո��������ǰ��
     * @return �����ַ���
     */
    @SuppressWarnings("unused")
    private String formatStr(int number, int fiexedLength, int isPrefix) {
        String str = String.valueOf(number);
        String prefixStr = "                                      ";
        if (fiexedLength <= str.length()) {
            return str;
        }
        if (isPrefix == 1) {
            return prefixStr.substring(0, fiexedLength - str.length()) + str;
        } else {
            return str + prefixStr.substring(0, fiexedLength - str.length());
        }
    }

}
