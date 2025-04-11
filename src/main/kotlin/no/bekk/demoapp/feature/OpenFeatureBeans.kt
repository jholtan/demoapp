package no.bekk.demoapp.feature

import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProvider
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions
import dev.openfeature.sdk.OpenFeatureAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenFeatureBeans {

  @Bean
  fun openFeatureAPI(): OpenFeatureAPI {
    val options = GoFeatureFlagProviderOptions.builder().endpoint("http://localhost:1031/").build()
    val provider = GoFeatureFlagProvider(options)

    OpenFeatureAPI.getInstance().setProviderAndWait(provider)
    OpenFeatureAPI.getInstance().addHooks(UserInfoHook())
    return OpenFeatureAPI.getInstance()
  }
}




