package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.ProgressBar
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

    lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        val pageId = navigationArgs.page
        val webView = binding.webView
        webView.webViewClient = WebViewClient()

        if (pageId == 1) {
            try {
                webView.loadUrl(PRIVACY_POLICY)
            } catch (e: Exception) {
                binding.textView.text = e.message
            }

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

    private inner class WebViewClient: android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }
}