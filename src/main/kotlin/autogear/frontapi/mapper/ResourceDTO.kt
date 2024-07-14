package autogear.frontapi.mapper

import autogear.frontapi.entity.ResourceMetadata
import autogear.frontapi.entity.ResourceType

data class ResourceDTO(
    var id: String? = null,
    var description: String? = null,
    val type: ResourceType? = null,
    val title: String? = null,
    val file: FileDto,
    val metadata: ResourceMetadata? = null
)
