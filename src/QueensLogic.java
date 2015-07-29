/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class QueensLogic {
    private int x = 0;
    private int y = 0;
    private int size;
    private int[][] board;
    private BDDFactory fact;
    private BDD True;
    private BDD False;
    private BDD superBDD;

    public QueensLogic() {
       //constructor
    }
    
    public void createStuff(int N) {
    	
    	
    }
    
    

    public void initializeGame(int size) {
        this.x = size;
        this.y = size;
        this.size = size;
        this.board = new int[x][y];
        
    	fact = JFactory.init(2000000,200000);
    	fact.setVarNum(size*size);
    	True = fact.one();
    	False = fact.zero();
    	superBDD = True;
    	
    	printBoard();
    	
    	// Create individual index rules
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			makeRule(i, j);
    		}
    	}
    	
    	// Create N queen rule
    	NQueenRule();
    	System.out.println("Before Nate Dogg");
    	printBoard();
    	
    	NateDoggAndWarrenG();
    	System.out.println("After Nate Dogg");
    	printBoard();
    }
    
    public void NQueenRule() {
    	
    	for (int i = 0; i < size; i++) {
    		BDD tempBDD = False;
    		for (int j = 0; j < size; j++) {
    			tempBDD = tempBDD.or(fact.ithVar(getVarNum(i, j)));
    		}
    		
    		superBDD = superBDD.and(tempBDD);
    	}
    	
    	
    	
    }
    
    public void makeRule(int x, int y) {
    	
    	// This method creates the rules for every array index
    	
    	// Create BDDs for rules
		BDD tempBDD = True;
    	
    	// Horizontally
    	for (int i = 0; i < size; i++) {
    		if (i != x) {
    			tempBDD = tempBDD.and(fact.nithVar(getVarNum(i, y)));
    		}
    	}    	
    	
    	// Vertically
    	for (int i = 0; i < size; i++) {
    		if (i != y) {
    			tempBDD = tempBDD.and(fact.nithVar(getVarNum(x, i)));
    		}
    	}
    	
    	// Top-Left to Bottom-Right
    	for (int i = 0; i < size; i++) {
    		if (i != x) {
    			if ((y + i - x < size) && (y + i - x > 0)) {
					tempBDD = tempBDD.and(fact.nithVar(getVarNum(i, y + i - x)));
				}
    		}
    	}
    	
    	// Bottom-Left to Top-Right
    	for (int i = 0; i < size; i++) {
    		if (i != x) {
    			if ((y - i + x < size) && (y - i + x > 0)) {
					tempBDD = tempBDD.and(fact.nithVar(getVarNum(i, y - i + x)));
				}
    		}
    	}
    	
    	// Negate the current index
    	BDD sub = fact.nithVar(getVarNum(x, y));
    	
    	// Make sub a choice between index taken or not
    	sub = sub.or(tempBDD);
    	
    	// Implement sub into main
    	superBDD = superBDD.and(sub);
    }

   
    public int[][] getGameBoard() {
        return board;
    }
    
    public void NateDoggAndWarrenG() {
    	// AKA the regulators
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			if (getInvalid(i, j)) {
    				board[i][j] = -1;
    			}
    		}
    	}
    }
    
    public boolean getInvalid(int x, int y) {
    	return superBDD.restrict(fact.ithVar(getVarNum(x, y))).isZero();
    }
    
    public void placeRemainingQueens() {
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			if (board[i][j] == 0) {
    				if (superBDD.restrict(fact.nithVar(getVarNum(i,j))).isZero()) {
    					insertQueen(i, j);
    				}
    			}
    		}
    	}
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;
        
        // put some logic here..
        printBoard();
//        System.out.println();
        // Restrict with the newly and most recently inserted queen.
        superBDD = superBDD.restrict(fact.ithVar(getVarNum(column, row)));
        
        NateDoggAndWarrenG();
        
        placeRemainingQueens();
        
        return true;
    }
    
    private int getVarNum(int x, int y) {
    	return size * y + x;
    }
    
    public void printBoard() {
    	for (int i = 0; i < x; i++) {
    		for (int j = 0; j < y; j++) {
    			System.out.print(board[j][i] + " ");
    		}
    		System.out.println();
    	}
    }
}
