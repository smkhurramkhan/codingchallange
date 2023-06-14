package com.example.codingchallange.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallange.databinding.ItemOptionBinding
import com.example.codingchallange.ui.main.vh.OptionsVH

class OptionsAdapter(
    val optionsList: List<String>,
    val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<OptionsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsVH {
        return OptionsVH(
            ItemOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionsVH, position: Int) {
        holder.binding.apply {
            tvOption.text = optionsList[position]

            llOption.setOnClickListener {
                onClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }


}