package zh.com.dialog_picker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
*@date 创建时间 :2018/1/31 18:07
*@author zh_legend
*@Emial code_legend@163.com
*@Description 设置身高的dialog
*@version 1.0
*/
public class HighPickerDialog extends Dialog {

    private Params params;

    public HighPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(HighPickerDialog.Params params) {
        this.params = params;
    }


    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopData.setCurrentItem(params.initSelection);
            }
        }
    }

    public interface OnDataSelectedListener {
        void onDataSelected(String itemValue);

        void setCurrentItem(int position);
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        //这里要设置三个view
        private LoopView loopData;
        //设置位置
        private int position;
        private String title;
        private String unit;
        private int initSelection;
        //设置选择监听回调
        private OnDataSelectedListener callback;
        private final List<String> dataList = new ArrayList<>();
    }

    public static class Builder {
        private final Context context;
        private final HighPickerDialog.Params params;

        public Builder(Context context, int position) {
            this.context = context;
            params = new HighPickerDialog.Params();
            params.position = position;
        }

        private final String getCurrDateValue() {
            return params.loopData.getCurrentItemValue();
        }

        public Builder setData(List<String> dataList) {
            params.dataList.clear();
            params.dataList.addAll(dataList);
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setUnit(String unit) {
            params.unit = unit;
            return this;
        }

        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }

        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public HighPickerDialog create() {
            final HighPickerDialog dialog = new HighPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_high, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tx_title);
                txTitle.setText(params.title);
            }


            final LoopView loopData = (LoopView) view.findViewById(R.id.loop_data);
            loopData.setArrayList(params.dataList);
            loopData.setCurrentItem(params.position);
            loopData.setNotLoop();
            if (params.dataList.size() > 0)
                loopData.setCurrentItem(params.position);
                view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getCurrDateValue());
                    params.callback.setCurrentItem(loopData.getCurrentItem());
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);
            //设置不同的params的参数
            params.loopData = loopData;
            dialog.setParams(params);

            return dialog;
        }
    }
}
