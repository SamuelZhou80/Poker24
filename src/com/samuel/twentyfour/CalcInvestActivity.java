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
 * 定投收益计算器
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
        btnCalc.setText("计  算");
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
        textViewTitle.setText("投资计算器");

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
            // 定投金额
            EditText editMoney = (EditText) findViewById(R.id.edit_money);
            // 期数
            EditText editYear = (EditText) findViewById(R.id.edit_year);
            // 回报率
            EditText editRate = (EditText) findViewById(R.id.edit_rate);
            int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
            int yearNum = GpsUtils.strToInt(editYear.getText().toString());
            double rate = GpsUtils.strToFloat(editRate.getText().toString());
            if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
                Toast.makeText(CalcInvestActivity.this, "请输入投资金额、期数、回报率等!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuffer sb = new StringBuffer(200);
            sb.append(" 期数  |   投资额    |     总金额  \n");

            double prevYearMoney = 0;
            for (int i = 1; i < yearNum + 1; i++) {
                // 投资本金=投入额*期数
                String principalStr = formatStr(initMoney * i, 7, 1);
                // 总金额=(往期金额+当期投资)*(1+每期收益率)
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
     * 在内容前面或后面加上一段空格, 输出一段定长的字符串
     * 
     * @param number
     *            输入的数字
     * @param fiexedLength
     *            定长字符串的长度
     * @param isPrefix
     *            可选参数, 默认为0, 1表示空格加在文字前面
     * @return 定长字符串
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
     * 计算等额本息还款
     *
     * @param principal
     *            贷款总额
     * @param months
     *            贷款期限
     * @param rate
     *            贷款利率
     * @return
     */
    @SuppressWarnings("unused")
    private int calculateMonthReturn(double principal, int months, double rate) {
        // ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);// 月利率
        // 计算每月还款金额
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);
        double totalMoney = preLoan * months;// 还款总额
        double interest = totalMoney - principal;// 还款总利息
//        data.add(FORMAT.format(totalMoney));//还款总额
//        data.add(FORMAT.format(principal));//贷款总额
//        data.add(FORMAT.format(interest));//还款总利息
//        data.add(FORMAT.format(preLoan));//每月还款金额
//        data.add(String.valueOf(months));//还款期限
        return ((int) preLoan);
        // return data.toArray(new String[data.size()]);
    }
}
