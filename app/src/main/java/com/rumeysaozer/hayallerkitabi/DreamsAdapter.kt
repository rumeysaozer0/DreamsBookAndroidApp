package com.rumeysaozer.hayallerkitabi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rumeysaozer.hayallerkitabi.databinding.RecyclerRowBinding

class DreamsAdapter(val dreamsList : ArrayList<Dreams>): RecyclerView.Adapter<DreamsAdapter.DreamsHolder>() {
    class DreamsHolder(val binding : RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamsHolder {
       val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DreamsHolder(binding)

    }
    override fun onBindViewHolder(holder: DreamsHolder, position: Int) {
      holder.binding.hayal.text = dreamsList.get(position).title
        holder.itemView.setOnClickListener{
            val action = GerceklesenHayallerDirections.actionGerceklesenHayallerToGerceklestiFragment(dreamsList.get(position).id,1,dreamsList.get(position).title,
                dreamsList.get(position).description, dreamsList.get(position).time, 1)
            Navigation.findNavController(it).navigate(action)
        }
    }
    override fun getItemCount(): Int {
      return dreamsList.size
    }
}