package com.xdja.zzu.common;

/**
 * Author:kongguoguang
 * Date:2017-05-08
 * Time:14:08
 * Summary:
 */

public class ConstantErrorCode {

    //网络不通
    public static final int NETWORK_ERROR = -2;

    //主要参数错误
    public static final int PRIMARY_PARAM_ERROR = -3;

    //修改密码模式错误
    public static final int EDIT_PASSWORD_MODE_ERROR = -4;

    //认证签名错误
    public static final int AUTH_PARAM_SIGN_ERROR = -5;

    //imei为空
    public static final int IMEI_ERROR = -6;

    //challenge为空
    public static final int CHALLENGE_ERROR = -7;

    //response为空
    public static final int RESPONSE_ERROR = -8;

    //证书错误
    public static final int CERT_TYPE_ERROR = -9;

    /*
     * connect exception
     */
    public static final int CONNECT_ERROR = -100;
    /*
     * protocol error.
     */
    public static final int PROTOCOL_ERROR = -101;
    /*
     * socket error
     */
    public static final int SOCKET_ERROR = -102;
    /*
     * socket timeout
     */
    public static final int TIMEOUT = -103;

    //服务器响应内容错误
    public static final int REPONSE_ERROR = -104;

    /*   服务器返回的错误码   */
    public static final int INVALID_FORMAT = 1; /* 数据格式非法 */
    public static final int MISSING_PARAMS = 2; /* 参数缺失 */
    public static final int ILLEGAL_PARAMS = 3; /* 请求参数非法 */
    public static final int URL_DATA_DISMATCH = 4; /* url参数与元数据不一致 */
    public static final int SERVER_ERROR = 5;	/* 服务器内部错 */
    public static final int REQUEST_DISMATCH = 6; /* 请求方法不匹配 */
    public static final int TICKET_OUTDATED = 14; /* ticket过期 */
    public static final int TICKET_NOT_EXIST = 15; /* Ticket不存在 */
    public static final int UNKNOW_ERROR = 16; /* 未知错误 */
    public static final int ERROR_CERT_VERIFY = 22; /* 证书验签失败 */
    public static final int OVERTIME_CERT_VERIFY = 23; /* 证书验签超时 */
    public static final int UNKNOWN_USER_OR_SN = 24; /*  用户名或证书不存在 */
    public static final int UNKNOWN_TERMINAL = 25; /* 未知终端 */
    public static final int PASSWORD_ERROR = 26; /* 密码错误 */
    public static final int CERT_EXPIRED_REVOCATION = 27; /* 证书过期或被吊销 */
    public static final int INVALID_PASSWORD = 28; /* 无效密码 */
    public static final int TOKEN_IS_EXIST = 29; /* TOKEN信息已存在，此账户处于认证状态 */
    public static final int UNKNOWN_MOBILE = 30;/*手机号未绑定人员*/
    public static final int AUTHCODE_SEND_SOMUCH = 31;/*手机号已经发送短信验证码，请不要频繁获取*/
    public static final int AUTHCODE_SEND_LIMIT = 32;/*发送手机验证码上限*/
    public static final int AUTHCODE_SEND_ERROR = 33;/*发送手机验证码失败*/
    public static final int UAS_SERVER_EXCEPTION = 34;/*UAS服务器异常*/
    public static final int SAFEGROUP_NOT_FOUND = 35;/*无授权的网络*/
    public static final int FACE_INFO_NOT_FOUND = 36;/*未录入人脸信息*/
    public static final int FACE_INFO_SERVER_EXCEPTION = 37;/*人脸识别服务异常*/
    public static final int FACE_INFO_NOT_MATCH = 38;/*人脸匹配失败*/
    public static final int FINGERSIGN_NOT_FOUND = 39;/*手势密码未录入*/
    public static final int FINGERSIGN_NOT_MATCH = 40;/*手势密码匹配失败*/
    public static final int FAST_AUTH_FAILED = 41;/*快速认证失败*/
    public static final int FAST_AUTH_CODE_NOT_EXIST = 42;/*快速认证码不存在*/
    public static final int D_SERVER_EXCEPTION = 43;/*-D服务异常*/
    public static final int USER_IS_LOCK = 44;/*用户被冻结*/
    public static final int UAS_SERVER_ERROR = 45;/*UAS服务器返回错误信息*/
}
