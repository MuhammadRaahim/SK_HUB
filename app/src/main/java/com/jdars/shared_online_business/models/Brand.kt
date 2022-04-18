package com.jdars.shared_online_business.models

data class Brand (
    var id: String,
    var brandTitle: String,
    var ownerId: String,
    var ownerName: String,
    var ownerEmail: String,
    var description: String,
    var brandImage: String
    )