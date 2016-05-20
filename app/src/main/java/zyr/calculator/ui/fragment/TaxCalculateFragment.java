package zyr.calculator.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import zyr.calculator.R;

/**
 * 税率计算界面的Fragment隶属于MainActivity
 *
 * @ author LTP
 * @ date 16/5/19
 */
public class TaxCalculateFragment extends Fragment implements View.OnClickListener {

    private EditText editText_tax_before, editText_social_insurance;
    private Spinner spinner_tax_threshold;
    private TextView textView_tax_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tax_calculate, container, false);
        initView(rootView);
        return rootView;
    }

    /**
     * 控件初始化
     *
     * @param rootView Fragment的父布局
     */
    private void initView(View rootView) {
        editText_tax_before = (EditText) rootView.findViewById(R.id.editText_tax_before);
        editText_social_insurance = (EditText) rootView.findViewById(R.id.editText_social_insurance);
        spinner_tax_threshold = (Spinner) rootView.findViewById(R.id.spinner_tax_threshold);
        rootView.findViewById(R.id.button_calculate_tax).setOnClickListener(this);
        rootView.findViewById(R.id.relativeLayout_tax).setOnClickListener(this);
        textView_tax_result = (TextView) rootView.findViewById(R.id.textView_tax_result);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_calculate_tax:
                String tax_beforeStr = editText_tax_before.getText().toString().trim();
                String social_insuranceStr = editText_social_insurance.getText().toString().trim();
                if (TextUtils.isEmpty(tax_beforeStr) || TextUtils.isEmpty(social_insuranceStr)) {
                    Toast.makeText(getContext(), "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                    break;
                }
                double tax_before = Integer.parseInt(tax_beforeStr);
                double social_insurance = Integer.parseInt(social_insuranceStr);
                double tax_threshold = Integer.parseInt(spinner_tax_threshold.getSelectedItem().toString().trim());
                double tax = (tax_before - social_insurance - tax_threshold) * 0.03;
                if (tax <= 0) {
                    textView_tax_result.setText("您无需缴纳个人所得税");
                } else {
                    textView_tax_result.setText("应缴税款：" + tax + "元\n\n实发工资：" + (tax_before - social_insurance - tax) + "元");
                }
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_result_scale);
                textView_tax_result.startAnimation(animation);
                break;
            default:
                break;
        }
        // 隐藏输入法界面
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
