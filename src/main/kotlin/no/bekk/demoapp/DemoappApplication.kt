package no.bekk.demoapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoappApplication

fun main(args: Array<String>) {
	runApplication<DemoappApplication>(*args)
}
