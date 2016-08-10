package net.iamavailable.app;

/**
 * Created by Arshad on 6/30/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class ViewPagerAdapterCurrentUserHistory extends PagerAdapter  {
    Context context;
    LayoutInflater inflater;
    public ViewPagerAdapterCurrentUserHistory(Context context) {
        this.context=context;
    }
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    double latitude = 0, longitude = 0;
    ImageView iv_user_pic, iv_venue_pic, group,place;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location,name,lik, likbtn;
    TextView like_btn_like,help;
    String[] item;
    public static String[] display;
    View itemView;
    int iterator;
    public static String id;

    @Override
    public int getCount() {
        return HistoryList.size;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            if(position==0) {
               // iterator = position + 1;
                iterator=(HistoryDataCurrentUser.locid)+1;
            }else{
               // iterator=(20*position)+1;
                iterator=((HistoryDataCurrentUser.locid)+(20*position))+1;
            }

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.like_items, container, false);
            item = HistoryDataCurrentUser.item;
            myPref = HistoryDataCurrentUser.myPref;
            name = (TextView) itemView.findViewById(R.id.like_intricate);
            tv_date = (TextView) itemView.findViewById(R.id.like_date);
            tv_duration = (TextView) itemView.findViewById(R.id.like_session);
            tv_status = (TextView) itemView.findViewById(R.id.like_alias);
            postStatus = (TextView) itemView.findViewById(R.id.post2_btn_post);
            tv_location = (TextView) itemView.findViewById(R.id.like_address);
            iv_user_pic = (ImageView) itemView.findViewById(R.id.like_pic_user);
            iv_venue_pic = (ImageView) itemView.findViewById(R.id.like_pic_venue);
            lik=(TextView) itemView.findViewById(R.id.like_post_likes);
            likbtn=(TextView) itemView.findViewById(R.id.like_btn_like);
            help=(TextView) itemView.findViewById(R.id.like_help);
            group=(ImageView) itemView.findViewById(R.id.like_group);
            place=(ImageView) itemView.findViewById(R.id.like_place);
            name.setText(item[iterator].toUpperCase() + "\nLUCID");
            Bitmap b = (Bitmap) decodeBase64(item[iterator + 6]);
            Bitmap vp = decodeBase64(item[iterator + 7]);
            iv_user_pic.setImageBitmap(b);
            iv_venue_pic.setImageBitmap(vp);
            tv_date.setText(item[iterator + 11]);
            tv_duration.setText(item[iterator + 9] + " - " + item[iterator + 10]);
            tv_status.setText(item[iterator + 14] + item[iterator + 8]);
            tv_location.setText(item[iterator + 17] + ", " + item[iterator + 18]);
            lik.setText((position+1)+"/"+HistoryList.size+" \nTHIS POST HAS "+item[iterator+15]+" LIKES");
            HistoryDataCurrentUser.likes.setText(myPref.getString("likess", " "));
            HistoryDataCurrentUser.dollars.setText(myPref.getString("tokenss", " "));


            if(HistoryList.flag==1){
                likbtn.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
            }


            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent i=new Intent(context,EnlargePic2.class);
                    // i.putExtra("up1",item[iterator+6]);
                    // i.putExtra("up2",item[iterator+7]);
                    i.putExtra("pos",HistoryDataCurrentUser.arr[position]);
                    i.putExtra("picno","1");
                    context.startActivity(i);*/
                }
            });
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent i=new Intent(context,EnlargePic2.class);
                    //i.putExtra("up1",item[iterator+7]);
                    // i.putExtra("up2",item[iterator+6]);
                    i.putExtra("pos",HistoryDataCurrentUser.arr[position]);
                    i.putExtra("picno","2");
                    context.startActivity(i);*/
                }
            });
            container.addView(itemView);

        }catch(Exception ex){}
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
