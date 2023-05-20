#NQUEENS-SOLVER
In chess, a queen can attack horizontally, vertically, and diagonally.
The N-queens problem asks: How can N queens be placed on an NxN chessboard so that no two of them attack each other?
The idea is to place queens one by one in different columns, starting from the leftmost column. When we place a queen in a column, 
we check for clashes with already placed queens. In the current column, if we find a row for which there is no clash,
we mark this row and column as part of the solution.
If we do not find such a row due to clashes, then we backtrack and return false
