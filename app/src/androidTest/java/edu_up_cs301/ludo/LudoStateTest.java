package edu_up_cs301.ludo;

import org.junit.Test;

import static org.junit.Assert.*;

public class LudoStateTest {



    @Test
    public void newRollTest() {
        LudoState state = new LudoState();
        state.newRoll();
        if(state.getDiceVal() <=6 && state.getDiceVal()>0){
            assertEquals(1,1);
        }
        else{    assertEquals(-1,1);
        }
    }

    @Test
    public void incPlayerScoreTest() {
        LudoState state = new LudoState();
        state.incPlayerScore(0);
        assertEquals(state.getPlayerScore(0),1);
        state.incPlayerScore(0);
        state.incPlayerScore(0);
        state.incPlayerScore(0);
        assertEquals(state.getPlayerScore(0),4);
    }



    @Test
    public void changePlayerTurnTest(){
        LudoState state = new LudoState();
        assertEquals(state.getWhoseMove(),0);
        state.changePlayerTurn();
        assertEquals(state.getWhoseMove(),1);
        state.changePlayerTurn();
        state.changePlayerTurn();
        state.changePlayerTurn();
        //It should wrap around back to player 0
        assertEquals(state.getWhoseMove(),0);

    }

    @Test
    public void advanceTokenTest() {
        LudoState state = new LudoState();
        state.pieces[0].setIsMovable(true);
        state.newRoll();
        state.advanceToken(0,0);
        assertEquals(state.pieces[0].getNumSpacesMoved()+state.getDiceVal(),state.getDiceVal());

    }


    @Test
    public void getOrder() {
    }
}