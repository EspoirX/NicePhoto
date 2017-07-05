package com.lzx.nickphoto.utils.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by lzx on 2017/7/5.
 */
abstract class RecyclerOnScrollListener(var recyclerView: RecyclerView) : RecyclerView.OnScrollListener() {
    var previousTotal: Int = 0
    var isloading: Boolean = true
    var currentPage: Int = 1
    var shouldLoading: Boolean = true

    var mHelper: RecyclerViewPositionHelper = RecyclerViewPositionHelper(recyclerView)

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoading) {
            if (isSlideToBottom(recyclerView)) {
                currentPage++
                onLoadMore(currentPage)
                isloading = true
            }
        }
    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = manager.itemCount
            val lastCompletelyVisiableItemPosition = manager.findLastCompletelyVisibleItemPosition()

            if (isloading) {
                if (totalItemCount > previousTotal) {
                    isloading = false
                    previousTotal = totalItemCount
                }
            }
            return !isloading && visibleItemCount > 0 && lastCompletelyVisiableItemPosition >= totalItemCount - 1
        }
        return false
    }

    fun setCanLoading(loading: Boolean) {
        shouldLoading = loading
        if (shouldLoading)
            previousTotal = mHelper.getItemCount()
    }

    fun setLoading(loading: Boolean) {
        this.isloading = loading
        if (loading)
            previousTotal = mHelper.getItemCount()
    }

    abstract fun onLoadMore(currentPage: Int)


}