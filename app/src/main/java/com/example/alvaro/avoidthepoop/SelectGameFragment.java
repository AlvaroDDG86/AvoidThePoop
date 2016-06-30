package com.example.alvaro.avoidthepoop;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelectGameFragment extends Fragment{
    Button buttonEasy,buttonMedium,buttonHard;
    ImageView imageViewExit;
    int level;
    View view2;
    boolean music;


    private OnFragmentInteractionListener mListener;

    public SelectGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_select_game, container, false);
        view2 = view;
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Sketch 3D.otf");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        music = sharedPref.getBoolean("music", true);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double width= size.x*0.70;


        buttonEasy = (Button)view.findViewById(R.id.buttonEasy);
        buttonEasy.setWidth((int)width);
        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 8;
                startGame(level);
            }
        });
        buttonEasy.setTypeface(font);

        buttonMedium = (Button)view.findViewById(R.id.buttonMedium);
        buttonMedium.setWidth((int)width);
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 10;
                startGame(level);

            }
        });
        buttonMedium.setTypeface(font);

        buttonHard = (Button)view.findViewById(R.id.buttonHard);
        buttonHard.setWidth((int)width);
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 14;
                startGame(level);
            }
        });
        buttonHard.setTypeface(font);

        imageViewExit = (ImageView)view.findViewById(R.id.imageViewExitF);
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit).remove(SelectGameFragment.this).commit();
            }
        });


        // Inflate the layout for this fragment
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void startGame(int level){
        MediaPlayer clickSound = MediaPlayer.create(getActivity(), R.raw.button);
        if(music){
            clickSound.start();
        }
        getFragmentManager().beginTransaction().remove(SelectGameFragment.this).commit();
        getActivity().finish();
        Intent intentGame= new Intent(view2.getContext(),Game.class);
        intentGame.putExtra("level",level);
        startActivity(intentGame);
    }


}
