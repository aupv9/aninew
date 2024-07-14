package autogear.frontapi.controller

import autogear.frontapi.mapper.ResourceDTO
import autogear.frontapi.service.IResourceService
import common.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/resources")
class ResourceController(
    private val resourceService: IResourceService,
//    private val driveService: IDriveService
) {

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).CREATE_RESOURCE)")
    @GetMapping("/")
    fun getResources(): ResponseEntity<CommonResponse<Collection<ResourceDTO>>> {
        return ResponseEntity.ok(
            CommonResponse(
                data = resourceService.getResources(),
                message = "Get resources successfully"
            ))
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).CREATE_RESOURCE)")
    @PostMapping
    fun createResource(@RequestBody resource: ResourceDTO): ResponseEntity<CommonResponse<ResourceDTO>> {
        return ResponseEntity.ok(
            CommonResponse(
                data = resourceService.addResource(resource),
                message = "Create resource successfully"
            ))
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).CREATE_RESOURCE)")
    @PostMapping("/bulk")
    fun createResources(@RequestBody resources: Collection<ResourceDTO>): ResponseEntity<CommonResponse<Collection<ResourceDTO>>> {
        return ResponseEntity.ok(
            CommonResponse(
                data =  resources.map {resourceService.addResource(it) },
                message = "Create resources successfully"
            ))
    }

//    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).CREATE_RESOURCE)")
//    @PostMapping("/bucket")
//    fun getResourceTypes(@RequestParam(name = "bucketName") bucketName: String): ResponseEntity<Any> {
//        return ResponseEntity.ok(
//            CommonResponse(
//                data = driveService.createFolder(bucketName, ""),
//                message = "Get resource types successfully"
//            ))
//    }

//    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).GET_ALL_RESOURCE)")
//    @GetMapping("/bucket")
//    fun getAllResources(): ResponseEntity<Any> {
//        return ResponseEntity.ok(
//            CommonResponse(
//                data = driveService.getFolders(),
//                message = "Get all resources successfully"
//            )
//        )
//    }

//    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.ResourcePermission).CREATE_RESOURCE)")
//    @PostMapping(value = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//    fun uploadFile(
////        @Parameter(description = "File to upload", required = true, schema = Schema(type = "String", format = "binary"), name = "image")
//        @RequestParam("image") file: MultipartFile): ResponseEntity<Any> {
//        val mediaContent  = InputStreamContent(
//            file.contentType,
//            file.inputStream
//        )
//        return ResponseEntity.ok(
//            CommonResponse(
//                data = driveService.uploadFile(file.originalFilename!!, "1R9zaoQXEy_a_dQogGKymy6ec0BzKsAUo", mediaContent),
//                message = "Upload file successfully"
//            )
//        )
//    }

}