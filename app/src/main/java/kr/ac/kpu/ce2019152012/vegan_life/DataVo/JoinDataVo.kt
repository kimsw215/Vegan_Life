package kr.ac.kpu.ce2019152012.vegan_life.DataVo

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class JoinDataVo(
    val profileImage: Uri ?= null,
    val nickname: String,
    val email: String,
    val password: String
    ): Parcelable