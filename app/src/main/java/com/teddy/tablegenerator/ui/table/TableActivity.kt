package com.teddy.tablegenerator.ui.table

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teddy.tablegenerator.R

class TableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_activity)
        if (savedInstanceState == null) {
            val fragment = TableFragment.newInstance().apply {
                this.arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    companion object {
        const val BUNDLE_KEY_ROWS = "ROWS"
        const val BUNDLE_KEY_COLUMN = "COLUMNS"
    }
}