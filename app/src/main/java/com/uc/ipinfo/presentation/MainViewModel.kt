package com.uc.ipinfo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uc.ipinfo.domain.usecase.GetIpInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.uc.ipinfo.domain.model.IpInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ipInfoUseCase: GetIpInfo
) : ViewModel() {
    private val _data = MutableLiveData<IpInfo?>()
    val data: LiveData<IpInfo?> = _data
    private fun updateData(newData: IpInfo?) {
        _data.value = newData
    }

    private val _searching = MutableLiveData(false)
    val searching: LiveData<Boolean> = _searching
    private fun setSearching(isSearching: Boolean) {
        _searching.value = isSearching
    }

    fun getIpInfo(ipAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                setSearching(true)
            }
            val ipInfo = ipInfoUseCase(ipAddress)
            launch(Dispatchers.Main) {
                setSearching(false)
                updateData(ipInfo)
            }
        }
    }
}