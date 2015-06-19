package com.hewei.spider.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/19  15:45
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final SerializeConfig browserJsonConfig = new SerializeConfig();
    private static final SerializerFeature[] browserJsonFeature;
    private static SerializerFeature[] jsonFeatures;

    //    private static final AfterFilter afterFilter = new AttrAfterFilter();
    //    private static final PropertyPreFilter propertyPreFilter = new AttrPropertyPreFilter();

    static {
        List<SerializerFeature> serializerFeatureList = Lists.newArrayList();
        serializerFeatureList.add(SerializerFeature.SkipTransientField);
        serializerFeatureList.add(SerializerFeature.WriteDateUseDateFormat);
        serializerFeatureList.add(SerializerFeature.WriteNullListAsEmpty);
        serializerFeatureList.add(SerializerFeature.QuoteFieldNames);
        serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
        serializerFeatureList.add(SerializerFeature.BrowserCompatible);
        serializerFeatureList.add(SerializerFeature.WriteEnumUsingToString);
        serializerFeatureList.add(SerializerFeature.SortField);
        browserJsonFeature = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
    }

    static {
        List<SerializerFeature> serializerFeatureList = Lists.newArrayList();
//        serializerFeatureList.add(SerializerFeature.WriteClassName);
        serializerFeatureList.add(SerializerFeature.SkipTransientField);
        serializerFeatureList.add(SerializerFeature.WriteDateUseDateFormat);
        serializerFeatureList.add(SerializerFeature.WriteNullListAsEmpty);

        jsonFeatures = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
    }

    //    /**
    //     * 将对象输出为浏览器兼容的json字符串,以用于进行浏览器显示和处理
    //     *
    //     * @param limitProperties 限制的属性集,如果存在,则只会限制内的属性处理
    //     */
    //    public static <T> String toBrowserJson(T t, String... limitProperties) {
    //        try (SerializeWriter out = new SerializeWriter(browserJsonFeature)) {
    //            JSONSerializer serializer = new JSONContextableSerializer(out, browserJsonConfig);
    //
    //            //启用attr属性处理
    //            serializer.getAfterFilters().add(afterFilter);
    //            serializer.getPropertyPreFilters().add(propertyPreFilter);
    //
    //            //启用属性限制
    //            if(!ObjectUtils.isNullOrEmpty(limitProperties)) {
    //                serializer.getPropertyFilters().add(new LimitedPropertyFilter(Lists.newArrayList(limitProperties), serializer));
    //            }
    //
    //            serializer.write(t);
    //
    //            return out.toString();
    //        }
    //    }

    /** 将对象输出为可反序列化的json字符串，以用于数据存储和传输 */
    public static <T> String toJson(T t) {
        return JSON.toJSONString(t, jsonFeatures);
    }

    /** 将json字符串转换为指定类型的对象 */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /** 将json字符串转换为指定类型的对象，如果不能转换，则返回指定的默认值 */
    public static <T> T parse(String json, Class<T> clazz, T defaultValue) {
        try {
            return JSON.parseObject(json, clazz);
        } catch(RuntimeException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 将json字符串转换为指定的对象
     * 如果对象是按照指定的fastjson组织的，则转换为@type指定的对象，否则转换为JsonObject类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String json) {
        try {
            return (T) JSON.parse(json);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}