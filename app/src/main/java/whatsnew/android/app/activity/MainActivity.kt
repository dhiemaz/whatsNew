package whatsnew.android.app.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.json.JSONException
import org.json.JSONObject
import whatsnew.android.app.R
import whatsnew.android.app.database.Database
import whatsnew.android.app.fragments.Favorite_Fragment
import whatsnew.android.app.fragments.NewsFragment
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    var KEEP: Boolean = true
    val DELAY = 1250L

    val executor = Executor { runnable -> runnable.run() }
    var bottomNavigationView: BottomNavigationView? = null

    var titles = ArrayList<String>()
    var description = ArrayList<String>()
    var authors = ArrayList<String>()
    var urlToImage = ArrayList<String>()
    var url = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // handle the splash screen transition
        installSplashScreen().setKeepOnScreenCondition{KEEP}
        Handler(Looper.getMainLooper()).postDelayed({KEEP = false}, DELAY)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_layout)

        val executorService = Executors.newFixedThreadPool(2)
        // build a thread pool ExecutorService j = Executors.newFixedThreadPool
        // 1) read
        // 2) write
        bottomNavigationView = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView
        val handler = Database(applicationContext, executor)

        val queue = Volley.newRequestQueue(this)
        val urlNewsApp = "https://saurav.tech/NewsAPI/everything/cnn.json"
        val stringRequest = StringRequest(Request.Method.GET, urlNewsApp, {
            response ->
                try {
                    val js = JSONObject(response)
                    var data = js.getJSONArray("articles")
                    for (i in 0 until data.length()) {
                        try {
                            urlToImage.add(data.getJSONObject(i).getString("urlToImage"))
                            titles.add(data.getJSONObject(i).getString("title"))
                            description.add(data.getJSONObject(i).getString("description"))
                            authors.add(data.getJSONObject(i).getString("author"))
                            url.add(data.getJSONObject(i).getString("url"))
                        } catch (e: JSONException) {
                            println("field for i cant load json file")
                        }

                        // TODO : fix this for list urlto image view and other

                        //                                RecyclerView.LayoutManager LayoutManager = new
                        //                                        LinearLayoutManager(NewsFragment, LinearLayoutManager.VERTICAL
                        //                                        , false);
                        //                                recyclerView.setLayoutManager(LayoutManager);
                        //                                recyclerView.setAdapter(new CycleAdapter(getApplicationContext(), urlToImage
                        //                                        , titles, description, authors));
                    }
                    val newsFragment = NewsFragment(urlToImage, titles, description, authors, url)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContiner, newsFragment, null)
                        .commit()
                } catch (err: JSONException) {
                    Log.e("MainActivity", err.toString())
                }
            })
        {
            Log.e("MainActivity", it.toString())
        }

        queue.add(stringRequest)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        val newsFragment = NewsFragment(urlToImage, titles, description, authors, url)
        bottomNavigationView?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.news) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContiner, newsFragment, null)
                    .commit()
                true
            } else {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContiner, Favorite_Fragment::class.java, null)
                    .commit()
                true
            }
        })
    }
}