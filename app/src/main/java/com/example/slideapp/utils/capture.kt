import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

fun captureScreen(activity: Activity) {
    if (isWriteExternalStoragePermissionGranted(activity)) {
        takeScreenshot(activity)
    } else {
        requestWriteExternalStoragePermission(activity)
    }
}

private fun takeScreenshot(activity: Activity) {
    val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
    rootView.isDrawingCacheEnabled = true
    val screenshot = rootView.drawingCache

    try {
        val fileName = "screenshot.jpg"
        val screenshotFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)
        val outputStream = FileOutputStream(screenshotFile)
        screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val contentResolver = activity.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        MediaScannerConnection.scanFile(activity, arrayOf(screenshotFile.absolutePath), null, null)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        rootView.isDrawingCacheEnabled = false
    }
}

private fun isWriteExternalStoragePermissionGranted(activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

private fun requestWriteExternalStoragePermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        REQUEST_WRITE_EXTERNAL_STORAGE
    )
}