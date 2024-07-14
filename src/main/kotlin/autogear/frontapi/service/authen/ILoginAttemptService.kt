package autogear.frontapi.service.authen

interface ILoginAttemptService{
    fun isBlocked(): Boolean
    fun loginFailed(key: String)
}
