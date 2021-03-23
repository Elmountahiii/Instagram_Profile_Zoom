package com.redgunner.instagramzommy.models.profile

data class AccountResponse(
    val edge_follow: EdgeFollow,
    val edge_followed_by: EdgeFollowedBy,
    val full_name: String,
    val is_private: Boolean,
    val is_verified: Boolean,
    val profile_pic_url: String,
    val profile_pic_url_hd: String,
    val username: String
)