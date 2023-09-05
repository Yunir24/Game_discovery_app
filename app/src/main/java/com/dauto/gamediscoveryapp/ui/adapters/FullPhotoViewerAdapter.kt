package com.dauto.gamediscoveryapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.dauto.gamediscoveryapp.R


class FullPhotoViewerAdapter(
    private val context: Context, private var imageList: List<String>
): PagerAdapter() {
    override fun getCount(): Int = imageList.size


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.full_photo_item, null)
        val imageItem = view.findViewById<ImageView>(R.id.imageItem)
        imageList[position].let{
            Glide.with(context).load(it).into(imageItem)
        }
        val viewPager = container as ViewPager
        viewPager.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}