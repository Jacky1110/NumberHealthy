package com.jotangi.NumberHealthy.ui.Member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentUsertermsBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding


class UsertermsFragment : BaseFragment() {

    private lateinit var binding: FragmentUsertermsBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsertermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("個資同意書與使用者條款")
    }
}