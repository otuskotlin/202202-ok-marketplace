package ru.otus.otuskotlin.marketplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplSettings
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

@Configuration
class ServiceConfig {

    @Bean
    fun repoAdTest(): IAdRepository = AdRepoInMemory()

    @Bean
    fun repoAdProd(): IAdRepository = AdRepoInMemory()

    @Bean
    fun mkplSettings(): MkplSettings = MkplSettings(
        repoTest = repoAdTest(),
        repoProd = repoAdProd(),
    )

    @Bean
    fun serviceAd(settings: MkplSettings): AdService = AdService(settings)
}
