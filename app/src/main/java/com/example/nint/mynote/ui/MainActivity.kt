package com.example.nint.mynote.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.example.nint.mynote.R
import com.example.nint.mynote.model.Item
import com.example.nint.mynote.model.RealmHelper
import com.example.nint.mynote.model.RecyclerViewAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    lateinit var mSearchView: SearchView
    lateinit var realm:Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        realm = Realm.getDefaultInstance()

        initRecyclerView()
        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            intent.putExtra("ID","NEW")
            startActivity(intent)
        }
    }

    override fun onResume() {
        initRecyclerView()
        super.onResume()
    }

    private fun initRecyclerView(){
        var list = RealmHelper.readToRealm(realm)
        var mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mLayoutManager.scrollToPosition(RealmHelper.position)

        rv_list_items.layoutManager = mLayoutManager
        rv_list_items.adapter = RecyclerViewAdapter(this,list)
        rv_list_items.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        mSearchView = menu.findItem(R.id.action_search).getActionView() as SearchView
        setupSearchView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                //startActivity(Intent(this@MainActivity,AddActivity::class.java))
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                intent.putExtra("ID","NEW")
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchView() {
        mSearchView.setIconifiedByDefault(true)
        //mSearchView.setOnQueryTextListener(this)
        mSearchView.setQueryHint("Search Here")
        mSearchView.setOnQueryTextListener(this)

    }
    override fun onQueryTextSubmit(query: String?): Boolean {

     return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var list = RealmHelper.readToRealm(realm,"nameInsensitive",newText!!.toUpperCase())
        var mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mLayoutManager.scrollToPosition(RealmHelper.position)

        rv_list_items.layoutManager = mLayoutManager
        rv_list_items.adapter = RecyclerViewAdapter(this,list)
        rv_list_items.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            )
        )

        return true
    }

}


