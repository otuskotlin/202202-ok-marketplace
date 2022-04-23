# 202202-ok-marketplace

Учебный проект
курса [Kotlin Backend Developer](https://otus.ru/lessons/kotlin/?int_source=courses_catalog&int_term=programming).
Marketplace -- это площадка, на которой пользователи выставляют предложения и потребности. Задача площадки --
предоставить наиболее подходящие варианты в обоих случаях: для предложения -- набор вариантов с потребностями, для
потребностей -- набор вариантов с предложениями.

## Учебный маркетинг приложения

Считаем, что целевая аудитория учебного сервиса -- это средние и крупные предприятия, желающие (а) продать свою
высокотехнологичную продукцию или (б) найти поставщиков для ее производства.

### Гипотетический портрет пользователя для службы Потребностей

1. Высококвалифицированный инженер среднего или высшего звена, который ищет поставщиков компонентов для разрабатываемого
   продукта.
1. Мужчина от 30 до 50 лет
1. С высшим образованием
1. С критическим складом ума, со склонностью сопоставлять цифры, технические характеристики и не ведется на броскую
   рекламу
1. Как правило женат, имеет детей (как маленьких, так и взрослых), обеспечен, имеет жилье и автомобиль

### Гипотетический портрет пользователя для службы Предложений

Можно выделить несколько сегментов пользователей

1. Высококвалифицированный и опытный сотрудник отдела продаж, знающий как продукт, так и технические детали
    1. Мужчина от 30 до 50 лет
    1. С высшим образованием, знает технологические нюансы продаваемой продукции и как эта продукция используется
    1. Знаком лично с большинством клиентов, большая часть всех продаж через личные контакты
1. Начинающий или низкоквалифицированный продажник
    1. Молодой мужчина до 30-35 лет
    1. Без высшего образование или без опыта работы в соответствующей отрасли
    1. Делает продажи посредством холодных звонков
    1. и т.д.
1. И т.д.

## Описание MVP

![](imgs/design-layout.png)

### Функции (эндпониты)

1. CRUDS (create, read, update, delete, search) для объявлений (ad)
1. ad.offers (опционально)

### Описание сущности ad

1. Info
    1. Title
    1. Description
    1. Owner
    1. Visibility
1. DealSide: Demand/Proposal
1. ProductType (гаечный ключ, ...)
1. IPropertyValues - интерфейс, описывающий различные реализации продуктов
    1. Болты - учебный пример
        1. Длина
        1. Толщина
        1. Резьба
        1. Форма

# Структура проекта

## Подпроекты для занятий по языку Kotlin

1. [m1l1-hello-world](m1l1-hello-world) - Вводное занятие, создание первой программы на Kotlin
   [//]: # (1. [ok-m1l3-oop]&#40;ok-m1l3-oop&#41; - Объектно-ориентированное программирование)
1. [m1l4-dsl](m1l4-dsl) - Предметно ориентированные языки (DSL)
1. [m1l5-coroutines](m1l5-coroutines) - Асинхронное и многопоточное программирование с корутинами
   [//]: # (1. [ok-m1l6-flows-and-channels]&#40;ok-m1l6-flows-and-channels&#41; - Асинхронное и многопоточное
   программирование с каналами)
1. [m1l7-multiplatform](m1l7-multiplatform) - Kotlin Multiplatform и интероперабельность с JVM, JS
1. [m2l2-testing](m2l2-testing) - Тестирование проекта, TDD, MDD

## Транспортные модели, API

1. [specs](specs) - описание API в форме OpenAPI-спецификаций
1. [ok-marketplace-api-v1-jackson](ok-marketplace-api-v1-jackson) - Генерация первой версии транспортных модеелй с
   Jackson
1. [ok-marketplace-api-v2-kmp](ok-marketplace-api-v2-kmp) - Генерация второй версии транспортных модеелй с KMP

[//]: # (1. [ok-marketplace-be-transport-mapping-openapi]&#40;ok-marketplace-be-transport-mapping-openapi&#41; - мапинг OpenAPI)

[//]: # (   транспортных моделей во внутренние модели)

[//]: # (1. [ok-marketplace-mp-transport-mp]&#40;ok-marketplace-mp-transport-mp&#41; - подпроект с транспортными моделями на Kotlin)

[//]: # (   Multiplatform)

[//]: # (1. [ok-marketplace-be-transport-mapping-openapi]&#40;ok-marketplace-be-transport-mapping-openapi&#41; - мапинг KMP транспортных)

[//]: # (   моделей во тренние модели)

## Фреймворки и транспорты

[//]: # (1. [ok-marketplace-be-service-openapi]&#40;ok-marketplace-be-service-openapi&#41; - Сервис для OpenAPI-реализации. Предназначен)

[//]: # (   для унификации вызовов бизнес-логики при использовании разных транспортных моделей)

[//]: # (1. [ok-marketplace-be-app-spring]&#40;ok-marketplace-be-app-spring&#41; - Приложение на Spring Framework)

[//]: # (1. [ok-marketplace-be-app-ktor]&#40;ok-marketplace-be-app-ktor&#41; - Приложение на Ktor)

[//]: # (1. [ok-marketplace-be-app-serverless]&#40;ok-marketplace-be-app-serverless&#41; - Бессерверное приложение для AWS)

[//]: # (1. [ok-marketplace-be-app-rabbit]&#40;ok-marketplace-be-app-rabbit&#41; - Микросервис на RabbitMQ)

[//]: # (1. [ok-marketplace-be-app-kafka]&#40;ok-marketplace-be-app-kafka&#41; - Микросервис на Kafka)

## Модули бизнес-логики

[//]: # (1. [ok-marketplace-be-stubs]&#40;ok-marketplace-be-stubs&#41; - Стабы для ответов сервиса)

[//]: # (2. [ok-marketplace-mp-common-cor]&#40;ok-marketplace-mp-common-cor&#41; - Библиотека для реализации шаблона Цепочка обязанностей)

[//]: # (3. [ok-marketplace-be-common-validation]&#40;ok-marketplace-be-common-validation&#41; - Библиотека для валидации)

## Хранение, репозитории, базы данных

[//]: # (1. [ok-marketplace-be-repo-test]&#40;ok-marketplace-be-repo-test&#41; - Базовые тесты для репозиториев всех баз данных)

[//]: # (2. [ok-marketplace-be-repo-inmemory]&#40;ok-marketplace-be-repo-inmemory&#41; - Репозиторий на базе EhCache для тестирования)

[//]: # (3. [ok-marketplace-be-repo-sql]&#40;ok-marketplace-be-repo-sql&#41; - Репозиторий на базе PostgreSQL)

[//]: # (4. [ok-marketplace-be-repo-cassandra]&#40;ok-marketplace-be-repo-cassandra&#41; - Репозиторий на базе Cassandra)

[//]: # (5. [ok-marketplace-be-repo-dynamo]&#40;ok-marketplace-be-repo-dynamo&#41; - Репозиторий на базе AWS Dynamo)
