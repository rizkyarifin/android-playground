package com.sample.playground.ui.main

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.playground.data.model.Article
import com.sample.playground.utils.view.State

class MainAdapter(private val retry: () -> Unit)
    : PagedListAdapter<Article, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = if (viewType == DATA_VIEW_TYPE) NewsViewHolder.create(parent) else ListFooterViewHolder.create(retry, parent)

    override fun getItemCount(): Int = super.getItemCount() + if (hasFooter()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            getItem(position)?.let { (holder as NewsViewHolder).bind(it) }
        else (holder as ListFooterViewHolder).bind(state)
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}