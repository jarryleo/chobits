package cn.leo.chobits.utils

import java.math.BigDecimal

/**
 * @author : ling luo
 * @date : 2020/9/8
 * @description : 字符串格式化拓展
 */

/**
 * 转换字符串
 */
fun Any?.toStringNotNull(nullText: String = ""): String {
    if (this == null) {
        return nullText
    }
    return toString()
}


/**
 * 转换W（万）
 */
fun Long?.toW(placeholder: String = "0"): String {
    if (this == null || this == 0L) {
        return placeholder
    }
    return toShortCount(9999, "w")
}

/**
 * 转换K（千）
 */
fun Long?.toK(placeholder: String = "0"): String {
    if (this == null || this == 0L) {
        return placeholder
    }
    return toShortCount(999, "k")
}


/**
 * 转换简单数量
 * @param num 大小 如：9，999，99999
 * @param suffix 后缀 如：千，万，w
 * @param scale 保留几位小数
 */
fun Long.toShortCount(num: Long = 9999, suffix: String = "万", scale: Int = 1): String {
    var count = this.toString()
    if (this > num) {
        count = BigDecimal(this)
            .divide(BigDecimal(num + 1), scale, BigDecimal.ROUND_DOWN)
            .toString()
            .plus(suffix)
    }
    return count
}

/**
 * Double 转换成金额，保留2位小数
 */
fun Double.toMoney(): String {
    return BigDecimal(this)
        .divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_DOWN)
        .toString()
}

//转换成百分比
fun Long.toPercent(sum: Long): String {
    if (sum == 0L || this < 0) {
        return "0%"
    }
    if (this > sum) {
        return "100%"
    }
    return BigDecimal(this)
        .multiply(BigDecimal(100))
        .divide(BigDecimal(sum), 0, BigDecimal.ROUND_HALF_UP)
        .toString().plus("%")
}

fun Long.toPercentInt(sum: Long): Int {
    if (sum == 0L) {
        return 0
    }
    return BigDecimal(this)
        .multiply(BigDecimal(100))
        .divide(BigDecimal(sum), 0, BigDecimal.ROUND_HALF_UP)
        .toInt()
}

/**
 * 将秒转换成时分秒
 */
fun Long.toHHmmSS(showSecond: Boolean = true): String {
    var time = ""
    val hours = this / (60 * 60)
    val minute = (this - (hours * 60 * 60)) / 60
    val second = this % 60
    if (hours < 10) {
        time += "0"
    }
    time += "$hours:"

    if (minute < 10) {
        time += "0"
    }
    time += "$minute"
    if (showSecond) {
        time += ":"
        if (second < 10) {
            time += "0"
        }
        time += "$second"
    }
    return time
}

/**
 * 小红点数字显示，1-99 显示数字，99以上显示99+ ，其余数字为空
 */
fun Int.getPopCountText(): String {
    return when {
        this < 1 -> {
            ""
        }
        this <= 99 -> {
            this.toString()
        }
        else -> {
            "99+"
        }
    }
}