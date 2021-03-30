package com.redgunner.instagramzommy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.models.search.User
import com.redgunner.instagramzommy.models.search.UserX
import kotlinx.android.synthetic.main.search_item_layout.view.*

class SearchInstagramUserListAdapter(val accountClick:(account: UserX)->Unit) :
    ListAdapter<User, SearchInstagramUserListAdapter.SearchInstagramUserViewHolder>(
        InstagramListComparator()
    ) {


    inner class SearchInstagramUserViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val image = itemView.ProfileImageVH
        private val userName = itemView.UserNameVH
        private val fullName = itemView.FullNameVH
        private val  verified =itemView.verifiedVH

        fun bind(instagramUser: UserX) {
            Glide.with(context)
                .load(instagramUser.profile_pic_url).into(image)

            userName.text = instagramUser.username
            fullName.text=instagramUser.full_name

            if (!instagramUser.is_verified){
                verified.visibility=View.INVISIBLE
            }



            image.setOnClickListener {
             accountClick(getItem(adapterPosition).user)
            }
            userName.setOnClickListener {
                accountClick(getItem(adapterPosition).user)

            }
            fullName.setOnClickListener {
                accountClick(getItem(adapterPosition).user)

            }
        }


    }


    class InstagramListComparator() : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return false
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchInstagramUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item_layout, parent, false)

        return SearchInstagramUserViewHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: SearchInstagramUserViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.user)
    }


}