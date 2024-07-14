package autogear.frontapi.service

import autogear.frontapi.entity.Resource
import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList

interface IDriveService {
    fun getDriveInfo(): String
    fun createFolder(folderName: String, parentId: String): Resource?
    fun uploadFile(fileMetadata: File, parentId: String, mediaContent: InputStreamContent?): Resource?
    fun createDrive(name: String): String
    fun getFolders(): Collection<FileList>
    fun uploadFileWithName(fileMetadata: File, parentName: String, mediaContent: InputStreamContent?): Resource?

    fun getFolderById(folderId: String): File?
    fun getFileById(fileId: String): File?

    fun sharingFile(fileId: String): Boolean
}