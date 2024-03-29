package ru.otus.otuskotlin.marketplace.biz

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import ru.otus.otuskotlin.marketplace.biz.general.initRepo
import ru.otus.otuskotlin.marketplace.biz.general.initStatus
import ru.otus.otuskotlin.marketplace.biz.general.operation
import ru.otus.otuskotlin.marketplace.biz.general.prepareResult
import ru.otus.otuskotlin.marketplace.biz.permissions.accessValidation
import ru.otus.otuskotlin.marketplace.biz.permissions.chainPermissions
import ru.otus.otuskotlin.marketplace.biz.permissions.frontPermissions
import ru.otus.otuskotlin.marketplace.biz.permissions.searchTypes
import ru.otus.otuskotlin.marketplace.biz.repo.*
import ru.otus.otuskotlin.marketplace.biz.stubs.*
import ru.otus.otuskotlin.marketplace.biz.validation.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*

class MkplAdProcessor(private val settings: MkplSettings = MkplSettings()) {
    suspend fun exec(ctx: MkplContext) = BuzinessChain.exec(ctx.apply { settings = this@MkplAdProcessor.settings })

    companion object {
        private val BuzinessChain = rootChain<MkplContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание объявления", MkplCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    // validate all fields for html TAGs to avoid
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                worker {
                    title = "Инициализация adRepoRead"
                    on { state == MkplState.RUNNING }
                    handle {
                        adRepoRead = adValidated
                        adRepoRead.ownerId = principal.id
                    }
                }
                accessValidation("Вычисление прав доступа")

                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
//                worker {
//                    title = "Подготовка ответа"
//                    on { state == MkplState.RUNNING }
//                    handle {
//                        state = MkplState.FINISHING
//                        adResponse = adRepoDone
//                    }
//                }
            }
            operation("Получить объявление", MkplCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")

                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == MkplState.RUNNING }
                        handle { adRepoDone = adRepoRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Изменить объявление", MkplCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")

                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    repoCheckReadLock("Проверяем блокировку")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Удалить объявление", MkplCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") {
                        adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
                    worker("Очистка lock") { adValidating.lock = MkplAdLock(adValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")

                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    repoCheckReadLock("Проверяем блокировку")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Поиск объявлений", MkplCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }

                    finishAdFilterValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")

                repoSearch("Поиск объявления в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
            operation("Поиск подходящих предложений для объявления", MkplCommand.OFFERS) {
                stubs("Обработка стабов") {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.copy() }
                    stubOffersSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")

                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareOffers("Подготовка данных для поиска предложений")
                    repoOffers("Поиск предложений для объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
