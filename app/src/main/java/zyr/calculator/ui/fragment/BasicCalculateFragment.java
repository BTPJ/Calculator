package zyr.calculator.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import zyr.calculator.R;
import zyr.calculator.utils.BasicCalculateCore;

/**
 * 基本计算界面的Fragment隶属于MainActivity
 *
 * @ author LTP
 * @ time 16/5/18
 */
public class BasicCalculateFragment extends Fragment implements View.OnClickListener {

    /**
     * 显示器,用于显示输出结果
     */
    public static EditText input;
    private Button degButton;

    /**
     * 输入控制，true为重新输入，false为接着输入
     */
    public boolean agin = true;

    /**
     * true表示正确，可以继续输入；false表示有误，输入被锁定
     */
    public boolean tip_lock = true;

    /**
     * 判断是否是按=之后的输入，true表示输入在=之前，false反之
     */
    public boolean equals_flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic_calculate, container, false);
        initView(rootView);
        return rootView;
    }

    /**
     * 控件初始化
     *
     * @param rootView Fragment的父布局
     */
    private void initView(View rootView) {
        input = (EditText) rootView.findViewById(R.id.et_input);
        // 设置不可编辑
        input.setKeyListener(null);
        rootView.findViewById(R.id.btn0).setOnClickListener(this);
        rootView.findViewById(R.id.btn1).setOnClickListener(this);
        rootView.findViewById(R.id.btn2).setOnClickListener(this);
        rootView.findViewById(R.id.btn3).setOnClickListener(this);
        rootView.findViewById(R.id.btn4).setOnClickListener(this);
        rootView.findViewById(R.id.btn5).setOnClickListener(this);
        rootView.findViewById(R.id.btn6).setOnClickListener(this);
        rootView.findViewById(R.id.btn7).setOnClickListener(this);
        rootView.findViewById(R.id.btn8).setOnClickListener(this);
        rootView.findViewById(R.id.btn9).setOnClickListener(this);
        rootView.findViewById(R.id.btn_jia).setOnClickListener(this);
        rootView.findViewById(R.id.btn_jian).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cheng).setOnClickListener(this);
        rootView.findViewById(R.id.btn_chu).setOnClickListener(this);
        rootView.findViewById(R.id.btn_equal).setOnClickListener(this);
        rootView.findViewById(R.id.btn_sin).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cos).setOnClickListener(this);
        rootView.findViewById(R.id.btn_tan).setOnClickListener(this);
        rootView.findViewById(R.id.btn_log).setOnClickListener(this);
        rootView.findViewById(R.id.btn_In).setOnClickListener(this);
        rootView.findViewById(R.id.btn_zhishu).setOnClickListener(this);
        rootView.findViewById(R.id.btn_kaifang).setOnClickListener(this);
        rootView.findViewById(R.id.btn_jiecheng).setOnClickListener(this);
        rootView.findViewById(R.id.btn_backSpace).setOnClickListener(this);
        rootView.findViewById(R.id.btn_kuohaoLeft).setOnClickListener(this);
        rootView.findViewById(R.id.btn_kuohaoRight).setOnClickListener(this);
        rootView.findViewById(R.id.btn_point).setOnClickListener(this);
        rootView.findViewById(R.id.btn_clear).setOnClickListener(this);
        degButton = (Button) rootView.findViewById(R.id.btn_deg);
        degButton.setOnClickListener(this);
    }

    /**
     * 命令缓存，用于检测输入合法性
     */
    String[] TipCommand = new String[500];

    /**
     * TipCommand的指针
     */
    int tip_i = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        String command = ((Button) v).getText().toString();
        String str = input.getText().toString();

        if (!equals_flag
                && "0123456789.()sincostanlogIn!+-×÷√^".contains(command)) {
            // 检测显示器上的字符串是否合法
            if (right(str)) {
                if ("+-×÷√^)".contains(command)) {
                    for (int i = 0; i < str.length(); i++) {
                        TipCommand[tip_i] = String.valueOf(str.charAt(i));
                        tip_i++;
                    }
                    agin = false;
                }
            } else {
                input.setText("0");
                agin = true;
                tip_i = 0;
                tip_lock = true;
            }
            equals_flag = true;
        }
        if (tip_i > 0)
            TipChecker(TipCommand[tip_i - 1], command);
        else if (tip_i == 0) {
            TipChecker("#", command);
        }
        if ("0123456789.()sincostanlogIn!+-×÷√^".contains(command)
                && tip_lock) {
            TipCommand[tip_i] = command;
            tip_i++;
        }

        // 若输入正确，则将输入信息显示到显示器上
        if ("0123456789.()sincostanlogIn!+-×÷√^".contains(command)
                && tip_lock) { // 共25个按键
            print(command);
            // 若果点击了“DEG”，则切换当前弧度角度制，并将切换后的结果显示到按键上。
        } else if (command.compareTo("DEG") == 0
                || command.compareTo("RAD") == 0 && tip_lock) {
            if (BasicCalculateCore.deg_mark) {
                BasicCalculateCore.deg_mark = false;
                degButton.setText("RAD");
            } else {
                BasicCalculateCore.deg_mark = true;
                degButton.setText("DEG");
            }
            // 如果输入时退格键，并且是在按=之前
        } else if (command.compareTo("←") == 0 && equals_flag) {
            // 一次删除3个字符
            if (TTO(str) == 3) {
                if (str.length() > 3)
                    input.setText(str.substring(0, str.length() - 3));
                else if (str.length() == 3) {
                    input.setText("0");
                    agin = true;
                    tip_i = 0;
                }
                // 一次删除2个字符
            } else if (TTO(str) == 2) {
                if (str.length() > 2)
                    input.setText(str.substring(0, str.length() - 2));
                else if (str.length() == 2) {
                    input.setText("0");
                    agin = true;
                    tip_i = 0;
                }
                // 一次删除一个字符
            } else if (TTO(str) == 1) {
                // 若之前输入的字符串合法则删除一个字符
                if (right(str)) {
                    if (str.length() > 1)
                        input.setText(str.substring(0, str.length() - 1));
                    else if (str.length() == 1) {
                        input.setText("0");
                        agin = true;
                        tip_i = 0;
                    }
                    // 若之前输入的字符串不合法则删除全部字符
                } else {
                    input.setText("0");
                    agin = true;
                    tip_i = 0;
                }
            }
            if (input.getText().toString().compareTo("-") == 0
                    || !equals_flag) {
                input.setText("0");
                agin = true;
                tip_i = 0;
            }
            tip_lock = true;
            if (tip_i > 0)
                tip_i--;
            // 如果是在按=之后输入退格键
        } else if (command.compareTo("←") == 0 && !equals_flag) {
            // 将显示器内容设置为0
            input.setText("0");
            agin = true;
            tip_i = 0;
            tip_lock = true;
            // 如果输入的是清除键
        } else if (command.compareTo("C") == 0) {
            input.setText("0"); // 将显示器内容设置为0
            agin = true; // 重新输入标志置为true
            tip_i = 0; // 缓存命令位数清0
            tip_lock = true; // 表明可以继续输入
            equals_flag = true;// 表明输入=之前
            // 如果输入的是=号，并且输入合法
        } else if (command.compareTo("=") == 0 && tip_lock && right(str)
                && equals_flag) {
            tip_i = 0;
            tip_lock = false;// 表明不可以继续输入
            equals_flag = false;// 表明输入=之后
            BasicCalculateCore.oldStr = str;// 保存原来算式样子
            // 替换算式中的运算符，便于计算
            str = str.replaceAll("sin", "s");
            str = str.replaceAll("cos", "c");
            str = str.replaceAll("tan", "t");
            str = str.replaceAll("log", "g");
            str = str.replaceAll("In", "I");
            str = str.replaceAll("!", "!");
            agin = true;// 重新输入标志设置true
            BasicCalculateCore.newStr = str.replaceAll("-", "-1×");// 将-1x转换成-
            new BasicCalculateCore().process(BasicCalculateCore.newStr);// 计算算式结果
        }
        tip_lock = true;// 表明可以继续输入
    }

    /**
     * 判断一个str是否是合法的（只包含0123456789.()sincostanlogIn!+-×÷√^的是合法的str，返回true，包含了除0123456789.()sincostanlogIn!+-×÷√^以外的字符的str为非法的，返回false）
     *
     * @param str 输入的字符串
     * @return true、false
     */
    private boolean right(String str) {
        int i;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '0' && str.charAt(i) != '1'
                    && str.charAt(i) != '2' && str.charAt(i) != '3'
                    && str.charAt(i) != '4' && str.charAt(i) != '5'
                    && str.charAt(i) != '6' && str.charAt(i) != '7'
                    && str.charAt(i) != '8' && str.charAt(i) != '9'
                    && str.charAt(i) != '.' && str.charAt(i) != '-'
                    && str.charAt(i) != '+' && str.charAt(i) != '×'
                    && str.charAt(i) != '÷' && str.charAt(i) != '√'
                    && str.charAt(i) != '^' && str.charAt(i) != 's'
                    && str.charAt(i) != 'i' && str.charAt(i) != 'n'
                    && str.charAt(i) != 'c' && str.charAt(i) != 'o'
                    && str.charAt(i) != 't' && str.charAt(i) != 'a'
                    && str.charAt(i) != 'l' && str.charAt(i) != 'g'
                    && str.charAt(i) != 'I' && str.charAt(i) != '('
                    && str.charAt(i) != ')' && str.charAt(i) != '!')
                break;
        }
        return i == str.length();
    }

    /**
     * 向input输出字符
     *
     * @param str 输出的字符
     */
    private void print(String str) {
        // 清屏后输出
        if (agin)
            input.setText(str);
            // 在屏幕原str后增添字符
        else
            input.append(str);
        agin = false;
    }

    /**
     * 检测函数，返回值为3、2、1 表示应当一次删除几个
     *
     * @param str 要删除的字符串
     * @return 返回3，表示str尾部为sin、cos、tan、中的一个，应当一次删除3个
     * 返回2，表示str尾部为In，应当一次删除2个
     * 返回1，表示为除返回3、2外的所有情况，只需删除一个（包含非法字符时要另外考虑：应清屏）
     */
    private int TTO(String str) {
        if ((str.charAt(str.length() - 1) == 'n'
                && str.charAt(str.length() - 2) == 'i' && str.charAt(str
                .length() - 3) == 's')
                || (str.charAt(str.length() - 1) == 's'
                && str.charAt(str.length() - 2) == 'o' && str
                .charAt(str.length() - 3) == 'c')
                || (str.charAt(str.length() - 1) == 'n'
                && str.charAt(str.length() - 2) == 'a' && str
                .charAt(str.length() - 3) == 't')
                || (str.charAt(str.length() - 1) == 'g'
                && str.charAt(str.length() - 2) == 'o' && str
                .charAt(str.length() - 3) == 'l')) {
            return 3;
        } else if ((str.charAt(str.length() - 1) == 'n' && str.charAt(str
                .length() - 2) == 'I')) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * 检测函数，对str进行前后语法检测 为Tip的提示方式提供依据
     * 编号               字符              其后可以跟随的合法字符
     * 1                  （                数字|（|-|.|函数
     * 2                   ）               算符|）|√ ^
     * 3                   .               数字|算符|）|√ ^
     * 4                  数字               .|数字|算符|）|√ ^
     * 5                  算符               数字|（|.|函数
     * 6                   √                  ^ （ |. |数字
     * 7                   函数               数字|（|. 小数点前后均可省略，表示0 数字第一位可以为0
     *
     * @param tipcommand1 先输入的字符
     * @param tipcommand2 后输入的字符
     */
    private void TipChecker(String tipcommand1, String tipcommand2) {
        int tiptype1 = 0, tiptype2 = 0;// 表示命令类型
        // “+-x÷√^”不能作为第一位
        if (tipcommand1.compareTo("#") == 0
                && (tipcommand2.compareTo("÷") == 0
                || tipcommand2.compareTo("×") == 0
                || tipcommand2.compareTo("+") == 0
                || tipcommand2.compareTo(")") == 0
                || tipcommand2.compareTo("√") == 0 || tipcommand2
                .compareTo("^") == 0)) {
            tip_lock = false;// 表明输入有误
        }
        // 定义存储字符串中最后一位的类型
        else if (tipcommand1.compareTo("#") != 0) {
            if (tipcommand1.compareTo("(") == 0) {
                tiptype1 = 1;
            } else if (tipcommand1.compareTo(")") == 0) {
                tiptype1 = 2;
            } else if (tipcommand1.compareTo(".") == 0) {
                tiptype1 = 3;
            } else if ("0123456789".contains(tipcommand1)) {
                tiptype1 = 4;
            } else if ("+-×÷".contains(tipcommand1)) {
                tiptype1 = 5;
            } else if ("√^".contains(tipcommand1)) {
                tiptype1 = 6;
            } else if ("sincostanlogIn!".contains(tipcommand1)) {
                tiptype1 = 7;
            }
            // 定义欲输入的按键类型
            if (tipcommand2.compareTo("(") == 0) {
                tiptype2 = 1;
            } else if (tipcommand2.compareTo(")") == 0) {
                tiptype2 = 2;
            } else if (tipcommand2.compareTo(".") == 0) {
                tiptype2 = 3;
            } else if ("0123456789".contains(tipcommand2)) {
                tiptype2 = 4;
            } else if ("+-×÷".contains(tipcommand2)) {
                tiptype2 = 5;
            } else if ("√^".contains(tipcommand2)) {
                tiptype2 = 6;
            } else if ("sincostanlogIn!".contains(tipcommand2)) {
                tiptype2 = 7;
            }

            switch (tiptype1) {
                case 1:
                    // 左括号后面直接接右括号,“+x÷”（负号“-”不算）,或者"√^"
                    if (tiptype2 == 2
                            || (tiptype2 == 5 && tipcommand2.compareTo("-") != 0)
                            || tiptype2 == 6)
                        tip_lock = false;// 表明输入有误
                    break;
                case 2:
                    // 右括号后面接左括号，数字，“+-x÷sin^...”
                    if (tiptype2 == 1 || tiptype2 == 3 || tiptype2 == 4
                            || tiptype2 == 7)
                        tip_lock = false;// 表明输入有误
                    break;
                case 3:
                    // “.”后面接左括号或者“sincos...”
                    if (tiptype2 == 1 || tiptype2 == 7)
                        tip_lock = false;// 表明输入有误
                    // 连续输入两个“.”
                    if (tiptype2 == 3)
                        tip_lock = false;// 表明输入有误
                    break;
                case 4:
                    // 数字后面直接接左括号或者“sincos...”
                    if (tiptype2 == 1 || tiptype2 == 7)
                        tip_lock = false;// 表明输入有误
                    break;
                case 5:
                    // “+-x÷”后面直接接右括号，“+-x÷√^”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6)
                        tip_lock = false;// 表明输入有误
                    break;
                case 6:
                    // “√^”后面直接接右括号，“+-x÷√^”以及“sincos...”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6
                            || tiptype2 == 7)
                        tip_lock = false;// 表明输入有误
                    break;
                case 7:
                    // “sincos...”后面直接接右括号“+-x÷√^”以及“sincos...”
                    if (tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6
                            || tiptype2 == 7)
                        tip_lock = false;// 表明输入有误
                    break;
            }
        }
        // 检测小数点的重复性，Tipconde1=0,表明满足前面的规则
        if (tip_lock && tipcommand2.compareTo(".") == 0) {
            int tip_point = 0;
            for (int i = 0; i < tip_i; i++) {
                // 若之前出现一个小数点点，则小数点计数加1
                if (TipCommand[i].compareTo(".") == 0) {
                    tip_point++;
                }
                // 若出现以下几个运算符之一，小数点计数清零
                if (TipCommand[i].compareTo("sin") == 0
                        || TipCommand[i].compareTo("cos") == 0
                        || TipCommand[i].compareTo("tan") == 0
                        || TipCommand[i].compareTo("log") == 0
                        || TipCommand[i].compareTo("In") == 0
                        || TipCommand[i].compareTo("!") == 0
                        || TipCommand[i].compareTo("√") == 0
                        || TipCommand[i].compareTo("^") == 0
                        || TipCommand[i].compareTo("÷") == 0
                        || TipCommand[i].compareTo("×") == 0
                        || TipCommand[i].compareTo("-") == 0
                        || TipCommand[i].compareTo("+") == 0
                        || TipCommand[i].compareTo("(") == 0
                        || TipCommand[i].compareTo(")") == 0) {
                    tip_point = 0;
                }
            }
            tip_point++;
            // 若小数点计数大于1，表明小数点重复了
            if (tip_point > 1) {
                tip_lock = false;// 表明输入有误
            }
        }
        // 检测右括号是否匹配
        if (tip_lock && tipcommand2.compareTo(")") == 0) {
            int tip_right_bracket = 0;
            for (int i = 0; i < tip_i; i++) {
                // 如果出现一个左括号，则计数加1
                if (TipCommand[i].compareTo("(") == 0) {
                    tip_right_bracket++;
                }
                // 如果出现一个右括号，则计数减1
                if (TipCommand[i].compareTo(")") == 0) {
                    tip_right_bracket--;
                }
            }
            // 如果右括号计数=0,表明没有响应的左括号与当前右括号匹配
            if (tip_right_bracket == 0) {
                tip_lock = false;// 表明输入有误
            }
        }
        // 检查输入=的合法性
        if (tip_lock && tipcommand2.compareTo("=") == 0) {
            // 括号匹配数
            int tip_bracket = 0;
            for (int i = 0; i < tip_i; i++) {
                if (TipCommand[i].compareTo("(") == 0) {
                    tip_bracket++;
                }
                if (TipCommand[i].compareTo(")") == 0) {
                    tip_bracket--;
                }
            }
            // 若大于0，表明左括号还有未匹配的
            if (tip_bracket > 0) {
                tip_lock = false;// 表明输入有误
            } else if (tip_bracket == 0) {
                // 若前一个字符是以下之一，表明=号不合法
                if ("+-×÷√^sincostan!".contains(tipcommand1)) {
                    tip_lock = false;// 表明输入有误
                }
            }
        }
    }
}
