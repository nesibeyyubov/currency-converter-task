package com.example.currencyconverter.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentCurrencyBinding
import com.example.currencyconverter.ui.adapters.CurrenciesAdapter
import com.example.currencyconverter.ui.viewmodels.CurrencyViewModel
import com.example.currencyconverter.ui.viewmodels.HomeViewModel
import com.example.currencyconverter.utils.Constants
import com.example.currencyconverter.utils.DataState


class CurrencyFragment : Fragment(R.layout.fragment_currency) {
    private lateinit var binding: FragmentCurrencyBinding
    private val homeViewModel: HomeViewModel by viewModels({requireActivity()})
    private val currencyAdapter by lazy { CurrenciesAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)
        setupClickListeners()
        setupRecyclerView()
        subscribeObservers()
        homeViewModel.getCurrencies()
    }

    private fun setupRecyclerView() {
        currencyAdapter.OnItemClickListener = { selectedCurrency ->
            val args = arguments?.getString(Constants.KEY_CURRENCY_TYPE)
            args?.let {
                homeViewModel.setCurrencyType(args, selectedCurrency.code)
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, HomeFragment()).commit()

        }
        binding.apply {
            rvCurrencies.apply {
                adapter = currencyAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun subscribeObservers() {
        homeViewModel.currencies.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    currencyAdapter.setData(it.data)
                    binding.pbCurrencies.visibility = View.GONE
                }
                is DataState.Error -> {
                    binding.pbCurrencies.visibility = View.GONE
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fl_main,
                HomeFragment()
            ).commit()
        }
    }


}