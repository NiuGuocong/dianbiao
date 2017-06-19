package util.common;

import java.text.SimpleDateFormat;

/**
 *
 * 字符串工具类
 * Created by hfl on 2017-4-18.
 */
public class StringUtil {

    //判断为空
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("")
                || str.matches("\\s*");
    }

    //判断不为空
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }



    //根据数据类型处理数据转成字符串
    public static String initValue(Object value){
        String valueStr = "";

        String classType = value.getClass().getName();
        if("java.sql.Timestamp".equals(classType)){
            valueStr =  value!=null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) : "";
        }else{
            valueStr = value + "";
        }
        return valueStr;
    }


}
