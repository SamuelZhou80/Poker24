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
 * ���ؼ� �������б�
 * 
 * @author ����
 * 
 */
public class TableView extends LinearLayout {

    private Context context;
    private ListView lv;
    private OnItemClickListener itemClickEvent; // ĳ�е����Ӧ
    private OnItemLongClickListener itemLongclickListener; // ������Ӧ

    private String[] title;
    private ArrayList<Integer> clickColumeArray = new ArrayList<Integer>();
    private ArrayList<OnClickListener> clickListenerArray = new ArrayList<View.OnClickListener>();
    private int[] columeWidth;
    private ArrayList<ArrayList<String>> datasArray; // ����Դ��ά����
    private LinearLayout titleLayout;

    // ����Դ�������� 0���ַ���TableCell.STRING��1��ͼƬ��TableCell.IMAGE ������Ϊ�����ʾȫΪ�ַ���
    // ��datasArrayͬʱ���� ���뱣֤��datasArray�����鳤��һ��
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
     * �������֮ǰһ��Ҫ�ȴ���title ��datas
     * 
     */
    public void buildListView() {
        if (title == null) {
            return;
        }
        lv = new CornerListView(context);

        int width = GpsUtils.getScreenWidth(context) / title.length;
        // �������

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
                        R.color.content_line_color));// ������ɫ
                textCell1.setWidth(1);
                textCell1.setHeight(GpsUtils.dip2px(context, 30));
                titleLayout.addView(textCell1);
            }
        }

        this.addView(titleLayout);
        TextView textCell1 = new TextView(context);
        textCell1.setGravity(Gravity.CENTER);
        textCell1.setBackgroundColor(getResources().getColor(
                R.color.content_line_color));// ������ɫ
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
    	                R.color.content_line_color));// ������ɫ
    	    textCell2.setHeight(1);
    	    this.addView(textCell2);
        }
    }

    /**
     * ��ȡ��ǰһ�����ӵ�view�������е�λ��
     * 
     */
    public int getPositionItem(View v) {
        return lv.getPositionForView((View) v.getParent());
    }

    /**
     * ˢ�±��
     * 
     */
    public void refreshTableView() {
        getTableRowData();
        tableAdapter.notifyDataSetChanged();
    }

    /**
     * ˢ�±��, ���Ԫ�����������б��ʱ��Ҫ����Newһ��������
     * 
     * @param isNeedNewAdapter
     *            �Ƿ���ҪNewһ��������
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
     * ��ȡ����
     * 
     */
    public String[] getTitle() {
        return title;
    }

    /**
     * ���ñ���
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
     * ��ȡ����п�
     * 
     * @return �����п������
     */
    public int[] getColumeWidth() {
        return columeWidth;
    }

    /**
     * ���ñ���п�
     * 
     */
    public void setColumeWidth(int[] columeWidth) {
        this.columeWidth = columeWidth;
    }

    /**
     * ��ȡ������������, ���ж�����
     * 
     * @return ��������
     */
    public int getTableRowCount() {
        return datasArray.size();
    }

    /**
     * ���ÿ��Ե�����м����Ӧ����Ӧ
     * 
     */
    public void setClickColume(int clickColume, OnClickListener clickListener) {
        clickColumeArray.add(clickColume);
        clickListenerArray.add(clickListener);
    }

    /**
     * ��ȡ�������
     * 
     */
    private void getTableRowData() {
        int type;

        tableRowData.clear();
        TableCell[] cells1 = new TableCell[title.length];
        // �ѱ�������ӵ����
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
