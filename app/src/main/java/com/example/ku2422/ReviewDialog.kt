package com.example.ku2422

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar

class ReviewDialog(context: Context) {
    val dialog = Dialog(context)
    lateinit var OnClickListener : ClickListener

    interface ClickListener{
        fun ClickBtn(menu : String, price : Int, review : String, rating : Int)
    }

    fun clickAdd(listener : ClickListener){
        OnClickListener = listener
    }
    fun showDlg(){
        dialog.setContentView(R.layout.dialog_review)
        dialog.setCancelable(false)
        var editMenu = dialog.findViewById<EditText>(R.id.edit_menu)
        var editPrice = dialog.findViewById<EditText>(R.id.edit_price)
        var editReview = dialog.findViewById<EditText>(R.id.edit_review)
        var rating_Num = dialog.findViewById<RatingBar>(R.id.rating_Num)
        var addBtn = dialog.findViewById<Button>(R.id.btn_add_review)
        var cancelBtn = dialog.findViewById<Button>(R.id.btn_cancel)

        addBtn.setOnClickListener {
            if(editPrice.text.toString() != "" && editMenu.text.toString() != ""){
                Log.i("test",editPrice.text.toString()+" "+editMenu.text.toString())
                OnClickListener.ClickBtn(editMenu.text.toString(),editPrice.text.toString().toInt(),editReview.text.toString(),rating_Num.rating.toInt())
                }
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }
}