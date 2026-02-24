package ru.lizyakin.vending_machines.controllers

import jakarta.persistence.Column
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.util.StringConverter
import ru.lizyakin.vending_machines.models.Country
import ru.lizyakin.vending_machines.models.Manufacturer
import ru.lizyakin.vending_machines.models.Modem
import ru.lizyakin.vending_machines.models.Status
import ru.lizyakin.vending_machines.models.User
import ru.lizyakin.vending_machines.models.VendingMachine
import ru.lizyakin.vending_machines.repositories.CountryRepository
import ru.lizyakin.vending_machines.repositories.ManufacturerRepository
import ru.lizyakin.vending_machines.repositories.ModemRepository
import ru.lizyakin.vending_machines.repositories.StatusRepository
import ru.lizyakin.vending_machines.repositories.UserRepository
import ru.lizyakin.vending_machines.repositories.VMRepository
import java.time.ZoneId
import java.util.Date
import java.util.Random

class CreateVMController {
    @FXML lateinit var statusComboBox: ComboBox<Status>
    @FXML lateinit var manufacturerComboBox: ComboBox<Manufacturer>
    @FXML lateinit var countryComboBox: ComboBox<Country>
    @FXML lateinit var modelNameTextField: TextField
    @FXML lateinit var createdAtDatePicker: DatePicker
    @FXML lateinit var commissionedAtDatePicker: DatePicker
    @FXML lateinit var lastTimeCheckedAtDatePicker: DatePicker
    @FXML lateinit var intercheckingIntervalTextField: TextField
    @FXML lateinit var resourceOfMachineTextField: TextField
    @FXML lateinit var nextCheckAtDatePicker: DatePicker
    @FXML lateinit var checkTimeTextField: TextField
    @FXML lateinit var inventoryAtDatePicker: DatePicker
    @FXML lateinit var lastCheckerComboBox: ComboBox<User>
    @FXML lateinit var modemComboBox: ComboBox<Modem>
    @FXML lateinit var addressTextField: TextField

    @FXML
    fun initialize() {
        initComboBox(statusComboBox, { StatusRepository.findAll() }, { it.statusName })
        initComboBox(manufacturerComboBox, { ManufacturerRepository.findAll() }, { it.name })
        initComboBox(countryComboBox, { CountryRepository.findAll() }, { it.name })
        initComboBox(lastCheckerComboBox, { UserRepository.findAll() }, {it.lastname + " " + it.firstname})
        initComboBox(modemComboBox, { ModemRepository.findAll() }, {it.modemNumber})
    }

    private fun <T> initComboBox(
        comboBox: ComboBox<T>,
        repositoryCall: () -> List<T>,
        nameExtractor: (T) -> String
    ) {
        comboBox.converter = object : StringConverter<T>() {
            override fun toString(item: T?): String = item?.let(nameExtractor) ?: ""
            override fun fromString(string: String?): T? = null
        }

        val task = object : Task<List<T>>() {
            override fun call(): List<T> = repositoryCall()
        }

        task.setOnSucceeded {
            comboBox.items.setAll(task.value)
        }

        Thread(task).start()
    }

    @FXML
    public fun onConfirm() {
        val confirmAlert = Alert(
            Alert.AlertType.CONFIRMATION,
            "Создать торговый автомат?",
            ButtonType.YES,
            ButtonType.NO
        )
        if (confirmAlert.showAndWait().get() != ButtonType.YES) return


        var rnd = Random()
        val vm = VendingMachine(
            id = null,
            inventoryId = rnd.nextInt() * 10000,
            status = statusComboBox.value,
            manufacturer = manufacturerComboBox.value,
            country = countryComboBox.value,
            modelName = modelNameTextField.text,
            createdAt = Date.from(createdAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            commissionedAt = Date.from(createdAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            lastTimeCheckedAt = Date.from(lastTimeCheckedAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            intercheckingInterval = intercheckingIntervalTextField.text.toShort(),
            resourceOfMachine = resourceOfMachineTextField.text.toShort(),
            nextCheckAt = Date.from(nextCheckAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            checkTime = checkTimeTextField.text.toShort(),
            inventoryAt = Date.from(inventoryAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            lastChecker = lastCheckerComboBox.value,
            modem = modemComboBox.value,
            address = addressTextField.text
        )

        val task = object : Task<Unit>() {
            override fun call() {
                return VMRepository.save(vm)
            }
        }

        task.setOnSucceeded {
            val alert = Alert(Alert.AlertType.INFORMATION, "Торговый автомат успешно добавлен")
            alert.showAndWait()

            val stage = statusComboBox.scene.window as Stage
            stage.close()
        }

        task.setOnFailed {
            task.exception.printStackTrace()

            val stage = statusComboBox.scene.window as Stage
            stage.close()
        }

        Thread(task).start()
    }

    private fun parseData(){
        if (statusComboBox.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Статус не выбран").showAndWait()
            return
        }

        if (manufacturerComboBox.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Производитель не выбран").showAndWait()
            return
        }

        if (countryComboBox.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Страна не выбрана").showAndWait()
            return
        }

        if (modelNameTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Название молдели не указано").showAndWait()
            return
        }

        if (createdAtDatePicker.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Дата создания не указана").showAndWait()
            return
        }

        if (commissionedAtDatePicker.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Дата комиссии не указана").showAndWait()
            return
        }

        if (lastTimeCheckedAtDatePicker.value == null) {
            lastTimeCheckedAtDatePicker.value = createdAtDatePicker.value
        }

        if (intercheckingIntervalTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Интервал обслуживания не указан").showAndWait()
            return
        }

        if (resourceOfMachineTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Ресурс автомата не указан").showAndWait()
            return
        }

        if (nextCheckAtDatePicker.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Дата следущего обслуживания не указана").showAndWait()
            return
        }

        if (checkTimeTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Время проверки не указано").showAndWait()
            return
        }

        if (inventoryAtDatePicker.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Дата инвенторизвации не указана").showAndWait()
            return
        }

        if (lastCheckerComboBox.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Последний прове").showAndWait()
            return
        }

        if (modemComboBox.value == null) {
            var alert = Alert(Alert.AlertType.ERROR, "Статус не выбран").showAndWait()
            return
        }

        if (addressTextField.text.isEmpty()) {
            var alert = Alert(Alert.AlertType.ERROR, "Статус не выбран").showAndWait()
            return
        }
    }
}