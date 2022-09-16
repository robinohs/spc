package dev.robinohs.coordinates

import org.orekit.data.DataContext
import org.orekit.data.DirectoryCrawler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import java.util.*
import javax.annotation.PostConstruct
import kotlin.system.exitProcess

@SpringBootApplication
class CoordinatesApplication {

    @PostConstruct
    private fun init() {
        loadOrekitData()
    }

    companion object {
        fun loadOrekitData() {
            Locale.setDefault(Locale.ENGLISH)
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            println("Loading OrekitData")
            val orekitData = File("./orekit-data")
            if (!orekitData.exists()) {
                exitProcess(1)
            }
            val manager = DataContext.getDefault().dataProvidersManager
            manager.addProvider(DirectoryCrawler(orekitData))
        }
    }

}


fun main(args: Array<String>) {
    runApplication<CoordinatesApplication>(*args)
}


