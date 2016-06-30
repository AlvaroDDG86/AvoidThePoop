package com.example.alvaro.avoidthepoop;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FinishFragment extends Fragment {
    //Variables para la clase
    ImageView imageViewNewGame;
    ImageView imageViewExit;
    String condition;
    TextView textViewState;
    int time;

    private OnFragmentInteractionListener mListener;

    public FinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Sketch 3D.otf");

        textViewState=(TextView)view.findViewById(R.id.textViewState);
        textViewState.setTypeface(font);

        condition = getArguments().getString("condition");
        time = getArguments().getInt("time");

        if(condition.equalsIgnoreCase("win")){
            textViewState.setText("YOU WIN!!!");
        }else if(condition.equalsIgnoreCase("error")){
            textViewState.setText("YOU`RE WRONG");
        }else{
            textViewState.setText("YOU LOST!!!");
        }

        imageViewNewGame=(ImageView)view.findViewById(R.id.imageViewNewGame);
        imageViewNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        imageViewExit=(ImageView)view.findViewById(R.id.imageViewExit);
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(FinishFragment.this).commit();
                Game.player.stop();
                getActivity().finish();
                Intent intent=new Intent(getActivity().getApplicationContext(), Main.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
