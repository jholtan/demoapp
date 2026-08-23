package no.bekk.demoapp.model

import kotlin.time.Clock
import kotlin.time.Instant

data class Resource(
  val id: Long? = null,
  var name: String = "",
  var description: String = "",
  var createdBy: String? = null,
  var updatedBy: String? = null,
  var createdAt: Instant = Clock.System.now(),
  var updatedAt: Instant = Clock.System.now()
) {
  fun markUpdated(updatedBy: String) {
    this.updatedBy = updatedBy
    this.updatedAt = Clock.System.now()
  }
}

