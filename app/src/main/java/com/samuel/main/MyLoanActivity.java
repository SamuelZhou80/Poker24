package com.samuel.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.common.TableView;
import com.samuel.mytools.R;

import java.util.ArrayList;

/**
 * 我的贷款汇总
 * 
 * @author Administrator
 *
 */
public class MyLoanActivity extends Activity {
    private Spinner mSpinnerType;
    private double[] mLoanSummary = new double[3];
    // 0: 按年显示; 2: 按月显示
    private int mCurType = 0;
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculateloan);

        initTitle();
        // 表格组件
        initTableView();

        // 表格显示格式选择框
        String[] numAry = { "年", "月" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.common_spinner, numAry);
        mAdapter.setDropDownViewResource(R.layout.my_simple_spinner);
        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
        mSpinnerType.setAdapter(mAdapter);
        mSpinnerType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurType = position;
                refreshLoanTable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        textViewTitle.setText("贷款计算器");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLoanActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("计算");
        btnRight.setOnClickListener(calcListener);
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
                Toast.makeText(MyLoanActivity.this, "请输入贷款金额、年限、利率等!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 显示概要信息
            TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
            double[] summary = calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            String summaryStr = "";
            summaryStr += String.format(" 月供: %.2f元,", summary[2]);// 每月还款金额
            summaryStr += String.format(" 年供: %.2f元\n", summary[2] * 12);// 每年还款金额
            summaryStr += String.format(" 总还款额: %.1f元,", summary[0]);// 还款总额
            summaryStr += String.format(" 利息合计: %.1f元", summary[1]);// 还款总利息
            tvSummary.setText(summaryStr);
            tvSummary.setVisibility(View.VISIBLE);

            // 加载明细表格的数据
            if (mTableView != null) {
                getTableData(initMoney, yearNum, rate, summary[2]);
                mTableView.refreshTableView();
            }
        }
    };

    private void refreshLoanTable() {
        EditText editMoney = (EditText) findViewById(R.id.edit_money);
        EditText editYear = (EditText) findViewById(R.id.edit_year);
        EditText editRate = (EditText) findViewById(R.id.edit_rate);
        int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
        int yearNum = GpsUtils.strToInt(editYear.getText().toString());
        double rate = GpsUtils.strToFloat(editRate.getText().toString());
        if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
            return;
        }
        if (mTableView != null) {
            calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            getTableData(initMoney, yearNum, rate, mLoanSummary[2]);
            mTableView.refreshTableView();
        }
    }

    /**
     * 初始化表格
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(MyLoanActivity.this) / 4 - 2;
        int[] columnwidth = { itemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "期数", "本金(元)", "利息(元)", "余额(元)" };

        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(title);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
    }

    /**
     * 获取贷款还款的明细表格, 分为按年和按月两种模式
     * 
     * @param principal
     *            贷款总额
     * @param years
     *            贷款年限
     * @param rate
     *            贷款年利率
     * @param preLoan
     *            月供额
     */
    private void getTableData(double principal, int years, double rate, double preLoan) {
        double curPrincipal = principal; // 当期余额
        double monthRate = rate / (100 * 12);// 月利率
        mTableData.clear();
        for (int i = 0; i < years; i++) {
            double yearInterest = 0.0;
            double yearPrincipal = 0.0;
            for (int j = 0; j < 12; j++) {
                if (curPrincipal <= 0) {
                    break;
                }
                double monthInterest = curPrincipal * monthRate; // 本月的利息
                double monthPrincipal = preLoan - monthInterest; // 本月归还本金
                curPrincipal = curPrincipal - monthPrincipal;
                yearInterest += monthInterest;
                yearPrincipal += monthPrincipal;
                if (mCurType == 1) {
                    ArrayList<String> rowData = new ArrayList<String>();
                    rowData.add(String.format("第%d年%d月", (i + 1), j + 1));
                    rowData.add(String.format("%.1f", monthPrincipal));
                    rowData.add(String.format("%.1f", monthInterest));
                    rowData.add(String.format("%.1f", curPrincipal));
                    mTableData.add(rowData);
                }
            }
            if (mCurType == 0) {
                ArrayList<String> rowData = new ArrayList<String>();
                rowData.add(String.format("第%d年", i + 1));
                rowData.add(String.format("%.1f", yearPrincipal));
                rowData.add(String.format("%.1f", yearInterest));
                rowData.add(String.format("%.1f", curPrincipal));
                mTableData.add(rowData);
            }
        }
    }

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
        mLoanSummary = new double[] { totalMoney, interest, preLoan };
        return mLoanSummary;
    }
}
