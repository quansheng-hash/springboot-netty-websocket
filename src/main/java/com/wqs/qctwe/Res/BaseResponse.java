package com.wqs.qctwe.Res;

import lombok.Data;

/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
@Data
public class BaseResponse {

    private static final int SUCCESS=0;
    private static final int FAIL=1;

    private int code;
    private String msg;
    private Object data;



    public BaseResponse(int code, String msg, Object o) {
        this.code = code;
        this.msg = msg;
        this.data = o;
    }
    public static BaseResponse  successToClient(Object o){
        return new BaseResponse(BaseResponse.SUCCESS,"ok",o);
    }
    public static BaseResponse  failToClient(String msg){
        return new BaseResponse(BaseResponse.FAIL,msg,null);
    }
}
