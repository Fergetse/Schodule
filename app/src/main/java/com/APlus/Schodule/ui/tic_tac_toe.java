package com.APlus.Schodule.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import com.APlus.Schodule.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;

public class tic_tac_toe extends AppCompatActivity {
    TableLayout gameBoard;
    private int grid_size;
    char[][] my_board;
    char turn;
    TextView txt_turn;

    /* access modifiers changed from: protected */
    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        int parseInt = Integer.parseInt("3");
        this.grid_size = parseInt;

        int[] iArr = new int[2];
        iArr[1] = parseInt;
        iArr[0] = parseInt;

        this.my_board = (char[][]) Array.newInstance(char.class, iArr);
        this.gameBoard = findViewById(R.id.mainBoard);
        this.txt_turn = findViewById(R.id.turn);
        resetBoard();

        setTitle("Minijuego \"Gato\"");
        TextView textView = this.txt_turn;
        textView.setText("Turno de: \"" + this.turn + "\"");

        for (int i = 0; i < this.gameBoard.getChildCount(); i++) {
            TableRow row = (TableRow) this.gameBoard.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(" ");
                tv.setOnClickListener(Move(i, j, tv));
            }
        }
        ((FloatingActionButton) findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent current = getIntent();
                finish();
                startActivity(current);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void resetBoard() {
        this.turn = 'X';
        for (int i = 0; i < this.grid_size; i++) {
            for (int j = 0; j < this.grid_size; j++) {
                this.my_board[i][j] = ' ';
            }
        }
    }

    /* access modifiers changed from: protected */
    public int gameStatus() {
        for (int i = 0; i < this.grid_size; i++) {
            if (check_Row_Equality(i, 'X') || check_Column_Equality(i, 'X')) {
                return 1;
            }
            if (check_Row_Equality(i, 'O') || check_Column_Equality(i, 'O')) {
                return 2;
            }
            if (check_Diagonal('X')) {
                return 1;
            }
            if (check_Diagonal('O')) {
                return 2;
            }
        }
        boolean boardFull = true;
        for (int i2 = 0; i2 < this.grid_size; i2++) {
            for (int j = 0; j < this.grid_size; j++) {
                if (this.my_board[i2][j] == ' ') {
                    boardFull = false;
                    break;
                }
            }
        }
        if (boardFull) {
            return -1;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean check_Diagonal(char player) {
        int i;
        int count_Equal1 = 0;
        int count_Equal2 = 0;
        for (int i2 = 0; i2 < this.grid_size; i2++) {
            if (this.my_board[i2][i2] == player) {
                count_Equal1++;
            }
        }
        int i3 = 0;
        while (true) {
            i = this.grid_size;
            if (i3 >= i) {
                break;
            }
            if (this.my_board[i3][(i - 1) - i3] == player) {
                count_Equal2++;
            }
            i3++;
        }
        if (count_Equal1 == i || count_Equal2 == i) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean check_Row_Equality(int r, char player) {
        int i;
        int count_Equal = 0;
        int i2 = 0;
        while (true) {
            i = this.grid_size;
            if (i2 >= i) {
                break;
            }
            if (this.my_board[r][i2] == player) {
                count_Equal++;
            }
            i2++;
        }
        if (count_Equal == i) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean check_Column_Equality(int c, char player) {
        int i;
        int count_Equal = 0;
        int i2 = 0;
        while (true) {
            i = this.grid_size;
            if (i2 >= i) {
                break;
            }
            if (this.my_board[i2][c] == player) {
                count_Equal++;
            }
            i2++;
        }
        if (count_Equal == i) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean Cell_Set(int r, int c) {
        return this.my_board[r][c] != ' ';
    }

    /* access modifiers changed from: protected */
    public void stopMatch() {
        for (int i = 0; i < this.gameBoard.getChildCount(); i++) {
            TableRow row = (TableRow) this.gameBoard.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ((TextView) row.getChildAt(j)).setOnClickListener((View.OnClickListener) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public View.OnClickListener Move(final int r, final int c, final TextView tv) {
        return new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            public void onClick(View v) {
                if (!Cell_Set(r, c)) {
                    my_board[r][c] = turn;
                    if (turn == 'X') {
                        tv.setText("X");
                        tv.setTextColor(SupportMenu.CATEGORY_MASK);
                        turn = 'O';
                    } else if (turn == 'O') {
                        tv.setText("O");
                        tv.setTextColor(-16776961);
                        turn = 'X';
                    }
                    if (gameStatus() == 0) {
                        TextView textView = txt_turn;
                        textView.setText("Turno de: \"" + turn + "\"");
                    } else if (gameStatus() == -1) {
                        txt_turn.setText("¡Empate!");
                        stopMatch();
                    } else {
                        TextView textView2 = txt_turn;
                        textView2.setText("\"" + turn + "\" Perdió");
                        stopMatch();
                    }
                } else {
                    txt_turn.setText("Escogió una casilla ocupada");
                }
            }
        };
    }

}
