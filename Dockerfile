FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/StochasticLexemes

WORKDIR /opt/StochasticLexemes

COPY ./target/scala-2.13/stochastic-lexemes-assembly-1.0.1.jar .

EXPOSE 7890

ENTRYPOINT ["java", "-jar", "stochastic-lexemes-assembly-1.0.1.jar"]
