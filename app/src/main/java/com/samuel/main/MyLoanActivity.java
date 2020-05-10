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
 * �ҵĴ������
 * 
 * @author Administrator
 *
 */
public class MyLoanActivity extends Activity {
    private Spinner mSpinnerType;
    private double[] mLoanSummary = new double[3];
    // 0: ������ʾ; 2: ������ʾ
    private int mCurType = 0;
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculateloan);

        initTitle();
        // ������
        initTableView();

        // �����ʾ��ʽѡ���
        String[] numAry = { "��", "��" };
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
        textViewTitle.setText("���������");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLoanActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("����");
        btnRight.setOnClickListener(calcListener);
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
                Toast.makeText(MyLoanActivity.this, "�������������ޡ����ʵ�!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ��ʾ��Ҫ��Ϣ
            TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
            double[] summary = calcEqualPrincipalAndInterest(initMoney, yearNum * 12, rate);
            String summaryStr = "";
            summaryStr += String.format(" �¹�: %.2fԪ,", summary[2]);// ÿ�»�����
            summaryStr += String.format(" �깩: %.2fԪ\n", summary[2] * 12);// ÿ�껹����
            summaryStr += String.format(" �ܻ����: %.1fԪ,", summary[0]);// �����ܶ�
            summaryStr += String.format(" ��Ϣ�ϼ�: %.1fԪ", summary[1]);// ��������Ϣ
            tvSummary.setText(summaryStr);
            tvSummary.setVisibility(View.VISIBLE);

            // ������ϸ��������
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
     * ��ʼ�����
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(MyLoanActivity.this) / 4 - 2;
        int[] columnwidth = { itemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "����", "����(Ԫ)", "��Ϣ(Ԫ)", "���(Ԫ)" };

        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(title);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
    }

    /**
     * ��ȡ��������ϸ���, ��Ϊ����Ͱ�������ģʽ
     * 
     * @param principal
     *            �����ܶ�
     * @param years
     *            ��������
     * @param rate
     *            ����������
     * @param preLoan
     *            �¹���
     */
    private void getTableData(double principal, int years, double rate, double preLoan) {
        double curPrincipal = principal; // �������
        double monthRate = rate / (100 * 12);// ������
        mTableData.clear();
        for (int i = 0; i < years; i++) {
            double yearInterest = 0.0;
            double yearPrincipal = 0.0;
            for (int j = 0; j < 12; j++) {
                if (curPrincipal <= 0) {
                    break;
                }
                double monthInterest = curPrincipal * monthRate; // ���µ���Ϣ
                double monthPrincipal = preLoan - monthInterest; // ���¹黹����
                curPrincipal = curPrincipal - monthPrincipal;
                yearInterest += monthInterest;
                yearPrincipal += monthPrincipal;
                if (mCurType == 1) {
                    ArrayList<String> rowData = new ArrayList<String>();
                    rowData.add(String.format("��%d��%d��", (i + 1), j + 1));
                    rowData.add(String.format("%.1f", monthPrincipal));
                    rowData.add(String.format("%.1f", monthInterest));
                    rowData.add(String.format("%.1f", curPrincipal));
                    mTableData.add(rowData);
                }
            }
            if (mCurType == 0) {
                ArrayList<String> rowData = new ArrayList<String>();
                rowData.add(String.format("��%d��", i + 1));
                rowData.add(String.format("%.1f", yearPrincipal));
                rowData.add(String.format("%.1f", yearInterest));
                rowData.add(String.format("%.1f", curPrincipal));
                mTableData.add(rowData);
            }
        }
    }

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
        mLoanSummary = new double[] { totalMoney, interest, preLoan };
        return mLoanSummary;
    }
}
