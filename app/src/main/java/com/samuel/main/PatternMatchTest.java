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
 * 正则表达式匹配测试接口
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
        // 表格组件
        initTableView();

        Button mButtonCalc = (Button) findViewById(R.id.button_1);
        mButtonCalc.setOnClickListener(calcListener);
        mButtonCalc.setText("测试语句");
        Button buttonReset = (Button) findViewById(R.id.button_2);
        buttonReset.setOnClickListener(calcSpeedListener);
        buttonReset.setText("计算配速");
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
        textViewTitle.setText("正则表达式");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PatternMatchTest.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("计算");
        btnRight.setOnClickListener(calcListener);
        btnRight.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化表格
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(PatternMatchTest.this) / 2;
        int[] columnwidth = { itemWidth, itemWidth, itemWidth, itemWidth };
        String[] title = { "里程", "时间" };

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
            // 收起软键盘
            InputMethodManager imManager = (InputMethodManager) PatternMatchTest.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (isMatchedPattern()) {
                Toast.makeText(PatternMatchTest.this, "正则表达式匹配正确", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(PatternMatchTest.this, "正则表达式匹配错误", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (TextUtils.isEmpty(speedStr) || speedStr.length() < 3) {
//                Toast.makeText(PatternMatchTest.this, "配速输入不正确", Toast.LENGTH_SHORT).show();
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
        // 常量定义
        final double gravityAcce = 9.8; // 重力加速度
        final double tMax = 500; // 发动机最大输出扭矩
        final double re = 5.143; // 主减速器速比  Re
        final double me = 0.95; // 机械效率 Me 手动变速箱约95%，自动变速箱约88%，而传动轴的万向节效率约98%
        final double radius = 0.487; // 轮胎半径    Ra 单位米


        // 从数据采集里获取车辆的行驶数据
        double prevSpeed = 40.796875; // 前一秒的速度
        double speed = 40.19921875;
        double act = 94;
        double rotateSpeed = 1078;
        double oss = 1074.625;
        double slope = 1.16199994087219; // 坡度

        // 车的质量     m=F÷a, F = F(驱动)-F(摩擦)-F(下滑)-F(空气)

        //加速度   a   (v-v0)÷∆t
        double acce = (speed - prevSpeed);

        // F(驱动)    T×Gr×Re×Me÷Ra,当前扭矩 T=Tmax×act
        double curIg = rotateSpeed / oss; // 变速箱齿比  Gr  n÷oss
        double drivingForce = tMax * act * curIg * re * me * radius;

        // 空气阻力 F(空气)   1/2×C×Ad×Wa×v²
        double coefficient = 0.6; // 空气阻力系数, 通常情况下，轿车的风阻系数为0.4到0.6，卡车的空气阻力系数约为0.6到0.8
        double airDensity = 1.293; // 空气密度
        double wa = 8; // 迎风面积
        double airResistance = 0.5 * speed * speed * coefficient * airDensity * wa; // 空气阻力

        // 摩擦力  F(摩擦)   μ×m×g×[1÷√(1+s²)], 计算m*g之外的参数μ×[1÷√(1+s²)]
        double paramA = 0.6 * (1 / Math.sqrt(1+ slope * slope));
        //下滑力   F(下滑)   m×g×[s÷√(1+s²)] , 计算m*g之外的参数[s÷√(1+s²)]
        double paramB = slope / Math.sqrt(1 + slope * slope);

        double weight = (drivingForce - airResistance) / (acce + gravityAcce * (paramA + paramB));
        Toast.makeText(PatternMatchTest.this, "计算车辆质量=" + weight, Toast.LENGTH_LONG).show();
    }

}
