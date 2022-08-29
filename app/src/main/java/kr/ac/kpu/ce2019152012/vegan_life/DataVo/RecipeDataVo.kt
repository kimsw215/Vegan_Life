package kr.ac.kpu.ce2019152012.vegan_life.DataVo


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeDataVo(
    val recipename: String?,
    val recipephoto: Int,
    val ingredient: String?,
    val how: String?
) : Parcelable
