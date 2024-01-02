package com.example.lab15

import android.R.attr.duration
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab15.R.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class MainActivity : AppCompatActivity() {

    private lateinit var stopButton : Button
    private lateinit var playButton : Button
    private lateinit var pauseButton : Button
    private lateinit var curDurText : TextView
    private lateinit var leastDurText : TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private val handler: Handler = Handler()


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, raw.audio)
        mediaPlayer.setOnCompletionListener {
            stopPlay()
        }
        stopButton = findViewById(id.stopBtn)
        playButton = findViewById(id.playBtn)
        pauseButton = findViewById(id.pauseBtn)
        seekBar = findViewById(id.playBar)
        curDurText = findViewById(id.curDurText)
        leastDurText =findViewById(id.leastDurText)
        stopButton.setOnClickListener{
            stop()
        }
        playButton.setOnClickListener {
            play()
        }
        pauseButton.setOnClickListener {
            pause()
        }

        val maxDur = mediaPlayer.duration
        val curDur = mediaPlayer.currentPosition

        seekBar.max = maxDur
        seekBar.progress = curDur





        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                {
                    mediaPlayer.seekTo(progress)
                    seekBar.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }


    private fun stopPlay()
    {

        pauseButton.isEnabled = false
        stopButton.isEnabled = false
        try {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(this, raw.audio)
            mediaPlayer.setOnCompletionListener {
                stopPlay()}
            mediaPlayer.seekTo(0)
            playButton.isEnabled = true
        }
        catch (t : Throwable)
        {
            Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startPlayProgressUpdater() {
        seekBar.progress = mediaPlayer.currentPosition

        curDurText.text = "${mediaPlayer.currentPosition / 1000}"
        if (mediaPlayer.isPlaying) {
            val notification = Runnable { startPlayProgressUpdater() }
            handler.postDelayed(notification, 1000)
        } else {
            mediaPlayer.pause()
        }
    }

    private fun play() {
        mediaPlayer.start()
        playButton.isEnabled = false
        pauseButton.isEnabled = true
        stopButton.isEnabled = true
        startPlayProgressUpdater()
    }
    private fun stop() {
        stopPlay()
    }
    private fun pause() {
        mediaPlayer.pause()
        playButton.isEnabled = true
        pauseButton.isEnabled = false
        stopButton.isEnabled = true
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer.isPlaying)
        {
            stopPlay();
        }
    }


}