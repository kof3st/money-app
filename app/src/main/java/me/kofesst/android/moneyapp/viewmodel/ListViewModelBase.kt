package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Базовый класс, представляющий [ViewModelBase],
 * который взаимодействует со списком моделей [Item].
 */
abstract class ListViewModelBase<Item>(
    application: Application
) : ViewModelBase(application) {
    private val _items = MutableStateFlow<List<Item>>(listOf())
    val items get() = _items.asStateFlow()

    /**
     * Возвращает список моделей [Item].
     */
    protected abstract suspend fun getItems(): List<Item>

    /**
     * Обновляет список моделей [Item].
     */
    open fun updateItems(callback: (() -> Unit) = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            _items.value = getItems()
            callback()
        }
    }
}