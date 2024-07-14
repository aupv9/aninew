package autogear.frontapi.listener

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.Permission
import autogear.frontapi.entity.User
import autogear.frontapi.enum.AdminPermission
import autogear.frontapi.enum.ResourcePermission
import autogear.frontapi.enum.Role
import autogear.frontapi.enum.UserMemberPermission
import autogear.frontapi.repository.PrivilegeRepo
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.repository.UserRepository
//import autogear.frontapi.service.DriveService
import autogear.frontapi.service.ResourceService
import autogear.frontapi.service.S3Service

import common.Utils
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class RefreshAuthorizeListener(
    val roleRepository: RoleRepository,
    val privilegeRepo: PrivilegeRepo,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mongoOperation: MongoOperations,
//    private val driveService: DriveService,
    private val resourceService: ResourceService,
    private val autoGearConfiguration: AutoGearConfiguration,
//    private val googleCloudStorageConfiguration: GoogleCloudStorageConfiguration,
    private val s3Service: S3Service
): ApplicationListener<ContextRefreshedEvent> {

    @Async
    @Transactional(rollbackFor = [Exception::class])
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        initializeRolesAndPermission()
    }

    private fun initializeRolesAndPermission(){

        UserMemberPermission.values().forEach {
            val permission = privilegeRepo.findByName(it.getName())
            if(permission == null){
                privilegeRepo.save(Permission(name = it.getName()))
            }
        }

        AdminPermission.values().forEach {
            val permission = privilegeRepo.findByName(it.getName())
            if(permission == null){
                privilegeRepo.save(Permission(name = it.getName()))
            }
        }
        ResourcePermission.values().forEach {
            val permission = privilegeRepo.findByName(it.getName())
            if(permission == null){
                privilegeRepo.save(Permission(name = it.getName()))
            }
        }

        Role.values().forEach {
            val role = roleRepository.findByName(it.name)
            if(role == null){
                roleRepository.save(autogear.frontapi.entity.Role(name = it.name,
                    permission = it.getPermissions().map { basePermission ->
                     privilegeRepo.findByName(basePermission.getName())!!
                    }.toSet()))
            }
        }
        val roleAdmin = roleRepository.findByName(Role.ROLE_ADMIN.name)
        if(roleAdmin != null){
            val userAdminDefault = userRepository.findByRolesNameIn(listOf(Role.ROLE_ADMIN.name))
            if(userAdminDefault.isEmpty()){
                val user = User(name = ADMIN,  email = "admin", userID = Utils.generateId(), roles = arrayListOf(roleAdmin), enabled = true,
                    password = passwordEncoder.encode(ADMIN_PASSW))
                userRepository.save(user)
            }
        }

        val resource = resourceService.getResourceByName(autoGearConfiguration.driveConfiguration.name)
        if(resource == null){
//            driveService.createFolder(autoGearConfiguration.driveConfiguration.name, "")
//            val storage = googleCloudStorageConfiguration.getStorage()
//            storage?.create(BucketInfo.newBuilder(autoGearConfiguration.gcpConfiguration.bucketName)
//                .setStorageClass(StorageClass.COLDLINE)
//                .build())

        }

        if(!s3Service.doesBucketExist(autoGearConfiguration.cfConfiguration.bucketName))
            s3Service.createBucket(autoGearConfiguration.cfConfiguration.bucketName)

    }
    companion object {
        const val ADMIN = "admin"

        const val ADMIN_PASSW  = "123456"
    }
}