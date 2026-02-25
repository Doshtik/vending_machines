package ru.lizyakin.vending_machines.controllers

import ru.lizyakin.vending_machines.interfaces.MonitorController
import ru.lizyakin.vending_machines.models.VendingMachine

class MonitorVMTableViewController : MonitorController {
    private lateinit var vmList: List<VendingMachine>
    override fun setVMList(list: List<VendingMachine>) {
        vmList = list
    }
}