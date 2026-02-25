package ru.lizyakin.vending_machines.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.layout.StackPane
import ru.lizyakin.vending_machines.interfaces.MonitorController
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.io.IOException


class MainMonitorVMController {
    @FXML private val contentArea: StackPane? = null

    private lateinit var vmList: List<VendingMachine>

    fun initialize() {
        vmList = VMRepository.findAll()
        showTableView()
    }

    @FXML
    private fun showTableView() {
        loadView<MonitorVMTableViewController>("ru/lizyakin/vending_machines/templates/MonitorVMTableView.fxml")
    }

    @FXML
    private fun showCardsView() {
        loadView<MonitorVMCardViewController>("ru/lizyakin/vending_machines/templates/MonitorVMCardView.fxml")
    }

    @FXML
    private fun exportToCSV() {

    }

    @FXML
    private fun exportToPDF() {

    }

    private fun <T: MonitorController> loadView(fxmlPath: String) {
        try {
            val loader = FXMLLoader(javaClass.getResource(fxmlPath))
            val view = loader.load<Parent?>()

            val controller = loader.getController<T>()
            controller.setVMList(vmList)

            contentArea!!.getChildren().clear()
            contentArea.getChildren().add(view)
        } catch (e: IOException) {
            e.printStackTrace()
            // Обработка ошибки: файл не найден или ошибка в FXML
        }
    }
}