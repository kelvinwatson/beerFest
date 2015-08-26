package com.iamhoppy.hoppy;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.text.TextUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView myNameView;
    private TextView timeView;
    private LinearLayout myCommentRow;

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
            postInitialCommentRow.setVisibility(View.GONE);
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
        //get reference to row
        LinearLayout othersCommentsRow = (LinearLayout)findViewById(R.id.othersCommentsRow);

        //define layout params
        LinearLayout.LayoutParams fullWidthText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        fullWidthText.setMargins(10,5,10,0);
        LinearLayout.LayoutParams halfWidth = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
        halfWidth.setMargins(10,5,10,0);
        LinearLayout.LayoutParams horizontalRuleParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
        horizontalRuleParam.setMargins(10,5,10,0);

        //loop through display other users' comments array and user firstName and lastInitial
        List<String> comments = beer.getComments();
        int i = 1;
        for(String c : comments){
            //parse out the name
            String[] commentLines = c.split(System.getProperty("line.separator"));
            String pUserName = commentLines[0];
            String pTime = commentLines[1];
            System.out.println("pTime="+pTime);
            //TODO: error handling: what if comment is only one line (name?) possible?

            //reconstruct comment from lines array
            StringBuilder sBuilder = new StringBuilder();
            for(int k=2,len=commentLines.length; k<len; k++){
                if(k != len-1) sBuilder.append(commentLines[k]+"\n");
                else sBuilder.append(commentLines[k]);
            }
            String pComment = sBuilder.toString();

            //generate and display horizontal rule
            if(i!=1) {
                View horizontalRule = new View(this);
                horizontalRule.setLayoutParams(horizontalRuleParam);
                horizontalRule.setId(99 - i);
                horizontalRule.setBackgroundColor(Color.parseColor("#B3B3B3"));
                othersCommentsRow.addView(horizontalRule);
            }

            //generate views
            TextView nameView = new TextView(this);
            nameView.setLayoutParams(halfWidth);

            nameView.setText(pUserName);
            nameView.setTypeface(null, Typeface.BOLD);
            nameView.setTextColor(Color.parseColor("#EB9100"));
            nameView.setTextSize(15);

            TextView timeView = new TextView(this);
            timeView.setLayoutParams(halfWidth);
            timeView.setGravity(Gravity.RIGHT);
            timeView.setText(pTime);
            timeView.setTextSize(10);

            TextView commentView = new TextView(this);
            commentView.setLayoutParams(fullWidthText);
            commentView.setText(pComment);
            commentView.setTypeface(null, Typeface.ITALIC);
            commentView.setTextSize(15);
            commentView.setTextColor(Color.parseColor("#000000"));
            //commentView.setBackgroundColor(Color.parseColor("#E0FFFF"));

            //generate new horizontal layout for name and timeStamp and specify params
            LinearLayout hLL = new LinearLayout(getApplicationContext());
            hLL.setOrientation(LinearLayout.HORIZONTAL);

            //add created views
            hLL.addView(nameView);
            hLL.addView(timeView);
            othersCommentsRow.addView(hLL);
            othersCommentsRow.addView(commentView);
            i++;
        }
    }



    private void displayMyComment(){
        //display user's comment at the top with edit option, hide the EditText and postCommentButton
        myCommentRow = (LinearLayout)findViewById(R.id.myCommentRow);
        //specify layout parameters for vertical layout
        LinearLayout.LayoutParams fullWidth = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        fullWidth.setMargins(10,5,10,0);
        //create new horizontal layout for name and timeStamp and specify params
        LinearLayout horizontalForNameTime = new LinearLayout(getApplicationContext());
        horizontalForNameTime.setOrientation(LinearLayout.HORIZONTAL);
        //horizontalLayout set weightSum
        LinearLayout.LayoutParams halfWidth = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
        halfWidth.setMargins(10,5,10,0);
        //split fullComment into name and comment http://stackoverflow.com/questions/3451903/extracting-substring-by-lines
        String[] commentLines = beer.getMyComment().split(System.getProperty("line.separator"));
        String parsedUserName = commentLines[0];
        String parsedTime = commentLines[1];
        //error handling: what if comment is only one line (name?) possible?

        //reconstruct comment from lines array
        StringBuilder aggregate = new StringBuilder();
        //copy array from index 1 into a commentString
        for(int k=2,len=commentLines.length; k<len; k++){
            if(k != len-1) aggregate.append(commentLines[k]+"\n");
            else aggregate.append(commentLines[k]);
        }
        String parsedComment = aggregate.toString();

        myNameView = new TextView(this);
        myNameView.setLayoutParams(halfWidth);
        myNameView.setText(parsedUserName);
        myNameView.setTypeface(null, Typeface.BOLD);
        myNameView.setTextColor(Color.parseColor("#EB9100"));
        myNameView.setTextSize(15);

        timeView = new TextView(this);
        timeView.setLayoutParams(halfWidth);
        timeView.setText(parsedTime);
        timeView.setGravity(Gravity.RIGHT);
        timeView.setTextSize(10);

        editableCommentView = new TextView(this);
        editableCommentView.setLayoutParams(fullWidth);
        editableCommentView.setText(parsedComment);
        editableCommentView.setTypeface(null, Typeface.ITALIC);
        editableCommentView.setLayoutParams(fullWidth);
        editableCommentView.setTextSize(15);
        editableCommentView.setTextColor(Color.parseColor("#000000"));
        editableCommentView.setBackgroundColor(Color.parseColor("#FFE5B4"));

        editCommentClickableView = new TextView(this);
        editCommentClickableView.setText("Edit Comment");
        editCommentClickableView.setTextColor(Color.parseColor("#006699"));
        editCommentClickableView.setTypeface(null, Typeface.ITALIC);
        editCommentClickableView.setLayoutParams(fullWidth);
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

        horizontalForNameTime.addView(myNameView);
        horizontalForNameTime.addView(timeView);
        myCommentRow.addView(horizontalForNameTime);
        myCommentRow.addView(editableCommentView);
        myCommentRow.addView(editCommentClickableView);
    }

    private void postComment(){
        userComment = commentTextBox.getText().toString();   //get text from EditText view
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:").format(Calendar.getInstance().getTime());
        System.out.println("post comment timeStamp="+timeStamp);
        userComment = user.getFirstName() + "\n" + timeStamp + "\n" + userComment;
        beer.setMyComment(userComment);
        callReviewService(userComment); //encodes comment and updates API
        commentTextBox.setVisibility(View.GONE); //gone means removed from layout and won't occupy space
        postCommentButton.setVisibility(View.GONE); //invisible means removed form layout but still occupies space
        postInitialCommentRow.setVisibility(View.GONE);
        myCommentRow.setVisibility(View.VISIBLE);
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
            callReviewService(beer.getMyComment());
        }
    }

    private void callReviewService(String comment) {
        try{
            comment = URLEncoder.encode(beer.getMyComment(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        Intent updateIntent = new Intent(getApplicationContext(), UpdateReview.class);
        try {
            updateIntent.putExtra("userID", user.getId());
            updateIntent.putExtra("beerID", beer.getId());
            updateIntent.putExtra("rating", beer.getRating());
            if(beer.getMyComment() != null && beer.getMyComment().length() > 1) {
                updateIntent.putExtra("comment", comment);
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
