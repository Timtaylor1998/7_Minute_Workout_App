package eu.tutorials.a7minuteworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View.*
import androidx.recyclerview.widget.LinearLayoutManager
import eu.tutorials.a7minuteworkoutapp.databinding.ActivityExerciseBinding
import eu.tutorials.a7minuteworkoutapp.databinding.DialogCustomBackConfirmationBinding
import eu.tutorials.a7minuteworkoutapp.databinding.FinishActivityBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1
    private var exerciseTimerDuration: Long = 1
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            setupCustomDialogForBackBtn()
        }

        tts = TextToSpeech(this, this)
        exerciseList = Constants.defaultExerciseList()

        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter

    }

    private fun setupRestView(){

        try {
            val soundURI = Uri.parse(
                "android.resource://eu.tutorials.a7_minutesworkoutapp/" +
                        R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = VISIBLE
        binding?.tvTitle?.visibility = VISIBLE
        binding?.tvUpcomingExercise?.visibility = VISIBLE
        binding?.tvExerciseName?.visibility = VISIBLE

        binding?.flExerciseView?.visibility = INVISIBLE
        binding?.tvExerciseTitle?.visibility = INVISIBLE
        binding?.ivImage?.visibility = INVISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvExerciseName?.text = (exerciseList!![currentExercisePosition + 1].getName())

        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(restTimerDuration * 10000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                exerciseRestView()

            }
        }.start()

    }

    private fun exerciseRestView(){
        binding?.flRestView?.visibility = INVISIBLE
        binding?.tvTitle?.visibility = INVISIBLE
        binding?.tvUpcomingExercise?.visibility = INVISIBLE
        binding?.tvExerciseName?.visibility = INVISIBLE

        binding?.flExerciseView?.visibility = VISIBLE
        binding?.tvExerciseTitle?.visibility = VISIBLE
        binding?.ivImage?.visibility = VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseTitle?.text = (exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()

        }

    private fun setExerciseProgressBar() {

        binding?.exerciseProgressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 10000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = 30 - exerciseProgress
                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()
            }
            override fun onFinish() {

                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter?.notifyDataSetChanged()
                    setupRestView()
                }else{
                        finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)

                }
            }
        }.start()

    }
    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "The Language specified is not supported")
                } else {
                    Log.e("TTS", "Initialization Failed!")
                }
            }
        }

    private fun speakOut(text: String) {
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }

    public override fun onDestroy() {


        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        super.onDestroy()
        binding = null
    }

    private fun setupCustomDialogForBackBtn(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onBackPressed() {
        setupCustomDialogForBackBtn()
    }

}


