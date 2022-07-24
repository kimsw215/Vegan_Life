package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinDataVo(
    val nickname: String = "0",
    val email: String,
    val password: String,
    val password2: String
    ): Parcelable