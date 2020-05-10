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
 * �����������
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
        textViewTitle.setText("��������");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLoanActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("����");
        btnRight.setOnClickListener(calcListener);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imManager != null) {
                imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

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
                Toast.makeText(AddLoanActivity.this, "�������������ޡ����ʵ�!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ��ʾ��Ҫ��Ϣ
            TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
            double[] summary = calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            String summaryStr = "";
            summaryStr += String.format(Locale.CHINA, " �¹�: %.2fԪ,", summary[2]);// ÿ�»�����
            summaryStr += String.format(Locale.CHINA, " �깩: %.2fԪ\n", summary[2] * 12);// ÿ�껹����
            summaryStr += String.format(Locale.CHINA, " �ܻ����: %.1fԪ,", summary[0]);// �����ܶ�
            summaryStr += String.format(Locale.CHINA, " ��Ϣ�ϼ�: %.1fԪ", summary[1]);// ��������Ϣ
            tvSummary.setText(summaryStr);
            tvSummary.setVisibility(View.VISIBLE);


            String dateStr = mTextDate.getText().toString();
            if (dateStr.startsWith("��ʼ")) {
                Toast.makeText(AddLoanActivity.this, "��ѡ��ʼ����", Toast.LENGTH_SHORT).show();
                return;
            }
            LoanInfo loan = new LoanInfo(dateStr, initMoney, yearNum, rate);
            LoanDB.getInstance().saveLoanInfo(loan);
            setResult(RESULT_OK);
            AddLoanActivity.this.finish();
        }
    };

    /**
     * ����ȶϢ����
     *
     * @param principal
     *            �����ܶ�
     * @param months
     *            ��������(����*12)
     * @param rate
     *            ����������
     * @return ���ش���� { �ܻ����, ����Ϣ, �¹� }
     */
    private double[] calcEqualPrincipalAndInterest(double principal, int months, double rate) {
        double monthRate = rate / (100 * 12);// ������
        // ÿ���¹���=�����������ʡ�[(1+������)^��������]��[(1+������)^��������-1]
        double tempValue = Math.pow((1 + monthRate), months);
        double preLoan = (principal * monthRate * tempValue) / (tempValue - 1);// ÿ�»�����
        double totalMoney = preLoan * months;// �����ܶ�
        double interest = totalMoney - principal;// ��������Ϣ
        return new double[] { totalMoney, interest, preLoan };
    }

    private void showDatePickDialog() {
        DatePickerDialog dialog = new DatePickerDialog(AddLoanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTextDate.setText(String.format(Locale.CHINA, "%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
            }
        }, 2020, 1, 1);
        dialog.setTitle("ѡ��ʼ����");
        dialog.show();
    }
}
