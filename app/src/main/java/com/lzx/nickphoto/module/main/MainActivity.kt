package com.lzx.nickphoto.module.main

import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.lzx.nickphoto.R
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.callback.PhotoDiffCallBack
import com.lzx.nickphoto.common.RxBaseActivity
import com.lzx.nickphoto.module.main.adapter.PhotoAdapter
import com.lzx.nickphoto.module.main.contract.PhotoContract
import com.lzx.nickphoto.module.main.model.PhotoModel
import com.lzx.nickphoto.module.main.presenter.PhotoPresenter
import com.lzx.nickphoto.utils.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxBaseActivity(), PhotoContract.IPhotoView {

    //api文档：https://unsplash.com/documentation#user-authentication
    //Application ID： 6c18f0d4f3c1fcd37b2388ec2c543f272777584f8ed62a4bcd0fba0fe904c6f8
    //Secret： a36ad9805b0f97f3e3d553763e957c0fcc4abf0026d8602314fbea844992a6f8
    //Callback URLs： https://lzx-images.com/callback (Authorize)

    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    lateinit var mPresenter: PhotoContract.IPhotoPresenter
    lateinit var mAdapter: PhotoAdapter
    lateinit var mPhotoList: ArrayList<PhotoInfo>

    override fun init() {
        mPresenter = PhotoPresenter(this)
        //SwipeRefreshLayout
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener({
            mPresenter.getAllPhotoList(bindToLifecycle(), false)
        })
        //RecycleView
        recycle_view.setHasFixedSize(true)
        recycle_view.layoutManager = LinearLayoutManager(this)
        mAdapter = PhotoAdapter(this)
        recycle_view.adapter = mAdapter
        mAdapter.mOnLoadMoreListener = object : LoadMoreAdapter.OnLoadMoreListener {
            override fun onLoadMore() {
                mPresenter.loadMorePhotoList(bindToLifecycle())
            }
        }
        mPresenter.getAllPhotoList(bindToLifecycle(), true)
    }

    override fun showPro(isShow: Boolean) {
        if (isShow) {
            load_pro.visibility = View.VISIBLE
        } else {
            load_pro.visibility = View.GONE
        }
    }

    override fun OnGetPhotoSuccess(result: ArrayList<PhotoInfo>) {
        mPhotoList = result
        val callback: PhotoDiffCallBack = PhotoDiffCallBack(mAdapter.mDataList, mPhotoList)
        val diffResult = DiffUtil.calculateDiff(callback)
        mAdapter.setDataList(mPhotoList)
        diffResult.dispatchUpdatesTo(mAdapter)
        mAdapter.setShowLoadMore(result.size >= PhotoModel.per_page)
        recycle_view.scrollToPosition(0)
        refresh_layout.isRefreshing = false
    }

    override fun loadMoreSuccess(result: ArrayList<PhotoInfo>) {
        mPhotoList.addAll(result)
        val callback: PhotoDiffCallBack = PhotoDiffCallBack(mAdapter.mDataList, mPhotoList)
        val diffResult = DiffUtil.calculateDiff(callback, true)
        mAdapter.setDataList(mPhotoList)
        diffResult.dispatchUpdatesTo(mAdapter)
        recycle_view.scrollToPosition(mAdapter.itemCount - PhotoModel.per_page - 2)
    }

    override fun loadMoreError(msg: String) {
        mAdapter.setCanLoading(false)
        Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show()
    }

    override fun loadFinishAllData() {
        if (mPhotoList.size >= PhotoModel.per_page) {
            mAdapter.setCanLoading(false)
            mAdapter.showLoadAllDataUI()
        } else {
            mAdapter.setShowLoadMore(false)
        }
    }

    override fun OnError(msg: String) {
        Toast.makeText(this, "msg = " + msg, Toast.LENGTH_SHORT).show()
    }
}


