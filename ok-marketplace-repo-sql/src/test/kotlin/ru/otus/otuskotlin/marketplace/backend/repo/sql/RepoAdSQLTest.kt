package ru.otus.otuskotlin.marketplace.backend.repo.sql

import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdCreateTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdDeleteTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdReadTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdSearchTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class RepoAdSQLCreateTest : RepoAdCreateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLReadTest : RepoAdReadTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLSearchTest : RepoAdSearchTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}
