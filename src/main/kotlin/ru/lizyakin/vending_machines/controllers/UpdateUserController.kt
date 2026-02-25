package ru.lizyakin.vending_machines.controllers

import javafx.application.Platform
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
    @FXML lateinit var passwordTextField: TextField
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
        if (!validateData()) return

        val confirmAlert = Alert(
            Alert.AlertType.CONFIRMATION,
            "Создать пользователя?",
            ButtonType.YES,
            ButtonType.NO
        )
        if (confirmAlert.showAndWait().orElse(null) != ButtonType.YES) return

        val user = User(
            id = editingUser.id,
            role = roleComboBox.value,
            lastname = lastnameTextField.text.trim(),
            firstname = firstnameTextField.text.trim(),
            surname = surnameTextField.text.trim(),
            email = emailTextField.text.trim(),
            password = passwordTextField.text.trim()
        )

        val stage = roleComboBox.scene.window as Stage

        val task = object : Task<Unit>() {
            override fun call() {
                UserRepository.update(user)
            }
        }

        task.setOnSucceeded {
            Platform.runLater {
                Alert(Alert.AlertType.INFORMATION, "Пользователь успешно добавлен").showAndWait()
                stage.close()
            }
        }

        task.setOnFailed {
            Platform.runLater {
                Alert(Alert.AlertType.ERROR, "Ошибка при сохранении: ${task.exception.message}").showAndWait()
            }
        }

        Thread(task).start()
    }

    private fun validateData(): Boolean {
        val errors = mutableListOf<String>()

        if (roleComboBox.value == null) {
            errors.add("Не выбрана роль пользователя")
        }

        if (lastnameTextField.text.isBlank()) {
            errors.add("Фамилия не может быть пустой")
        }
        if (firstnameTextField.text.isBlank()) {
            errors.add("Имя не может быть пустым")
        }
        if (surnameTextField.text.isBlank()) {
            errors.add("Отчество не может быть пустым")
        }
        if (emailTextField.text.isBlank()) {
            errors.add("Email не может быть пустым")
        } else {
            val email = emailTextField.text.trim()
            if (!isValidEmail(email)) {
                errors.add("Введён некорректный email (должен содержать @ и домен)")
            }
        }

        if (errors.isNotEmpty()) {
            showErrors(errors)
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".") - 1
    }

    private fun showErrors(errors: List<String>) {
        val message = errors.joinToString("\n• ", "• ")
        Alert(Alert.AlertType.ERROR, message).showAndWait()
    }
}