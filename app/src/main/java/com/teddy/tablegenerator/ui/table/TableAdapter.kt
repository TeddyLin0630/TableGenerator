package com.teddy.tablegenerator.ui.table

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teddy.tablegenerator.R

class TableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var totalCells = Pair(0, 0) // Pair(NumberOfColumn, NumberOfRow)
    private var selectedPosition = -1 // Start from position 0
    private var screenSize = Pair(0, 0) // Pair(screen.x, screen.y)

    fun setup(cells: Pair<Int, Int>, screen: Pair<Int, Int>) {
        screenSize = screen
        totalCells = cells
        notifyDataSetChanged()
    }

    fun select(selected: Int) {
        selectedPosition = selected
        notifyDataSetChanged()
    }

    // columns * rows = total cells
    override fun getItemCount(): Int = totalCells.first * totalCells.second

    override fun getItemViewType(position: Int): Int {
        // position / column : where the current position in table's row
        // We need to (row - 1) because shift for position from 0 not 1
        return if (position / (totalCells.first) == totalCells.second - 1) VIEW_TYPE_BOTTOM else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> NormalCell(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.table_normal_cell,
                    parent,
                    false
                )
            )

            else -> BottomCell(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.table_bottom_cell,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NormalCell -> {
                val heightOfCell = screenSize.second / totalCells.second
                (holder.imgCell.layoutParams as RelativeLayout.LayoutParams).height = heightOfCell
                (holder.divideStart.layoutParams as RelativeLayout.LayoutParams).height =
                    heightOfCell
                (holder.divideEnd.layoutParams as RelativeLayout.LayoutParams).height =
                    heightOfCell
                holder.textRandom.isVisible = (selectedPosition == position)
                if (selectedPosition == -1) {
                    clearHighlight(listOf(holder.divideEnd, holder.divideStart, holder.divideTop))
                } else {
                    tryHighLightCells(listOf(holder.divideEnd, holder.divideStart), position)
                    tryHighlightTop(holder.divideTop, position)
                }
            }

            is BottomCell -> {
                val heightOfCell = screenSize.second / totalCells.second
                (holder.btnConfirm.layoutParams as RelativeLayout.LayoutParams).height =
                    heightOfCell
                if (selectedPosition == -1) {
                    clearBottomHighlight(listOf(holder.textConfirm, holder.btnConfirm))
                } else {
                    tryEnableBottomViews(listOf(holder.textConfirm, holder.btnConfirm), position)
                }
                holder.btnConfirm.setOnClickListener { select(-1) }
            }
        }
    }

    /*
    * Highlight start and end divide if the view is same with selected's column
    * */
    private fun tryHighLightCells(views: List<View>, position: Int) {
        views.forEach {
            it.setBackgroundColor(
                if (selectedPosition % totalCells.first != position % totalCells.first) {
                    ContextCompat.getColor(
                        it.context,
                        android.R.color.white
                    )
                } else {
                    ContextCompat.getColor(
                        it.context,
                        R.color.lightBlue
                    )
                }
            )
        }
    }

    /*
    * if position is at top of row and same with selected's column
    * highlight the top of divide
    * */
    private fun tryHighlightTop(view: View, position: Int) {
        view.setBackgroundColor(
            if (selectedPosition % totalCells.first != position % totalCells.first
                || position / totalCells.first != 0
            ) {
                ContextCompat.getColor(
                    view.context,
                    android.R.color.white
                )
            } else {
                ContextCompat.getColor(
                    view.context,
                    R.color.lightBlue
                )
            }
        )
    }

    /*
    * Clear all cell's highlight
    * */
    private fun clearHighlight(views: List<View>) {
        views.forEach {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    it.context,
                    android.R.color.white
                )
            )
        }
    }

    /*
    * Clear all bottom's view highlight
    * */
    private fun clearBottomHighlight(views: List<View>) {
        views.forEach { it.isEnabled = false }
    }

    private fun tryEnableBottomViews(views: List<View>, position: Int) {
        views.forEach {
            it.isEnabled = (selectedPosition % totalCells.first == position % totalCells.first)
        }
    }

    class NormalCell(view: View) : RecyclerView.ViewHolder(view) {
        val imgCell = view.findViewById<ImageView>(R.id.imgCellBg)!!
        val textRandom = view.findViewById<TextView>(R.id.textRandom)!!
        val divideTop = view.findViewById<View>(R.id.divideTop)!!
        val divideStart = view.findViewById<View>(R.id.divideStart)!!
        val divideEnd = view.findViewById<View>(R.id.divideEnd)!!
    }

    class BottomCell(view: View) : RecyclerView.ViewHolder(view) {
        val btnConfirm = view.findViewById<ImageButton>(R.id.btnConfirm)!!
        val textConfirm = view.findViewById<TextView>(R.id.textConfirm)!!
    }

    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_BOTTOM = 1
    }
}