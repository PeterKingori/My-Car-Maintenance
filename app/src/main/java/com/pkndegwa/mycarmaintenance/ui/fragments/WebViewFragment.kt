package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pkndegwa.mycarmaintenance.databinding.FragmentWebViewBinding

/**
 * The [WebViewFragment] shows web pages inside the application.
 */

const val PRIVACY_POLICY = "https://sites.google.com/view/mycarmaintenanceprivacypolicy/home"
const val TERMS_OF_SERVICE = "https://sites.google.com/view/mycarmaintenancetermsofservice/documentation"

class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: WebViewFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val pageId = navigationArgs.page

        if (pageId == 1) {
            binding.webView.loadUrl(PRIVACY_POLICY)
        }

        if (pageId == 2) {
            binding.webView.loadUrl(TERMS_OF_SERVICE)
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