package sample.base.app.utils.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object BindingAdapter {

    @BindingAdapter("app:viewState")
    @JvmStatic
    fun setViewState(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun setImageUrl(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}