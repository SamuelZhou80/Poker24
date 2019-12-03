package com.samuel.main.eco;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.samuel.common.worklog.WorklogManage;
import com.samuel.main.FileManager;
import com.samuel.main.GpsUtils;
import com.samuel.mytools.R;
import com.samuel.utils.OpenFileUtil;

/**
 * 车辆数据分析
 * 
 * @author Samuel Zhou
 *
 */
@SuppressLint("DefaultLocale")
public class EcoDataTest extends Activity {
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();
    private EditText mEditPattern;
    private EditText mEditResource;

    private String mFilePath;
    private double mPrevSpeed = -1;
    private ArrayList<CarDriveData> mDataList;

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
        buttonReset.setText("计算载重");
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
        textViewTitle.setText("载重模拟计算");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EcoDataTest.this.finish();
            }
        });

        Button rightBtn = (Button) findViewById(R.id.common_btn_right);
        rightBtn.setText("选择日志");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// 无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                String path = OpenFileUtil.getPath(EcoDataTest.this, uri);
                if (path == null) {
                    Toast.makeText(EcoDataTest.this, "无效的文件路径", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFilePath = path;
                File file = new File(path);
                String logStr = FileManager.readFile(file, "UTF-8");
                if (TextUtils.isEmpty(logStr)) {
                    Toast.makeText(EcoDataTest.this, path + " 打开失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                parserLog(logStr);
            } catch (Exception e) {
                Toast.makeText(EcoDataTest.this, "解析文件异常" + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化表格
     */
    private void initTableView() {
        int itemWidth = GpsUtils.getScreenWidth(EcoDataTest.this) / 2;
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
            InputMethodManager imManager = (InputMethodManager) EcoDataTest.this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (isMatchedPattern()) {
                Toast.makeText(EcoDataTest.this, "正则表达式匹配正确", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(EcoDataTest.this, "正则表达式匹配错误", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    private OnClickListener calcSpeedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mFilePath == null) {
                Toast.makeText(EcoDataTest.this, "无效的文件路径", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(mFilePath);
            String logStr = FileManager.readFile(file, "UTF-8");
            if (TextUtils.isEmpty(logStr)) {
                Toast.makeText(EcoDataTest.this, mFilePath + " 打开失败", Toast.LENGTH_SHORT).show();
                return;
            }
            parserLog(logStr);
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

    private double calcCarWeight(CarDriveData item) {
        // 常量定义
        final double gravityAcce = 9.8; // 重力加速度
        final double tMax = 500; // 发动机最大输出扭矩
        final double re = 5.143; // 主减速器速比 Re
        final double me = 0.95; // 机械效率 Me 手动变速箱约95%，自动变速箱约88%，而传动轴的万向节效率约98%
        final double radius = 0.487; // 轮胎半径 Ra 单位米

        // 从数据采集里获取车辆的行驶数据
        double prevSpeed = item.getPrevSpeed(); // 前一秒的速度
        double speed = item.getSpeed();
        double act = item.getAct();
        double rotateSpeed = item.getRotateSpeed();
        double oss = item.getOss();
        double slope = item.getSlope(); // 坡度

        // 加速度 a (v-v0)÷∆t
        double acce = (speed - prevSpeed);

        // F(驱动) T×Gr×Re×Me÷Ra,当前扭矩 T=Tmax×act
        double curIg = rotateSpeed / oss; // 变速箱齿比 Gr n÷oss
        double drivingForce = tMax * act * curIg * re * me * radius;

        // 空气阻力 F(空气) 1/2×C×Ad×Wa×v²
        double coefficient = 0.6; // 空气阻力系数, 通常情况下，轿车的风阻系数为0.4到0.6，卡车的空气阻力系数约为0.6到0.8
        double airDensity = 1.293; // 空气密度
        double wa = 8; // 迎风面积
        double airResistance = 0.5 * speed * speed * coefficient * airDensity * wa; // 空气阻力

        // 摩擦力 F(摩擦) μ×m×g×[1÷√(1+s²)], 计算m*g之外的参数μ×[1÷√(1+s²)]
        double paramA = 0.6 * (1 / Math.sqrt(1 + slope * slope));
        // 下滑力 F(下滑) m×g×[s÷√(1+s²)] , 计算m*g之外的参数[s÷√(1+s²)]
        double paramB = slope / Math.sqrt(1 + slope * slope);

        // 车的质量 m=F÷a, F = F(驱动)-F(摩擦)-F(下滑)-F(空气)
        // m*a = (drivingForce - airResistance - paramA*m*g - paramB*m*g)
        // m*a + m*(paramA*g + paramB * g) = (drivingForce - airResistance)
        // m*(a + paramA*g + paramB * g) = (drivingForce - airResistance)
        // m = (drivingForce - airResistance)/(a + (paramA + paramB)*g)

        double weight = (drivingForce - airResistance) / (acce + gravityAcce * (paramA + paramB));
        return weight;
    }

    private void parserLog(String logStr) {
        if (logStr == null || logStr.length() < 10) {
            return;
        }

        int itemStartIndex = 0;
        int itemEndIndex = 0;
        int startIndex;
        int nextIndex;
        int logLength = logStr.length();
        int totalCount = GpsUtils.findCharNum(logStr, '\r', logLength) - 1;
        mDataList = new ArrayList<CarDriveData>(totalCount);
        WorklogManage.saveWorklog(3, totalCount, "---开始模拟计算载重---", 1);
        for (int i = 0; i < totalCount; i++) {
            itemEndIndex = GpsUtils.findCharPos(logStr, '\r', i, logLength);
            String itemSubStr;
            if (itemEndIndex < logLength) {
                itemSubStr = logStr.substring(itemStartIndex, itemEndIndex);
                itemStartIndex = itemEndIndex + 1;
            } else {
                return;
            }

            CarDriveData item = new CarDriveData();
//        0  lon,lat,dir,gpsSpeed,accuracy,datetime,  5
//        difTime_can,canSpeed,oss,act,n,      10
//        cs,brk,app,fct,fr,             15
//        oil,mil,difTime_adas,indepent_id,slope,  20
            if (GpsUtils.findCharNum(itemSubStr, ',', itemSubStr.length()) < 22) {
                return;
            }

            item.setPrevSpeed(mPrevSpeed);
            startIndex = GpsUtils.findCharPos(itemSubStr, ',', 4, logLength);
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 5, logLength);
            item.setTimeStr(itemSubStr.substring(startIndex + 1, nextIndex));

            startIndex = GpsUtils.findCharPos(itemSubStr, ',', 6, logLength);
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 7, logLength);
            item.setSpeed(GpsUtils.strToFloat(itemSubStr.substring(startIndex + 1, nextIndex)));
            mPrevSpeed = item.getSpeed();

            startIndex = nextIndex;
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 8, logLength);
            item.setOss(GpsUtils.strToFloat(itemSubStr.substring(startIndex + 1, nextIndex)));

            startIndex = nextIndex;
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 9, logLength);
            item.setAct(GpsUtils.strToInt(itemSubStr.substring(startIndex + 1, nextIndex)));

            startIndex = nextIndex;
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 10, logLength);
            item.setRotateSpeed(GpsUtils.strToInt(itemSubStr.substring(startIndex + 1, nextIndex)));

            startIndex = GpsUtils.findCharPos(itemSubStr, ',', 19, logLength);
            nextIndex = GpsUtils.findCharPos(itemSubStr, ',', 20, logLength);
            double slope = GpsUtils.strToFloat(itemSubStr.substring(startIndex + 1, nextIndex));
            if (slope > 100) {
                item.setSlope(0);
            }

            item.setWeight(calcCarWeight(item));
            if (item.getPrevSpeed() >= 0) {
                mDataList.add(item);
                WorklogManage.saveWorklog(3, 0, item.toString(), 1);
            }
        }

        double aver = calcAverage();
        WorklogManage.saveWorklog(4, 0, "---计算结束---" + aver, 1);
        Toast.makeText(EcoDataTest.this, "载重计算结束,平均值=" + aver, Toast.LENGTH_SHORT).show();
    }

    private double calcAverage() {
        double result = 0;
        // 计算区间分布
        int[] weightSection = new int[20];
        for (int i = 0; i < mDataList.size(); i++) {
            int section = (int) mDataList.get(i).getWeight() / 1000 + 1; // 换算成吨
            section = Math.max(1, Math.min(19, section));
            weightSection[section]++;
            result += mDataList.get(i).getWeight();
        }

        String logStr = "载重结果分布:";
        for (int i = 1; i < weightSection.length - 1; i++) {
            logStr += i + "吨出现" + weightSection[i] + ';';
        }
        WorklogManage.saveWorklog(4, 0, logStr, 1);

        return (result / mDataList.size());
    }
}
