package com.fxqyem.bean


object AppConstants {
    val VW_STATE_GONE = 0
    val VW_STATE_SHOW = 1
    val VW_STATE_SHOW_START = 2
    val VW_STATE_GONE_START = 3

    //Files
    val APP_BASE_PATH = "Android/data/com.fxqy.slimMusic"
    val DB_BASE_PATH = APP_BASE_PATH + "/database"
    val APP_TMP_PIC_PATH = APP_BASE_PATH + "/tmppic"
    val APP_BKGPIC_PATH = APP_BASE_PATH + "/bkgpic"

    //keys
    val PLAYER_KEY_LS = "com.fxqyem.player_LS"
    //CTRL
    val PLAYER_CTRL_ACTION_PLAY = "com.fxqyem.player_ctrl_action_PLAY"
    val PLAYER_CTRL_ACTION_NEXT = "com.fxqyem.player_ctrl_action_NEXT"
    val PLAYER_CTRL_ACTION_PREV = "com.fxqyem.player_ctrl_action_PREV"
    val PLAYER_CTRL_ACTION_INDX = "com.fxqyem.player_ctrl_action_INDX"
    val PLAYER_CTRL_ACTION_SENDLS = "com.fxqyem.player_ctrl_action_SENDLS"
    val PLAYER_CTRL_ACTION_SEEK = "com.fxqyem.player_ctrl_action_SEEK"
    val PLAYER_CTRL_ACTION_VOLUME = "com.fxqyem.player_ctrl_action_VOLUME"
    val PLAYER_CTRL_ACTION_STOP = "com.fxqyem.player_ctrl_action_STOP"
    val PLAYER_CTRL_ACTION_ACTIVITY_STOP = "com.fxqyem.player_ctrl_action_ACTIVITY_STOP"
    val PLAYER_CTRL_ACTION_ACTIVITY_RESUME = "com.fxqyem.player_ctrl_action_ACTIVITY_RESUME"
    val PLAYER_CTRL_ACTION_SHOWMODECHG = "com.fxqyem.player_ctrl_action_SHOWMODECHG"//显示模式改变
    //REVW
    val PLAYER_REVW_ACTION_PLAY = "com.fxqyem.player_revw_action_PLAY"
    val PLAYER_REVW_ACTION_PROG = "com.fxqyem.player_revw_action_PROG"
    val PLAYER_REVW_ACTION_SENDLS = "com.fxqyem.player_revw_action_SENDLS"


    //pref Names
    val PREF_NAME_DATAS = "datas"
    val PREF_NAME_PARAMS = "params"
    //pref keys
    val PREF_KEY_FAV_INDX = "k_favIndx"
    val PREF_KEY_TMPLS_INDX = "k_tmpLsIndx"
    val PREF_KEY_FRGMHM_LOCOPT = "k_frgmHmLocOpt"

    val PREF_KEY_APP_LS_BKG = "k_applsbkg"
    val PREF_KEY_APP_CTRL_BKG = "k_appctrlbkg"

    val PREF_KEY_BKG_PIC_ISBLUR = "k_bkgpicisblur"
    val PREF_KEY_BKG_SLD_PLAYMODE = "k_sldplayMode"



    //Db Names


    val DB_NAME_DATAS = "datas.db"
    val DB_NAME_PARAMS = "params.db"
    //table names
    val DB_TBNAME_FAVINFO = "fav_info"
    val DB_TBNAME_FAVLS = "fav_ls"
    val DB_TBNAME_LOCLS = "loc_ls"
    val DB_TBNAME_TMPLS = "tmp_ls"


}
