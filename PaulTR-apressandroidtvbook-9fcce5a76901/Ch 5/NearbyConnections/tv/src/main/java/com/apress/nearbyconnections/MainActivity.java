package com.apress.nearbyconnections;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Connections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 11/28/15.
 */
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener {

    private static final long CONNECTION_TIME_OUT = 60000L;
    private List<String> mRemotePeerEndpoints = new ArrayList<String>();

    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET };

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( Nearby.CONNECTIONS_API )
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

    private void disconnect() {
        Nearby.Connections.stopAdvertising(mGoogleApiClient);
        Nearby.Connections.stopAllEndpoints(mGoogleApiClient);

        mRemotePeerEndpoints.clear();
    }

    private void advertise() {
        if( !isConnectedToNetwork() )
            return;

        String name = "Nearby Advertising";

        Nearby.Connections.startAdvertising(mGoogleApiClient, name, null, CONNECTION_TIME_OUT, this).setResultCallback(
            new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.v( "Apress", "Successfully advertising" );
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        advertise();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId,
        final String remoteDeviceId,
        final String remoteEndpointName,
        byte[] payload) {
        Nearby.Connections.acceptConnectionRequest( mGoogleApiClient,
            remoteEndpointId, payload, this ).setResultCallback(
                new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if( status.isSuccess() ) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    if( !mRemotePeerEndpoints.contains( remoteEndpointId ) ) {
                        mRemotePeerEndpoints.add( remoteEndpointId );
                    }
                } else {
                    Log.e( "Apress", "onConnectionRequest failed: " + status.getStatusMessage() );
                }
            }
        });
    }

    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {
        Toast.makeText(this, new String(bytes), Toast.LENGTH_SHORT).show();
        Nearby.Connections.sendReliableMessage( mGoogleApiClient, mRemotePeerEndpoints, bytes );
    }

    @Override
    public void onDisconnected(String s) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
