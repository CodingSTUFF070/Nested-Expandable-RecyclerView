package com.example.nestedgridexpandable

data class ParentItem(
    val parentContent1: ParentContent,
    val parentContent2: ParentContent
)

data class ParentContent(
    val image : Int ,
    val title : String,
    val childItemList : List<ChildItem>,
    var isOpen : Boolean = false
)
data class ChildItem(val title : String , val image : Int)
