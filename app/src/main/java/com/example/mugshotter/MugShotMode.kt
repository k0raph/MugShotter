package com.example.mugshotter

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.provider.MediaStore
import android.view.SurfaceView
import android.graphics.PixelFormat
import java.io.IOException

import android.hardware.Camera;
import android.content.ContentValues
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.OutputStream


class MugShotMode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mug_shot_mode)

        val selectedImg = intent.getParcelableExtra<Uri>("imageUri")

        val rootImgView = findViewById<ImageView>(R.id.rootImgView)
        rootImgView.setImageURI(selectedImg)

        rootImgView.setOnLongClickListener {
            finish()
            return@setOnLongClickListener true
        }

        val camera = Camera.open(1)
        val parameters = camera.parameters
        parameters.pictureFormat = PixelFormat.JPEG
        camera.parameters = parameters
        val mview = SurfaceView(baseContext)

        rootImgView.setOnClickListener {
            takePicture(camera, mview)
        }
    }

    var photoCallback: Camera.PictureCallback = Camera.PictureCallback { data, camera ->
        val uriTarget = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        val imageFileOS: OutputStream?

        try {

            imageFileOS = contentResolver.openOutputStream(uriTarget!!)
            imageFileOS!!.write(data)
            imageFileOS.flush()
            imageFileOS.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    private fun takePicture(camera: Camera, mview: SurfaceView) {
        try {
            camera.setPreviewDisplay(mview.holder)
            camera.startPreview()
            camera.takePicture( null, null, photoCallback)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
