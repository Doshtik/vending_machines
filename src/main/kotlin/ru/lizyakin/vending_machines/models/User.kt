package ru.lizyakin.vending_machines.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Transient
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role")
    var role: Role,

    @Column(name = "lastname")
    var lastname: String,

    @Column(name = "firstname")
    var firstname: String,

    @Column(name = "surname")
    var surname: String,

    @Column(name = "email")
    var email: String
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id ?: 0)

    @Transient
    fun roleNameProperty(): StringProperty = SimpleStringProperty(role?.roleName ?: "—")
}
