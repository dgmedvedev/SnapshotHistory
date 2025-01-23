package com.medvedev.snapshothistory.app.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.snapshothistory.domain.model.Snapshot

class SnapshotDiffCallback : DiffUtil.ItemCallback<Snapshot>() {

    override fun areItemsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
        return oldItem == newItem
    }
}