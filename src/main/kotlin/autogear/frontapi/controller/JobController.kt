package autogear.frontapi.controller

import autogear.frontapi.entity.Job
import autogear.frontapi.service.JobService
import common.CommonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/jobs")
class JobController @Autowired constructor(
    private val jobService: JobService
) {

    @PostMapping
    fun createJob(@RequestBody job: Job): ResponseEntity<CommonResponse<Any>> {
        val createdJob = jobService.createNewJob(job)
        return ResponseEntity.ok(CommonResponse(
            code = 200,
            message = "Job created successfully",
            data = createdJob
        ))
    }

    @GetMapping
    fun getAllJobs(): ResponseEntity<CommonResponse<List<Job>>> {
        val usernamePasswordAuthenticationToken = SecurityContextHolder.getContext().authentication as UsernamePasswordAuthenticationToken
        val userId = usernamePasswordAuthenticationToken.principal as String
        val jobs = jobService.getAllJobByUserId(userId)
        return ResponseEntity.ok(CommonResponse(
            code = 200,
            message = "Jobs fetched successfully",
            data = jobs
        ))
    }
}