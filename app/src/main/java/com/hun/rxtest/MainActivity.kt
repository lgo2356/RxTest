package com.hun.rxtest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    var list: ArrayList<String> by Delegates.observable(arrayListOf()) { prop, old, new ->
        Log.d("Debug", "new")
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvData: TextView = findViewById(R.id.tv_data)
        val tvOld: TextView = findViewById(R.id.tv_old)
        val tvProp: TextView = findViewById(R.id.tv_prop)
        val btnInput: Button = findViewById(R.id.btn_input)
        var strData: String by Delegates.observable("Hello world!") { prop, old, new ->
            tvData.text = new
            tvOld.text = old
            tvProp.text = prop.toString()
        }

//        val input = btnInput.clicks()
//        input.subscribe {
//            Toast.makeText(applicationContext, "aa", Toast.LENGTH_LONG).show()
//        }

        val oList: ObservableList<String> = ObservableList()
        val disposable: Disposable = oList.getObservable()
            .subscribe {
                it?.let { str ->
                    Log.d("Debug", str)
                }
            }
        compositeDisposable.add(disposable)

        btnInput.setOnClickListener {
            // Hello world
//            strData = "!dlrow olleH"
//            list.add("a")
            oList.add("bbb")
        }
    }

    class ObservableList<String> {

        private val list: ArrayList<String> = ArrayList()
        private val subject: PublishSubject<String> = PublishSubject.create()

        fun add(value: String) {
            value?.let {
                list.add(it)
                subject.onNext(it)
            }
        }

        fun getObservable(): PublishSubject<String> {
            return subject
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }
}
