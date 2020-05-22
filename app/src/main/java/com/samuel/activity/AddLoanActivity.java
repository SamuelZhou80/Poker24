package com.samuel.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.bean.LoanDB;
import com.samuel.bean.LoanInfo;
import com.samuel.mytools.R;
import com.samuel.utils.GpsUtils;

import java.util.Locale;

/**
 * 新增贷款界面
 *
 * @author Administrator
 */
@SuppressLint("DefaultLocale")
public class AddLoanActivity extends Activity {
    private TextView mTextDate;
    private LoanInfo mCurLoan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);
        mCurLoan = (LoanInfo) getIntent().getSerializableExtra("Loan");
        mTextDate = findViewById(R.id.text_seldate);
        if (mCurLoan == null) {
            mCurLoan = new LoanInfo();
        } else {
            EditText editMoney = findViewById(R.id.edit_money);
            editMoney.setText(String.valueOf(mCurLoan.getAmount()));
            // 期数
            EditText editYear = findViewById(R.id.edit_year);
            editYear.setText(String.valueOf(mCurLoan.getYears()));
            // 回报率
            EditText editRate = findViewById(R.id.edit_rate);
            editRate.setText(String.format("%.2f", mCurLoan.getRate()));
            mTextDate.setText(mCurLoan.getStartDate());
        }

        initTitle();
        mTextDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDialog();
            }
        });

        Button btnView = findViewById(R.id.button_view);
        btnView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurLoan.getAmount() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("Loan", mCurLoan);
                    intent.setClass(AddLoanActivity.this, CalcLoanActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddLoanActivity.this, "信息填写不完整", Toast.LENGTH_SHORT).show();
                }
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
        TextView textViewTitle = findViewById(R.id.commontitle_textview);
        textViewTitle.setText("新增贷款");

        Button btnReturn = findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLoanActivity.this.finish();
            }
        });

        Button btnRight = findViewById(R.id.common_btn_right);
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
            EditText editMoney = findViewById(R.id.edit_money);
            // 期数
            EditText editYear = findViewById(R.id.edit_year);
            // 回报率
            EditText editRate = findViewById(R.id.edit_rate);
            int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
            int yearNum = GpsUtils.strToInt(editYear.getText().toString());
            double rate = GpsUtils.strToFloat(editRate.getText().toString());
            if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
                Toast.makeText(AddLoanActivity.this, "请输入贷款金额、年限、利率等!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 显示概要信息
            TextView tvSummary = findViewById(R.id.text_summary_result);
            double[] summary = calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            String summaryStr = "";
            summaryStr += String.format(Locale.CHINA, " 月供: %.1f元,", summary[2]);// 每月还款金额
            summaryStr += String.format(Locale.CHINA, " 年供: %.1f元\n", (summary[2] * 12));// 每年还款金额
            summaryStr += String.format(Locale.CHINA, " 总还款额: %.1f元,", summary[0]);// 还款总额
            summaryStr += String.format(Locale.CHINA, " 利息合计: %.1f元", summary[1]);// 还款总利息
            tvSummary.setText(summaryStr);
            tvSummary.setVisibility(View.VISIBLE);

            String dateStr = mTextDate.getText().toString();
            if (dateStr.startsWith("开始")) {
                Toast.makeText(AddLoanActivity.this, "请选择开始日期", Toast.LENGTH_SHORT).show();
                return;
            }

            mCurLoan.setAmount(initMoney);
            mCurLoan.setRate(rate);
            mCurLoan.setStartDate(dateStr);
            mCurLoan.setYears(yearNum);
            LoanDB.getInstance().saveLoanInfo(mCurLoan);
            setResult(RESULT_OK);
            AddLoanActivity.this.finish();
        }
    };

    /**
     * 计算等额本息还款
     *
     * @param principal
     *         贷款总额
     * @param months
     *         贷款月数(年限*12)
     * @param rate
     *         贷款年利率
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
        String dateStr = mTextDate.getText().toString();
        int[] dateAry = { 2020, 1, 1 };
        if (TextUtils.isEmpty(dateStr)) {
            dateAry = GpsUtils.getCurDateBytes(dateStr);
        }
        DatePickerDialog dialog = new DatePickerDialog(AddLoanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTextDate.setText(String.format(Locale.CHINA, "%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
            }
        }, dateAry[0], dateAry[1] - 1, dateAry[2]);
        dialog.setTitle("选择开始日期");
        dialog.show();
    }
}
