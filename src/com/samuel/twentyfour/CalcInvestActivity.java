package com.samuel.twentyfour;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ͷ���������
 * 
 * @author Administrator
 *
 */
public class CalcInvestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_loan_layout);

        initTitle();
        Button btnCalc = (Button) findViewById(R.id.button_calc);
        btnCalc.setText("��  ��");
        btnCalc.setOnClickListener(calcListener);
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

        findViewById(R.id.common_btn_right).setVisibility(View.GONE);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ��Ͷ���
            EditText editMoney = (EditText) findViewById(R.id.edit_money);
            // ����
            EditText editYear = (EditText) findViewById(R.id.edit_year);
            // �ر���
            EditText editRate = (EditText) findViewById(R.id.edit_rate);
            int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
            int yearNum = GpsUtils.strToInt(editYear.getText().toString());
            double rate = GpsUtils.strToFloat(editRate.getText().toString());
            if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
                Toast.makeText(CalcInvestActivity.this, "������Ͷ�ʽ��������ر��ʵ�!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuffer sb = new StringBuffer(200);
            sb.append(" ����  |   Ͷ�ʶ�    |     �ܽ��  \n");

            double prevYearMoney = 0;
            for (int i = 1; i < yearNum + 1; i++) {
                // Ͷ�ʱ���=Ͷ���*����
                String principalStr = formatStr(initMoney * i, 7, 1);
                // �ܽ��=(���ڽ��+����Ͷ��)*(1+ÿ��������)
                int totalMoney = (int) ((prevYearMoney + initMoney) * (1 + rate / 100));
                String moneyStr = formatStr(totalMoney, 7, 1);
                prevYearMoney = totalMoney;
                sb.append(String.format("  %02d.     %s           %s \n", i, principalStr, moneyStr));
            }
            TextView tvResult = (TextView) findViewById(R.id.text_result);
            tvResult.setText(sb.toString().substring(0, sb.length() - 1));
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

    /**
     * ����ȶϢ����
     *
     * @param principal
     *            �����ܶ�
     * @param months
     *            ��������
     * @param rate
     *            ��������
     * @return
     */
    @SuppressWarnings("unused")
    private int calculateMonthReturn(double principal, int months, double rate) {
        // ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);// ������
        // ����ÿ�»�����
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);
        double totalMoney = preLoan * months;// �����ܶ�
        double interest = totalMoney - principal;// ��������Ϣ
//        data.add(FORMAT.format(totalMoney));//�����ܶ�
//        data.add(FORMAT.format(principal));//�����ܶ�
//        data.add(FORMAT.format(interest));//��������Ϣ
//        data.add(FORMAT.format(preLoan));//ÿ�»�����
//        data.add(String.valueOf(months));//��������
        return ((int) preLoan);
        // return data.toArray(new String[data.size()]);
    }
}
