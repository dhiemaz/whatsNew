package whatsnew.android.app.fragments

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whatsnew.android.app.R
import whatsnew.android.app.adapter.FavoriteAdapter
import whatsnew.android.app.database.Database
import java.util.concurrent.Executor

class Favorite_Fragment : Fragment(R.layout.fragment_favourite_layout) {
    private var recyclerView: RecyclerView? = null
    var title = ArrayList<String>()
    var url = ArrayList<String>()
    var imageurl = ArrayList<String>()
    private var executor: Executor? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executor = Executor { runnable -> runnable.run() }

        // TODO : bug is here not get data from while
        val handler = Database(context, executor!!)
        val cursor: Cursor? = handler.readData()
        if (cursor?.count != 0) {
            cursor?.moveToFirst()
            do {
                val item = cursor?.getString(cursor.getColumnIndexOrThrow("Title"))
                if (item != null) {
                    title.add(item)
                }

                val itemImageUrl = cursor?.getString(cursor.getColumnIndexOrThrow("ImageUrl"))
                if (itemImageUrl != null) {
                    imageurl.add(itemImageUrl)
                }
                val itemUrl = cursor?.getString(cursor.getColumnIndexOrThrow("Url"))
                if (itemUrl != null) {
                    url.add(itemUrl)
                }
            } while (cursor?.moveToNext() == true)

            cursor?.close()
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setAdapter(FavoriteAdapter(context, title, imageurl, url))
        } else {
            Toast.makeText(context, "favorite has empty", Toast.LENGTH_SHORT).show()
        }
    }
}
