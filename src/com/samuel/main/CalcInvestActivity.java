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
 * 定投收益计算器
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

        // 初始化表格
        int firstItemWidth = GpsUtils.dip2px(CalcInvestActivity.this, 60);
        int itemWidth = (GpsUtils.getScreenWidth(CalcInvestActivity.this) - firstItemWidth) / 3 - 2;
        int[] columnwidth = { firstItemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "期数", "累计投入(元)", "累计收益(元)", "总金额(元)" };
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
        textViewTitle.setText("投资计算器");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcInvestActivity.this.finish();
            }
        });

        Button btnCalc = (Button) findViewById(R.id.common_btn_right);
        btnCalc.setText("计算");
        btnCalc.setOnClickListener(calcListener);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 收起软键盘
            InputMethodManager imManager = (InputMethodManager) CalcInvestActivity.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            // 定投金额
            EditText editMoney = (EditText) findViewById(R.id.edit_money);
            // 期数
            EditText editYear = (EditText) findViewById(R.id.edit_year);
            EditText editTotalYear = (EditText) findViewById(R.id.edit_total_year);
            // 回报率
            EditText editRate = (EditText) findViewById(R.id.edit_rate);
            int initMoney = GpsUtils.strToInt(editMoney.getText().toString());
            int yearNum = GpsUtils.strToInt(editYear.getText().toString());
            double rate = GpsUtils.strToFloat(editRate.getText().toString());
            if (initMoney <= 0 || yearNum <= 0 || rate <= 0) {
                Toast.makeText(CalcInvestActivity.this, "请输入投资金额、期数、回报率等!", Toast.LENGTH_SHORT).show();
                return;
            }

            double prevYearMoney = 0;
            mTableData.clear();
            for (int i = 1; i < yearNum + 1; i++) {
                // 总金额=(往期金额+当期投资)*(1+每期收益率)
                int totalMoney = (int) ((prevYearMoney + initMoney) * (1 + rate / 100));
                ArrayList<String> itemData = new ArrayList<String>();
                itemData.add("第" + i + "期");
                itemData.add(String.valueOf(initMoney * i)); // 累计投入本金
                itemData.add(String.valueOf(totalMoney - initMoney * i)); // 累计收益
                itemData.add(String.valueOf(totalMoney)); // 总金额
                mTableData.add(itemData);
                prevYearMoney = totalMoney;
            }
            int totalYearNum = GpsUtils.strToInt(editTotalYear.getText().toString());
            if (totalYearNum < yearNum) {
                totalYearNum = yearNum;
            }
            // 计算停止定投后到投资期末的收益
            initMoney = initMoney * yearNum;
            for (int i = yearNum + 1; i < totalYearNum + 1; i++) {
                // 总金额=(往期金额)*(1+每期收益率)
                int totalMoney = (int) (prevYearMoney * (1 + rate / 100));
                ArrayList<String> itemData = new ArrayList<String>();
                itemData.add("第" + i + "期");
                itemData.add(String.valueOf(initMoney)); // 累计投入本金
                itemData.add(String.valueOf(totalMoney - initMoney)); // 累计收益
                itemData.add(String.valueOf(totalMoney)); // 总金额
                mTableData.add(itemData);
                prevYearMoney = totalMoney;
            }

            mTableView.refreshTableView();

            StringBuffer sb = new StringBuffer(200);
            sb.append(String.format("总投资额: %.2f万元, ", (float) (initMoney) / 10000L));
            sb.append("累计收益: ");
            sb.append(String.format("%.2f万元, ", (prevYearMoney - initMoney) / 10000L));
            sb.append(String.format("期末总金额: %.2f万元", prevYearMoney / 10000L));
            TextView tvResult = (TextView) findViewById(R.id.text_summary_result);
            tvResult.setText(sb.toString());
            tvResult.setVisibility(View.VISIBLE);
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
