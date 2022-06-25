package ru.otus.otuskotlin.marketplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

@Configuration
class ServiceConfig {

    @Bean
    fun repoAd(): IAdRepository = AdRepoInMemory()

    @Bean
    fun serviceAd(repoAd: IAdRepository): AdService = AdService(repoAd)
}
