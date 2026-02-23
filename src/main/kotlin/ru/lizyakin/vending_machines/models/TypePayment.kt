package ru.lizyakin.vending_machines.models

import jakarta.persistence.*
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

class TypePayment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,


    @Column(name = "type_name")
    var typeName: String = ""
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id)

    @Transient
    fun typeNameProperty(): StringProperty = SimpleStringProperty(typeName)
}