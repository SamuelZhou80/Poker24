package com.samuel.twentyfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private String[] mToolArray = { "贷款计算器", "投资收益计算", "24点游戏" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_list);

        initTitle();
        ToolListAdapter adapter = new ToolListAdapter();
        ListView listView = (ListView) findViewById(R.id.listview_tools);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (position == 0) {
                    intent.setClass(MainActivity.this, CalcLoanActivity.class); // 贷款计算器
                } else if (position == 1) {
                    intent.setClass(MainActivity.this, CalcInvestActivity.class); // 投资计算器
                } else if (position == 2) {
                    intent.setClass(MainActivity.this, PlayPoker24.class); // 24点游戏
                }

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("小工具");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.common_btn_right).setVisibility(View.GONE);
    }

    private class ToolListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mToolArray != null) {
                return mToolArray.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mToolArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                convertView = inflater.inflate(R.layout.common_listview_item, parent, false);
                holder = new ViewHolder();
                holder.txtView = (TextView) convertView.findViewById(R.id.text);
                holder.txtView.setText(mToolArray[position]);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class ViewHolder {
            TextView txtView;
            // ImageView image;
        }

    }

}
