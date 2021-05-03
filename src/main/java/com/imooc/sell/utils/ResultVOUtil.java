package com.imooc.sell.utils;

import com.imooc.sell.VO.ResultVO;

public class ResultVOUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg("成功");
        resultVO.setCode(0);
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO success() {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg("成功");
        resultVO.setCode(0);
        resultVO.setData(null);
        return resultVO;
    }

    public static ResultVO error(Integer code,String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }
}
