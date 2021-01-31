package com.alxdev.two.moneychanger.ui.changer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alxdev.two.moneychanger.ui.adapters.IRecyclerViewAdapter
import com.alxdev.two.moneychanger.databinding.FragmentChangerBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class ChangerFragment : Fragment() {

    private val changerViewModel: ChangerViewModel by viewModels()
    private lateinit var viewDataBinding: FragmentChangerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentChangerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChangerFragment.viewLifecycleOwner
            viewModel = this@ChangerFragment.changerViewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleViewAdapter()
    }

    private fun initRecycleViewAdapter() {
        viewDataBinding.viewModel?.let { _viewModel ->
            val adapter = IRecyclerViewAdapter(_viewModel)
            viewDataBinding.historyRecycleView.apply {
                this.adapter = adapter
            }
        }

    }
}
