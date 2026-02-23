package ru.lizyakin.vending_machines.models

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "vending_machines_types_payment")
class VendingMachineTypePayment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vending_machine")
    var idVendingMachine: VendingMachine,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_payment")
    var idTypePayment: TypePayment
)