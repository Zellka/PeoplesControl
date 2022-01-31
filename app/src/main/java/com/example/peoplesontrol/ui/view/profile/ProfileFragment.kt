package com.example.peoplesontrol.ui.view.profile

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Profile
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.data.model.TokenData
import com.example.peoplesontrol.databinding.FragmentProfileBinding
import com.example.peoplesontrol.ui.adapter.RequestAdapter
import com.example.peoplesontrol.ui.view.login.LoginActivity
import com.example.peoplesontrol.ui.view.request.DetailRequestFragment.Companion.REQUEST
import com.example.peoplesontrol.ui.view.request.RequestFragment
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModel
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private lateinit var adapter: RequestAdapter
    private var list: List<Request> = listOf()
    private var profile: Profile? = null

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
        setupViewModel()
        setupUI()
        getProfile()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this.requireContext()))
            )
        )[ProfileViewModel::class.java]
    }


    private fun setupUI() {
        binding.rvRequests.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RequestAdapter { appeal: Request, isAdd: Boolean -> showRequest(appeal, isAdd) }
        adapter.setData(arrayListOf())
        binding.rvRequests.adapter = adapter
        setupSwipe()
    }

    private fun getProfile() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.getProfile().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.rvRequests.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { profile -> setProfile(profile) }
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this.requireContext(),
                                resource.message,
                                Toast.LENGTH_LONG
                            ).show()
                            if (resource.message?.contains("401") == true) {
                                refreshToken()
                            } else {
                                Error.showError(this.requireActivity())
                            }
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvRequests.visibility = View.GONE
                        }
                    }
                }
            })
        } else {
            viewModel.getProfileFromDB().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.rvRequests.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { profile -> setProfile(profile) }
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Error.showError(this.requireActivity())
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvRequests.visibility = View.GONE
                        }
                    }
                }
            })
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun setProfile(profile: Profile) {
        this.profile = profile
        binding.userName.text = profile.full_name
        binding.userNumber.text = profile.phone
        if (profile.requests.isNotEmpty()) {
            adapter.apply {
                list = profile.requests.filter { it.deleted_at == null }
                setData(list)
                notifyDataSetChanged()
            }
        }
    }

    private fun showRequest(request: Request, isAddRequest: Boolean) {
        if (isAddRequest) {
            val bundle =
                bundleOf(REQUEST to request, RequestFragment.IS_ADD_TO_REQUEST to isAddRequest)
            findNavController().navigate(R.id.action_profileFragment_to_editRequestFragment, bundle)
        } else {
            val bundle = bundleOf(REQUEST to request)
            findNavController().navigate(
                R.id.action_profileFragment_to_detailRequestFragment,
                bundle
            )
        }
    }


    private fun editRequest(request: Request) {
        val bundle = bundleOf(REQUEST to request)
        findNavController().navigate(R.id.action_profileFragment_to_editRequestFragment, bundle)
    }

    private fun removeRequest(requestId: Long) {
        if (Network.isConnected(this.requireActivity())) {
            if (view?.parent != null) {
                viewModel.deleteRequest(requestId).observe(this.viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this.requireContext(),
                                    resources.getString(R.string.success_remove),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            Status.ERROR -> {
                                if (resource.message?.contains(resources.getString(R.string.error_401)) == true) {
                                    refreshToken()
                                } else {
                                    Error.showError(this.requireActivity())
                                }
                            }
                        }
                    }
                })
            }
        } else {
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun refreshToken() {
        viewModel.refreshToken().observe(this.viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokens -> setTokens(tokens) }
                        val sharedPreference = this.requireContext()
                            .getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.clear()
                        editor.putString(
                            resources.getString(R.string.token_name),
                            Data.token.refreshToken
                        )
                        editor.apply()
                        Toast.makeText(
                            this.requireContext(),
                            resources.getString(R.string.try_again),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.ERROR -> {
                        startActivity(Intent(this.requireContext(), LoginActivity::class.java))
                    }
                }
            }
        })
    }

    private fun setTokens(tokens: TokenData) {
        Data.token = tokens
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
                    removeRequest(list[position].requestId)
                    adapter.removeItem(position)
                } else {
                    editRequest(list[position])
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
        itemTouchHelper.attachToRecyclerView(binding.rvRequests)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val bundle = bundleOf(PROFILE to profile)
        if (id == R.id.action_edit) {
            findNavController().navigate(
                (R.id.action_profileFragment_to_editProfileFragment),
                bundle
            )
        }
        if (id == R.id.action_archive) {
            findNavController().navigate(
                (R.id.action_profileFragment_to_archiveFragment),
                bundle
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PROFILE = "PROFILE"
    }
}