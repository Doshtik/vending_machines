package ru.lizyakin.vending_machines.controllers

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
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Random

class UpdateVMController {
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

    private var editingMachine: VendingMachine? = null

    fun setVendingMachine(machine: VendingMachine) {
        this.editingMachine = machine
        fillFields(machine)
    }

    private fun fillFields(machine: VendingMachine) {
        modelNameTextField.text = machine.modelName
        addressTextField.text = machine.address
        intercheckingIntervalTextField.text = machine.intercheckingInterval.toString()
        resourceOfMachineTextField.text = machine.resourceOfMachine.toString()
        checkTimeTextField.text = machine.checkTime.toString()

        createdAtDatePicker.value = machine.createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        commissionedAtDatePicker.value = machine.commissionedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        lastTimeCheckedAtDatePicker.value = machine.lastTimeCheckedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        nextCheckAtDatePicker.value = machine.nextCheckAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        inventoryAtDatePicker.value = machine.inventoryAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        statusComboBox.value = machine.status
        manufacturerComboBox.value = machine.manufacturer
        countryComboBox.value = machine.country
        lastCheckerComboBox.value = machine.lastChecker
        modemComboBox.value = machine.modem
    }

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
    fun onConfirm() {
        val confirmAlert = Alert(Alert.AlertType.CONFIRMATION, "Обновить торговый автомат?", ButtonType.YES, ButtonType.NO)
        if (confirmAlert.showAndWait().get() != ButtonType.YES) return

        fun localDateToDate(localDate: LocalDate): Date =
            Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val vm = VendingMachine(
            id = editingMachine?.id,
            inventoryId = editingMachine?.inventoryId,
            status = statusComboBox.value,
            manufacturer = manufacturerComboBox.value,
            country = countryComboBox.value,
            modelName = modelNameTextField.text,
            createdAt = localDateToDate(createdAtDatePicker.value),
            commissionedAt = localDateToDate(commissionedAtDatePicker.value),
            lastTimeCheckedAt = localDateToDate(lastTimeCheckedAtDatePicker.value),
            intercheckingInterval = intercheckingIntervalTextField.text.toShort(),
            resourceOfMachine = resourceOfMachineTextField.text.toShort(),
            nextCheckAt = localDateToDate(nextCheckAtDatePicker.value),
            checkTime = checkTimeTextField.text.toShort(),
            inventoryAt = localDateToDate(inventoryAtDatePicker.value),
            lastChecker = lastCheckerComboBox.value,
            modem = modemComboBox.value,
            address = addressTextField.text
        )

        val task = object : Task<Unit>() {
            override fun call() {
                VMRepository.update(vm)
            }
        }

        task.setOnSucceeded {
            Alert(Alert.AlertType.INFORMATION, "Данные успешно сохранены").showAndWait()
            (statusComboBox.scene.window as Stage).close()
        }

        task.setOnFailed {
            task.exception.printStackTrace()
            Alert(Alert.AlertType.ERROR, "Ошибка при сохранении: ${task.exception.message}").showAndWait()
        }

        Thread(task).start()
    }
}