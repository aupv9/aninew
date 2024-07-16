package autogear.frontapi.utils

import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream

//object ByteArrayToMultipartFileConverter {
//    fun convert(
//        bytes: ByteArray,
//        name: String,
//        originalFilename: String,
//        contentType: String
//    ): MultipartFile {
//        return MockMultipartFile(
//            name,
//            originalFilename,
//            contentType,
//            ByteArrayInputStream(bytes)
//        )
//    }
//}