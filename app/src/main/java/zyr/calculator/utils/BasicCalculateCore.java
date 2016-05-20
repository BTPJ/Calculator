package zyr.calculator.utils;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.util.Log;

import zyr.calculator.ui.fragment.BasicCalculateFragment;

/**
 * 基本计算的核心类
 *
 * @ author LTP
 * @ date  16/5/19
 */
public class BasicCalculateCore {

    /**
     * 控制DEG按键，true为角度，false为弧度
     */
    public static boolean deg_mark = true;

    /**
     * π值：90度
     */
    public double pi = 4 * Math.atan(1);

    /**
     * 保存原来的算式样子，为了输出时好看，因计算时，算式样子被改变
     */
    public static String oldStr;

    /**
     * 变换样子后的式子
     */
    public static String newStr;

    /**
     * 计算表达式 从左向右扫描，数字入number栈，运算符入operator栈 +-基本优先级为1，×÷基本优先级为2， sin cos tan
     * !基本优先级为3，√^基本优先级为4 括号内层运算符比外层同级运算符优先级高4
     * 当前运算符优先级高于栈顶压栈，低于栈顶弹出一个运算符与两个数进行运算 重复直到当前运算符大于栈顶 扫描完后对剩下的运算符与数字依次计算
     *
     * @param str 输入的字符串
     */
    public void process(String str) {
        int weightPlus = 0, topOp = 0, topNum = 0, flag = 1, weightTemp;
        // weightPlus为同一（）下的基本优先级，weightTemp临时记录优先级的变化
        // topOp为weight[]，operator[]的计数器；topNum为number[]的计数器
        // flag为正负数的计数器，1为正数，-1为负数
        int weight[]; // 保存operator栈中运算符的优先级，以topOp计数
        double number[]; // 保存数字，以topNum计数
        char ch, ch_gai, operator[];// operator[]保存运算符，以topOp计数
        String num;// 记录数字，str以+-×÷()sct!√^分段，+-×÷()sct!√^字符之间的字符串即为数字
        weight = new int[500];
        number = new double[500];
        operator = new char[500];
        StringTokenizer expToken = new StringTokenizer(str,
                "+-×÷()sctgI!√^");
        int i = 0;
        while (i < str.length()) {
            ch = str.charAt(i);
            // 判断正负数
            if (i == 0) {
                if (ch == '-')
                    flag = -1;
            } else if (str.charAt(i - 1) == '(' && ch == '-')
                flag = -1;
            // 取得数字，并将正负符号转移给数字
            if (ch <= '9' && ch >= '0' || ch == '.' || ch == 'E') {
                num = expToken.nextToken();
                ch_gai = ch;
                Log.e("guojs", ch + "--->" + i);
                // 取得整个数字
                while (i < str.length()
                        && (ch_gai <= '9' && ch_gai >= '0' || ch_gai == '.' || ch_gai == 'E')) {
                    ch_gai = str.charAt(i++);
                    Log.e("guojs", "i的值为：" + i);
                }
                // 将指针退回之前的位置
                if (i >= str.length())
                    i -= 1;
                else {
                    i -= 2;
                }
                if (num.compareTo(".") == 0)
                    number[topNum++] = 0;
                    // 将正负符号转移给数字
                else {
                    number[topNum++] = Double.parseDouble(num) * flag;
                    flag = 1;
                }
            }
            // 计算运算符的优先级
            if (ch == '(')
                weightPlus += 4;
            if (ch == ')')
                weightPlus -= 4;
            if (ch == '-' && flag == 1 || ch == '+' || ch == '×' || ch == '÷'
                    || ch == 's' || ch == 'c' || ch == 't' || ch == 'g'
                    || ch == 'I' || ch == '!' || ch == '√' || ch == '^') {
                switch (ch) {
                    // +-的优先级最低，为1
                    case '+':
                    case '-':
                        weightTemp = 1 + weightPlus;
                        break;
                    // x÷的优先级稍高，为2
                    case '×':
                    case '÷':
                        weightTemp = 2 + weightPlus;
                        break;
                    // sincos之类优先级为3
                    case 's':
                    case 'c':
                    case 't':
                    case 'g':
                    case 'I':
                    case '!':
                        weightTemp = 3 + weightPlus;
                        break;
                    // 其余优先级为4
                    // case '^':
                    // case '√':
                    default:
                        weightTemp = 4 + weightPlus;
                        break;
                }
                // 如果当前优先级大于堆栈顶部元素，则直接入栈
                if (topOp == 0 || weight[topOp - 1] < weightTemp) {
                    weight[topOp] = weightTemp;
                    operator[topOp] = ch;
                    topOp++;
                    // 否则将堆栈中运算符逐个取出，直到当前堆栈顶部运算符的优先级小于当前运算符
                } else {
                    while (topOp > 0 && weight[topOp - 1] >= weightTemp) {
                        switch (operator[topOp - 1]) {
                            // 取出数字数组的相应元素进行运算
                            case '+':
                                number[topNum - 2] += number[topNum - 1];
                                break;
                            case '-':
                                number[topNum - 2] -= number[topNum - 1];
                                break;
                            case '×':
                                number[topNum - 2] *= number[topNum - 1];
                                break;
                            // 判断除数为0的情况
                            case '÷':
                                if (number[topNum - 1] == 0) {
                                    showError(1, oldStr);
                                    return;
                                }
                                number[topNum - 2] /= number[topNum - 1];
                                break;
                            case '√':
                                if (number[topNum - 1] == 0
                                        || (number[topNum - 2] < 0 && number[topNum - 1] % 2 == 0)) {
                                    showError(2, oldStr);
                                    return;
                                }
                                number[topNum - 2] = Math.pow(number[topNum - 2],
                                        1 / number[topNum - 1]);
                                break;
                            case '^':
                                number[topNum - 2] = Math.pow(number[topNum - 2],
                                        number[topNum - 1]);
                                break;
                            // 计算时进行角度弧度的判断及转换
                            // sin
                            case 's':
                                if (deg_mark) {
                                    number[topNum - 1] = Math
                                            .sin((number[topNum - 1] / 180) * pi);
                                } else {
                                    number[topNum - 1] = Math
                                            .sin(number[topNum - 1]);
                                }
                                topNum++;
                                break;
                            // cos
                            case 'c':
                                if (deg_mark) {
                                    number[topNum - 1] = Math
                                            .cos((number[topNum - 1] / 180) * pi);
                                } else {
                                    number[topNum - 1] = Math
                                            .cos(number[topNum - 1]);
                                }
                                topNum++;
                                break;
                            // tan
                            case 't':
                                if (deg_mark) {
                                    if ((Math.abs(number[topNum - 1]) / 90) % 2 == 1) {
                                        showError(2, oldStr);
                                        return;
                                    }
                                    number[topNum - 1] = Math
                                            .tan((number[topNum - 1] / 180) * pi);
                                } else {
                                    if ((Math.abs(number[topNum - 1]) / (pi / 2)) % 2 == 1) {
                                        showError(2, oldStr);
                                        return;
                                    }
                                    number[topNum - 1] = Math
                                            .tan(number[topNum - 1]);
                                }
                                topNum++;
                                break;
                            // log
                            case 'g':
                                if (number[topNum - 1] <= 0) {
                                    showError(2, oldStr);
                                    return;
                                }
                                number[topNum - 1] = Math.log10(number[topNum - 1]);
                                topNum++;
                                break;
                            // In
                            case 'I':
                                if (number[topNum - 1] <= 0) {
                                    showError(2, oldStr);
                                    return;
                                }
                                number[topNum - 1] = Math.log(number[topNum - 1]);
                                topNum++;
                                break;
                            // 阶乘
                            case '!':
                                if (number[topNum - 1] > 170) {
                                    showError(3, oldStr);
                                    return;
                                } else if (number[topNum - 1] < 0) {
                                    showError(2, oldStr);
                                    return;
                                }
                                number[topNum - 1] = N(number[topNum - 1]);
                                topNum++;
                                break;
                        }
                        // 继续取堆栈的下一个元素进行判断
                        topNum--;
                        topOp--;
                    }
                    // 将运算符如堆栈
                    weight[topOp] = weightTemp;
                    operator[topOp] = ch;
                    topOp++;
                }
            }
            i++;
        }
        // 依次取出堆栈的运算符进行运算
        while (topOp > 0) {
            // +-x直接将数组的后两位数取出运算
            switch (operator[topOp - 1]) {
                case '+':
                    number[topNum - 2] += number[topNum - 1];
                    break;
                case '-':
                    number[topNum - 2] -= number[topNum - 1];
                    break;
                case '×':
                    number[topNum - 2] *= number[topNum - 1];
                    break;
                // 涉及到除法时要考虑除数不能为零的情况
                case '÷':
                    if (number[topNum - 1] == 0) {
                        showError(1, oldStr);
                        return;
                    }
                    number[topNum - 2] /= number[topNum - 1];
                    break;
                case '√':
                    if (number[topNum - 1] == 0
                            || (number[topNum - 2] < 0 && number[topNum - 1] % 2 == 0)) {
                        showError(2, oldStr);
                        return;
                    }
                    number[topNum - 2] = Math.pow(number[topNum - 2],
                            1 / number[topNum - 1]);
                    break;
                case '^':
                    number[topNum - 2] = Math.pow(number[topNum - 2],
                            number[topNum - 1]);
                    break;

                case 's':
                    // sin
                    if (deg_mark) {
                        number[topNum - 1] = Math.sin((number[topNum - 1] / 180)
                                * pi);
                    } else {
                        number[topNum - 1] = Math.sin(number[topNum - 1]);
                    }
                    topNum++;
                    break;

                case 'c':
                    // cos
                    if (deg_mark) {
                        number[topNum - 1] = Math.cos((number[topNum - 1] / 180)
                                * pi);
                    } else {
                        number[topNum - 1] = Math.cos(number[topNum - 1]);
                    }
                    topNum++;
                    break;

                case 't':
                    // tan
                    if (deg_mark) {
                        if ((Math.abs(number[topNum - 1]) / 90) % 2 == 1) {
                            showError(2, oldStr);
                            return;
                        }
                        number[topNum - 1] = Math.tan((number[topNum - 1] / 180)
                                * pi);
                    } else {
                        if ((Math.abs(number[topNum - 1]) / (pi / 2)) % 2 == 1) {
                            showError(2, oldStr);
                            return;
                        }
                        number[topNum - 1] = Math.tan(number[topNum - 1]);
                    }
                    topNum++;
                    break;

                case 'g':
                    // 对数log
                    if (number[topNum - 1] <= 0) {
                        showError(2, oldStr);
                        return;
                    }
                    number[topNum - 1] = Math.log10(number[topNum - 1]);
                    topNum++;
                    break;

                case 'I':
                    // 自然对数In
                    if (number[topNum - 1] <= 0) {
                        showError(2, oldStr);
                        return;
                    }
                    number[topNum - 1] = Math.log(number[topNum - 1]);
                    topNum++;
                    break;

                case '!':
                    // 阶乘
                    if (number[topNum - 1] > 170) {
                        showError(3, oldStr);
                        return;
                    } else if (number[topNum - 1] < 0) {
                        showError(2, oldStr);
                        return;
                    }
                    number[topNum - 1] = N(number[topNum - 1]);
                    topNum++;
                    break;
            }
            // 取堆栈下一个元素计算
            topNum--;
            topOp--;
        }
        // 如果是数字太大，提示错误信息
        if (number[0] > 7.3E306) {
            showError(3, oldStr);
            return;
        }
        // 输出最终结果
        BasicCalculateFragment.input.setText(String.valueOf(FP(number[0])));
    }

