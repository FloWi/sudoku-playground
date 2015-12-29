package de.flwi.sudoku.model.logic

object Coordinate {
  def apply(row: Byte, column: Byte): Coordinate = {
    val box = row / 3 * 3 + column / 3
    Coordinate(row.toByte, column.toByte, box.toByte)
  }

  def apply(index: Byte): Coordinate = {
    val row = index / 9
    val column = index % 9
    val box = row / 3 * 3 + column / 3
    Coordinate(row.toByte, column.toByte, box.toByte)
  }
}

case class Coordinate(row: Byte, column: Byte, box: Byte) {
  override def toString: String = s"r$row|c$column|b$box"
}

sealed trait Cell {
  def coordinate: Coordinate
}

case class SolvedCell(value: Byte, coordinate: Coordinate) extends Cell

case class UnsolvedCell(coordinate: Coordinate) extends Cell

case class CandidateList(coordinate: Coordinate, candidates: Set[Byte])

case class Sudoku(cells: Array[Cell]) {

  def apply(index: Int): Cell = {
    cells(index)
  }

  def apply(rowIndex: Int, columnIndex: Int): Cell = {
    cells(SudokuParser.calculateIndex(rowIndex, columnIndex))
  }

  def apply(coordinate: Coordinate): Cell = {
    cells(SudokuParser.calculateIndex(coordinate))
  }

  def calcCandidates(coordinate: Coordinate): Option[CandidateList] = {
    apply(coordinate) match {
      case _: SolvedCell => None
      case _ =>
        val takenValuesFromTheNeighbours = findNeighbours(coordinate).collect {
          case SolvedCell(value, _) => value
        }

        val candidates: Set[Byte] = 1.to(9)
          .map(_.toByte)
          .toSet.--(takenValuesFromTheNeighbours)

        Some(CandidateList(coordinate, candidates))
    }
  }

  def findNeighbours(coordinate: Coordinate): IndexedSeq[Cell] = {
    val Coordinate(row, column, box) = coordinate

    0.to(80)
      .map(apply)
      .filter(c => c != coordinate && (c.coordinate.row == row || c.coordinate.column == column || c.coordinate.box == box) )
  }

  def getCellsOfBox(boxIndex: Int): Array[Cell] = {
    cells.filter(_.coordinate.box == boxIndex)
  }

  def getCellsOfRow(rowIndex: Int): Array[Cell] = {
    cells.filter(_.coordinate.row == rowIndex)
  }

  def getCellsOfColumn(columnIndex: Int): Array[Cell] = {
    cells.filter(_.coordinate.column == columnIndex)
  }
}

object SudokuParser {

  def calculateIndex(rowIndex: Int, columnIndex: Int): Int = {
    rowIndex * 9 + columnIndex
  }

  def calculateIndex(coordinate: Coordinate): Int = {
    calculateIndex(coordinate.row, coordinate.column)
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
            val coordinate = Coordinate(index.toByte)
            c match {
              case '0' => UnsolvedCell(coordinate)
              case i if i > '0' && i <= '9' => SolvedCell(i.toString.toInt.toByte, coordinate)
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
