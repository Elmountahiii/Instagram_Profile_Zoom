package com.redgunner.instagramzommy.models.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocalAccountList")
data class UserX(
    val full_name: String,
    val has_anonymous_profile_picture: Boolean,
    val is_private: Boolean,
    val is_verified: Boolean,
    val profile_pic_url: String,
    @PrimaryKey()
    val username: String,
    val is_favorite:Boolean=false
){


}