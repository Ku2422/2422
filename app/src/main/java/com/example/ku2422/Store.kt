package com.example.ku2422

//pk userId
data class Store(
    val userId: String = "",
    var menu: String= "",
    var price: Int= 0,
    var review: String= "",
    var star: Double=0.0,
    var locationX: Float=0f,
    var locationY: Float=0f
) {

}
