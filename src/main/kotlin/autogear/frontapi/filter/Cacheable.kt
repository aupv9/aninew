package autogear.frontapi.filter

interface Cacheable {
    fun checkCache(key: String): Boolean
    fun handleWithCache(key: String)
}