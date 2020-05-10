package com.samuel.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
 * ������ʽƥ����Խӿ�
 * @author Samuel Zhou
 *
 */
@SuppressLint("DefaultLocale")
public class PatternMatchTest extends Activity {
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();
    private EditText mEditPattern;
    private EditText mEditResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pacer_calculate);

        mEditPattern = (EditText) findViewById(R.id.edit_speed);
        mEditResource = (EditText) findViewById(R.id.edit_totaltime);
        initTitle();
        // ������
        initTableView();

        Button mButtonCalc = (Button) findViewById(R.id.button_1);
        mButtonCalc.setOnClickListener(calcListener);
        mButtonCalc.setText("�������");
        Button buttonReset = (Button) findViewById(R.id.button_2);
        buttonReset.setOnClickListener(calcSpeedListener);
        buttonReset.setText("��������");
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
        textViewTitle.setText("������ʽ");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PatternMatchTest.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("����");
        btnRight.setOnClickListener(calcListener);
        btnRight.setVisibility(View.INVISIBLE);
    }

    /**
     * ��ʼ�����
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(PatternMatchTest.this) / 2;
        int[] columnwidth = { itemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "���", "ʱ��" };

        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(title);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
        mTableView.setVisibility(View.GONE);
    }

    private OnClickListener calcListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // ���������
            InputMethodManager imManager = (InputMethodManager) PatternMatchTest.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (isMatchedPattern()) {
                Toast.makeText(PatternMatchTest.this, "������ʽƥ����ȷ", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(PatternMatchTest.this, "������ʽƥ�����", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (TextUtils.isEmpty(speedStr) || speedStr.length() < 3) {
//                Toast.makeText(PatternMatchTest.this, "�������벻��ȷ", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (mTableView != null) {
//                getTableData(speedStr, 1);
//                mTableView.refreshTableView();
//                mTableView.setVisibility(View.VISIBLE);
//            }
        }
    };

    private OnClickListener calcSpeedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            calcCarWeight();
        }
    };
    
    private boolean isMatchedPattern() {
        String patternStr = mEditPattern.getEditableText().toString();
        String targetStr = mEditResource.getEditableText().toString();
        if (TextUtils.isEmpty(patternStr) || TextUtils.isEmpty(targetStr)) {
            return false;
        }
        Pattern r = Pattern.compile(patternStr);
        Matcher m = r.matcher(targetStr.trim());
        return m.matches();
    }
    
    private void calcCarWeight() {
        // ��������
        final double gravityAcce = 9.8; // �������ٶ�
        final double tMax = 500; // ������������Ť��
        final double re = 5.143; // ���������ٱ�  Re
        final double me = 0.95; // ��еЧ�� Me �ֶ�������Լ95%���Զ�������Լ88%����������������Ч��Լ98%
        final double radius = 0.487; // ��̥�뾶    Ra ��λ��


        // �����ݲɼ����ȡ��������ʻ����
        double prevSpeed = 40.796875; // ǰһ����ٶ�
        double speed = 40.19921875;
        double act = 94;
        double rotateSpeed = 1078;
        double oss = 1074.625;
        double slope = 1.16199994087219; // �¶�

        // ��������     m=F��a, F = F(����)-F(Ħ��)-F(�»�)-F(����)

        //���ٶ�   a   (v-v0)��?t
        double acce = (speed - prevSpeed);

        // F(����)    T��Gr��Re��Me��Ra,��ǰŤ�� T=Tmax��act
        double curIg = rotateSpeed / oss; // ������ݱ�  Gr  n��oss
        double drivingForce = tMax * act * curIg * re * me * radius;

        // �������� F(����)   1/2��C��Ad��Wa��v?
        double coefficient = 0.6; // ��������ϵ��, ͨ������£��γ��ķ���ϵ��Ϊ0.4��0.6�������Ŀ�������ϵ��ԼΪ0.6��0.8
        double airDensity = 1.293; // �����ܶ�
        double wa = 8; // ӭ�����
        double airResistance = 0.5 * speed * speed * coefficient * airDensity * wa; // ��������

        // Ħ����  F(Ħ��)   �̡�m��g��[1�¡�(1+s?)], ����m*g֮��Ĳ����̡�[1�¡�(1+s?)]
        double paramA = 0.6 * (1 / Math.sqrt(1+ slope * slope));
        //�»���   F(�»�)   m��g��[s�¡�(1+s?)] , ����m*g֮��Ĳ���[s�¡�(1+s?)]
        double paramB = slope / Math.sqrt(1 + slope * slope);

        double weight = (drivingForce - airResistance) / (acce + gravityAcce * (paramA + paramB));
        Toast.makeText(PatternMatchTest.this, "���㳵������=" + weight, Toast.LENGTH_LONG).show();
    }

}
