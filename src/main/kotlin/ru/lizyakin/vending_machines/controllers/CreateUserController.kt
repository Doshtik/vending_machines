package ru.lizyakin.vending_machines.controllers

import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.util.StringConverter
import ru.lizyakin.vending_machines.models.Role
import ru.lizyakin.vending_machines.models.User
import ru.lizyakin.vending_machines.repositories.RoleRepository
import ru.lizyakin.vending_machines.repositories.UserRepository
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.time.ZoneId
import java.util.Date
import java.util.Random

class CreateUserController {
    @FXML lateinit var emailTextField: TextField
    @FXML lateinit var surnameTextField: TextField
    @FXML lateinit var firstnameTextField: TextField
    @FXML lateinit var lastnameTextField: TextField
    @FXML lateinit var roleComboBox: ComboBox<Role>

    fun initialize() {
        roleComboBox.converter = object : StringConverter<Role>() {
            override fun toString(role: Role?): String = role?.roleName ?: ""
            override fun fromString(string: String?): Role? = null
        }

        val task = object : Task<List<Role>>() {
            override fun call(): List<Role> = RoleRepository.findAll()
        }

        task.setOnSucceeded {
            roleComboBox.items.setAll(task.value)
        }

        Thread(task).start()
    }

    @FXML
    fun onConfirm() {
        val confirmAlert = Alert(Alert.AlertType.CONFIRMATION, "Создать пользователя?", ButtonType.YES, ButtonType.NO)
        if (confirmAlert.showAndWait().get() != ButtonType.YES) return

        val user = User(
            id = null,
            role = roleComboBox.value,
            lastname = lastnameTextField.text,
            firstname = firstnameTextField.text,
            surname = surnameTextField.text,
            email = emailTextField.text,
            password = ""
        )

        val task = object : Task<Unit>() {
            override fun call() {
                return UserRepository.save(user)
            }
        }

        val stage = roleComboBox.scene.window as Stage
        task.setOnSucceeded {
            val alert = Alert(Alert.AlertType.INFORMATION, "Пользователь успешно добавлен")
            alert.showAndWait()
            stage.close()
        }

        task.setOnFailed {
            task.exception.printStackTrace()
            stage.close()
        }

        Thread(task).start()
    }
}