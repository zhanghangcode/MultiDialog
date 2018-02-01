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
*@date 创建时间 :2018/1/31 18:04
*@author zh_legend
*@Emial code_legend@163.com
*@Description 三围的dialog
*@version 1.0
*/
public class BwhPickerDialog extends Dialog {

    private Params params;


    public BwhPickerDialog(Context context, int themeResId) {

        super(context, themeResId);
    }

    private void setParams(BwhPickerDialog.Params params) {

        this.params = params;
    }


    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopB.setCurrentItem(params.initSelection);
            }
        }
    }

    public interface OnDataSelectedListener {
        void onDataSelected(int[] itemValue);

        //分别代表胸围，腰围，臀围
        void setCurrentItem(int b, int w, int h);
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        //设置不同的参数，胸围,腰围,臀围
        private LoopView loopB, loopW, loopH;
        //有数据的时候直接跳转到现在的数据
        public int positionB, positionW, positionH;
        private String title;
        private int initSelection;
        private OnDataSelectedListener callback;
        private final List<String> dataList = new ArrayList<>();
    }

    public static class Builder {
        private final Context context;
        private final BwhPickerDialog.Params params;

        public Builder(Context context, int positionB, int positionW, int positionH) {
            this.context = context;
            params = new BwhPickerDialog.Params();
            params.positionB = positionB;
            params.positionW = positionW;
            params.positionH = positionH;
        }

        private final int[] getCurrDateValue() {
            int currentItemValueB = Integer.parseInt(params.loopB.getCurrentItemValue());
            int currentItemValueW = Integer.parseInt(params.loopW.getCurrentItemValue());
            int currentItemValueH = Integer.parseInt(params.loopH.getCurrentItemValue());
            return new int[]{currentItemValueB, currentItemValueW, currentItemValueH};

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


        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }

        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public BwhPickerDialog create() {
            final BwhPickerDialog dialog = new BwhPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_bwh, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tx_title);
                txTitle.setText(params.title);
            }

            //胸围，腰围，臀围
            final LoopView loopDataB = (LoopView) view.findViewById(R.id.loop_b);
            //设置数据
            loopDataB.setArrayList(params.dataList);
            loopDataB.setCurrentItem(params.positionB);
            //设置是否循环
            loopDataB.setNotLoop();
            //腰围
            final LoopView loopDataW = (LoopView) view.findViewById(R.id.loop_w);
            //设置数据
            loopDataW.setArrayList(params.dataList);
            loopDataW.setCurrentItem(params.positionW);
            //设置是否循环
            loopDataW.setNotLoop();
            //腰围
            final LoopView loopDataH = (LoopView) view.findViewById(R.id.loop_h);
            //设置数据
            loopDataH.setArrayList(params.dataList);
            loopDataH.setCurrentItem(params.positionH);
            //设置是否循环
            loopDataH.setNotLoop();


            if (params.dataList.size() > 0)

                loopDataB.setCurrentItem(params.positionB);
            loopDataW.setCurrentItem(params.positionW);
            loopDataH.setCurrentItem(params.positionH);
            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getCurrDateValue());
                    params.callback.setCurrentItem(loopDataB.getCurrentItem(), loopDataW.getCurrentItem(), loopDataH.getCurrentItem());
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
            //设置胸围
            params.loopB = loopDataB;
            params.loopW = loopDataW;
            params.loopH = loopDataH;

            dialog.setParams(params);
            return dialog;
        }


    }
}
