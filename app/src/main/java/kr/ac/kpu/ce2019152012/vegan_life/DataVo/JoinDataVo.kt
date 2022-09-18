package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinDataVo(
    val profileImage: Int ?= null,
    val nickname: String,
    val email: String,
    val password: String
    ): Parcelable