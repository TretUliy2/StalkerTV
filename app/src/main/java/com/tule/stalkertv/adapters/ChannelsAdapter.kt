package com.tule.stalkertv.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tule.stalkertv.R

class ChannelsAdapter (private val channels : Array<String>)
    : RecyclerView.Adapter<ChannelsAdapter.ChannelsViewHolder>() {
    private var listener : Listener? = null

    interface Listener {
        fun onClick(position: Int)
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    fun setListener(newListener : Listener) {
        listener = newListener
    }


    override fun onBindViewHolder(holder: ChannelsViewHolder, position: Int) {
        val textView = holder.linearView.findViewById<TextView>(R.id.channel_text_view)
        holder.linearView.setOnClickListener({ listener?.onClick(position) })
        textView.text = channels[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.channel_text_view, parent, false) as LinearLayout

        return ChannelsViewHolder(linearLayout)
    }

    class ChannelsViewHolder ( val linearView: LinearLayout) : RecyclerView.ViewHolder(linearView)
}