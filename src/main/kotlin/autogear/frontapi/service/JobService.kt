package autogear.frontapi.service

import autogear.frontapi.entity.Job
import autogear.frontapi.entity.Status
import autogear.frontapi.repository.ExecutionRepository
import autogear.frontapi.repository.JobRepository
import autogear.frontapi.repository.UserRepository
import autogear.frontapi.repository.UserSocialRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.util.Date
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.concurrent.TimeUnit

@EnableScheduling
@Service
class JobService @Autowired constructor(
    private val jobRepository: JobRepository,
    private val executionRepository: ExecutionRepository,
    private val userRepository: UserRepository,
    private val userSocialRepository: UserSocialRepository,
    private val clientFactory: SocialMediaClientFactory
): IJobService {

    private val logger = org.slf4j.LoggerFactory.getLogger(JobService::class.java)

    @Transactional(rollbackFor = [Exception::class])
    override fun createNewJob(job: Job): Job {
        val usernamePasswordAuthenticationToken = SecurityContextHolder.getContext().authentication as UsernamePasswordAuthenticationToken
        job.userId = usernamePasswordAuthenticationToken.principal as String
        val savedJob = jobRepository.save(job)
        return savedJob
    }

    override fun getAllJobs(): List<Job> {
        TODO("Not yet implemented")
    }

    override fun getJobById(id: Long): Job {
        TODO("Not yet implemented")
    }

    override fun updateJob(id: Long, job: Job): Job {
        TODO("Not yet implemented")
    }

    override fun deleteJob(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES )
    override fun executePendingJobs() {
        logger.debug("Executing pending jobs")
        println("Executing pending jobs")
        try {
            val pendingJobs = jobRepository.findByStatus(Status.PENDING)
            for (job in pendingJobs) {
                if(Date().after(job.schedule.startTime)){
                    this.executeJob(job)
                }
            }
        } catch (e: Exception) {
            logger.debug(e.message)
        }
    }

    override fun executeJob(job: Job) {
        logger.debug("Executing pending jobs id: {}", job.id)

        val user = userRepository.findByUserID(job.userId).orElseThrow { RuntimeException("User not found") }
        val credential = userSocialRepository.findByUserId(user.userID).orElseThrow { RuntimeException("Credential not found") }
            ?: throw RuntimeException("Credentials not found for platform ${job.platform}")

        val client = clientFactory.createClient(job.platform, credential.attributes?.get("accessToken")!!)
        val result = client.post(job.payload)

        job.status = if (result) Status.COMPLETED else Status.FAILED
        jobRepository.save(job)
        logger.debug("Finished executing pending jobs id: {}", job.id)
    }

    override fun getAllJobByUserId(userId: String): List<Job> {
        return jobRepository.findByUserId(userId)
    }

}