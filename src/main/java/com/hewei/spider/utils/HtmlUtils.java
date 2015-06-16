package com.hewei.spider.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  0:18
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class HtmlUtils {
	public static String getPlainText(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		try {
			Parser parser = new Parser();
			parser.setInputHTML(str);
			//            TextExtractingVisitor visitor=new TextExtractingVisitor();
			//            parser.visitAllNodesWith(visitor);
			//            str=visitor.getExtractedText();
			StringBean bean = new StringBean();
			bean.setLinks(false);//设置不需要得到页面锁包含的链接信息
			bean.setReplaceNonBreakingSpaces(true);//设置将不间断空格由正规空格所替代
			bean.setCollapse(true);//设置将一序列空格由一个单一空格所代替
			parser.visitAllNodesWith(bean);
			str = bean.getStrings();

		} catch (ParserException e) {
			e.printStackTrace();
		}

		return str;
	}

    public static Map<String, String> eval(String str) {
        if (StringUtils.isEmpty(str)) {
            return Maps.newHashMap();
        }
        String[] xx = str.split(";");

        Map<String, String> map = new HashMap<>();
        for (String x : xx) {
            String[] tmp = x.replace("var", "").trim().split("=");
            map.put(tmp[0], tmp[1]);
        }
        Set<String> keys = map.keySet();
        while (true) {
            int index = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                int loop = 0;
                for (String key : keys) {
                    if (!entry.getValue().contains(key)) {
                        loop++;
                        continue;
                    }
                    entry.setValue(entry.getValue().replace(key, "(" + map.get(key) + ")"));
                }
                if (loop == keys.size()) {
                    index++;
                }
            }
            if (index == keys.size()) {
                break;
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.setValue(String.valueOf(MVEL.eval(entry.getValue())));
        }
        return map;
    }

}
