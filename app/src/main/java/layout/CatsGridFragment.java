package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pelaez.bautista.catadopter.Cat;
import com.pelaez.bautista.catadopter.CatAdapter;
import com.pelaez.bautista.catadopter.CatDialog;
import com.pelaez.bautista.catadopter.R;

public class CatsGridFragment extends Fragment {

    private OnGridClickListener mListener;

    private GridView mCatGrid;
    private CatAdapter mAdapter;

    private DatabaseReference mDatabase;

    public CatsGridFragment() {
        // Required empty public constructor
    }

    public static CatsGridFragment newInstance(/*String param1, String param2*/) {
        CatsGridFragment fragment = new CatsGridFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        final View v = localInflater.inflate(R.layout.fragment_cats_grid, container, false);
        mCatGrid = (GridView)v.findViewById(R.id.catsGridView);
        mAdapter = new CatAdapter(getActivity(), Cat.class, R.layout.cat_info, mDatabase.child("cats"));
        mCatGrid.setAdapter(mAdapter);
        mCatGrid.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        if(mListener != null) mListener.onGridClick((Cat)view.getTag());
                    }
                }
        );
        final View fab = v.findViewById(R.id.addCatFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CatsGridFragment.this.getActivity(), com.pelaez.bautista.catadopter.NewCatActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onCatClicked(Cat cat) {
//        if (mListener != null) {
//            mListener.onGridClick(cat);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGridClickListener) {
            mListener = (OnGridClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGridClickListener {
        // TODO: Update argument type and name
        void onGridClick(Cat cat);
    }
}
