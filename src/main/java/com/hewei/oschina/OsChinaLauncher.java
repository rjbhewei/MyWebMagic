package com.hewei.oschina;

import com.hewei.oschina.inits.OsChinaRunner;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:04
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaLauncher {

    public static void main(String[] args) {

        new Thread() {

            @Override
            public void run() {
                OsChinaRunner.start();
            }
        }.start();


    }
}
