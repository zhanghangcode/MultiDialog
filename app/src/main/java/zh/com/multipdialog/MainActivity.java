package zh.com.multipdialog;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zh.com.dialog_picker.BwhPickerDialog;
import zh.com.dialog_picker.DataPickerDialog;
import zh.com.dialog_picker.DatePickerDialog;
import zh.com.dialog_picker.HighPickerDialog;
import zh.com.dialog_picker.RegionPickerDialog;

/**
*@date 创建时间 :2018/1/31 18:05
*@author zh_legend
*@Emial code_legend@163.com
*@Description 高仿QQ时间选择器
*@version 1.0
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDate();
            }
        });

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRegion();
            }
        });

    }

    /**
     * 测试选择数据
     */
    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});

        DataPickerDialog dialog = builder.setUnit("单位").setData(data).setSelection(1).setTitle("标题")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                    }
                }).create();

        dialog.show();
    }

    /**
     * 用户选择身高
     * @param v
     */
    public void high(View v) {
        String string = getSharedPreferences("tag", MODE_PRIVATE).getString("positions", "2");
        int i = Integer.parseInt(string);
        HighPickerDialog.Builder builder = new HighPickerDialog.Builder(this,i);

        List<String> data = new ArrayList<>();
        for (int l = 150; l < 200; l++) {
            data.add(l + "cm");
        }
        HighPickerDialog dialog = builder.setUnit("cm").setData(data).setSelection(1).setTitle("标题")
                .setOnDataSelectedListener(new HighPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setCurrentItem(int position) {
                        getSharedPreferences("tag",MODE_PRIVATE).edit().putString("positions",position+"").commit();

                    }
                }).create();

        dialog.show();
    }

    /**
     * 展示三围的dialog
     * @param v
     */
    public void sanwei(View v) {
        final int bz, wz, hz;

        String string = getSharedPreferences("tag", MODE_PRIVATE).getString("position", "50#50#50");
        String[] split = string.split("#");
        String s = split[0];
        String s1 = split[1];
        String s2 = split[2];
        int i1 = Integer.parseInt(s);
        int i2 = Integer.parseInt(s1);
        int i3 = Integer.parseInt(s2);

        BwhPickerDialog.Builder builder = new BwhPickerDialog.Builder(this, i1, i2 ,i3);
        List<String> data = new ArrayList<>();

        for (int i = 50; i < 150; i++) {
            data.add(i + "");
        }
        BwhPickerDialog dialog = builder.setData(data)
                .setOnDataSelectedListener(new BwhPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(int[] dates) {
                        Toast.makeText(getApplicationContext(), dates[0] + "#" + dates[1] + "#" + dates[2], Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setCurrentItem(int b, int w, int h) {
                        Log.e("Tag",b+"ss"+w+"ss"+h);
                        getSharedPreferences("tag",MODE_PRIVATE).edit().putString("position",b+"#"+w+"#"+h).commit();
                    }
                }).create();

        dialog.show();
    }

    /**
     * 展示时间的dialog
     */
    private final void showDialogDate() {
        String string = getSharedPreferences("tag", MODE_PRIVATE).getString("date", "40#0#0");
        String[] split = string.split("#");
        String s = split[0];
        String s1 = split[1];
        String s2 = split[2];
        //分割之后得到的并不是具体的时间，而是年，月，日的位置。
        int i1 = Integer.parseInt(s);
        int i2 = Integer.parseInt(s1);
        int i3 = Integer.parseInt(s2);
        //这里直接创建 DatePickerDialog,同时把值传递给构造方法
        DatePickerDialog.Builder builder=new DatePickerDialog.Builder(this,i1,i2,i3);
        //通过builder设置时间
       DatePickerDialog dialog=builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
           @Override
           public void onDateSelected(int[] dates) {
               Toast.makeText(getApplicationContext(), dates[0] + "#" + dates[1] + "#" + dates[2], Toast.LENGTH_SHORT).show();
           }

           @Override
           public void setCurrentItem(int positionYear, int positionMonth, int positionDay) {
               Log.e("Tag","年"+positionYear+"月"+positionMonth+"日"+positionDay);
               //设置完成时间之后,保存数据。用于下次回显数据。
               getSharedPreferences("tag",MODE_PRIVATE).edit().putString("date",positionYear+"#"+positionMonth+"#"+positionDay).commit();
           }
       }).create();
        //最后显示Dialog
        dialog.show();
    }

    /**
     * 省市联动的dialog
     */
    private final void showDialogRegion() {

        RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(this);

        RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnRegionSelectedListener() {
            @Override
            public void onRegionSelected(String[] cityAndArea) {
                Toast.makeText(getApplicationContext(), cityAndArea[0] + "#" + cityAndArea[1], Toast.LENGTH_SHORT).show();
            }
        }).create();

        dialog.show();
    }

}
