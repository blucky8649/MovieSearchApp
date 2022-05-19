package com.example.moviesearchapp.util

fun removeTags(title: String?): String? {
    var new_title = title?.replace("<b>", "")
    new_title = new_title?.replace("</b>", "")
    return new_title
}

fun removeVerticalBarFromText(text: String): String {
    if (text.isEmpty()) return ""
    var newText = text.replace("|", ", ")
    return newText.substring(0, newText.length - 2)
}