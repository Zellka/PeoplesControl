package com.example.peoplesontrol.ui.view.profile

import android.graphics.*
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentProfileBinding
import com.example.peoplesontrol.ui.adapter.AppealAdapter
import com.example.peoplesontrol.ui.view.appeal.DetailAppealFragment
import kotlinx.android.synthetic.main.layout_error.view.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AppealAdapter
    private var list = listOf<Appeal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = listOf(
            Appeal(
                131,
                "Петрова Ольга Николаевна",
                "Авария",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                14
            ),
            Appeal(
                146,
                "Петрова Ольга Николаевна",
                "Авария",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                4,
                1
            )
        )

        binding.rvAppeals.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = AppealAdapter { appeal: Appeal -> showAppeal(appeal) }
        adapter.setData(list)
        binding.rvAppeals.adapter = adapter
        setupSwipe()
    }


    private fun showAppeal(appeal: Appeal) {
        val bundle = bundleOf(DetailAppealFragment.APPEAL to appeal)
        findNavController().navigate(R.id.action_profileFragment_to_detailAppealFragment, bundle)
    }

    private fun editAppeal(appeal: Appeal) {
        val bundle = bundleOf(DetailAppealFragment.APPEAL to appeal)
        findNavController().navigate(R.id.action_profileFragment_to_newAppealFragment, bundle)
    }

    private fun showErrorMessage() {
        val layout: View = activity?.layoutInflater!!.inflate(R.layout.layout_error, null)
        val text = layout.findViewById<View>(R.id.text_error) as TextView
        val img = layout.img_error
        text.text = "Ошибка сервера"
        text.width = 900
        img.setImageResource(R.drawable.ic_error)
        Toast(activity).apply {
            duration = Toast.LENGTH_LONG
            this.view = layout
            setGravity(Gravity.TOP, 0, 0)
        }.show()
    }

    private fun setupSwipe() {
        val point = Paint()
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position)
                } else {
                    removeView()
                    editAppeal(list[position])
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                point.color = Color.WHITE
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, point)
                        icon = ContextCompat.getDrawable(
                            this@ProfileFragment.requireContext(),
                            R.drawable.ic_edit
                        )?.toBitmap()!!
                        c.drawBitmap(
                            icon, null, RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            ), point
                        )
                    } else {
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, point)
                        icon =
                            ContextCompat.getDrawable(
                                this@ProfileFragment.requireContext(),
                                R.drawable.ic_delete
                            )?.toBitmap()!!
                        c.drawBitmap(
                            icon, null, RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            ), point
                        )
                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAppeals)
    }

    private fun removeView() {
        if (view?.parent != null) {
            (view?.parent as ViewGroup).removeView(view)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_archive) {
            findNavController().navigate((R.id.action_profileFragment_to_archiveFragment))
        }
        if (id == R.id.action_edit) {
            findNavController().navigate((R.id.action_profileFragment_to_editProfileFragment))
        }
        return super.onOptionsItemSelected(item)
    }
}