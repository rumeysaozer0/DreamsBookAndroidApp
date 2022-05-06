package com.rumeysaozer.hayallerkitabi

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rumeysaozer.hayallerkitabi.databinding.FragmentHayalEkleBinding
import com.rumeysaozer.hayallerkitabi.databinding.FragmentHayallerBinding


class HayallerFragment : Fragment() {
    private var _binding: FragmentHayallerBinding? = null
    private val binding get() = _binding!!
    private lateinit var dreamList : ArrayList<Dream>
    private lateinit var dreamAdapter: DreamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHayallerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dreamList = ArrayList<Dream>()
        dreamAdapter = DreamAdapter(dreamList)
        binding.recyclerViewHayaller.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHayaller.adapter = dreamAdapter
        try{
        val database = this.requireActivity().openOrCreateDatabase("Hayaller",  Context.MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM dream",null)
            val titleIx = cursor.getColumnIndex("title")
            val idIx = cursor.getColumnIndex("id")
            val descriptionIx = cursor.getColumnIndex("description")
            val timeIx = cursor.getColumnIndex("time")
            while(cursor.moveToNext()){
               val title = cursor.getString(titleIx)
               val id = cursor.getInt(idIx)
                val description = cursor.getString(descriptionIx)
                val time = cursor.getString(timeIx)
                val dream = Dream(title,id,description,time)
                dreamList.add(dream)
            }
            dreamAdapter.notifyDataSetChanged()
            cursor.close()

        }catch (e: Exception){
            e.printStackTrace()

        }
        binding.add.setOnClickListener {
            val action = HayallerFragmentDirections.actionHayallerFragmentToHayalEkleFragment2(1,1,"","","")
            findNavController().navigate(action)
        }
        binding.switch1.setOnClickListener {
            val action = HayallerFragmentDirections.actionHayallerFragmentToGerceklesenHayaller()
            findNavController().navigate(action)
        }
    }


}