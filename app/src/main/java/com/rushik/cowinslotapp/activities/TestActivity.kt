package com.rushik.cowinslotapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.rushik.cowinslotapp.BuildConfig
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
import com.rushik.cowinslotapp.viewmodel.HomeViewModel
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
    private lateinit var intervalAdapter: ArrayAdapter<Int>
    private lateinit var homeViewModel: HomeViewModel
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(AppConstant.DEFAULT_DATE_FORMAT, Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)
        activityTestBinding.homeViewModel = homeViewModel
        activityTestBinding.lifecycleOwner = this

        fetchAndSetStates()

        setStatesSpinner()
        setDistrictSpinner()
        setIntervalSpinner()
        setDatePicker()

        AppCache.selectedState.nonNull().observe(this, {
            fetchAndSetDistricts(it)
            val position = AppCache.statesLiveData.value?.indexOf(it) ?: -1
            activityTestBinding.spinnerState.setSelection(position)
        })

        AppCache.selectedDistrict.nonNull().observe(this, {
            val position = AppCache.districtLiveData.value?.indexOf(it) ?: -1
            activityTestBinding.spinnerDistrcit.setSelection(position)
        })

        title = getString(R.string.app_name) + "-" + BuildConfig.VERSION_NAME
    }

    fun onStartService(view: View) {
        val selectedState = AppCache.selectedState.value
        val selectedDistrict = AppCache.selectedDistrict.value
        if (selectedState == null || selectedDistrict == null) {
            ToastWrp.error(applicationContext, "Please Select All Above Fields")
            return
        }

        AlertDialog.Builder(this)
            .setMessage("Are you want to Subscribe for ${selectedDistrict.districtName} : ${AppCache.selectedDateString.value}")
            .setPositiveButton("Yes") { dialog, which ->

                if (AppCache.isServiceRunningLiveData.value == false) {
                    startService(Intent(applicationContext, SlotCheckService::class.java))
                    AppCache.isServiceRunningLiveData.postValue(true)
                }

                dialog.dismiss()
            }.setNegativeButton("No", null).create().show()
    }

    fun onStopService(view: View) {
        val selectedDistrict = AppCache.selectedDistrict.value

        AlertDialog.Builder(this)
            .setMessage("Are you want to UnSubscribe for ${selectedDistrict?.districtName} : ${AppCache.selectedDateString.value}")
            .setPositiveButton("Yes") { dialog, which ->

                stopService(Intent(applicationContext, SlotCheckService::class.java))
                AppCache.isServiceRunningLiveData.postValue(false)

                dialog.dismiss()
            }.setNegativeButton("No", null).create().show()
    }

    private fun setIntervalSpinner() {
        intervalAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item, AppCache.timeIntervals)
        activityTestBinding.spinnerInterval.adapter = intervalAdapter
        activityTestBinding.spinnerInterval.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    AppCache.checkSlotsIntervalLiveData.postValue(AppCache.timeIntervals[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AppCache.checkSlotsIntervalLiveData.postValue(AppCache.timeIntervals[0])
                }
            }
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
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()
        activityTestBinding.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { view, year, monthOfYear, dayOfMonth ->
            val calendar1 = Calendar.getInstance()
            calendar1.set(year, monthOfYear, dayOfMonth)

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
                activityTestBinding.datePicker.updateDate(
                    calendar1.get(Calendar.YEAR),
                    calendar1.get(Calendar.MONTH),
                    calendar1.get(Calendar.DAY_OF_MONTH)
                )
            } catch (e: Exception) {
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