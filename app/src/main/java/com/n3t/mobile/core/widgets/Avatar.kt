package com.n3t.mobile.core.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.n3t.mobile.R
import com.n3t.mobile.utils.extensions.dpToPx

class Avatar(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    private var avatarUri: String? = null
    private var name: String? = null
    private var avatarSize: Int = 48.dpToPx()

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GofaAvatar)

        try {
            avatarSize = attributes.getDimensionPixelSize(R.styleable.GofaAvatar_size, 48.dpToPx())
            name = attributes.getString(R.styleable.GofaAvatar_title)
            avatarUri = attributes.getString(R.styleable.GofaAvatar_avatarUri)

            updateView()
        } finally {
            attributes.recycle()
        }
    }

    private fun updateView() {
        if (avatarUri != null) {
            val avatar = ImageView(context)
            Glide.with(context)
                .load(avatarUri)
                .into(avatar)
            avatar.layoutParams = LayoutParams(avatarSize, avatarSize)
            avatar.scaleType = ImageView.ScaleType.CENTER_CROP
            avatar.background = ContextCompat.getDrawable(context, R.drawable.bg_avatar)
            this.addView(avatar)
        } else if (name != null) {
            val avatar = TextView(context)
            avatar.text = name
            avatar.layoutParams = LayoutParams(avatarSize, avatarSize)
            avatar.gravity = Gravity.CENTER
            avatar.background = ContextCompat.getDrawable(context, R.drawable.bg_avatar)
            avatar.setTextColor(ContextCompat.getColor(context, R.color.white))
            avatar.textSize = 16f
            avatar.typeface = Typeface.DEFAULT_BOLD
            this.addView(avatar)
        } else {
            val avatar = ImageView(context)
            avatar.setImageResource(R.drawable.ic_person)
            avatar.layoutParams = LayoutParams(avatarSize, avatarSize)
            avatar.scaleType = ImageView.ScaleType.CENTER_CROP
            avatar.background = ContextCompat.getDrawable(context, R.drawable.bg_avatar)
            avatar.setPadding(4.dpToPx(), 4.dpToPx(), 4.dpToPx(), 4.dpToPx())
            avatar.setColorFilter(ContextCompat.getColor(context, R.color.white))
            this.addView(avatar)
        }
    }

    fun setAvatarUri(avatarUri: String?) {
        this.avatarUri = avatarUri
        this.removeAllViews()
        updateView()
    }

    fun setName(name: String?) {
        this.name = name
        this.removeAllViews()
        updateView()
    }

    fun setAvatarSize(avatarSize: Int) {
        this.avatarSize = avatarSize
        this.removeAllViews()
        updateView()
    }

    fun setOnClickAvatarListener(onPressAvatar: () -> Unit) {
        this.setOnClickListener {
            onPressAvatar()
        }
    }
}

