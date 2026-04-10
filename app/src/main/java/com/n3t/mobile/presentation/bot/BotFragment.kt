package com.n3t.mobile.presentation.bot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.databinding.FragmentBotBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BotFragment : Fragment() {

    private var _binding: FragmentBotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BotViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.handleLlmMessage(text)
                binding.etMessage.text?.clear()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.botModel.collect { model ->
                        model?.response?.let { response ->
                            binding.tvResponse.text = response
                            binding.tvResponse.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    viewModel.botInteractionState.collect { state ->
                        when (state) {
                            InteractionState.LISTENING -> binding.tvStatus.text = "Đang nghe..."
                            InteractionState.PROCESSING -> binding.tvStatus.text = "Đang xử lý..."
                            InteractionState.RESPONDING -> binding.tvStatus.text = "Đang trả lời..."
                            else -> binding.tvStatus.text = ""
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
