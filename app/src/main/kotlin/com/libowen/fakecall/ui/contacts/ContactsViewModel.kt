package com.libowen.fakecall.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.domain.repository.CallerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: CallerRepository
) : ViewModel() {

    val contacts: StateFlow<List<CallerInfo>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(caller: CallerInfo) {
        viewModelScope.launch {
            repository.delete(caller)
        }
    }

    fun save(name: String, number: String, avatarUri: String? = null, setAsDefault: Boolean = false) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val caller = CallerInfo(
                id = UUID.randomUUID().toString(),
                name = name,
                number = number,
                avatarUri = avatarUri
            )
            repository.save(caller)
            if (setAsDefault) {
                repository.setDefault(caller.id)
            }
        }
    }

    fun setDefault(id: String) {
        viewModelScope.launch {
            repository.setDefault(id)
        }
    }
}
