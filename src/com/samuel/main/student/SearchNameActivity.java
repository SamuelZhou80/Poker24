package com.samuel.main.student;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.common.PrefsSys;
import com.samuel.common.TableView;
import com.samuel.main.FileManager;
import com.samuel.main.GpsUtils;
import com.samuel.mytools.R;

public class SearchNameActivity extends Activity {
    private String mNameStr = "";
    private TableView mTableView;
    private ArrayList<ArrayList<String>> mTableData = new ArrayList<ArrayList<String>>();
    private boolean mIsHideBar = false;
    private StudentFile mFileInfo = new StudentFile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_table);
        initTitle();

        Button btnSelect = (Button) findViewById(R.id.button_selectfile);
        btnSelect.setOnClickListener(selFileListener);
        Button btnStart = (Button) findViewById(R.id.button_start);
        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = PrefsSys.getRecentFileName();
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(SearchNameActivity.this, "请先选择一个文本文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(path);
                mNameStr = FileManager.readFile(file, "UTF-16");
                if (TextUtils.isEmpty(mNameStr)) {
                    Toast.makeText(SearchNameActivity.this, path + " 打开失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
                tvSummary.setVisibility(View.GONE);

                // 如果首次加载或是文件大小有改变则检查数据是否正确
                if (mNameStr.length() != mFileInfo.getFileSize()) {
                    mFileInfo.setChecked(false);
                    mFileInfo.setInvalidName("");
                }
                mFileInfo.setFileName(path);
                mFileInfo.setFileSize(mNameStr.length());

                EditText editKeyword = (EditText) findViewById(R.id.edit_keyword);
                String keyword = editKeyword.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    if (path.contains("school")) {
                        searchSchoolTable();
                    } else {
                        String checkResult = "";
                        if (mFileInfo.isChecked()) {
                            checkResult = mFileInfo.getInvalidName();
                        } else {
                            checkResult = checkDataIsValid();
                        }

                        if (!TextUtils.isEmpty(checkResult)) {
                            mFileInfo.setInvalidName(checkResult);
                            Toast.makeText(SearchNameActivity.this, "名字数据有重复:" + checkResult, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        refreshTableView();
                    }
                } else {
                    searchKeywordInNames(keyword);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                String path = getPath(SearchNameActivity.this, uri);
                if (path != null) {
                    PrefsSys.setRecentFileName(path);
                }
            } catch (Exception e) {
                Toast.makeText(SearchNameActivity.this, "解析文件异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void initTitle() {
        TextView textViewTitle = (TextView) findViewById(R.id.commontitle_textview);
        textViewTitle.setText("姓名分析");

        Button btnReturn = (Button) findViewById(R.id.common_btn_left);
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchNameActivity.this.finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.common_btn_right);
        btnRight.setText("隐藏");
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mIsHideBar = !mIsHideBar;
                if (mIsHideBar) {
                    ((Button) v).setText("显示");
                    findViewById(R.id.layout_select).setVisibility(View.GONE);
                    findViewById(R.id.edit_keyword).setVisibility(View.GONE);
                } else {
                    ((Button) v).setText("隐藏");
                    findViewById(R.id.layout_select).setVisibility(View.VISIBLE);
                    findViewById(R.id.edit_keyword).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 初始化表格
     */
    private void initTableView(String[] titles) {
        int itemWidth = GpsUtils.getScreenWidth(SearchNameActivity.this) / 3 - 2;
        int[] columnwidth = { itemWidth + 60, itemWidth + 30, itemWidth - 90 };
        mTableView = (TableView) findViewById(R.id.table_detail);
        mTableView.setColumeWidth(columnwidth);
        mTableView.setTitle(titles);
        mTableView.setDatasArray(mTableData);
        mTableView.buildListView();
    }

    private String checkDataIsValid() {
        mFileInfo.setChecked(true);
        String[] nameAry = GpsUtils.stringToArray(mNameStr, "\r\n");
        for (int i = 0; i < nameAry.length - 1; i++) {
            for (int j = i + 1; j < nameAry.length - 1; j++) {
                // 出现两个名字一样的时候, 判断他们的前一项和后一项如果还是同名的话说明数据有问题
                if (nameAry[i].equals(nameAry[j])) {
                    if (i > 0) {
                        if (nameAry[i - 1].equals(nameAry[j - 1])
                                && nameAry[i + 1].equals(nameAry[j + 1])) {
                            return nameAry[i];
                        }
                    } else if (i < nameAry.length - 2 && j < nameAry.length - 2) {
                        if (nameAry[i + 1].equals(nameAry[j + 1])
                                && nameAry[i + 2].equals(nameAry[j + 2])) {
                            return nameAry[i];
                        }
                    }
                }
            }
        }

        return "";
    }

    /**
     * 更新表格数据
     */
    private void refreshTableView() {
        // 获取姓名的名字部分的拼音MAP
        String[] nameAry = GpsUtils.stringToArray(mNameStr, "\r\n");
        HashMap<String, ArrayList<String>> pinyinMaps = new HashMap<String, ArrayList<String>>(100);
        HashMap<String, Integer> wordFreqMap = new HashMap<String, Integer>(nameAry.length);
        for (int i = 0; i < nameAry.length; i++) {
            // 获取名字的字符串列表
            int nameLen = nameAry[i].length();
            if (nameLen < 2) {
                continue;
            }
            String secondName = nameAry[i].substring(1, nameLen);

            for (int j = 0; j < secondName.length(); j++) {
                String word = String.valueOf(secondName.charAt(j));
                if (wordFreqMap.containsKey(word)) {
                    int count = wordFreqMap.get(word);
                    wordFreqMap.put(word, count + 1);
                } else {
                    wordFreqMap.put(word, 1);
                }
            }

            // 拼音检索不处理单名的情况
            if (nameLen > 2) {
                // 把姓去掉, 只获取名字部分的拼音
                String pinyin = GpsUtils.name2PinyinSzmStr(secondName);
                if (pinyinMaps.containsKey(pinyin)) {
                    pinyinMaps.get(pinyin).add(nameAry[i]);
                } else {
                    ArrayList<String> nameList = new ArrayList<String>();
                    nameList.add(nameAry[i]);
                    pinyinMaps.put(pinyin, nameList);
                }
            }
        }

        // 添加排序接口, 按出现次数进行降序排列
        List<Map.Entry<String, ArrayList<String>>> secNameList;
        secNameList = new ArrayList<Map.Entry<String, ArrayList<String>>>(pinyinMaps.entrySet());
        Collections.sort(secNameList, new Comparator<Map.Entry<String, ArrayList<String>>>() {

            @Override
            public int compare(Entry<String, ArrayList<String>> o1,
                    Entry<String, ArrayList<String>> o2) {
                return (o2.getValue().size() - o1.getValue().size());
            }

        });

        // 输出数据到表格
        String maxName = "姓名总数:" + nameAry.length + "个,";
        mTableData.clear();
        for (Map.Entry<String, ArrayList<String>> mapping : secNameList) {
            ArrayList<String> values = mapping.getValue();
            if (values.size() < 10) {
                continue;
            }
            ArrayList<String> rowData = new ArrayList<String>();
            rowData.add(mapping.getKey());
            rowData.add(values.size() > 0 ? values.get(0) : "无数据");
            rowData.add(String.valueOf(values.size()));
            mTableData.add(rowData);
        }
        // 表格组件
        String[] title = { "拼音", "名字示例", "次数" };
        initTableView(title);

        // 排序并统计名字里出现最多的单字情况
        List<Map.Entry<String, Integer>> wordList;
        wordList = new ArrayList<Map.Entry<String, Integer>>(wordFreqMap.entrySet());
        Collections.sort(wordList, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }

        });

        maxName += "单字出现最多的: ";
        int wordCount = Math.min(10, wordList.size());
        for (int i = 0; i < wordCount; i++) {
            maxName += String.format("%s(%d次),", wordList.get(i).getKey(), wordList.get(i).getValue());
        }

        // 显示简要统计信息
        TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
        tvSummary.setText(maxName);
        tvSummary.setVisibility(View.VISIBLE);
    }

    private void searchKeywordInNames(String keyword) {
        String[] nameAry = GpsUtils.stringToArray(mNameStr, "\r\n");
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < nameAry.length; i++) {
            // 获取名字的字符串列表
            int nameLen = nameAry[i].length();
            String secondName = nameAry[i].substring(1, nameLen);
            if (secondName.contains(keyword)) {
                nameList.add(nameAry[i]);
            }
        }

        mTableData.clear();
        for (int i = 0; i < nameList.size(); i++) {
            ArrayList<String> rowData = new ArrayList<String>();
            rowData.add(String.valueOf(i + 1));
            rowData.add(nameList.get(i));
            rowData.add(String.valueOf(1));
            mTableData.add(rowData);
        }
        String[] title = { "序号", "姓名", "次数" };
        initTableView(title);

        // 显示简要统计信息
        String maxName = String.format("'%s' 累计出现%d次", keyword, nameList.size());
        TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
        tvSummary.setText(maxName);
        tvSummary.setVisibility(View.VISIBLE);
    }

    private void searchSchoolTable() {
        // 获取姓名的名字部分的拼音MAP
        String[] nameAry = GpsUtils.stringToArray(mNameStr, "\r\n");
        HashMap<String, ArrayList<String>> countyMaps = new HashMap<String, ArrayList<String>>(100);
        HashMap<String, Integer> schoolFreqMap = new HashMap<String, Integer>(nameAry.length);
        for (int i = 0; i < nameAry.length; i++) {
            // 获取名字的字符串列表
            int nameLen = nameAry[i].length();
            if (nameLen < 3) {
                continue;
            }
            // 获取区名和校名
            String county = nameAry[i].substring(0, 3);
            String secondName = nameAry[i].substring(3, nameLen);
            if (schoolFreqMap.containsKey(nameAry[i])) {
                int count = schoolFreqMap.get(nameAry[i]);
                schoolFreqMap.put(nameAry[i], count + 1);
            } else {
                schoolFreqMap.put(nameAry[i], 1);
            }

            if (countyMaps.containsKey(county)) {
                countyMaps.get(county).add(secondName);
            } else {
                ArrayList<String> nameList = new ArrayList<String>();
                nameList.add(secondName);
                countyMaps.put(county, nameList);
            }
        }

        // 添加排序接口, 按出现次数进行降序排列
        List<Map.Entry<String, ArrayList<String>>> secNameList;
        secNameList = new ArrayList<Map.Entry<String, ArrayList<String>>>(countyMaps.entrySet());
        Collections.sort(secNameList, new Comparator<Map.Entry<String, ArrayList<String>>>() {

            @Override
            public int compare(Entry<String, ArrayList<String>> o1,
                    Entry<String, ArrayList<String>> o2) {
                return (o2.getValue().size() - o1.getValue().size());
            }

        });

        // 排序并统计学校的个数
        List<Map.Entry<String, Integer>> schoolList;
        schoolList = new ArrayList<Map.Entry<String, Integer>>(schoolFreqMap.entrySet());
        Collections.sort(schoolList, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }

        });

        // 输出数据到表格
        mTableData.clear();
        for (Map.Entry<String, Integer> mapping : schoolList) {
            int nameLen = mapping.getKey().length();
            if (nameLen < 3) {
                continue;
            }
            // 获取区名和校名
            String county = mapping.getKey().substring(0, 3);
            if (county.equals("思明区") || county.equals("湖里区")) {
                continue;
            }
            String secondName = mapping.getKey().substring(3, nameLen);
            ArrayList<String> rowData = new ArrayList<String>();
            rowData.add(county);
            rowData.add(secondName);
            rowData.add(String.valueOf(mapping.getValue()));
            mTableData.add(rowData);
        }
        String[] title = { "地区", "学校", "次数" };
        initTableView(title);

        String maxName = "样本数量" + nameAry.length;
        maxName += "\n各区报名人数: ";
        int wordCount = Math.min(6, secNameList.size());
        for (int i = 0; i < wordCount; i++) {
            maxName += String.format("%s(%d人),", secNameList.get(i).getKey(), secNameList.get(i).getValue().size());
        }

        // 显示简要统计信息
        TextView tvSummary = (TextView) findViewById(R.id.text_summary_result);
        tvSummary.setText(maxName);
        tvSummary.setVisibility(View.VISIBLE);
    }

    private OnClickListener selFileListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // intent.setType(“image/*”);//选择图片
            // intent.setType(“audio/*”); //选择音频
            // intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
            // intent.setType(“video/*;image/*”);//同时选择视频和图片
            intent.setType("*/*");// 无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        }
    };

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
