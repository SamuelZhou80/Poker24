package com.samuel.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.Toast;

import com.samuel.MyApplication;
import com.samuel.main.student.SearchNameActivity;
import com.samuel.mytools.R;

public class MainActivity extends Activity {
    private ArrayList<Modules> mToolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_list);

        initTitle();
        initList();
        ToolListAdapter adapter = new ToolListAdapter();
        ListView listView = (ListView) findViewById(R.id.listview_tools);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mToolList != null && mToolList.size() > position) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, mToolList.get(position).moduleClass);
                    startActivity(intent);
                }
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
        MyApplication.getApp().exitApp();
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("С����");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        // btnRight.setVisibility(View.GONE);
        btnRight.setText("�������ݿ�");
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                copyDbFile();
            }
        });
    }

    private void initList() {
        mToolList = new ArrayList<Modules>();
        mToolList.add(new Modules("���������", CalcLoanActivity.class));
        mToolList.add(new Modules("Ͷ���������", CalcInvestActivity.class)); // Ͷ�ʼ�����
        mToolList.add(new Modules("24����Ϸ", PlayPoker24.class));
//        mToolList.add(new Modules("���ֱ��빤��", GBKToUTFActivity.class));
//        mToolList.add(new Modules("ͼ��ʶ��", PhotoDetectActivity.class));
        mToolList.add(new Modules("����ͳ��", SearchNameActivity.class));
        mToolList.add(new Modules("���ټ�����", PacerCalculate.class));
    }

    private void copyDbFile() {
        String destPath = Constant.CRM_DIR + File.separator;
        String srcDataPath = Constant.SYS_DIR + File.separator + "databases"
                + File.separator + Constant.DATABASE_NAME;
        /* Ŀ�����ݿ��ļ�·�� */
        String destDataPath = destPath + Constant.DATABASE_NAME;
        /* �������ݿ��ļ� */
        try {
            if (FileManager.copyFile(srcDataPath, destDataPath)) {
                Toast.makeText(MainActivity.this, "���ݿ⿽���ɹ�", Toast.LENGTH_LONG).show();
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ToolListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mToolList != null) {
                return mToolList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mToolList.get(position);
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
                holder.txtView.setText(mToolList.get(position).name);
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

    private class Modules {
        String name;
        Class<?> moduleClass;

        Modules(String name, Class<?> className) {
            this.name = name;
            this.moduleClass = className;
        }
    }

}
