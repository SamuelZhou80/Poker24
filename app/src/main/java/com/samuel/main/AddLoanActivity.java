package com.samuel.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.main.bean.LoanDB;
import com.samuel.main.bean.LoanInfo;
import com.samuel.mytools.R;

import java.util.Locale;

/**
 * 新增贷款界面
 * 
 * @author Administrator
 *
 */
public class AddLoanActivity extends Activity {
    private TextView mTextDate;

//    private double[] mLoanSummary = new double[3];
//    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        initTitle();

        mTextDate = (TextView) findViewById(R.id.text_seldate);
        mTextDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDialog();
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
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("新增贷款");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLoanActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("保存");
        btnRight.setOnClickListener(calcListener);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 收起软键盘
            InputMethodManager imManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imManager != null) {
                imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

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
                Toast.makeText(AddLoanActivity.this, "请输入贷款金额、年限、利率等!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 显示概要信息
            TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
            double[] summary = calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            String summaryStr = "";
            summaryStr += String.format(Locale.CHINA, " 月供: %.2f元,", summary[2]);// 每月还款金额
            summaryStr += String.format(Locale.CHINA, " 年供: %.2f元\n", summary[2] * 12);// 每年还款金额
            summaryStr += String.format(Locale.CHINA, " 总还款额: %.1f元,", summary[0]);// 还款总额
            summaryStr += String.format(Locale.CHINA, " 利息合计: %.1f元", summary[1]);// 还款总利息
            tvSummary.setText(summaryStr);
            tvSummary.setVisibility(View.VISIBLE);


            String dateStr = mTextDate.getText().toString();
            if (dateStr.startsWith("开始")) {
                Toast.makeText(AddLoanActivity.this, "请选择开始日期", Toast.LENGTH_SHORT).show();
                return;
            }
            LoanInfo loan = new LoanInfo(dateStr, initMoney, yearNum, rate);
            LoanDB.getInstance().saveLoanInfo(loan);
            setResult(RESULT_OK);
            AddLoanActivity.this.finish();
        }
    };

    /**
     * 计算等额本息还款
     *
     * @param principal
     *            贷款总额
     * @param months
     *            贷款月数(年限*12)
     * @param rate
     *            贷款年利率
     * @return 返回贷款的 { 总还款额, 总利息, 月供 }
     */
    private double[] calcEqualPrincipalAndInterest(double principal, int months, double rate) {
        double monthRate = rate / (100 * 12);// 月利率
        // 每月月供额=贷款本金×月利率×[(1+月利率)^还款月数]÷[(1+月利率)^还款月数-1]
        double tempValue = Math.pow((1 + monthRate), months);
        double preLoan = (principal * monthRate * tempValue) / (tempValue - 1);// 每月还款金额
        double totalMoney = preLoan * months;// 还款总额
        double interest = totalMoney - principal;// 还款总利息
        return new double[] { totalMoney, interest, preLoan };
    }

    private void showDatePickDialog() {
        DatePickerDialog dialog = new DatePickerDialog(AddLoanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTextDate.setText(String.format(Locale.CHINA, "%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
            }
        }, 2020, 1, 1);
        dialog.setTitle("选择开始日期");
        dialog.show();
    }
}
