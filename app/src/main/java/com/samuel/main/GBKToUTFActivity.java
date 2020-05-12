package com.samuel.main;

import java.io.UnsupportedEncodingException;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.FileManager;
import com.samuel.common.Constant;
import com.samuel.mytools.R;

public class GBKToUTFActivity extends Activity {
    private static final String GbkTableFile = Constant.CRM_DIR + "/gbk_table.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charset_tools_layout);

        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("汉字编码");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GBKToUTFActivity.this.finish();
            }
        });
        findViewById(R.id.common_btn_right).setVisibility(View.GONE);

        initData();
        Button btnHanzi = (Button) findViewById(R.id.button_hanzi);
        Button btnGbk = (Button) findViewById(R.id.button_gbk);
        Button btnUtf = (Button) findViewById(R.id.button_utf);
        btnHanzi.setOnClickListener(onPressButton);
        btnGbk.setOnClickListener(onPressButton);
        btnUtf.setOnClickListener(onPressButton);

        TextView tvHint = (TextView) findViewById(R.id.text_hint);
        tvHint.setText("点击汉字按钮用于获取全部GBK汉字的总表, 点击GBK按钮将获取全部汉字的GBK编码文件, 点击UTF-16按钮将获取全部汉字的UTF-16编码文件");

        Button btnQuery = (Button) findViewById(R.id.button_query);
        btnQuery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    EditText edit = (EditText) findViewById(R.id.edit_hanzi);
                    EditText editGBK = (EditText) findViewById(R.id.edit_gbk);
                    EditText editUTF = (EditText) findViewById(R.id.edit_utf);

                    String input = edit.getEditableText().toString();
                    if (!TextUtils.isEmpty(input)) {
                        StringBuffer sb = new StringBuffer(input.length() * 4);
                        byte[] gbkArray = input.getBytes("GBK");
                        for (int i = 0; i < gbkArray.length; i++) {
                            int a = byteToInt(gbkArray[i]);
                            int b = byteToInt(gbkArray[++i]);
                            sb.append(String.format("%04x ", a * 256 + b));
                        }
                        editGBK.setText(sb.substring(0, sb.length() - 1).toUpperCase());

                        sb = new StringBuffer(input.length() * 4);
                        byte[] utfArray = input.getBytes("UTF-16");
//                        boolean isBigEndian = false;
//                        if (utfArray[0] == -1 && utfArray[1] == -2) {
//                            isBigEndian = true;
//                        }
                        for (int i = 2; i < utfArray.length; i++) {
                            int a = byteToInt(utfArray[i]);
                            int b = byteToInt(utfArray[++i]);
//                            if (isBigEndian) {
//                                sb.append(String.format("%04x,", a * 256 + b));
//                            } else {
                            sb.append(String.format("%04x ", b * 256 + a));
                            // }
                        }
                        editUTF.setText(sb.substring(0, sb.length() - 1).toUpperCase());
                    } else {
                        editGBK.setText("");
                        editUTF.setText("");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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

    private void initData() {
//        String str = getGBKTable(0x8140, 0x8180);
//        EditText edit = (EditText) findViewById(R.id.edit_hanzi);
//        edit.setText(str);
    }

    private OnClickListener onPressButton = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // EditText edit = (EditText) findViewById(R.id.edit_hanzi);
            // String input = edit.getText().toString();
            if (v.getId() == R.id.button_gbk) {
                getGBKOutput();
            } else if (v.getId() == R.id.button_utf) {
                getUTF16Output();
            } else if (v.getId() == R.id.button_hanzi) {
                // initData();
                getTotalTable();
            }
        }
    };

    private void getGBKOutput() {
        String input = FileManager.readFile(GbkTableFile);
        if (input != null) {
            getGbkCodeFile(input);
        }
    }

    private void getUTF16Output() {
        String input = FileManager.readFile(GbkTableFile);
        if (input != null) {
            getUtfCodeFile(input);
        }
    }

    private void getTotalTable() {
        String totalStr = getGBKTable(0x8140, 0xFEFF);
        FileManager.writeFile(GbkTableFile, totalStr);
        // 显示字符串的片段
        EditText edit = (EditText) findViewById(R.id.edit_hanzi);
        edit.setText(totalStr.substring(0, 100));
        Toast.makeText(GBKToUTFActivity.this, "合计输出GBK字符 " + totalStr.length(), Toast.LENGTH_SHORT).show();
    }

    private void getGbkCodeFile(String input) {
        try {
            StringBuffer sb = new StringBuffer(input.length() * 4);
            byte[] gbkArray = input.getBytes("GBK");
            for (int i = 0; i < gbkArray.length; i++) {
                int a = byteToInt(gbkArray[i]);
                int b = byteToInt(gbkArray[++i]);
                sb.append(String.format("%04x,", a * 256 + b));
            }
            String fileName = Constant.CRM_DIR + "/gbk_code.txt";
            FileManager.writeFile(fileName, sb.substring(0, sb.length() - 1).toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getUtfCodeFile(String input) {
        try {
            StringBuffer sb = new StringBuffer(input.length() * 4);
            byte[] utfArray = input.getBytes("UTF-16");
//            boolean isBigEndian = false;
//            if (utfArray[0] == -1 && utfArray[1] == -2) {
//                isBigEndian = true;
//            }
            for (int i = 2; i < utfArray.length; i++) {
                int a = byteToInt(utfArray[i]);
                int b = byteToInt(utfArray[++i]);
//                if (isBigEndian) {
//                    sb.append(String.format("%04x,", a * 256 + b));
//                } else {
                sb.append(String.format("%04x,", b * 256 + a));
                // }
            }
            String fileName = Constant.CRM_DIR + "/utf_code.txt";
            FileManager.writeFile(fileName, sb.substring(0, sb.length() - 1).toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字节转化为整型
     * 
     * @param a
     * @return 整数
     */
    private int byteToInt(byte a) {
        return (a & 0xff);
    }

    /**
     * 获取一段GBK编码范围内的字符串, GBK采用双字节表示, 总体编码范围为 8140-FEFE，首字节在 81-FE 之间，尾字节在 40-FE 之间，剔除 xx7F 一条线
     *
     * @param start
     *            编码起始值, 大于等于0x8140
     * @param end
     *            编码结束值, 小于等于0xFEFE
     * @return GBK编码范围内的字符串
     */
    private String getGBKTable(int start, int end) {
        StringBuilder sb = new StringBuilder(10000);
        if (start < 0x8140) {
            start = 0x8140;
        }
        if (end < start || end > 0xFEFF) {
            end = 0xFEFF;
        }
        int highStart = (start >> 8) & 0xff;
        int lowStart = start & 0xff;
        int highEnd = (end >> 8) & 0xff;
        int lowEnd = end & 0xff;
        byte[] data = new byte[2];
        try {
            for (int high = highStart; high <= highEnd; high++) {
                for (int low = lowStart; low < lowEnd; low++) {
                    // 跳过0xXX7F这个空白的码位
                    if (low == 0x7F) {
                        continue;
                    }

                    // 跳过空白码位, 用户自定义区: F8A1-FEFE
                    if (high >= 0xF8 && low > 0xA0) {
                        continue;
                    }
                    // 跳过用户自定义区: A140-A7A0
                    if (high >= 0xA1 && high <= 0xA7) {
                        if (low <= 0xA0) {
                            continue;
                        }
                    }
                    data[0] = (byte) (high);
                    data[1] = (byte) (low);
                    sb.append(new String(data, "GBK"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
