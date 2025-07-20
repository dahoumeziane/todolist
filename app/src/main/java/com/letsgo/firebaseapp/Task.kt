package com.letsgo.firebaseapp

data class Task(
    val title:String = "",
    val description : String = "",
    val category : String = "",
    val done : Boolean = false,
    val id : String = "",
    val priority: String?=null
){

}
