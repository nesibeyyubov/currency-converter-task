package com.example.currencyconverter.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.currencyconverter.databinding.FragmentHomeBinding
import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.viewmodels.HomeViewModel
import com.example.currencyconverter.utils.Constants
import com.example.currencyconverter.utils.DataState
import java.util.*
import kotlin.concurrent.timerTask


class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var numberButtons: List<TextView>
    private var currentNumber = "0"
    private var buttonTouchMoved = false
    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setOnClickListeners()
        subscribeObservers()
        homeViewModel.getCurrencies()
        setupRequestInterval()
    }

    private fun setupRequestInterval() {
        Timer().schedule(timerTask {
            homeViewModel.getCurrencies()
        }, 0, 1000)
    }

    private fun subscribeObservers() {
        homeViewModel.exchangeResult.observe(viewLifecycleOwner) {
            binding.tvExchangeResult.text = it
        }
        homeViewModel.selectedFromCurrency.observe(viewLifecycleOwner) {
            binding.tvCurrencyFrom.text = it
            binding.tvExchangeResult.text = "0.0"
        }

        homeViewModel.selectedToCurrency.observe(viewLifecycleOwner) {
            binding.tvCurrencyTo.text = it
            binding.tvExchangeCurrency.text = it
            binding.tvExchangeResult.text = "0.0"
        }

        homeViewModel.currencies.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    binding.apply {
                        pbHomeLoading.visibility = View.GONE
                        llHomeContent.visibility = View.VISIBLE
                    }
                }
                is DataState.Error -> {
                    binding.apply {
                        pbHomeLoading.visibility = View.GONE
                        llHomeContent.visibility = View.INVISIBLE
                    }
                }
                is DataState.Loading -> {
                    binding.apply {
                        pbHomeLoading.visibility = View.VISIBLE
                        llHomeContent.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }


    private fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                0
            )
        }
    }

    private fun navigateCurrenciesFragment(currencyType: String) {
        val args =
            Bundle().apply { putString(Constants.KEY_CURRENCY_TYPE, currencyType) }
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left).replace(
            R.id.fl_main,
            CurrencyFragment::class.java,
            args
        ).commit()
    }

    private fun setOnClickListeners() {
        binding.ivSwapCurrencies.setOnClickListener {
            homeViewModel.swapCurrencies()
            homeViewModel.calculateExchangeResult(currentNumber.toDouble())
        }
        binding.llCurrencyFrom.setOnClickListener {
            navigateCurrenciesFragment(Constants.VALUE_CURRENCY_FROM)
        }
        binding.llCurrencyTo.setOnClickListener {
            navigateCurrenciesFragment(Constants.VALUE_CURRENCY_TO)
        }
        binding.etAmount.addTextChangedListener {
            hideSoftKeyboard()
            if (currentNumber.isNotEmpty()) {
                Log.d("mytag2", "text changed: $currentNumber")
                homeViewModel.calculateExchangeResult(currentNumber.toDouble())
            }
        }

        binding.apply {
            numberButtons = listOf(
                tvNumber0,
                tvNumber1,
                tvNumber2,
                tvNumber3,
                tvNumber4,
                tvNumber5,
                tvNumber6,
                tvNumber7,
                tvNumber8,
                tvNumber9,
                tvNumberDelete,
                tvNumberDot,
            )

        }

        numberButtons.forEach {
            it.setOnTouchListener { v, event ->
                onNumberTouched(event, v)
                true
            }
            it.setOnClickListener(this)
        }
    }

    private fun onNumberTouchUp(v: View, buttonTouchMoved: Boolean) {
        v.setBackgroundResource(R.drawable.keyboard_item_unpressed_bg)
        (v as TextView).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        if (!buttonTouchMoved) {
            v.performClick()
        }

    }

    private fun onNumberTouchDown(v: View) {
        v.setBackgroundResource(R.drawable.keyboard_item_pressed_bg)
        (v as TextView).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }


    override fun onClick(v: View?) {
        onNumberClicked(v?.id ?: 0)
    }


    private fun onNumberClicked(viewId: Int) {
        if (currentNumber == "0" && viewId != R.id.tv_number_delete) {
            currentNumber = ""
            binding.etAmount.text.clear()
        }
        when (viewId) {
            R.id.tv_number_0 -> {
                currentNumber += "0"
            }
            R.id.tv_number_1 -> {
                currentNumber += "1"
            }
            R.id.tv_number_2 -> {
                currentNumber += "2"
            }
            R.id.tv_number_3 -> {
                currentNumber += "3"
            }
            R.id.tv_number_4 -> {
                currentNumber += "4"
            }
            R.id.tv_number_5 -> {
                currentNumber += "5"
            }
            R.id.tv_number_6 -> {
                currentNumber += "6"
            }
            R.id.tv_number_7 -> {
                currentNumber += "7"
            }
            R.id.tv_number_8 -> {
                currentNumber += "8"
            }
            R.id.tv_number_9 -> {
                currentNumber += "9"
            }
            R.id.tv_number_dot -> {
                currentNumber += "."
            }
            R.id.tv_number_delete -> {
                if (currentNumber.isNotEmpty()) {
                    currentNumber = currentNumber.substring(0..currentNumber.length - 2)
                    if (currentNumber.isEmpty()) {
                        currentNumber += "0"
                    }
                }
            }
        }
        binding.etAmount.setText(currentNumber)


    }

    private fun onNumberTouched(event: MotionEvent, view: View) {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                onNumberTouchUp(view, buttonTouchMoved)
                buttonTouchMoved = false
            }
            MotionEvent.ACTION_MOVE -> {
                buttonTouchMoved = true
            }
            MotionEvent.ACTION_DOWN -> {
                onNumberTouchDown(view)
            }
        }
    }

    private fun clearPassword() {
        currentNumber = ""
    }
}