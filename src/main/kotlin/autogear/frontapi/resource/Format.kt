package autogear.frontapi.resource


abstract class Format(
    val value: String
){
    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Format

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}