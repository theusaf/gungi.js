const path = require("path");
const foulDetector = require(path.join(__dirname,"src/foul.js"));
const notationGetter = require(path.join(__dirname,"src/notation.js"));
const pieces = require(path.join(__dirname,"src/pieces.js"));
const checkDetector = require(path.join(__dirname,"src/checkmate.js"));

class Gungi {
  constructor() {
    this.initGame();
    this.turn = false; // false = black, true = white
    this.stage = "draft";
  }
  // creates the 9x9x3 board and resets the game
  initGame() {
    this.board = [];
    for(let x = 0; x < 9; ++x) {
      let col = [];
      for(let y = 0; y < 9; ++y){
        let row = [];
        for(let z = 0; z < 3; ++z){
          row.push(null);
        }
        col.push(row);
      }
      this.board.push(col);
    }
  }
  // adds a piece to the board.
  placePiece(x,y,piece) {

  }
  // detects fouls
  checkFoul() {

  }
  // detects check
  detectCheck() {

  }
}

module.exports = Gungi;
