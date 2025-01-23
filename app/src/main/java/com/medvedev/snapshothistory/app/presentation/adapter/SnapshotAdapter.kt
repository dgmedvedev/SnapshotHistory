package com.medvedev.snapshothistory.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.snapshothistory.databinding.SnapshotItemBinding
import com.medvedev.snapshothistory.domain.model.Snapshot

class SnapshotAdapter :
    ListAdapter<Snapshot, SnapshotAdapter.SnapshotHolder>(SnapshotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
        val binding = SnapshotItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SnapshotHolder(binding)
    }

    override fun onBindViewHolder(holder: SnapshotHolder, position: Int) {
        val snapshotItem: Snapshot = getItem(position)
        val binding: SnapshotItemBinding = holder.binding
        binding.tvName.text = snapshotItem.name
        binding.tvDate.text = snapshotItem.date.toString()
    }

    inner class SnapshotHolder(val binding: SnapshotItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}