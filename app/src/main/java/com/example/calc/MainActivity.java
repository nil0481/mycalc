package com.example.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextView disp,textView;

    private String display="";
    private String operator="";
    private String result="";
    private int count=0;
    private Double a=4.6;
    private Double b=3.3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disp=(TextView) findViewById(R.id.disp);
        textView=(TextView) findViewById(R.id.textView);

    }

    private void screen(){
        disp.setText(display);
    }

    public void onClkno(View v) {
        // if (operator=="")count=0;
        if (result != "") {
            clear();
            screen();
        }
        if (!disp.getText().toString().contains("Error")) {
            Button b = (Button) v;
            display += b.getText();
            screen();
        }
    }
    public void onClkdot(View v){
        if (result!=""){
            clear();
            screen();
        }
        if (!disp.getText().toString().contains("Error")) {
            Button b = (Button) v;
            if (operator == "" && count == 0) {
                display += b.getText();
                screen();
                count++;
            }
            if (count == 0) {
                display += b.getText();
                screen();
                count++;
            }
        }
    }
    public boolean isop(char opp){
        switch (opp){
            case '+': return true;
            case '-': return true;
            case '*': return true;
            case '/': return true;
            case '%': return true;
            default: return false;
        }

    }

    public void onClkop(View v) {
        count = 0;
        if (display == "" || (disp.getText().charAt(0)=='.' && display.length()==1)) return;
        Button b = (Button) v;
        if (result != "") {
            display = result;
            //textView.setText(display);
            result = "";
        }
        if (!disp.getText().toString().contains("Error")) {
            if (!operator.equals("")) {
                if (operator != "%"){
                    if (isop(display.charAt(display.length() - 1))) {
                        display = display.substring(0, display.length() - 1);
                        disp.setText(display);
                    } else {
                        try {
                            getresult();
                            display = result;
                            result = "";
                            screen();
                        } catch (Exception e) {
                            display = "Error";
                            screen();
                        }
                    }
                }
//                else if(operator=="%"){
//                    try {
//                        getresult();
//                        display = result;
//                        result = "";
//                        screen();
//                    } catch (Exception e) {
//                        display = "Error";
//                        screen();
//                    }
//
//                }
            }
            if (!disp.getText().toString().contains("Error")) {
                display += b.getText();
                operator = b.getText().toString();
                screen();
            }
        }
    }
    public void clear(){
        display="";
        operator="";
        result="";
    }
    public void onClkclear(View v){
        count=0;
        clear();
        screen();

    }
    public void onclkdel(View v) {
        String fin = disp.getText().toString();
        if (fin != "" && !disp.getText().toString().contains("Error")) {
            StringBuilder builder = new StringBuilder(fin);
            builder.deleteCharAt(fin.length() - 1);
            display = builder.toString();
            screen();
            if (!display.contains(operator))operator="";
        }
    }
    private Double compute(String a, String b, String op){
        Double aa=Double.parseDouble(a);
        Double bb=Double.parseDouble(b);
        switch (op){

            case "+": return aa+(bb);
            case "-": return aa-(bb);
            case "*": return aa*(bb);
            case "%": return aa*bb/100.0;
            case "/": try{
                return aa/(bb);
            }catch (Exception e){
                Log.d("Calc",e.getMessage());
                disp.setText(e.getMessage());
            }
            default:return -1.0;
        }

    }
//    private BigDecimal compute(String a, String b, String op){
//        BigDecimal aa=BigDecimal.valueOf(Double.valueOf(a));
//        BigDecimal bb=BigDecimal.valueOf(Double.valueOf(b));
//        switch (op){
//
//            case "+": return aa.add(bb);
//            case "-": return aa.subtract(bb);
//            case "*": return aa.multiply(bb);
//            case "/": try{
//                    return aa.divide(bb);
//            }catch (Exception e){
//                Log.d("Calc",e.getMessage());
//                disp.setText(e.getMessage());
//            }
//            default:return BigDecimal.valueOf(-1.0);
//        }
//
//    }
    public boolean getresult(){
        String[] operation=display.split(Pattern.quote(operator));
        if (!operator.equals("%")) {
            if (operation.length < 2) return false;
            else if (operation.length>2) {
                result = String.valueOf(compute("-"+operation[1], operation[2], operator));
                return true;
            }
            else {
                result = String.valueOf(compute(operation[0], operation[1], operator));
                return true;
            }
        }
        else {
            try {
                    result = String.valueOf(compute(operation[0], operation[1], operator));
                    return true;
            }catch (Exception e){
                result = String.valueOf(compute(operation[0], "1", operator));
                return true;

            }
        }
    }
    public void onClkequal(View v) {
        count=0;
        try {
            if (display == "") return;
            else if (operator == "") {
                if (result == "") disp.setText(display + "\n" + "= " + display);
                else return;
            } else if (operator == "%") {
                getresult();
                display = result;
                screen();
            } else if (!getresult()) {
                return;
            } else {
                disp.setText(display + "\n" + "= " + String.valueOf(result));
                operator = "";
            }
        }catch (Exception e){
            return;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key",disp.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        disp.setText(savedInstanceState.getString("key"));
    }


}
