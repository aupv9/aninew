package autogear.frontapi.service

import autogear.frontapi.mapper.ResourceDTO

interface IResourceService {
    fun getResources(): Collection<ResourceDTO>
    fun getResource(id: String): ResourceDTO?
    fun addResource(resource: ResourceDTO): ResourceDTO
    fun updateResource(resource: ResourceDTO): ResourceDTO
    fun deleteResource(id: String)
    fun createBucket(bucketName: String): Boolean

    fun uploadFile()

    fun getResourceByName(fileName: String): ResourceDTO?

}