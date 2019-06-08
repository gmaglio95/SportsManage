package it.gmaglio.sportsmanage.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import it.gmaglio.sportsmanage.R;
import it.gmaglio.sportsmanage.fragment.SignInFragment;

public class SignInDialog extends BottomSheetDialogFragment implements  Toolbar.OnMenuItemClickListener {

   private Toolbar toolbar;
   private BottomSheetBehavior mBehavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SignInDialog showSignInDialog(FragmentManager fragmentManager){
        SignInDialog signInDialog = new SignInDialog();
        signInDialog.show(fragmentManager, signInDialog.getClass().getCanonicalName());
        return signInDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        FragmentTransaction signInDialogFragmentManager = this.getChildFragmentManager().beginTransaction();
        signInDialogFragmentManager.add(R.id.frameLayout,new SignInFragment());
        signInDialogFragmentManager.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(getContext(), R.layout.sign_in_with_mail_dialog, null);
        toolbar = view.findViewById(R.id.toolbar);

        return view;
      }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.sign_in_with_mail_dialog, null);
        LinearLayout linearLayout = view.findViewById(R.id.rootDialog);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
       // linearLayout.setLayoutParams(params);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitleTextColor(Color.WHITE);
   //     toolbar.inflateMenu(R.menu.example_dialog);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        return true;
    }
}
