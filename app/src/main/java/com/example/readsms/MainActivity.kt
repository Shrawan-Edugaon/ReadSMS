package com.example.readsms

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val READ_SMS_PERMISSION_CODE = 1
    lateinit var listSMS:ArrayList<String>
    lateinit var btnReadSMS:Button
    lateinit var phoneSMSList:ListView
    lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReadSMS = findViewById(R.id.readSMSButton)
        phoneSMSList = findViewById(R.id.smsList)

        btnReadSMS.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {

        val checkPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)
        if (checkPermission == PackageManager.PERMISSION_GRANTED)
        {
            phoneSMSDetails()
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),READ_SMS_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            READ_SMS_PERMISSION_CODE ->{
                if (grantResults.size >=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    phoneSMSDetails()
                }
                else
                {
                    Toast.makeText(applicationContext,"You don't have permission",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun phoneSMSDetails() {
        val uri = Uri.parse("content://sms/inbox")
        listSMS = ArrayList()

        val contentResolver = contentResolver
        cursor = contentResolver.query(uri,null,null,null,null)!!

        while (cursor.moveToNext())
        {
            val number = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString()
            val smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
            listSMS.add("Number:$number \nBody : $smsBody")
        }
        cursor.close()

        val smsAdapter = ArrayAdapter<String>(this@MainActivity,android.R.layout.simple_list_item_1,listSMS)
        phoneSMSList.adapter=smsAdapter
    }
}