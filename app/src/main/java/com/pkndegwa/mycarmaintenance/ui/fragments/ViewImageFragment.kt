package com.pkndegwa.mycarmaintenance.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pkndegwa.mycarmaintenance.databinding.FragmentViewImageBinding

/**
 * [ViewImageFragment] allows a user to view an Image in fullscreen.
 */
class ViewImageFragment : Fragment() {
    private var _binding: FragmentViewImageBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ViewImageFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receiptImageUriString = navigationArgs.imageUriString
        val receiptImageUri = Uri.parse(receiptImageUriString)
        binding.receiptImageView.setImageURI(receiptImageUri)
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}