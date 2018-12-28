package org.ciroque.lexeme

import akka.actor.Actor

import scala.io.Source
import scala.util.Random

class Stochastic extends Actor {
  override def receive: Receive = {
    case LexemeRequest(howMany) =>
      sender() ! Stochastic.selectRandomWords(howMany)
  }
}

object Stochastic {
  lazy val words: Array[String] = loadWords()
  lazy val random: Random = new Random(System.nanoTime())

  def selectRandomWords(howMany: Int): Lexemes = {
    val lexemes = (for(
      index <- 1 to howMany ;
      randomIndex = random.nextInt(words.length)
    ) yield words(randomIndex)).toList
    Lexemes(lexemes)
  }

  def loadWords(): Array[String] = {
    Source.fromResource("words").getLines().toArray.sorted
  }
}
