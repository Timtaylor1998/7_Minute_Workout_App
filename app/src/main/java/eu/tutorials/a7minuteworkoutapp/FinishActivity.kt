package eu.tutorials.a7minuteworkoutapp


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import eu.tutorials.a7minuteworkoutapp.databinding.FinishActivityBinding
import kotlinx.coroutines.launch
import java.util.*
import java.text.SimpleDateFormat

class FinishActivity : AppCompatActivity() {
    private var binding: FinishActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FinishActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val workoutHistoryDao = (application as WorkoutHistoryApplication).db.workoutHistoryDao()
        addDateToDatabase(workoutHistoryDao)
    }

    private fun addDateToDatabase(workoutHistoryDao: WorkoutHistoryDao){

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date: ", "" +dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date: ", "" +date)

        lifecycleScope.launch {
            workoutHistoryDao.insert(WorkoutHistoryEntity(date))
            Log.e(
                "Date: ",
                "Added..."
            )
        }

    }
}


