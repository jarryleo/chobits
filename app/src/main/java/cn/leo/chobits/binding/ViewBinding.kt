package cn.leo.chobits.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leo.chobits.R
import cn.leo.chobits.view.StatusPager
import cn.leo.paging_ktx.SimplePagingAdapter
import cn.leo.paging_ktx.State
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : DataBinding 适配器
 */

@BindingAdapter("bindAdapter")
fun bindingAdapter(recyclerView: RecyclerView, adapter: SimplePagingAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("bindLinearLayoutManager")
fun bindingLayoutManager(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
}

/**
 * 绑定下拉刷新的状态
 * recyclerView 不能是 SmartRefreshLayout的直接子孩子
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
    adapter.setOnRefreshStateListener {
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
    adapter.setOnLoadMoreStateListener {
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