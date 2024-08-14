package autogear.frontapi.service

import autogear.frontapi.entity.Job

interface IJobService {
    fun createNewJob(job: Job): Job
    fun getAllJobs(): List<Job>
    fun getJobById(id: Long): Job
    fun updateJob(id: Long, job: Job): Job
    fun deleteJob(id: Long): Boolean
    fun executePendingJobs()

    fun executeJob(job: Job)

    fun getAllJobByUserId(userId: String): List<Job>
}