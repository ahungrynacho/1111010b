package fabflix.cinephim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView keywords = null;
    private ListView listView = null;
    private View mSearchResultView = null;
    private View mProgressView = null;

    private static final String MOVIES_PREF = "movies_cache";
    private List<Movie> MOVIES = null;
    private int START_INDEX = 0;
    private String QUERY_CACHE = "";
    private boolean NEXT = true;
    private boolean BACK = !NEXT;

    private List<Movie> movieSublist(List<Movie> movies, int startIndex, boolean next) {
        /* Returns a sublist of 10 Movie objects from the main movie list. */

        List<Movie> sublist = new ArrayList<Movie>();


        int endIndex = 0;
        if (next) {
            // add the next 10 movies to the sublist
            if (movies.size() < 10) {
                START_INDEX = 0;
                endIndex = movies.size();
            }
            else if (startIndex + 10 >= movies.size()) {
                START_INDEX = movies.size();    // required for the back-button feature
                endIndex = movies.size();
            }
            else {
                endIndex = START_INDEX = startIndex + 10;
            }

            if ((startIndex % 10) != 0) {
                startIndex -= (startIndex % 10);
            }

            for (int i = startIndex; i < endIndex; ++i) {
                sublist.add(movies.get(i));
            }
        }
        else {
            // add the previous 10 movies to the sublist skipping the current 10 shown
            if (movies.size() < 10) {
                START_INDEX = movies.size();
                endIndex = 0;
            }
            else if ((movies.size() >= 10) && startIndex == movies.size()) {
                startIndex -= (START_INDEX % 10);
                START_INDEX = startIndex;
                endIndex = startIndex - 10;
            }
            else if (startIndex - 20 < 0) {
                // START_INDEX stays the same
                endIndex = 0;
            }
            else {
                START_INDEX = startIndex - 10;
                endIndex = startIndex - 20;
            }


            for (int i = endIndex; i < START_INDEX; ++i) {
                sublist.add(movies.get(i));
            }
        }
        return sublist;
    }

    private void displayOnScreen(List<Movie> movieSublist) {
        ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, movieSublist);
        listView.setAdapter(adapter);       // allows the list of Movie objects to be displayed

        SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("START_INDEX_CACHE", START_INDEX);
        editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itms to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.sign_out) {
            SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(SearchActivity.this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // needed for showProgress()
        mSearchResultView = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.search_progress);

        listView = (ListView) findViewById(R.id.search_list);
        keywords = (AutoCompleteTextView) findViewById(R.id.keywords);

        Button mSearchButton = (Button) findViewById(R.id.submit_search_button);
        Button mBackButton = (Button) findViewById(R.id.back_button);
        Button mNextButton = (Button) findViewById(R.id.next_button);

        SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
        if (preferences.contains("QUERY_CACHE") && preferences.contains("START_INDEX_CACHE")) {
            showProgress(true);

            START_INDEX = preferences.getInt("START_INDEX_CACHE", 0);
            QUERY_CACHE = preferences.getString("QUERY_CACHE", "");
            keywords.setText(QUERY_CACHE);
            SearchTask task = new SearchTask(QUERY_CACHE);
            task.execute((Void) null);
        }

        mSearchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress(true);

                        // cache the user's search query keyword input
                        SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("QUERY_CACHE", keywords.getText().toString());
                        editor.commit();

                        START_INDEX = 0;        // reset the index on every new search query
                        SearchTask task = new SearchTask(keywords.getText().toString());
                        task.execute((Void) null);
                    }
                }
        );

        mBackButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
                        if (preferences.contains("START_INDEX_CACHE")) {
                            List<Movie> sublist = movieSublist(MOVIES, START_INDEX, BACK);
                            displayOnScreen(sublist);
                        }
                    }
                }
        );

        mNextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getSharedPreferences(MOVIES_PREF, 0);
                        if (preferences.contains("START_INDEX_CACHE")) {
                            List<Movie> sublist = movieSublist(MOVIES, START_INDEX, NEXT);
                            displayOnScreen(sublist);
                        }
                    }
                }
        );




    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSearchResultView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSearchResultView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchResultView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSearchResultView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class SearchTask extends AsyncTask<Void, Void, List<Movie>> {
        String keywords = null;
        public SearchTask(String keywords) {
            this.keywords = keywords;
        }
        @Override
        protected List<Movie> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                FabflixModelJdbc database = new FabflixModelJdbc();
                return database.searchByTitle(this.keywords);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(final List<Movie> movieList) {
            showProgress(false);
            View mNoMoviesFoundView = findViewById(R.id.no_movies_found_view);
            View mSearchResultView = findViewById(R.id.search_list);
            View mNextBackView = findViewById(R.id.next_back_buttons);

            if (movieList.size() > 0) {
                mNoMoviesFoundView.setVisibility(View.GONE);
                mSearchResultView.setVisibility(View.VISIBLE);
                mNextBackView.setVisibility(View.VISIBLE);

//                Collections.sort(movieList);
                MOVIES = movieList;
                List<Movie> movieSublist = movieSublist(MOVIES, START_INDEX, NEXT);
                displayOnScreen(movieSublist);
            }
            else {
                mNextBackView.setVisibility(View.GONE);
                mSearchResultView.setVisibility(View.GONE);
                mNoMoviesFoundView.setVisibility(View.VISIBLE);
            }
        }
    }
}
