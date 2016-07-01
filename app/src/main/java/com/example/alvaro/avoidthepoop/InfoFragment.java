package com.example.alvaro.avoidthepoop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InfoFragment extends Fragment {
    ImageView exit,share,start;
    TextView ayuda;
    TextView enlace;
    TextView cancion;


    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Sketch 3D.otf");
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        enlace = (TextView) view.findViewById(R.id.textViewSobreMi);
        enlace.setMovementMethod(LinkMovementMethod.getInstance());
        enlace.setTypeface(font);
        cancion = (TextView) view.findViewById(R.id.textViewCancion);
        cancion.setTypeface(font);
        exit = (ImageView)view.findViewById(R.id.imageViewExitInfo);
        share = (ImageView)view.findViewById(R.id.imageViewShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String share = "https://play.google.com/store/apps/details?id=com.adgprogramador.eurodollarrates";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Buscaminas perruno");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(sharingIntent, "Compartir mediante:"));
            }
        });
        start = (ImageView)view.findViewById(R.id.imageViewStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.king.farmheroessupersaga"));
                startActivity(browserIntent);
            }
        });
        ayuda = (TextView) view.findViewById(R.id.textViewAyuda);
        ayuda.setTypeface(font);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter, R.animator.exit).remove(InfoFragment.this).commit();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
