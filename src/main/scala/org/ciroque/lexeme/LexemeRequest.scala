package org.ciroque.lexeme


trait LexemeRequest {
  val howMany: Int
  val maxWordLength: Int
  val minWordLength: Int
}

case class AdjectiveRequest(howMany: Int, maxWordLength: Int = -1, minWordLength: Int = -1) extends LexemeRequest

case class NounRequest(howMany: Int, maxWordLength: Int = -1, minWordLength: Int = -1) extends LexemeRequest

case class WordRequest(howMany: Int, maxWordLength: Int = -1, minWordLength: Int = -1) extends LexemeRequest
