package com.iamhoppy.hoppy;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BeerProfile extends AppCompatActivity {
    private Beer beer = new Beer();
    private User user = new User();
    private List<ImageView> ratingImages;
    private EditText commentTextBox;
    private Button postCommentButton;
    private String userComment;
    public LinearLayout postInitialCommentRow;
    private TextView editCommentClickableView;
    private TextView editableCommentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_profile);
        final Bundle bundle = getIntent().getExtras();
        beer = (Beer)bundle.getSerializable("beer");
        user = (User)bundle.getSerializable("user");
        populateBeer();

        //Reference images and add to arrays
        ratingImages = new ArrayList<ImageView>();
        ratingImages.add((ImageView) findViewById(R.id.ratingImg1));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg2));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg3));
        ratingImages.add((ImageView) findViewById(R.id.ratingImg4));
        ratingImages.add((ImageView) findViewById(R.id.ratingImg5));

        if(beer.getRating() >= 0) {
            ratingClicked(beer.getRating(), false);
        }
        ratingImages.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(0, true);
            }
        });
        ratingImages.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(1, true);
            }
        });
        ratingImages.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(2, true);
            }
        });
        ratingImages.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(3, true);
            }
        });
        ratingImages.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(4, true);
            }
        });

        //Reference views
        postInitialCommentRow = (LinearLayout)findViewById(R.id.postInitialCommentRow);
        commentTextBox = (EditText)findViewById(R.id.commentTextBox);
        postCommentButton = (Button)findViewById(R.id.postCommentButton);

        //Listener for postCommentButton
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        //Set visibility on whether or not user has already posted comment
        if(beer.getMyComment() != null && !beer.getMyComment().equals("NULL")){  //user has previously recorded a comment
            commentTextBox.setVisibility(View.GONE); //gone means removed from layout and won't occupy space
            postCommentButton.setVisibility(View.GONE); //invisible means removed form layout but still occupies space
            displayMyComment();
        } else {                        //make commentTextBox and postCommentButton visible as this is the user's initial post
            postInitialCommentRow.setVisibility(View.VISIBLE);
            commentTextBox.setVisibility(View.VISIBLE);
            postCommentButton.setVisibility(View.VISIBLE);
        }
        displayOtherComments(); //until user wants to edit
    }

    private void displayOtherComments() {
        LinearLayout commentsSection = (LinearLayout)findViewById(R.id.commentsSection);
        //loop through display other users' comments array and user firstName and lastInitial
        List<String> comments = beer.getComments();
        int i = 1;
        for(String c : comments){
            //generate and display horizontal rule
            View horizontalRule = new View(this);
            horizontalRule.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            horizontalRule.setId(99 - i);
            horizontalRule.setBackgroundColor(Color.parseColor("#B3B3B3"));
            commentsSection.addView(horizontalRule);
            //createTextView and setText
            TextView commentView = new TextView(this);
            commentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            commentView.setText(c);
            commentView.setTextSize(15);
            //commentView.setId(99 + i);
            commentsSection.addView(commentView);
            i++;
        }
    }

    private void displayMyComment(){
        //display user's comment at the top with edit option, hide the EditText and postCommentButton
        postInitialCommentRow = (LinearLayout)findViewById(R.id.postInitialCommentRow);
        //add clickable "edit comment" view and to this layout
        editableCommentView = new TextView(this);
        editableCommentView.setText(beer.getMyComment());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        editableCommentView.setLayoutParams(layoutParams);
        editCommentClickableView = new TextView(this);
        editCommentClickableView.setText("Edit Comment");
        editCommentClickableView.setTextColor(Color.parseColor("#006699"));
        editCommentClickableView.setTypeface(null, Typeface.ITALIC);
        editCommentClickableView.setLayoutParams(layoutParams);
        editCommentClickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postInitialCommentRow.setVisibility(View.VISIBLE);
                commentTextBox.setVisibility(View.VISIBLE);
                postCommentButton.setVisibility(View.VISIBLE);
                editableCommentView.setVisibility(View.GONE);
                editCommentClickableView.setVisibility(View.GONE);
            }
        });
        postInitialCommentRow.addView(editableCommentView);
        postInitialCommentRow.addView(editCommentClickableView);
    }

    private void postComment(){
        userComment = commentTextBox.getText().toString();   //get text from EditText view
        userComment += " -- " + user.getFirstName();
        try{
            userComment = URLEncoder.encode(userComment, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        beer.setMyComment(userComment);
        callReviewService();
        commentTextBox.setVisibility(View.GONE); //gone means removed from layout and won't occupy space
        postCommentButton.setVisibility(View.GONE); //invisible means removed form layout but still occupies space
        postInitialCommentRow.setVisibility(View.VISIBLE);
        displayMyComment();
    }

    private void setClickable(int img1, int img2, int img3, int img4){
        ratingImages.get(img1).setClickable(true);
        ratingImages.get(img2).setClickable(true);
        ratingImages.get(img3).setClickable(true);
        ratingImages.get(img4).setClickable(true);
    }

    private void ratingClicked(double rating, boolean makeCall) {
        int intRating = (int)rating;
        beer.setRating(rating);
        ratingImages.get(intRating).setClickable(false);
        switch (intRating) {
            case 0: {
                ratingImages.get(0).setImageResource(R.drawable.rate1);
                setClickable(1, 2, 3, 4);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 1: {
                ratingImages.get(1).setImageResource(R.drawable.rate2);
                setClickable(0, 2, 3, 4);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 2: {
                ratingImages.get(2).setImageResource(R.drawable.rate3);
                setClickable(0, 1, 3, 4);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 3: {
                ratingImages.get(3).setImageResource(R.drawable.rate4);
                setClickable(0, 1, 2, 4);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 4: {
                ratingImages.get(4).setImageResource(R.drawable.rate5);
                setClickable(0, 1, 2, 3);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                break;
            }
            default: { //rate null
                //no action
            }
        }
        if(makeCall) {
            callReviewService();
        }
    }

    private void callReviewService() {
        Intent updateIntent = new Intent(getApplicationContext(), UpdateReview.class);
        try {
            updateIntent.putExtra("userID", user.getId());
            updateIntent.putExtra("beerID", beer.getId());
            updateIntent.putExtra("rating", beer.getRating());
            if(beer.getMyComment() != null && beer.getMyComment().length() > 1) {
                updateIntent.putExtra("comment", beer.getMyComment());
            } else {
                updateIntent.putExtra("comment", "NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getApplicationContext().startService(updateIntent);
    }

    private void populateBeer() {
        ImageView breweryLogo = (ImageView)findViewById(R.id.breweryLogo);
        Picasso.with(getApplicationContext())
                .load(beer.getBreweryLogoURL())
                .fit()
                .centerInside()// here you resize your image to whatever width and height you like
                .into(breweryLogo);
        ((TextView)findViewById(R.id.beerName)).setText(beer.getName());
        ((TextView)findViewById(R.id.breweryName)).setText(beer.getBrewery());
        if(beer.getAbv() != null && beer.getIbu() != null) {
            ((TextView) findViewById(R.id.beerABVIBU)).setText("ABV " + beer.getAbv() + ", IBU " + beer.getIbu());
        }
        ((TextView)findViewById(R.id.beerDescription)).setText(beer.getDescription());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
