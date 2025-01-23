package com.medvedev.snapshothistory.app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.snapshothistory.databinding.SnapshotItemBinding
import com.medvedev.snapshothistory.domain.model.Snapshot
import java.text.SimpleDateFormat
import java.util.Locale

class SnapshotAdapter(private val onSnapshotItemClickListener: (Snapshot) -> Unit) :
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
        val snapshot: Snapshot = getItem(position)
        holder.bind(snapshot = snapshot)
    }

    inner class SnapshotHolder(private val binding: SnapshotItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var snapshotItem: Snapshot

        fun bind(snapshot: Snapshot) {
            snapshotItem = snapshot
            val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(snapshotItem.date)
            binding.apply {
                tvName.text = snapshotItem.name
                tvDate.text = date
                root.setOnClickListener {
                }
            }
        }

        override fun onClick(v: View?) {
            onSnapshotItemClickListener.invoke(snapshotItem)
        }
    }

    private class SnapshotDiffCallback : DiffUtil.ItemCallback<Snapshot>() {
        override fun areItemsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy  HH:mm"
    }
}