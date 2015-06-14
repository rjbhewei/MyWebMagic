package com.hewei.spider.utils;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;

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

}
