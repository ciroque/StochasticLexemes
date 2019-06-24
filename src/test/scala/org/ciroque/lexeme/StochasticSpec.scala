package org.ciroque.lexeme

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, Matchers, WordSpecLike}
import scala.concurrent.duration._

class StochasticSpec
  extends TestKit(ActorSystem("LexemeStochasticSpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Stochastic" must {
    "should respond to a WordRequest for 0 words" in {
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(0)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual 0
    }

    "should respond to a WordRequest for 3 words" in {
      val wordCount = 3
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(wordCount)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual wordCount
    }

    "should respect maxWordLength" in {
      val wordCount = 7
      val maxWordLength = 3
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(wordCount, maxWordLength)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual wordCount
      words.forall(word => word.length <= maxWordLength) shouldBe true
    }

    "should respect minWordLength" in {
      val wordCount = 7
      val minWordLength = 3
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(wordCount, -1, minWordLength)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual wordCount
      words.forall(word => word.length >= minWordLength) shouldBe true
    }

    "should respect maxWordLength and minWordLength" in {
      val wordCount = 7
      val maxWordLength = 6
      val minWordLength = 3
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(wordCount, maxWordLength, minWordLength)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual wordCount
      words.forall(word => word.length >= minWordLength && word.length <= maxWordLength) shouldBe true
    }

    "should return empty list for contradictory min / max values" in {
      val wordCount = 7
      val maxWordLength = 3
      val minWordLength = 6
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! WordRequest(wordCount, maxWordLength, minWordLength)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual 0
    }
  }
}
