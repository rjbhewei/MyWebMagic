package com.hewei.spider.main;

import com.hewei.spider.processor.SpiderProcessor;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  0:21
 * @desc
 */
public class Launcher {

	public static void main(String[] args) {

//		new Thread() {
//
//			@Override
//			public void run() {
//				BaiduBaikeProcessor.start();
//			}
//		}.start();

		new Thread() {

			@Override
			public void run() {
				SpiderProcessor.start();
			}
		}.start();

//		new Thread() {
//
//			@Override
//			public void run() {
//				SpiderProcessor.scan();
//			}
//		}.start();
	}

}
