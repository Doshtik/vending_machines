package ru.lizyakin.vending_machines.repositories

import org.hibernate.Session
import ru.lizyakin.vending_machines.HibernateUtil
import ru.lizyakin.vending_machines.models.Modem

object ModemRepository {
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

    fun findAll(): List<Modem> = execute {
        it.createQuery("FROM Modem", Modem::class.java).list()
    }
}