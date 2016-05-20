package zyr.calculator.ui.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import zyr.calculator.R;
import zyr.calculator.adapter.RecordAdapter;
import zyr.calculator.bean.Record;
import zyr.calculator.dao.BmiDao;
import zyr.calculator.utils.SharedPreferencesUtils;

/**
 * BMI计算界面的Fragment隶属于MainActivity
 *
 * @ author LTP
 * @ time 16/5/18
 */
public class BMICalculateFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    /**
     * 身高、体重
     */
    private EditText editText_height, editText_weight;

    /**
     * 查询结果
     */
    private TextView textView_BMI_result;

    private ListView listView_bmi_history;
    private RelativeLayout relativeLayout_result, relativeLayout_history;
    /**
     * 查询记录集合
     */
    private List<Record> recordList;
    private Button button_record;
    private boolean isShowHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bmi_calculate, container, false);
        initView(rootView);
        return rootView;
    }

    /**
     * 控件初始化
     *
     * @param rootView Fragment的父布局
     */
    @SuppressLint("SetTextI18n")
    private void initView(View rootView) {
        editText_height = (EditText) rootView.findViewById(R.id.editText_height);
        // 从SharedPreferences中取出上次输入的身高值并显示到身高输入框
        editText_height.setText(SharedPreferencesUtils.getParam(getContext(), "String", "") + "");
        editText_weight = (EditText) rootView.findViewById(R.id.editText_weight);
        textView_BMI_result = (TextView) rootView.findViewById(R.id.textView_BMI_result);
        rootView.findViewById(R.id.button_cal).setOnClickListener(this);
        button_record = (Button) rootView.findViewById(R.id.button_record);
        button_record.setOnClickListener(this);
        rootView.findViewById(R.id.relativeLayout_bmi).setOnClickListener(this);
        relativeLayout_result = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_result);
        relativeLayout_history = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history);
        listView_bmi_history = (ListView) rootView.findViewById(R.id.listView_bmi_history);
        listView_bmi_history.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cal:
                isShowHistory = false;
                updateView(false);
                relativeLayout_history.setVisibility(View.GONE);
                relativeLayout_result.setVisibility(View.VISIBLE);
                String heightStr = editText_height.getText().toString();
                String weightStr = editText_weight.getText().toString();
                if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
                    Toast.makeText(getContext(), "您输入的身高或体重为空!", Toast.LENGTH_SHORT).show();
                } else {
                    double height = Double.parseDouble(heightStr);
                    double weight = Integer.parseInt(weightStr);
                    if (height > 299) {
                        Toast.makeText(getContext(), "身高须在0-299cm之间", Toast.LENGTH_SHORT).show();
                    } else if (weight > 499) {
                        Toast.makeText(getContext(), "体重须在0-499kg之间", Toast.LENGTH_SHORT).show();
                    } else {
                        double bmi = obtainBMI(height, weight);
                        String result = obtainResult(bmi);
                        saveData(height, weight, bmi, result);
                    }
                }
                break;
            case R.id.button_record:
                isShowHistory = !isShowHistory;
                loadRecordData();
                updateView(isShowHistory);
                break;
            default:
                break;
        }
        // 隐藏输入法界面
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 点击 显示历史/显示结果 后更新View
     *
     * @param isShowHistory 是否显示历史
     */
    private void updateView(boolean isShowHistory) {
        if (isShowHistory) {
            relativeLayout_result.setVisibility(View.GONE);
            relativeLayout_history.setVisibility(View.VISIBLE);
            button_record.setText("显示结果");
        } else {
            relativeLayout_result.setVisibility(View.VISIBLE);
            relativeLayout_history.setVisibility(View.GONE);
            button_record.setText("显示历史");
        }
    }

    /**
     * 计算出BMI的值
     *
     * @param height 身高
     * @param weight 体重
     * @return BMI值
     */
    private double obtainBMI(double height, double weight) {
        double bmi_ = weight / Math.pow(height / 100, 2);
        // 四舍五入保留两位小数
        return Math.round(bmi_ * 100) / 100.0;
    }

    /**
     * 通过bmi来判别结果并输出结果
     *
     * @param bmi BMI值
     * @return 结果：过轻/正常/过重/肥胖/非常肥胖
     */
    @SuppressLint("SetTextI18n")
    private String obtainResult(double bmi) {
        String result;
        if (bmi < 18.5) {
            textView_BMI_result.setTextColor(Color.YELLOW);
            textView_BMI_result.setText("BMI=" + bmi + "\n\t  过轻!");
            result = "过轻!";
        } else if (bmi <= 24.99 && bmi >= 18.5) {
            textView_BMI_result.setTextColor(Color.GREEN);
            textView_BMI_result.setText("BMI=" + bmi + "\n\t\t\t\t\t正常!");
            result = "正常!";
        } else if (bmi <= 28 && bmi >= 25) {
            textView_BMI_result.setTextColor(Color.BLUE);
            textView_BMI_result.setText("BMI=" + bmi + "\n\t\t\t\t\t过重!");
            result = "过重!";
        } else if (bmi <= 32 && bmi >= 28) {
            textView_BMI_result.setTextColor(Color.CYAN);
            textView_BMI_result.setText("BMI=" + bmi + "\n\t\t\t\t\t肥胖!");
            result = "肥胖!";
        } else {
            textView_BMI_result.setTextColor(Color.RED);
            textView_BMI_result.setText("BMI=" + bmi + "\n\t非常肥胖!");
            result = "非常肥胖!";
        }
        // 播放缩放动画
        Animation scale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_result_scale);
        textView_BMI_result.startAnimation(scale);
        return result;
    }

    /**
     * 保存数据相关
     *
     * @param height 身高
     * @param weight 体重
     * @param bmi    BMI值
     * @param result 结果
     */
    private void saveData(double height, double weight, double bmi, String result) {
        // 把身高存到SharedPreferences中，以便第二次启动无需输入
        SharedPreferencesUtils.setParam(getContext(), "String", height + "");
        long currentMills = System.currentTimeMillis();
        Record record = new Record(height, weight, bmi, result, currentMills);
        Log.d("LTP", record.toString());
        BmiDao bmiDao = BmiDao.getInstance(getContext());
        bmiDao.saveRecord(record);
    }

    /**
     * 从数据库查询所有记录
     */
    private void loadRecordData() {
        recordList = BmiDao.getInstance(getContext()).queryRecord();
        // 把记录倒排序（相当于按时间前后排序）
        Collections.reverse(recordList);
        RecordAdapter adapter = new RecordAdapter(getContext(), R.layout.listview_item_bmi_record, recordList);
        listView_bmi_history.setAdapter(adapter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO ListView的长按回调函数
        final Record record = recordList.get(position);
        // 获取时间属性作为删除数据库一行的依据
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()).setItems(new String[]{"删除", "清空"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 选择删除记录的回调
                        switch (which) {
                            case 0:
                                // 当点击删除时根据时间属性从数据表删除一整行
                                BmiDao.getInstance(getContext()).deleteRecord(record);
                                break;
                            case 1:
                                // 当点击清空时清空数据表
                                BmiDao.getInstance(getContext()).deleteAllRecord();
                                break;

                            default:
                                break;
                        }
                        loadRecordData();
                    }
                });
        dialog.show();
        return true;
    }
}
