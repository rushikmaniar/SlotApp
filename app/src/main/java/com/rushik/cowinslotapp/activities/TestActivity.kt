package com.rushik.cowinslotapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rushik.cowinslotapp.R
import com.rushik.cowinslotapp.data.localdatabase.providers.ApiProvider
import com.rushik.cowinslotapp.databinding.ActivityTestBinding
import com.rushik.cowinslotapp.domain.AppDomain
import com.rushik.cowinslotapp.frameworks.AppCache
import com.rushik.cowinslotapp.frameworks.AppConstant
import com.rushik.cowinslotapp.frameworks.ToastWrp
import com.rushik.cowinslotapp.frameworks.nonNull
import com.rushik.cowinslotapp.models.State
import com.rushik.cowinslotapp.services.SlotCheckService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TestActivity : AppCompatActivity() {
    private lateinit var activityTestBinding: ActivityTestBinding
    private var appDomain: AppDomain = AppDomain(ApiProvider())
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var districtAdapter: ArrayAdapter<String>
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(AppConstant.DEFAULT_DATE_FORMAT, Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        fetchAndSetStates()

        setStatesSpinner()
        setDistrictSpinner()
        setDatePicker()

        AppCache.isServiceRunning.observe(this, {
            activityTestBinding.startBtn.isEnabled = !it
            activityTestBinding.stopBtn.isEnabled = it
            activityTestBinding.spinnerState.isEnabled = !it
            activityTestBinding.spinnerDistrcit.isEnabled = !it
            activityTestBinding.datePicker.isEnabled = !it
        })

    }

    fun onStartService(view: View) {
        val selectedState = AppCache.selectedState.value
        val selectedDistrict = AppCache.selectedState.value
        if (selectedState == null || selectedDistrict == null) {
            ToastWrp.error(applicationContext, "Please Select All Above Fileds")
            return
        }

        if (AppCache.isServiceRunning.value == false) {
            startService(Intent(applicationContext, SlotCheckService::class.java))
            AppCache.isServiceRunning.postValue(true)
        }
    }

    fun onStopService(view: View) {
        stopService(Intent(applicationContext, SlotCheckService::class.java))
        AppCache.isServiceRunning.postValue(false)
    }

    private fun setStatesSpinner() {
        stateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListOf())
        activityTestBinding.spinnerState.adapter = stateAdapter
        activityTestBinding.spinnerState.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val states = AppCache.statesLiveData.value ?: arrayListOf()
                    AppCache.selectedState.postValue(states[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AppCache.selectedState.postValue(null)
                }
            }

        AppCache.statesLiveData.nonNull().observe(this, { states ->
            stateAdapter.clear()
            stateAdapter.addAll(states.map { it.stateName })
            stateAdapter.notifyDataSetChanged()
        })
    }

    private fun setDistrictSpinner() {
        districtAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListOf())
        activityTestBinding.spinnerDistrcit.adapter = districtAdapter
        activityTestBinding.spinnerDistrcit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val districts = AppCache.districtLiveData.value ?: arrayListOf()
                    AppCache.selectedDistrict.postValue(districts[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AppCache.selectedDistrict.postValue(null)
                }
            }

        AppCache.districtLiveData.nonNull().observe(this, { districts ->
            districtAdapter.clear()
            districtAdapter.addAll(districts.map { it.districtName })
            districtAdapter.notifyDataSetChanged()
        })

        AppCache.selectedState.nonNull().observe(this, {
            fetchAndSetDistricts(it)
        })
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()
        activityTestBinding.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { view, year, monthOfYear, dayOfMonth ->
            val calendar1 = Calendar.getInstance()
            calendar1.set(year,monthOfYear,dayOfMonth)

            val dateString = simpleDateFormat.format(Date.from(calendar1.toInstant()))
            val selectedDate = AppCache.selectedDateString.value
            if (selectedDate != null) {
                if (dateString != selectedDate) {
                    AppCache.selectedDateString.postValue(dateString)
                }
            } else {
                AppCache.selectedDateString.postValue(dateString)
            }
        }

        activityTestBinding.datePicker.minDate = Date().time
        AppCache.selectedDateString.nonNull().observe(this, {
            val dateString = it ?: return@observe
            val sdf = SimpleDateFormat(AppConstant.DEFAULT_DATE_FORMAT, Locale.US)
            val date: Date?
            try {
                date = sdf.parse(dateString)
                val calendar1 = Calendar.getInstance()
                calendar1.time = date
                activityTestBinding.datePicker.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH))
            }catch (e:Exception){
                e.printStackTrace()
            }
        })
    }

    fun onStateRefresh(view: View) {
        fetchAndSetStates()
    }

    fun onDistrictRefresh(view: View) {
        fetchAndSetDistricts(AppCache.selectedState.value)
    }

    private fun fetchAndSetStates() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDomain.fetchAndSetStates()
            } catch (e: Exception) {
                ToastWrp.error(applicationContext, "Unable to fetch States")
                e.printStackTrace()
            }
        }
    }

    private fun fetchAndSetDistricts(state: State?) {
        if (state == null) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDomain.fetchAndSetDistricts(stateId = state.stateId.toString())
            } catch (e: Exception) {
                ToastWrp.error(applicationContext, "Unable to fetch Districts")
                e.printStackTrace()
            }
        }
    }
}