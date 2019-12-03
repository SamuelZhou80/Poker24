package com.samuel.main;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.samuel.common.SelectPokerDialogView;
import com.samuel.common.SelectPokerDialogView.ConfirmListener;
import com.samuel.mytools.R;

/**
 * 24����Ϸ����
 * 
 * @author Samuel Zhou
 *
 */
public class PlayPoker24 extends Activity {
    private TextView mText1, mText2, mText3, mText4;
    private int mCurCardId;

    private Button mButtonCalc;
    private TextView mResultDetail;
    /** �Ƿ���ʾ�� */
    private boolean mIsShowResult = false;
    private int[] number = new int[4];
    private String[] exp = new String[4];
    private ArrayList<String> mResultArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_poker);
        initTitle();
        initPokerCard();

        mButtonCalc = (Button) findViewById(R.id.button_check);
        mButtonCalc.setOnClickListener(checkClickListener);
        mButtonCalc.setText("��ʾ��");
        Button buttonReset = (Button) findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(resetClickListener);
        buttonReset.setText("�������");

        mResultDetail = (TextView) findViewById(R.id.text_result_detail);
        mResultDetail.setText("");
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("24����Ϸ");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayPoker24.this.finish();
            }
        });

        findViewById(R.id.common_btn_right).setVisibility(View.GONE);
    }

    private void initPokerCard() {
        mText1 = (TextView) findViewById(R.id.text_num1);
        mText2 = (TextView) findViewById(R.id.text_num2);
        mText3 = (TextView) findViewById(R.id.text_num3);
        mText4 = (TextView) findViewById(R.id.text_num4);

        // �����ʼ��һ������
        mText1.setText(String.valueOf((int) Math.ceil(Math.random() * 10)));
        mText2.setText(String.valueOf((int) Math.ceil(Math.random() * 10)));
        mText3.setText(String.valueOf((int) Math.ceil(Math.random() * 10)));
        mText4.setText(String.valueOf((int) Math.ceil(Math.random() * 10)));

        // ��������ֶ�ѡ������
        mText1.setOnClickListener(selectNumListener);
        mText2.setOnClickListener(selectNumListener);
        mText3.setOnClickListener(selectNumListener);
        mText4.setOnClickListener(selectNumListener);
    }

    /**
     * �ֶ�ѡ�����ֽӿ�
     */
    private OnClickListener selectNumListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mCurCardId = v.getId();
            int prevNum = 0;
            if (mCurCardId == R.id.text_num1) {
                prevNum = GpsUtils.strToInt(mText1.getText().toString());
            } else if (mCurCardId == R.id.text_num2) {
                prevNum = GpsUtils.strToInt(mText2.getText().toString());
            } else if (mCurCardId == R.id.text_num3) {
                prevNum = GpsUtils.strToInt(mText3.getText().toString());
            } else if (mCurCardId == R.id.text_num4) {
                prevNum = GpsUtils.strToInt(mText4.getText().toString());
            }
            SelectPokerDialogView dialog = new SelectPokerDialogView(PlayPoker24.this, new ConfirmListener() {

                @Override
                public void onConfirm(int num) {
                    if (num > 0 && num <= 10) {
                        if (mCurCardId == R.id.text_num1) {
                            mText1.setText(String.valueOf(num));
                        } else if (mCurCardId == R.id.text_num2) {
                            mText2.setText(String.valueOf(num));
                        } else if (mCurCardId == R.id.text_num3) {
                            mText3.setText(String.valueOf(num));
                        } else if (mCurCardId == R.id.text_num4) {
                            mText4.setText(String.valueOf(num));
                        }
                    }
                }
            }, prevNum);
            dialog.show();
        }
    };

    /**
     * ������ư�ť�ĵ�������ӿ�
     */
    private OnClickListener resetClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mIsShowResult = false;
            mButtonCalc.setText("��ʾ��");
            mResultDetail.setText("");

            while (true) {
                int num1 = (int) Math.ceil(Math.random() * 10);
                int num2 = (int) Math.ceil(Math.random() * 10);
                int num3 = (int) Math.ceil(Math.random() * 10);
                int num4 = (int) Math.ceil(Math.random() * 10);
                mResultArray.clear();
                exhaustiveCalc(num1, num2, num3, num4);
                if (mResultArray != null && mResultArray.size() > 0) {
                    mText1.setText(String.valueOf(num1));
                    mText2.setText(String.valueOf(num2));
                    mText3.setText(String.valueOf(num3));
                    mText4.setText(String.valueOf(num4));
                    break;
                }
            }
        }
    };

    /**
     * �����ʾ�����ؼ�����
     */
    private OnClickListener checkClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ��ȡ�����4������, ����Ƿ�������
            int num1, num2, num3, num4;
            num1 = GpsUtils.strToInt(mText1.getText().toString());
            num2 = GpsUtils.strToInt(mText2.getText().toString());
            num3 = GpsUtils.strToInt(mText3.getText().toString());
            num4 = GpsUtils.strToInt(mText4.getText().toString());
            if (num1 == 0 || num2 == 0 || num3 == 0 || num4 == 0) {
                Toast.makeText(PlayPoker24.this, "��ѡ�����������", Toast.LENGTH_SHORT).show();
                return;
            }

            mIsShowResult = !mIsShowResult;
            if (mIsShowResult) {
                // �����Ƿ������Ч��
                mButtonCalc.setText("���ش�");
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
                    Toast.makeText(PlayPoker24.this, "û�з��������Ľ��", Toast.LENGTH_SHORT).show();
                }
            } else {
                mResultArray.clear();
                mResultDetail.setText("");
                mButtonCalc.setText("��ʾ��");
            }
        }
    };

    /**
     * �ݹ��㷨, ���������ݽ�����������, ���ؽ���Ƿ����24
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
            for (int j = i + 1; j < n; j++) { // �������
                int a, b;
                String expa, expb;
                a = number[i]; // �����������ڷ�������ٻָ����Ա��������
                b = number[j]; // �����������ڷ�������ٻָ����Ա��������
                number[j] = number[n - 1]; // �����һ����Ų����
                expa = exp[i]; // �����������ڷ�������ٻָ����Ա��������
                expb = exp[j]; // �����������ڷ�������ٻָ����Ա��������
                exp[j] = exp[n - 1]; // �����һ��ʽ��Ų����j'
                exp[i] = "(" + expa + "+" + expb + ")"; // �����ӷ��ܷ����,��������������true
                number[i] = a + b;
                if (is24(n - 1)) {
                    // return true;
                }

                if (a >= b) {
                    exp[i] = "(" + expa + "-" + expb + ")"; // ���������ܷ���
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
                exp[i] = "(" + expa + "*" + expb + ")"; // �����˷��ܷ���
                number[i] = a * b;
                if (is24(n - 1)) {
                    // return true;
                }
                if (b != 0 && (a % b == 0)) {
                    exp[i] = "(" + expa + "/" + expb + ")"; // ���������ܷ���
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
                // ������ϵļӡ������ˡ��������ܵõ���Ч�Ľ������ָ����ݽ�����һ�ֵļ��㡣
                number[i] = a; // �ָ�
                number[j] = b;
                exp[i] = expa;
                exp[j] = expb;
            }
        }
        return false;
    }

    /**
     * ����㷨, ��4�����ֽ���ȫ�������(ȥ���ظ�����ʽ)
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
     * �������п��ܵ���ʽ��ϼ���, ��ȡ������������ʽ�ı��б�
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
            mResultArray.add(String.format("(%d+%d)��(%d+%d) ", a, b, c, d));
        }
        if (a <= b && (a + b) * (c - d) == 24) {
            mResultArray.add(String.format("(%d+%d)��(%d-%d) ", a, b, c, d));
        }
        if (a >= b && (a - b <= c - d) && (a - b) * (c - d) == 24) {
            mResultArray.add(String.format("(%d-%d)��(%d-%d) ", a, b, c, d));
        }
        if (a >= b && (a - b) * c + d == 24) {
            mResultArray.add(String.format("(%d-%d)��%d+%d ", a, b, c, d));
        }
        if (a >= b && (a - b) * c - d == 24) {
            mResultArray.add(String.format("(%d-%d)��%d-%d ", a, b, c, d));
        }
        if (a >= b && ((a - b) * c % d == 0) && (a - b) * c / d == 24) {
            mResultArray.add(String.format("(%d-%d)��%d��%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a + b + c) * d == 24) {
            mResultArray.add(String.format("(%d+%d+%d)��%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a + b + c) % d == 0 && (a + b + c) / d == 24) {
            mResultArray.add(String.format("(%d+%d+%d)��%d ", a, b, c, d));
        }
        if (b >= c && (a - b - c) * d == 24) {
            mResultArray.add(String.format("(%d-%d-%d)��%d ", a, b, c, d));
        }
        if (a <= b && (a + b - c) * d == 24) {
            mResultArray.add(String.format("(%d+%d-%d)��%d ", a, b, c, d));
        }
        if ((a <= b && b <= c) && (a * b * c) % d == 0 && (a * b * c) / d == 24) {
            mResultArray.add(String.format("%d��%d��%d��%d ", a, b, c, d));
        }
        if ((a <= b && c <= d) && (a * b) * (c + d) == 24) {
            mResultArray.add(String.format("%d��%d��(%d+%d) ", a, b, c, d));
        }
        if (a <= b && (a * b) * (c - d) == 24) {
            mResultArray.add(String.format("%d��%d��(%d-%d) ", a, b, c, d));
        }
        if (a <= b && b <= c && a * b * c - d == 24) {
            mResultArray.add(String.format("%d��%d��%d-%d ", a, b, c, d));
        }
        if (a <= b && b < c && a * b * c + d == 24) {
            mResultArray.add(String.format("%d��%d��%d+%d ", a, b, c, d));
        }
        if (a <= b && b <= c && c <= d && a * b * c * d == 24) {
            mResultArray.add(String.format("%d��%d��%d��%d ", a, b, c, d));
        }
        if (a <= b && c % d == 0 && (a + b) + (c / d) == 24) {
            mResultArray.add(String.format("(%d+%d)+(%d��%d) ", a, b, c, d));
        }
        if (a <= b && ((a + b) * c % d == 0) && (a + b) * c / d == 24) {
            mResultArray.add(String.format("(%d+%d)��%d��%d ", a, b, c, d));
        }
        if (a <= b && (a + b) * c + d == 24) {
            mResultArray.add(String.format("(%d+%d)��%d+%d ", a, b, c, d));
        }
        if (a <= b && (a + b) * c - d == 24) {
            mResultArray.add(String.format("(%d+%d)��%d-%d ", a, b, c, d));
        }
        if (a <= b && (a + b) % c == 0 && (a + b) / c + d == 24) {
            mResultArray.add(String.format("(%d+%d)��%d+%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b + c + d) == 24) {
            mResultArray.add(String.format("%d��%d+%d+%d ", a, b, c, d));
        }
        if (a <= b && (a * b) + c - d == 24) {
            mResultArray.add(String.format("%d��%d+%d-%d ", a, b, c, d));
        }
        if (a <= b && (a * b + c) * d == 24) {
            mResultArray.add(String.format("(%d��%d+%d)��%d ", a, b, c, d));
        }
        if (a <= b && c % d == 0 && (a * b) - (c / d) == 24) {
            mResultArray.add(String.format("(%d��%d)-(%d��%d) ", a, b, c, d));
        }
        if (a <= b && a <= c && c % d == 0 && (a * b) + (c / d) == 24) {
            mResultArray.add(String.format("(%d��%d)+(%d��%d) ", a, b, c, d));
        }
        if (a <= b && c >= d && (a * b) - c - d == 24) {
            mResultArray.add(String.format("%d��%d-%d-%d ", a, b, c, d));
        }
        if (a <= b && c <= d && a <= c && (a * b) + (c * d) == 24) {
            mResultArray.add(String.format("%d��%d+%d��%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) - (c * d) == 24) {
            mResultArray.add(String.format("%d��%d-%d��%d ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) % (c * d) == 0 && (a * b) / (c * d) == 24) {
            mResultArray.add(String.format("(%d��%d)��(%d��%d) ", a, b, c, d));
        }
        if (a <= b && (c - d) != 0 && (a * b) % (c - d) == 0 && (a * b) / (c - d) == 24) {
            mResultArray.add(String.format("(%d��%d)��(%d-%d) ", a, b, c, d));
        }
        if (a <= b && c <= d && (a * b) % (c + d) == 0 && (a * b) / (c + d) == 24) {
            mResultArray.add(String.format("(%d��%d)��(%d+%d) ", a, b, c, d));
        }
        if ((a % b == 0) && (a / b + c) * d == 24) {
            mResultArray.add(String.format("((%d��%d)+%d)��%d ", a, b, c, d));
        }
        if ((a % b == 0) && (a / b - c) * d == 24) {
            mResultArray.add(String.format("((%d��%d)-%d)��%d ", a, b, c, d));
        }
        if (a <= b && (a * b % c == 0) && (a * b) / c - d == 24) {
            mResultArray.add(String.format("(%d��%d)��%d-%d ", a, b, c, d));
        }
        if (a <= b && (a * b % c == 0) && (a * b) / c + d == 24) {
            mResultArray.add(String.format("(%d��%d)��%d+%d ", a, b, c, d));
        }
        if (a <= b && ((a * b - c) % d == 0) && ((a * b) - c) / d == 24) {
            mResultArray.add(String.format("(%d��%d-%d)��%d ", a, b, c, d));
        }
        if (a <= b && ((a * b) - c) * d == 24) {
            mResultArray.add(String.format("(%d��%d-%d)��%d ", a, b, c, d));
        }
        if ((b % c == 0) && (a - b / c) * d == 24) {
            mResultArray.add(String.format("(%d-(%d��%d))��%d ", a, b, c, d));
        }
        if (b <= c && (a - b * c) * d == 24) {
            mResultArray.add(String.format("(%d-(%d��%d))��%d ", a, b, c, d));
        }
    }
}
