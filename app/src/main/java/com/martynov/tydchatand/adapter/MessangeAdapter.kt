package com.martynov.tydchatand.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.martynov.tydchatand.R
import com.martynov.tydchatand.model.AutorModel
import com.martynov.tydchatand.model.MessangeModel

class MessangeAdapter(private val list: MutableList<MessangeModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var delBtnClickListener: OnDelBtnClickListener? = null
    var me: AutorModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ideaView =
                LayoutInflater.from(parent.context).inflate(R.layout.iteam_chat, parent, false)
        return MessangeViewHolder(this, ideaView)
    }

    interface OnDelBtnClickListener {
        fun onDelBtnClicked(item: MessangeModel, position: Int)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ideaIndex = position
        when (holder) {
            is MessangeViewHolder -> holder.bind(list[ideaIndex])
        }

    }

    inner class MessangeViewHolder(val adapter: MessangeAdapter, view: View) : RecyclerView.ViewHolder(view) {



        fun bind(message: MessangeModel) {
            with(itemView) {
                val myMesange: TextView = findViewById(R.id.myMesseng)
                myMesange.text = message.messangeText
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

}