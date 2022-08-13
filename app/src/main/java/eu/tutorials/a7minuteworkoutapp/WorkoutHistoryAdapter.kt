package eu.tutorials.a7minuteworkoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.tutorials.a7minuteworkoutapp.databinding.ItemsRowBinding

class WorkoutHistoryAdapter(private val items: ArrayList<String>):RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder>(){

    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llWorkoutEntriesIcons = binding.llWorkoutEntriesIcons
        val tvPosition = binding.tvPosition
        val tvDate = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String = items.get(position)
        holder.tvPosition.text = (position + 1).toString()
        holder.tvDate.text = date


        if (position % 2 == 0) {
            holder.llWorkoutEntriesIcons.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorLightGray
                )
            )

        } else {
            holder.llWorkoutEntriesIcons.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
    }

        override fun getItemCount(): Int {
            return items.size
        }
    }

