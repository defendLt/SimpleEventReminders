package com.platdmit.simpleeventreminders.domains.converters


interface EventsConverter<DbModel, DomainModel>
{
    fun fromDomainToDb(model: DomainModel) : DbModel
    fun fromDbToDomain(dbModel: DbModel) : DomainModel
}