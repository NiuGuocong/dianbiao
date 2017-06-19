package util.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import play.Logger;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liming on 16/6/16.
 */
public class ConvertUtil {
    //类型转换
    public static String toString(Object value) {
        return value != null ? value.toString() : "";
    }

    public static Double toDouble(Object value) {
        return value == null ? 0 : Double.valueOf(value.toString());
    }

    public static Long toLong(Object value) {
        return value == null ? 0 : Math.round(Double.valueOf(value.toString()));
    }

    public static Integer toInteger(Object value) {
        return value == null ? 0 : Math.round(Float.valueOf(value.toString()));
    }

    public static Boolean toBoolean(Boolean value) {
        return value == null ? false : value;
    }

    //转换成Json类型并且将日期设置为yyyy-MM-dd HH:ss的格式
    public static String toJson(Object value) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(value);
    }

    //转换为MD5
    public static String toMD5(String pwd) {
        String pwdMD5 = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            pwdMD5 = base64en.encode(md5.digest(pwd.getBytes("utf-8")));
        } catch (Exception ex) {
            Logger.error("加密异常");
        }
        return pwdMD5;
    }

    //截取URL
    public static String spliteUrlCtrl(String str) {
        String resultStr = "";
        String[] splitStr = str.split("/");
        if (splitStr != null && splitStr.length > 0)
            for (int i = 1; i < splitStr.length - 1; i++)
                resultStr += "/" + splitStr[i];
        return resultStr;
    }

    /**
     * 防止sql注入
     *
     * @param value
     * @return
     */
    public static String escapeSql(String value) {
        return StringEscapeUtils.escapeSql(value);
    }

    /**
     * 计算合计行
     *
     * @return
     */
    public static Map<String, Object> caculateSummary(List<Map<String, Object>> dataList, String... keys) {
        Map<String, Object> resultMap = new HashMap<>();
        for (String key : keys)
            resultMap.put(key, 0D);
        for (Map<String, Object> data : dataList)
            for (String key : keys)
                resultMap.put(key, toLong(resultMap.get(key)) + toLong(data.get(key)));
        return resultMap;
    }
}
