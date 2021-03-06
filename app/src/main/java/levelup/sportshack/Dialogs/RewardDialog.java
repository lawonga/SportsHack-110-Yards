package levelup.sportshack.Dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import levelup.sportshack.FOURQuestionsActivity;
import levelup.sportshack.R;

/**
 * Created by Andy W on 2015-11-29.
 */
public class RewardDialog extends DialogFragment {
    TextView close;
    Button rewardOkay;
    ImageView giftimage;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reward_card, container);
        close = (TextView)view.findViewById(R.id.close_popup_reward);
        rewardOkay = (Button)view.findViewById(R.id.reward_okay);
        giftimage = (ImageView)view.findViewById(R.id.gift_image);

        rewardOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What happens when the user clicks OKAY; should go to next question
                Intent intent = new Intent(getActivity(), FOURQuestionsActivity.class);
                startActivity(intent);


                dismiss();
                getActivity().finish();

            }
        });

        return view;
    }
}
