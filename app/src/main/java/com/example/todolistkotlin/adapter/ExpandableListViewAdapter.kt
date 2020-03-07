package com.example.todolistkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.model.DaysClass

class ExpandableListViewAdapter(ctx: Context, groups: List<DaysClass>) :
    BaseExpandableListAdapter() {
    var ctx: Context
    var groups: List<DaysClass>

    init {
        this.ctx = ctx
        this.groups = groups
    }


    override fun getGroup(groupPosition: Int): Any {
        return this.groups.get(groupPosition)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val Title = this.groups.get(groupPosition).date
        var _converterView = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(ctx)
            _converterView = layoutInflater.inflate(R.layout.expandable_days, null)
        }
        val dateText = _converterView!!.findViewById<TextView>(R.id.title)
        dateText.text = Title

        return _converterView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.groups.get(groupPosition).itemCount
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return this.groups.get(groupPosition).itemClass!!.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val _item = this.groups.get(groupPosition).items.get(childPosition)
        val todo = _item.Title
        val description = _item.Description
        var _converterView = convertView
        if (_converterView == null) {
            val layoutInflater = LayoutInflater.from(ctx)
            _converterView = layoutInflater.inflate(R.layout.item_layout, null)
        }
        val todoView = _converterView!!.findViewById<TextView>(R.id.text)
        val descriptionView = _converterView.findViewById<TextView>(R.id.text2)
        todoView.text = todo
        descriptionView.text = description

        return _converterView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.groups.size
    }

}