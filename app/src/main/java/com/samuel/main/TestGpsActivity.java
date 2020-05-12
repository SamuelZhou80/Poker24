package com.samuel.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.samuel.mytools.R;

public class TestGpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gps_layout);
        Button btn = (Button) findViewById(R.id.button_refresh);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getPostion();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getPostion() {
//        int status = GpsWork.getInstance().getGpsStatus();
//        if (status == 1) {
//            status = 0;
//        }
//        int gpsLon = GpsWork.getInstance().getChangedLon();
//        double gpsLonD = ((double) gpsLon) / 3600 / 1000;
//        int gpsLat = GpsWork.getInstance().getChangedLat();
//        double gpsLatD = ((double) gpsLat) / 3600 / 1000;
//
//        int longitudeHour = (int) gpsLonD;
//        double tmpLongitudeHour = gpsLonD - longitudeHour;
//        double tmpLongitudeMinue = tmpLongitudeHour * 60;
//        int longitudeMinue = (int) tmpLongitudeMinue;// 经度分
//        double tmpLongitudeSecond = tmpLongitudeMinue - longitudeMinue;
//        int longitudeSecond = (int) (tmpLongitudeSecond * 60);// 经度秒
//        double floatSecondLon = tmpLongitudeSecond * 60 - longitudeSecond;
//        int minSecond = (int) (floatSecondLon * 10);
//
//        int latitudeHour = (int) gpsLatD;
//        double tmpLatitudeHour = gpsLatD - latitudeHour;
//        double tmpLatitudeMinue = tmpLatitudeHour * 60;
//        int latitudeMinue = (int) tmpLatitudeMinue;// 纬度分
//        double tmpLatitudeSecond = tmpLatitudeMinue - latitudeMinue;
//        int latitudeSecond = (int) (tmpLatitudeSecond * 60);// 纬度秒
//        double floatSecondLat = tmpLatitudeSecond * 60 - latitudeSecond;
//        int minSecondLat = (int) (floatSecondLat * 10);
//
//        TextView tvGps = (TextView) findViewById(R.id.text_gps);
//        String gpsStr = String.format("GPS: %d, 经度: %d(%d度%d分%d.%d秒), 纬度: %d(%d度%d分%d.%d秒)",
//                status, gpsLon, longitudeHour, longitudeMinue, longitudeSecond, minSecond,
//                gpsLat, latitudeHour, latitudeMinue, latitudeSecond, minSecondLat);
//        tvGps.setText(gpsStr);
//
//        getBaiduPos();
    }

//    private void getBaiduPos() {
//        int bdStatus = GpsBaidu.getInstance().getBaiduGpsStatus();
//        int bdLon = GpsBaidu.getInstance().getBaiduLon();
//        double baiduLonD = ((double) bdLon) / 3600 / 1000;
//        int bdLat = GpsBaidu.getInstance().getBaiduLat();
//        double baiduLatD = ((double) bdLat) / 3600 / 1000;
//
//        int longitudeHour = (int) baiduLonD;
//        double tmpLongitudeHour = baiduLonD - longitudeHour;
//        double tmpLongitudeMinue = tmpLongitudeHour * 60;
//        int longitudeMinue = (int) tmpLongitudeMinue;// 经度分
//        double tmpLongitudeSecond = tmpLongitudeMinue - longitudeMinue;
//        int longitudeSecond = (int) (tmpLongitudeSecond * 60);// 经度秒
//        double floatSecondLon = tmpLongitudeSecond * 60 - longitudeSecond;
//        int minSecond = (int) (floatSecondLon * 10);
//
//        int latitudeHour = (int) baiduLatD;
//        double tmpLatitudeHour = baiduLatD - latitudeHour;
//        double tmpLatitudeMinue = tmpLatitudeHour * 60;
//        int latitudeMinue = (int) tmpLatitudeMinue;// 纬度分
//        double tmpLatitudeSecond = tmpLatitudeMinue - latitudeMinue;
//        int latitudeSecond = (int) (tmpLatitudeSecond * 60);// 纬度秒
//        double floatSecondLat = tmpLatitudeSecond * 60 - latitudeSecond;
//        int minSecondLat = (int) (floatSecondLat * 10);
//
//        TextView tvBaidu = (TextView) findViewById(R.id.text_baidu);
//        String baiduStr = String.format("Baidu: %d, 经度: %d(%d度%d分%d.%d秒), 纬度: %d(%d度%d分%d.%d秒)",
//                bdStatus, bdLon, longitudeHour, longitudeMinue, longitudeSecond, minSecond,
//                bdLat, latitudeHour, latitudeMinue, latitudeSecond, minSecondLat);
//        tvBaidu.setText(baiduStr);
//
////        TextView tvBaidu = (TextView) findViewById(R.id.text_baidu);
////        String baiduStr = String.format("Baidu: %d, 经度: %d(%3.6f), 纬度: %d()", bdStatus, bdLon, baiduLonD, bdLat);
////        tvBaidu.setText(baiduStr);
//    }
}
