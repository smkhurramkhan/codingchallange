package com.example.codingchallange.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallange.databinding.ItemSchoolsBinding
import com.example.codingchallange.details.model.SchoolDetails
import com.example.codingchallange.main.model.Schools
import com.example.codingchallange.main.viewholder.SchoolsVH

class SchoolsAdapter(
    val context: Context,
    val mSchools: SchoolDetails?,
    val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<SchoolsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolsVH {
        return SchoolsVH(
            ItemSchoolsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SchoolsVH, position: Int) {
        holder.binding.apply {
            schoolName.text = mSchools?.get(position)?.school_name
            schoolWebsite.text = mSchools?.get(position)?.num_of_sat_test_takers
            totalStudents.text = mSchools?.get(position)?.sat_writing_avg_score
        }

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mSchools?.size!!
    }

}