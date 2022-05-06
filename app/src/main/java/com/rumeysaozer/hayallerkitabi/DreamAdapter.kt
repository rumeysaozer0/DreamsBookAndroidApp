package com.rumeysaozer.hayallerkitabi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rumeysaozer.hayallerkitabi.databinding.RecyclerRowBinding

class DreamAdapter(val dreamList: ArrayList<Dream>) : RecyclerView.Adapter<DreamAdapter.DreamHolder>(){

    class DreamHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DreamHolder(binding)
    }

    override fun onBindViewHolder(holder: DreamHolder, position: Int) {
    holder.binding.hayal.text = dreamList.get(position).title
    holder.itemView.setOnClickListener {
    val action = HayallerFragmentDirections.actionHayallerFragmentToHayalEkleFragment2(dreamList.get(position).id,2,dreamList.get(position).title,dreamList.get(position).date, dreamList.get(position).description)
        Navigation.findNavController(it).navigate(action)
    }
    }
    override fun getItemCount(): Int {
        return dreamList.size
    }
}