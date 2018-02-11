package com.example.android.dartsscore;

import android.content.Context;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    int scorePlayerA = 501;
    int scorePlayerB = 501;
    int darts = 3;
    boolean playerATurn = true;
    boolean playerBTurn = false;
    int playerAWins = 0;
    int playerBWins = 0;
    Chronometer counter;

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("scorePlayerA", scorePlayerA);
//        outState.putInt("scorePlayerB", scorePlayerB);
//        outState.putInt("darts", darts);
//        outState.putBoolean("playerATurn", playerATurn);
//        outState.putBoolean("playerATurn", playerATurn);
//        outState.putInt("playerAWins", playerAWins);
//        outState.putInt("playerBWins", playerBWins);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        scorePlayerA = savedInstanceState.getInt("scorePlayerA");
//        scorePlayerB = savedInstanceState.getInt("scorePlayerB");
//        darts = savedInstanceState.getInt("darts");
//        playerAWins = savedInstanceState.getInt("playerAWins");
//        playerBWins = savedInstanceState.getInt("playerBWins");
//        playerATurn = savedInstanceState.getBoolean("playerATurn");
//        playerBTurn = savedInstanceState.getBoolean("playerBTurn");
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayScorePlayerA(scorePlayerA);
        displayScorePlayerB(scorePlayerB);
        displayPlayerWinsA();
        displayPlayerWinsB();
        displayPlayerTurn();
        displayPlayerDarts();
        timer();
    }


    /**
     * Displays the current score for Team A.
     */
    public void displayScorePlayerA(int score) {
        TextView scoreView = findViewById(R.id.player_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the current score for Player B.
     */
    public void displayScorePlayerB(int score) {
        TextView scoreView = findViewById(R.id.player_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Tells the user how many previous games player A has won
     */
    private void displayPlayerWinsA() {
        TextView wins = findViewById(R.id.player_a_won);
        wins.setText(String.format(getString(R.string.games_won1), playerAWins));
    }

    /**
     * Tells the user how many previous games player B has won
     */
    private void displayPlayerWinsB() {
        TextView wins = findViewById(R.id.player_b_won);
        wins.setText(String.format(getString(R.string.games_won1), playerBWins));
    }


    /**
     * Tells the user who's turn it is
     */
    private void displayPlayerTurn() {
        TextView playerTextView = findViewById(R.id.player_turn);
        String player = "";
        if (playerATurn) {
            player = getString(R.string.player_a);
        }

        if (playerBTurn) {
            player = getString(R.string.player_b);
        }

        playerTextView.setText(getString(R.string.player_turn) + player);
    }


    /**
     * Tells the user how many darts the current player has remaining
     */
    private void displayPlayerDarts() {
        TextView playerDarts = findViewById(R.id.darts_remaining);
        playerDarts.setText(getString(R.string.darts_remaining) + darts);
    }

    /**
     * This method checks the number entered is a valid score on the dartboard and returns its value
     * deducts 1 from players darts
     */
    public int getplayerAthrow() {
        int thrown = 0;
        EditText numberHit = findViewById(R.id.number_hit);

        //if (playerADarts > 0){
        if (numberHit.getText().toString().equals(null) || numberHit.getText().toString().equals("")) {
            Toast.makeText(this, R.string.field_empty, Toast.LENGTH_LONG).show();
            return 0;
        } else {
            thrown = Integer.parseInt(numberHit.getText().toString());
            numberHit.setText("");
            if (thrown < 1) {
                Toast.makeText(this, R.string.too_low, Toast.LENGTH_SHORT).show();
                return 0;
            } else if (thrown > 20) {
                Toast.makeText(this, R.string.too_high, Toast.LENGTH_SHORT).show();
                return 0;
            }

        }
        //Log.v("MainActivity.java", "player threw " + thrownA);
        darts -= 1;
        //Log.v("MainActivity.java", "darts remaining " + darts);
        return thrown;
    }


    /**
     * This method resets the number field on turns where number entry isn't necessary and deducts a players throw.
     */
    public void otherThrow() {
        EditText numberHit = findViewById(R.id.number_hit);
        numberHit.setText("");
        darts -= 1;
        displayPlayerDarts();
    }

    /**
     * This method updates and displays the score for current player after hitting a single number
     */
    public void hitSingle(View view) {
        int thrown = getplayerAthrow();

        if (playerATurn) {
            if (scorePlayerA >= (thrown + 2)) {
                scorePlayerA = scorePlayerA - thrown;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerA(scorePlayerA);
        }

        if (playerBTurn) {
            if (scorePlayerB >= (thrown + 2)) {
                scorePlayerB = scorePlayerB - thrown;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerB(scorePlayerB);
        }
        displayPlayerDarts();
        if (darts == 0) {
            playerSwitch();
        }

    }

    /**
     * This method updates and displays the score for current player after hitting a double
     */
    public void hitDouble(View view) {
        int thrown = getplayerAthrow();
        if (playerATurn) {
            if (scorePlayerA == (thrown * 2)) {
                Toast.makeText(this, R.string.winnerA, Toast.LENGTH_LONG).show();
                scorePlayerA = scorePlayerA - (thrown * 2);
                playerAWins += +1;
                displayPlayerWinsA();
                resetG();

            } else if (scorePlayerA >= ((thrown * 2) + 2)) {
                scorePlayerA = scorePlayerA - (thrown * 2);
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerA(scorePlayerA);
        }

        if (playerBTurn) {
            if (scorePlayerB == (thrown * 2)) {
                Toast.makeText(this, R.string.winnerB, Toast.LENGTH_LONG).show();
                scorePlayerB = scorePlayerB - (thrown * 2);
                playerBWins += +1;
                displayPlayerWinsB();
                resetG();

            } else if (scorePlayerB >= ((thrown * 2) + 2)) {
                scorePlayerB = scorePlayerB - (thrown * 2);
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerB(scorePlayerB);
        }
        displayPlayerDarts();
        if (darts == 0) {
            playerSwitch();
        }

    }

    /**
     * This method updates and displays the score for current player after hitting a treble
     */
    public void hitTreble(View view) {
        int thrown = getplayerAthrow();
        if (playerATurn) {
            if (scorePlayerA >= ((thrown * 3) + 2)) {
                scorePlayerA = scorePlayerA - (thrown * 3);
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerA(scorePlayerA);
        }

        if (playerBTurn) {
            if (scorePlayerB >= ((thrown * 3) + 2)) {
                scorePlayerB = scorePlayerB - (thrown * 3);
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerB(scorePlayerB);
        }
        displayPlayerDarts();
        if (darts == 0) {
            playerSwitch();
        }

    }

    /**
     * This method updates and displays the score current player after hitting the 'bull'
     */
    public void bull(View view) {
        otherThrow();
        if (playerATurn) {
            if (scorePlayerA >= 52) {
                scorePlayerA = scorePlayerA - 50;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerA(scorePlayerA);
        }
        if (playerBTurn) {
            if (scorePlayerB >= 52) {
                scorePlayerB = scorePlayerB - 50;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerB(scorePlayerB);
        }
        if (darts == 0) {
            playerSwitch();
        }
    }

    /**
     * This method updates and displays the score for current player after hitting the 'outer bull'
     */
    public void outerBull(View view) {
        otherThrow();
        if (playerATurn) {
            if (scorePlayerA >= 27) {
                scorePlayerA = scorePlayerA - 25;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerA(scorePlayerA);
        }

        if (playerBTurn) {
            if (scorePlayerB >= 27) {
                scorePlayerB = scorePlayerB - 25;
            } else {
                darts = 0;
                Toast.makeText(this, R.string.bust, Toast.LENGTH_SHORT).show();
            }
            displayScorePlayerB(scorePlayerB);
        }
        if (darts == 0) {
            playerSwitch();
        }

    }

    /**
     * This method updates the game and darts thrown when a player misses without updating score
     */
    public void miss(View view) {
        otherThrow();
        Toast.makeText(this, R.string.miss, Toast.LENGTH_LONG).show();
        if (darts == 0) {
            playerSwitch();
        }

    }

    /**
     * This method switches player turns and resets darts after 3 darts used or when a player is 'bust'.
     */
    public void playerSwitch() {
        if (playerATurn) {
            playerATurn = false;
            playerBTurn = true;
        } else if (playerBTurn) {
            playerBTurn = false;
            playerATurn = true;
        }

        darts = 3;
        displayPlayerTurn();
        displayPlayerDarts();
    }


    /**
     * current game and timer is reset on button press
     */
    public void resetGame(View view) {
        resetG();
    }

    /**
     * button will reset all including previous wins
     */
    public void resetAll(View view) {
        resetG();
        playerAWins = 0;
        playerBWins = 0;
        displayPlayerWinsA();
        displayPlayerWinsB();
    }

    /**
     * This method resets the current game
     */
    public void resetG() {
        scorePlayerA = 501;
        scorePlayerB = 501;
        darts = 3;
        playerATurn = true;
        playerBTurn = false;
        displayScorePlayerA(scorePlayerA);
        displayScorePlayerB(scorePlayerB);
        displayPlayerTurn();
        displayPlayerDarts();
        timer();
    }

    /**
     * displays running time of the current game
     */
    public void timer() {
        Chronometer counter = findViewById(R.id.timer);
        counter.setBase(SystemClock.elapsedRealtime());
        counter.start();
        counter.setFormat("Current game time:  %s");

    }


}
