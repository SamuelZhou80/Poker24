package com.samuel.twentyfour;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Samuel Zhou
 *
 */
public class MainActivity extends Activity {

    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private Spinner mSpinner3;
    private Spinner mSpinner4;
    private Button mButtonCalc;
    private TextView mResultDetail;
    /** 是否显示答案 */
    private boolean mIsShowResult = false;
    private int[] number = new int[4];
    private String[] exp = new String[4];
    private ArrayList<String> mResultArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinner1 = (Spinner) findViewById(R.id.spinner_num1);
        mSpinner2 = (Spinner) findViewById(R.id.spinner_num2);
        mSpinner3 = (Spinner) findViewById(R.id.spinner_num3);
        mSpinner4 = (Spinner) findViewById(R.id.spinner_num4);

        mButtonCalc = (Button) findViewById(R.id.button_check);
        mButtonCalc.setOnClickListener(checkClickListener);
        mButtonCalc.setText("显示答案");
        Button buttonReset = (Button) findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(resetClickListener);
        buttonReset.setText("随机发牌");

        mResultDetail = (TextView) findViewById(R.id.text_result_detail);
        mResultDetail.setText("");

        String[] numAry = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.common_spinner, numAry);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(mAdapter);
        mSpinner1.setPromptId(R.string.please_select);
        mSpinner2.setAdapter(mAdapter);
        mSpinner2.setPromptId(R.string.please_select);
        mSpinner3.setAdapter(mAdapter);
        mSpinner3.setPromptId(R.string.please_select);
        mSpinner4.setAdapter(mAdapter);
        mSpinner4.setPromptId(R.string.please_select);

