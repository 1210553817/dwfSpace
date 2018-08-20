package com.fxqyem.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.ref.WeakReference


object BitMapUtil {
    private val TAG = "BitMapUtil"

    fun getBitmapFromFile(fpath: String?): Bitmap? {
        var fis: FileInputStream? = null
        var bitmap: Bitmap?=null
        try {
            fis = FileInputStream(fpath)
            bitmap = BitmapFactory.decodeStream(fis)
        } catch (e: Exception) {
            Log.e(TAG, "读取Bitmap失败：" + e.message)
        } finally {
            try {
                if (fis != null) fis.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return bitmap
    }

    fun convertToBitmap(path: String, w: Int, h: Int): Bitmap {
        val opts = BitmapFactory.Options()
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888
        // 返回为空
        BitmapFactory.decodeFile(path, opts)
        val width = opts.outWidth
        val height = opts.outHeight
        var scaleWidth = 0f
        var scaleHeight = 0f
        if (width > w || height > h) {
            scaleWidth = width.toFloat() / w
            scaleHeight = height.toFloat() / h
        }
        opts.inJustDecodeBounds = false
        val scale = Math.max(scaleWidth, scaleHeight)
        opts.inSampleSize = scale.toInt()
        val weak = WeakReference(BitmapFactory.decodeFile(path, opts))
        return Bitmap.createScaledBitmap(weak.get(), w, h, true)
    }


    fun saveBitmap(bm: Bitmap, path: String, name: String) {
        if (!SDCardUtils.isSDCardEnable) return
        val f = File(path)
        if (!f.exists()) {
            f.mkdirs()
        }
        val fpth = path + File.separator + name
        try {
            val out = FileOutputStream(fpth)
            bm.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Log.e(TAG, "保存Bitmap为文件时失败：" + e.message)
        }

    }

    fun bitmapCrop(bitmap: Bitmap?, wt: Int, ht: Int): Bitmap {
        val w = bitmap?.width // 得到图片的宽，高
        val h = bitmap?.height

        val wh = if (w!! > wt) wt else w
        val hh = if (h!! > ht) ht else h

        val rebitmp = Bitmap.createBitmap(bitmap, 0, 0, wh, hh, null, false)
        return rebitmp
    }

    fun cutPic4Result(mact: Activity, imgf: File, w: Int, h: Int) {
        val mUri = getImageContentUri(mact, imgf) ?: return
        val intent = Intent()
        intent.action = "com.android.camera.action.CROP"
        intent.setDataAndType(mUri, "image/*")// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)// 裁剪框比例
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", w)// 输出图片大小
        intent.putExtra("outputY", h)
        intent.putExtra("return-data", true)

        mact.startActivityForResult(intent, 200)
        //        private void cropImageUri(Uri uri, int requestCode){
        //            Intent intent = new Intent("com.android.camera.action.CROP");
        //            intent.setDataAndType(uri, "image/*");
        //            intent.putExtra("crop", "true");
        //            intent.putExtra("aspectX", aspectX);
        //            intent.putExtra("aspectY", aspectY);
        //            intent.putExtra("outputX", outputX);
        //            intent.putExtra("outputY", outputY);
        //            intent.putExtra("scale", true);
        //            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //            intent.putExtra("return-data", false);
        //            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //            intent.putExtra("noFaceDetection", true); // no face detection
        //            startActivityForResult(intent, requestCode);
        //        }
        //        这个函数存在问题，裁减时打开图片的uri与保存图片的uri相同，产生冲突，导致裁减完成后图片的大小变成0Byte。
        //        可将相机照片保存在另外的位置，将intent.setDataAndType(uri, "image/*");中的uri换成相机照片倮存的路径即可。
    }

    fun getImageContentUri(context: Context, imageFile: java.io.File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
        //
        //        Uri path = Uri.fromFile(new File(Environment
        //                .getExternalStorageDirectory(),"temp.jpg"));
    }

    fun getFilePathFromContentUri(selectedVideoUri: Uri,
                                  contentResolver: ContentResolver): String {
        val filePath: String
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null)
        //      也可用下面的方法拿到cursor
        //      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }

    fun matrixBitmap(oriBitmp:Bitmap,farr: FloatArray): Bitmap{
        val pat = Paint()
        pat.colorFilter=ColorMatrixColorFilter(farr)
        var bitMp=Bitmap.createBitmap(oriBitmp.width,oriBitmp.height,oriBitmp.config)
        val cvs = Canvas(bitMp)
        cvs.drawBitmap(oriBitmp,Matrix(),pat)

        return bitMp
    }

    fun getMatrixDrawableByRid(ctx: Context,rid: Int,farr: FloatArray): Drawable{
        val bmp = BitmapFactory.decodeResource(ctx.resources,rid)
        val nbmp=matrixBitmap(bmp,farr)
        val drawable = BitmapDrawable(ctx.resources,nbmp)

        return drawable

    }

    fun blurBitmap(context: Context,bitmap: Bitmap,radius: Float): Bitmap {
        if(context==null||bitmap==null||radius<1||radius>25) return bitmap
        val w=90
        val h=160
        val outBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val reBitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val allIn = Allocation.createFromBitmap(rs, reBitmap)
        val allOut = Allocation.createFromBitmap(rs, outBitmap)
        blurScript.setRadius(radius)
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)
        allOut.copyTo(reBitmap)
        rs.destroy()
        return reBitmap
    }


}