    /**
     * FP = floating point 控制小数位数，达到精度 否则会出现0.6-0.2=0.39999999999999997的情况，用FP即可解决，使得数为0.4 本格式精度为15位
     *
     * @param n 输入的数字
     * @return 格式化后的数字
     */
    public double FP(double n) {
        // NumberFormat format=NumberFormat.getInstance(); //创建一个格式化类f
        // format.setMaximumFractionDigits(18); //设置小数位的格式
        DecimalFormat format = new DecimalFormat("0.#############");
        return Double.parseDouble(format.format(n));
    }

    /**
     * 阶乘
     *
     * @param n 要阶乘的数字
     * @return 阶乘后的结果
     */
    public double N(double n) {
        double sum = 1;
        // 依次将小于等于n的值相乘
        for (int i = 1; i <= n; i++) {
            sum = sum * i;
        }
        return sum;
    }

    /**
     * 错误提示，按了"="之后，若计算式在process()过程中，出现错误，则进行提示
     *
     * @param code 错误代号
     * @param str  产生错误的字符串
     */
    @SuppressLint("SetTextI18n")
    public void showError(int code, String str) {
        String message = "";
        switch (code) {
            case 1:
                message = "零不能作除数";
                break;
            case 2:
                message = "函数格式错误";
                break;
            case 3:
                message = "值太大了，超出范围";
        }
        BasicCalculateFragment.input.setText("\"" + str + "\"" + ": " + message);
    }
}