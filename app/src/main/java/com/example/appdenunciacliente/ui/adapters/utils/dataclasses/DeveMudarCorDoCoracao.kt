package com.example.appdenunciacliente.ui.adapters.utils.dataclasses

/**
 * essa classe serve para encapsular a logica de se o item da recycler
 * view deve ou nao mudar a coração que representa o like.
 *
 * com essa classe eu consigo armazenar se a cor deve mudar e a posicao
 * da recycler view que deve ser mudada
 */
data class DeveMudarCorDoCoracao(
    val deve: Boolean,
    val posicao: Int
)
