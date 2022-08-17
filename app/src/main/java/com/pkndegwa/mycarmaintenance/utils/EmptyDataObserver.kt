package com.pkndegwa.mycarmaintenance.utils

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyDataObserver(
    private val isEmpty: Boolean, private val recyclerView: RecyclerView?, private val emptyView: View?) :
    RecyclerView.AdapterDataObserver() {

    init {
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        emptyView?.visibility = if (isEmpty) View.VISIBLE else View.GONE

        recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE

        Log.d("EmptyDataObserver", "Empty checked")
    }

    override fun onChanged() {
        super.onChanged()
        checkIfEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkIfEmpty()
    }
}