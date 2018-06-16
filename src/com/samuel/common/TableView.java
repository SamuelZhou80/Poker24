package com.samuel.common;

import java.util.ArrayList;

import com.samuel.common.TableAdapter.TableCell;
import com.samuel.common.TableAdapter.TableRow;
import com.samuel.twentyfour.GpsUtils;
import com.samuel.twentyfour.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 表格控件 本质是列表
 * 
 * @author 解玉芳
 * 
 */
public class TableView extends LinearLayout {

    private Context context;
    private ListView lv;
    private OnItemClickListener itemClickEvent; // 某行点击响应
    private OnItemLongClickListener itemLongclickListener; // 长按响应

    private String[] title;
    private ArrayList<Integer> clickColumeArray = new ArrayList<Integer>();
    private ArrayList<OnClickListener> clickListenerArray = new ArrayList<View.OnClickListener>();
    private int[] columeWidth;
    private ArrayList<ArrayList<String>> datasArray; // 数据源二维数组
    private LinearLayout titleLayout;

    // 数据源类型数组 0：字符串TableCell.STRING，1：图片，TableCell.IMAGE 若对象为空则表示全为字符串
    // 和datasArray同时传入 ，须保证与datasArray的数组长度一致
    private ArrayList<ArrayList<Integer>> dataTypesArray;

    private TableAdapter tableAdapter;
    private ArrayList<TableRow> tableRowData = new ArrayList<TableAdapter.TableRow>();

    public TableView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public TableView(Context context, OnItemClickListener itemClickEvent) {
        super(context);
        init(context);
        this.context = context;
        this.itemClickEvent = itemClickEvent;
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setOrientation(LinearLayout.VERTICAL);

    }

    /**
     * 创建表格，之前一定要先传入title 和datas
     * 
     */
    public void buildListView() {
        if (title == null) {
            return;
        }
        lv = new CornerListView(context);

        int width = GpsUtils.getScreenWidth(context) / title.length;
        // 定义标题

        if (columeWidth == null) {
            columeWidth = new int[title.length];
            for (int i = 0; i < title.length; i++) {
                columeWidth[i] = width;
            }
        }
        titleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayout.setLayoutParams(params);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < title.length; i++) {
            TextView textCell = new TextView(context);
            textCell.setLines(1);
            textCell.setGravity(Gravity.CENTER);
            textCell.setBackgroundColor(getResources().getColor(
                    R.color.table_title_bg));
            textCell.setTextColor(getResources().getColor(
                    R.color.table_title_text));
            textCell.setText(title[i]);
            textCell.setWidth(columeWidth[i]);
            textCell.setHeight(GpsUtils.dip2px(context, 30));
            titleLayout.addView(textCell);
            if (i != title.length - 1) {
                TextView textCell1 = new TextView(context);
                textCell1.setGravity(Gravity.CENTER);
                textCell1.setBackgroundColor(getResources().getColor(
                        R.color.content_line_color));// 背景黑色
                textCell1.setWidth(1);
                textCell1.setHeight(GpsUtils.dip2px(context, 30));
                titleLayout.addView(textCell1);
            }
        }

        this.addView(titleLayout);
        TextView textCell1 = new TextView(context);
        textCell1.setGravity(Gravity.CENTER);
        textCell1.setBackgroundColor(getResources().getColor(
                R.color.content_line_color));// 背景黑色
        textCell1.setHeight(1);
        this.addView(textCell1);

        if (datasArray == null) {
            return;
        }
        getTableRowData();
        tableAdapter = new TableAdapter(context, tableRowData);

        lv.setAdapter(tableAdapter);
        if (itemClickEvent != null) {
            lv.setOnItemClickListener(itemClickEvent);
        }
        if (itemLongclickListener != null) {
            lv.setOnItemLongClickListener(itemLongclickListener);
        }
        lv.setDivider(getResources().getDrawable(R.color.content_line_color));
        lv.setDividerHeight(1);
        lv.setScrollbarFadingEnabled(false);
        lv.setVerticalScrollBarEnabled(false);
        lv.setHorizontalScrollBarEnabled(false);
        ScrollView sv = new ScrollView(context);
        this.addView(sv);
        sv.addView(lv);

