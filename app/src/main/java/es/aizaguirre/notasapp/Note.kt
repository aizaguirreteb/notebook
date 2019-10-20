package es.aizaguirre.notasapp

import java.util.*

data class Note (var title : String, var text: String, var date : String){

    /**
     * Since it's only a simple expression we don't need to use the whole function structure
     */
    override fun toString(): String = "$title:        ${date.substring(0..10)}"
}