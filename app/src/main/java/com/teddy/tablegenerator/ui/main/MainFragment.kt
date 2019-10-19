package com.teddy.tablegenerator.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.teddy.tablegenerator.BuildConfig
import com.teddy.tablegenerator.R
import com.teddy.tablegenerator.consts.EditError
import com.teddy.tablegenerator.databinding.MainFragmentBinding
import com.teddy.tablegenerator.ui.table.TableActivity

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textAppInfo.text = getString(R.string.text_app_info, BuildConfig.VERSION_NAME)

        // Column's edit view
        binding.editColumnLayout.hint =
            getString(R.string.edit_outline_hint_column, MAX_NUMBER_OF_COLUMNS)
        binding.editColumn.addTextChangedListener {
            val columns = it.toString().toIntOrNull() ?: 1
            binding.editColumnLayout.error =
                errorMessage(viewModel.checkNumberOfRowAndColumn(columns, MAX_NUMBER_OF_COLUMNS), MAX_NUMBER_OF_COLUMNS)
            updateButtonState()
        }

        // Row's edit view
        binding.editRowLayout.hint = getString(R.string.edit_outline_hint_row, MAX_NUMBER_OF_ROWS)
        binding.editRow.addTextChangedListener {
            val rows = it.toString().toIntOrNull() ?: 1
            binding.editRowLayout.error =
                errorMessage(viewModel.checkNumberOfRowAndColumn(rows, MAX_NUMBER_OF_ROWS), MAX_NUMBER_OF_ROWS)
            updateButtonState()
        }

        // Generate button
        binding.btnGenerate.setOnClickListener {
            val intent = Intent(context, TableActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        Pair(
                            TableActivity.BUNDLE_KEY_COLUMN,
                            binding.editColumn.text.toString().toInt()
                        ),
                        Pair(TableActivity.BUNDLE_KEY_ROWS, binding.editRow.text.toString().toInt())
                    )
                )
            }
            startActivity(intent)
        }
    }

    private fun errorMessage(error: EditError, limit: Int) =
        when (error) {
            EditError.NUMBER_TOO_SMALL -> getString(R.string.error_number_too_small)
            EditError.NUMBER_TOO_LARGE -> getString(R.string.error_number_too_large, limit)
            else -> null
        }

    private fun updateButtonState() {
        val columns = binding.editColumn.text.toString().toIntOrNull() ?: 0
        val rows = binding.editRow.text.toString().toIntOrNull() ?: 0
        binding.btnGenerate.isEnabled =
            columns in 1..MAX_NUMBER_OF_COLUMNS && rows in 1..MAX_NUMBER_OF_ROWS
    }

    companion object {
        fun newInstance() = MainFragment()
        const val MAX_NUMBER_OF_ROWS = 10
        const val MAX_NUMBER_OF_COLUMNS = 10
    }
}