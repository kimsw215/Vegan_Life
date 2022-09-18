package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinInfoDataVo(
    val email: String,
    val password: String,
    val nickname: String,
    val image: Uri?,
    val height: Int,
    val weight: Int,
    val age: Int,
    val basiccal: Double,
    val vegantype: Int,
    val sex: Int
):Parcelable