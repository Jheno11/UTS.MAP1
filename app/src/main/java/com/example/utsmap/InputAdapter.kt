package com.example.utsmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InputAdapter(private val inputs: List<Pair<String, Int>>) : RecyclerView.Adapter<InputAdapter.InputViewHolder>() {

    class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.input_text_view)
        val imageView: ImageView = itemView.findViewById(R.id.input_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_input, parent, false)
        return InputViewHolder(view)
    }

    override fun onBindViewHolder(holder: InputViewHolder, position: Int) {
        holder.textView.text = inputs[position].first
        holder.imageView.setImageResource(inputs[position].second) // Set the image
    }

    override fun getItemCount(): Int {
        return inputs.size
    }
}
