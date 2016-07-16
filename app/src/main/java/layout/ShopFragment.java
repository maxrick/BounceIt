package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.max.jumpingapp.R;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.util.PrefsHandler;
import com.max.jumpingapp.views.Buyable;
import com.max.jumpingapp.views.RecommendScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 7/10/2016.
 */
public class ShopFragment extends Fragment {

    private SliderLayout slideShow;
    public TextSliderView[] textSliders;
    public static final int PLAYERIMAGE_EGGMAN  = R.drawable.eggman;
    public static final int PLAYERIMAGE_DEFAULT = R.drawable.playerimage;
    public static final int PLAXERIMAGE_STICK = R.drawable.playerimage_stickfigure;
    public static final int PLAYERIMAGE_HAT_AND_SHOES = R.drawable.playerimage_hat_and_shoes;
    public static final int PLAYERIMAGE_EGGMAN_LOCKED  = R.drawable.eggman_locked;
    public static final int PLAXERIMAGE_STICK_LOCKED = R.drawable.playerimage_stickfigure_locked;
    public static final int PLAYERIMAGE_HAT_AND_SHOES_LOCKED = R.drawable.playerimage_hat_and_shoes_locked;
    public Buyable[] buyables = {
            new Buyable(PLAYERIMAGE_DEFAULT, PLAYERIMAGE_DEFAULT,  Constants.PLAYERNAME_CLASSIC, 0),
            new Buyable(PLAYERIMAGE_HAT_AND_SHOES, PLAYERIMAGE_HAT_AND_SHOES_LOCKED, Constants.PLAYERNAME_SHOES_AND_HAT, 1),
            new Buyable(PLAXERIMAGE_STICK, PLAXERIMAGE_STICK_LOCKED, Constants.PLAYERNAME_STICK_FIGURE, 1),
            new Buyable(PLAYERIMAGE_EGGMAN, PLAYERIMAGE_EGGMAN_LOCKED, Constants.PLAYERNAME_EGGMAN, 2)};
    private Map<TextSliderView, Buyable> slideToImgMap = new HashMap<>();


    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }
    public ShopFragment() {
        // Required empty public constructor
    }

    public static float leftOfImage(int playerImgage) {
        if(playerImgage == PLAYERIMAGE_EGGMAN){
            return 0.05F;
        }
        //default
        return 0.3F;
    }

    public static float rightOfImage(int playerImgage) {
        if(playerImgage == PLAYERIMAGE_EGGMAN){
            return 0.05F;
        }
        //default
        return 0.2F;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shop_fragment, container, false);
        int playerImgage = PrefsHandler.getPlayerImage(getContext().getSharedPreferences(PrefsHandler.GANME_PREFS, ShopFragment.PLAYERIMAGE_DEFAULT));
        int currentSlide = 0;
        slideShow = (SliderLayout) view.findViewById(R.id.slider);
        slideShow.stopAutoCycle();
//@// TODO: 5/28/2016 open closed principle
        textSliders = new TextSliderView[buyables.length];
        for (int i = 0; i < buyables.length; i++) {
            final Buyable buyable = buyables[i];
            if (buyable.getImage() == playerImgage){
                currentSlide=i;
            }
            textSliders[i] = new TextSliderView(this.getContext());
            if(PrefsHandler.playerImageBought(getContext().getSharedPreferences(PrefsHandler.GANME_PREFS, 0), buyable.getImage())){
                textSliders[i].image(buyable.getImage());
            }else {
                textSliders[i].image(buyable.getLockedImageImage());
                textSliders[i].setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        buyPlayerImagePopup(buyable);
                    }
                });
            }
            slideToImgMap.put(textSliders[i], buyable);
            slideShow.addSlider(textSliders[i]);
        }
        slideShow.setCurrentPosition(currentSlide);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean currentPlayerCanBeSet(){
        BaseSliderView currentSlider = slideShow.getCurrentSlider();
        Buyable buyable= slideToImgMap.get(currentSlider);
        if (alreadyOwns(buyable.getImage()) || isDefaultImage(buyable.getImage())) {
            setPlayerImage(buyable.getImage());
            return true;
        }else {
            buyPlayerImagePopup(buyable);
            return false;
        }
    }

    private void buyPlayerImagePopup(final Buyable buyable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (buyImage(buyable)) {
                    setPlayerImage(buyable.getImage());
                    GemFragment gemFragment = (GemFragment) getFragmentManager().findFragmentByTag("gemFragment");
                    gemFragment.updateGemText();
                    getActivity().recreate();//@// TODO: 5/29/2016 bugfix in github
                    Snackbar.make(getActivity().findViewById(R.id.slider), R.string.player_bought_and_set, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.slider), getString(R.string.sorry_not_enough)+" "+getString(R.string.gems), Snackbar.LENGTH_LONG).show();
                    getMoreGemsPopup();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(getString(R.string.Do_you_want_to_buy_this_player)+buyable.getPrice()+" "+
                ((buyable.getPrice()>1) ? getString(R.string.gems): getString(R.string.gem)));
        builder.create().show();
    }

    private void getMoreGemsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        final ShopFragment that = this;
        builder.setPositiveButton(getString(R.string.get_free_coins), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(that.getContext(), RecommendScreen.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(getString(R.string.You_need_more_gems_do_you_want_to));
        builder.create().show();
    }

    private boolean buyImage(Buyable buyable) {
        return PrefsHandler.buyPlayerImage(getContext().getSharedPreferences(PrefsHandler.GANME_PREFS, 0), buyable);
    }

    private boolean isDefaultImage(int playerImage) {
        return playerImage == PLAYERIMAGE_DEFAULT;
    }

    private boolean alreadyOwns(int playerImage) {
        if (playerImage == PLAYERIMAGE_DEFAULT) {
            return true;
        }
        return PrefsHandler.playerImageBought(getContext().getSharedPreferences(PrefsHandler.GANME_PREFS, 0), playerImage);
    }

    public void setPlayerImage(int playerImage) {
        PrefsHandler.setPlayerImage(getContext().getSharedPreferences(PrefsHandler.GANME_PREFS, 0), playerImage);
    }



}
