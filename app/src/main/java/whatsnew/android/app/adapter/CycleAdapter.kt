package whatsnew.android.app.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import whatsnew.android.app.R
import whatsnew.android.app.database.Database
import whatsnew.android.app.model.TransferData
import java.util.concurrent.Executor


class CycleAdapter(context: Context, urlImage: ArrayList<String>, titles: ArrayList<String>,
    descriptions: ArrayList<String>, authors: ArrayList<String>, url: ArrayList<String>
) : RecyclerView.Adapter<CycleAdapter.myCycleAdapter>() {
    private val context: Context
    private var urlImage = ArrayList<String>()
    private var titles = ArrayList<String>()
    private var descriptions = ArrayList<String>()
    private var authors = ArrayList<String>()
    var url = ArrayList<String>()
    private var executor: Executor? = null

    init {
        this.authors = authors
        this.context = context
        this.descriptions = descriptions
        this.titles = titles
        this.urlImage = urlImage
        this.url = url
    }

    inner class myCycleAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var description: TextView
        var title: TextView
        var author: TextView
        var shimmerFrameLayout: ShimmerFrameLayout
        var like: ImageView

        init {
            like = itemView.findViewById<ImageView>(R.id.like)
            imageView = itemView.findViewById<ImageView>(R.id.image)
            description = itemView.findViewById<TextView>(R.id.description)
            title = itemView.findViewById<TextView>(R.id.title)
            author = itemView.findViewById<TextView>(R.id.author)
            shimmerFrameLayout = itemView.findViewById(R.id.shimmereffect)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myCycleAdapter {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return myCycleAdapter(view)
    }

    override fun onBindViewHolder(holder: myCycleAdapter, position: Int) {
        holder.shimmerFrameLayout.startShimmer()
        val runnable = Runnable { holder.shimmerFrameLayout.hideShimmer() }
        val handler = Handler()
        handler.postDelayed(runnable, 2000)
        Picasso.with(context).load(urlImage[position]).into(holder.imageView)
        holder.author.text = authors[position]
        holder.title.text = titles[position]
        holder.description.text = descriptions[position]
        executor = Executor { runnable -> runnable.run() }
        val dbHandler = Database(context, executor!!)
        holder.like.setOnClickListener {
            holder.like.setImageResource(R.drawable.ic_baseline_favorite_24)
            dbHandler.addData(titles[position], url[position], urlImage[position])
            TransferData().isTransferData = true
            Toast.makeText(context, "this Articles saved", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return authors.size
    }
}
