package com.alxdev.two.moneychanger.ui.changer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alxdev.two.moneychanger.databinding.FragmentChangerBinding


/**
 * A simple [Fragment] subclass.
 */
class ChangerFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentChangerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val changerViewModel =
            ViewModelProviders.of(this).get(ChangerViewModel::class.java)
        viewDataBinding = FragmentChangerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChangerFragment.viewLifecycleOwner
            viewModel = changerViewModel
        }

        initViewModelSubscribers()

        return viewDataBinding.root
    }

    private fun initViewModelSubscribers() {
        viewDataBinding.viewModel?.errorMessage?.observe(this,
            Observer<String>() {
                userErrorMessage(it)
            })
    }

    private fun userErrorMessage(errorMessage: String?) {
        Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show()
    }
}
