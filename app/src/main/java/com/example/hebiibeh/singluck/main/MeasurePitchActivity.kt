package com.example.hebiibeh.singluck.main

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.common.FFT4g
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_measure_pitch.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.concurrent.thread
import kotlin.math.*


class MeasurePitchActivity : AppCompatActivity() {

    // サンプリングレート（Hz）
    // １秒間に何回サンプリングするか。サンプリング回数/秒
    // 全デバイスサポートは44100のみ
    private val samplingRate = 44100

    // フレームレート（処理回数/秒）
    // onPeriodicNotificationメソッドにて、フレームレートごとに処理を行う。
    private val frameRate = 10

    // 1フレームのサンプリングデータの数（Short値）
    private val oneFrameDataCount = samplingRate / frameRate

    // 1フレームの音声データのバイト数
    private var oneFrameSizeInByte = oneFrameDataCount * 2

    // 音声データのバッファサイズ（byte）
    // oneFrameSizeInByteより大きくする→１フレーム毎の処理をする際に、データが入りきらない。
    // デバイスの要求する最小値より大きくする→小さすぎる（精度を上げる）とデバイスが対応できない。
    private val audioBufferSizeInByte =
        max(
            oneFrameSizeInByte * 10,
            AudioRecord.getMinBufferSize(samplingRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        )

    private val FFT_SIZE = 4096

    private val db_baseline = 2.0.pow(15.0) * FFT_SIZE * sqrt(2.0) * 1000

    private val resol = ((samplingRate.toDouble() / FFT_SIZE.toDouble()))

    private lateinit var audioRecord: AudioRecord

    private var testCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure_pitch)
        initAudioRecord()
        initBtn()
        initBar(0.0)
    }

    private fun initBar(frequency: Double) {
        bar_chart.data = BarData(setBarData(frequency))


        bar_chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 100f
            labelCount = 5
            setDrawTopYLabelEntry(true)
            setValueFormatter { value, axis -> "" + value.toInt() }
        }
        bar_chart.axisRight.apply {
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawZeroLine(false)
            setDrawTopYLabelEntry(true)
        }
        val labels = arrayOf("Pitch")
        bar_chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            labelCount = 1
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }

        bar_chart.apply {
            setDrawValueAboveBar(true)
            description.isEnabled = false
            isClickable = false
            legend.isEnabled = false
            setScaleEnabled(false)
            //animateY(1200, Easing.EasingOption.Linear)
        }

    }

    private fun initAudioRecord() {
        // AudioRecordオブジェクトを作成
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            samplingRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            audioBufferSizeInByte
        )

        // onPeriodicNotificationを呼び出すタイミングを設定（1フレーム毎とする）
        audioRecord.positionNotificationPeriod = oneFrameDataCount


        // 1フレーム毎の音声データを格納する配列
        val audioDataArray = ByteArray(oneFrameDataCount)

        audioRecord.setRecordPositionUpdateListener(object : AudioRecord.OnRecordPositionUpdateListener {
            override fun onMarkerReached(recorder: AudioRecord?) {
            }

            override fun onPeriodicNotification(recorder: AudioRecord?) {
//                testCount++
//                audioRecord.stop()
//                audioRecord.release()
//                initAudioRecord()
//                audioRecord.startRecording()
//                Log.d("呼び出された回数", testCount.toString())
                thread {
                    recorder?.read(audioDataArray, 0, oneFrameDataCount)
                    editSound(audioDataArray)
                    if (testCount < 10) {
                        audioRecord.stop()
                        audioRecord.release()
                        initAudioRecord()
                        audioRecord.startRecording()
                    }
                }
            }
        })

    }

    private fun editSound(buf: ByteArray) {
        var bf = ByteBuffer.wrap(buf)
        bf.order(ByteOrder.LITTLE_ENDIAN)
        var s = ShortArray((audioBufferSizeInByte * 2))
        for (i in bf.position() until bf.capacity() / 2 step 1) {
            s[i] = bf.short
        }

        var fft = FFT4g(FFT_SIZE)
        var FFTdata = DoubleArray(FFT_SIZE)
        for (i in 0 until FFT_SIZE step 1) {
            FFTdata[i] = s[i].toDouble()
        }
        fft.rdft(1, FFTdata)

        var dbfs = DoubleArray(FFT_SIZE / 2)
        var max_db = (-120).toDouble()
        var max_i = 0
        for (i in 0 until FFT_SIZE step 2) {
            dbfs[i / 2] = (20 * log10(
                sqrt(
                    FFTdata[i].pow(2)
                            + FFTdata[i + 1].pow(2)
                ) / db_baseline
            ))
            if (max_db < dbfs[i / 2]) {
                max_db = dbfs[i / 2]
                max_i = i / 2
            }
        }

        //frequencyTxt.text = ("周波数：" + resol * max_i + " [Hz] 音量：" + max_db + " [dB]")
        var frequency = "%.3f".format(resol * max_i)
        frequencyTxt.text = ("周波数：" + "%.3f".format(resol * max_i) + " [Hz]")

        bar_chart.data = BarData(setBarData(frequency.toDouble()))

        // TODO
//        var data = bar_chart.data
//        var dataSet = data.getDataSetByIndex(0)
//        var entry = BarEntry(dataSet.entryCount.toFloat(), ((frequency.toFloat() / 1000f) * 100f), 0)
//        data.addEntry(entry, 0)
        bar_chart.notifyDataSetChanged()
        bar_chart.invalidate()
    }

    private fun initBtn() {
        startMeasureBtn.setOnClickListener {
            if (audioRecord != null) {
                audioRecord.startRecording()
            }
        }
        stopMeasureBtn.setOnClickListener {
            if (audioRecord != null) {
                audioRecord.stop()
                audioRecord.release()
            }
            // TODO:TEST用
            testCount = 0
        }
    }

    private fun setBarData(frequency: Double): ArrayList<IBarDataSet> {
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(1f, ((frequency / 1000) * 100).toFloat()))
            // TODO:TEST
            //add(BarEntry(1f, 60f))
        }

        val dataSet = BarDataSet(entries, "bar").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            isHighlightEnabled = false
            setColors(intArrayOf(R.color.material_green), this@MeasurePitchActivity)
        }

        val bars = ArrayList<IBarDataSet>()
        bars.add(dataSet)
        return bars
    }
}