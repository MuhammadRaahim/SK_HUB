package com.jdars.shared_online_business.models

data class Product(
    var id: String = "",
    var ownerId: String = "",
    var pTitle: String = "",
    var pCategory: String = "",
    var pSubCategory: String = "",
    var pPrice: String = "",
    var pBrand: String = "",
    var pSize: String = "",
    var pColour: String = "",
    var pDescription: String = "",
    var createdAt: String = "",
    var pImage: String? = null
    )