package com.lzx.nickphoto.module.main

import android.Manifest
import android.os.Bundle
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
import com.lzx.nickphoto.utils.CommonUtil
import com.lzx.nickphoto.utils.FileUtil
import com.lzx.nickphoto.utils.adapter.LoadMoreAdapter
import com.lzx.nickphoto.widget.DownloadDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxBaseActivity(), PhotoContract.IPhotoView {

    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    lateinit var mPresenter: PhotoContract.IPhotoPresenter
    lateinit var mAdapter: PhotoAdapter
    lateinit var mPhotoList: ArrayList<PhotoInfo>
    lateinit var mRxPermissions: RxPermissions

    override fun init() {
        mPresenter = PhotoPresenter(this)
        mRxPermissions = RxPermissions(this)

        //SwipeRefreshLayout
        refresh_layout.setColorSchemeResources(R.color.colorPrimaryDark)
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
        mAdapter.setOnDownloadClickListener(object : PhotoAdapter.OnDownloadClickListener {
            override fun download(view: View, downloadUrl: String) {
                downloadImage(view, downloadUrl)
            }
        })
        mPresenter.getAllPhotoList(bindToLifecycle(), true)
    }

    fun downloadImage(view: View, downloadUrl: String) {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter { aBoolean -> aBoolean }
                .filter {
                    if (FileUtil.isExistsImage(downloadUrl)) {
                        CommonUtil.showSnackBar(this@MainActivity, view,
                                "文件已经下载了哦,到 ../NickPhoto/download 查看吧")
                        return@filter false
                    }
                    return@filter true
                }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val downloadDialog: DownloadDialog = DownloadDialog()
                    val bundle: Bundle = Bundle()
                    bundle.putString("downloadUrl", downloadUrl)
                    downloadDialog.arguments = bundle
                    downloadDialog.show(supportFragmentManager, "DownloadDialog")
                }, {
                    CommonUtil.toast(this@MainActivity, "下载失败,请重试")
                })
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


