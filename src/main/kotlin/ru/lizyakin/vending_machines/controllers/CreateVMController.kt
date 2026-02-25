package ru.lizyakin.vending_machines.controllers

import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import javafx.util.StringConverter
import javafx.application.Platform
import ru.lizyakin.vending_machines.models.*
import ru.lizyakin.vending_machines.repositories.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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
    fun onConfirm() {
        // Сначала валидация
        if (!validateData()) return

        // Подстановка значения по умолчанию для lastTimeCheckedAt, если не указано
        if (lastTimeCheckedAtDatePicker.value == null) {
            lastTimeCheckedAtDatePicker.value = createdAtDatePicker.value
        }

        val confirmAlert = Alert(
            Alert.AlertType.CONFIRMATION,
            "Создать торговый автомат?",
            ButtonType.YES,
            ButtonType.NO
        )
        if (confirmAlert.showAndWait().get() != ButtonType.YES) return

        val vm = VendingMachine(
            id = null,
            inventoryId = null,
            status = statusComboBox.value,
            manufacturer = manufacturerComboBox.value,
            country = countryComboBox.value,
            modelName = modelNameTextField.text.trim(),
            createdAt = Date.from(createdAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            commissionedAt = Date.from(commissionedAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            lastTimeCheckedAt = Date.from(lastTimeCheckedAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            intercheckingInterval = intercheckingIntervalTextField.text.trim().toShort(),
            resourceOfMachine = resourceOfMachineTextField.text.trim().toShort(),
            nextCheckAt = Date.from(nextCheckAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            checkTime = checkTimeTextField.text.trim().toShort(),
            inventoryAt = Date.from(inventoryAtDatePicker.value.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            lastChecker = lastCheckerComboBox.value,
            modem = modemComboBox.value,
            address = addressTextField.text.trim()
        )

        val task = object : Task<Unit>() {
            override fun call() {
                VMRepository.save(vm)
            }
        }

        task.setOnSucceeded {
            val alert = Alert(Alert.AlertType.INFORMATION, "Торговый автомат успешно добавлен")
            alert.showAndWait()
            (statusComboBox.scene.window as Stage).close()
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

        // Проверка заполненности комбобоксов
        if (statusComboBox.value == null) errors.add("Не выбран статус")
        if (manufacturerComboBox.value == null) errors.add("Не выбран производитель")
        if (countryComboBox.value == null) errors.add("Не выбрана страна")
        if (lastCheckerComboBox.value == null) errors.add("Не выбран последний проверяющий")
        if (modemComboBox.value == null) errors.add("Не выбран модем")

        // Текстовые поля (не пустые)
        if (modelNameTextField.text.isBlank()) errors.add("Не указано название модели")
        if (addressTextField.text.isBlank()) errors.add("Не указан адрес")

        // Дата создания обязательна
        val createdAt = createdAtDatePicker.value
        if (createdAt == null) {
            errors.add("Не указана дата создания")
        }

        // Дата комиссии
        val commissionedAt = commissionedAtDatePicker.value
        if (commissionedAt == null) {
            errors.add("Не указана дата комиссии")
        }

        // Дата последней проверки может быть не указана (заменится на дату создания)
        val lastTimeCheckedAt = lastTimeCheckedAtDatePicker.value
        // Если не указана – позже подставим createdAt, поэтому здесь не ругаемся

        // Дата следующей проверки
        val nextCheckAt = nextCheckAtDatePicker.value
        if (nextCheckAt == null) {
            errors.add("Не указана дата следующего обслуживания")
        }

        // Дата инвентаризации
        val inventoryAt = inventoryAtDatePicker.value
        if (inventoryAt == null) {
            errors.add("Не указана дата инвентаризации")
        }

        // Числовые поля
        val intercheckingInterval = parseShort(intercheckingIntervalTextField.text)
        if (intercheckingInterval == null) {
            errors.add("Интервал обслуживания должен быть целым положительным числом")
        }

        val resourceOfMachine = parseShort(resourceOfMachineTextField.text)
        if (resourceOfMachine == null) {
            errors.add("Ресурс автомата должен быть целым положительным числом")
        } else if (resourceOfMachine <= 0) {
            errors.add("Ресурс автомата должен быть больше 0")
        }

        val checkTime = parseShort(checkTimeTextField.text)
        if (checkTime == null) {
            errors.add("Время проверки должно быть целым числом")
        } else if (checkTime <= 0 || checkTime >= 20) {
            errors.add("Время проверки должно быть > 0 и < 20")
        }

        // Если есть ошибки с датами, дальше не проверяем соотношения, чтобы избежать NPE
        if (errors.isNotEmpty()) {
            showErrors(errors)
            return false
        }

        // Теперь проверяем соотношения дат (все даты гарантированно не null кроме lastTimeCheckedAt)
        val today = LocalDate.now()

        // commissioned_at >= created_at
        if (commissionedAt!! < createdAt!!) {
            errors.add("Дата комиссии не может быть раньше даты создания")
        }

        // inventory_at >= created_at И inventory_at <= CURRENT_TIMESTAMP
        if (inventoryAt!! < createdAt) {
            errors.add("Дата инвентаризации не может быть раньше даты создания")
        }
        if (inventoryAt > today) {
            errors.add("Дата инвентаризации не может быть в будущем")
        }

        // last_time_checked_at (если задано) >= created_at и <= today
        if (lastTimeCheckedAt != null) {
            if (lastTimeCheckedAt < createdAt) {
                errors.add("Дата последней проверки не может быть раньше даты создания")
            }
            if (lastTimeCheckedAt > today) {
                errors.add("Дата последней проверки не может быть в будущем")
            }
        }

        // next_check_at >= commissioned_at
        if (nextCheckAt!! < commissionedAt) {
            errors.add("Дата следующего обслуживания не может быть раньше даты комиссии")
        }

        // Дополнительная проверка на положительность интервала и ресурса (уже частично сделано)
        if (intercheckingInterval!! <= 0) {
            errors.add("Интервал обслуживания должен быть больше 0")
        }

        if (errors.isNotEmpty()) {
            showErrors(errors)
            return false
        }

        return true
    }

    private fun parseShort(s: String): Short? {
        val trimmed = s.trim()
        return if (trimmed.matches(Regex("-?\\d+"))) trimmed.toShort() else null
    }

    private fun showErrors(errors: List<String>) {
        val message = errors.joinToString("\n• ", "• ")
        Alert(Alert.AlertType.ERROR, message).showAndWait()
    }
}