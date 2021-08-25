package app.ticktasker.task.adapters

import android.annotation.SuppressLint
import android.util.Log
import app.ticktasker.databinding.ItemDropLocationBinding
import app.ticktasker.databinding.ItemTaskAndLocationBinding
import app.ticktasker.networking.model.request.TaskDetailJsonLocationRequest
import app.ticktasker.task.model.GeneralTask
import app.ticktasker.task.model.Job
import app.ticktasker.utils.Data
import app.ticktasker.utils.PrefManager
import app.ticktasker.utils.doOnMotionEventUp

class GeneralTaskRecyclerAdapter(
//    private var dropLocationListener: ((position: Int) -> Unit)? = null,
    job: Job, val lList: ArrayList<TaskDetailJsonLocationRequest>?
) : BaseTaskDialogAdapter(job) {

    private var dropLocation: String? = null

    //    val pos: Int
    override fun addOneMoreTask() {
        job.tasks.add(GeneralTask())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if (holder.binding is ItemTaskAndLocationBinding) {

            lList?.let {
                holder.binding.pickupLocationEdittext.setText(lList.get(position).pickupLocation)
            } ?: run {
                holder.binding.pickupLocationEdittext.setText("pick up loc")
            }

//            if (lList.size != 0) {
//               for (i in 0 until Data.tryList.size-1) {
//                   if (pos == position) {
//                       holder.binding.pickupLocationEdittext.setText(lList.get(pos).pickupLocation)
//                       Log.d("TAGada1", "onBindViewHolder: ${lList.get(pos).pickupLocation}")
//                   }
//               }
//            } else {
//                holder.binding.pickupLocationEdittext.setText("pick up loc")
//            }

            holder.binding.plusIcon.setOnClickListener {
                doWhenPlusClicked.invoke(holder.adapterPosition)
            }

            holder.binding.addTaskText.text =
                (job.tasks[position] as GeneralTask).category ?: "Add Task"
            holder.binding.pickupLocationEdittext.hint = "Pickup Location"

            holder.binding.pickupLocationEdittext.doOnMotionEventUp {
                doWhenPickOrDropLocationClicked.invoke(true, holder.adapterPosition)
            }

            holder.binding.taskDetailsChip.setOnClickListener {
                doWhenTaskDetailsClicked.invoke(
                    holder.adapterPosition
                )
            }

        } else if (holder.binding is ItemDropLocationBinding) {
            holder.binding.dropLocationEdittext.setText(
                dropLocation ?: PrefManager.getString("location")
            )
            holder.binding.dropLocationEdittext.doOnMotionEventUp {
                doWhenPickOrDropLocationClicked.invoke(false, -1)
            }

//            holder.binding.dropLocationEdittext.setOnClickListener {
//                dropLocationListener?.invoke(holder.adapterPosition)
//            }
        }
    }

    fun setDropLocation(location: String) {
        dropLocation = location
    }

}