package de.flwi.sudoku

import de.flwi.sudoku.model.logic.{SudokuParser, Sudoku}
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.annotation.JSExport

import scalatags.Text.all._

@JSExport
object ExampleJS {

  @JSExport
  def main(): Unit = {
    g.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }

  /** Computes the square of an integer.
    *  This demonstrates unit testing.
    */
  def square(x: Int): Int = x*x
}



object SudokuJS extends js.JSApp {

  def main(): Unit = {

  }

  @JSExport
  def renderSudoku(sudokuString: String) = {
    SudokuParser.parse(sudokuString) match {
      case Left(error) => jQuery("#sudoku-content").append(s"Sudoku kapott: $error")
      case Right(sudoku) => render(sudoku, sudokuString)
    }
  }

  def render(sudoku: Sudoku, sudokuString: String) = {

    val content: String = h3(s"Render of sudoku: $sudokuString").render
    jQuery("#sudoku-content").append(content)



  }
}
