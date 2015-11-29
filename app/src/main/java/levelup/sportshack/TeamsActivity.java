package levelup.sportshack;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import levelup.sportshack.Dialogs.WelcomeDialog;

/**
 * Created by Andy W on 2015-11-27.
 */

//TODO Added in Picasso, CircleImageView, AndroidViewAnimations libraries
//TODO Implement these things
public class TeamsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teams_activity);
        GridView gridView = (GridView)findViewById(R.id.team_grid);
        gridView.setAdapter(new ImageAdapter(this));
        getSupportActionBar().setElevation(0);
    }

    /** THIS IS THE IMAGEADAPATER **/
    public class ImageAdapter extends BaseAdapter{
        private Context mContext;
        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RoadActivity.class);
                    intent.putExtra("name", name[position]);
                    intent.putExtra("gift", gifts[position]);
                    startActivity(intent);
                }
            });
            return imageView;
        }
    }

    public Integer[] mThumbIds = {
        R.drawable.oval_20_group, R.drawable.oval_20_copy_group,
            R.drawable.oval_20_copy_2_group,
            R.drawable.oval_20_copy_7_group,
            R.drawable.oval_20_copy_4_group,
            R.drawable.oval_20_copy_5_group,
            R.drawable.oval_20_copy_6_group,
            R.drawable.oval_20_copy_3_group,
            R.drawable.ottawa_red_blacks

    };

    public String[] name = {
        "Saskatchewan Roughriders", "Tiger-Cats", "Winnipeg Bluebombers", "Toronto Agronauts",
            "BC Lions", "Calgary Stampeders", "Edmonton Eskimos",
            "Montreal Alouettes", "Ottawa Redblacks"
    };

    public String[] gifts = {
        "GET A 10% DISCOUNT ON A SEASON PASS",
            "GET 10% OFF ON THE SEASON PASS",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",
            "GET A 25% DISCOUNT ON YOUR BEER",

    };
}
