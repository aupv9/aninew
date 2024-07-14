package autogear.frontapi.service


import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.mapper.ResourceDTO
import autogear.frontapi.mapper.ResourceMapper
import autogear.frontapi.repository.ResourceRepository

import org.springframework.stereotype.Service

@Service
class ResourceService(
    private val resourceRepository: ResourceRepository,
    private val resourceMapper: ResourceMapper,
    private val autoGearConfiguration: AutoGearConfiguration,
): IResourceService {

    companion object{
        const val RESOURCES_CACHE = "resources"
        const val KEY = "'all'"
    }

//    @Cacheable(cacheNames = [RESOURCES_CACHE], key = RESOURCES_CACHE + KEY)
    override fun getResources(): Collection<ResourceDTO> {
       return resourceRepository.findAll().map { resourceMapper.toDto(it) }
    }

    override fun getResource(id: String): ResourceDTO? {
        val resourceOpt = resourceRepository.findById(id)
        return if(resourceOpt.isEmpty) {
            resourceMapper.toDto(resourceOpt.get())
        }else{
            null
        }
    }

//    @CachePut(cacheNames = [RESOURCES_CACHE], key = "$RESOURCES_CACHE #resource.id")
    override fun addResource(resource: ResourceDTO): ResourceDTO {

        val resourceEntity = resourceRepository.save(resourceMapper.toEntity(resource))
        return resourceMapper.toDto(resourceEntity)
    }


    override fun updateResource(resource: ResourceDTO): ResourceDTO {
        TODO("Not yet implemented")
    }

    override fun deleteResource(id: String) {
        TODO("Not yet implemented")
    }

    override fun createBucket(bucketName: String): Boolean {

        return true
    }

    override fun uploadFile() {
        TODO("Not yet implemented")
    }

    override fun getResourceByName(fileName: String): ResourceDTO? {
        val resourceOpt = resourceRepository.findByFileFileName(fileName)
        return if (!resourceOpt.isEmpty){
            resourceMapper.toDto(resourceOpt.get())
        }else null
    }
}