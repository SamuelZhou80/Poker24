package com.samuel.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.samuel.bean.LoanDB;
import com.samuel.bean.LoanInfo;
import com.samuel.mytools.R;
import com.samuel.utils.GpsUtils;

import java.util.ArrayList;

/**
 * 我的贷款汇总列表
 *
 * @author Samuel Zhou
 */
@SuppressLint("DefaultLocale")
public class MyLoanActivity extends Activity {
    private static final int ADD_LOAN = 100;
    private ArrayList<LoanInfo> mLoanList;
    private LoanListAdapter mAdapter = new LoanListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myloan_list);

        initTitle();
        mLoanList = LoanDB.getInstance().getLoanList();

        ListView listView = findViewById(R.id.listview_loan);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLoanList != null && mLoanList.size() > position) {
                    Intent intent = new Intent();
                    intent.putExtra("Loan", mLoanList.get(position));
                    intent.setClass(MyLoanActivity.this, AddLoanActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 长按删除一条记录
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mLoanList.size()) {
                    if (LoanDB.getInstance().deleteLoan(mLoanList.get(position).getLoanId())) {
                        mLoanList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });


        Button btnAdd = findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyLoanActivity.this, AddLoanActivity.class);
                startActivityForResult(intent, ADD_LOAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_LOAN) {
            if (resultCode == RESULT_OK) {
                mLoanList = LoanDB.getInstance().getLoanList();
                mAdapter.notifyDataSetChanged();
            }
        }
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
        textViewTitle.setText("我的贷款");

        Button btnReturn = findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLoanActivity.this.finish();
            }
        });

        Button btnRight = findViewById(R.id.common_btn_right);
        btnRight.setText("查看");
        // btnRight.setOnClickListener(calcListener);
    }

    private class LoanListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mLoanList != null) {
                return mLoanList.size();
            }
            return 0;
        }

        @Override
        public LoanInfo getItem(int position) {
            return mLoanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(MyLoanActivity.this);
                convertView = inflater.inflate(R.layout.common_listview_item, parent, false);
                holder = new ViewHolder();
                holder.txtView = convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String[] remainInfo = getCurRemain(mLoanList.get(position));
            String loanSum = String.format("贷款金额 %d元,", mLoanList.get(position).getAmount());
            loanSum += " 月供 " + remainInfo[0] + "元";
            loanSum += "\n已归还本金 " + remainInfo[1] + "元";
            loanSum += ", 已归还利息 " + remainInfo[2] + "元";
            holder.txtView.setText(loanSum);
            return convertView;
        }

        class ViewHolder {
            TextView txtView;
        }
    }

    /**
     * 计算截止目前的贷款还款情况, 返回数组[月还款, 已还本金, 已还利息]
     *
     * @param info
     *         贷款记录信息
     */
    private String[] getCurRemain(LoanInfo info) {
        double curPrincipal = info.getAmount(); // 当期余额
        double monthRate = info.getRate() / (100 * 12);// 月利率
        // 每月月供额=贷款本金×月利率×[(1+月利率)^还款月数]÷[(1+月利率)^还款月数-1]
        double tempValue = Math.pow((1 + monthRate), info.getYears() * 12);
        double preLoan = (curPrincipal * monthRate * tempValue) / (tempValue - 1);// 每月还款金额

        // 计算已归还期数
        int[] startDay = GpsUtils.getCurDateBytes(info.getStartDate());
        int[] curDay = GpsUtils.getCurDateBytes(GpsUtils.getDate());
        int elapsedMonth = Math.max(0, (curDay[0] - startDay[0]) * 12 + curDay[1] - startDay[1]);
        double totalReturnIntereset = 0.0; // 合计归还利息
        double totalReturnPrincipal = 0.0; // 合计归还本金
        for (int i = 0; i < elapsedMonth; i++) {
            if (curPrincipal <= 0) {
                break;
            }
            double monthInterest = curPrincipal * monthRate; // 本月的利息
            double monthPrincipal = preLoan - monthInterest; // 本月归还本金
            curPrincipal = Math.max(curPrincipal - monthPrincipal, 0); // 确保余额一栏不会显示为负数
            totalReturnIntereset += monthInterest;
            totalReturnPrincipal += monthPrincipal;
        }

        return new String[] {
                String.format("%.1f", preLoan),
                String.format("%.1f", totalReturnPrincipal),
                String.format("%.1f", totalReturnIntereset),
        };
    }

}
