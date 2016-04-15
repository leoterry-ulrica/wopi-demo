package com.dist.common;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 用于字符串处理的工具类<br>
 * 主要有对字符串去空格，驼峰标识转换，是否以指定字符串开头或含有指定字符，字符串与数组之间的转换的各种方法
 * 
 * @author DongQihong
 * @version 1.0, 2013-8-20
 */
public class StringUtil {
    /**
     * 参数为null或""
     */
    public static final String PARAMETER_IS_NULL_OR_EMPTY_MSG = "参数为null或\"\"";
 /**
 * 字符串索引超出范围
 */
    public static final String STRING_INDEX_OUT_OF_BOUNDS_MSG = "字符串索引超出范围";

    /**
     * 字符串"_"
     */
    public static final String UNDERLINE_STR = "_";
    /**
     * 笑脸分隔符 -_-
     */
    public static final String SMILE_STR = "-_-";
    /**
     * 空字符串 {@code ""}.
     */
    public static final String EMPTY = "";

    /**
     * 判断某字符串是否为空或长度为0。
     * 
     * <pre>
     * 例
     * StringUtils.isEmpty(null) = true
     * StringUtils.isEmpty("") = true
     * StringUtils.isEmpty(" ") = false //注意在 StringUtils 中空格作非空处理 
     * StringUtils.isEmpty("   ") = false
     * </pre>
     * 
     * @param str
     *            需要判断的字符串
     * @return 若是为null或者""则返回true，否则返回false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断传入的字符串 集合是否都为null或者"";若满足则返回true，否则返回false
     * 
     * @param strs
     *            可变长度的字符串
     * @return 若strs都为null或者""则返回true，否则返回false
     */
    public static boolean isNullOrEmptyAnd(String... strs) {
        for (String string : strs) {

            // string不为null或空串
            if (!isNullOrEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断传入的字符串 集合是否有一个为null或者"";若满足则返回true，否则返回false
     * 
     * @param strs
     *            可变长度的字符串
     * @return 若strs有一个为null或者""则返回true，否则返回false
     */
    public static boolean isNullOrEmptyOr(String... strs) {
        for (String string : strs) {

            // string为null或空串
            if (isNullOrEmpty(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某字符串是否为空或长度为0或由空白符(whitespace) 构成。
     * 
     * <pre>
     * 例
     * StringUtil.isBlank(null) = true 
     * StringUtil.isBlank("") = true
     * StringUtil.isBlank(" ") = true
     * StringUtil.isBlank("        ") = true
     * StringUtil.isBlank("\t \n \f \r") = true   //对于制表符、换行符、换页符和回车符
     * </pre>
     * 
     * @param str
     *            要检查的字符串
     * @return boolean 是否为空或长度为0或由空白符构成
     */
    public static boolean isBlank(String str) {
        int strLen;

        // str为null
        if (str == null) {
            return true;
        }
        strLen = str.length();

        // str长度为0
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {

            // 当前字符是非空白字符
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 去除字符串字符串中的空格，包括首尾、中间的空格。
     * <P>
     * 例如：字符串"    a b c   "，使用该方法去除左边空格后结果为"abc"。
     * 
     * @param str
     *            需要去除空格的字符串
     * @return 返回没有空格的字符串
     */
    public static String allTrim(String str) {
        /*
        * <pre> 1. 验证传入的字符串参数是否为null或""（空串）,如果是则返回""（空串） 2.
        * 传入参数不为空，使用String类的replaceAll()方法，将所有的空格替换为空串，即replaceAll(" ","")
        * </pre>
        */

        // 为null或""
        if (isNullOrEmpty(str)) {
            return EMPTY;
        }
        return str.replaceAll(" ", EMPTY);
    }

    /**
     * 去除左边空格。
     * <P>
     * 例如：字符串"    a b c   "，使用该方法去除左边空格后结果为"a b c   "。
     * 
     * @param str
     *            字符串
     * @return 左边无空格的字符串
     */
    public static String leftTrim(String str) {
        /*
        * <pre> 1. 判断字符串是否为null或""，是则返回"" 2.
        * 使用for循环从小到大遍历字符串，使用charAt()方法取每一个字符char 3.
        * 利用Character.isWhitespace(char)是否为空白字符 4. 当第三步返回值为false即不为
        * 空白字符时则截取当前字符以后的所有字符串并停止循环 5. 返回截取后字符串 </pre>
        */
        int star = 0;// 开始截取的位置

        // 为null或""
        if (isNullOrEmpty(str)) {
            return EMPTY;
        }
        char[] strChar = str.toCharArray();
        // 字符为空白
        while (Character.isWhitespace(strChar[star])) {
            star++;
        }
        return str.substring(star);
    }

    /**
     * 去除右边空格。
     * <P>
     * 例如：字符串"    a b c   "，使用该方法去除左边空格后结果为"   a b c"。
     * 
     * @param str
     *            字符串
     * @return 右边无空格的字符串
     */
    public static String rightTrim(String str) {
        /*
        * <pre> 1. 判断字符串是否为null或""，是则返回"" 2. 使用循环从后往前遍历字符串，取得结尾的字符串位置 3.
        * 返回截取后字符串 </pre>
        */
        int endIndex;// 结尾字符位置

        // str为空或""
        if (isNullOrEmpty(str)) {
            return EMPTY;
        }
        endIndex = str.length();
        char[] strChar = str.toCharArray();

        // 为字符空白
        while (Character.isWhitespace(strChar[endIndex - 1])) {
            endIndex--;
        }
        return str.substring(0, endIndex);
    }

    /**
     * 在指定位置插入字符串。 例如：在字符串"abc"的1处插入字符"f"，插入后的结果为"afbc"
     * 
     * <p>
     * 如果下标超出原字符串的长度，则直接跟在原字符串之后
     * </p>
     * 
     * @param index
     *            插入下标
     * @param str
     *            原字符串
     * @param content
     *            带插入的字符串内容
     * @return 插入新内容后的字符串
     */
    public static String insertStr(int index, String str, String content) {
        /*
        * 判断原字符串是否为null或""，若空则抛出IllegalArgumentException异常
        * 得原字符串创建一个StringBuffer对象
        * 判断指定下标的值是否超过原字符串的长度，直接返回跟在原字符后面的字符串
        * 利用StringBuffer对象对下标前的字符串、插入内容、下标后的字符串进行拼接
        * 返回新的字符串<br/>
        */
        StringBuffer sb = new StringBuffer(str);

        // 原字符串是否为空或""串
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException("PARAMETER_IS_NULL_OR_EMPTY_MSG");
        }

        // 带插入的字符串是否为空或""串
        if (isNullOrEmpty(content)) {
            return sb.toString();
        }

        // 下标是否超出范围
        if (index > str.length()) {
            return sb.append(content).toString();
        }

        sb.insert(index, content);
        return sb.toString();
    }

    /**
     * 将此字符序列用其反转形式取代。
     * <p>
     * 例：原字符串str="abcdef"反转以后的结果为"fedcba"。
     * 
     * @param str
     *            字符串
     * @return 倒序排列后的字符串
     */
    public static String reverse(String str) {
        /* <pre>
        * 1.  调用isNullOrEmpty()方法判断原字符串是否为null或""，若空则抛出IllegalArgumentException异常提示，不为空则执行以下步骤
        * 2.  使用StringBuffer的带参的构造器构造一个对象
        * 3.  使用StringBuffer类中的reverse()方法
        * 4.  对StringBuffer对象toString()并返回
        * </pre>
        */

        // 原字符串是否为空或""串
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(PARAMETER_IS_NULL_OR_EMPTY_MSG);
        }
        StringBuffer sb = new StringBuffer(str);
        return sb.reverse().toString();
    }

    /**
     * 把数组转换为字符串列表，默认使用“,”把数组的每个元素隔开。
     * <p>
     * 例如：将数组{"a","b","c","@","D"}转为字符串："a,b,c,@,D"。
     * 
     * @param array
     *            数组
     * @return 用","隔开的字符串列表
     */
    public static String arrayToStr(String[] array) {
        return arrayToStr(array, ",");
    }

    /**
     * 把数组转换为字符串列表，使用自定义分隔符把数组的每个元素隔开。
     * <p>
     * 例如：将数组{"a","b","c","@","D"}按自定义分割符“-”转为字符串："a-b-c-@-D"。
     * 
     * @param array
     *            数组
     * @param splitChar
     *            分隔符
     * @return 用指定分隔符隔开的字符串列表
     */
    public static String arrayToStr(String[] array, String splitChar) {
        StringBuffer sb = new StringBuffer();
        if (null == array) {
            return EMPTY;
        }
        for (String string : array) {
            sb.append(splitChar).append(string);
        }
        return sb.substring(1);
    }

    /**
     * 字符串转换数组。
     * <p>
     * 例：将字符串"abcdef"转为字符数组的结果为{"a","b","c","d","e","f"}。
     * 
     * @param str
     *            字符串
     * @return 字符数组
     */
    public static String[] strToArray(String str) {
        /* <pre>
        * 1.  判断传入的数组是否为null，若为null则直接返回null
        * 2.  使用String的toCharArray()方法将字符串转换为char数组
        * 3.  定义一个String数组，长度与char数组相同
        * 4.  遍历char数组，对每个下标对于的char使用Character.toString(char c)方法转换为String并赋给String数组
        * </pre>
        */
        String[] strArry;

        // str为null
        if (null == str) {
            return null;
        }
        strArry = new String[str.length()];
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            strArry[i] = Character.toString(ch[i]);
        }
        return strArry;
    }

    /**
     * 将字符串中的首字母转为大写。
     * 
     * @param str
     *            必须为字母的字符串
     * @return 首字母为大写的字符串
     */
    public static String firstUppercase(String str) {
        /* <pre>
        * 1.  对传入的字符串进行判断
        * 2.  若为null则返回空串
        * 3.  若字符串第一个字符不为字母则不做任何处理直接返回该字符串
        * 4.  若都不是第2和第3步的情况则执行5、6步
        * 5.  截取第一个字符，并使用String类的toUpperCase()将此字符转换为大写
        * 6.  将原字符串中的首字母替换为大写的字符
        * </pre>
        */
        char[] ch;

        // 判断str是否为null
        if (null == str) {
            return EMPTY;
        }
        ch = str.toCharArray();

        // 判断第一个字符是否为字母
        if (Character.isLetter(ch[0])) {
            ch[0] = Character.toUpperCase(ch[0]);
        }
        return new String(ch);
    }

    /**
     * 将字符串中的首字母转为小写。
     * 
     * @param str
     *            必须为字母的字符串
     * @return 首字母为大写的字符串
     */
    public static String firstLowercase(String str) {
        /* <pre>
        * 1.  对传入的字符串进行判断
        * 2.  若为null则返回空串
        * 3.  若字符串第一个字符不为字母则不做任何处理直接返回该字符串
        * 4.  若都不是第2和第3步的情况则执行5、6步
        * 5.  截取第一个字符，并使用String类的toLowerCase()将此字符转换为小写
        * 6.  将原字符串中的首字母替换为小写的字符
        * </pre>
        */
        char[] ch;

        // 判断str是否为null
        if (null == str) {
            return EMPTY;
        }
        ch = str.toCharArray();

        // 判断第一个字符是否为字母
        if (Character.isLetter(ch[0])) {
            ch[0] = Character.toLowerCase(ch[0]);
        }
        return new String(ch);
    }

    /**
     * 将含有下划线的字符转为小驼峰命名的字符串，如果字符串中不含有下划线将全部转为小写字符，字符串中首字符不能为下划线。
     * 
     * <pre>
     * 将下划线相邻的后一个字母转为写，其余字母都转为小写，然后再拼接。例如：传入test_test或 TEst_Test，返回值都为testTest。
     * </pre>
     * 
     * @param str
     *            需要转换的字符串，传入的字符串必须为英语
     * @return 返回按小驼峰命名的字符串
     */
    public static String toCamelCase(String str) {
        return firstLowercase(toCapitalizeCamelCase(str));
    }

    /**
     * 将按照大驼峰和小驼峰命名的字符串转为含有下划线的字符串，即除首字母大写不转为小写外其余大写字母转为小写且在前面加上下划线。
     * 
     * <pre>
     * 例如：传入TestTest和testTest,返回值都为test_test。
     * </pre>
     * 
     * @param str
     *            传入的字符串必须为英语
     * @return 返回小写的英语单词且每个单词之间都有下划线
     */
    public static String toUnderScoreCase(String str) {
        /* <pre>
        * 1.  判断传入参数是否为空或全为字母，不是则停止操作并抛出IllegalArgumentException异常提示
        * 2.  定义StringBuilder对象stringBuilder重新拼接字符串
        * 3.  循环遍历字符串，利用String类的charAt()方法取char值
        * 4.  将字符串的第一个字符转为小写
        * 5.  判断当前char值的后一个字符是否是大写，若是大写则在该大写字符之前加上下划线，然后将该大写字符转为小写
        * 6.  返回
        * </pre>
        */

        // 原字符串是否为空或""串
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(PARAMETER_IS_NULL_OR_EMPTY_MSG);
        }
        StringBuffer sb = new StringBuffer(str);
        char[] ch = str.toCharArray();
        for (int i = ch.length - 1; i >= 0; i--) {
            if (Character.isUpperCase(ch[i])) {
                sb.setCharAt(i, Character.toLowerCase(ch[i]));
                if (i > 0) {
                    sb.insert(i, UNDERLINE_STR);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将含有下划线的字符串转为按照大驼峰命名的字符串，即将下划线相邻的后一个字母转为大写，首字母转为大写，其余字母都转为小写，然后再拼接；
     * 如果字符串中不喊有下划线，则只有首字符大写，其余全为小写。
     * 
     * <pre>
     * 例如：传入test_test或 TEst_Test，返回值都为TestTest；传入testtest，返回值为Testtest
     * </pre>
     * 
     * @param str
     *            传入的字符串只能包含英语和下划线
     * @return 返回按大驼峰命名的字符串
     */
    public static String toCapitalizeCamelCase(String str) {
        /* <pre>
        * 1.  调用toCamelCase()方法得到小驼峰标识的字符串
        * 2.  再调用toUppercaseFirstChar()方法将第一步得到的字符串的首字母转为大写，得到大驼峰标识的字符串
        * 3.  返回字符串
        * </pre>
        */
        String[] strs;
        StringBuffer sb = new StringBuffer();

        // 原字符串是否为空或""串
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(PARAMETER_IS_NULL_OR_EMPTY_MSG);
        }
        str = str.toLowerCase();
        strs = str.split("_");
        for (String string : strs) {
            sb.append(firstUppercase(string));
        }
        return sb.toString();
    }

    /**
     * utf-8格式解码。
     * @param str 字符串
     * @return 解码后的子ufc
     * @throws UnsupportedEncodingException 转码异常
     */
    public static String utf8Dncode(String str) throws UnsupportedEncodingException {
        if (isBlank(str)) {
            return "";
        }
        return java.net.URLDecoder.decode(str, "UTF-8");
    }
    
    /**
     * urf-8格式转码。
     * @param str 字符串
     * @return 转码后的字符串
     * @throws UnsupportedEncodingException 转码异常
     */
    public static String utf8Encode(String str) throws UnsupportedEncodingException {
        if (isBlank(str)) {
            return "";
        }
        return java.net.URLEncoder.encode(str, "UTF-8");
    }
    /**
     * 获取当前项目的路径
     * @return
     */
    public static String getRootPath() {
        String classPath = StringUtil.class.getClassLoader().getResource("/").getPath();
        String rootPath  = "";
        //windows下
        if("\\".equals(File.separator)){   
         rootPath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
         rootPath = rootPath.replace("/", "\\");
        }
        //linux下
        if("/".equals(File.separator)){   
         rootPath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
         rootPath = rootPath.replace("\\", "/");
        }
        return rootPath;
       }

}
