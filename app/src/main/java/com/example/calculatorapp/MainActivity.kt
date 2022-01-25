package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorapp.math.StringMath.Companion.rpnToResult
import com.example.calculatorapp.data.History
import com.example.calculatorapp.databinding.ActivityMainBinding
import com.example.calculatorapp.fragment.HistoryFragment
import com.example.calculatorapp.math.ShuntingYard
import com.example.calculatorapp.math.StringMath.Companion.powerStrings
import com.example.calculatorapp.viewmodel.AppViewModel
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        obverseData()
    }

    private fun obverseData() {
        viewModel.textResult.observe(this){
            binding.tvResult.text = it
        }
        viewModel.textExpression.observe(this){
            binding.tvCurrent.text = it
        }
        viewModel.message.observe(this){
            showToast(it)
        }
    }

    private fun showToast(st: String?) {
        st?.let {
            Toast.makeText(this,st,Toast.LENGTH_SHORT).show()
        }
    }

    fun clickBracket(view: View) {
        if (view is Button) {
            viewModel.onBracket(view.text as String)
        }
    }

    fun clickNumber(view: View) {
        if (view is Button) {
            viewModel.onNumber(view.text.toString())
        }
    }

    fun clickOperation(view: View) {
        if (view is Button) {
            viewModel.onOperation(view.text.toString())
        }
    }

    fun clickSqrt(view: View) {
        if (view is Button) {
            viewModel.onSqrt(view.text.toString())
        }
    }

    fun clickEquals(view: View) {
        viewModel.onEquals()
//       val test = powerStrings("2","12345")
//        Log.e("test",test)
//        Log.e("test",test.length.toString())
    }



    fun clickClear(view: View) {
        viewModel.onClear()

    }

    fun clickUndo(view: View) {
       viewModel.onUndo()
    }

    fun clickShowHistory(view: View) {
        val fragment = HistoryFragment()
        fragment.show(supportFragmentManager, "bottom_sheet_frag")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}