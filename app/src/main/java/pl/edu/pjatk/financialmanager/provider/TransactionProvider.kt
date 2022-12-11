package pl.edu.pjatk.financialmanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pl.edu.pjatk.financialmanager.persistance.FinancialManagerDatabase
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.persistance.repository.TransactionRepository

class TransactionProvider : ContentProvider() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var database: FinancialManagerDatabase
    private lateinit var repository: TransactionRepository

    companion object {
        private const val AUTHORITY: String = "pl.edu.pjatk.financialmanager.provider"
        private val TABLE_NAME: String = Transaction::class.java.simpleName
        private val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        private val transactionUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "$CONTENT_URI/", 1)
            addURI(AUTHORITY, "$CONTENT_URI/#", 2)
        }
    }

    override fun onCreate(): Boolean {
        database = FinancialManagerDatabase.getDatabase(
            context!!,
            applicationScope
        )
        repository = TransactionRepository(database.transactionDao())
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = getCursorMatchingUri(uri)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    private fun getCursorMatchingUri(uri: Uri): Cursor = when (transactionUriMatcher.match(uri)) {
        1 -> {
            repository.allTransactionsCursor
        }
        2 -> {
            val id = ContentUris.parseId(uri)
            repository.transactionCursor(id)
        }
        else -> {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        val typePart = "vnd"
        var subtypePart = "android.cursor."
        val providerSpecificPart = "$typePart.$AUTHORITY.$TABLE_NAME"
        when (transactionUriMatcher.match(uri)) {
            1 -> { // multiple rows
                subtypePart += "dir"
            }
            2 -> { // single row
                subtypePart += "item"
            }
        }
        return "$typePart.$subtypePart/$providerSpecificPart"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Transaction.fromContentValues(values)?.let {
            val id = repository.insertOnMainThread(it)
            id
        }?.let {
            return Uri.parse("$CONTENT_URI/$it")
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        ContentUris.parseId(uri).let {
            return repository.deleteOnMainThread(it)
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Transaction.fromContentValues(values)?.let {
            return repository.updateOnMainThread(it)
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }
}