package no.bekk.demoapp.roll

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.Timer.ResourceSample
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ThreadLocalRandom

@RestController
class RollController(@Autowired val meterRegistry: MeterRegistry) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/rolldice")
  fun index(@RequestParam("player") player: String?): String {
    val result = this.getRandomNumber(1, 6)
    count("demoapp.rolldice.event") {
      if (!player.isNullOrBlank()) {
        logger.info("{} is rolling the dice: {}", player, result)
      } else {
        logger.info("Anonymous player is rolling the dice: {}", result)
      }
    }
    return result.toString()
  }

  fun getRandomNumber(min: Int, max: Int): Int = ThreadLocalRandom.current().nextInt(min, max +1)

  fun <T> count(metricName: String, process: ResourceSample.() -> T): T =
    Timer.resource(meterRegistry, metricName)
      .use {
        process(it)
      }
}
