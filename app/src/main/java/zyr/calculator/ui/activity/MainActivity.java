package zyr.calculator.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zyr.calculator.R;
import zyr.calculator.adapter.MyViewPagerAdapter;
import zyr.calculator.ui.fragment.BMICalculateFragment;
import zyr.calculator.ui.fragment.BasicCalculateFragment;
import zyr.calculator.ui.fragment.TaxCalculateFragment;

/**
 * 主界面的Activity包含3个Fragment（）
 *
 * @ author LTP
 * @ time 16/5/18
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        BasicCalculateFragment basicCalculateFragment = new BasicCalculateFragment();
        TaxCalculateFragment taxCalculateFragment = new TaxCalculateFragment();
        BMICalculateFragment bmiCalculateFragment = new BMICalculateFragment();
        fragmentList.add(basicCalculateFragment);
        fragmentList.add(taxCalculateFragment);
        fragmentList.add(bmiCalculateFragment);
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(this);
        final RadioButton radioButton_basic_calculator = (RadioButton) findViewById(R.id.radioButton_basic_calculator);
        final RadioButton radioButton_tax_calculator = (RadioButton) findViewById(R.id.radioButton_tax_calculator);
        final RadioButton radioButton_bmi_calculator = (RadioButton) findViewById(R.id.radioButton_bmi_calculator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioButton_basic_calculator.setBackgroundColor(getResources().getColor(R.color.orange));
                        radioButton_tax_calculator.setBackgroundColor(Color.WHITE);
                        radioButton_bmi_calculator.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        radioButton_basic_calculator.setBackgroundColor(Color.WHITE);
                        radioButton_tax_calculator.setBackgroundColor(getResources().getColor(R.color.orange));
                        radioButton_bmi_calculator.setBackgroundColor(Color.WHITE);
                        break;
                    case 2:
                        radioButton_basic_calculator.setBackgroundColor(Color.WHITE);
                        radioButton_tax_calculator.setBackgroundColor(Color.WHITE);
                        radioButton_bmi_calculator.setBackgroundColor(getResources().getColor(R.color.orange));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton_basic_calculator:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radioButton_tax_calculator:
                viewPager.setCurrentItem(1);
                break;
            case R.id.radioButton_bmi_calculator:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 是否退出程序
     */
    private boolean isExit;

    /**
     * 定时触发器
     */
    private Timer timer = null;

    @Override
    public void onBackPressed() {
        // TODO 重写onBackPressed(返回键)实现两次快速点击才退出
        if (!isExit) {
            isExit = true;
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            };
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            timer.schedule(timerTask, 2000);
        } else {
            finish();
        }
    }
}
