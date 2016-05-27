package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.max.jumpingapp.R;
import com.max.jumpingapp.util.PrefsHandler;
import com.max.jumpingapp.views.MainActivity;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
// * {@link OnGemFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GemFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private Button gemButton;

    private OnGemFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GemFragment newInstance(String param1, String param2) {
        GemFragment fragment = new GemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public GemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.gem_fragment, container, false);

        //Find the +1 button
        gemButton = (Button) view.findViewById(R.id.gemButton);
        updateGemText();
        return view;
    }

    public void updateGemText() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.GANME_PREFS, 0);
        int gems = PrefsHandler.getGems(sharedPreferences);
        gemButton.setText(String.valueOf(gems));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
//        gemButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        System.out.println("onButtonPressed Uri: "+view.toString());
        if (mListener != null) {
            mListener.onGemFragmentInteraction(view);
        }
    }

    public void gemButtonClicked(View view){
        System.out.println("gem button pressed");
        popup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGemFragmentInteractionListener) {
            mListener = (OnGemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGemFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) mListener);
        final SharedPreferences  sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.GANME_PREFS, 0);
        builder.setPositiveButton(R.string.addGem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefsHandler.addGem(sharedPreferences);
                updateGemText();
            }
        });
        builder.setNegativeButton(R.string.resetGems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefsHandler.resetGems(sharedPreferences);
                updateGemText();
            }
        });
        builder.create().show();
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
    public interface OnGemFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGemFragmentInteraction(View view);
    }

}
