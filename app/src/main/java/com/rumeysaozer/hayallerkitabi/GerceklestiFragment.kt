package com.rumeysaozer.hayallerkitabi

import android.Manifest
import android.app.Activity
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rumeysaozer.hayallerkitabi.databinding.FragmentGerceklestiBinding
import java.io.ByteArrayOutputStream
import java.lang.Exception


class GerceklestiFragment : Fragment() {
    private var _binding: FragmentGerceklestiBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private var selectedBitmap : Bitmap? = null
    private var mid = 1
    private var value = 1
    private var title = ""
    private var description = ""
    private var time = ""
    private var image = 1




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mid = GerceklestiFragmentArgs.fromBundle(it).mid
            value = GerceklestiFragmentArgs.fromBundle(it).value
            title = GerceklestiFragmentArgs.fromBundle(it).title
            description =  GerceklestiFragmentArgs.fromBundle(it).description
            time =  GerceklestiFragmentArgs.fromBundle(it).time
            image =  GerceklestiFragmentArgs.fromBundle(it).picture
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGerceklestiBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerLauncher()
        if(value==2){
            binding.textView2.text =title
            binding.date.setText("")
            binding.editTextTextMultiLine.setText("")
            binding.save.visibility = View.VISIBLE
        }
        else{
            binding.save.visibility = View.INVISIBLE
            try {
                val database = this.requireActivity().openOrCreateDatabase("Hayaller",
                    Context.MODE_PRIVATE,null)
                val cursor = database.rawQuery("SELECT * FROM dreams WHERE id = ?",arrayOf(mid.toString()))
                val titleIx = cursor.getColumnIndex("title")
                val descriptionIx = cursor.getColumnIndex("description")
                val timeIx = cursor.getColumnIndex("time")
                val imageIx = cursor.getColumnIndex("image")

                while (cursor.moveToNext()){
                    binding.textView2.text = title
                    binding.date.setText(time)
                    binding.editTextTextMultiLine.setText(description)
                    val byteArray = cursor.getBlob(imageIx)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    binding.imageAdd.setImageBitmap(bitmap)
                }
                cursor.close()
            }catch (e: Exception){
                e.printStackTrace()
            }

        }





        binding.imageAdd.setOnClickListener {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }else{
           val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
        }

        binding.save.setOnClickListener {
            val description = binding.editTextTextMultiLine.text.toString()
            val title = title
            val time = binding.date.text.toString()

        if(selectedBitmap != null){
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()
            try {
                val database = this.requireActivity().openOrCreateDatabase("Hayaller", Context.MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS dreams(id INTEGER PRIMARY KEY,title VARCHAR, description VARCHAR, time VARCHAR, image BLOB)")
                val sqlString = "INSERT INTO dreams(title, description, time, image) VALUES (?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,title)
                statement.bindString(2,description)
                statement.bindString(3,time)
                statement.bindBlob(4,byteArray)
                statement.execute()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
            val action = GerceklestiFragmentDirections.actionGerceklestiFragmentToGerceklesenHayaller()
            findNavController().navigate(action)
        }

    }



    private fun makeSmallerBitmap(image:Bitmap, maximumSize: Int): Bitmap{
        var width = image.width
        var height = image.height
        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if(bitmapRatio > 1){
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        }else{
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)

    }

private fun registerLauncher(){
activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
    ActivityResultCallback {
        if(it.resultCode == RESULT_OK){
            val intentFromResult = it.data
            if(intentFromResult != null){
               val imageUri = intentFromResult.data
               // binding.imageAdd.setImageURI(imageUri)
                if(imageUri != null){
                    try{
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageAdd.setImageBitmap(selectedBitmap)
                        }
                        else{
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                            binding.imageAdd.setImageBitmap(selectedBitmap)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    })
    permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }else{
           Toast.makeText(requireContext(), "Permission needed!", Toast.LENGTH_LONG).show()
        }

    }
}
}