package com.example.todolistkotlin

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_layout.*
import com.example.todolistkotlin.MainActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*

class CustomDialog: AppCompatDialogFragment(){


    lateinit var dialogListener: CustomDialogListener

    lateinit var editText2: TextInputEditText
    lateinit var calendarView: CalendarView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_layout, null)
        val editText = view?.findViewById(R.id.editText) as TextInputEditText
        editText2 = view.findViewById(R.id.editText2) as TextInputEditText

        calendarView = view.findViewById(R.id.calendar) as CalendarView
        calendarView.minDate = System.currentTimeMillis() - 1000
        val c = Calendar.getInstance()
        val cDate = "${c.get(Calendar.YEAR)}/${c.get(Calendar.MONTH) +1}/${c.get(Calendar.DAY_OF_MONTH)}"


        editText2.setText(cDate)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var dataString = "${year}/${month+1}/${dayOfMonth}"
            editText2.setText(dataString)
        }

        dialog.setView(view)
            .setTitle("Add more")
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->

                    Toast.makeText(activity, "CANCEL", Toast.LENGTH_LONG).show()
                })
            .setPositiveButton("Send", DialogInterface.OnClickListener { dialog, which ->
                val name = editText.text.toString()
                val name2 = editText2.text.toString()
                dialogListener.applyText(name, name2)
                Toast.makeText(activity, "SEND", Toast.LENGTH_LONG).show()
            })

        return dialog.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as CustomDialogListener
    }

    interface CustomDialogListener {
        fun applyText(name: String, scnd: String)
    }




}