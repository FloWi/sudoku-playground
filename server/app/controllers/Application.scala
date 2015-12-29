package controllers

import de.flwi.sudoku.model.logic.SudokuParser
import play.api.mvc._
import play.api._

object Application extends Controller {

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
