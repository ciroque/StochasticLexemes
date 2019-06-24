package org.ciroque.web

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import org.ciroque.lexeme._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait Protocols {
  import DefaultJsonProtocol._
  implicit val lexemesFormat: RootJsonFormat[Lexemes] = jsonFormat1(Lexemes)
  implicit val jsonApiFormat: RootJsonFormat[JsonApi] = jsonFormat1(JsonApi)
}


trait CORSHandler{
  private val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`("Authorization",
      "Content-Type", "X-Requested-With")
  )
  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(corsResponseHeaders)
  }
  //this handles preflight OPTIONS requests.
  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(`Access-Control-Allow-Methods`(OPTIONS, GET)))
  }
  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }
  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCORSHeaders(response: HttpResponse):HttpResponse =
    response.withHeaders(corsResponseHeaders)
}

trait Service extends Protocols with CORSHandler {
  import scala.concurrent.duration._
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  def config: Config
//  val logger: LoggingAdapter
  implicit def requestTimeout: Timeout = Timeout(5 seconds)
  val stochastic: ActorRef = system.actorOf(Props(new Stochastic()))

  lazy val wordsRoute: Route = corsHandler({
    parameters("howMany".as[Int] ? 3, "maxWordLength".as[Int] ? -1, "minWordLength".as[Int] ? -1) {
      (howMany, maxWordLength, minWordLength) =>
      pathPrefix("api") {
        path("words") {
          get {
            val eventualLexemes = (stochastic ? WordRequest(howMany, maxWordLength, minWordLength)).mapTo[Lexemes]
            complete(eventualLexemes.flatMap(l => Future(JsonApi(l))))
          }
        }
      }
    }
  })

  lazy val adjectivesRoute: Route = corsHandler({
    parameters("howMany".as[Int] ? 3, "maxWordLength".as[Int] ? -1, "minWordLength".as[Int] ? -1) {
      (howMany, maxWordLength, minWordLength) =>
      pathPrefix("api") {
        pathPrefix("words") {
          path("adjectives") {
            get {
              val eventualLexemes = (stochastic ? AdjectiveRequest(howMany, maxWordLength, minWordLength)).mapTo[Lexemes]
              complete(eventualLexemes.flatMap(l => Future(JsonApi(l))))
            }
          }
        }
      }
    }
  })

  lazy val nounsRoute: Route = corsHandler({
    parameters("howMany".as[Int] ? 3, "maxWordLength".as[Int] ? -1, "minWordLength".as[Int] ? -1) {
      (howMany, maxWordLength, minWordLength) =>
      pathPrefix("api") {
        pathPrefix("words") {
          path("nouns") {
            get {
              val eventualLexemes = (stochastic ? NounRequest(howMany, maxWordLength, minWordLength)).mapTo[Lexemes]
              complete(eventualLexemes.flatMap(l => Future(JsonApi(l))))
            }
          }
        }
      }
    }
  })

  lazy val routes: Route = wordsRoute ~ adjectivesRoute ~ nounsRoute
}
