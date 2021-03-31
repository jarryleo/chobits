package cn.leo.chobits.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.leo.chobits.R
import cn.leo.chobits.ext.singleClick
import cn.leo.chobits.view.StatusPager
import cn.leo.paging_ktx.adapter.DifferData
import cn.leo.paging_ktx.adapter.State
import cn.leo.paging_ktx.simple.SimplePager
import cn.leo.paging_ktx.simple.SimplePagingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : DataBinding 适配器
 */


@BindingAdapter(
    "bindAdapter",
    "bindPager",
    "bindItemClick",
    "bindItemChildClick",
    requireAll = false
)
fun <T : DifferData> bindingAdapter(
    recyclerView: RecyclerView,
    adapter: SimplePagingAdapter?,
    pager: SimplePager<*, T>?,
    itemClickListener: OnItemClickListener?,
    itemChildClickListener: OnItemClickListener?
) {
    if (adapter == null) return
    recyclerView.adapter = adapter
    if (pager == null) return
    adapter.setPager(pager)
    //条目点击
    if (itemClickListener != null) {
        adapter.setOnItemClickListener { _, v, position ->
            itemClickListener.onItemClick(adapter, v, position)
        }
    }
    //条目内view点击
    if (itemChildClickListener != null) {
        adapter.setOnItemChildClickListener { _, v, position ->
            itemChildClickListener.onItemClick(adapter, v, position)
        }
    }
}

@BindingAdapter("bindLayoutManager", "bindOrientation", "bindSpanCount", requireAll = false)
fun bindLayoutManager(
    recyclerView: RecyclerView,
    layoutManager: String?,
    orientation: Int?,
    spanCount: Int?
) {
    if (layoutManager == null) return
    when (layoutManager) {
        LinearLayoutManager::class.java.simpleName -> {
            recyclerView.layoutManager =
                LinearLayoutManager(
                    recyclerView.context,
                    orientation ?: LinearLayoutManager.VERTICAL,
                    false
                )
        }
        GridLayoutManager::class.java.simpleName -> {
            recyclerView.layoutManager = GridLayoutManager(
                recyclerView.context,
                spanCount ?: 1,
                orientation ?: GridLayoutManager.VERTICAL,
                false
            )
        }
        StaggeredGridLayoutManager::class.java.simpleName -> {
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(
                    spanCount ?: 2,
                    StaggeredGridLayoutManager.VERTICAL
                )
        }
        else -> {
            recyclerView.layoutManager =
                LinearLayoutManager(
                    recyclerView.context,
                    orientation ?: LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

}

/**
 * View点击事件,加入点击事件防重
 */
@BindingAdapter("android:bindClick")
fun bindingClick(view: View, onClickListener: View.OnClickListener) {
    view.singleClick {
        onClickListener.onClick(it)
    }
}

/**
 * 绑定下拉刷新的状态
 */
@BindingAdapter("bindState")
fun bindingState(
    smartRefreshLayout: SmartRefreshLayout,
    adapter: SimplePagingAdapter
) {
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