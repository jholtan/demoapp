package no.bekk.demoapp.feature

import dev.openfeature.sdk.EvaluationContext
import dev.openfeature.sdk.Hook
import dev.openfeature.sdk.HookContext
import dev.openfeature.sdk.ImmutableContext
import java.util.*

class UserInfoHook : Hook<Any> {
  override fun before(ctx: HookContext<Any>?, hints: MutableMap<String, Any>?): Optional<EvaluationContext> {
    return Optional.of(ImmutableContext("asdfsadf") )
  }
}
