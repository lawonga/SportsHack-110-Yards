package levelup.sportshack.Dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import levelup.sportshack.R;
import levelup.sportshack.ShopActivity;

/**
 * Created by Andy W on 2015-11-29.
 */
public class BeerDialog extends DialogFragment {
    TextView close, rewardHeader, rewardDescription;
    Button rewardOkay, shop;
    ImageView football;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reward_card, container);
        close = (TextView)view.findViewById(R.id.close_popup_reward);
        rewardHeader = (TextView)view.findViewById(R.id.reward_header);
        rewardDescription = (TextView)view.findViewById(R.id.reward_description);
        rewardOkay = (Button)view.findViewById(R.id.reward_okay);
        football = (ImageView)view.findViewById(R.id.gift_image);
        shop = (Button)view.findViewById(R.id.shop_icon);

        // Theme changes
        rewardHeader.setText("FREE BEER");
        rewardHeader.setTextColor(getResources().getColor((R.color.red)));
        rewardHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);
        rewardDescription.setText("Get a Free Beer from Molson Canadian");
        rewardOkay.setBackgroundColor(getResources().getColor(R.color.red));
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.beersmall);
        football.setImageBitmap(bm);

        shop.setVisibility(View.VISIBLE);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // START SHOP
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                startActivity(intent);
            }
        });
        rewardOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What happens when the user clicks OKAY; should just dismiss
                dismiss();
            }
        });

        return view;
    }
}
