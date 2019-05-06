package com.example.nint.mynote.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import com.example.nint.mynote.R
import com.example.nint.mynote.model.Item
import com.example.nint.mynote.model.RealmHelper
import com.example.nint.mynote.model.RecyclerViewAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.content.res.AssetManager
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig
import java.io.IOException
import java.lang.Exception
import java.nio.file.DirectoryIteratorException


class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    val DIRECTORY_IMPORT = 0
    val DIRECTORY_EXPORT = 1
    lateinit var mSearchView: SearchView
    lateinit var realm:Realm
    var pathForBackup = ""
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
        val intent = Intent(this@MainActivity,DirectoryChooserActivity::class.java)
        val config = DirectoryChooserConfig.builder()
            .newDirectoryName("MyNote")
            .allowReadOnlyDirectory(true)
            .allowNewDirectoryNameModification(true)
            .build()

        intent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG,config)

        return when (item.itemId) {
            R.id.action_settings -> {
                //startActivity(Intent(this@MainActivity,AddActivity::class.java))
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                intent.putExtra("ID","NEW")
                startActivity(intent)
                return true
            }
            R.id.action_import->{
                startActivityForResult(intent,DIRECTORY_IMPORT)
                return true
            }
            R.id.action_export ->{

                startActivityForResult(intent,DIRECTORY_EXPORT)
                return true
            }

            R.id.action_help ->{
                if (myBackup( this.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE).path, this.getDir("backup", Context.MODE_PRIVATE).path)){
                    Toast.makeText(this,"Копирование завершено",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Ошибка копирования",Toast.LENGTH_LONG).show()
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MYTAG","onActivityresult")
        Log.d("MYTAG","request_code="+requestCode.toString())

        when(requestCode) {
            DIRECTORY_EXPORT -> {
                Log.d("MYTAG","request_code"+requestCode.toString())
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    pathForBackup = data!!.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR)
                    Log.d("MYTAG",this.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE).path)
                    Log.d("MYTAG",pathForBackup)

                    if (myBackup( this.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE).path, pathForBackup))
                        Log.d("MYTAG","copy OK")
                } else {
                    // Nothing selected
                }
            }
            DIRECTORY_IMPORT -> {
                Toast.makeText(this,"Toast",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun myBackup(oldPath:String,newPath:String):Boolean{
        val old = File(oldPath)
        val new = File(newPath)
        val data = File(newPath+"/backup")
        data.writeText(RealmHelper.exportRealmToJson(realm))

        return old.copyRecursively(new,true)
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


    fun getListFile():String{
        var result= ""
        var dir = this.getDir("backup", Context.MODE_PRIVATE)

        var list = dir.listFiles()
        for(a in list){
            result += a.toString()+";"
        }
        return result
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


