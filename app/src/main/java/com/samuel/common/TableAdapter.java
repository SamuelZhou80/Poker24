package com.samuel.common;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samuel.main.GpsUtils;
import com.samuel.mytools.R;

/**
 * 表格控件使用的适配器
 * @author 解玉芳
 *
 */

public class TableAdapter extends BaseAdapter {
    private Context context;
    private List<TableRow> table;

    public TableAdapter(Context context, List<TableRow> table) {
        this.context = context;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TableRow getItem(int position) {
        return table.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableRow tableRow = table.get(position);
        if (convertView == null) {
            return new TableRowView(this.context, tableRow, position);
        } else {
            LinearLayout ll = (LinearLayout) convertView;
            updateConvertView(tableRow, ll);
            return convertView;
        }
    }

    /**
     * convertview存在时刷新view，并不重新new
     *
     * @author xieyufang
     */
    private void updateConvertView(TableRow tableRow, LinearLayout ll){
        for (int i = 0; i < tableRow.getSize(); i++) {
            if (tableRow.getCellValue(i).type == TableCell.STRING) {// 如果格单元是文本内容
                TextView tv = (TextView) ll.getChildAt(i);
                tv.setText((CharSequence) tableRow.getCellValue(i).value);
            } else if (tableRow.getCellValue(i).type == TableCell.IMAGE) {// 如果格单元是图片
                LinearLayout layout = (LinearLayout)ll.getChildAt(i);
                ImageView imgCell = (ImageView) layout.getChildAt(0);
                imgCell.setImageResource(GpsUtils.strToInt((String) tableRow.getCellValue(i).value));
            }

        }
    }


    /**
     * TableRowView 实现表格行的样式
     *
     * @author hellogv
     */
    class TableRowView extends LinearLayout {
        public TableRowView(Context context, TableRow tableRow, int position) {
            super(context);

            this.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < tableRow.getSize(); i++) {// 逐个格单元添加到行
                TableCell tableCell = tableRow.getCellValue(i);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(tableCell.width, tableCell.height);// 按照格单元指定的大小设置空间
                layoutParams.setMargins(0, 0, 1, 0);// 预留空隙制造边框

                if (tableCell.type == TableCell.STRING) {// 如果格单元是文本内容
                    TextView textCell = new TextView(context);
                    // textCell.setLines(1);
                    textCell.setGravity(Gravity.CENTER);
                    textCell.setBackgroundColor(getResources().getColor(R.color.white));//
                    textCell.setText(String.valueOf(tableCell.value));
                    textCell.setTextColor(getResources().getColor(R.color.text_color));
                    textCell.setPadding(0, GpsUtils.dip2px(context, 10), 0, GpsUtils.dip2px(context, 10));
                    addView(textCell, layoutParams);

                    if (tableCell.listener != null) {
                        textCell.setOnClickListener(tableCell.listener);
                    }
                    if (tableCell.width == 0) {
                        textCell.setVisibility(View.GONE);
                    }
                } else if (tableCell.type == TableCell.IMAGE) {// 如果格单元是图像内容
                    ImageView imgCell = new ImageView(context);
                    imgCell.setBackgroundColor(getResources().getColor(R.color.table_content_bg));// 背景黑色
                    imgCell.setImageResource(GpsUtils.strToInt((String)tableCell.value));
                    LinearLayout layout = new LinearLayout(context);
                    layout.setGravity(Gravity.CENTER);
                    Resources resources = context.getResources();
                    Bitmap bm = BitmapFactory.decodeResource(resources, GpsUtils.strToInt((String)tableCell.value));
                    layout.addView(imgCell, bm.getWidth(), bm.getHeight());
                    layout.setBackgroundColor(getResources().getColor(R.color.white));
                    addView(layout, layoutParams);
                    if (tableCell.listener != null) {
                        layout.setOnClickListener(tableCell.listener);
                    }
                    if (tableCell.width == 0) {
                        layout.setVisibility(View.GONE);
                    }
                }
            }
            this.setBackgroundColor(getResources().getColor(R.color.content_line_color));// 利用空隙来实现边框
        }
    }
    /**
     * TableRow 实现表格的行
     * @author hellogv
     */
    static public class TableRow {
        private TableCell[] cell;
        public TableRow(TableCell[] cell) {
            this.cell = cell;
        }
        public int getSize() {
            return cell.length;
        }
        public TableCell getCellValue(int index) {
            if (index >= cell.length)
                return null;
            return cell[index];
        }
    }
    /**
     * TableCell 实现表格的格单元
     * @author hellogv
     */
    static public class TableCell {
        static public final int STRING = 0;
        static public final int IMAGE = 1;
        public Object value;
        public int width;
        public int height;
        private int type;
        public OnClickListener listener;
        public TableCell(Object value, int width, int height, int type, OnClickListener listener) {
            this.value = value;
            this.width = width;
            this.height = height;
            this.type = type;
            this.listener = listener;
        }
    }
}