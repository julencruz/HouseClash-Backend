package com.houseclash.backend.infrastructure.web.user

import com.houseclash.backend.domain.usecase.GetUserProfileUsecase
import com.houseclash.backend.domain.usecase.LoginUserUsecase
import com.houseclash.backend.domain.usecase.RegisterUserUsecase
import com.houseclash.backend.domain.usecase.UpdateUserUsecase
import com.houseclash.backend.infrastructure.security.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuaris", description = "Registre, autenticació i gestió del perfil d'usuari")
class UserController(
    private val registerUserUsecase: RegisterUserUsecase,
    private val loginUserUsecase: LoginUserUsecase,
    private val getUserProfileUsecase: GetUserProfileUsecase,
    private val updateUserUsecase: UpdateUserUsecase,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Registrar un nou usuari", description = "Crea un nou compte d'usuari amb nom d'usuari, correu electrònic i contrasenya")
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        logger.info("Registering new user with email '{}'", request.email)
        val user = registerUserUsecase.execute(
            username = request.username,
            email = request.email,
            rawPassword = request.passwordRaw
        )
        logger.info("User {} registered successfully", user.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toResponse())
    }

    @Operation(summary = "Iniciar sessió", description = "Autentica l'usuari amb correu i contrasenya i retorna un token JWT per usar a la resta d'endpoints")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        logger.info("Login attempt for email '{}'", request.email)
        val user = loginUserUsecase.execute(
            email = request.email,
            password = request.passwordRaw
        )
        val token = jwtService.generateToken(user.id!!)
        logger.info("User {} logged in successfully", user.id)
        return ResponseEntity.ok(LoginResponse(token = token, user = user.toResponse()))
    }

    @Operation(summary = "Obtenir el meu perfil", description = "Retorna les dades del perfil de l'usuari autenticat")
    @GetMapping("/me")
    fun getProfile(authentication: Authentication): ResponseEntity<UserResponse> {
        val userId = authentication.principal as Long
        logger.info("User {} fetching their profile", userId)
        val user = getUserProfileUsecase.execute(userId)
        return ResponseEntity.ok(user.toResponse())
    }

    @Operation(summary = "Actualitzar el meu perfil", description = "Permet modificar el nom d'usuari o canviar la contrasenya. Per canviar la contrasenya cal indicar l'actual")
    @PatchMapping("/me")
    fun updateProfile(
        @RequestBody request: UpdateUserRequest,
        authentication: Authentication
    ): ResponseEntity<UserResponse> {
        val userId = authentication.principal as Long
        logger.info("User {} updating their profile", userId)
        val user = updateUserUsecase.execute(
            userId = userId,
            username = request.username,
            oldPassword = request.oldPassword,
            newPassword = request.newPassword
        )
        logger.info("Profile updated for user {}", userId)
        return ResponseEntity.ok(user.toResponse())
    }
}
