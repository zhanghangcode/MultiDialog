package zh.com.dialog_picker;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
*@date 创建时间 :2018/1/31 18:07
*@author zh_legend
*@Emial code_legend@163.com
*@Description 设置日期的dialog
*@version 1.0
*/
public class DatePickerDialog extends Dialog {

    private static final int MIN_YEAR = 1950;
    private Params params;

    public DatePickerDialog(Context context, int themeResId) {

        super(context, themeResId);

    }

    private void setParams(DatePickerDialog.Params params) {

        this.params = params;
    }

    public interface OnDateSelectedListener {

         void onDateSelected(int[] dates);
         void setCurrentItem(int positionYear, int positionMonth, int positionDay);
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopYear, loopMonth, loopDay;
        public  int positionYear,positionMonth,positionDay;
        private OnDateSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final DatePickerDialog.Params params;

        public Builder(Context context, int positionYear, int positionMonth, int positionDay) {
            this.context = context;
            params = new DatePickerDialog.Params();
            params.positionYear=positionYear;
            params.positionMonth=positionMonth;
            params.positionDay=positionDay;
        }

        /**
         * 获取当前选择的日期
         *
         * @return int[]数组形式返回。例[1990,6,15]
         */
        private final int[] getCurrDateValues() {
            int currYear = Integer.parseInt(params.loopYear.getCurrentItemValue().substring(0,4));
            int currMonth = Integer.parseInt(params.loopMonth.getCurrentItemValue().substring(0,2));
            int currDay = Integer.parseInt(params.loopDay.getCurrentItemValue().substring(0,2));
            return new int[]{currYear, currMonth, currDay};
        }

        public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
            params.callback = onDateSelectedListener;
            return this;
        }


        public DatePickerDialog create() {
            final DatePickerDialog dialog = new DatePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
//            final DatePickerDialog dialog = new DatePickerDialog(context, params.shadow ? R.style.LoadDialog : R.style.LoadDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date, null);

            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            loopDay.setArrayList(d(1, 30,"日"));
            loopDay.setCurrentItem(params.positionDay);

            loopDay.setNotLoop();

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            //设置年
            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            loopYear.setArrayList(d(MIN_YEAR, year - MIN_YEAR + 1,"年"));
            Log.e("Tag",""+(year - MIN_YEAR - 27));
            loopYear.setCurrentItem(params.positionYear);
            loopYear.setNotLoop();
            //设置月
            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            loopMonth.setArrayList(d(1, 12,"月"));
            loopMonth.setCurrentItem(params.positionMonth);
            loopMonth.setNotLoop();

            final LoopListener maxDaySyncListener = new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    Calendar c = Calendar.getInstance();
//                    Log.e("Main","得到的数据"+loopYear.getCurrentItemValue().toString()+"替换后"+loopYear.getCurrentItemValue().substring(0,4));
//                    str=str.replace(str.charAt(str.length-1)+"","新字符");
//                    c.set(Integer.parseInt(loopYear.getCurrentItemValue()), Integer.parseInt(loopMonth.getCurrentItemValue()) - 1, 1);
                    c.set(Integer.parseInt(loopYear.getCurrentItemValue().substring(0,4)), Integer.parseInt(loopMonth.getCurrentItemValue().substring(0,2)) - 1, 1);
                    c.roll(Calendar.DATE, false);
                    int maxDayOfMonth = c.get(Calendar.DATE);
                    int fixedCurr = loopDay.getCurrentItem();
                    loopDay.setArrayList(d(1, maxDayOfMonth,"日"));
                    // 修正被选中的日期最大值
                    if (fixedCurr > maxDayOfMonth) fixedCurr = maxDayOfMonth - 1;
                    loopDay.setCurrentItem(fixedCurr);
                }
            };
            loopYear.setListener(maxDaySyncListener);
            loopMonth.setListener(maxDaySyncListener);

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDateSelected(getCurrDateValues());
                    params.callback.setCurrentItem(loopYear.getCurrentItem(),loopMonth.getCurrentItem(),loopDay.getCurrentItem());
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置dialog的高度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopYear = loopYear;
            params.loopMonth = loopMonth;
            params.loopDay = loopDay;
            dialog.setParams(params);

            return dialog;
        }

        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count, String str) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;

                values[i - startNum] = tempValue+str;
            }
            return Arrays.asList(values);
        }
    }
}
