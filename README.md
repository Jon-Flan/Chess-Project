[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6074901&assignment_repo_type=AssignmentRepo)
# Chessproject
Chess Project for "Introduction to Artificial Intelligence" course. National College of Ireland 4th year <br>
<br>
![Chess Board](https://assets.dicebreaker.com/chess-playing-hand.jpeg/BROK/resize/844%3E/format/jpg/quality/80/chess-playing-hand.jpeg)


# Deliverables
This project is split into different deliverables across multiple continuous assessment assignments.<br>

## CA1
The first deliverable is a functioning chessboard where all pieces move in accordance with the standard rules of chess.<br>
In the first deliverable a winner is achieved when either player captures the oppponents King.<br>
<br>
List of things to achieve in CA1
- Setup the board
- Place pieces on the board
- White Pawn Movements
- Black Pawn Movements
- Knight Movements
- Bishop Movements
- Rook Movements
- Queen Movements
- King Movements
- Player Turn Logic
- Winner Logic
<br>
Taking other pieces and avoiding players own pieces are part of the movement logic and is addressed as such<br>

## Setup & Generic Methods
### Seting up the board
To create a base for the the board a JLayeredPane with a JPanel of specific dimensions placed ontop is used in conjuction with Mouse event listners. <br>
The specific dimensions of the chessboard are used later for evaluting the boundries as well as where pieces are placed. <br>
```Java
// Use a Layered Pane for this application
layeredPane = new JLayeredPane();
getContentPane().add(layeredPane);
layeredPane.setPreferredSize(boardSize);
layeredPane.addMouseListener(this);
layeredPane.addMouseMotionListener(this);

// Add a chess board to the Layered Pane 
chessBoard = new JPanel();
layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
chessBoard.setLayout( new GridLayout(8, 8) );
chessBoard.setPreferredSize( boardSize );
chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
```
<br>

The grid layout is then used to place the chessboard tiles on the chessboard layer. <br>

```Java
// Add the squares to the chess board
for (int i = 0; i < 64; i++) {
    JPanel square = new JPanel( new BorderLayout() );
    chessBoard.add( square );

    int row = (i / 8) % 2;
    if (row == 0)
        square.setBackground( i % 2 == 0 ? Color.white : Color.gray );
    else
        square.setBackground( i % 2 == 0 ? Color.gray : Color.white );
}
```

### Place pieces on the board
Now that we have a chessboard, its time to add the pieces. <br>
The pieces imgs are in the main folder and are added one at a time to the board. <br>

![WhitePawn](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhitePawn.png)
![WhiteKnight](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhiteKnight.png)
![WhiteRook](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhiteRook.png)
![WhiteBishop](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhiteBishup.png)
![WhiteQueen](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhiteQueen.png)
![WhiteKing](https://github.com/ard24ie-courses/ca1-Jon-Flan/blob/main/WhiteKing.png)

<br>
The pieces are added depending on the starting location as indicated by the get.Component() method in the snipped below. <br>
A new JLabel is created and a new image icon used to get the png file containing each piece. <br>

```Java
// Add the white pieces
for(int i=8;i < 16; i++){			
      pieces = new JLabel( new ImageIcon("WhitePawn.png") );
  panels = (JPanel)chessBoard.getComponent(i);
      panels.add(pieces);	        
}
pieces = new JLabel( new ImageIcon("WhiteRook.png") );
panels = (JPanel)chessBoard.getComponent(0);
  panels.add(pieces);
pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
panels = (JPanel)chessBoard.getComponent(1);
  panels.add(pieces);
```
<br> 
With all the pieces we have ourselves a chess board! <br>

![Chess_board](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/board_and_pieces.PNG)

### Basic Movement

Basic movements are just the ability to pick a piece up and move it anywhere on the board. <br>
To achieve this we use listners for mouse events, click, drag, release. <br>
First we need to make sure that when we click the mouse, a piece has been selected.<br>

```Java
// This method is called when we press the Mouse. So we need to find out what piece we have 
	// selected. We may also not have selected a piece!
    public void mousePressed(MouseEvent e){
        chessPiece = null;
        Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel) 
			return;
 
        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel)c;
		initialX = e.getX();
		initialY = e.getY();
		startX = (e.getX()/75);
		startY = (e.getY()/75);
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);

    }
```
In doing this, when the mouse is pressed we get the X and Y coordinates of where the mouse clicked.<br>
Then check if what was clicked was a JPanel, if it was, then its an empty tile on the board so we simply return. <br>
Otherwise it must be a piece, so we get the Label and the tile location, and allow that piece to be dragged around the board. <br>
<br>
As the piece is being dragged around the board, we get its coordinates and set the location (so we can see the piece being dragged)<br>

```Java
// When the mouse is dragged, get the coordinates
    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
         chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
     }
```
When the mouse is released, after an initial check that a piece was actually selected, <br>
we get the tile location and add the piece to that tile.

```Java
// This method is used when the Mouse is released...we need to make sure the move was valid before 
// putting the piece back on the board.
public void mouseReleased(MouseEvent e) {
if(chessPiece == null) return;

chessPiece.setVisible(false);
	Boolean success =false;
Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
	String tmp = chessPiece.getIcon().toString();
	String pieceName = tmp.substring(0, (tmp.length()-4));
	Boolean validMove = false;

	int landingX = (e.getX()/75);
	int landingY = (e.getY()/75);
	int xMovement = Math.abs((e.getX()/75) - startX);
	int yMovement = Math.abs((e.getY()/75) - startY);
	
	// More movement logic below, this mouse event will
	//be the main driver for most of our methods.

```
**_In the above snippet validMove and Success booleans are present but will be discussed in the next section._**
The X and Y movements are also recorded for the helper output to the terminal which will be discussed in a later section.<br>

### Checking for Opponent & Valid Movements
After being able to move pieces around the board we can control if a player can capture a piece. <br>
To do this we create some methods that we can use later when establishing the particular movements of each piece <br>

#### Check if a piece is present: 
We check if the tile we landed on contains a piece. Returning a boolean value.

```Java
// This method checks if there is a piece present on a particular square.
private Boolean piecePresent(int x, int y){
	Component c = chessBoard.findComponentAt(x, y);
	if(c instanceof JPanel){
		return false;
	}
	else{
		return true;
	}
}
```

#### Check if it is an opponents piece:
If the above snippet returns True, we can use the next two methods to get the label of the piece already on <br> 
the tile and see if its Label contains the oppposite colour text. Returning a boolean value. <br>

```Java
// This is a method to check if a piece is a Black piece.
private Boolean checkWhiteOponent(int newX, int newY){
	Boolean oponent;
	Component c1 = chessBoard.findComponentAt(newX, newY);
	JLabel awaitingPiece = (JLabel)c1;
	String tmp1 = awaitingPiece.getIcon().toString();			
	if(((tmp1.contains("Black")))){
		oponent = true;
	}
	else{
		oponent = false; 
	}		
	return oponent;
}	

// This is a method to check if a piece is a White piece.
private Boolean checkBlackOponent(int newX, int newY){
	Boolean oponent;
	Component c1 = chessBoard.findComponentAt(newX, newY);
	JLabel awaitingPiece = (JLabel)c1;
	String tmp1 = awaitingPiece.getIcon().toString();			
	if(((tmp1.contains("White")))){
		oponent = true;
	}
	else{
		oponent = false; 
	}		
	return oponent;
}	

```
#### Valid Move:
Now that the means to check if an opposing players piece is on a tile, the concept of if the move <br>
we are making is Valid comes into play. <br> 
Checking the conditions above while restricting piece movements in the next sections we can determine <br>
if a movement is indeed valid. We do this by,<br>
When a piece is moved: <br>
1. Is there piece present on the tile we moved on to.
2. Is it part of the opposing side.

```Java
if((!piecePresent(e.getX(), e.getY()))){
	validMove = true;					
}
else{
	validMove = false;
}

// and...

if((piecePresent(e.getX(),e.getY()))){
// check if opponents piece
if(checkWhiteOponent(e.getX(), e.getY())){
	validMove = true;
	if(landingY == 7){
		success = true;
	}	
}
else{
	validMove = false;
}
```
#### Capture a piece:
If we returnsed valid maove, **Capture the piece.** <br>
To capture the piece we swap our Jlabel Icon for the one present on the tile. <br>

```Java
// If a piece is being taken
if (c instanceof JLabel){
	Container parent = c.getParent();
	// Remove the piece being taken
	parent.remove(0);
	// Add the new piece
	parent.add( chessPiece );
	
}
// If a piece isn't being taken
else {
	Container parent = (Container)c;
	parent.add( chessPiece );
}
chessPiece.setVisible(true);
```
#### On the Board?
One more piece of generic movement logic we can use, is to make sure the piece we moved <br>
was placed back onto the board. <br>
<br>
For this we can use the size of the board we created earlier, we could also use the tile locations. <br>
for this project I went with the actual bounds of the board as in testing there where some instances that <br>
threw errors. Due to the time restrictions on this submission I moved forward with what worked instead of <br>
debugging the logic that was giving an error. <br>

```Java
// This Method checks if the piece is dropped within the bounds of the board
private Boolean withinBoardBounds(int x, int y){
	Boolean withinBounds = false;
	// if within the size of the board
	if(((x > 0) && (x < 600)) && ((y > 0) && (y < 600)) ){
		withinBounds = true;
	}
	return withinBounds;
}
```

## Piece Movements

### White Pawn Movements
A pawn should have three initial moves allowed:
1. If there are no pieces directly infront it can move one square
2. if there are no pieces two squares infront it can move two squares
3. if there is a piece to one if its immediate diagonals (in a forward direction) and its an enemy piece,
it can move there and capture the piece

After its initial move a pawn has two moves allowed:
1. is move one above 
2. is move three above 

Additional move:
1. if the pawn reaches the opposite side of the board it can be converted into any of its teams pieces

![WhitePawn](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/pawn_movement.PNG)

```Java
// WHITE PAWN
if(pieceName.equals("WhitePawn") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	// if it is in row 1 (starting row)
	if((startY == 1) && ((startX == landingX)&&(((landingY-startY)==1)||(landingY-startY)==2))){	
		// check there are no pieces in the way
		if((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY()-75)))){
			validMove = true;					
		}
		else{
			validMove = false;
		}							
	}
	// if capturing an apponents piece 
	else if((piecePresent(e.getX(),e.getY())) && (((landingX == (startX+1) && (landingY == startY + 1))) || ((landingX == (startX-1)) && (landingY == (startY + 1))))){
		// check if opponents piece
		if(checkWhiteOponent(e.getX(), e.getY())){
			validMove = true;
			if(landingY == 7){
				success = true;
			}	
		}
		else{
			validMove = false;
		}
	}
	else{
		// standard move 1 square forward
		if( (!piecePresent(e.getX(),e.getY())) && ((startX == landingX)&&(landingY-startY)==1) ){	
			validMove = true;
			// if on opponents back row
			if(landingY == 7){
				success = true;
			}			
		}
		else{
			validMove = false;	
		}			
	}			
}
```
### Black Pawn Movements
A pawn should have three initial moves allowed:
1. If there are no pieces directly infront it can move one square
2. if there are no pieces two squares infront it can move two squares
3. if there is a piece to one if its immediate diagonals (in a forward direction) and its an enemy piece,
it can move there and capture the piece

After its initial move a pawn has two moves allowed:
1. is move one above 
2. is move three above 

Additional move:
1. if the pawn reaches the opposite side of the board it can be converted into any of its teams pieces

![BlackPawn](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/pawn_movement_2.PNG)

```Java
// BLACK PAWN
if(pieceName.equals("BlackPawn") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	// if it is in row 1 (starting row)
	if((startY == 6) && ((startX == landingX)&&(((startY - landingY)==1)||(startY - landingY)==2))){	
		// check there are no pieces in the way
		if((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY()+75)))){
			validMove = true;					
		}
		else{
			validMove = false;
		}							
	}
	// if capturing an apponents piece 
	else if((piecePresent(e.getX(),e.getY())) && (((landingX == (startX+1) && (landingY == startY - 1))) || ((landingX == (startX-1)) && (landingY == (startY - 1))))){
		// check if opponents piece
		if(checkBlackOponent(e.getX(), e.getY())){
			validMove = true;
			if(landingY == 0){
				success = true;
			}	
		}
		else{
			validMove = false;
		}
	}
	else{
		// standard move 1 square forward
		if( (!piecePresent(e.getX(),e.getY())) && ((startX == landingX)&&(startY - landingY)==1) ){	
			validMove = true;
			// if on opponents back row
			if(landingY == 0){
				success = true;
			}			
		}
		else{
			validMove = false;	
		}			
}
```
### Knight Movements
A knight should be able to:
1. Travel in an L shape 
	- (1 square left/right, 2 squares forward/back)
	- (2 squares left/right, 1 square forward/back)
2. Jump over pieces in its way and not be affected
3.  Land on an empty square or one occupied by an opponents piece

![Knight](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/knight_movement.PNG)

```Java
// KNIGHT 
if(pieceName.contains("Knight") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	// L Shape movement
	if((xMovement == 1 && yMovement == 2) || (xMovement == 2 && yMovement == 1)){
		// Piece not on landing square
		if(!piecePresent(e.getX(), e.getY())){
			validMove = true;
		}else{
			if(pieceName.contains("White") && checkWhiteOponent(e.getX(), e.getY())){
				validMove = true;
			}
			else if(pieceName.contains("Black") && checkBlackOponent(e.getX(), e.getY())){
				validMove = true;
			}
		}
	}
}
```
### Bishop Movements
A Bishop should be able to:
1. Move in any of the four diagnols from its current poisition as long as:
	- There are no pieces in the path between starting square and landling square
	- The landing square isn't occupied by a piece of the same colour
	- Within the confines of the board

![Bishop](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/bishop_movement.PNG)

```Java
// BISHOP
if(pieceName.contains("Bishup") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	// Diagnol Movement only
	if(xMovement == yMovement){

		Boolean inTheWay = false;
		// Moving right....
		if(startX < landingX){
			// .... and down
			if(startY < landingY){
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX + (i*75)), (initialY+(i*75)))){
						inTheWay = true;
					}
				}
			}
			// ..... and Up
			else{
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX + (i*75)), (initialY-(i*75)))){
						inTheWay = true;
					}
				}
			}	
		}
		// Moving Left....
		else if (startX > landingX){
			// .... and Down
			if(startY < landingY){
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX - (i*75)), (initialY+(i*75)))){
						inTheWay = true;
					}
				}
			}
			// ..... and Up
			else{
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX - (i*75)), (initialY - (i*75)))){
						inTheWay = true;
					}
				}
			}
		}	
		// Nothing in the way and checking landing square
		if(!inTheWay && !piecePresent(e.getX(), e.getY())){
			validMove = true;
		}else if(!inTheWay){
			// if something on the landing square check if its an oppenents piece
			if(pieceName.contains("White") && checkWhiteOponent(e.getX(), e.getY())){
				validMove = true;
			}
			else if(pieceName.contains("Black") && checkBlackOponent(e.getX(), e.getY())){
				validMove = true;
			}
		}
	}
}
```
### Rook Movements
A Rook should be able to: <br>
1. Move in any straight line (Forward, Back, Left, Right)
	 - Any amount of squares within the board : <br>
	 - There are no pieces in the path between starting square and landling square: <br>
	 - The landing square isn't occupied by a piece of the same colour: <br>
	 
Special Movements: <br>
1. Swap places with the King:
	- As long as there are no pieces in the way
	- Neither the King or the Rook have already made a move
	- The path is not blocked by the possible attack of an opponents piece

![Rook](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/rook_movement.PNG)

```Java
// ROOK
if(pieceName.contains("Rook") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	Boolean inTheWay = false;
	// If xmovement is zero and y is greater than zero OR the opposite
	if(((xMovement == 0) && (yMovement > 0)) || (xMovement > 0) && (yMovement == 0)){
		if(startX - landingX != 0){
			if((startX - landingX) > 0){
				// Moving Left
				for(int i = 0; i < xMovement; i++){
					if(piecePresent(Math.abs(initialX - (i*75)), e.getY())){
						inTheWay = true;
						break;
					}
				}
			}
			else{
				// Moving Right
				for(int i = 0; i < xMovement; i++){
					if(piecePresent(Math.abs(initialX + (i*75)), e.getY())){
						inTheWay = true;
						break;
					}
				}
			}
		}else{ 
			if((startY - landingY) > 0){
				// Moving Up
				for(int i = 0; i < yMovement; i++){
					if(piecePresent(e.getX(), Math.abs(initialY - (i*75)))){
						inTheWay = true;
						break;
					}
				}
			}
			else{
				// Moving Down
				for(int i = 0; i < yMovement; i++){
					if(piecePresent(e.getX(), Math.abs(initialY + (i*75)))){
						inTheWay = true;
						break;
					}
				}
			}
		}
	}else{
		inTheWay = true;
	}

	// Nothing in the way and checking landing square
	if(!inTheWay && !piecePresent(e.getX(), e.getY())){
		validMove = true;
	}else if(!inTheWay){
		// if something on the landing square check if its an oppenents piece
		if(pieceName.contains("White") && checkWhiteOponent(e.getX(), e.getY())){
			validMove = true;
		}
		else if(pieceName.contains("Black") && checkBlackOponent(e.getX(), e.getY())){
			validMove = true;
		}
	}

}
```
### Queen Movements
A Queen should be able to: <br>
1. Follow the same movement rules as a Bishop
2. Follow the same movement rules as a Rook

![Queen](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/queen_movement.PNG)

```Java
// QUEEN
if(pieceName.contains("Queen") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	Boolean inTheWay = false;
	// If xmovement is zero and y is greater than zero OR the opposite
	if(((xMovement == 0) && (yMovement > 0)) || (xMovement > 0) && (yMovement == 0)){
		if(startX - landingX != 0){
			if((startX - landingX) > 0){
				// Moving Left
				for(int i = 0; i < xMovement; i++){
					if(piecePresent(Math.abs(initialX - (i*75)), e.getY())){
						inTheWay = true;
						break;
					}
				}
			}
			else{
				// Moving Right
				for(int i = 0; i < xMovement; i++){
					if(piecePresent(Math.abs(initialX + (i*75)), e.getY())){
						inTheWay = true;
						break;
					}
				}
			}
		}else{ 
			if((startY - landingY) > 0){
				// Moving Up
				for(int i = 0; i < yMovement; i++){
					if(piecePresent(e.getX(), Math.abs(initialY - (i*75)))){
						inTheWay = true;
						break;
					}
				}
			}
			else{
				// Moving Down
				for(int i = 0; i < yMovement; i++){
					if(piecePresent(e.getX(), Math.abs(initialY + (i*75)))){
						inTheWay = true;
						break;
					}
				}
			}
		}
	} // Diagnol Movement only
	else if(xMovement == yMovement){

		// Moving right....
		if(startX < landingX){
			// .... and down
			if(startY < landingY){
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX + (i*75)), (initialY+(i*75)))){
						inTheWay = true;
					}
				}
			}
			// ..... and Up
			else{
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX + (i*75)), (initialY-(i*75)))){
						inTheWay = true;
					}
				}
			}	
		}
		// Moving Left....
		else if (startX > landingX){
			// .... and Down
			if(startY < landingY){
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX - (i*75)), (initialY+(i*75)))){
						inTheWay = true;
					}
				}
			}
			// ..... and Up
			else{
				for(int i = 0; i <  xMovement; i++){
					if(piecePresent((initialX - (i*75)), (initialY - (i*75)))){
						inTheWay = true;
					}
				}
			}
		}
	}else{
		inTheWay = true;
	}

	// Nothing in the way and checking landing square
	if(!inTheWay && !piecePresent(e.getX(), e.getY())){
		validMove = true;
	}else if(!inTheWay){
		// if something on the landing square check if its an oppenents piece
		if(pieceName.contains("White") && checkWhiteOponent(e.getX(), e.getY())){
			validMove = true;
		}
		else if(pieceName.contains("Black") && checkBlackOponent(e.getX(), e.getY())){
			validMove = true;
		}
	}
}
```
### King Movements
A King should be able to: <br>
1. Move one square at a time in any direction
2. Not be able to move to square that is already occupied by one of the same colour pieces
3. Not be able to move into a square that puts it into Check
4. Not be able to move to a square adjacent to the opponent King

Special Moves: <br>
1. Castling as in the ROOK Movements, This should only be able to happen if: <br>
	- Neither the King or Rook have already moved  <br>
	- There is a direct line between the King and the Rook  <br>
	- The movement does not move the King across a line of Check  <br>

#### Moving the King
![King1](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/king_movement_1.PNG)

```Java
// KING
if(pieceName.contains("King") && withinBoardBounds(e.getX(), e.getY()) && pieceName.contains(player)){
	if((xMovement ==1 && yMovement == 0) || (xMovement ==0 && yMovement == 1) || (xMovement ==1 && yMovement == 1)){
		if(!checkForKing(e.getX(), e.getY())){
			//checking landing square
			if(!piecePresent(e.getX(), e.getY())){
			validMove = true;
			}else{
				// if something on the landing square check if its an oppenents piece
				if(pieceName.contains("White") && checkWhiteOponent(e.getX(), e.getY())){
					validMove = true;
				}
				else if(pieceName.contains("Black") && checkBlackOponent(e.getX(), e.getY())){
					validMove = true;
				}
			}
		}
	}	
	else{
		validMove = false;
	}
}
```
#### Making sure the king isn't near opposing king
To this we loop through all the tiles surrounding where the player plans to place their King.<br>
By looping through the tiles and checking if a King piece is present or not adds another step<br>
to determining if a move is valid or not. <br>
<br>
While implimenting this we could have explicitley checked ecah square but appeared redundanted,<br>
So nested loop was chosen instead. In testing this null exceptions where raised when kings where<br>
at the edge of the board, so added a check to make sure the check was within bounds using the <br>
within bounds method.<br>
![King](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/king_movement.PNG)
```Java
// This method checks if the opponents King is in an adjacent square when moving the King
private Boolean checkForKing(int checkX, int checkY)
{
	Boolean kingPresent = false;
	// Loop over the X & Y coordinates around the new square
	for(int x = -1; x < 2; x++){
		for(int y = -1; y <2; y++){
			// Adjust the x & y cordinates to check
			int X_Adj = checkX + (x*75);
			int Y_Adj = checkY + (y*75);

			// Check the square being checked is within the bounds of the board (Stop NuLL exception)
			if(withinBoardBounds(X_Adj,Y_Adj)){
				Component c = chessBoard.findComponentAt(X_Adj, Y_Adj);
				if(c instanceof JPanel){
					kingPresent = false;
				}else{
					JLabel awaitingPiece = (JLabel)c;
					String tmp1 = awaitingPiece.getIcon().toString();			
					if(((tmp1.contains("King")))){
					kingPresent = true;
					return kingPresent;
					}
					else{
						kingPresent = false; 
					}	
				}
			}
		}
	}
	return kingPresent;
}
```

## Other Logic
### Player Turn Logic
The rules of chess state that White always moves first and each player makes one move per turn. <br>
This gives us a pattern of movement to work with. We can count the moves performed and use<br>
an odd number for white to move and an even number for black to move. To do this we can use a modulus calcualtion.<br>
Returning the string of either "White" or "Black" depending on if an odd or even number is returned. <br>
We can use this string later in displaying the winner also.<br>
```Java
// This method checks who's turn it is
private String checkTurn(int turn){
	if(turn % 2 == 0){
		player="White";
	}else{
		player="Black";
	}
	return player;
}
```
A turn counter is placed as a global variable and from the valid movement snippets above we can see that it gets <br>
incrimented by 1 on every valid move performed. Giving us the logic for one player turn at a time. <br>
Also to follow as close to the rules of chess, a player should be able to pick up a piece and decide not <br>
to move it by placing it back where it was. **This should not count as a move.** Therefore the below is added when <br>
incrimenting a players turn. <br>
```Java
// If it was a valid move and the piece wasn't placed back in inital position
if((startX != landingX) || (startY != landingY)){
	turn++;
	showHelper(pieceName, xMovement, yMovement, landingX, landingY);
} 
```

### Winner Logic
For the CA1 requirements, a winner is when a player captures another players King. <br>

![Winner](https://github.com/Jon-Flan/Chess-Project/blob/main/imgs/winner.PNG)
<br>
To achieve this,we can add a method that checks if when a piece is captured, <br>
Is it a King?, we already make sure that a player cannot attack any of their own pieces. <br>
So we can simply check for the string containing "King".<br>

```Java
// this method checks if the piece being taken is a king
public Boolean kingCapture(JLabel awaitingPiece){
	String tmp2 = awaitingPiece.getIcon().toString();
	Boolean winner = false;

	// If the piece being taken is a King, display winner message.
	if(tmp2.contains("King")){
		winner = true;
	}
	return winner;
}
```
<br>
For this method to work, when we land on an occupied tile and player has passed all previous checks, <br>
We pass the previous label to the above method and return the boolean. If true, we print the player <br>
that has won and once the message is exited, the game ends.<br>

```Java
if(winner){
	chessPiece.setVisible(true);
	turn++;
	showHelper(pieceName, xMovement, yMovement, landingX, landingY);
	JOptionPane.showMessageDialog(null, player + " wins!" + "\nThank you for playing.");
	System.exit(0);
}	
```
### Further Notes
#### Helper Method
A helper method was created so that once a valid move was completed, a text output to<br>
the console gives details about the move that just took place. <br> 

```Java
// helper output to understand what is going on with piece movements.
// I've placed it with it's own method so to only show on a valid move.
// I've also added the turn number to the output.
public void showHelper(String pieceName, int xMovement, int yMovement, int landingX, int landingY){
	System.out.println("----------------------------------------------");
	System.out.println("The piece that is being moved is : " + pieceName);
	System.out.println("The starting coordinates are : " + "( " +startX+ "," +startY+ " )");
	System.out.println("The xMovement is : " +xMovement);
	System.out.println("The yMovement is : " +yMovement);
	System.out.println("The landing coordinates are : " + "( " +landingX+ "," +landingY+ " )");
	System.out.println("Move number: " +turn);
	System.out.println("----------------------------------------------");
}
```
