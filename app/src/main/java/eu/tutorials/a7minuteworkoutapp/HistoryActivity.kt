package eu.tutorials.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.tutorials.a7minuteworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val workoutHistoryDao = (application as WorkoutHistoryApplication).db.workoutHistoryDao()
        getAllCompletedDates(workoutHistoryDao)

    }

    private fun getAllCompletedDates(workoutHistoryDao: WorkoutHistoryDao){
        Log.e("Date ", "getAllCompletedDates run")
        lifecycleScope.launch {
            workoutHistoryDao.fetchAllDates().collect(){allCompletedDatesList ->
                if(allCompletedDatesList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvWorkoutEntries?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                    binding?.rvWorkoutEntries?.layoutManager = LinearLayoutManager(this@HistoryActivity)


                    val dates = ArrayList<String>()
                    for(date in allCompletedDatesList){
                        dates.add(date.date)
                    }
                    val historyAdapter = WorkoutHistoryAdapter(dates)

                    binding?.rvWorkoutEntries?.adapter = historyAdapter

                }else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvWorkoutEntries?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}