/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.fragmentexample;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SimpleFragment.OnFragmentInteractionListener {


    static final String STATE_FRAGMENT = "state_of_fragment"; // key (optional)
    private Button mButton;
    private boolean isFragmentDisplayed = false;
    private int mRadioButtonChoice = 2; // The default (no choice).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.open_button);
        if (savedInstanceState != null) {
            isFragmentDisplayed =
                    savedInstanceState.getBoolean(STATE_FRAGMENT);

            // Why would I neeed this?
            // If isFragmentDisplay is true does it not already imply
            // that mButton is set to "close" since the only method
            // to set isFragmentDisplay to true also sets mButton to "close"?

            // My suspicion is that when an activity begins to stop (say when the
            // screen rotates), the text on mButton is not saved so it
            // defaults to "open"

            // docs however mentions that "By default, the system uses
            // the Bundle instance state to save information about
            // each View object in your activity layout
            // (such as the text value entered into an EditText widget).
            // So, if your activity instance is destroyed and recreated,
            // the state of the layout is restored to its previous
            // state with no code required by you."

            // however upon experimentation, if I blank out the code below
            // mButton text is reverts to "open" upon rotation.
            // hmmmmm *spinning face effect*

            if (isFragmentDisplayed) {
                // If the fragment is displayed, change button to "close".
                mButton.setText(R.string.close);
            }
        }



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFragmentDisplayed){
                    displayFragment();
                } else {
                    closeFragment();
                }

            }
        });
    }

    public void displayFragment() {
        SimpleFragment simpleFragment = SimpleFragment.newInstance(mRadioButtonChoice);
        // Get the FragmentManager and start a transaction.

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();


        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment_container, // fragment_container is a placeholder for the Fragment required by Activity code
                simpleFragment).addToBackStack(null).commit();
        // Update the Button text.
        mButton.setText(R.string.close);
        // Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    public void closeFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if the fragment is already showing.
        SimpleFragment simpleFragment = (SimpleFragment) fragmentManager
                .findFragmentById(R.id.fragment_container);
        if (simpleFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
        }
        // Update the Button text.
        mButton.setText(R.string.open);
        // Set boolean flag to indicate fragment is closed.
        isFragmentDisplayed = false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the state of the fragment (true=open, false=closed).
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    // ^seems like putting in a carriage, liken to putExtra for intents
    // from docs:
    /* As your activity begins to stop (like when you change orientation),
    the system calls the onSaveInstanceState() method
    so your activity can save state information to an instance state bundle.
    The default implementation of this method saves transient information about
    the state of the activity's view hierarchy, such as the text in an EditText
    widget or the scroll position of a ListView widget.

    To save additional instance state information for your activity,
    you must override onSaveInstanceState() and add key-value pairs
    to the Bundle object that is saved in the event that your activity
    is destroyed unexpectedly. If you override onSaveInstanceState(),
    you must call the superclass implementation if you want the default
    implementation to save the state of the view hierarchy.
     */

    @Override
    public void onRadioButtonChoice(int choice) {

        // Keep the radio button choice to pass it back to the fragment.
        mRadioButtonChoice = choice;
        Toast.makeText(this, "Choice is " + Integer.toString(choice),
                Toast.LENGTH_SHORT).show();

    }
}
