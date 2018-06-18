package com.samuel.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.samuel.twentyfour.R;

public class SelectPokerDialogView extends Dialog {
    private ConfirmListener listener;
    private CancelListener cancelListener;
    private View view;
    private Context context;
    private int mCurValue = 0;

    public SelectPokerDialogView(Context context, ConfirmListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public SelectPokerDialogView(Context context, ConfirmListener listener, int value) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.mCurValue = value;
    }

    public interface ConfirmListener {
        void onConfirm(int num);
    }

    public interface MiddleListener {
        void onConfirm();
    }

    public interface CancelListener {
        void onConfirm();
    }

    @SuppressLint("InflateParams")
    private void Init(Context context) {
        // 找到dialog的布局文件
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.poker_dialog_view, null);

        TextView tvNum1 = (TextView) view.findViewById(R.id.text_num1);
        TextView tvNum2 = (TextView) view.findViewById(R.id.text_num2);
        TextView tvNum3 = (TextView) view.findViewById(R.id.text_num3);
        TextView tvNum4 = (TextView) view.findViewById(R.id.text_num4);
        TextView tvNum5 = (TextView) view.findViewById(R.id.text_num5);
        TextView tvNum6 = (TextView) view.findViewById(R.id.text_num6);
        TextView tvNum7 = (TextView) view.findViewById(R.id.text_num7);
        TextView tvNum8 = (TextView) view.findViewById(R.id.text_num8);
        TextView tvNum9 = (TextView) view.findViewById(R.id.text_num9);
        TextView tvNum10 = (TextView) view.findViewById(R.id.text_num10);

        tvNum1.setOnClickListener(pokerClickListener);
        tvNum2.setOnClickListener(pokerClickListener);
        tvNum3.setOnClickListener(pokerClickListener);
        tvNum4.setOnClickListener(pokerClickListener);
        tvNum5.setOnClickListener(pokerClickListener);
        tvNum6.setOnClickListener(pokerClickListener);
        tvNum7.setOnClickListener(pokerClickListener);
        tvNum8.setOnClickListener(pokerClickListener);
        tvNum9.setOnClickListener(pokerClickListener);
        tvNum10.setOnClickListener(pokerClickListener);

        int selcolor = context.getResources().getColor(R.color.orange);
        switch (mCurValue) {
        case 1:
            tvNum1.setTextColor(selcolor);
            break;
        case 2:
            tvNum2.setTextColor(selcolor);
            break;
        case 3:
            tvNum3.setTextColor(selcolor);
            break;
        case 4:
            tvNum4.setTextColor(selcolor);
            break;
        case 5:
            tvNum5.setTextColor(selcolor);
            break;
        case 6:
            tvNum6.setTextColor(selcolor);
            break;
        case 7:
            tvNum7.setTextColor(selcolor);
            break;
        case 8:
            tvNum8.setTextColor(selcolor);
            break;
        case 9:
            tvNum9.setTextColor(selcolor);
            break;
        case 10:
            tvNum10.setTextColor(selcolor);
            break;
        default:
            break;
        }

        // 设置dialog的布局,并显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init(context);
    }

    private android.view.View.OnClickListener pokerClickListener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int number = 0;
            if (id == R.id.text_num1) {
                number = 1;
            } else if (id == R.id.text_num2) {
                number = 2;
            } else if (id == R.id.text_num3) {
                number = 3;
            } else if (id == R.id.text_num4) {
                number = 4;
            } else if (id == R.id.text_num5) {
                number = 5;
            } else if (id == R.id.text_num6) {
                number = 6;
            } else if (id == R.id.text_num7) {
                number = 7;
            } else if (id == R.id.text_num8) {
                number = 8;
            } else if (id == R.id.text_num9) {
                number = 9;
            } else if (id == R.id.text_num10) {
                number = 10;
            }
            if (listener != null) {
                listener.onConfirm(number);
            }
            dismiss();
        }
    };

    @Override
    public void cancel() {
        super.cancel();
        if (cancelListener != null) {
            cancelListener.onConfirm();
        }
    }

}
