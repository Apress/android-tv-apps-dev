package com.apress.nearbyconnections;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Connections;

/**
 * Created by Paul on 11/28/15.
 */
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener {

    private static int[] NETWORK_TYPES = {
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET
    };

    private GoogleApiClient mGoogleApiClient;
    private String mRemoteHostEndpoint;

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if ( !TextUtils.isEmpty( mRemoteHostEndpoint ) ) {
                Nearby.Connections.sendReliableMessage(mGoogleApiClient,
                        mRemoteHostEndpoint, "Hello World".getBytes() );
                mHandler.postDelayed(this, 5000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            disconnect();
            mGoogleApiClient.disconnect();
        }
    }

private boolean isConnectedToNetwork() {
    ConnectivityManager connectivityManager =
            (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
    for( int networkType : NETWORK_TYPES ) {
        NetworkInfo info = connectivityManager.getNetworkInfo( networkType );
        if( info != null && info.isConnectedOrConnecting() ) {
            return true;
        }
    }
    return false;
}

private void discoverHost() {
    if( !isConnectedToNetwork() ) {
        return;
    }

    String serviceId = getString( R.string.service_id );
    Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, 60000L, this)
            .setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Log.v("Apress", "Started discovering");
                    }
                }
            });
}

    @Override
    public void onConnected(Bundle bundle) {
        discoverHost();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

@Override
public void onEndpointFound(String endpointId, String deviceId,
    final String serviceId, String endpointName) {

    byte[] payload = null;

    Nearby.Connections.sendConnectionRequest( mGoogleApiClient, deviceId, endpointId, payload,
            new Connections.ConnectionResponseCallback() {
        @Override
        public void onConnectionResponse(String s, Status status, byte[] bytes) {
            if( status.isSuccess() ) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Nearby.Connections.stopDiscovery(mGoogleApiClient, serviceId);
                mRemoteHostEndpoint = s;
                mHandler.post( mRunnable );
            } else {
                Log.e( "Apress", "Connection to endpoint failed" );
            }
        }
    }, this );

}

    private void disconnect() {
        if( !isConnectedToNetwork() )
            return;

        if( TextUtils.isEmpty(mRemoteHostEndpoint) ) {
            Nearby.Connections.stopDiscovery( mGoogleApiClient, getString( R.string.service_id ) );
        } else {
            Nearby.Connections.disconnectFromEndpoint( mGoogleApiClient, mRemoteHostEndpoint );
            mRemoteHostEndpoint = null;
        }
    }

    @Override
    public void onEndpointLost(String s) {
        mRemoteHostEndpoint = null;
    }

    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {
        Toast.makeText( this, new String( bytes ), Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDisconnected(String s) {
        mRemoteHostEndpoint = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
