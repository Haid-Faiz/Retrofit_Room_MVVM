package app.ticktasker.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.ticktasker.DataViewModel
import app.ticktasker.R
import app.ticktasker.databinding.FragmentSearchLocationBinding
import app.ticktasker.main.dialogs.PickLocationDialog
import app.ticktasker.networking.model.places_api.PlacesApiResponse
import app.ticktasker.networking.model.places_api.PlacesResult
import app.ticktasker.networking.BaseDataSource
import app.ticktasker.networking.model.request.TaskDetailJsonLocationRequest
import app.ticktasker.task.model.Job
import app.ticktasker.task.model.Place
import app.ticktasker.task.model.Task
import app.ticktasker.utils.Data
import app.ticktasker.utils.PrefManager
import app.ticktasker.viewmodels.BaseViewModel
import app.ticktasker.viewmodels.LoginViewModel
import app.ticktasker.viewmodels.ViewModelFactory

abstract class SearchLocationFragment : Fragment(R.layout.fragment_search_location) {
    lateinit var binding: FragmentSearchLocationBinding
    lateinit var vmLogin: LoginViewModel
    abstract fun taskPosition(): Int
    abstract fun fromPickLocation(): Boolean
    lateinit var placesList: ArrayList<Place>
    lateinit var locAdapter: LocationsAdapter
    lateinit var navController: NavController
    abstract val viewModel: BaseViewModel
    var mLocation: String? = null
    var mLat: String? = null
    var mLang: String? = null

    private var list: ArrayList<TaskDetailJsonLocationRequest>? = null
    private var locPosition = 0
    private var isOnClikcked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback(this@SearchLocationFragment) {
//        }

        parentFragmentManager.setFragmentResultListener(
            "req_key_1_2",
            viewLifecycleOwner
        ) { key, bundle ->
            list = bundle.getParcelableArrayList("list_key_1_2")
            locPosition = bundle.getInt("loc_position")
            Log.d("Balti2", "onViewCreated: $list   || ${list?.size}  || Position: $locPosition")
        }

        navController = findNavController()
        vmLogin = ViewModelProvider(
            this,
            ViewModelFactory(requireContext())
        ).get(LoginViewModel::class.java)
        binding = FragmentSearchLocationBinding.bind(view)
        locAdapter = LocationsAdapter(object : LocationsAdapter.OnClickPlace {
            override fun onClick(location: String?, lat: String?, lang: String?) {

                val t = TaskDetailJsonLocationRequest(location, lat, lang)

                if (locPosition < list!!.size-1) {
                    list?.set(locPosition, t)
                } else {
                    list?.add(locPosition, t)
                }

                Log.d("Balti3", "onViewCreated: $list")
                parentFragmentManager.setFragmentResult(
                    "location_info",
                    bundleOf("list" to list)
                )

                navController.popBackStack()
            }
        })

        placesList = ArrayList()

        binding.searchQueryEdittext.doOnTextChanged { text, start, before, count ->
            text?.let { it ->
                if (it.length > 3) {
                    placesList.clear()
                    vmLogin.getPlaces(it.toString()).observe(viewLifecycleOwner, {
                        when (it.status) {
                            BaseDataSource.Resource.Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            BaseDataSource.Resource.Status.SUCCESS -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                if (it.data is PlacesApiResponse) {
                                    for (i in (it.data as PlacesApiResponse).results!!) {
                                        placesList.addAll(listOf(i))
                                    }
                                    locAdapter.locationsLists(placesList, navController)
                                    binding.recyclerview.adapter = locAdapter
                                }
                            }

                            BaseDataSource.Resource.Status.ERROR -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_LONG).show()
                                Log.d("TAGdata3", "onChanged: ${it.message}")
                            }
                        }
                    })
                } else {
                    placesList.clear()
                }
            }
        }

        binding.cutBtn.setOnClickListener {
            binding.searchQueryEdittext.text.clear()
            binding.searchQueryEdittext.text = null
        }
    }

//    override fun onStop() {
//        super.onStop()
//        val location = binding.searchQueryEdittext.text.toString()
//        if (!fromPickLocation()) {
//            // drop location
//            viewModel.job.dropLocation.locationName = location
//        } else {
//            // pick up location
//            val task = viewModel.getTask(taskPosition())
//            task.pickLocation.locationName = location
//        }
//    }
}