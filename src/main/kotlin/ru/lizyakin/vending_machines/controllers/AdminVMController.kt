package ru.lizyakin.vending_machines.controllers

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.stage.Stage
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.util.Date

class AdminVMController {
    @FXML private lateinit var tableView: TableView<VendingMachine>
    @FXML private lateinit var idColumn: TableColumn<VendingMachine, Int>
    @FXML private lateinit var modelNameColumn: TableColumn<VendingMachine, String>
    @FXML private lateinit var companyColumn: TableColumn<VendingMachine, String>
    @FXML private lateinit var modemColumn: TableColumn<VendingMachine, String>
    @FXML private lateinit var addressColumn: TableColumn<VendingMachine, String>
    @FXML private lateinit var worktimeColumn: TableColumn<VendingMachine, Date>

    private lateinit var selectedVendingMachine: VendingMachine

    @FXML
    fun initialize() {
        idColumn.setCellValueFactory { it.value.idProperty() }
        modelNameColumn.setCellValueFactory { it.value.modelNameProperty() }
        companyColumn.setCellValueFactory { it.value.manufacturerProperty() }
        modemColumn.setCellValueFactory { it.value.modemProperty() }
        addressColumn.setCellValueFactory { it.value.addressProperty() }
        worktimeColumn.setCellValueFactory { it.value.lastTimeCheckedAtProperty() }

        tableView.getSelectionModel().selectedItemProperty()
            .addListener(ChangeListener { obs: ObservableValue<out VendingMachine?>?, oldSelection: VendingMachine?, newSelection: VendingMachine? ->
                if (newSelection != null) {
                    selectedVendingMachine = newSelection
                }
            })

        loadData()
    }

    private fun loadData() {
        val task = object : Task<List<VendingMachine>>() {
            override fun call(): List<VendingMachine> {
                return VMRepository.findAll()
            }
        }

        task.setOnSucceeded {
            tableView.items.clear()
            tableView.items.setAll(task.value)
        }

        task.setOnFailed {
            task.exception.printStackTrace()
        }

        Thread(task).start()
    }

    @FXML
    public fun onCreateVendingMachine() {
        try {
            // Укажи путь к FXML от корня resources
            val fxmlLocation = javaClass.getResource("/ru/lizyakin/vending_machines/create-vm-view.fxml")
            val loader = FXMLLoader(fxmlLocation)
            val root = loader.load<Parent>()

            val stage = Stage()
            stage.scene = Scene(root)
            stage.minWidth = 1200.0
            stage.minHeight = 800.0
            stage.title = "Добавление торгового автомата"
            stage.showAndWait()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loadData()
        }
    }

    @FXML
    public fun onUpdateVendingMachine() {
        if (selectedVendingMachine == null) {
            val alert: Alert = Alert(Alert.AlertType.ERROR, "Торговый автомат не выбран")
            alert.showAndWait()
            return
        }

        val loader = FXMLLoader(javaClass.getResource("/ru/lizyakin/vending_machines/update-vm-view.fxml"))
        val root = loader.load<Parent>()

        // Получаем контроллер и передаем в него пользователя
        val controller = loader.getController<UpdateVMController>()
        controller.setVendingMachine(selectedVendingMachine)

        val stage = Stage()
        stage.scene = Scene(root)
        stage.minWidth = 1200.0
        stage.minHeight = 800.0
        stage.title = "Редактирование " + selectedVendingMachine.modelName
        stage.showAndWait()

        loadData()
    }

    @FXML
    fun onDeleteVendingMachine() {
        if (selectedVendingMachine == null) {
            val alert: Alert = Alert(Alert.AlertType.WARNING, "Торговый автомат не выбран")
            alert.showAndWait()
            return
        }

        val alert = Alert(Alert.AlertType.CONFIRMATION, "Удалить выбранную строку?", ButtonType.YES, ButtonType.NO)
        val result = alert.showAndWait()

        if (result.isPresent() && result.get() == ButtonType.NO) {
            return
        }

        val deleteTask = object : Task<Unit>() {
            override fun call() {
                VMRepository.delete(selectedVendingMachine)
            }
        }

        deleteTask.setOnSucceeded {
            tableView.items.remove(selectedVendingMachine)
        }

        deleteTask.setOnFailed {
            deleteTask.exception.printStackTrace()
        }

        Thread(deleteTask).start()
    }
}