package com.example.nestedgridexpandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nestedgridexpandable.databinding.ParentItemBinding

class ParentAdapter(private val parentItemList: List<ParentItem>) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {


    inner class ParentViewHolder(private val binding: ParentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun setChildRecyclerView(childItemList: List<ChildItem>) {
            binding.recyclerCard.visibility = View.VISIBLE
            val adapter = ChildAdapter(childItemList)
            binding.childRecyclerView.adapter = adapter

        }

        private fun isAnyItemOpened(position: Int) {

            if (position != RecyclerView.NO_POSITION){

                var whichItemOpened : Int? = null
                val temp = parentItemList.indexOfFirst {
                    it.parentContent1.isOpen or it.parentContent2.isOpen
                }

                if (temp >= 0 && temp != position) {

                    if (parentItemList[temp].parentContent1.isOpen){
                        parentItemList[temp].parentContent1.isOpen = false
                        whichItemOpened = 0
                    }else if (parentItemList[temp].parentContent2.isOpen){
                        parentItemList[temp].parentContent2.isOpen = false
                        whichItemOpened = 1
                    }
                    notifyItemChanged(temp, whichItemOpened)
                }
            }


        }


        fun closeExpandedViews(whichItemOpened : Int) {
            val upAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.up)

            when(whichItemOpened){

                0 -> {
                    binding.card1.startAnimation(upAnim)
                }
                1 -> {
                    binding.card2.startAnimation(upAnim)
                }
            }


            binding.recyclerCard.visibility = View.GONE

        }

        init {
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            binding.card1.setOnClickListener {

                val upAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.up)
                val downAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.down)

                val parentContent = parentItemList[adapterPosition].parentContent1
                isAnyItemOpened(adapterPosition)

                parentContent.isOpen =
                    !parentContent.isOpen

                if (parentContent.isOpen) {
                    binding.card1.startAnimation(downAnim)
                    binding.recyclerCard.startAnimation(downAnim)
                } else {
                    binding.card1.startAnimation(upAnim)
                }

                if (parentItemList[adapterPosition].parentContent2.isOpen) {
                    binding.card2.startAnimation(upAnim)
                    parentItemList[adapterPosition].parentContent2.isOpen = false

                }
                notifyItemChanged(adapterPosition, Unit)

            }

            binding.card2.setOnClickListener {

                val upAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.up)
                val downAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.down)

                val parentContent = parentItemList[adapterPosition].parentContent2
                isAnyItemOpened(adapterPosition)

                parentContent.isOpen =
                    !parentContent.isOpen

                if (parentContent.isOpen) {
                    binding.card2.startAnimation(downAnim)
                    binding.recyclerCard.startAnimation(downAnim)

                } else {
                    binding.card2.startAnimation(upAnim)
                }

                if (parentItemList[adapterPosition].parentContent1.isOpen) {
                    binding.card1.startAnimation(upAnim)
                    parentItemList[adapterPosition].parentContent1.isOpen = false

                }

                notifyItemChanged(adapterPosition, Unit)

            }
        }

        fun bind(parentItem: ParentItem) {

            binding.parentTv.text = parentItem.parentContent1.title
            binding.parentIv.setImageResource(parentItem.parentContent1.image)

            binding.parent2Tv.text = parentItem.parentContent2.title
            binding.parent2Iv.setImageResource(parentItem.parentContent2.image)


            when (true) {

                parentItem.parentContent1.isOpen -> {
                    setChildRecyclerView(parentItem.parentContent1.childItemList)
                }

                parentItem.parentContent2.isOpen -> {
                    setChildRecyclerView(parentItem.parentContent2.childItemList)

                }

                else -> {
                    binding.recyclerCard.visibility = View.GONE

                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: ParentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && ((payloads[0] == 0) or (payloads[0] == 1))) {
            holder.closeExpandedViews(payloads[0] as Int)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ParentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return parentItemList.size
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(parentItemList[position])
    }
}