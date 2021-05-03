package com.imooc.sell.utils;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

public class KeyUtil {
    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     * */
    public static synchronized String genUniqueKey(){
        Random random = new Random();

        System.currentTimeMillis();

        Integer number = random.nextInt(900000)+100000;

        return System.currentTimeMillis()+ String.valueOf(number);
    }
}
