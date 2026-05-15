package com.houseclash.backend.infrastructure.persistence.category

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository

@Repository
interface SpringDataCategoryRepository : JpaRepository<CategoryJpaEntity, Long> {

    fun findByHouseId(houseId: Long): List<CategoryJpaEntity>
    fun findByHouseIdAndIsDefault(houseId: Long, isDefault: Boolean): CategoryJpaEntity?

    @Modifying
    @Transactional
    fun deleteByHouseId(houseId: Long)
}
