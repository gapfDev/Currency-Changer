package com.alxdev.two.moneychanger.ui.changer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alxdev.two.moneychanger.adapters.IRecyclerViewAdapter
import com.alxdev.two.moneychanger.databinding.FragmentChangerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class ChangerFragment : Fragment() {
    @Inject
    lateinit var changerViewModel: ChangerViewModel
    private lateinit var viewDataBinding: FragmentChangerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentChangerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChangerFragment.viewLifecycleOwner
            viewModel = changerViewModel
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
