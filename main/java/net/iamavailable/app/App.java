package net.iamavailable.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.newrelic.agent.android.NewRelic;
import net.iamavailable.app.data.DataService;

public class App extends Application {

    private static App applicationContext;
    private static NewRelic newrelicAgent;

    public static App getInstance(Context c) {
        return (App) c.getApplicationContext();
    }

    public static App getInstance() {

        if (applicationContext == null) {
            throw new IllegalStateException("Must not be called before onCreate is finished.");
        }
        return applicationContext;
    }

    /**
     * Provide public access to New Relic android agent.
     * <p/>
     * This method will also setup the agent if required.
     *
     * @return New Relic android agent
     */
    public static NewRelic getNewRelicAgent() {
        if (newrelicAgent == null) {
            newrelicAgent = NewRelic.withApplicationToken(
                    "AA2e74da6db7e404934f3e4559e3dd93402723de73"
            );
        }
        newrelicAgent.start(applicationContext);
        return newrelicAgent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

        getNewRelicAgent();
        requestCurrentLocation();
    }
    private void requestCurrentLocation()  {

        Intent locationIntent = new Intent();
        locationIntent.setAction(DataService.BROADCAST_CURRENT_LOCATION);
        ComponentName locationComponent = new ComponentName(DataService.class.getPackage().getName(), DataService.class.getName());
        locationIntent.setComponent(locationComponent);
        startService(locationIntent);
    }
}

