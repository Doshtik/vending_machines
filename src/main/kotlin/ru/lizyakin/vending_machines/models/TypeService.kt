package ru.lizyakin.vending_machines.models

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class TypeService (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,


    @Column(name = "type_name")
    var typeName: String = ""
)