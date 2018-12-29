FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/StochasticLexemes

WORKDIR /opt/StochasticLexemes

COPY ./target/scala-2.12/stochastic-lexemes-assembly-1.0.jar .

EXPOSE 7890

ENTRYPOINT ["java", "-jar", "stochastic-lexemes-assembly-1.0.jar"]
