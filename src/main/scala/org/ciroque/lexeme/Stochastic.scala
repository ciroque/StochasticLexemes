package org.ciroque.lexeme

import akka.actor.Actor

import scala.io.Source
import scala.util.Random

class Stochastic extends Actor {
  override def receive: Receive = {
    case request: LexemeRequest =>
      sender() ! Stochastic.selectRandomWords(request)
  }
}

object Stochastic {
  lazy val words: Array[String] = loadWords()
  lazy val random: Random = new Random(System.nanoTime())

  def selectRandomWords(request: LexemeRequest): Lexemes = {
    candidateWords(request) match {
      case Array() => Lexemes(List[String]())
      case candidates =>
        val indexes = Random.shuffle(candidates.indices.toList).take(request.howMany)
        var lexes = indexes.map(index => candidates(index)).toList
        Lexemes(lexes)
    }
  }

  def loadWords(): Array[String] = Source.fromResource("words").getLines().toArray.sorted

  private def candidateWords(request: LexemeRequest): Array[String] = {
    val filtered: Array[String] = if(request.maxWordLength == -1) words
    else words.filter(word => word.length <= request.maxWordLength)

    (if(request.minWordLength == -1) filtered
    else filtered.filter(word => word.length >= request.minWordLength)).toArray[String]
  }
}
