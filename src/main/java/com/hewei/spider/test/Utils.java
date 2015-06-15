package com.hewei.spider.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class Utils {
	public static ObjectMapper mapper = new ObjectMapper(); 
	public static ObjectMapper getMapper(){
		return mapper;
	}
	public static String toJson(Object o){
		try {
			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			return writer.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "转换JSON时发生异常";
		}
	}

    public static Article readMessage(String message) {
        try {
            return mapper.readValue(message, Article.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
