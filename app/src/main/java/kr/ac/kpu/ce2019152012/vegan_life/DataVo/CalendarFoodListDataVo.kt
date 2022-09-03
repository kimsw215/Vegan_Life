package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarFoodListDataVo(
    val foodname : String?,
    val kcal : String?,
    val delete : String
    ):Parcelable
