package com.martynov.tydchatand

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.martynov.tydchatand.adapter.MessangeAdapter
import com.martynov.tydchatand.databinding.ActivityFeedBinding
import com.martynov.tydchatand.model.AutorModel
import com.martynov.tydchatand.model.MessangeModel
import kotlinx.coroutines.launch
import java.lang.Exception

private lateinit  var binding: ActivityFeedBinding

class FeedActivity : AppCompatActivity()/*, MessangeAdapter.OnDelBtnClickListener*/{
    var iteams : ArrayList<MessangeModel> = ArrayList()
    var adapter = MessangeAdapter(ArrayList<MessangeModel>())
    var me: AutorModel? = null
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar!!.setSubtitle(R.string.chat)


    }
//        lifecycleScope.launch {
//            me = App.repository.getMe().body()?.copy()
//        }
//        binding.sendMessange.setOnClickListener {
//            Log.d("My", "Отпарвил")
//            lifecycleScope.launch {
//                val sendMessangeText = App.repository.newMessange(messangeModel = MessangeModel(autor = me,messangeText = binding.inputMessenge.text.toString()))
//                if(sendMessangeText.isSuccessful){
//                    with(binding.container){
//                        layoutManager  = LinearLayoutManager(this@FeedActivity)
//                        adapter = MessangeAdapter(sendMessangeText.body() as MutableList<MessangeModel>).apply {
//                            delBtnClickListener = this@FeedActivity
//                            this.me = me
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//        lifecycleScope.launch {
//            dialog = ProgressDialog(this@FeedActivity).apply {
//                setMessage(getString(R.string.please_wait))
//                //setTitle(getString(R.string.loading_ideas))
//                setCancelable(false)
//                setProgressBarIndeterminate(true)
//                show()
//            }
//            try{
//                val result = App.repository.getAllMessange()
//                dialog?.dismiss()
//                if (result.isSuccessful) {
//                    with(binding.container) {
//                        iteams = result.body() as ArrayList<MessangeModel>
//                        layoutManager  = LinearLayoutManager(this@FeedActivity)
//                        adapter = MessangeAdapter(iteams as MutableList<MessangeModel>).apply {
//                            delBtnClickListener = this@FeedActivity
//                            this.me = me
//                        }
//                    }
//                }
//
//            }catch (e: Exception){
//                dialog?.dismiss()
//            }
//
//        }
//    }
//
//    override fun onDelBtnClicked(item: MessangeModel, position: Int) {
//        Log.d("My", "Активити")
//        Log.d("My", position.toString())
////        iteams.removeAt(position)
////        adapter.notifyItemRemoved(position)
//
//    }

}