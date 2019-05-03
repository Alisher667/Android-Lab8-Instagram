/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


  Boolean signUpModeActive = true;
  TextView changeSignupModeTextView;
  EditText passwordEditText;


  public void showUserList() {

    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);

  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

      signUp(view);

    }

    return false;
  }

  @Override
  public void onClick(View view){
    if (view.getId() == R.id.changeSignupModeTextView){

      Button signupButton = (Button) findViewById(R.id.signupButton);

      if (signUpModeActive) {

        signUpModeActive = false;
        signupButton.setText("Login");
        changeSignupModeTextView.setText("Or, Signup");

      } else {

        signUpModeActive = false;
        signupButton.setText("Signup");
        changeSignupModeTextView.setText("Or, Login");


      }

    } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoimageView) {

      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
  }

  public void signUp(View view){

    EditText usernameEditText = (EditText) findViewById(R.id.userNameEditText);


    if (usernameEditText.getText().toString().matches("")|| passwordEditText.getText().toString().matches("")) {

      Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();

    } else {

      if (signUpModeActive) {


        ParseUser user = new ParseUser();

        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {

              Log.i("Signup", "Successful");

            } else {

              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
          }
        });
      } else {

        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {

            if (user != null){

              Log.i("Signup", "Login successful");
              showUserList();

            } else {

              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

          }
        });

      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

    changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

    changeSignupModeTextView.setOnClickListener(this);

    RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

    ImageView logoImageView = (ImageView) findViewById(R.id.logoimageView);

    backgroundRelativeLayout.setOnClickListener(this);

    logoImageView.setOnClickListener(this);

    passwordEditText = (EditText) findViewById(R.id.passwordEditText);


    passwordEditText.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null){
        showUserList();
    }


//    Advanced Parse Techniques
//
//
    /*
    ParseObject score = new ParseObject("Score");
    score.put("username", "rob1");
    score.put("score", 862);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {

        if (e == null){

          Log.i("SaveInBackground", "Successful");

        } else {

          Log.i("SaveInBackground", "Failed. Error: " + e.toString());

        }

      }
    });
    */

    /*
    ParseQuery<ParseObject>  query = ParseQuery.getQuery("Score");

    query.getInBackground("cD9Sk79ATM", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {

        if (e == null && object != null) {

          object.put("username", "rob");
          object.put("score", 200);
          object.saveInBackground(

          );

          Log.i("ObjectValue", object.getString("username"));
          Log.i("ObjectValue", Integer.toString(object.getInt("score")));
        }

      }
    });


    //Create Tweet class, username tweet, save on Parse, then query it, and update the tweet content
    ParseObject tweet = new ParseObject("Tweet");
    tweet.put("username", "tommy");
    tweet.put("tweet", "Hey there!");

    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {

          Log.i("Tweet", "Successful");

        } else {

          Log.i("Tweet", "Failed");

        }
      }
    });


    ParseQuery<ParseObject>  query = ParseQuery.getQuery("Tweet");

    query.getInBackground("KqHDt9SfZx", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {

        if (e == null && object != null) {

          Log.i("Tweet", "Successful");

          object.put("tweet", "Bye!");
          object.saveInBackground();

        } else {

          Log.i("Tweet", "Failed");

        }

      }
    });
    */

//    Advanced Queries
//
//
    /*
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.whereEqualTo("username", "tommy");
    query.setLimit(1);


    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {

        if (e == null){

          Log.i("findInBackground", "Retrieved" + objects.size() + "objects" );

          if (objects.size() > 0){

            for (ParseObject object : objects){

              Log.i("findInBackgroundResult", object.getString("username"));
              Log.i("findInBackgroundResult", Integer.toString(object.getInt("score")));


            }

          }

        }

      }
    });

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.whereGreaterThan("score", 200);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {

        if (e == null && objects != null) {

          for (ParseObject object : objects){

            object.put("score", object.getInt("score") + 50);
            object.saveInBackground();

          }

        }

      }
    });
    */


//    Parse Users
    /*
    ParseUser user = new ParseUser();

    user.setUsername("robpercival");
    user.setPassword("myPass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {

       if (e == null) {

         Log.i("Sign Up", "Successful");

       } else {

         Log.i("Sign Up", "Failed");

       }

      }
    });


    ParseUser.logInInBackground("robpercival", "asdf", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {

        if (user != null) {

          Log.i("Login", "Successful");

        } else {

          Log.i("Login", "Failed: " + e.toString());

        }

      }
    });


    ParseUser.logOut();
    if (ParseUser.getCurrentUser() != null){

      Log.i("currentUser", "User logged in " + ParseUser.getCurrentUser().getUsername());

    } else {

      Log.i("currentUser", "User not logged in");

    }
    */




    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}