package com.metehanbolat.thinqeek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.makeramen.roundedimageview.RoundedImageView
import com.metehanbolat.thinqeek.databinding.SlideItemContainerBinding
import com.metehanbolat.thinqeek.model.SliderItem
import com.squareup.picasso.Picasso

class SliderAdapter internal constructor(sliderItems: MutableList<SliderItem>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val sliderItems: List<SliderItem>

    init {
        this.sliderItems = sliderItems
    }

    class SliderViewHolder(val binding: SlideItemContainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        Picasso.get().load(sliderItems[position].image).into(holder.binding.imageSlide)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }
}