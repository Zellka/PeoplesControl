package com.example.peoplesontrol.ui.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.FragmentDialogRequestBinding

class DialogRequestFragment : DialogFragment() {

    private var _binding: FragmentDialogRequestBinding? = null
    private val binding get() = _binding!!

    private var request: Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getParcelable(MAP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.idRequest.text = "№" + request?.requestId.toString()
        binding.descriptionRequest.text = request?.description
        if(request?.problem_categories?.isEmpty() == true) {
            binding.nameRequest.text = resources.getString(R.string.input_category)
        } else {
            binding.nameRequest.text = request?.problem_categories?.get(0)?.title
        }
        binding.addressRequest.text = request?.location
    }

    companion object {
        const val MAP = "MAP"

        @JvmStatic
        fun newInstance(request: Request) =
            DialogRequestFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MAP, request)
                }
            }
    }
}