package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {
    TextView count_text;
    TextView count_preview;
    TextView oper_text;

    CircularSeekBar seekbar;
    Button btn_plus;
    Button btn_minus;
    Button btn_multi;
    Button btn_divide;
    Button btn_equal;
    Button btn_delete;
    Button btn_reset;
    Stack<Character> stack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count_text = findViewById(R.id.count_text);
        count_preview = findViewById(R.id.count_preview);
        oper_text = findViewById(R.id.oper_text);
        seekbar = findViewById(R.id.progress_circular);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_multi = findViewById(R.id.btn_multi);
        btn_divide = findViewById(R.id.btn_divide);
        btn_equal = findViewById(R.id.btn_equal);
        // update Button
        btn_delete = findViewById(R.id.btn_delete);
        btn_reset = findViewById(R.id.btn_reset);

        setEvent();
    }
    private void setEvent(){
        seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button_plus", "Success");
                String previous_text = (String) count_text.getText();
                if(previous_text.equals("0")) return;
                previous_text += "+";
                count_text.setText(previous_text);
            }
        });
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button_plus", "Success");
                String previous_text = (String) count_text.getText();
                if(previous_text.equals("0")) return;
                previous_text += "-";
                count_text.setText(previous_text);
            }
        });
        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button_plus", "Success");
                String previous_text = (String) count_text.getText();
                if(previous_text.equals("0")) return;
                previous_text += "*";
                count_text.setText(previous_text);
            }
        });
        btn_divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button_plus", "Success");
                String previous_text = (String) count_text.getText();
                if(previous_text.equals("0")) return;
                previous_text += "/";
                count_text.setText(previous_text);
            }
        });
        btn_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button_plus", "Success");
                String previous_text = (String) count_text.getText();
                if(previous_text.equals("0")) return;
                String result = getCalculate(previous_text);
                previous_text += "=";
                oper_text.setText(previous_text);


                count_text.setText(result);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener(){

            // delete 버튼 - 한 자씩 제거
            @Override
            public void onClick(View v) {
                Log.d("Button_delete","Success");

                String stringTempCount = count_text.getText().toString();
                count_text.setText(stringTempCount.substring(0, stringTempCount.length()-1));

                String stringTempOper = oper_text.getText().toString();

                // Error -
                //oper_text.setText(stringTempOper.substring(0,stringTempOper.length()-1));
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener(){

            // reset button - 초기화
            @Override
            public void onClick(View v) {
                Log.d("Button_reset", "Success");
                count_text.setText(null);
                oper_text.setText(null);
            }
        });

    }

    public class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {

        @Override
        public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
            Log.d("SEEKBAR_EVENT", Float.toString(circularSeekBar.getProgress()));
            count_preview.setText(Integer.toString((int) circularSeekBar.getProgress()));

        }

        @Override
        public void onStopTrackingTouch(CircularSeekBar seekBar) {
            Log.d("SEEKBAR_EVENT", "STOP_TRACKING");
            String previous_text = (String) count_text.getText();
            if(previous_text.equals("0")) previous_text = "";
            previous_text += Integer.toString((int) seekBar.getProgress());
            count_text.setText(previous_text);

            seekBar.setProgress(0);
            count_preview.setText("0");
        }

        @Override
        public void onStartTrackingTouch(CircularSeekBar seekBar) {
            Log.d("SEEKBAR_EVENT", "START_TRACKING");
        }
    }

    private String getCalculate(String content) {

        char[] operationCode = {'+', '-', '*', '/', '(', ')'}; //연산 부호

        ArrayList<String> postfixList = new ArrayList<String>(); //후위표기법으로 변환 후 저장 할 ArrayList
        Stack<Character> opStack = new Stack<Character>(); // 연산 부호 우선순위처리 하며 후위 표기법으로 변경하는 Stack
        Stack<String> calculatorStack = new Stack<String>(); //후위 표기법을 계산하는 Stack

        int index = 0;//content.substring() 인수

        for (int i = 0; i < content.length(); i++) {
            for (int j = 0; j < operationCode.length; j++) {
                if (content.charAt(i) == operationCode[j]) { //문자열과 연산 부호 비교

                    //postfixList에 연산 부호가 나오기 전까지의 숫자를 담는다(공백제거)
                    postfixList.add(content.substring(index, i).trim().replace("(", "").replace(")", ""));
                    if (content.charAt(i) == '(') {
                        if (content.charAt(i) == ')') {//우 괄호가 나오면 좌 괄호가 나오거나 스택에 비어있을때 까지 pop하여 list에 저장
                            while (true) {
                                postfixList.add(opStack.pop().toString());
                                if (opStack.pop() == '(' || opStack.isEmpty()) {
                                    break;
                                }
                            }
                        }
                    }

                    if (opStack.isEmpty()) { //opStack이 비어 있을 경우
                        opStack.push(operationCode[j]); //연산 부호 저장
                    } else { //opStack이 비어 있지 않을 경우
                        if (opOrder(operationCode[j]) > opOrder(opStack.peek())) { //우선 순위 비교
                            opStack.push(operationCode[j]); //스택에 top 값 보다 높은 우선순위이면 그대로 저장
                        } else if (opOrder(operationCode[j]) <= opOrder(opStack.peek())) {//우선 순위 비교
                            postfixList.add(opStack.peek().toString());//스택에 있는 값이 우선순위가 같거나 작을 경우 list에 저장
                            opStack.pop();//스택 제거
                            opStack.push(operationCode[j]);//높은 우선순위 연산 부호 스택에 저장
                        }
                    }
                    index = i + 1;// 다음 순서 처리
                }
            }
        }
        postfixList.add(content.substring(index, content.length()).trim().replace("(", "").replace(")", "")); //마지막 숫자 처리

        if (!opStack.isEmpty()) { //Stack에 남아있는 연산 모두 postfixList에 추가
            for (int i = 0; i < opStack.size();) {
                postfixList.add(opStack.peek().toString());
                opStack.pop();
            }
        }

        //list에 공백, 괄호 제거
        for (int i = 0; i < postfixList.size(); i++) {
            if (postfixList.get(i).equals("")) {
                postfixList.remove(i);
                i = i - 1;
            } else if (postfixList.get(i).equals("(")) {
                postfixList.remove(i);
                i = i - 1;
            } else if (postfixList.get(i).equals(")")) {
                postfixList.remove(i);
                i = i - 1;
            }
        }

        System.out.println(postfixList);

        opStack.clear(); //Stack 비우기

        //postfixList를 calculatorStack에 저장하면서 후위연산 처리
        for (int i = 0; i < postfixList.size(); i++) {
            calculatorStack.push(postfixList.get(i));
            for (int j = 0; j < operationCode.length; j++) {
                if (postfixList.get(i).charAt(0) == operationCode[j]) { //연산 부호 비교
                    calculatorStack.pop(); //stack에 저장된 연산 부호 제거
                    double s2, s1; //stack에서 pop 되는 값들을 저장할 변수
                    String rs; // 연산 처리 후 문자열로 변환 후 stack에 저장할 변수

                    s2 = Double.parseDouble(calculatorStack.pop()); //스택에서 pop하여 문자열을 숫자로 형변환
                    if(calculatorStack.empty()){
                        s1 = 0;
                    }else{
                        s1 = Double.parseDouble(calculatorStack.pop());
                    }

                    //연산 부호에 해당하는 산술 처리 후 stack에 저장
                    switch (operationCode[j]) {
                        case '+':
                            rs = String.valueOf(s1 + s2);
                            calculatorStack.push(rs);
                            break;
                        case '-':
                            rs = String.valueOf(s1 - s2);
                            calculatorStack.push(rs);
                            break;
                        case '*':
                            rs = String.valueOf(s1 * s2);
                            calculatorStack.push(rs);
                            break;
                        case '/':
                            rs = String.valueOf(s1 / s2);
                            calculatorStack.push(rs);
                            break;
                    }
                }
            }
        }


        double re = Double.parseDouble(calculatorStack.peek()); //Stack Top 데이터
        String result = String.format("%.10f", re); //소수점 10째짜리

        //정수 부분 자리 구하기
        int num = 0;
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '.') {
                num = i;
                break;
            }
        }

        //정수부분
        String mok = result.substring(0, num);

        //나머지 연산
        double divde = Double.parseDouble(result) % Double.parseDouble(mok);

        //나머지가 0이면 소수점 자릿 수 안보이게
        if (divde == 0) {
            result = String.format("%.0f", re);
        }

        return result;
    }

    /**
     * 연산 부호 우선순위 정하는 메서드
     *
     * @param op - 연산 부호
     */
    public int opOrder(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}