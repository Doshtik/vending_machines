package ru.lizyakin.vending_machines.controllers

import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import ru.lizyakin.vending_machines.interfaces.MonitorController
import ru.lizyakin.vending_machines.models.VendingMachine

class MonitorVMCardViewController : MonitorController {
    lateinit var vmTableView: TableView<Any!>
    lateinit var idColumn: TableColumn<Any!, Any!>
    lateinit var vmColumn: TableColumn<Any!, Any!>
    lateinit var connectionColumn: TableColumn<Any!, Any!>
    lateinit var loadColumn: TableColumn<Any!, Any!>
    lateinit var cashColumn: TableColumn<Any!, Any!>
    lateinit var eventColumn: TableColumn<Any!, Any!>
    lateinit var epuipmentColumn: TableColumn<Any!, Any!>
    lateinit var informationColumn: TableColumn<Any!, Any!>
    lateinit var additionalColumn: TableColumn<Any!, Any!>
    private lateinit var vmList: List<VendingMachine>
    override fun setVMList(list: List<VendingMachine>) {
        vmList = list
    }
}