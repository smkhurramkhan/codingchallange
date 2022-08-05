package com.example.codingchallange

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.codingchallange.databinding.ActivityMainBinding
import com.example.codingchallange.details.model.SchoolDetails
import com.example.codingchallange.main.adapter.SchoolsAdapter
import com.example.codingchallange.main.model.Schools
import com.example.codingchallange.main.viewmodel.SchoolsViewModel
import com.example.codingchallange.utils.ExtensionFunctions.hide
import com.example.codingchallange.utils.ExtensionFunctions.show
import com.example.codingchallange.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private var schoolsAdapter: SchoolsAdapter? = null
    private var mSchools: Schools? = null
    private var schoolDetails: SchoolDetails? = null
    private val viewModel: SchoolsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        getAllSchools()

    }

    private fun getAllSchools() {
        viewModel.getSchoolDetails().observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    mainBinding.progressBar.show()

                }
                Status.SUCCESS -> {
                    mainBinding.progressBar.hide()
                    schoolDetails = it.data

                    Timber.d("data received from api is ${mSchools?.size}")
                    schoolsAdapter = SchoolsAdapter(
                        context = this,
                        mSchools = schoolDetails,
                        onClick = {position->
                            Toast.makeText(
                                this,
                                schoolDetails?.get(position)?.school_name,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    mainBinding.schoolsRecycler.adapter = schoolsAdapter
                }
                Status.ERROR -> {
                    mainBinding.progressBar.hide()
                    Timber.d("Error is ${it.message}")
                }
            }
        }
    }
}