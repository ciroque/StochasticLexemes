FROM openjdk:11-jre-alpine

RUN mkdir -p /opt/StochasticLexemes

WORKDIR /opt/StochasticLexemes

ENTRYPOINT ["sh"]
