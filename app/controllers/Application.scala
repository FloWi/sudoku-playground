package controllers

import logic.{Sudoku, SudokuParser}
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def sudoku(inputString: String) = Action {

    SudokuParser.parse(inputString) match {
      case Left(error) => BadRequest(error)
      case Right(sudoku) => Ok(views.html.sudokuBoard(sudoku))
    }
  }
}
