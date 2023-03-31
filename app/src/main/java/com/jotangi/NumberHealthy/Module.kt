package com.jotangi.NumberHealthy

import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.api.watch.WatchViewModel
import org.koin.dsl.module


val viewModule = module {
    single {
        WatchViewModel(get())
    }
}

val repoModule = module {
    single {
        WatchApiRepository()
    }
}

val appModule = listOf(
    viewModule,
    repoModule
)


val viewModule2 = module {
    single {
        BookViewModel(get())
    }
}

val repoModule2 = module {
    single {
        BookApiRepository()
    }
}

val appModule2 = listOf(
    viewModule2,
    repoModule2
)