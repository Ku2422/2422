package com.example.ku2422

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar

class ReviewDialog(context: Context) {
    val dialog = Dialog(context)
    lateinit var OnClickListener : ClickListener

    interface ClickListener{
        fun ClickBtn(price : String, review : String, rating : Int)
    }

    fun clickAdd(listener : ClickListener){
        OnClickListener = listener
    }
    fun showDlg(){
        dialog.setContentView(R.layout.dialog_review)
        dialog.setCancelable(false)

        var editPrice = dialog.findViewById<EditText>(R.id.edit_price)
        var editReview = dialog.findViewById<EditText>(R.id.edit_review)
        var rating_Num = dialog.findViewById<RatingBar>(R.id.rating_Num)
        var addBtn = dialog.findViewById<Button>(R.id.btn_add_review)
        var cancelBtn = dialog.findViewById<Button>(R.id.btn_cancel)

        addBtn.setOnClickListener {
            OnClickListener.ClickBtn(editPrice.text.toString(),editReview.text.toString(),rating_Num.rating.toInt())
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }
}