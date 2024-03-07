package corbella83.aoc

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private fun remoteUrl(day: Int, year: Int) = URI("https://adventofcode.com/$year/day/$day/input")
private fun localFolder(year: Int): File {
    var dir = System.getProperty("user.dir")
    if (!dir.endsWith("y$year")) dir += "/y$year"
    return File(dir, "inputs").apply { mkdir() }
}

interface ResourceLoader {
    fun load(day: Int, part: Int): Resource
}

class RealResourceLoader(private val year: Int) : ResourceLoader {

    override fun load(day: Int, part: Int): Resource {
        return File(localFolder(year), "d${day}.txt")
            .apply { if (!exists()) download(day, year) }
            .inputStream()
            .bufferedReader()
            .readLines()
            .let { Resource(it) }
    }

    private fun File.download(day: Int, year: Int) {
        val session = System.getenv("aocSession") ?: throw Exception("You need to set an environment variable (aocSession) with your session id")
        val request = HttpRequest.newBuilder(remoteUrl(day, year))
            .header("Cookie", "session=$session")
            .build()

        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream())
        if (response.statusCode() < 300) {
            outputStream().use { response.body().transferTo(it) }
        } else {
            throw Exception("Input file not found for Day $day ($year)")
        }
    }
}

class TestResourceLoader : ResourceLoader {

    override fun load(day: Int, part: Int): Resource {
        val stream = Resource::class.java.classLoader.getResourceAsStream("test${day}_$part.txt")
            ?: Resource::class.java.classLoader.getResourceAsStream("test${day}.txt")
            ?: throw Exception("Test file not found for Day $day")

        return stream.bufferedReader()
            .readLines()
            .let { Resource(it) }
    }
}
