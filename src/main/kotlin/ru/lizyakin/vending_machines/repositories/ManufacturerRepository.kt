package ru.lizyakin.vending_machines.repositories

import org.hibernate.Session
import ru.lizyakin.vending_machines.HibernateUtil
import ru.lizyakin.vending_machines.models.Manufacturer

object ManufacturerRepository {
    // Helper для автоматического закрытия сессии и управления транзакциями
    private fun <T> execute(action: (Session) -> T): T {
        val session = HibernateUtil.sessionFactory.openSession()
        val transaction = session.beginTransaction()
        return try {
            val result = action(session)
            transaction.commit()
            result
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }

    fun findAll(): List<Manufacturer> = execute {
        it.createQuery("FROM Manufacturer", Manufacturer::class.java).list()
    }
}