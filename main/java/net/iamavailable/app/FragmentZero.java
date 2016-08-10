package net.iamavailable.app;

/**
 * Created by Arshad on 7/2/2016.
 */
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class FragmentZero extends Fragment{
    View itemView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        itemView=inflater.inflate(
                R.layout.fragment_zero, container, false);
        return itemView;
    }
}
