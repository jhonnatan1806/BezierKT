package com.example.myktactil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myktactil.databinding.FragmentBezierPointsBinding

class BezierPointsFragment : Fragment() {

    private var _binding: FragmentBezierPointsBinding? = null
    private val binding get() = _binding!!
    private var nodosList: ArrayList<Nodo>? = null

    // Declaración del adaptador
    private lateinit var adapter: BezierPointsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBezierPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar la lista de nodos del Bundle
        nodosList = arguments?.getParcelableArrayList<Nodo>("nodosList")

        // Si nodosList no es nulo, configurar el RecyclerView
        nodosList?.let { nodos ->
            adapter = BezierPointsAdapter(nodos) { nodo ->
                if(nodos.size > 3){
                    eliminarNodo(nodo)
                }
                else{
                    // Mostrar mensaje de error
                    Toast.makeText(requireContext(), "Debe haber al menos 3 nodos", Toast.LENGTH_SHORT).show()
                }
            }

            // Configura el RecyclerView con el adaptador
            binding.recyclerViewBezierPoints.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewBezierPoints.adapter = adapter
        }
    }

    // Función para eliminar un nodo
    private fun eliminarNodo(nodo: Nodo) {
        nodosList?.remove(nodo)
        adapter.notifyDataSetChanged() // Asegúrate de notificar cambios al adaptador
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
