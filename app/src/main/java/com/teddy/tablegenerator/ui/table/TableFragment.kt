package com.teddy.tablegenerator.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.teddy.tablegenerator.R
import com.teddy.tablegenerator.databinding.TableFragmentBinding
import com.teddy.tablegenerator.extensions.getScreenSize

class TableFragment : Fragment() {
    private lateinit var viewModel: TableViewModel
    private lateinit var binding: TableFragmentBinding
    private val tableAdapter by lazy { TableAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TableViewModel::class.java).also {
            val columns = arguments?.get(TableActivity.BUNDLE_KEY_COLUMN).toString().toInt()
            val rows = arguments?.get(TableActivity.BUNDLE_KEY_ROWS).toString().toInt()
            it.totalCells = Pair(columns, rows + 1 /** Be careful! Add an extra row to show button view*/)
            it.numberOfCells = columns * rows
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.table_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedEvent.observe(viewLifecycleOwner, Observer {
            tableAdapter.select(it)
        })

        binding.table.apply {
            val screenSize = activity?.getScreenSize() ?: Pair(0, 0)  //Default is position 0
            layoutManager = GridLayoutManager(context, viewModel.totalCells.first)
            adapter = tableAdapter
            tableAdapter.setup(viewModel.totalCells, screenSize)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTick()
    }

    override fun onPause() {
        viewModel.stopTick()
        super.onPause()
    }

    companion object {
        fun newInstance() = TableFragment()
    }
}