package ru.tbank.dao.transport

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.TransportBrand
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.NoResultException

class TransportBrandSimpleDao private constructor() : TransportBrandDao {
    companion object {
        val singleton: TransportBrandSimpleDao = lazy { TransportBrandSimpleDao() }.value
    }

    override fun findAll(entityManager: EntityManager): LinkedHashSet<TransportBrand> {
        return entityManager.createQuery("FROM TransportBrand", TransportBrand::class.java)
            .resultList.toCollection(LinkedHashSet<TransportBrand>())
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(entityManager: EntityManager, brandId: UUID): TransportBrand {
        try {
            return entityManager.find(TransportBrand::class.java, brandId)
        } catch (e: Exception) {
            throw
                if (e is NoResultException || e is NullPointerException) EntityNotFoundException("Запись не найдена!", e)
                else e
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findByName(entityManager: EntityManager, brand: String): TransportBrand {
        try {
            return entityManager.createQuery("SELECT b FROM TransportBrand b WHERE brand=:brand", TransportBrand::class.java)
                .setParameter("brand", brand).singleResult
        } catch (e: Exception) {
            throw
                if (e is NoResultException || e is NullPointerException) EntityNotFoundException("Запись не найдена!", e)
                else e
        }
    }

    /** Удаление */
    override fun delete(entityManager: EntityManager, brand: TransportBrand): Boolean {
        val brandNotDetached =
            if (!entityManager.contains(brand)) entityManager.find(brand::class.java, brand.id)
            else brand
        entityManager.remove(brandNotDetached)
        return true
    }

    /** Добавление */
    override fun add(entityManager: EntityManager, brand: TransportBrand): Boolean {
        entityManager.persist(brand)
        return true
    }

    /** Обновление
     * @throws javax.persistence.OptimisticLockException */
    override fun update(entityManager: EntityManager, brand: TransportBrand): Boolean {
        entityManager.merge(brand)
        return true
    }
}