//        TextView btnLoan = (TextView) findViewById(R.id.button_loan);
//        btnLoan.setText("投资计算器");
//        btnLoan.setTextColor(getResources().getColor(R.drawable.list_item_texview_gray_color));
//        btnLoan.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, CalcLoadActivity.class);
//                startActivity(intent);
//            }
//        });
        TextView btnLoan = (TextView) findViewById(R.id.button_loan);
        btnLoan.setText("编码工具");
        btnLoan.setTextColor(getResources().getColor(R.drawable.list_item_texview_gray_color));
        btnLoan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, GBKToUTFActivity.class);
                startActivity(intent);                
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 随机发牌按钮的点击监听接口
     */
    private OnClickListener resetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mIsShowResult = false;
            mButtonCalc.setText("显示答案");
            mResultDetail.setText("");
            mSpinner1.setSelection((int) Math.floor(Math.random() * 10));
            mSpinner2.setSelection((int) Math.floor(Math.random() * 10));
            mSpinner3.setSelection((int) Math.floor(Math.random() * 10));
            mSpinner4.setSelection((int) Math.floor(Math.random() * 10));
        }
    };

    /**
     * 点击显示或隐藏计算结果
     */
    private OnClickListener checkClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 获取输入的4个数字, 检查是否都有输入
            int num1, num2, num3, num4;
            num1 = Integer.parseInt((String) mSpinner1.getSelectedItem());
            num2 = Integer.parseInt((String) mSpinner2.getSelectedItem());
            num3 = Integer.parseInt((String) mSpinner3.getSelectedItem());
            num4 = Integer.parseInt((String) mSpinner4.getSelectedItem());
            if (num1 == 0 || num2 == 0 || num3 == 0 || num4 == 0) {
                Toast.makeText(MainActivity.this, "请选择输入的数字", Toast.LENGTH_SHORT).show();
                return;
            }

            mIsShowResult = !mIsShowResult;
            if (mIsShowResult) {
                // 计算是否存在有效解
                mButtonCalc.setText("隐藏答案");
                mResultArray.clear();
                mResultDetail.setText("");
                number = new int[] { num1, num2, num3, num4 };
                exp = new String[] { String.valueOf(num1), String.valueOf(num2), String.valueOf(num3), String.valueOf(num4) };
                // is24(4);
                exhaustiveCalc(num1, num2, num3, num4);
                if (mResultArray != null && mResultArray.size() > 0) {
                    String result = mResultArray.toString();
                    // result = result.replace(',', '\n');
                    mResultDetail.setText(result.substring(1, result.length() - 1));
                } else {
                    Toast.makeText(MainActivity.this, "没有符合条件的结果", Toast.LENGTH_SHORT).show();
                }
            } else {
                mResultArray.clear();
                mResultDetail.setText("");
                mButtonCalc.setText("显示答案");
            }
        }
    };

    /**
     * 递归算法, 对输入数据进行四则运算, 返回结果是否等于24
     * 
     * @param n
     * @return
     */
    public boolean is24(int n) {
        if (n == 1) {
            if (number[0] == 24) {
                mResultArray.add(exp[0].substring(1, exp[0].length() - 1));
                return true;
            }
            return false;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // 进行组合
                int a, b;
                String expa, expb;
                a = number[i]; // 保存起来，在方法最后再恢复，以便继续计算
                b = number[j]; // 保存起来，在方法最后再恢复，以便继续计算
                number[j] = number[n - 1]; // 将最后一个数挪过来
                expa = exp[i]; // 保存起来，在方法最后再恢复，以便继续计算
                expb = exp[j]; // 保存起来，在方法最后再恢复，以便继续计算
                exp[j] = exp[n - 1]; // 将最后一个式子挪过来j'
                exp[i] = "(" + expa + "+" + expb + ")"; // 看看加法能否算出,如果能算出，返回true
                number[i] = a + b;
                if (is24(n - 1)) {
                    // return true;
                }

                if (a >= b) {
                    exp[i] = "(" + expa + "-" + expb + ")"; // 看看减法能否算
                    number[i] = a - b;
                    if (is24(n - 1)) {
                        // return true;
                    }
                }
                if (b >= a) {
                    exp[i] = "(" + expb + "-" + expa + ")";
                    number[i] = b - a;
                    if (is24(n - 1)) {
                        // return true;
                    }
                }
                exp[i] = "(" + expa + "*" + expb + ")"; // 看看乘法能否算
                number[i] = a * b;
                if (is24(n - 1)) {
                    // return true;
                }
                if (b != 0 && (a % b == 0)) {
                    exp[i] = "(" + expa + "/" + expb + ")"; // 看看除法能否算
                    number[i] = a / b;
                    if (is24(n - 1)) {
                        // return true;
                    }
                }
                if (a != 0 && (b % a == 0)) {
                    exp[i] = "(" + expb + "/" + expa + ")";
                    number[i] = b / a;
                    if (is24(n - 1)) {
                        // return true;
                    }
                }
                // 如果以上的加、减、乘、除都不能得到有效的结果，则恢复数据进行下一轮的计算。
                number[i] = a; // 恢复
                number[j] = b;
                exp[i] = expa;
                exp[j] = expb;
            }
        }
        return false;
    }

    /**
     * 穷举算法, 对4个数字进行全排序计算(去掉重复的算式)
     */
    public void exhaustiveCalc(int a, int b, int c, int d) {
        // a
        caluate(a, b, c, d);
        if (c != d) {
            caluate(a, b, d, c);
        }
        if (b != c) {
            caluate(a, c, b, d);
        }
        if (b != c && b != d) {
            caluate(a, c, d, b);
        }
        if (b != d && c != d) {
            caluate(a, d, b, c);
        }
        if (b != c && b != d && c != d) {
            caluate(a, d, c, b);
        }
        // b
        if (a != b) {
            caluate(b, a, c, d);
            if (c != d) {
                caluate(b, a, d, c);
            }
            if (a != c) {
                caluate(b, c, a, d);
            }
            if (a != d && a != c) {
                caluate(b, c, d, a);
            }
            if (c != d && a != d) {
                caluate(b, d, c, a);
            }
            if (a != c && a != d && c != d) {
                caluate(b, d, a, c);
            }
        }
        // c
        if (a != c && b != c) {
            caluate(c, a, b, d);
            if (b != d) {
                caluate(c, a, d, b);
            }
            if (a != b) {
                caluate(c, b, a, d);
            }
            if (a != b && a != d) {
                caluate(c, b, d, a);
            }
            if (a != d && b != d) {
                caluate(c, d, a, b);
            }
            if (a != b && a != d && b != d) {
                caluate(c, d, b, a);
            }
        }
        // d
        if (a != d && b != d && c != d) {
            caluate(d, a, b, c);
            if (b != c) {
                caluate(d, a, c, b);
            }
            if (a != b) {
                caluate(d, b, c, a);
            }
            if (a != b && a != c) {
                caluate(d, b, a, c);
            }
            if (a != c && b != c) {
                caluate(d, c, a, b);
            }
            if (a != b && a != c && b != c) {
                caluate(d, c, b, a);
            }
        }
    }

    /**
     * 根据所有可能的算式组合计算, 获取满足条件的算式文本列表
     * 
     * @param a
     * @param b
     * @param c
     * @param d
     */
    private void caluate(int a, int b, int c, int d) {
        if (a <= b && b <= c && c <= d && a + b + c + d == 24) {
            mResultArray.add(String.format("%d+%d+%d+%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && a + b + c - d == 24) {
            mResultArray.add(String.format("%d+%d+%d-%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a + b <= c + d) && (a + b) * (c + d) == 24) {
            mResultArray.add(String.format("(%d+%d)×(%d+%d) ", a, b, c, d));
        }
        if (a <= b && (a + b) * (c - d) == 24) {
            mResultArray.add(String.format("(%d+%d)×(%d-%d) ", a, b, c, d));
        }
        if (a >= b && (a - b <= c - d) && (a - b) * (c - d) == 24) {
            mResultArray.add(String.format("(%d-%d)×(%d-%d) ", a, b, c, d));
        }
        if (a >= b && (a - b) * c + d == 24) {
            mResultArray.add(String.format("(%d-%d)×%d+%d ", a, b, c, d));
        }
        if (a >= b && (a - b) * c - d == 24) {
            mResultArray.add(String.format("(%d-%d)×%d-%d ", a, b, c, d));
        }
        if (a >= b && ((a - b) * c % d == 0) && (a - b) * c / d == 24) {
            mResultArray.add(String.format("(%d-%d)×%d÷%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a + b + c) * d == 24) {
            mResultArray.add(String.format("(%d+%d+%d)×%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a + b + c) % d == 0 && (a + b + c) / d == 24) {
            mResultArray.add(String.format("(%d+%d+%d)÷%d ", a, b, c, d));
        }
        if (b >= c && (a - b - c) * d == 24) {
            mResultArray.add(String.format("(%d-%d-%d)×%d ", a, b, c, d));
        }
        if (a <= b && (a + b - c) * d == 24) {
            mResultArray.add(String.format("(%d+%d-%d)×%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a * b * c) % d == 0 && (a * b * c) / d == 24) {
            mResultArray.add(String.format("%d×%d×%d÷%d ", a, b, c, d));
        }
        if ((a <= b && c <= d) && (a * b) * (c + d) == 24) {
            mResultArray.add(String.format("%d×%d×(%d+%d) ", a, b, c, d));
        }
        if (a <= b && (a * b) * (c - d) == 24) {
            mResultArray.add(String.format("%d×%d×(%d-%d) ", a, b, c, d));
        }
        if (a <= b && b <= c && a * b * c - d == 24) {
            mResultArray.add(String.format("%d×%d×%d-%d ", a, b, c, d));
        }
        if (a <= b && b < c && a * b * c + d == 24) {
            mResultArray.add(String.format("%d×%d×%d+%d ", a, b, c, d));
        }
        if (a <= b && b <= c && c <= d && a * b * c * d == 24) {
            mResultArray.add(String.format("%d×%d×%d×%d ", a, b, c, d));
        }
        if (a <= b && c % d == 0 && (a + b) + (c / d) == 24) {
            mResultArray.add(String.format("(%d+%d)+(%d÷%d) ", a, b, c, d));
        }
        if (a <= b && ((a + b) * c % d == 0) && (a + b) * c / d == 24) {
            mResultArray.add(String.format("(%d+%d)×%d÷%d ", a, b, c, d));
        }
        if (a <= b && (a + b) * c + d == 24) {
            mResultArray.add(String.format("(%d+%d)×%d+%d ", a, b, c, d));
        }
        if (a <= b && (a + b) * c - d == 24) {
            mResultArray.add(String.format("(%d+%d)×%d-%d ", a, b, c, d));
        }
        if (a <= b && (a + b) % c == 0 && (a + b) / c + d == 24) {
            mResultArray.add(String.format("(%d+%d)÷%d+%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b + c + d) == 24) {
            mResultArray.add(String.format("%d×%d+%d+%d ", a, b, c, d));
        }
        if (a <= b && (a * b) + c - d == 24) {
            mResultArray.add(String.format("%d×%d+%d-%d ", a, b, c, d));
        }
        if (a <= b && (a * b + c) * d == 24) {
            mResultArray.add(String.format("(%d×%d+%d)×%d ", a, b, c, d));
        }
        if (a <= b && c % d == 0 && (a * b) - (c / d) == 24) {
            mResultArray.add(String.format("(%d×%d)-(%d÷%d) ", a, b, c, d));
        }
        if (a <= b && a <= c && c % d == 0 && (a * b) + (c / d) == 24) {
            mResultArray.add(String.format("(%d×%d)+(%d÷%d) ", a, b, c, d));
        }
        if (a <= b && c >= d && (a * b) - c - d == 24) {
            mResultArray.add(String.format("%d×%d-%d-%d ", a, b, c, d));
        }
        if (a <= b && c <= d && a <= c && (a * b) + (c * d) == 24) {
            mResultArray.add(String.format("%d×%d+%d×%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) - (c * d) == 24) {
            mResultArray.add(String.format("%d×%d-%d×%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) % (c * d) == 0 && (a * b) / (c * d) == 24) {
            mResultArray.add(String.format("(%d×%d)÷(%d×%d) ", a, b, c, d));
        }
        if (a <= b && (c - d) != 0 && (a * b) % (c - d) == 0 && (a * b) / (c - d) == 24) {
            mResultArray.add(String.format("(%d×%d)÷(%d-%d) ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) % (c + d) == 0 && (a * b) / (c + d) == 24) {
            mResultArray.add(String.format("(%d×%d)÷(%d+%d) ", a, b, c, d));
        }
        if ((a % b == 0) && (a / b + c) * d == 24) {
            mResultArray.add(String.format("((%d÷%d)+%d)×%d ", a, b, c, d));
        }
        if ((a % b == 0) && (a / b - c) * d == 24) {
            mResultArray.add(String.format("((%d÷%d)-%d)×%d ", a, b, c, d));
        }
        if (a <= b && (a * b % c == 0) && (a * b) / c - d == 24) {
            mResultArray.add(String.format("(%d×%d)÷%d-%d ", a, b, c, d));
        }
        if (a <= b && (a * b % c == 0) && (a * b) / c + d == 24) {
            mResultArray.add(String.format("(%d×%d)÷%d+%d ", a, b, c, d));
        }
        if (a <= b && ((a * b - c) % d == 0) && ((a * b) - c) / d == 24) {
            mResultArray.add(String.format("(%d×%d-%d)÷%d ", a, b, c, d));
        }
        if (a <= b && ((a * b) - c) * d == 24) {
            mResultArray.add(String.format("(%d×%d-%d)×%d ", a, b, c, d));
        }
        if ((b % c == 0) && (a - b / c) * d == 24) {
            mResultArray.add(String.format("(%d-(%d÷%d))×%d ", a, b, c, d));
        }
        if (b <= c && (a - b * c) * d == 24) {
            mResultArray.add(String.format("(%d-(%d×%d))×%d ", a, b, c, d));
        }
    }
}
