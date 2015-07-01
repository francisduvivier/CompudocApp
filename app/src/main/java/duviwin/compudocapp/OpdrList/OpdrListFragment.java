package duviwin.compudocapp.OpdrList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import duviwin.compudocapp.AppSettings;
import duviwin.compudocapp.Connection.Connection;
import duviwin.compudocapp.Events.EventSystem;
import duviwin.compudocapp.Events.MyEventListener;
import duviwin.compudocapp.OpdrachtDetails.Opdracht;
import duviwin.compudocapp.R;
import duviwin.compudocapp.OpdrachtDetails.ShowDetailsActivity;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class OpdrListFragment extends Fragment implements AbsListView.OnItemClickListener, MyEventListener {

    @Override
    public void handleMsg(String msg){
        opdrachten.add(Opdracht.getDummy(msg));
//        mAdapter.notifyDataSetChanged();

    }
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private OpdrItemAdapter mAdapter;
    LayoutInflater li;
    public void fillAdapter(List<Opdracht> result){

// mAdapter = new OpdrItemAdapter(getActivity(),R.layout.opdracht_item,result);
//        opdrachten.clear();
        opdrachten.addAll(result);

        mAdapter.notifyDataSetChanged();
        System.out.println("We notified the adapter");


    }
    public static OpdrListFragment newInstance() {
        OpdrListFragment fragment = new OpdrListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OpdrListFragment() {

    }
    List<Opdracht> opdrachten=new ArrayList<Opdracht>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connection.getConnection().opdrListFrgmt =this;
        opdrachten.add(Opdracht.getDummy("Loading..."));
        mAdapter = new OpdrItemAdapter(getActivity(),
                R.layout.opdracht_item, opdrachten);
        refresh();
    }
    public void refresh(){
            SharedPreferences prefMgr= PreferenceManager
                    .getDefaultSharedPreferences(getActivity().getBaseContext());
            AppSettings.userName=prefMgr.getString("userNameKey", "");
            AppSettings.password=prefMgr.getString("passwordKey", "");

        EventSystem.subscribe(Connection.getConnection().getPublisherId(), this);
        (new HttpAsyncTask()).execute(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //FDCODE
        li=inflater;
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.my_opdr_list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener&&!opdrachten.get(position).isDummy) {
           Intent intent=new Intent(getActivity(),ShowDetailsActivity.class);
            intent.putExtra("opdracht",opdrachten.get(position));
            startActivity(intent);

    }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
    private class HttpAsyncTask extends AsyncTask<OpdrListFragment, Void, List<Opdracht>> {
        private OpdrListFragment f;
        @Override
        protected List<Opdracht> doInBackground(OpdrListFragment... fragments) {
            this.f=fragments[0];
            return OpdrachtenInfo.getOpdrachtList();
        }
        @Override
        protected void onPostExecute(List<Opdracht> result) {
            f.fillAdapter(result);
        }
    }

}