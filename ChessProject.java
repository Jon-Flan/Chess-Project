import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
	int startX;
	int startY;
	int initialX;
	int initialY;
	JPanel panels;
	JLabel pieces;
	int turn = 0;
	String player = "White";
	

    public ChessProject(){
        Dimension boardSize = new Dimension(600, 600);
 
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
		pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
		panels = (JPanel)chessBoard.getComponent(6);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(2);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(5);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKing.png") );
		panels = (JPanel)chessBoard.getComponent(3);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
		panels = (JPanel)chessBoard.getComponent(4);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteRook.png") );
		panels = (JPanel)chessBoard.getComponent(7);
	    panels.add(pieces);

		// Add the black pieces
		for(int i=48;i < 56; i++){			
       		pieces = new JLabel( new ImageIcon("BlackPawn.png") );
			panels = (JPanel)chessBoard.getComponent(i);
	        panels.add(pieces);	        
		}
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(56);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(57);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(62);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(58);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(61);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKing.png") );
		panels = (JPanel)chessBoard.getComponent(59);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackQueen.png") );
		panels = (JPanel)chessBoard.getComponent(60);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(63);
	    panels.add(pieces);	
    }
	
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

	// This Method checks if the piece is dropped within the bounds of the board
	private Boolean withinBoardBounds(int x, int y){
		Boolean withinBounds = false;
		// if within the size of the board
		if(((x > 0) && (x < 600)) && ((y > 0) && (y < 600)) ){
			withinBounds = true;
		}
		return withinBounds;
	}
	
	// This method checks who's turn it is
	private String checkTurn(int turn){
		if(turn % 2 == 0){
			player="White";
		}else{
			player="Black";
		}
		return player;
	}

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
   
	// When the mouse is dragged, get the coordinates
    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
         chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
     }
     
 	
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

		// function to check if it was correct players to move
		checkTurn(turn);

		/* KING MOVEMENTS
			A King should be able to:
				1. Move one square at a time in any direction - done
				2. Not be able to move to square that is already occupied by one of the same colour pieces - done
				3. Not be able to move into a square that puts it into Check
				4. Not be able to move to a square adjacent to the opponent King - done

				Special Moves:
				1. Castling as in the ROOK Movements, This should only be able to happen if:
						- Neither the King or Rook have already moved
						- There is a direct line between the King and the Rook
						- The movement does not move the King across a line of Check
		
		*/ 

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

		/* QUEEN MOVEMENTS
			A Queen should be able to:
				1. Follow the same movement rules as a Bishop - done
				2. Follow the same movement rules as a Rook - done
		*/

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

		/* ROOK MOVEMENTS
			A Rook should be able to:
				1. Move in any straight line (Forward, Back, Left, Right)
					 - Any amount of squares within the board - done
					 - There are no pieces in the path between starting square and landling square - done
					 - The landing square isn't occupied by a piece of the same colour - done
			
			Special Movements:
				1. Swap places with the King:
					- As long as there are no pieces in the way
					- Neither the King or the Rook have already made a move
					- The path is not blocked by the possible attack of an opponents piece
		*/

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

		/* BISHOP MOVEMENTS
			A Bishop should be able to:
				1. Move in any of the four diagnols from its current poisition as long as:
					- There are no pieces in the path between starting square and landling square -done
					- The landing square isn't occupied by a piece of the same colour - done
					- Within the confines of the board - done
		*/

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

		/*	KNIGHT MOVEMENTS
			A knight should be able to:
				1 - Travel in an L shape 
					- (1 square left/right, 2 squares forward/back) - done
					- (2 squares left/right, 1 square forward/back) - done
				2 - Jump over pieces in its way and not be affected - done
				3 - Land on an empty square or one occupied by an opponents piece - done
		*/
		
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
		/*	PAWN MOVEMENTS
			A pawn should have three initial moves allowed:
				1 - If there are no pieces directly infront it can move one square - done
				2 - if there are no pieces two squares infront it can move two squares - done
				3 - if there is a piece to one if its immediate diagonals (in a forward direction) and its an enemy piece,
					it can move there and capture the piece - done

			After its initial move a pawn has two moves allowed:
				1 - is move one above - done
				2 - is move three above - done

			Additional move:
				1 - if the pawn reaches the opposite side of the board it can be converted into any of its teams pieces
		*/

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
		}
		// If not a valid move
		if(!validMove){		
			int location=0;
			if(startY ==0){
				location = startX;
			}
			else{
				location  = (startY*8)+startX;
			}
			String pieceLocation = pieceName+".png"; 
			pieces = new JLabel( new ImageIcon(pieceLocation) );
			panels = (JPanel)chessBoard.getComponent(location);
		    panels.add(pieces);			
		}
		else{
			// If a Pawn gets to opponents back row
			if(success){
				// If its a white Pawn.
				if(pieceName.equals("WhitePawn")){
					int location = 56 + (e.getX()/75);
					if (c instanceof JLabel){
						Container parent = c.getParent();
						parent.remove(0);
						// Get the piece name from the JLabel
						JLabel awaitingPiece = (JLabel)c;
						Boolean winner = kingCapture(awaitingPiece);
						pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
						parent = (JPanel)chessBoard.getComponent(location);
						parent.add(pieces);	
						// If it was a valid move and the piece wasn't placed back in inital position
						if((startX != landingX) || (startY != landingY)){
							if(winner){
								turn++;
								showHelper(pieceName, xMovement, yMovement, landingX, landingY);
								JOptionPane.showMessageDialog(null, player + " wins!" + "\nThank you for playing.");
								System.exit(0);
							}else{
								turn++;
								showHelper(pieceName, xMovement, yMovement, landingX, landingY);
							}
						}			
					}
					else{
						Container parent = (Container)c;
						pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
						parent = (JPanel)chessBoard.getComponent(location);
						parent.add(pieces);	 
						// If it was a valid move and the piece wasn't placed back in inital position
						if((startX != landingX) || (startY != landingY)){
							turn++;
							showHelper(pieceName, xMovement, yMovement, landingX, landingY);
						}           	
					}
				}
				// If its a Black Pawn
				else if(pieceName.equals("BlackPawn")){
					int location = 0 + (e.getX()/75);
					if (c instanceof JLabel){
						Container parent = c.getParent();
						parent.remove(0);
						// Get the piece name from the JLabel
						JLabel awaitingPiece = (JLabel)c;
						Boolean winner = kingCapture(awaitingPiece);
						pieces = new JLabel( new ImageIcon("BlackQueen.png") );
						parent = (JPanel)chessBoard.getComponent(location);
						parent.add(pieces);	
						// If it was a valid move and the piece wasn't placed back in inital position
						if((startX != landingX) || (startY != landingY)){
							if(winner){
								turn++;
								showHelper(pieceName, xMovement, yMovement, landingX, landingY);
								JOptionPane.showMessageDialog(null, player + " wins!" + "\nThank you for playing.");
								System.exit(0);
							}else{
								turn++;
								showHelper(pieceName, xMovement, yMovement, landingX, landingY);
							}
						}	
					}
					else{
						Container parent = (Container)c;
						pieces = new JLabel( new ImageIcon("BlackQueen.png") );
						parent = (JPanel)chessBoard.getComponent(location);
						parent.add(pieces);
						// If it was a valid move and the piece wasn't placed back in inital position
						if((startX != landingX) || (startY != landingY)){
							turn++;
							showHelper(pieceName, xMovement, yMovement, landingX, landingY);
						}           	
					}
				}		
			}
			// Else its a valid move from different piece
			else{
				// If a piece is being taken
				if (c instanceof JLabel){
	            	Container parent = c.getParent();
					// Remove the piece being taken
	            	parent.remove(0);
					// Add the new piece
	            	parent.add( chessPiece );
					// Get the piece name from the JLabel
					JLabel awaitingPiece = (JLabel)c;
					Boolean winner = kingCapture(awaitingPiece);
					if(winner){
						chessPiece.setVisible(true);
						turn++;
						showHelper(pieceName, xMovement, yMovement, landingX, landingY);
						JOptionPane.showMessageDialog(null, player + " wins!" + "\nThank you for playing.");
						System.exit(0);
					}		
	        	}
				// If a piece isn't being taken
	        	else {
	            	Container parent = (Container)c;
	            	parent.add( chessPiece );
	        	}
	    		chessPiece.setVisible(true);
				// If it was a valid move and the piece wasn't placed back in inital position
				if((startX != landingX) || (startY != landingY)){
					turn++;
					showHelper(pieceName, xMovement, yMovement, landingX, landingY);
				}									
			}
		}
    }
 
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e) {
    }
 	

	//	Main method that gets the ball moving.
    public static void main(String[] args) {
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
     }
}


