package ru.lizyakin.vending_machines.models

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Transient
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

class Modem (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    var modemNumber: String = ""
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id)

    @Transient
    fun modemNumberProperty(): StringProperty = SimpleStringProperty(modemNumber)
}