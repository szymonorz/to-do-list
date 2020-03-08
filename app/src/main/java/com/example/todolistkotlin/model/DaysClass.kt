package com.example.todolistkotlin.model

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


data class DaysClass(val date: String?, val itemClass: ArrayList<ItemClass>) :
    ExpandableGroup<ItemClass>(date, itemClass)
