package ru.lizyakin.vending_machines.controllers

import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import ru.lizyakin.vending_machines.VendingMachineApplication
import ru.lizyakin.vending_machines.models.User
import ru.lizyakin.vending_machines.repositories.UserRepository

class AuthController {
    @FXML lateinit var passwordField: PasswordField
    @FXML lateinit var emailTextField: TextField

    @FXML
    fun onConfirm() {
        if (emailTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Email не указан").showAndWait()
        }

        val task = object : Task<User?>() {
            override fun call(): User? {
                return UserRepository.findByEmail(emailTextField.text)
            }
        }

        task.setOnSucceeded {
            val user = task.value

            if (user == null) {
                Alert(Alert.AlertType.WARNING, "Пользователь не найден").showAndWait()
            }
            else if (user.password == passwordField.text.trim()) {
                val stage = emailTextField.scene.window as Stage
                val loader = FXMLLoader(VendingMachineApplication::class.java.getResource("layout-view.fxml"))
                val newRoot: Parent = loader.load()
                val controller: LayoutController = loader.getController()
                controller.adminMenuButton.text = user.lastname + "." + user.firstname.get(0) + "." + user.surname.get(0)
                stage.scene.root = newRoot
            }
            else {
                Alert(Alert.AlertType.ERROR, "Неверный пароль: " + passwordField.text + " " + user.password).showAndWait()
            }
        }

        task.setOnFailed {
            task.exception.printStackTrace()
            var alert = Alert(Alert.AlertType.ERROR, task.exception.message).show()
        }

        Thread(task).start()
    }
}