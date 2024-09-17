package com.example.myktactil

import kotlin.math.pow

class Bezier {

    // Función para calcular el coeficiente binomial (n sobre k)
    private fun binomialCoefficient(n: Int, k: Int): Int {
        return (factorial(n) / (factorial(k) * factorial(n - k)))
    }

    // Función para calcular el factorial de un número
    private fun factorial(num: Int): Int {
        return if (num <= 1) 1 else num * factorial(num - 1)
    }

    // Función que genera la curva de Bézier
    fun generateBezierCurve(nodos: List<Nodo>, steps: Int = 100): List<Pair<Float, Float>> {
        val curvePoints = mutableListOf<Pair<Float, Float>>()

        // t es el parámetro que varía de 0 a 1
        for (step in 0..steps) {
            val t = step / steps.toFloat()
            var x = 0f
            var y = 0f

            val n = nodos.size - 1

            // Aplicamos la fórmula de Bézier para calcular el punto en (x, y)
            for (i in nodos.indices) {
                val binCoeff = binomialCoefficient(n, i)
                val term = (1 - t).pow(n - i) * t.pow(i)
                x += binCoeff * term * nodos[i].x
                y += binCoeff * term * nodos[i].y
            }

            curvePoints.add(Pair(x, y))
        }

        return curvePoints
    }
}
