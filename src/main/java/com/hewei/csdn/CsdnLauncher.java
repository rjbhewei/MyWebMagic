package com.hewei.csdn;

import com.hewei.csdn.inits.CsdnRunner;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/10  14:59
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnLauncher {
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                CsdnRunner.start();
            }
        }.start();
    }
}
