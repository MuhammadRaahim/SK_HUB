package com.jdars.shared_online_business.models

import java.io.Serializable

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
    var pLocation: String = "",
    var pCondition: Boolean = false,
    var pImage: String? = null
    ): Serializable