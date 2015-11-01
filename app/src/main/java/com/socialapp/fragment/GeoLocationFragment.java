package com.socialapp.fragment;



import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.socialapp.R;
import com.socialapp.adapter.LocalityAdapter;
import com.socialapp.inter.FragmentInterface;
import com.socialapp.utils.LocationProvider;
import com.socialapp.utils.LoginInfo;
import com.socialapp.utils.Network;
import com.socialapp.utils.ProgressDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class GeoLocationFragment extends Fragment  implements View.OnClickListener, LocationProvider.AppLocation, AdapterView.OnItemSelectedListener
{
    private String currentLocation="0.0,0.0";
    private Spinner spinner;
    private String TAG=GeoLocationFragment.class.getSimpleName();
    private FragmentInterface fragmentInterface;
    private Address selectedLocality;
    private ProgressDialog progress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface= (FragmentInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LocationProvider.getInstance().doConnectLocationClient(this);
        View rootView = inflater.inflate(R.layout.frgment_geolocation, container, false);
        spinner= (Spinner) rootView.findViewById(R.id.spinner_choose_locality);
        Button ok= (Button) rootView.findViewById(R.id.locality_choose_button);
        ok.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.locality_choose_button:
                if(selectedLocality!=null)
                {
                    LoginInfo info=new LoginInfo(getActivity());
                    info.setLocalityInfo(selectedLocality.getLocality());
                    fragmentInterface.goToMainScreen();
                }
        }
    }

    @Override
    public void getLocation(double lat, double lng) {
        Log.i(TAG, "lat++++++++++++++lng" + lat + "," + lng);
        currentLocation=lat+","+lng;
        if(new Network(getActivity()).isAvailable(true))
        {
            progress = new ProgressDialog(getActivity(),getActivity().getResources().getString(R.string.fetching_locality));
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress.show();
            getLocality(lat,lng);
        }
    }

    private void getLocality(double lat,double lng)
    {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 5);
            if(addresses.size()==0)
            {
                showApplicationCloseDialog();
                return;
            }

            ArrayList<Address> result=new ArrayList<Address>();
            Set<String> titles = new HashSet<String>();

            for (int i=0;i<addresses.size();i++)
            {
                if(titles.add(addresses.get(i).getLocality()))
                {
                    result.add(addresses.get(i));;
                }
            }

            Address[] resultArray = new Address[result.size()];
            resultArray = result.toArray(resultArray);

            LocalityAdapter adapter=new LocalityAdapter(getActivity(),R.layout.category_adapter_layout, resultArray);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }
        catch (Exception ex)
        {
            Log.i(TAG,""+ex.getMessage());
            showApplicationCloseDialog();
        }
        if(progress!=null)
        {
            progress.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedLocality= (Address) spinner.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* take user permission to close application
    */
    private void showApplicationCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.failed_fetching_locality));
        builder.setPositiveButton(getResources().getString(R.string.app_close_dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fragmentInterface.goToMainScreen();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.app_close_dialog_button_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
