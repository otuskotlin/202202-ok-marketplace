package ru.otus.otuskotlin.marketplace.springapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService

@Configuration
class ServiceConfig {
    @Bean
    fun serviceAd(): AdService = AdService()

    @Bean
    fun serviceOffer(): OfferService = OfferService()
}
