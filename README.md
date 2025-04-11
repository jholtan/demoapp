# Demo Application


![Production](https://github.com/jholtan/demoapp/actions/workflows/deploy.yaml/badge.svg)


-javaagent:./opentelemetry-javaagent.jar -Dotel.instrumentation.common.default-enabled=false -Dotel.resource.attributes=service.name=demoapp,service.version=1.0.0
