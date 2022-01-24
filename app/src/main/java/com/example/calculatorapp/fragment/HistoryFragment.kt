package com.example.calculatorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculatorapp.R
import com.example.calculatorapp.adapter.HistoryAdapter
import com.example.calculatorapp.viewmodel.AppViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.DividerItemDecoration





class HistoryFragment : BottomSheetDialogFragment() {

    private var viewModel: AppViewModel? = null
    private val adapter = HistoryAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_history)
        rv.adapter = adapter
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val decor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rv.addItemDecoration(decor)
        viewModel?.getAll()?.observe(viewLifecycleOwner) {
            if (it != null) adapter.setData(it)
        }
    }

    companion object {

    }
}