package com.example.nint.mynote.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.example.nint.mynote.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mSearchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //openAddActivity()
            openBrowseActivity()
        }
    }

    private fun openAddActivity(){
        val intent = Intent(this@MainActivity, AddActivity::class.java)
        startActivity(intent)
    }


    private fun openBrowseActivity(){
        val intent = Intent(this@MainActivity, BrowseActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        mSearchView = menu.findItem(R.id.action_search).getActionView() as SearchView
        setupSearchView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchView() {
        mSearchView.setIconifiedByDefault(true)
        //mSearchView.setOnQueryTextListener(this)
        mSearchView.setQueryHint("Search Here")
    }
}