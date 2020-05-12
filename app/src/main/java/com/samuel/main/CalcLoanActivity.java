package com.samuel.main;

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

import com.samuel.main.bean.LoanDB;
import com.samuel.main.bean.LoanInfo;
import com.samuel.mytools.R;

import java.util.ArrayList;

/**
 * 贷款计算器
 *
 * @author Administrator
 */
public class CalcLoanActivity extends Activity {
    private static final int ADD_LOAN = 100;
    private ArrayList<LoanInfo> mLoanList;
    private LoanListAdapter mAdapter = new LoanListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myloan_list);

        initTitle();

        Button btnAdd = (Button) findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CalcLoanActivity.this, AddLoanActivity.class);
                startActivityForResult(intent, ADD_LOAN);
            }
        });

        mLoanList = LoanDB.getInstance().getLoanList();

        ListView listView = (ListView) findViewById(R.id.listview_loan);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLoanList != null && mLoanList.size() > position) {
                    Intent intent = new Intent();
                    intent.putExtra("Loan", mLoanList.get(position));
                    intent.setClass(CalcLoanActivity.this, MyLoanActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_LOAN) {
            if (resultCode == RESULT_OK) {
                // todo 新增后的界面刷新
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
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("贷款计算器");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcLoanActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("计算");
        btnRight.setVisibility(View.INVISIBLE);
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
                LayoutInflater inflater = LayoutInflater.from(CalcLoanActivity.this);
                convertView = inflater.inflate(R.layout.common_listview_item, parent, false);
                holder = new ViewHolder();
                holder.txtView = (TextView) convertView.findViewById(R.id.text);
                holder.txtView.setText(mLoanList.get(position).getStartDate());
                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class ViewHolder {
            TextView txtView;
            // ImageView image;
        }
    }

}
