FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/StochasticLexemes

WORKDIR /opt/StochasticLexemes

COPY ./target/scala-2.12/stochastic-lexemes-assembly-1.0.jar .

ENTRYPOINT ["sh"]
