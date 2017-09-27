package com.example.saubhagyam.thetalklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MyDeatils.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MyDeatils#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyDeatils extends Fragment {
    PopupWindow popupWindow;

    Button save;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public MyDeatils() {
//        // Required empty public constructor
//    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MyDeatils.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MyDeatils newInstance(String param1, String param2) {
//        MyDeatils fragment = new MyDeatils();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the studentlayout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_deatils, container, false);
        final View popupView=inflater.inflate(R.layout.popupscreen,null);
        popupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap)null));

//        Context context = popupWindow.getContentView().getContext();
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
//        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        p.dimAmount = 0.3f;
//        wm.updateViewLayout(container, p);
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
        TextView t1,t2,t3,t4,t5, t6,t7,t8,t9;
        t1=(TextView)view.findViewById(R.id.editPerson);
        t2=(TextView)view.findViewById(R.id.datePerson);
        t3=(TextView)view.findViewById(R.id.statusPerson);
        t4=(TextView)view.findViewById(R.id.intraPerson);
        t5=(TextView)view.findViewById(R.id.localPerson);
        t6=(TextView)view.findViewById(R.id.lcaPerson);
        t7=(TextView)view.findViewById(R.id.emailPersona);
        t8=(TextView)view.findViewById(R.id.phonePersona);
        t9=(TextView)view.findViewById(R.id.passwordPersona);
        t1.setTypeface(typeface);
        t2.setTypeface(typeface);
        t3.setTypeface(typeface);
        t4.setTypeface(typeface);
        t5.setTypeface(typeface);
        t6.setTypeface(typeface);
        t7.setTypeface(typeface);
        t8.setTypeface(typeface);
        t9.setTypeface(typeface);
        save= (Button) view.findViewById(R.id.saveButton);
        save.setTypeface(typeface);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//                builder.setMessage("Popup Window");
//                AlertDialog alert=builder.create();
//                alert.setTitle("");
//                alert.show();
//                Dialog dialog=new Dialog(getContext());
//                dialog.setContentView(R.studentlayout.popupscreen);
//                dialog.show();


                popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);

                if(Build.VERSION.SDK_INT>=21){
                        popupWindow.setElevation(5.0f);
                    }
                ImageView i1=(ImageView)popupView.findViewById(R.id.student);
                    ImageView i2=(ImageView)popupView.findViewById(R.id.tutor);
                    TextView t1,t2,t3,t4,t5,t6,t7;
                    Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
                    t1=(TextView)popupView.findViewById(R.id.customPopUpText1);
                    t1.setTypeface(typeface);
                    t2=(TextView)popupView.findViewById(R.id.customPopUpText2);
                    t2.setTypeface(typeface);
                    t3=(TextView)popupView.findViewById(R.id.customPopUpText3);
                    t3.setTypeface(typeface);
                    t4=(TextView)popupView.findViewById(R.id.customPopUpText4);
                    t4.setTypeface(typeface);
                    t5=(TextView)popupView.findViewById(R.id.textstudent);
                    t5.setTypeface(typeface);
                    t6=(TextView)popupView.findViewById(R.id.texttutor);
                    t6.setTypeface(typeface);
                i1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getContext(),IntroScreenSwipe.class);
                            startActivity(intent);


                        }
                    });
                    i2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getContext(),IntroScreenSwipe.class);
                            startActivity(intent);

                        }
                    });

//


            }
        });
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
