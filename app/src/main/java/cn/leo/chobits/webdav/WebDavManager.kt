package cn.leo.chobits.webdav

import com.thegrizzlylabs.sardineandroid.DavResource
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine

/**
 * @author : ling luo
 * @date : 2021/3/25
 * @description : WebDavManager
 */
class WebDavManager private constructor() {

    companion object {
        private val sardine = OkHttpSardine()
        fun getInstance(username: String, password: String): WebDavManager {
            return WebDavManager().login(username, password)
        }
    }

    /**
     * 账号密码
     */
    private fun login(username: String, password: String): WebDavManager {
        sardine.setCredentials(username, password)
        return this
    }

    /**
     * 读取服务器路径下的文件列表
     * @param url  范例 https://dav.jianguoyun.com/dav/davTest
     */
    suspend fun fileList(url: String): List<DavResource> {
        return try {
            sardine.list(url)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 数据写入远程文件
     * @param url  范例 https://dav.jianguoyun.com/dav/davTest/test.txt
     * 由服务器地址 +  /文件名组成
     */
    suspend fun put(url: String, data: ByteArray): Boolean {
        return try {
            sardine.put(url, data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 读取文件文本内容
     * @param url  范例 https://dav.jianguoyun.com/dav/davTest/test.txt
     */
    suspend fun get(url: String): String {
        return try {
            sardine.get(url).readBytes().decodeToString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}