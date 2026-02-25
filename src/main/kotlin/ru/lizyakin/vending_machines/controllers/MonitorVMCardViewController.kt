package ru.lizyakin.vending_machines.controllers

import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import ru.lizyakin.vending_machines.interfaces.MonitorController
import ru.lizyakin.vending_machines.models.VendingMachine
import java.util.*

class MonitorVMCardViewController : MonitorController {
    lateinit var vmTableView: TableView<VendingMachine>
    lateinit var idColumn: TableColumn<VendingMachine, Int>
    lateinit var vmColumn: TableColumn<VendingMachine, VendingMachine>
    lateinit var connectionColumn: TableColumn<VendingMachine, String>
    lateinit var loadColumn: TableColumn<VendingMachine, Float>
    lateinit var cashColumn: TableColumn<VendingMachine, List<Int>>
    lateinit var eventColumn: TableColumn<VendingMachine, List<Date>>
    lateinit var epuipmentColumn: TableColumn<VendingMachine, List<Boolean>>
    lateinit var informationColumn: TableColumn<VendingMachine, List<Boolean>>
    lateinit var additionalColumn: TableColumn<VendingMachine, List<Button>>
    private lateinit var vmList: List<VendingMachine>
    override fun setVMList(list: List<VendingMachine>) {
        vmList = list
    }
}