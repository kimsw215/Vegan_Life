package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarFoodDataVo(
    val foodname: String,
    val foodphoto: Uri? = null,
    val cal: Int,
    val protein: Int,
    val fat: Int,
    val time: String,
    val timetype: Int
    ):Parcelable
