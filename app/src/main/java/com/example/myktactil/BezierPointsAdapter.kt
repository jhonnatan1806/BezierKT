package com.example.myktactil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BezierPointsAdapter(
    private val puntosList: MutableList<Nodo>,
    private val onDeleteClick: (Nodo) -> Unit
) : RecyclerView.Adapter<BezierPointsAdapter.BezierPointsViewHolder>() {

    class BezierPointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pointName: TextView = itemView.findViewById(R.id.tvPointName)
        val pointX: TextView = itemView.findViewById(R.id.tvPointX)
        val pointY: TextView = itemView.findViewById(R.id.tvPointY)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BezierPointsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bezier_point, parent, false)
        return BezierPointsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BezierPointsViewHolder, position: Int) {
        val nodo = puntosList[position]

        holder.pointName.text = nodo.nombre
        holder.pointX.text = "X: ${nodo.x}"
        holder.pointY.text = "Y: ${nodo.y}"

        // Configura el botón de eliminar
        holder.deleteButton.setOnClickListener {
            onDeleteClick(nodo) // Llama a la función que elimina el nodo
        }
    }

    override fun getItemCount(): Int {
        return puntosList.size
    }
}
