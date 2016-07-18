package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.max.jumpingapp.R;
import com.maxrick.bounceit.util.PrefsHandler;
import com.maxrick.bounceit.views.RecommendScreen;

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
    private final GemFragment that;

    private Button gemButton;

    private OnGemFragmentInteractionListener mListener;

    public static GemFragment newInstance(String param1, String param2) {
        GemFragment fragment = new GemFragment();
        return fragment;
    }
    public GemFragment() {
        // Required empty public constructor
        that = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
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
        if (mListener != null) {
            mListener.onGemFragmentInteraction(view);
        }
    }

    public void gemButtonClicked(View view){
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
        final SharedPreferences  sharedPreferences = this.getActivity().getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
//        builder.setPositiveButton(R.string.addGem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PrefsHandler.addGem(sharedPreferences);
//                updateGemText();
//            }
//        });
//        builder.setNegativeButton(R.string.resetGems, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PrefsHandler.resetGems(sharedPreferences);
//                updateGemText();
//            }
//        });
        builder.setNeutralButton(getString(R.string.get_free_coins), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(that.getContext(), RecommendScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
//        builder.setPositiveButton(getString(R.string.spend_coins), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(that.getContext(), Shop.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
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
