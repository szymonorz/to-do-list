package com.example.todolistkotlin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.todolistkotlin.interfaces.FullscreenDialogInterface
import java.util.*

class FullscreenDialog : DialogFragment(), View.OnClickListener {

    private lateinit var dialogInterface: FullscreenDialogInterface
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val _view = inflater.inflate(R.layout.dialog_layout, container, false)
        val calendarView = _view.findViewById<CalendarView>(R.id.calendar)
        calendarView.minDate = System.currentTimeMillis() - 1000
        titleEditText = _view.findViewById(R.id.title)
        descriptionEditText = _view.findViewById(R.id.description)
        dateEditText = _view.findViewById(R.id.editText2)
        val closeButton = _view.findViewById<ImageButton>(R.id.close)
        closeButton.setOnClickListener(this)
        val applyButton = _view.findViewById<ImageButton>(R.id.apply)
        applyButton.setOnClickListener(this)
        val c = Calendar.getInstance()
        lateinit var cDate: String
        if (c.get(Calendar.MONTH) + 1 < 10)
            cDate =
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
        else
            cDate =
                "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"

        dateEditText.setText(cDate)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            lateinit var dateString: String
            if (month + 1 < 10)
                dateString = "${year}-0${month + 1}-${dayOfMonth}"
            else
                dateString = "${year}-${month + 1}-${dayOfMonth}"
            dateEditText.setText(dateString)
        }



        return _view
    }

    private fun apply() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val date = dateEditText.text.toString()
        dialogInterface.applyText(title, description, date)
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogInterface = context as FullscreenDialogInterface
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.close -> dismiss()
            R.id.apply -> apply()
        }
    }


}