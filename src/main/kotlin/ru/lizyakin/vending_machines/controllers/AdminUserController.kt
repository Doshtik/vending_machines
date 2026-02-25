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
import ru.lizyakin.vending_machines.models.User
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.UserRepository
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.util.Date

class AdminUserController {
    @FXML private lateinit var tableView: TableView<User>
    @FXML private lateinit var idColumn: TableColumn<User, Int>
    @FXML private lateinit var roleColumn: TableColumn<User, String>
    @FXML private lateinit var lastnameColumn: TableColumn<User, String>
    @FXML private lateinit var firstnameColumn: TableColumn<User, String>
    @FXML private lateinit var surnameColumn: TableColumn<User, String>
    @FXML private lateinit var emailColumn: TableColumn<User, String>

    private lateinit var selectedUser: User

    @FXML
    fun initialize() {
        idColumn.setCellValueFactory { it.value.idProperty() }
        roleColumn.setCellValueFactory { it.value.roleNameProperty() }
        lastnameColumn.setCellValueFactory { it.value.lastnameProperty() }
        firstnameColumn.setCellValueFactory { it.value.firstnameProperty() }
        surnameColumn.setCellValueFactory { it.value.surnameProperty() }
        emailColumn.setCellValueFactory { it.value.emailProperty() }

        tableView.getSelectionModel().selectedItemProperty()
            .addListener(ChangeListener { obs: ObservableValue<out User?>?, oldSelection: User?, newSelection: User? ->
                if (newSelection != null) {
                    selectedUser = newSelection
                }
            })

        loadData()
    }

    private fun loadData() {
        val task = object : Task<List<User>>() {
            override fun call(): List<User> {
                return UserRepository.findAll()
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
    public fun onCreateUser() {
        try {
            // Укажи путь к FXML от корня resources
            val fxmlLocation = javaClass.getResource("/ru/lizyakin/vending_machines/create-user-view.fxml")
            val loader = FXMLLoader(fxmlLocation)
            val root = loader.load<Parent>()

            val stage = Stage()
            stage.scene = Scene(root)
            stage.minWidth = 1200.0
            stage.minHeight = 800.0
            stage.title = "Добавление пользователя"
            stage.showAndWait()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loadData()
        }
    }

    @FXML
    public fun onUpdateUser() {
        if (selectedUser == null) {
            val alert: Alert = Alert(Alert.AlertType.WARNING, "Пользователь не выбран")
            alert.showAndWait()
            return
        }

        val loader = FXMLLoader(javaClass.getResource("/ru/lizyakin/vending_machines/update-user-view.fxml"))
        val root = loader.load<Parent>()

        // Получаем контроллер и передаем в него пользователя
        val controller = loader.getController<UpdateUserController>()
        controller.setUser(selectedUser)

        val stage = Stage()
        stage.scene = Scene(root)
        stage.minWidth = 1200.0
        stage.minHeight = 800.0
        stage.title = "Редактирование " + selectedUser.lastname + " " + selectedUser.firstname
        stage.showAndWait()

        loadData()
    }

    @FXML
    fun onDeleteUser() {
        if (selectedUser == null) {
            val alert: Alert = Alert(Alert.AlertType.WARNING, "Пользователь не выбран")
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
                UserRepository.delete(selectedUser)
            }
        }

        deleteTask.setOnSucceeded {
            tableView.items.remove(selectedUser)
        }

        deleteTask.setOnFailed {
            deleteTask.exception.printStackTrace()
        }

        Thread(deleteTask).start()
    }
}