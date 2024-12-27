package com.example.wapp.screen.components

sealed class InputType{
    object Email: InputType()
    object Phone: InputType()
    object Text: InputType()
}