package levelup.sportshack.Dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import levelup.sportshack.R;
import levelup.sportshack.RoadActivity;

/**
 * Created by Andy W on 2015-11-28.
 */
public class WelcomeDialog extends DialogFragment {
    TextView cancel, team_name, deal_text;
    String previousName, previousGift;
    Button okay_button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_card, container);
        cancel = (TextView)view.findViewById(R.id.close_popup_reward);
        team_name = (TextView)view.findViewById(R.id.team_name);
        deal_text = (TextView)view.findViewById(R.id.deal);
        okay_button = (Button)view.findViewById(R.id.welcome_okay);


        previousName = getArguments().getString("name");
        previousGift = getArguments().getString("gift");

        //Set stuff
        team_name.setText(previousName);
        deal_text.setText(previousGift);
        getDialog().setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
