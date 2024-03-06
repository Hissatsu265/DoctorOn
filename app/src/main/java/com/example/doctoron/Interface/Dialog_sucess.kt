package com.example.doctoron.Interface
import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView

class Dialog_sucess (context: Context, private val imageDrawable: Drawable) : AlertDialog(context){

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Thiết lập giao diện của dialog
            val imageView = ImageView(context)
            imageView.setImageDrawable(imageDrawable)

            // Đặt layout cho ImageView (có thể bạn cần tùy chỉnh kích thước, margins, ... tùy theo nhu cầu)
            val layoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            imageView.layoutParams = layoutParams

            // Thêm ImageView vào dialog
            setView(imageView)

            // Nút đóng dialog
            setButton(BUTTON_POSITIVE, "Đóng") { _, _ -> dismiss() }
    }
}