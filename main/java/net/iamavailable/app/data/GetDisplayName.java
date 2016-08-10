package net.iamavailable.app.data;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GetDisplayName extends ActionHandler {
    @Override
    public void performAction() {
        Log.i(DataService.TAG,"displayName request received");
        waitUntilConnected();

        Person currentPerson = Plus.PeopleApi.getCurrentPerson(getGoogleApiClient());
        if (currentPerson != null) {
            String name = currentPerson.getDisplayName();
            String email= Plus.AccountApi.getAccountName(getGoogleApiClient());
            Intent i=new Intent(DataService.BROADCAST_GET_DISPLAY_NAME);
            i.putExtra("displayName", name);
            i.putExtra("id",email);
            sendLocalBroadcast(i);
        }
    }
}
