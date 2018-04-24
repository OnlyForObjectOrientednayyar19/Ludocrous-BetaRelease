package edu_up_cs301.ludo;

import android.util.Log;

import edu_up_cs301.game.GameComputerPlayer;
import edu_up_cs301.game.infoMsg.GameInfo;

/**
 * ComputerSmartPlayer
    This implements the smart computer player.
    @author Ravi Nayyar
    @author Avery Guillermo
    @author Luke Danowski
    @author Chris Sebrechts
 */

public class ComputerSmartPlayer extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ComputerSmartPlayer(String name) {
        super(name);
    }

    int indexFirstPiece, outOfBaseIndex, indexFirstPieceInStart, getIndexFirstPieceOutOfStart;
    int furthestPieceTravelled;
    @Override
    protected void receiveInfo(GameInfo info) {
        Log.i("smart computer recieve", " " + playerNum);
        // if it's not a LudoState message, ignore it; otherwise
        // cast it
        if (!(info instanceof LudoState)) return;
        LudoState myState = (LudoState) info;
        // if it's not our move, ignore it
        if (myState.getWhoseMove() != this.playerNum) return;
        // sleep for 0.3 seconds to slow down the game
        sleep(700);
        //if it is the computer's turn to roll
        if (myState.getWhoseMove() == this.playerNum) {
            if (myState.getIsRollable()) {
                Log.i("Computer Player: " + this.playerNum, "Rolling the dice");
                game.sendAction(new ActionRollDice(this));
                return;
            }
            indexFirstPiece = myState.getIndexOfFirstPlayerPiece(this.playerNum);
            indexFirstPieceInStart = myState.getTokenIndexOfFirstPieceInStart(this.playerNum);
            furthestPieceTravelled = myState.getPieceFurthestTravelled(this.playerNum);
            outOfBaseIndex = 0;
            for (int i = indexFirstPiece; i < (indexFirstPiece + 4); i++) {
                if (!(myState.pieces[i].getIsHome())) {
                    outOfBaseIndex++;
                }
            }
            Log.i("The_Number of Pieces Out of Base is: ", "" + outOfBaseIndex);
            getIndexFirstPieceOutOfStart = myState.getTokenIndexOfFirstPieceOutOfStart(this.playerNum);
            //IF there are 0 pieces out of the base
            if (outOfBaseIndex == 0) {
                if (myState.getDiceVal() == 6) {
                    int[] order = myState.getOrder(this.playerNum);
                    Log.i("Order 3",""+order[3]);
                    game.sendAction(new ActionRemoveFromBase(this, indexFirstPieceInStart));
                    return;

                }
            }

            //if there are between 1 and 3 pieces out of base
            if (outOfBaseIndex > 0 && outOfBaseIndex < 4) {
                if (myState.getDiceVal() == 6) {
                    Log.i("First Piece In Start",""+indexFirstPieceInStart);
                    game.sendAction(new ActionRemoveFromBase(this, indexFirstPieceInStart));
                    return;
                }
                //if(myState.getDiceVal()!=6)
                else {
                    game.sendAction(new ActionMoveToken(this, determineWhichPieceToMove(myState)));
                    return;
                }
            }

            //If there are four pieces out of base
            if (outOfBaseIndex == 4) {
                game.sendAction(new ActionMoveToken(this, determineWhichPieceToMove(myState)));
                return;
            }
        }
    }
    public int determineWhichPieceToMove(LudoState myState){
        int pieceIndex, pieceScoreIndex;
        int[] pieceScoreArray = new int[]{0, 0, 0, 0};
        int[] safeSpaceArray = new int[]{0, 8, 13, 21, 26, 34, 39, 47};
        int[] order = myState.getOrder(this.playerNum);
        boolean[] isOnSafeSpace = new boolean[]{false, false, false, false};
        boolean[] willBeOnSafeSpace = new boolean[]{false, false, false, false};
        boolean boardHalfTravelled = false;
        int quarter = 13, half = 26, threefourths = 39;
        int diceVal = myState.getDiceVal();

        //Cycles throught all the pieces that the player owns and increments its score arrays accordingly
        for (pieceIndex = indexFirstPiece, pieceScoreIndex = 0; pieceIndex < (pieceIndex + 4) &&
                pieceScoreIndex<4 ; pieceIndex++, pieceScoreIndex++) {
            Log.i("Index of first Piece",""+indexFirstPiece);
            //if the piece is movable
            if (myState.pieces[pieceIndex].getIsMovable()) {
                Log.i("piece Index", "" + pieceIndex + "\n");
                int range = myState.pieces[pieceIndex].getNumSpacesMoved() + diceVal;
                //is the piece currently on a safe tile?
                for (int j = 0; j < 7; j++) {
                    if (myState.pieces[pieceIndex].getNumSpacesMoved() == safeSpaceArray[j]) {
                        isOnSafeSpace[pieceScoreIndex] = true;
                        if (j < 3) {
                            pieceScoreArray[pieceScoreIndex] = 1;
                            if (order[1] == pieceIndex) {
                                pieceScoreArray[pieceScoreIndex] = 2;
                            }
                        } else if (j < 6) {
                            pieceScoreArray[pieceScoreIndex] = 0;
                            if (order[1] == pieceIndex) {
                                pieceScoreArray[pieceScoreIndex] = 1;
                            }
                        } else if (j <= 7) {
                            pieceScoreArray[pieceScoreIndex] = 0;
                        }
                    }
                }
                //If range == home strech and Is not on a safe space
                if (range > 51 && !(myState.pieces[pieceIndex].getNumSpacesMoved() < 51) &&
                    !isOnSafeSpace[pieceScoreIndex]) {
                    Log.i("Piece Range equals home-strech", "!");
                    pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 10;
                    return pieceIndex;
                }
                //If Safe Spaces are in reach
                for (int j = 0; j < 7; j++) {
                    if (range == safeSpaceArray[j]) {
                        Log.i("Safe Space", j + " is in range");
                        willBeOnSafeSpace[pieceScoreIndex] = true;
                        if (j < 3) {
                            pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 4;
                        } else if (j < 6) {
                            pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 6;
                        } else if (j <= 7) {
                            pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 8;
                        }
                    }
                }
                //if the piece is not on a safe space and has moved more than three fourths of the board
                if (myState.pieces[pieceIndex].getNumSpacesMoved() > threefourths && !isOnSafeSpace[pieceScoreIndex]) {
                    pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 5;
                }
                //If the HomeTile is in Reach
                if (range == 57) {
                    Log.i("Home Tile Is In Reach", "!");
                    pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 3;
                }
                //If Piece is in home-strech
                if (myState.pieces[pieceIndex].getNumSpacesMoved() > 51 && range != 57) {
                    pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 2;
                }
                //Ensuring that even the least ahead pieces can move
                //If farthest piece has moved more than a quarter and it is not on a safe space
                if (myState.pieces[order[3]].getNumSpacesMoved() >= quarter &&
                    isOnSafeSpace[pieceScoreIndex] == false) {
                     //if second farthest is not in home
                    if (myState.pieces[order[2]].getIsHome() == false && pieceIndex == order[2]) {
                        Log.i("The Least Fathest Piece has moved more than half", "It is not on a space");
                        Log.i("\tThe Score for "+order[2]+"has been incremented by", "3");
                        pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 3;
                    }
                    //if piece is on starting square
                    if (myState.pieces[pieceIndex].getNumSpacesMoved() == 0 &&
                            myState.pieces[pieceIndex].getIsHome() == false) {
                        Log.i("The Piece is on a starting square","0 move");
                        Log.i("\t The Score for "+pieceScoreIndex+"has been incremented by", "1");
                        pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 1;
                    }
                }
                //if the second farthest piece has moved more than half and it is not at a safe space
                if (myState.pieces[order[2]].getNumSpacesMoved() >= half &&
                    isOnSafeSpace[pieceScoreIndex] == false) {
                     if (myState.pieces[order[1]].getIsHome() == false && pieceIndex == order[1]) {
                         Log.i("Second Fathest Piece has moved more than half", "It is not on a space");
                         Log.i("\t The Score for "+order[1]+"has been incremented by", "4");
                         pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 4;
                    }
                }
                //If third farthest piece has moved more than half and it is not on the home tile
                if (myState.pieces[order[1]].getNumSpacesMoved() >= half &&
                        isOnSafeSpace[pieceScoreIndex] == false) {
                     if (!myState.pieces[order[0]].getIsHome() && pieceIndex == order[1]) {
                         Log.i("Fathest Piece has moved more than half", "It is not on a safe space");
                         Log.i("\t The Score for "+order[1]+"has been incremented by", "4");
                        pieceScoreArray[pieceScoreIndex] = pieceScoreArray[pieceScoreIndex] + 5;
                    }
                }
            } //If the piece is movable
                //checking to see if entire pieceScoreArray is null.
                int p0s = pieceScoreArray[0];
                int p1s = pieceScoreArray[1];
                int p2s = pieceScoreArray[2];
                int p3s = pieceScoreArray[3];
                int totalPieceScore = 0;
                     for (int i = 0; i < 4; i++) {
                         if (pieceScoreArray[i] > 0) {
                             totalPieceScore++;
                         }
                     }
                     //Seeing if no piece has an associated score
                     for(int i=0;i<4;i++){
                         if(totalPieceScore ==0 || p0s==p1s && p1s==p2s && p2s==p3s){
                             Log.i("Total Piece Score is 0","or all the pieces have the same score");
                             if(!isOnSafeSpace[order[i]-indexFirstPiece]){
                                 pieceScoreArray[order[i]-indexFirstPiece] =
                                         pieceScoreArray[order[i]-indexFirstPiece]+1;
                                 break;
                             }
                             else{
                                 pieceScoreArray[order[i]-indexFirstPiece] =
                                         pieceScoreArray[order[i]-indexFirstPiece]+1;
                             }
                        }
                     }
        }//Large For Loop

        //Finding the largest score and returing the index of the piece associated with that score.
        int greatestScore = 0;
        int pieceReturn =0;
        int k;
        for(k=0;k<4;k++){
            if(greatestScore <= pieceScoreArray[k] && myState.pieces[indexFirstPiece+k].getIsMovable()){
                greatestScore = pieceScoreArray[k];
                pieceReturn = k;
            }
        }
        if(myState.getPieceFurthestTravelled(this.playerNum) == order[3]){
         }
         return indexFirstPiece+pieceReturn;
    }//determineWhichPieceToMove

}//Smart Computer Plauer