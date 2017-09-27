package com.example.saubhagyam.thetalklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

public class DailyGame extends Fragment {

    Button dailyGame_submitBtn;
    Boolean ans_right;
    RadioButton DailyGame_chk_A, DailyGame_chk_B, DailyGame_chk_C, DailyGame_chk_D;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }





    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_game, container, false);

        dailyGame_submitBtn = (Button) view.findViewById(R.id.dailyGame_submitBtn);


        DailyGame_chk_A= (RadioButton) view.findViewById(R.id.DailyGame_chk_A);
        DailyGame_chk_B= (RadioButton) view.findViewById(R.id.DailyGame_chk_B);
        DailyGame_chk_C= (RadioButton) view.findViewById(R.id.DailyGame_chk_C);
        DailyGame_chk_D= (RadioButton) view.findViewById(R.id.DailyGame_chk_D);




        dailyGame_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DailyGame_chk_B.isChecked())
                    ans_right = false;
                else ans_right = true;

                View view1 = inflater.inflate(R.layout.daily_game_result_layout, null);
                final PopupWindow popupWindow1 = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

                TextView daily_game_correctTXT = (TextView) view1.findViewById(R.id.daily_game_correctTXT);
                TextView daily_game_TXT = (TextView) view1.findViewById(R.id.daily_game_TXT);
                TextView daily_game_rightOption = (TextView) view1.findViewById(R.id.daily_game_rightOption);
                Button daily_game_okbtn = (Button) view1.findViewById(R.id.daily_game_okbtn);
                if (DailyGame_chk_A.isChecked() || DailyGame_chk_B.isChecked() || DailyGame_chk_C.isChecked() || DailyGame_chk_D.isChecked()) {



                    if (!ans_right) {

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                        popupWindow1.setFocusable(true);
                        popupWindow1.setOutsideTouchable(false);

                        daily_game_okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow1.dismiss();
                            }
                        });


                    } else {
                        daily_game_correctTXT.setText("Incorrect!");
                        daily_game_TXT.setText("Sorry, the answer was");
                        daily_game_rightOption.setText("B");


                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                        popupWindow1.setFocusable(true);
                        popupWindow1.setOutsideTouchable(false);

                        daily_game_okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow1.dismiss();
                            }
                        });

                    }
                }
                else
                {
                    daily_game_correctTXT.setText("");
                    daily_game_TXT.setText("Please Select any option...");
                    daily_game_rightOption.setText("");

                    popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupWindow1.setFocusable(true);
                    popupWindow1.setOutsideTouchable(false);

                    daily_game_okbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow1.dismiss();
                        }
                    });

                }
            }
        });


        return view;
    }


   /* public class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        int position;

        public MyCheckedChangeListener(int position) {
            this.position = position;
        }

        private void changeVisibility(CheckBox layout, boolean isChecked) {
            if (isChecked) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (position) {
                case 1:
                    DailyGame_chk_B.setChecked(false);
                    DailyGame_chk_C.setChecked(false);
                    DailyGame_chk_D.setChecked(false);
                    break;
                case 2:
//                    changeVisibility(DailyGame_chk_B, isChecked);
                    DailyGame_chk_A.setChecked(false);
                    DailyGame_chk_C.setChecked(false);
                    DailyGame_chk_D.setChecked(false);
                    break;
                case 3:
//                    changeVisibility(DailyGame_chk_C, isChecked);
                    DailyGame_chk_B.setChecked(false);
                    DailyGame_chk_A.setChecked(false);
                    DailyGame_chk_D.setChecked(false);
                    break;
                case 4:
//                    changeVisibility(DailyGame_chk_D, isChecked);
                    DailyGame_chk_B.setChecked(false);
                    DailyGame_chk_C.setChecked(false);
                    DailyGame_chk_A.setChecked(false);
                    break;
            }
        }
    }*/

}
