package com.fxqyem.brw.act

import android.os.Bundle
import android.view.KeyEvent
import com.fxqyem.brw.ben.BackKeyLsn
import com.fxqyem.brw.lay.BrwLay


/**
 * Created by Dwf on 2018-12-23
 */
class BrwActivity : BrwBaseActivity() , BackKeyLsn {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(BrwLay.createView(this))

    }

    override fun onBackKeyUp(event: KeyEvent?, holder: BackKeyLsn.BackHolder): Boolean {

        return false
    }

    companion object {
        private val TAG = "MsgActivity"
        /*back key*/
    }
}