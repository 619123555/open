package com.open.common.constants;

/**
 * 常量类
 */
public class Constants {

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final int HASH_INTERATIONS = 1;
    public static final int SALT_SIZE = 8;
    public static final String EMAILMATCH =
            "^([a-z0-9A-Z]+[-|\\.|\\_]*)+@(([a-z0-9A-Z]+[-|\\.|\\_]*)+([a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String MOBILE_MATCH =
            "^\\d{11}$";
    public static String SESSION_USER = "session_user";
    public static String CARD_USER_MENU = "trans:user:menu:";
    public static String CARD_SYS_DICT = "trans:sys:dict:";
    public static String CARD_SYS_DICT_TYPE = "trans:sys:dict:type:";
}
