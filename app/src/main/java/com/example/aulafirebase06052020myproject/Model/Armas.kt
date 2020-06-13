package com.example.aulafirebase06052020myproject.Model

class Armas (
    var nome: String? = null,
    var tipo: String? = null,
    var origem: String? = null
) {
    override fun toString(): String {
        return "$nome, arma tipo $tipo é original do(a) $origem."
    }
}