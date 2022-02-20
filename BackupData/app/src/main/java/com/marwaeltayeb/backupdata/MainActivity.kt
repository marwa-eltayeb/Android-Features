package com.marwaeltayeb.backupdata

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.marwaeltayeb.backupdata.databinding.ActivityMainBinding
import java.io.*
import java.lang.StringBuilder
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var gson: Gson? = null
    private var allJsonResponse: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customers = ArrayList<Customer>()
        customers.add(Customer("Hala", "developer", 34.5f))
        customers.add(Customer("Nora", "teacher", 43.5f))
        customers.add(Customer("Rana", "doctor", 56.5f))

        val employees = ArrayList<Employee>()
        employees.add(Employee("ALi", "carpenter"))
        employees.add(Employee("Yaser", "mechanic"))

        gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
        allJsonResponse = convertClassToJson(customers, employees)

        val allData = convertJsonToClass(allJsonResponse!!)
        Log.d(TAG, allData!!.customers.get(0).name)
        Log.d(TAG, allData.employees.get(0).nameEmployee)

        binding.btnExport.setOnClickListener {
            if (isStoragePermissionGranted()) {
                writeTextToFile(allJsonResponse)
            }
        }

        binding.btnImport.setOnClickListener {
            openFileManager()
        }
    }


    private fun convertClassToJson(customers: List<Customer>, employees: List<Employee>): String? {
        val allData = Data(customers, employees)
        return gson?.toJson(allData)
    }

    private fun convertJsonToClass(jsonResponse: String): Data? {
        val dataAllType = object : TypeToken<Data?>() {}.type
        return gson!!.fromJson(jsonResponse, dataAllType)
    }

    private fun writeTextToFile(jsonResponse: String?) {
        if (jsonResponse != "") {
            // Create a File object like this.
            val dir = File("//sdcard//Download//")
            val myExternalFile = File(dir, getRandomFileName())
            // Create an object of FileOutputStream for writing data to myFile.txt
            var fos: FileOutputStream? = null
            try {
                // Instantiate the FileOutputStream object and pass myExternalFile in constructor
                fos = FileOutputStream(myExternalFile)
                // Write to the file
                fos.write(jsonResponse?.toByteArray())
                // Close the stream
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(
                this,
                "Information saved to SD card. $myExternalFile",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
                true
            } else {
                //Permission is revoked
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else {
            // Permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    private fun readTextFromUri(uri: Uri): String {
        var inputStream: InputStream? = null
        val stringBuilder = StringBuilder()
        try {
            inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line = reader.readLine()
            // Read the entire file
            while (line != null) {
                // Append the line read to StringBuilder object. Also, append a new-line
                stringBuilder.append(line).append('\n')
                // Read the next line and store in variable line
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private fun getRandomFileName(): String {
        return Calendar.getInstance().timeInMillis.toString() + ".json"
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        getDataFromFile.launch(intent)
    }

    private var getDataFromFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                val fileContents = readTextFromUri(uri!!)
                Toast.makeText(this, fileContents, Toast.LENGTH_SHORT).show()
            }
        }
}
