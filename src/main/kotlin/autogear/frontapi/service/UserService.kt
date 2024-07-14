package autogear.frontapi.service

import autogear.frontapi.entity.User
import autogear.frontapi.mapper.UserDto
import autogear.frontapi.mapper.UserMapper
import autogear.frontapi.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional


interface IUserService{
    fun getALlUser(): List<UserDto>
    fun findUserByEmail(email: String): Optional<User>

    fun findUserById(userID: String): Optional<User>

}

@Service
class UserService(private val userMapper: UserMapper,
                      private val authenticateRepository: UserRepository) : IUserService {
    override fun getALlUser(): List<UserDto> {
        val users = authenticateRepository.findAll()
        return users.map{ user: User -> userMapper.toDto(user)}
    }

    override fun findUserByEmail(email: String): Optional<User> = this.authenticateRepository.findByEmail(email)
    override fun findUserById(userID: String): Optional<User> = this.authenticateRepository.findByUserID(userID)

}