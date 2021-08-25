package app.ticktasker.main.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.ticktasker.BaseDialogFragment
import app.ticktasker.DataViewModel
import app.ticktasker.MainActivity
import app.ticktasker.MainActivity.Companion.navController
import app.ticktasker.R
import app.ticktasker.databinding.DialogPickLocationBinding
import app.ticktasker.networking.model.request.TaskDetailJsonLocationRequest
import app.ticktasker.task.adapters.BaseTaskDialogAdapter
import app.ticktasker.task.adapters.GeneralTaskRecyclerAdapter
import app.ticktasker.task.model.GeneralTask
import app.ticktasker.task.view.PickupAndDropTaskFragmentDirections
import app.ticktasker.utils.Data
import app.ticktasker.utils.PrefManager
import app.ticktasker.viewmodels.GeneralTaskViewModel
import app.ticktasker.utils.applyInvalidAttributes
import app.ticktasker.utils.applyValidAttributes
import app.ticktasker.viewmodels.LoginViewModel

@SuppressLint("ClickableViewAccessibility")
class PickLocationDialog : BaseDialogFragment(R.layout.dialog_pick_location) {
    val viewModel: GeneralTaskViewModel by navGraphViewModels(R.id.new_general_task_nav_graph)
    var doOnViewDetailsClick: () -> Unit = {}
    var doOnCancelClick: () -> Unit = {}
    var pos: Int = 0
    var mLocation: String? = null
    var mLat: String? = null
    var mLang: String? = null
    var taskPos: Int = 0
    lateinit var binding: DialogPickLocationBinding

    // ye list declare kri
    var list: ArrayList<TaskDetailJsonLocationRequest>? = null
    private var dropLocation: String? = null
    private val vmLogin: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogPickLocationBinding.bind(view)
        binding.dialog = this
        isCancelable = false

        Log.d("isonViewCreated", "onViewCreated: called")

        val tLoc = TaskDetailJsonLocationRequest(Data.location, Data.lat, Data.lang)
        Data.tryList.add(Data.taskPosition, tLoc)

//        Log.d("TAGl0", "onViewCreated: ${Data.tryList[Data.taskPosition].pickupLocation}//${Data.taskPosition}")
//        setupRecyclerView()
//        ensureJobComplete()

        setupRecyclerView()

        parentFragmentManager.setFragmentResultListener(
            "location_info",
            viewLifecycleOwner
        ) { key, bundle ->

            list = bundle.getParcelableArrayList<TaskDetailJsonLocationRequest>("list")

            list?.let {
                Log.d("Balti1", "onViewCreated: $it")
                setupRecyclerView()
            }
        }

        ensureJobComplete()
    }

    override fun onResume() {
        super.onResume()

        vmLogin.dropLocation?.let {
            // here we will get drop location if it was initialised
            this.dropLocation = it
        }
        vmLogin.list?.let {
            list = it
            Log.d("ListCheck2", "onViewCreated: $it")
            setupRecyclerView()
        }
    }

    private fun ensureJobComplete() {
        if (viewModel.job.hasCompleteTask()) {
            binding.viewTaskDetailsButton.visibility = View.VISIBLE
            applyValidAttributes(binding.confirmButton)
        } else {
            applyInvalidAttributes(binding.confirmButton)
        }
    }

    private fun setupRecyclerView() {
        binding.tasksRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(requireActivity())  // Data.taskPosition, Data.tryList
            adapter = GeneralTaskRecyclerAdapter(
                viewModel.job, list
            ).apply {

                dropLocation?.let {
                    setDropLocation(it)
                }

                // Plus click
                doWhenPlusClicked { taskPosition ->
                    val action =
                        PickLocationDialogDirections.actionPickLocationDialogToTaskCategoriesDialog(
                            taskPosition
                        )
                    navController.navigate(action)
                }

                doWhenRemoveClicked { removedPosition ->
                    list?.removeAt(removedPosition)
                }

                // search location click
                doWhenPickOrDropLocationClicked { fromPickLocation, taskPosition ->

                    val action =
                        PickLocationDialogDirections.actionPickLocationDialogToSearchLocationFragment(
                            fromPickLocation,
                            taskPosition
                        )
                    this@PickLocationDialog.pos = taskPosition
                    Log.d("TAGp1", "setupRecyclerView: $taskPosition")

                    if (taskPosition == -1) {
                        // this is drop location click callback
                    }

                    vmLogin.list = null

                    if (list == null) {
                        list = ArrayList()
//                        for (i in 0 until taskPosition) {
//                            list!!.add(
//                                TaskDetailJsonLocationRequest(
//                                    pickupLocation = "Please select pickup location",
//                                    null, null
//                                )
//                            )
//                        }
                    }

                    Log.d("Balti0", "onViewCreated: $list  || size: ${list?.size}")

                    parentFragmentManager.setFragmentResult(
                        "req_key_1_2",
                        bundleOf(
                            "list_key_1_2" to list,
                            "loc_position" to taskPosition
                        )
                    )

                    MainActivity.navController.navigate(action)
                }

                // task details
                doWhenTaskDetailsClicked { taskPosition ->
                    Log.d("ClickCheck", "setupRecyclerView: doWhenTaskDetailsClicked  called")
                    val task = (viewModel.getTask(taskPosition) as GeneralTask)
                    val action =
                        PickLocationDialogDirections.actionPickLocationDialogToAddTaskFragment(
                            task.category!!,
                            task.categoryDrawableRes!!,
                            taskPosition
                        )
                    navController.navigate(action)
                }
            }
        }
    }

    //when confirm is clicked
    fun showReviewAndAgreeDialog() {
        navController.navigateUp() // go back
        val action =
            PickupAndDropTaskFragmentDirections.actionNewTaskFragmentToReviewAndAgreeDialog()
        navController.navigate(action)
    }

    // when plus icon is clicked
    fun addOneMoreTask() {
        Log.d("ClickCheck", "setupRecyclerView: addOneMoreTask  called")
        viewModel.addedDetailsForOneTaskMinimum.value = false
        val adapter = binding.tasksRecyclerview.adapter as BaseTaskDialogAdapter
        adapter.addTask()
        ensureJobComplete()

        list?.add(
            TaskDetailJsonLocationRequest(
                pickupLocation = "Please select location", null, null
            )
        )
    }

    //when view task details is clicked
    fun showTaskDetailsScreen() {
        val action =
            PickLocationDialogDirections.actionPickLocationDialogToAllTasksDetailsFragment()
        findNavController().navigate(action)
    }

    //when cancel is clicked
    fun goToHomeFragment() {
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    override fun onStop() {
        super.onStop()
        PrefManager.putString("location", "BookTaskerLocation")
    }
}