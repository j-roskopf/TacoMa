package com.example.joeroskopf.resume.ui.favorite.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.db.TacoEntity
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.ui.main.MainViewModel
import kotlinx.android.synthetic.main.favorite_row_item.view.*

class FavoritesAdapter(private val onItemClickListener: OnItemClickListener, private val tacos: List<TacoEntity>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(tacoResponse: TacoResponse)
        fun onItemCloseClicked(tacoEntity: TacoEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.favorite_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tacoHeaderText.text = MainViewModel.getHeadingFromTaco(tacos[position].toTacoResponse())
        holder.tacoHeaderCloseButton.setOnClickListener {
            onItemClickListener.onItemCloseClicked(tacos[position])
        }
        holder.tacoRowItemBaseLayout.setOnClickListener {
            onItemClickListener.onItemClick(tacos[position].toTacoResponse())
        }
    }

    // Gets the number of tacos in the list
    override fun getItemCount(): Int {
        return tacos.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tacoHeaderText = view.tacoRowItemText as TextView
    val tacoHeaderCloseButton = view.tacoRowItemCloseButton as ImageView
    val tacoRowItemBaseLayout = view.tacoRowItemBaseLayout as ConstraintLayout
}