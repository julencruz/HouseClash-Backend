package com.houseclash.backend.infrastructure.persistence.user

import com.houseclash.backend.domain.model.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val username: String = "",

    @Column(unique = true, nullable = false)
    val email: String = "",

    @Column(nullable = false)
    val passwordHash: String = "",

    @Column(name = "house_id")
    val houseId: Long? = null,

    @Column(nullable = false)
    val kudosBalance: Int = 0,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_visited_houses", joinColumns = [JoinColumn(name = "user_id")])
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "house_id")
    val visitedHouseIds: Set<Long> = emptySet(),

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val totalTasksCompleted: Int = 0,

    @Column(nullable = false)
    val totalKudosEarned: Int = 0,

    @Column(nullable = false)
    val totalCardsPlayed: Int = 0
)

fun UserJpaEntity.toDomain(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
        passwordHash = this.passwordHash,
        kudosBalance = this.kudosBalance,
        houseId = this.houseId,
        visitedHouseIds = this.visitedHouseIds,
        createdAt = this.createdAt,
        totalTasksCompleted = this.totalTasksCompleted,
        totalKudosEarned = this.totalKudosEarned,
        totalCardsPlayed = this.totalCardsPlayed
    )
}

fun User.toEntity(): UserJpaEntity {
    return UserJpaEntity(
        id = this.id,
        username = this.username,
        email = this.email,
        passwordHash = this.passwordHash,
        houseId = this.houseId,
        kudosBalance = this.kudosBalance,
        visitedHouseIds = this.visitedHouseIds,
        createdAt = this.createdAt,
        totalTasksCompleted = this.totalTasksCompleted,
        totalKudosEarned = this.totalKudosEarned,
        totalCardsPlayed = this.totalCardsPlayed
    )
}
