package kr.co.hs.cleanarchitecturesample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import java.net.URL

class TestBookStoreRepository : BookStoreRepository {

    private val dummyData = mapOf(
        "0" to object : BookDetailEntity {
            override val authors = "저자0"
            override val publisher = "출판사0"
            override val pages = "0"
            override val year: String = "2024"
            override val rating: String = "5"
            override val desc: String = "설명0"
            override val url: URL = URL("https://google.com")
            override val key: String = "0"
            override val title: String = "제목0"
            override val subtitle: String = "부제0"
            override val price: String = "0원"
            override val imageUrl: URL =
                URL("https://p-j-m.github.io/design-compass/assets/img/design-system/icon/main@2x.png")
        },
        "1" to object : BookDetailEntity {
            override val authors = "저자1"
            override val publisher = "출판사1"
            override val pages = "1"
            override val year: String = "2024"
            override val rating: String = "4"
            override val desc: String = "설명1"
            override val url: URL = URL("https://google.com")
            override val key: String = "1"
            override val title: String = "제목1"
            override val subtitle: String = "부제1"
            override val price: String = "1원"
            override val imageUrl: URL =
                URL("https://p-j-m.github.io/design-compass/assets/img/design-system/icon/main@2x.png")
        },
        "2" to object : BookDetailEntity {
            override val authors = "저자2"
            override val publisher = "출판사2"
            override val pages = "2"
            override val year: String = "2024"
            override val rating: String = "3"
            override val desc: String = "설명2"
            override val url: URL = URL("https://google.com")
            override val key: String = "2"
            override val title: String = "제목2"
            override val subtitle: String = "부제2"
            override val price: String = "2원"
            override val imageUrl: URL =
                URL("https://p-j-m.github.io/design-compass/assets/img/design-system/icon/main@2x.png")
        },
        "3" to object : BookDetailEntity {
            override val authors = "저자3"
            override val publisher = "출판사3"
            override val pages = "3"
            override val year: String = "2024"
            override val rating: String = "3"
            override val desc: String = "설명3"
            override val url: URL = URL("https://google.com")
            override val key: String = "3"
            override val title: String = "subject3"
            override val subtitle: String = "부제3"
            override val price: String = "3원"
            override val imageUrl: URL =
                URL("https://p-j-m.github.io/design-compass/assets/img/design-system/icon/main@2x.png")
        },
        "4" to object : BookDetailEntity {
            override val authors = "저자4"
            override val publisher = "출판사4"
            override val pages = "4"
            override val year: String = "2024"
            override val rating: String = "4"
            override val desc: String = "설명4"
            override val url: URL = URL("https://google.com")
            override val key: String = "4"
            override val title: String = "subject4"
            override val subtitle: String = "부제4"
            override val price: String = "4원"
            override val imageUrl: URL =
                URL("https://p-j-m.github.io/design-compass/assets/img/design-system/icon/main@2x.png")
        }
    )

    private val perPage = 2

    override fun search(query: String): Flow<BookSummaryEntity> = search(query, 0)
    override fun search(query: String, page: Int): Flow<BookSummaryEntity> = dummyData
        .filter { it.value.title.contains(query) }
        .values
        .toList()
        .let { list ->
            val requestPage = page.takeIf { it > 0 } ?: 1
            val start = perPage.times((requestPage - 1)).takeIf { it < list.size } ?: list.size
            val end = perPage.times(requestPage).takeIf { it < list.size } ?: list.size
            list.subList(start, end)
        }
        .asFlow()

    override fun getNewBooks(): Flow<BookSummaryEntity> = flow { emit(dummyData.get("0")!!) }

    override fun getBookDetails(bookSummaryEntity: BookSummaryEntity): Flow<BookDetailEntity> =
        flow { emit(dummyData.get(bookSummaryEntity.key)!!) }
}