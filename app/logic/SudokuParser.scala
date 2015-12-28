package logic

case class Coordinate(row: Byte, column: Byte, box: Byte) {
  override def toString: String = s"r$row|c$column|b$box"
}

sealed trait Cell {
  def coordinate: Coordinate
}

case class SolvedCell(value: Byte, coordinate: Coordinate) extends Cell

case class UnsolvedCell(candidates: IndexedSeq[Byte], coordinate: Coordinate) extends Cell

case class Sudoku(cells: Array[Cell])

object SudokuParser {
  def calculateCoordinate(index: Int): Coordinate = {
    val row = index / 9
    val column = index % 9
    val box = row * 3 + column / 3
    Coordinate(row.toByte, column.toByte, box.toByte)
  }

  def parse(inputString: String): Either[String, Sudoku] = {

    if (inputString.length > 81) Left("input too long")
    else if (inputString.length < 81) Left("input too short")
    else
      try {
        val cells: Array[Cell] = inputString
          .toCharArray
          .zipWithIndex
          .map { case (c, index) =>
            val coordinate = calculateCoordinate(index)
            c match {
              case '0' => UnsolvedCell(Vector.empty[Byte], coordinate)
              case i if i > '0' && i <= '9' => SolvedCell(i.toByte, coordinate)
              case i => throw new IllegalArgumentException(s"'$i' is not valid at $coordinate")
            }
          }
          .toArray

        Right(Sudoku(cells.toArray))
      } catch {
        case ex: Exception => Left(ex.getMessage)
      }
  }
}
