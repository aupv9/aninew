package autogear.frontapi.annotation

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
    value = [
        ApiResponse(responseCode = "200", description = "Confirm Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]),
        ApiResponse(responseCode = "400", description = "Verify Invalid",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)])
    ]
)
annotation class OpenAPIForJson(val desc: String){


}
