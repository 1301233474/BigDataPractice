package com.common.util;

import java.util.regex.Pattern;

public class StringValidation {

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[0-9])|(18[0-9])|(19[0-9])|(17[0-9])|(17[6-8]))\\d{8}$";
    /**
     * 正则表达式：验证座机号码
     */
    public static final String REGEX_NUM = "^((d{3,4})|d{3,4}-)?d{7,8}$";
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 正则表达式：邮编
     */
    public static final String REGEX_CODE = "[1-9]\\d{5}(?!\\d)";

    /**
     *
     */
    public static final String REGEX_BIRTHDAY = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) ";
    /**
     * 只能输入数字：
     */
    public static final String NUMBER = "^[0-9]*$";
    /**
     * 只能输入n位的数字：
     */
    public static final String NUMBER_N = "d{n}$";

    /**
     *
     * 只能输入至少n位的数字
     */
    public static final String NUMBER_N_ = "d{n,}$";

    /**
     * 只能输入m~n位的数字
     */
    public static final String NUMBER_M_N = "d{m,n}$";

    /**
     * 只能输入零和非零开头的数字：
     */
    public static final String HEAD_NUMBER = "^(0|[1-9][0-9]*)$";

    // 只能输入有两位小数的正实数："^[0-9]+(.[0-9]{2})?$"。
    // 只能输入有1~3位小数的正实数："^[0-9]+(.[0-9]{1,3})?$"。
    // 只能输入非零的正整数："^\+?[1-9][0-9]*$"。
    // 只能输入非零的负整数："^\-[1-9][]0-9"*$。
    // 只能输入长度为3的字符："^.{3}$"。
    // 只能输入由26个英文字母组成的字符串："^[A-Za-z]+$"。
    // 只能输入由26个大写英文字母组成的字符串："^[A-Z]+$"。
    // 只能输入由26个小写英文字母组成的字符串："^[a-z]+$"。
    /**
     * 只能输入由数字和26个英文字母组成的字符串：
     */
    public static final String NAME = "[A-Za-z0-9]{6,15}";

    // 只能输入由数字、26个英文字母或者下划线组成的字符串："^\w+$"。
    // 验证用户密码："^[a-zA-Z]\w{5,17}$"正确格式为：以字母开头，长度在6~18之间，只能包含字符、数字和下划线。
    // 验证是否含有^%&',;=?$\"等字符："[^%&',;=?$\x22]+"。
    // 只能输入汉字："^[\u4e00-\u9fa5]{0,}$"
    // 验证Email地址："^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$"。
    // 验证InternetURL："^http://%28[/\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$"。
    // 验证电话号码："^(\(\d{3,4}-)|\d{3.4}-)?\d{7,8}$"正确格式为："XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX"。
    // 验证身份证号（15位或18位数字）："^\d{15}|\d{18}$"。
    // 验证一年的12个月："^(0?[1-9]|1[0-2])$"正确格式为："01"～"09"和"1"～"12"。
    // 验证一个月的31天："^((0?[1-9])|((1|2)[0-9])|30|31)$"正确格式为；"01"～"09"和"1"～"31"。
    // 利用正则表达式限制网页表单里的文本框输入内容：
    //
    // 用正则表达式限制只能输入中文：οnkeyup="value=value.replace(/[^\u4E00-\u9FA5]/g,'')"
    // onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\u4E00-\u9FA5]/g,''))"
    //
    // 用正则表达式限制只能输入全角字符： οnkeyup="value=value.replace(/[^\uFF00-\uFFFF]/g,'')"
    // onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\uFF00-\uFFFF]/g,''))"
    //
    // 用正则表达式限制只能输入数字：οnkeyup="value=value.replace(/[^\d]/g,'')
    // "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
    //
    // 用正则表达式限制只能输入数字和英文：οnkeyup="value=value.replace(/[\W]/g,'')
    // "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
    //
    // 得用正则表达式从URL地址中提取文件名的javascript程序，如下结果为page1
    //
    // s="http://www.jb51.net/about.htm"
    // s=s.replace(/(.*\/){0,}([^\.]+).*/ig,"$2")
    // alert(s)
    //
    // 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
    //
    // 应用：计算字符串的长度（一个双字节字符长度计2，ASCII字符计1）
    //
    // String.prototype.len=function(){return
    // this.replace([^\x00-\xff]/g,"aa").length;}
    //
    // 匹配空行的正则表达式：\n[\s| ]*\r
    //
    // 匹配HTML标记的正则表达式：/<(.*)>.*<\/\1>|<(.*) \/>/
    //
    // 匹配首尾空格的正则表达式：(^\s*)|(\s*$)

    /**
     * 整形数字格式化
     *
     * @param format
     * @param covers
     *            位数
     * @return
     */
    public static String zeroCover(int format, int covers) {
        return String.format("%0" + covers + "d", format);
    }

    /**
     * 整形数字格式化
     *
     * @param format
     * @param covers
     *            位数
     * @return
     */
    public static String blankCover(int format, int covers) {
        return String.format("% " + covers + "d", format);
    }

    /**
     * 数字分组
     *
     * @param format
     *            位数
     * @return
     */
    public static String group(int format) {
        return String.format("%,d", format);
    }

    /**
     * 显示正负号
     *
     * @param format
     * @param covers
     *            位数
     * @return
     */
    public static String plusOrMinus(int format) {
        return String.format("%+d", format);
    }

    /**
     * 保留几位小数
     *
     * @param d
     * @param covers
     * @return
     */
    public static String decimal(double d, int covers) {
        return String.format("%." + covers + "f", d);
    }

    /**
     * 分组
     *
     * @param d
     * @param covers
     * @return
     */
    public static String group(double d, int covers) {
        return String.format("%,f", d);
    }

    /**
     * 显示正负号
     *
     * @param d
     * @param covers
     * @return
     */
    public static String plusOrMinus(double d, int covers) {
        return String.format("%+f", d);
    }

    /**
     * 10进制转化为16进制
     *
     * @param format
     * @return
     */
    public static String decimal2hexadecimal(int format) {
        return String.format("%x", format);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验座机
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isNum(String mobile) {
        return Pattern.matches(REGEX_NUM, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 验证生日
     * @param birthday
     * @return
     */
    public static boolean isBirthDay(String birthday) {
        return Pattern.matches(REGEX_BIRTHDAY, birthday);
    }

    /**
     * 校验邮编
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isCode(String email) {
        System.out.println(email + email.length());
        return Pattern.matches(REGEX_CODE, email);
    }
}
