//package autogear.frontapi.service
//
//import autogear.frontapi.entity.*
//import autogear.frontapi.enum.GoogleDriveMimeType
//import autogear.frontapi.repository.ProviderRepository
//import autogear.frontapi.repository.ResourceRepository
//import com.google.api.client.googleapis.batch.json.JsonBatchCallback
//import com.google.api.client.googleapis.json.GoogleJsonError
//import com.google.api.client.googleapis.json.GoogleJsonResponseException
//import com.google.api.client.http.HttpHeaders
//import com.google.api.client.http.InputStreamContent
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.model.File
//import com.google.api.services.drive.model.FileList
//import com.google.api.services.drive.model.Permission
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Service
//import java.util.*
//
//@Service
//class DriveService(
//    private val driveService: Drive,
//    private val providerRepository: ProviderRepository,
//    private val resourceRepository: ResourceRepository
//): IDriveService {
//
//    private val logger = LoggerFactory.getLogger(this::class.java)
//
//    override fun getDriveInfo(): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun createFolder(folderName: String, parentId: String): Resource? {
//        val fileMetadata = File()
//        fileMetadata.name = folderName
//        fileMetadata.originalFilename = folderName
//        fileMetadata.mimeType = GoogleDriveMimeType.FOLDER.value
//        if(parentId.isNotBlank()){
//            val folderResource = resourceRepository.findByFileProviderId(parentId)
//            folderResource.ifPresent {
//                fileMetadata.parents =  listOf(
//                    it.file.providerId
//                )
//            }
//        }
//        return this.uploadFileToDrive(fileMetadata, null)
//    }
//
//    override fun getFolderById(folderId: String): File? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getFileById(fileId: String): File? {
//        this.shareFile(fileId, "readbook2312345@gmail.com", "1234")
//        return driveService.files().get(fileId).setFields("webViewLink").setSupportsAllDrives(true).execute()
//    }
//
//    override fun sharingFile(fileId: String): Boolean {
//        return this.shareFile(fileId, "owner-645@manga-428507.iam.gserviceaccount.com", "manga")
//    }
//
//    private fun uploadFileToDrive(fileMetadata: File, mediaContent: InputStreamContent?): Resource?{
//        try {
//            val file: File  = if(mediaContent != null) {
//                driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id, name, originalFilename, mimeType, size, webViewLink, thumbnailLink")
//                    .execute()
//            }else{
//                driveService.files().create(fileMetadata)
//                    .setFields("id, name, originalFilename, mimeType, size, webViewLink, thumbnailLink")
//                    .execute()
//            }
//            println("File ID: " + file.id)
//            if(file.id.isNotBlank()){
//                val fileInfo =  FileInfo(
//                    fileName = file.name,
//                    contentType = file.mimeType,
//                    size = file.size.toLong(),
//                    path = file.webViewLink,
//                    providerId = file.id,
//                    uploadDate = Date(),
//                    thumbnail = if(file.thumbnailLink != null) file.thumbnailLink else ""
//                )
//                val resourceNew = Resource(provider = ProviderType.DRIVE, file = fileInfo)
//                return resourceRepository.save(resourceNew)
//            }
//        }catch (e: GoogleJsonResponseException){
//            println("Unable to upload file: "
//                    + e.details.code + " " + e.details.message
//            )
//        }
//        return null
//    }
//
//    override fun uploadFile(fileMetadata: File, parentId: String, mediaContent: InputStreamContent?): Resource? {
//        if(parentId.isNotBlank()){
//            val folderResource = resourceRepository.findByFileProviderId(parentId)
//            folderResource.ifPresent {
//                fileMetadata.parents =  listOf(
//                    it.file.providerId
//                )
//            }
//        }
//        return this.uploadFileToDrive(fileMetadata, mediaContent)
//    }
//
//    override fun createDrive(name: String): String {
//        val drive = com.google.api.services.drive.model.Drive()
//        drive.name = name
//        try {
//            driveService.drives().create(UUID.randomUUID().toString(), drive).execute()
//        }catch (e: GoogleJsonResponseException){
//            println("Unable to create drive: "
//                    + e.details.code + " " + e.details.message
//            )
//        }
//        return drive.id ?: ""
//    }
//
//    override fun getFolders():  Collection<FileList> {
//        var pageToken: String? = ""
//        val fileLists = mutableListOf<FileList>()
//        do {
//            val result = driveService.files().list()
//                .setQ("mimeType='application/vnd.google-apps.folder'")
//                .setSpaces("drive")
//                .setFields("nextPageToken, files(id, name)")
//                .setPageToken(
//                    pageToken
//                )
//                .execute()
//            pageToken = result.nextPageToken;
//            fileLists.add(result)
//        }while (pageToken != null)
//        return fileLists
//    }
//
//    override fun uploadFileWithName(fileMetadata: File, parentName: String, mediaContent: InputStreamContent?): Resource? {
//        if(parentName.isNotBlank()){
//            val folderResource = resourceRepository.findByFileFileName(parentName)
//            folderResource.ifPresent {
//                fileMetadata.parents =  listOf(
//                    it.file.providerId
//                )
//            }
//        }
//        return this.uploadFileToDrive(fileMetadata, mediaContent)
//    }
//
//    fun createShareableLink(fileId: String): File {
//        // Create a permission to make the file public
//        val permission = Permission()
//            .setType("anyone")
//            .setRole("reader")
//
//        // Apply the permission to the file
//        driveService.permissions().create(fileId, permission).execute()
//
//        // Get the shareable link
//        val file = driveService.files().get(fileId).setFields("webViewLink").execute()
//        return file
//    }
//
//
//
//    private fun shareFile(fileId: String, realUser: String, realDomain: String): Boolean{
//
//        val ids =  mutableListOf<String>()
//        val userPermission = Permission()
//            .setType("user")
//            .setRole("writer")
//        userPermission.emailAddress = realUser
//        val batchRequest = driveService.batch()
//
//        val callback = object : JsonBatchCallback<Permission>() {
//            override fun onSuccess(p0: Permission?, p1: HttpHeaders?) {
//                logger.info("Permission ID: " + p0!!.id)
//                ids.add(p0.id)
//            }
//            override fun onFailure(e: GoogleJsonError?, p1: HttpHeaders?) {
//                logger.error(
//                    "Unable to share file: "
//                            + e!!.code + " " + e.message
//                )
//            }
//        }
//
//        try {
//            driveService.permissions().create(fileId, userPermission).setFields("id").queue(
//                batchRequest, callback
//            )
//
//            val domainPermission = Permission()
//                .setType("anyone")
//                .setRole("reader")
//
//            driveService.permissions().create(fileId, domainPermission).setFileId("id").queue(batchRequest, callback)
//
//            batchRequest.execute()
//            return ids.isNotEmpty()
//        }catch (e: GoogleJsonResponseException){
//            logger.error(
//                "Unable to modify permission: "
//                        + e.details.code + " " + e.details.message
//            )
//        }
//
//
//        return false
//    }
//}
