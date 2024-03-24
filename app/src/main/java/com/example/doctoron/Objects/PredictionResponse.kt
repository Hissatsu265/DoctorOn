package com.example.doctoron.Objects

class PredictionResponse(var prediction: Int = 0) {
    init{
        prediction=prediction
    }
    fun getPredictionValue(): Int {
        return prediction
    }
}