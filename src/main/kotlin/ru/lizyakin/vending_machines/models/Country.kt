package ru.lizyakin.vending_machines.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Transient
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

@Entity
@Table(name = "countries")
class Country(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "name")
    var name: String = ""
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id ?: 0)

    @Transient
    fun nameProperty(): StringProperty = SimpleStringProperty(name)

    override fun toString(): String = name
}