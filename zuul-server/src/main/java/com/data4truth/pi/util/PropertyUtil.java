package com.data4truth.pi.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lindj
 * @date: 2018/4/12 12:41
 * @description: 读取业务配置资源，例如异常提示等
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }

    /**
     * 加载资源
     */
    private static synchronized void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getResourceAsStream("/biz/biz.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 获取属性值
     *
     * @param key String
     * @return String
     */
    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    /**
     * 获取值
     *
     * @param key    key
     * @param values String... 替换占位符
     * @return String
     */
    public static String getProperty(String key, String... values) {
        //对应占位符参数值
        String[] vs = values;
        //属性文件中的值
        String v = getProperty(key);
        //如果没有参数
        if (vs == null || vs.length == 0) {
            return getProperty(key);
        }
        //如果属性文件中没有值,则返回空字符串
        if (v == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        //遍历参数数组
        for (int i = 0; i < vs.length; i++) {
            //替换前清空原有替换值
            buffer.delete(0, buffer.length());
            Pattern pattern = Pattern.compile("\\{" + i + "\\}");
            Matcher matcher = pattern.matcher(v);
            while (matcher.find()) {
                matcher.appendReplacement(buffer, vs[i]);
            }
            matcher.appendTail(buffer);
            //进行下一次替换
            v = buffer.toString();
        }
        //返回后替换的字符串
        return buffer.toString();
    }

}
