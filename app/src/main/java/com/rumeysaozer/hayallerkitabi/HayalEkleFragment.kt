package com.rumeysaozer.hayallerkitabi


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rumeysaozer.hayallerkitabi.databinding.FragmentHayalEkleBinding
import java.lang.Exception


class HayalEkleFragment : Fragment() {
    private var _binding: FragmentHayalEkleBinding? = null
    private val binding get() = _binding!!
    private var mid = 1
    private var value = 1
    private var title = ""
    private var description = ""
    private var time = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mid = HayalEkleFragmentArgs.fromBundle(it).mid
            value = HayalEkleFragmentArgs.fromBundle(it).value
            title =  HayalEkleFragmentArgs.fromBundle(it).title
            description =  HayalEkleFragmentArgs.fromBundle(it).description
            time = HayalEkleFragmentArgs.fromBundle(it).time
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHayalEkleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try{
            if(value == 1){
                binding.title.setText("")
                binding.date.setText("")
                binding.description.setText("")
                binding.save.visibility = View.VISIBLE
            }
            else{
                binding.save.visibility = View.INVISIBLE

                val database = this.requireActivity().openOrCreateDatabase("Hayaller",
                    Context.MODE_PRIVATE,null)
                val cursor = database.rawQuery("SELECT * FROM dream WHERE id = ?",arrayOf(mid.toString()))
                val titleIx = cursor.getColumnIndex("title")
                val descriptionIx = cursor.getColumnIndex("description")
                val timeIx = cursor.getColumnIndex("time")

                while (cursor.moveToNext()){
                    binding.title.setText(title)
                    binding.date.setText(time)
                    binding.description.setText(description)
                }
                cursor.close()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        binding.realized.setOnClickListener {
            val action = HayalEkleFragmentDirections.actionHayalEkleFragmentToGerceklestiFragment(1,2,binding.title.text.toString(),"","",0)
            findNavController().navigate(action)
        }
        binding.save.setOnClickListener {
          val title =   binding.title.text.toString()
            val description = binding.description.text.toString()
            val date = binding.date.text.toString()
            try {
                val database = this.requireActivity().openOrCreateDatabase("Hayaller",
                  Context.MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS dream(id INTEGER PRIMARY KEY,title VARCHAR, description VARCHAR, time VARCHAR)")
                val sqlString = "INSERT INTO dream(title, description, time) VALUES (?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,title)
                statement.bindString(2,description)
                statement.bindString(3,date)
                statement.execute()

            }catch (e: Exception){
                e.printStackTrace()
            }
            val action = HayalEkleFragmentDirections.actionHayalEkleFragmentToHayallerFragment()
            findNavController().navigate(action)
    }
}}