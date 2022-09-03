package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarFoodDataVo(
    val timetype: Int?,
    val foodphoto: Uri? = null,
    val foodname: String,
    val day: String?,
    val kcal: Int?,
    val gml: Int?,
    val cal: Int?,
    val protein: Int?,
    val fat: Int?
    ):Parcelable
