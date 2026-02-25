package ru.lizyakin.vending_machines.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.layout.StackPane
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.lizyakin.vending_machines.interfaces.MonitorController
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat


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
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Vending Machines")
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")

        val headerRow = sheet.createRow(0)
        val columns = listOf("ID", "Инв. номер", "Модель", "Статус", "Адрес", "Дата создания")
        columns.forEachIndexed { index, title ->
            headerRow.createCell(index).setCellValue(title)
        }

        vmList.forEachIndexed { index, machine ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(machine.id?.toDouble() ?: 0.0)
            row.createCell(1).setCellValue(machine.inventoryId?.toDouble() ?: 0.0)
            row.createCell(2).setCellValue(machine.modelName)
            row.createCell(3).setCellValue(machine.status.statusName)
            row.createCell(4).setCellValue(machine.address)
            row.createCell(5).setCellValue(dateFormat.format(machine.createdAt))
        }

        for (i in columns.indices) {
            sheet.autoSizeColumn(i)
        }

        FileOutputStream("report.xlsx").use { bos ->
            workbook.write(bos)
        }
        workbook.close()
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
        }
    }
}