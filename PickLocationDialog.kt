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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogPickLocationBinding.bind(view)
        binding.dialog = this
        isCancelable = false

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
                // Plus click
                doWhenPlusClicked { taskPosition ->
                    val action =
                        PickLocationDialogDirections.actionPickLocationDialogToTaskCategoriesDialog(
                            taskPosition
                        )
                    MainActivity.navController.navigate(action)
                }

                // search location click
                doWhenPickOrDropLocationClicked { fromPickLocation, taskPosition ->
                    Log.d(
                        "ClickCheck",
                        "setupRecyclerView: doWhenPickOrDropLocationClicked  called"
                    )

                    val action =
                        PickLocationDialogDirections.actionPickLocationDialogToSearchLocationFragment(
                            fromPickLocation,
                            taskPosition
                        )
                    this@PickLocationDialog.pos = taskPosition
                    Log.d("TAGp1", "setupRecyclerView: $taskPosition")


                    list?.let {
                        parentFragmentManager.setFragmentResult(
                            "req_key_1_2",
                            bundleOf(
                                "list_key_1_2" to list,
                                "loc_position" to taskPosition
                            )
                        )
                        Log.d("Balti0", "onViewCreated: $list")
                    } ?: run {
                        list = ArrayList()
                        parentFragmentManager.setFragmentResult(
                            "req_key_1_2",
                            bundleOf(
                                "list_key_1_2" to list,
                                "loc_position" to taskPosition
                            )
                        )
                    }

//                    list?.let {
//                        dataViewModel.list = list
//                    } ?: run {
//                        list = ArrayList()
//                        dataViewModel.list = list
//                    }


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

//        if (!dataViewModel.isAddedOneMore) {
//            dataViewModel.isAddedOneMore = true
//            list?.add(
//                TaskDetailJsonLocationRequest(
//                    pickupLocation = "Please select location", null, null
//                )
//            )
//        }
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