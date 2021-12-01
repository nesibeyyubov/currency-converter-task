package com.example.currencyconverter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ItemCurrencyBinding
import com.example.currencyconverter.model.Currency

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {
    private val currencyList = mutableListOf<Currency>()
    var OnItemClickListener: ((Currency) -> Unit)? = null

    fun setData(newCurrencyList: List<Currency>) {
        currencyList.clear()
        currencyList.addAll(newCurrencyList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCurrencyBinding.bind(view)
        fun bindData(currency: Currency) {
            binding.apply {
                this.root.setOnClickListener { OnItemClickListener?.let { it(currency) } }
                tvCurrencyName.text = "(${currency.name})"
                tvCurrencyCode.text = currency.code
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(currencyList[position])
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }
}