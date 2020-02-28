package com.example.todolistkotlin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CustomDialog: AppCompatDialogFragment(){


    lateinit var dialogListener: CustomDialogListener

    lateinit var editText2: TextInputEditText
    lateinit var data: TextInputEditText
    lateinit var calendarView: CalendarView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_layout, null)
        val editText = view?.findViewById(R.id.title) as TextInputEditText
        editText2 = view.findViewById(R.id.description) as TextInputEditText
        data = view.findViewById(R.id.editText2)

        calendarView = view.findViewById(R.id.calendar) as CalendarView
        calendarView.minDate = System.currentTimeMillis() - 1000
        val c = Calendar.getInstance()
        lateinit var cDate: String
        if (c.get(Calendar.MONTH) + 1 < 10)
            cDate =
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
        else
            cDate =
                "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"


        data.setText(cDate)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            lateinit var dateString: String
            if (month + 1 < 10)
                dateString = "${year}-0${month + 1}-${dayOfMonth}"
            else
                dateString = "${year}-${month + 1}-${dayOfMonth}"
            data.setText(dateString)
        }

        dialog.setView(view)
            .setTitle("Add more")
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->

                    Toast.makeText(activity, "CANCEL", Toast.LENGTH_LONG).show()
                })
            .setPositiveButton("Send", DialogInterface.OnClickListener { dialog, which ->
                val title = editText.text.toString()
                val description = editText2.text.toString()
                val date = data.text.toString()
                dialogListener.applyText(title, description, date)
                Toast.makeText(activity, "SEND", Toast.LENGTH_LONG).show()
            })

        return dialog.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as CustomDialogListener
    }

    interface CustomDialogListener {
        fun applyText(title: String, description: String, date: String)
    }




}