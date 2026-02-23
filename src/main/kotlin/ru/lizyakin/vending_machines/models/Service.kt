package ru.lizyakin.vending_machines.models

import jakarta.persistence.Column
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.Date

class Service (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_service")
    var typeService: TypeService,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vending_machine")
    var vendingMachine: VendingMachine,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    var user: User,

    @Column(name = "description")
    var description: String,

    @Column(name = "problems")
    var problems: String,

    @Column(name = "date_of_service")
    var dateOfService: Date
)