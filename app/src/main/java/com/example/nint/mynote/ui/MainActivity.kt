package com.example.nint.mynote.ui


import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nint.mynote.MySaveFileDialog
import com.example.nint.mynote.R
import com.example.nint.mynote.model.RealmHelper
import com.example.nint.mynote.model.RecyclerViewAdapter
import com.rustamg.filedialogs.FileDialog
import com.rustamg.filedialogs.OpenFileDialog
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.StandardOpenOption


class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener, FileDialog.OnFileSelectedListener {


    lateinit var mSearchView: SearchView
    lateinit var realm:Realm
    var pathForBackup = ""
    lateinit var exportFile:File
    lateinit var importFile:File
    lateinit var openFileDialog: OpenFileDialog
    lateinit var saveFileDialog: MySaveFileDialog

    override fun onFileSelected(dialog: FileDialog?, file: File?) {
        if(dialog == openFileDialog){
            Toast.makeText(this,"open",Toast.LENGTH_LONG).show()
        }
        if(dialog == saveFileDialog){
            myBackup(file!!)
            Toast.makeText(this,"save",Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        realm = Realm.getDefaultInstance()
        openFileDialog = OpenFileDialog()
        saveFileDialog = MySaveFileDialog()

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
            R.id.action_export->{
                val args = Bundle()
                args.putString(FileDialog.EXTENSION,".mnb")
                args.putString(MySaveFileDialog.DEFAULT_FILE,"MyNoteBackup")
                saveFileDialog.arguments = args
                saveFileDialog.show(supportFragmentManager,"Save a file")
                return true
            }

            R.id.action_import ->{
                val args = Bundle()
                args.putString(FileDialog.EXTENSION,"mnb")
                openFileDialog.arguments = args
                openFileDialog.show(supportFragmentManager,"Open a file")
                return true
            }

            R.id.action_help ->{

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun clearBackupDir(dir:File):Boolean{
        try {
            for(tempFile in dir.listFiles()){
                tempFile.delete()
            }
            return true
        }catch (e:Exception){
            Log.d("MYTAG","File is not clear")
        }
        return false
    }


    fun myBackup(file:File){

        var dir = this.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE)

        var list = dir.listFiles()
        var b = file.outputStream()
        for(a in list){
            b.write(a.readBytes())
        }
        b.close()
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


