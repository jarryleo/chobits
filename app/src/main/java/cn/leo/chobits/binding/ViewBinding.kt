package cn.leo.chobits.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leo.chobits.R
import cn.leo.chobits.activity.NoteActivity
import cn.leo.chobits.db.NoteEntity
import cn.leo.chobits.ext.singleClick
import cn.leo.chobits.view.StatusPager
import cn.leo.paging_ktx.adapter.DifferData
import cn.leo.paging_ktx.adapter.State
import cn.leo.paging_ktx.simple.SimplePager
import cn.leo.paging_ktx.simple.SimplePagingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : DataBinding 适配器
 */

@BindingAdapter("bindAdapter", "bindData")
fun <T : DifferData> bindingAdapter(
    recyclerView: RecyclerView,
    adapter: SimplePagingAdapter,
    data: SimplePager<*, T>?
) {
    data?.let {
        recyclerView.adapter = adapter
        adapter.setPager(data)
    }
}

@BindingAdapter("bindLinearLayoutManager")
fun bindingLayoutManager(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
}

/**
 * 列表条目点击事件跳转
 */
@ExperimentalCoroutinesApi
@FlowPreview
@BindingAdapter("bindItemClick")
fun bindingItemClick(recyclerView: RecyclerView, adapter: SimplePagingAdapter) {
    adapter.setOnItemClickListener { _, _, position ->
        val data = adapter.getData(position) as? NoteEntity
        data?.let { NoteActivity.jumpActivity(recyclerView.context, it) }
    }
}

/**
 * View点击事件
 */
@BindingAdapter("bindClick")
fun bindingClick(view: View, clickHandler: ClickHandler) {
    view.singleClick {
        clickHandler.onClick(it)
    }
}

/**
 * 绑定下拉刷新的状态
 */
@BindingAdapter("bindState")
fun bindingState(smartRefreshLayout: SmartRefreshLayout, adapter: SimplePagingAdapter) {
    var statePager = smartRefreshLayout.getTag(R.id.status_pager_id) as? StatusPager
    if (statePager == null) {
        statePager = StatusPager.builder(smartRefreshLayout)
            .emptyViewLayout(R.layout.state_empty)
            .loadingViewLayout(R.layout.state_loading)
            .errorViewLayout(R.layout.state_error)
            .addRetryButtonId(R.id.btn_retry)
            .setRetryClickListener { _, _ ->
                adapter.refresh()
            }
            .build()
        smartRefreshLayout.setTag(R.id.status_pager_id, statePager)
    }
    //设置下拉刷新
    smartRefreshLayout.setOnRefreshListener {
        adapter.refresh()
    }
    //上拉加载更多
    smartRefreshLayout.setOnLoadMoreListener {
        adapter.retry()
    }
    //下拉刷新状态
    adapter.addOnRefreshStateListener {
        when (it) {
            is State.Loading -> {
                //如果是手动下拉刷新，则不展示loading页
                if (smartRefreshLayout.state != RefreshState.Refreshing) {
                    statePager.showLoading()
                }
                smartRefreshLayout.resetNoMoreData()
            }
            is State.Success -> {
                if (adapter.itemCount == 0) {
                    statePager.showEmpty()
                } else {
                    statePager.showContent()
                }
                smartRefreshLayout.finishRefresh(true)
                smartRefreshLayout.setNoMoreData(it.noMoreData)
            }
            is State.Error -> {
                statePager.showError()
                smartRefreshLayout.finishRefresh(false)
            }
        }
    }
    //加载更多状态
    adapter.addOnLoadMoreStateListener {
        when (it) {
            is State.Loading -> {
                smartRefreshLayout.resetNoMoreData()
            }
            is State.Success -> {
                if (it.noMoreData) {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    smartRefreshLayout.finishLoadMore(true)
                }
            }
            is State.Error -> {
                smartRefreshLayout.finishLoadMore(false)
            }
        }
    }
}