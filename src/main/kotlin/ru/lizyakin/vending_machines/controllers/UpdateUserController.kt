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
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.RoleRepository
import ru.lizyakin.vending_machines.repositories.UserRepository
import java.time.ZoneId

class UpdateUserController {
    @FXML lateinit var emailTextField: TextField
    @FXML lateinit var surnameTextField: TextField
    @FXML lateinit var firstnameTextField: TextField
    @FXML lateinit var lastnameTextField: TextField
    @FXML lateinit var roleComboBox: ComboBox<Role>

    private lateinit var editingUser: User

    fun setUser(user: User) {
        this.editingUser = user
        fillFields(user)
    }

    private fun fillFields(user: User) {
        roleComboBox.value = user.role
        lastnameTextField.text = user.lastname
        firstnameTextField.text = user.firstname
        surnameTextField.text = user.surname
        emailTextField.text = user.email
    }

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
        val confirmAlert = Alert(Alert.AlertType.CONFIRMATION, "Обновить пользователя?", ButtonType.YES, ButtonType.NO)
        if (confirmAlert.showAndWait().get() != ButtonType.YES) return

        val user = User(
            id = editingUser.id,
            role = roleComboBox.value,
            lastname = lastnameTextField.text,
            firstname = firstnameTextField.text,
            surname = surnameTextField.text,
            email = emailTextField.text,
            password = ""
        )

        val task = object : Task<Unit>() {
            override fun call() {
                UserRepository.update(user)
            }
        }

        val stage = roleComboBox.scene.window as Stage
        task.setOnSucceeded {
            val alert = Alert(Alert.AlertType.INFORMATION, "Пользователь успешно обновлен").showAndWait()
            stage.close()
        }

        task.setOnFailed {
            task.exception.printStackTrace()
            stage.close()
        }

        Thread(task).start()
    }
}