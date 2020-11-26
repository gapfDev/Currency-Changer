package com.alxdev.two.moneychanger.ui.changer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.alxdev.two.moneychanger.adapters.IRecyclerViewAdapter
import com.alxdev.two.moneychanger.databinding.FragmentChangerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class ChangerFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentChangerBinding
    @Inject lateinit var changerViewModel: ChangerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewDataBinding.viewModel?.let {
            val adapter = IRecyclerViewAdapter(it)
            viewDataBinding.historyRecycleView.apply {
                this.adapter = adapter
            }
        }

    }
}
