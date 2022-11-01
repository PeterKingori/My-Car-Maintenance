package com.pkndegwa.mycarmaintenance.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddServiceBinding
import com.pkndegwa.mycarmaintenance.viewmodels.ServicesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.ServicesViewModelFactory

/**
 * [AddServiceFragment] allows a user to add a new service that has been performed to the vehicle.
 */
class AddServiceFragment : Fragment() {
    private var _binding: FragmentAddServiceBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ServicesViewModel by activityViewModels {
        ServicesViewModelFactory((activity?.application as CarMaintenanceApplication).database.serviceDao())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment.
        _binding = FragmentAddServiceBinding.inflate(inflater, container, false)
        return binding.root
    }



    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}