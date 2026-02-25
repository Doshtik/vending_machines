package ru.lizyakin.vending_machines.interfaces

import ru.lizyakin.vending_machines.models.VendingMachine

interface MonitorController {
    fun setVMList(list: List<VendingMachine>)
}