package com.fxqyem.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log

import java.util.ArrayList

/**
 * 
 */
object PermissionUtils {
    private val TAG = "PermissionUtils"
    val PERMISSION_REQUEST_CODE = 0x10
    val PERMISSION_SETTING_REQ_CODE = 0x1000

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermission(cxt: Any, permission: String, requestCode: Int): Boolean {
        if (!checkSelfPermissionWrapper(cxt, permission)) {
            if (!shouldShowRequestPermissionRationaleWrapper(cxt, permission)) {
                requestPermissionsWrapper(cxt, arrayOf(permission), requestCode)
            } else {
                Log.d(TAG, "should show rational")
            }
            return false
        }

        return true
    }


    private fun requestPermissionsWrapper(cxt: Any, permission: Array<String>, requestCode: Int) {
        if (cxt is Activity) {
            ActivityCompat.requestPermissions(cxt, permission, requestCode)
        } else if (cxt is Fragment) {
            cxt.requestPermissions(permission, requestCode)
        } else {
            throw RuntimeException("cxt is net a activity or fragment")
        }
    }


    private fun shouldShowRequestPermissionRationaleWrapper(cxt: Any, permission: String): Boolean {
        if (cxt is Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale(cxt,
                    permission)
        } else if (cxt is Fragment) {
            return cxt.shouldShowRequestPermissionRationale(permission)
        } else {
            throw RuntimeException("cxt is net a activity or fragment")
        }
    }

    @TargetApi(23)
    private fun checkSelfPermissionWrapper(cxt: Any, permission: String): Boolean {
        if (cxt is Activity) {
            return ActivityCompat.checkSelfPermission(cxt,
                    permission) == PackageManager.PERMISSION_GRANTED
        } else if (cxt is Fragment) {
            return cxt.activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            throw RuntimeException("cxt is net a activity or fragment")
        }
    }

    @TargetApi(23)
    private fun checkSelfPermissionArray(cxt: Any, permission: Array<String>): Array<String> {
        val permiList = ArrayList<String>()
        for (p in permission) {
            if (!checkSelfPermissionWrapper(cxt, p)) {
                permiList.add(p)
            }
        }

        return permiList.toTypedArray()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermissionArray(cxt: Any, permission: Array<String>, requestCode: Int): Boolean {
        val permissionNo = checkSelfPermissionArray(cxt, permission)
        if (permissionNo.size > 0) {
            requestPermissionsWrapper(cxt, permissionNo, requestCode)
            return false
        } else
            return true
    }

    fun verifyPermissions(grantResults: IntArray): Boolean {
        // At least one result must be checked.
        if (grantResults.size < 1) {
            return false
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * WRITE_SETTINGS 权限
     * @param cxt
     * *
     * @param req
     * *
     * @return
     */
    @TargetApi(23)
    fun checkSettingSystemPermission(cxt: Any, req: Int): Boolean {
        if (cxt is Activity) {
            val activity = cxt
            if (!Settings.System.canWrite(activity)) {
                Log.i(TAG, "Setting not permission")

                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + activity.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivityForResult(intent, req)
                return false
            }
        } else if (cxt is Fragment) {
            val fragment = cxt
            if (!Settings.System.canWrite(fragment.context)) {
                Log.i(TAG, "Setting not permission")

                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + fragment.context.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                fragment.startActivityForResult(intent, req)
                return false
            }
        } else {
            throw RuntimeException("cxt is not a activity or fragment")
        }

        return true
    }

    /**
     * 检测系统弹出权限

     * @param cxt
     * *
     * @param req
     * *
     * @return
     */
    @TargetApi(23)
    fun checkSettingAlertPermission(cxt: Any, req: Int): Boolean {
        if (cxt is Activity) {
            val activity = cxt
            if (!Settings.canDrawOverlays(activity.baseContext)) {
                Log.i(TAG, "Setting not permission")

                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.packageName))
                activity.startActivityForResult(intent, req)
                return false
            }
        } else if (cxt is Fragment) {
            val fragment = cxt
            if (!Settings.canDrawOverlays(fragment.activity)) {
                Log.i(TAG, "Setting not permission")

                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + fragment.activity.packageName))
                fragment.startActivityForResult(intent, req)
                val c: Context
                return false
            }
        } else {
            throw RuntimeException("cxt is not a activity or fragment")
        }

        return true
    }

}
