package com.samuel.main.education;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.samuel.mytools.R;

public class DataInputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_input_layout);
        saveItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveItem() {
        ZhongKao item = new ZhongKao();
        item.setSchoolName("集美中学");
        item.setShuangshibenbu(9);
        item.setToHighSchool(-1);
        item.setTop3(88);
        item.setTotalNum(666);
        item.setWaiguoyu(-1);
        item.setYear(2018);
        item.setYizhongbenbu(2);
        item.setYizhonghaicang(-1);
        ZhongKaoDB.getInstance().saveZhongkaoInfo(item);
    }
}
