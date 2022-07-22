# 202202-ok-marketplace

Учебный проект
курса [Kotlin Backend Developer](https://otus.ru/lessons/kotlin/?int_source=courses_catalog&int_term=programming).
Marketplace -- это площадка, на которой пользователи выставляют предложения и потребности. Задача площадки --
предоставить наиболее подходящие варианты в обоих случаях: для предложения -- набор вариантов с потребностями, для
потребностей -- набор вариантов с предложениями.

## Что нового в проекте
По сравнению с предыдущим потоком появились следующие изменения:
1. Проекты поддерживают две версии API (v1 - Jackson, v2 - KMP).
2. Ktor скомпилирован в двух версиях: Jvm и Native.
3. В связи с санкциями, пришлось отказаться от AWS (и DynamoDB) в пользу KNative.
4. Появился раздел по конкурентному доступу к базам данных и распределенным приложениям.
5. Добавлен репозиторий с графовой базой данных.

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
3. [m1l4-dsl](m1l4-dsl) - Предметно ориентированные языки (DSL)
4. [m1l5-coroutines](m1l5-coroutines) - Асинхронное и многопоточное программирование с корутинами
5. [m1l7-multiplatform](m1l7-multiplatform) - Kotlin Multiplatform и интероперабельность с JVM, JS
6. [m2l2-testing](m2l2-testing) - Тестирование проекта, TDD, MDD

## Транспортные модели, API

1. [specs](specs) - описание API в форме OpenAPI-спецификаций
2. [ok-marketplace-api-v1-jackson](ok-marketplace-api-v1-jackson) - Генерация первой версии транспортных модеелй с
   Jackson
3. [ok-marketplace-api-v2-kmp](ok-marketplace-api-v2-kmp) - Генерация второй версии транспортных модеелй с KMP
4. [ok-marketplace-common](ok-marketplace-common) - модуль с общими классами для модулей проекта. В частности, там
   располагаются внутренние модели и контекст.
5. [ok-marketplace-api-v1-jackson](ok-marketplace-api-v1-jackson) - Мапер между внутренними моделями и моделями API v1
6. [ok-marketplace-api-v2-kmp](ok-marketplace-api-v2-kmp) - Мапер между внутренними моделями и моделями API v1

## Фреймворки и транспорты

1. [ok-marketplace-services](ok-marketplace-services) - Сервис. Служит оберткой для модуля бизнес-логики. Подключается
   всеми фреймворками (модулями `*-app-*`)
1. [ok-marketplace-app-spring](ok-marketplace-app-spring) - Приложение на Spring Framework
1. [ok-marketplace-app-ktor](ok-marketplace-app-ktor) - Приложение на Ktor JVM
1. [ok-marketplace-app-ktor-native](ok-marketplace-app-ktor-native) - Приложение на Ktor Native
1. [ok-marketplace-app-ktor-common](ok-marketplace-app-ktor-common) - Общая логика для Ktor JVM и Ktor Native
1. [ok-marketplace-app-knative](ok-marketplace-app-knative) - Скрипты запуска приложения в KNative
1. [ok-marketplace-app-rabbit](ok-marketplace-app-rabbit) - Микросервис на RabbitMQ
1. [ok-marketplace-app-kafka](ok-marketplace-app-kafka) - Микросервис на Kafka

## Модули бизнес-логики

1. [ok-marketplace-stubs](ok-marketplace-stubs) - Стабы для ответов сервиса
1. [ok-marketplace-biz](ok-marketplace-biz) - Модуль бизнес-логики приложения

## Хранение, репозитории, базы данных

1. [ok-marketplace-repo-test](ok-marketplace-repo-test) - Базовые тесты для репозиториев всех баз данных
2. [ok-marketplace-repo-inmemory](ok-marketplace-repo-inmemory) - Репозиторий на базе кэша в памяти для тестирования
3. [ok-marketplace-repo-sql](ok-marketplace-repo-sql) - Репозиторий на базе PostgreSQL
4. [ok-marketplace-repo-cassandra](ok-marketplace-repo-cassandra) - Репозиторий на базе Cassandra
5. [ok-marketplace-repo-gremlin](ok-marketplace-repo-gremlin) - Репозиторий на базе Apache TinkerPop Gremlin и ArcadeDb
