package com.rumeysaozer.hayallerkitabi

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rumeysaozer.hayallerkitabi.databinding.FragmentGerceklesenHayallerBinding



class GerceklesenHayaller : Fragment() {
    private var _binding: FragmentGerceklesenHayallerBinding? = null
    private val binding get() = _binding!!
    private lateinit var dreamsList : ArrayList<Dreams>
    private lateinit var dreamsAdapter: DreamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGerceklesenHayallerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dreamsList = ArrayList<Dreams>()
        dreamsAdapter = DreamsAdapter(dreamsList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = dreamsAdapter
        try{
            val database = this.requireActivity().openOrCreateDatabase("Hayaller",
                Context.MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM dreams",null)
            val titleIx = cursor.getColumnIndex("title")
            val idIx = cursor.getColumnIndex("id")
            val descriptionIx = cursor.getColumnIndex("description")
            val timeIx = cursor.getColumnIndex("time")
            val imageIx = cursor.getColumnIndex("image")
            while(cursor.moveToNext()){
                val title = cursor.getString(titleIx)
                val id = cursor.getInt(idIx)
                val time = cursor.getString(timeIx)
                val description = cursor.getString(descriptionIx)
                val byteArray = cursor.getBlob(imageIx)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                val dream = Dreams(title,id,description,time,bitmap)
                dreamsList.add(dream)
            }
            dreamsAdapter.notifyDataSetChanged()
            cursor.close()
        }catch(e : Exception){
            e.printStackTrace()
        }


    }

}