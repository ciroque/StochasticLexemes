FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/StochasticLexemes

WORKDIR /opt/StochasticLexemes

ENTRYPOINT ["sh"]