        if(tableRowData.size() > 0){
      	    TextView textCell2 = new TextView(context);
    	    textCell2.setGravity(Gravity.CENTER);
    	    textCell2.setBackgroundColor(getResources().getColor(
    	                R.color.content_line_color));// 背景黑色
    	    textCell2.setHeight(1);
    	    this.addView(textCell2);
        }
    }

    /**
     * 获取当前一个格子的view处于哪行的位置
     * 
     */
    public int getPositionItem(View v) {
        return lv.getPositionForView((View) v.getParent());
    }

    /**
     * 刷新表格
     * 
     */
    public void refreshTableView() {
        getTableRowData();
        tableAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新表格, 表格单元的数据类型有变更时需要重新New一个适配器
     * 
     * @param isNeedNewAdapter
     *            是否需要New一个适配器
     */
    public void refreshTableView(boolean isNeedNewAdapter) {
        getTableRowData();
        if (isNeedNewAdapter) {
            tableAdapter = new TableAdapter(context, tableRowData);
            lv.setAdapter(tableAdapter);
        }
        tableAdapter.notifyDataSetChanged();
    }

    /**
     * 获取标题
     * 
     */
    public String[] getTitle() {
        return title;
    }

    /**
     * 设置标题
     * 
     */
    public void setTitle(String[] title) {
        this.title = title;
    }
    
    public void hideTitle(boolean isHide) {
    	if (titleLayout != null) {
    		if (isHide) {
    			titleLayout.setVisibility(View.GONE);
    		} else {
    			titleLayout.setVisibility(View.VISIBLE);
    		}
    	}
    }

    /**
     * 获取表格列宽
     * 
     * @return 表格的列宽度数组
     */
    public int[] getColumeWidth() {
        return columeWidth;
    }

    /**
     * 设置表格列宽
     * 
     */
    public void setColumeWidth(int[] columeWidth) {
        this.columeWidth = columeWidth;
    }

    /**
     * 获取表格的数据条数, 即有多少行
     * 
     * @return 数据条数
     */
    public int getTableRowCount() {
        return datasArray.size();
    }

    /**
     * 设置可以点击的列及其对应的响应
     * 
     */
    public void setClickColume(int clickColume, OnClickListener clickListener) {
        clickColumeArray.add(clickColume);
        clickListenerArray.add(clickListener);
    }

    /**
     * 获取表格数据
     * 
     */
    private void getTableRowData() {
        int type;

        tableRowData.clear();
        TableCell[] cells1 = new TableCell[title.length];
        // 把表格的行添加到表格
        for (int i = 0; i < datasArray.size(); i++) {
            cells1 = new TableCell[title.length];
            for (int j = 0; j < cells1.length; j++) {
                type = (dataTypesArray == null) ? TableCell.STRING
                        : dataTypesArray.get(i).get(j);
                cells1[j] = new TableCell(datasArray.get(i).get(j),
                        columeWidth[j], LayoutParams.MATCH_PARENT, type, null);

                if (clickColumeArray.contains(j)) {
                    int index = clickColumeArray.indexOf(j);
                    if (clickListenerArray.get(index) != null) {
                        cells1[j].listener = clickListenerArray.get(index);
                    }
                }
            }

            tableRowData.add(new TableRow(cells1));
        }
    }

    public OnItemClickListener getItemClickEvent() {
        return itemClickEvent;
    }

    public void setItemClickEvent(OnItemClickListener itemClickEvent) {
        this.itemClickEvent = itemClickEvent;
    }

    public ArrayList<ArrayList<String>> getDatasArray() {
        return datasArray;
    }

    public void setDatasArray(ArrayList<ArrayList<String>> datasArray) {
        this.datasArray = datasArray;
    }

    public ArrayList<ArrayList<Integer>> getDataTypesArray() {
        return dataTypesArray;
    }

    public void setDataTypesArray(ArrayList<ArrayList<Integer>> dataTypesArray) {
        this.dataTypesArray = dataTypesArray;
    }

    public OnItemLongClickListener getItemLongclickListener() {
        return itemLongclickListener;
    }

    public void setItemLongClickListener(
            OnItemLongClickListener itemLongclickListener) {
        this.itemLongclickListener = itemLongclickListener;
    }
}
