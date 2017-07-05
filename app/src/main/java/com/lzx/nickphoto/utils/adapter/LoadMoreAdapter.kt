package com.lzx.nickphoto.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.lzx.nickphoto.R
import com.lzx.nickphoto.widget.CircleProgressView

/**
 * Created by lzx on 2017/7/5.
 */
abstract class LoadMoreAdapter<T> (var context: Context) : RecyclerView.Adapter<BaseViewHolder>() {

    /**
     * 是否显示加载更多
     */
    var showCanLoadMore: Boolean = true
    var mDataList: ArrayList<T> = ArrayList()
    var mRecyclerView: RecyclerView? = null
    var onScrollListener: RecyclerOnScrollListener? = null
    var mNotifyObserver: NotifyObserver? = null
    var mOnFootViewClickListener: OnFootViewClickListener? = null
    var mOnLoadMoreListener: OnLoadMoreListener? = null
    var mFootViewHolder: FootViewHolder? = null
    val TYPE_FOOTVIEW: Int = 100

    fun setDataList(dataList: ArrayList<T>) {
        mDataList = dataList
        if (mDataList.size <= 3) {
            setShowLoadMore(false)
        } else {
            setShowLoadMore(true)
        }
    }

    fun getDataList(): List<T> {
        return mDataList
    }

    /**
     * 设置是否需要显示加载更多
     */
    fun setShowLoadMore(showLoadMore: Boolean) {
        this.showCanLoadMore = showLoadMore
        setCanLoading(showLoadMore)
    }

    /**
     * 是否需要加载更多
     */
    fun setCanLoading(loading: Boolean) {
        if (onScrollListener != null) {
            onScrollListener?.setCanLoading(loading)
            onScrollListener?.setLoading(!loading)
        }
    }

    /**
     * 显示-已加载全部数据
     */
    fun showLoadAllDataUI() {
        if (mFootViewHolder != null) {
            mFootViewHolder?.setClickable(false)
            mFootViewHolder?.showTextOnly("已全部加载")
            setCanLoading(false)
        }
    }

    fun showProgress() {
        if (mFootViewHolder != null) {
            mFootViewHolder?.setClickable(true)
            mFootViewHolder?.showProgress()
            setCanLoading(true)
        }
    }

    fun clearAllData() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    fun setOnFootViewClickListener(onFootViewClickListener: OnFootViewClickListener) {
        mOnFootViewClickListener = onFootViewClickListener
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener
    }

    inner class FootViewHolder(itemView: View, context: Context) : BaseViewHolder(itemView, context, false) {
        internal var mRootLayout: LinearLayout = find(R.id.root_layout)
        internal var mProgressView: CircleProgressView = find(R.id.load_pro)
        internal var mLoadText: TextView = find(R.id.loading_text)

        fun setClickable(clickable: Boolean) {
            mRootLayout.isClickable = clickable
        }

        fun setLoadText(text: String) {
            mLoadText.text = text
        }

        fun showProgress() {
            mProgressView.visibility = View.VISIBLE
            mLoadText.text = "正在加载..."
        }

        fun showClickText() {
            mProgressView.visibility = View.GONE
            mLoadText.text = "点击重新加载"
        }

        fun showTextOnly(text: String) {
            mProgressView.visibility = View.GONE
            setLoadText(text)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        onScrollListener = object : RecyclerOnScrollListener(mRecyclerView!!) {
            override fun onLoadMore(currentPage: Int) {
                if (mOnLoadMoreListener != null)
                    mOnLoadMoreListener?.onLoadMore()
            }
        }
        mRecyclerView?.addOnScrollListener(onScrollListener)
        onScrollListener?.setLoading(false)
        mNotifyObserver = NotifyObserver()
        registerAdapterDataObserver(mNotifyObserver)

        if (mDataList.size <= 3) {
            setShowLoadMore(false)
        } else {
            setShowLoadMore(true)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (onScrollListener != null) {
            mRecyclerView?.removeOnScrollListener(onScrollListener)
        }
        if (mNotifyObserver != null) {
            unregisterAdapterDataObserver(mNotifyObserver)
        }
        onScrollListener = null
        mNotifyObserver = null
        mRecyclerView = null
    }

    override fun getItemCount(): Int {
        return if (showCanLoadMore) if (mDataList != null) mDataList.size + 1 else 0 else if (mDataList != null) mDataList.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (showCanLoadMore) {
            if (position == itemCount - 1) {
                return TYPE_FOOTVIEW
            } else {
                return getViewType(position)
            }
        }
        return getViewType(position)
    }

    protected abstract fun getViewType(position: Int): Int

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        if (getItemViewType(position) == TYPE_FOOTVIEW) {
            mFootViewHolder?.mRootLayout?.setOnClickListener { v ->
                if (mOnFootViewClickListener != null) {
                    mOnFootViewClickListener?.onFootViewClick()
                }
            }
        } else {
            BindViewHolder(holder!!, position)
        }
    }

    protected abstract fun BindViewHolder(holder: BaseViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        if (viewType == TYPE_FOOTVIEW) {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_footview, parent, false)
            mFootViewHolder = FootViewHolder(view, context)
            return mFootViewHolder!!
        } else {
            return onCreateBaseViewHolder(parent!!, viewType)
        }
    }

    abstract fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    interface OnFootViewClickListener {
        fun onFootViewClick()
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    /**
     * 数据变化时回调
     */
    inner class NotifyObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            if (onScrollListener != null) {
                onScrollListener?.setLoading(false)
            }
            if (mDataList.size <= 3) {
                setShowLoadMore(false)
            } else {
                setShowLoadMore(true)
            }
        }
    }


}