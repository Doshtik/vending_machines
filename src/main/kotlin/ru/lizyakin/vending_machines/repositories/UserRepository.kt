package ru.lizyakin.vending_machines.repositories

import org.hibernate.Session
import ru.lizyakin.vending_machines.HibernateUtil
import ru.lizyakin.vending_machines.models.User

object UserRepository {

    // Helper для автоматического закрытия сессии и управления транзакциями
    fun <T> execute(action: (Session) -> T): T {
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

    fun findAll(): List<User> = execute {
        it.createQuery("FROM User", User::class.java).list()
    }

    fun findById(id: Int): User? = execute { it.get(User::class.java, id) }

    fun findByEmail(email: String): User? = execute { session ->
        session.createQuery("FROM User WHERE email = :email", User::class.java)
            .setParameter("email", email)
            .uniqueResult()
    }

    fun save(user: User) = execute { it.persist(user) }

    fun update(user: User) = execute { it.merge(user) }

    fun delete(user: User) = execute { it.remove(user) }
}