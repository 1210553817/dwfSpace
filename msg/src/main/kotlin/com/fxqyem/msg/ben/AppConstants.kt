package com.fxqyem.msg.ben


object AppConstants {
    val VW_STATE_GONE = 0
    val VW_STATE_SHOW = 1
    val VW_STATE_SHOW_START = 2
    val VW_STATE_GONE_START = 3

    /**Files*/
    val APP_BASE_PATH = "Android/data/com.fxqy.MSG"
    val DB_BASE_PATH = APP_BASE_PATH + "/database"
    val APP_TMP_PIC_PATH = APP_BASE_PATH + "/tmppic"
    val APP_BKGPIC_PATH = APP_BASE_PATH + "/bkgpic"

    /**Ations*/
    /*activity*/
    val ACTION_LOAD_MEM_ITEMS="com.fxqyem.MSG.LOAD_MEM_ITEMS"
    val ACTION_RECEIVE_MSG="com.fxqyem.MSG.RECEIVE_MSG"
    val ACTION_ALERT_MSG="com.fxqyem.MSG.ALERT_MSG"
    val ACTION_FILE_PRGRS_MSG="com.fxqyem.MSG.FILE_PRGRS_MSG"
    /*service*/
    val ACTION_SER_SEND_MSG="com.fxqyem.MSG.SER_SEND_MSG"
    val ACTION_SER_GET_FILE="com.fxqyem.MSG.SER_GET_FILE"
    val ACTION_SER_SEND_FILE="com.fxqyem.MSG.SER_SEND_FILE"
    val ACTION_SER_RELOAD_MEMLS="com.fxqyem.MSG.SER_RELOAD_MEMLS"
    val ACTION_SER_ACT_RESUME="com.fxqyem.MSG.SER_ACT_RESUME"
    val ACTION_SER_ACT_STOP="com.fxqyem.MSG.SER_ACT_STOP"

    /**pref Names*/
    val PREF_NAME_CHAT_NUMS = "chatdatas"
    val PREF_NAME_PARAMS = "params"
    /**pref keys*/
    val PREF_KEY_SELF_UNM = "k_SELF_UNM"
    val PREF_KEY_SELF_TIT = "k_SELF_TIT"
    val PREF_KEY_SELF_SUB = "k_SELF_SUB"
    val PREF_KEY_FILE_RCV_PATH = "k_FILE_RCV_PATH"

    /**Db Names*/
    val DB_NAME_MSGDATAS = "msgdatas.db"

    /**Db Tables*/
    val DB_TBNAME_MSGLS = "msg_ls"

    /**network*/
    val MSG_GLOBAL_PORT = 2425
    val MSG_GLOBAL_ZEROBYTE = 0x00.toByte()
    val MSG_GLOBAL_ZERO = String(byteArrayOf(MSG_GLOBAL_ZEROBYTE))

    val MSG_NOOPERATION = 0x00000000
    val MSG_BR_ENTRY = 0x00000001
    val MSG_BR_EXIT = 0x00000002
    val MSG_ANSENTRY = 0x00000003
    val MSG_BR_ABSENCE = 0x00000004

    val MSG_SENDMSG = 0x00000020
    val IPMSG_RECVMSG = 0x00000021
    val MSG_GETFILEDATA = 0x00000060
    val MSG_SENDCHECKOPT = 0x00000100
    val MSG_FILEATTACHOPT = 0x00200000




}
