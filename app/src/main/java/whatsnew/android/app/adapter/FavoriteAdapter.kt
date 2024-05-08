package whatsnew.android.app.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import whatsnew.android.app.R
import whatsnew.android.app.database.Database
import java.util.concurrent.Executor


class FavoriteAdapter(val context: Context?, val title: ArrayList<String>,
                      val favoriteImage: ArrayList<String>, val goToUrl: ArrayList<String>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    private val actualPosition = 0
    private val executor = Executor { runnable -> runnable.run() }

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var favoriteImage: ImageView
        var title: TextView
        var button: Button
        var deleteItemFromList: ImageView

        init {
            deleteItemFromList = itemView.findViewById<ImageView>(R.id.deleteFavoriteItem)
            favoriteImage = itemView.findViewById<ImageView>(R.id.favoriteImage)
            title = itemView.findViewById<TextView>(R.id.favoriteTitle)
            button = itemView.findViewById<Button>(R.id.buttonUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_items_layout, parent, false)

        // View view = LayoutInflater.from(parent.getContext())
        // .inflate(R.Layout.EXAMPLE ,parent ,flase);
        return FavoriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        Picasso.with(context).load(favoriteImage[position]).into(holder.favoriteImage)
        holder.title.text = title[position]
        val dbHandler = Database(context, executor)
        holder.deleteItemFromList.setOnClickListener {
            dbHandler.deleteData(
                title[position],
                goToUrl[position], favoriteImage[position]
            )
            removeItem(holder.getAdapterPosition())
        }
        holder.button.setOnClickListener {
            val url = goToUrl[position]
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            val customTabsIntent: CustomTabsIntent = builder.build()
            if (context != null) {
                customTabsIntent.launchUrl(context, Uri.parse(url))
            }
        }
    }

    override fun getItemCount(): Int {
        return title.size
    }

    fun removeItem(position: Int) {
        title.removeAt(position)
        favoriteImage.removeAt(position)
        goToUrl.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, title.size)
        notifyItemRangeChanged(position, favoriteImage.size)
        notifyItemRangeChanged(position, goToUrl.size)
    }
}
