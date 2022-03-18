package structuredconcurency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main() {
    val vocabulary = Vocabulary()


//    coroutineScope {
        findWordsAsync(vocabulary, "привет", "любимый")
//    }

}

// Loads sequantially
// Time measure == time of each function++
suspend fun findWordsSlowly(vocabulary: Vocabulary, word: String, word2: String) {
    val word = vocabulary.find(word)
    val word2 = vocabulary.find(word2, withTime = 5000)

    println("Make some other stuff")
    println("Found $word && $word2")
    println("End some other stuff")
}


suspend fun findWordsAsync(vocabulary: Vocabulary, word: String, word2: String) = coroutineScope {
    val deferred = async {
        vocabulary.find(word)
        coroutineScope {
            println()
        }
    }
    val deferred2 = async { vocabulary.find(word2, withTime = 5000) } // Resume (with GlobalScope)

    println("Make some other stuff")

//    println("deffered is active? ${deferred.isActive}; deffered2 is active? ${deferred2.isActive}")
    runCatching {
        println("Found ${deferred.await()} && ${deferred2.await()}")
    }.onFailure {
        println("Deffered still running? ${deferred.isActive}")
        println("Deffered is canceled? ${deferred.isCancelled}")
        println("Deffered2 still running? ${deferred2.isActive}")
        println("Deffered2 is canceled? ${deferred2.isCancelled}")
    }.getOrThrow()
    println("End some other stuff")
}