package com.example.myktactil

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myktactil.databinding.FragmentBezierBinding

class BezierFragment : Fragment() {

    private var _binding: FragmentBezierBinding? = null
    private val binding get() = _binding!!
    private val nodosList = mutableListOf<Nodo>()
    private var nodoCount = 1 // Contador de nodos para generar los nombres automáticamente
    private val bezier = Bezier()
    private var puntosInicializados = false // Para controlar si los puntos ya han sido agregados

    // Variables para el Bitmap y Canvas (deben ser globales)
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas

    // Paints globales
    private lateinit var axisPaint: Paint
    private lateinit var bezierPaint: Paint
    private lateinit var controlPointPaint: Paint

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBezierBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "PC1 - Bezier"

        // Inicializar puntos solo una vez
        if (!puntosInicializados) {
            puntosInicializados = true
        }

        // Crear el Bitmap y Canvas
        mBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        // Dibujar cuadrículas de fondo
        drawGrid(mCanvas)

        // Configurar Paints para ejes, curva y puntos de control
        axisPaint = Paint().apply {
            color = Color.parseColor("#808080") // Gris oscuro
            style = Paint.Style.STROKE
            strokeWidth = 3F
            isAntiAlias = true
        }

        // Paint para la curva de Bézier
        bezierPaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 6F
            isAntiAlias = true
        }

        // Paint para los puntos de control
        controlPointPaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // Dibujar ejes
        drawAxes(mCanvas, axisPaint)

        // Dibujar la curva de Bézier con los puntos inicializados
        drawBezierCurve(mCanvas, bezierPaint, controlPointPaint)

        // Actualizar el ImageView con el bitmap
        binding.imgView.setImageBitmap(mBitmap)

        // Configurar evento táctil para capturar nodos adicionales en el primer cuadrante
        binding.imgView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                // Obtener las coordenadas del toque
                val x = motionEvent.x
                val y = motionEvent.y

                // Ajustar las coordenadas del toque al tamaño del Bitmap
                val adjustedX = (x * mBitmap.width) / binding.imgView.width
                val adjustedY = (y * mBitmap.height) / binding.imgView.height

                // Limitar las coordenadas al primer cuadrante
                if (adjustedX >= 0 && adjustedY >= 0) {
                    val nodo = Nodo(
                        idNodo = nodoCount,
                        x = adjustedX,
                        y = mBitmap.height - adjustedY, // Invertir la coordenada Y para el Canvas
                        nombre = "nodo$nodoCount"
                    )
                    nodosList.add(nodo)
                    nodoCount++

                    // Redibujar el canvas con los nuevos puntos
                    mCanvas.drawColor(Color.WHITE)
                    drawGrid(mCanvas)
                    drawAxes(mCanvas, axisPaint)
                    drawBezierCurve(mCanvas, bezierPaint, controlPointPaint)

                    // Actualizar el ImageView con el nuevo bitmap
                    binding.imgView.setImageBitmap(mBitmap)
                }
            }
            true
        }

        // Configurar el evento de clic en el botón de puntos
        binding.btnDots.setOnClickListener {
            val bundle = Bundle()

            // Pasar la lista de nodos a través del Bundle
            bundle.putParcelableArrayList("nodosList", ArrayList(nodosList))
            findNavController().navigate(R.id.action_bezierFragment_to_bezierPointsFragment, bundle)
        }

        // Configurar el evento de clic en el botón de limpiar
        binding.btnClear.setOnClickListener {
            nodosList.clear()
            nodoCount = 1
            // Redibujar el canvas con los nuevos puntos
            mCanvas.drawColor(Color.WHITE)
            drawGrid(mCanvas)
            drawAxes(mCanvas, axisPaint)
            drawBezierCurve(mCanvas, bezierPaint, controlPointPaint)
            // Actualizar el ImageView con el nuevo bitmap
            binding.imgView.setImageBitmap(mBitmap)
        }

        // Escuchar los resultados del BezierPointsFragment
        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
            val updatedList = bundle.getParcelableArrayList<Nodo>("updatedNodosList")
            if (updatedList != null) {
                nodosList.clear()
                nodosList.addAll(updatedList)
                // Redibujar el canvas con los nuevos puntos
                mCanvas.drawColor(Color.WHITE)
                drawGrid(mCanvas)
                drawAxes(mCanvas, axisPaint)
                drawBezierCurve(mCanvas, bezierPaint, controlPointPaint)
                // Actualizar el ImageView con el nuevo bitmap
                binding.imgView.setImageBitmap(mBitmap)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Dibujar cuadrícula en el Canvas
    private fun drawGrid(canvas: Canvas) {
        val gridPaint = Paint().apply {
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 2F
        }
        val stepSize = 50f
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        // Dibujar líneas verticales
        for (x in 0..width.toInt() step stepSize.toInt()) {
            canvas.drawLine(x.toFloat(), 0F, x.toFloat(), height, gridPaint)
        }

        // Dibujar líneas horizontales
        for (y in 0..height.toInt() step stepSize.toInt()) {
            canvas.drawLine(0F, y.toFloat(), width, y.toFloat(), gridPaint)
        }
    }

    // Dibujar ejes X y Y
    private fun drawAxes(canvas: Canvas, paint: Paint) {
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()
        canvas.drawLine(0F, height, width, height, paint)
        canvas.drawLine(0F, 0F, 0F, height, paint)
    }

    // Dibujar la curva de Bézier y los puntos de control
    private fun drawBezierCurve(canvas: Canvas, paint: Paint, controlPointPaint: Paint) {
        val height = canvas.height.toFloat()

        if (nodosList.size > 2) {

            val bezierPoints = bezier.generateBezierCurve(nodosList)

            // Dibujar la curva
            for (i in 1 until bezierPoints.size) {
                val p1 = bezierPoints[i - 1]
                val p2 = bezierPoints[i]
                canvas.drawLine(p1.first, height - p1.second, p2.first, height - p2.second, paint)
            }
        }

        // Dibujar los puntos de control
        for (nodo in nodosList) {
            canvas.drawCircle(nodo.x, height - nodo.y, 8F, controlPointPaint)
        }
    }
}
