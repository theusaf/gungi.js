# Gungi

![gameboard](https://i.imgur.com/CkNhrgdl.png)

Gungi is a two-player fictional strategy game created by Yoshihiro Togashi and is featured in the popular manga/anime series Hunter X Hunter. It draws aspects from similar games such as chess, shogi, and go, but adds an interesting 3rd dimension to gameplay by allowing pieces to be stacked on top of each other forming towers. A detailed ruleset was not specified in Togashi's work, however thanks to the Hunter X Hunter community the game has become a reality.

## Features/How To Play

Implemented game rules established by reddit users [u/Zaneme](https://www.reddit.com/user/Zaneme) and [u/squal777](https://www.reddit.com/user/squal777). See [rules](https://www.docdroid.net/P4r6Fvq/gungi.pdf).

#### Draft Phase
- Players take turns placing pieces from their stockpile anywhere on to the board within their first 3 ranks (rows) until satisfied with an initial setup (press the R key when ready to proceed to the next phase)
- The game board flips after each turn
- Black places first
- The Marshall must be the first piece placed
- A player may have a maximum of 26 pieces on the board at a time
- Pieces may be stacked in this phase according to their individual rules
- A player can be placed in check during this phase and must place their next piece as to protect their Marshall
- When both players are readied the game phase starts

#### Game Phase
- White takes first turn
- A turn can be used for four things
  - Move: move a piece to an empty square (towers don't move, they only increase range of mobility for the top)
  - Attack: if an enemy square is within your range of mobility you may attack and capture that piece. Resulting in your piece occupying the place where the enemy had been.
  - Stack: if a friendly or enemy piece is within your range of mobility you can stack on top of that piece to form a tower (only the top controls the tower)
  - Place: if you have less than the maximum number of pieces, you may place a piece from your stockpile (not including captured pieces) anywhere on the board with a few exceptions:
    - Multiple pawns cannot be placed in the same file (column)
    - Pieces cannot be placed within the first 3 ranks of the opponents side of the board
- The game ends when the Marshall is mated. A player can forfeit the game by pressing the F key


## TODO

- menu screen
- help screen
- save game
- load save
- show piece/tower info on hover instead of click
- check for mate / stalemate
- screen resolution
- port to js to make accessible for online play

---
*Any feedback is appreciated.*

**arigatou gozaimasu**
