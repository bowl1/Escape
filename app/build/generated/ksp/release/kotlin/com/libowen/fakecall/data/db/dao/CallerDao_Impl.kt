package com.libowen.fakecall.`data`.db.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.libowen.fakecall.`data`.db.entity.CallerEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CallerDao_Impl(
  __db: RoomDatabase,
) : CallerDao {
  private val __db: RoomDatabase

  private val __deleteAdapterOfCallerEntity: EntityDeleteOrUpdateAdapter<CallerEntity>

  private val __upsertAdapterOfCallerEntity: EntityUpsertAdapter<CallerEntity>
  init {
    this.__db = __db
    this.__deleteAdapterOfCallerEntity = object : EntityDeleteOrUpdateAdapter<CallerEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `callers` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CallerEntity) {
        statement.bindText(1, entity.id)
      }
    }
    this.__upsertAdapterOfCallerEntity = EntityUpsertAdapter<CallerEntity>(object : EntityInsertAdapter<CallerEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `callers` (`id`,`name`,`number`,`avatarUri`,`isDefault`,`createdAt`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CallerEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.number)
        statement.bindText(4, entity.avatarUri)
        val _tmp: Int = if (entity.isDefault) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindLong(6, entity.createdAt)
      }
    }, object : EntityDeleteOrUpdateAdapter<CallerEntity>() {
      protected override fun createQuery(): String = "UPDATE `callers` SET `id` = ?,`name` = ?,`number` = ?,`avatarUri` = ?,`isDefault` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CallerEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.number)
        statement.bindText(4, entity.avatarUri)
        val _tmp: Int = if (entity.isDefault) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindLong(6, entity.createdAt)
        statement.bindText(7, entity.id)
      }
    })
  }

  public override suspend fun delete(caller: CallerEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCallerEntity.handle(_connection, caller)
  }

  public override suspend fun upsert(caller: CallerEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfCallerEntity.upsert(_connection, caller)
  }

  public override fun getAll(): Flow<List<CallerEntity>> {
    val _sql: String = "SELECT * FROM callers ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("callers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfNumber: Int = getColumnIndexOrThrow(_stmt, "number")
        val _columnIndexOfAvatarUri: Int = getColumnIndexOrThrow(_stmt, "avatarUri")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<CallerEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CallerEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpNumber: String
          _tmpNumber = _stmt.getText(_columnIndexOfNumber)
          val _tmpAvatarUri: String
          _tmpAvatarUri = _stmt.getText(_columnIndexOfAvatarUri)
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = CallerEntity(_tmpId,_tmpName,_tmpNumber,_tmpAvatarUri,_tmpIsDefault,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDefault(): Flow<CallerEntity?> {
    val _sql: String = "SELECT * FROM callers WHERE isDefault = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("callers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfNumber: Int = getColumnIndexOrThrow(_stmt, "number")
        val _columnIndexOfAvatarUri: Int = getColumnIndexOrThrow(_stmt, "avatarUri")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: CallerEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpNumber: String
          _tmpNumber = _stmt.getText(_columnIndexOfNumber)
          val _tmpAvatarUri: String
          _tmpAvatarUri = _stmt.getText(_columnIndexOfAvatarUri)
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = CallerEntity(_tmpId,_tmpName,_tmpNumber,_tmpAvatarUri,_tmpIsDefault,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearDefault() {
    val _sql: String = "UPDATE callers SET isDefault = 0"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setDefault(id: String) {
    val _sql: String = "UPDATE callers SET isDefault = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
