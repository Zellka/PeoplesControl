package com.example.peoplesontrol.ui.view.appeal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentDetailAppealBinding

class DetailAppealFragment : Fragment() {

    private var _binding: FragmentDetailAppealBinding? = null
    private val binding get() = _binding!!

    private var appeal: Appeal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        arguments?.let {
            appeal = it.getParcelable(APPEAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAppealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setupImages()
        binding.btnShowMap.setOnClickListener {
            val dialogMapFragment = DialogMapFragment()
            dialogMapFragment.show(childFragmentManager, "MAP")
        }
    }

    private fun setData() {
        binding.statusAppeal.text = appeal?.status
        binding.idAppeal.text = "№" + appeal?.id.toString()
        binding.descriptionAppeal.text = appeal?.description
        binding.addressAppeal.text = appeal?.address
        binding.dateAppeal.text = appeal?.date
        binding.numAppeal.text = appeal?.numAppeal.toString()
        if (appeal?.media != "") {
            binding.videoAppeal.visibility = View.VISIBLE
            binding.titleVideo.visibility = View.VISIBLE
            binding.videoAppeal.text = appeal?.media
        }
    }

    private fun setupImages() {
        val images = intArrayOf(
            R.drawable.transport_1,
            R.drawable.road_2, R.drawable.city_3
        )
        var position = 0
        binding.imagesAppeal.setFactory {
            val imgView = ImageView(this.requireContext())
            imgView.scaleType = ImageView.ScaleType.FIT_CENTER
            imgView.setPadding(8, 8, 8, 8)
            imgView
        }

        binding.imagesAppeal.setImageResource(images[position])
        binding.imagesAppeal.inAnimation =
            AnimationUtils.loadAnimation(this.requireContext(), android.R.anim.slide_in_left)
        binding.imagesAppeal.outAnimation =
            AnimationUtils.loadAnimation(this.requireContext(), android.R.anim.slide_out_right)
        binding.btnPrev.setOnClickListener {
            position = if (position - 1 >= 0) position - 1 else 2
            binding.imagesAppeal.setImageResource(images[position])
        }
        binding.btnNext.setOnClickListener {
            position = if (position + 1 < images.size) position + 1 else 0
            binding.imagesAppeal.setImageResource(images[position])
        }
    }

    companion object {
        const val APPEAL = "appeal"
    }
}