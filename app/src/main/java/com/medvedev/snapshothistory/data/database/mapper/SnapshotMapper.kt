package com.medvedev.snapshothistory.data.database.mapper

import com.medvedev.snapshothistory.data.database.model.SnapshotEntity
import com.medvedev.snapshothistory.domain.model.Snapshot

fun Snapshot.toEntity() = SnapshotEntity(
    id = id,
    date = date,
    latitude = latitude,
    longitude = longitude,
    name = name,
    filePath = filePath
)

fun SnapshotEntity.toDomain() = Snapshot(
    id = id,
    date = date,
    latitude = latitude,
    longitude = longitude,
    name = name,
    filePath = filePath
)

fun List<SnapshotEntity>.toSnapshotDomainList() = this.map { it.toDomain() }