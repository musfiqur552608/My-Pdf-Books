package com.freedu.mypdfbooks.view.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freedu.mypdfbooks.R
import com.github.chrisbanes.photoview.PhotoView

class PdfPagerAdapter(
    private val pdfRenderer: PdfRenderer
) : RecyclerView.Adapter<PdfPagerAdapter.PageViewHolder>() {

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoView: PhotoView = view.findViewById(R.id.photoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf_page, parent, false)
        return PageViewHolder(view)
    }

    override fun getItemCount(): Int = pdfRenderer.pageCount

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        pdfRenderer.openPage(position).use { page ->
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            if (isDarkMode(holder.itemView.context)) {
                holder.photoView.setImageBitmap(invertBitmapColors(bitmap))
            } else {
                holder.photoView.setImageBitmap(bitmap)
            }
        }
    }
    fun isDarkMode(context: Context): Boolean {
        return (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    fun invertBitmapColors(original: Bitmap): Bitmap {
        val inverted = Bitmap.createBitmap(original.width, original.height, original.config!!)
        val canvas = Canvas(inverted)
        val paint = Paint()
        val colorMatrixInvert = ColorMatrix(
            floatArrayOf(
                -1f,  0f,  0f,  0f, 255f,
                0f, -1f,  0f,  0f, 255f,
                0f,  0f, -1f,  0f, 255f,
                0f,  0f,  0f,  1f,   0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(colorMatrixInvert)
        canvas.drawBitmap(original, 0f, 0f, paint)
        return inverted
    }
}