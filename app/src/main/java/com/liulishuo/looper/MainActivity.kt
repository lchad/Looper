package com.liulishuo.looper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var imageBuffer: ImageView
    private var disposable: Disposable? = null

    companion object {
        const val interval = 4000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JupiterGlobal.init(applicationContext)
        setContentView(R.layout.activity_main)
        initView()

        val arr = arrayOf(
            R.drawable.timothy1,
            R.drawable.timothy4,
            R.drawable.timothy5,
            R.drawable.timothy6,
            R.drawable.timothy7,
            R.drawable.timothy8
        )
        disposable =
            Observable.interval(0, interval, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                val position: Int = (it % (arr.size)).toInt()
                Log.e("####", "it: $position ${arr[position]}")
                updateAd(arr[position])
            }
        MediaPlayerUtils.getInstance().play(R.raw.cannon, true)
    }

    private fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        supportActionBar?.hide()
        actionBar?.hide()
        image = findViewById(R.id.image) as ImageView
        imageBuffer = findViewById(R.id.image_buffer) as ImageView
    }

    private fun updateAd(id: Int) {
        val backImg: ImageView
        if (image.isShown) {
            imageBuffer.visibility = View.VISIBLE
            image.visibility = View.INVISIBLE
            backImg = image
        } else {
            imageBuffer.visibility = View.INVISIBLE
            image.visibility = View.VISIBLE
            backImg = imageBuffer
        }

        Glide.with(this).load(id).dontAnimate().dontTransform().into(backImg)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
