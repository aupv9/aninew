package autogear.frontapi.mapper


interface IMapper<D, E> {
    fun toEntity(resource: D): E
    fun toDto(entity: E): D

}