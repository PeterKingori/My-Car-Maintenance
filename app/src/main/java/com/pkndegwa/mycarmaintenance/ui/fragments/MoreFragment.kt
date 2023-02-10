package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pkndegwa.mycarmaintenance.databinding.FragmentMoreBinding

/**
 * Use the [MoreFragment] to add Settings and more options for the user.
 */
class MoreFragment : Fragment() {
    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            privacyPolicyTextView.setOnClickListener {
                val action =
                    MoreFragmentDirections.actionMoreFragmentToWebViewFragment(title = "Privacy Policy", page = 1)
                this@MoreFragment.findNavController().navigate(action)
            }

            termsOfServiceTextView.setOnClickListener {
                val action = MoreFragmentDirections.actionMoreFragmentToWebViewFragment(title = "Terms of Service",
                    page = 2)
                this@MoreFragment.findNavController().navigate(action)
            }
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}