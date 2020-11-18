package cn.leo.chobits.utils

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.leo.paging_ktx.PagingDataAdapterKtx

/**
 * @author : ling luo
 * @date : 2020/11/18
 * @description : DataBinding 帮助类
 */


/**
 * RecyclerView 列表条目绑定，PagingAdapter库定制拓展
 */
const val BINDING_KEY = "DataBinding_Key"
inline fun <reified T : ViewDataBinding> PagingDataAdapterKtx.ItemHelper.binding(): T? {
    var dataBinding = getTag(BINDING_KEY) as? T
    if (dataBinding == null) {
        dataBinding = DataBindingUtil.bind<T>(itemView)?.apply {
            setTag(BINDING_KEY, this)
        }
    }
    return dataBinding
}

/**
 * activity 绑定
 */
inline fun <reified T : ViewDataBinding> Activity.binding(@LayoutRes resId: Int): Lazy<T> =
    lazy { DataBindingUtil.setContentView<T>(this, resId) }