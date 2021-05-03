package com.imooc.sell.utils;

import com.imooc.sell.enums.CodeEnum;

public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer integer, Class<T> enumClass){
        for(T each : enumClass.getEnumConstants()){
            if(integer.equals(each.getCode())){
                return each;
            }
        }
        return null;
    }
}
