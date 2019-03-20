package com.example.pr06_version2.ui.detail;

import android.content.ActivityNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pr06_version2.R;
import com.example.pr06_version2.data.local.Database;
import com.example.pr06_version2.data.model.User;
import com.example.pr06_version2.databinding.FragmentDetailBinding;
import com.example.pr06_version2.ui.main.MainActivityViewModel;
import com.example.pr06_version2.ui.main.MainActivityViewModelFactory;
import com.example.pr06_version2.utils.IntentUtils;
import com.example.pr06_version2.utils.KeyboardUtils;
import com.example.pr06_version2.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding b;
    private User userObtainedFromNavigation;
    private MainActivityViewModel viewModel;
    private NavController navController;
    private static final int NUMBER_OF_EDIT_TEXT = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupNavigation();
        obtainArguments();
        viewModel = ViewModelProviders.of(requireActivity(), new MainActivityViewModelFactory(Database.getInstance())).get(MainActivityViewModel.class);
        if (viewModel.getAvatarLiveData().getValue() == null) {
            setupViews();
        }
        observeAvatar();
        setLogicOfFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuSave) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAppBar() {
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController, appBarConfiguration);
    }

    private void setupNavigation() {
        navController = NavHostFragment.findNavController(this);
        setupAppBar();
    }

    private void obtainArguments() {
        if (getArguments() != null) {
            DetailFragmentArgs detailFragmentArgs = DetailFragmentArgs.fromBundle(getArguments());
            userObtainedFromNavigation = detailFragmentArgs.getUser();
        }
    }

    private void setupViews() {
        if (userObtainedFromNavigation != null) {
            viewModel.setAvatarToObserve(userObtainedFromNavigation.getImg_Avatar());

            b.layoutProfile.txtAddress.setText(userObtainedFromNavigation.getAddress());
            b.layoutProfile.txtEmail.setText(userObtainedFromNavigation.getEmail());
            b.layoutProfile.txtName.setText(userObtainedFromNavigation.getName());
            b.layoutProfile.txtPhonenumber.setText(String.valueOf(userObtainedFromNavigation.getTlf()));
            b.layoutProfile.txtWeb.setText(userObtainedFromNavigation.getWeb());
        } else {
            viewModel.setAvatarToObserve(viewModel.getDefaultAvatar());
        }
    }

    private void navigateToAvatarFragment() {
        b.imgAvatar.setOnClickListener(v -> navController.navigate(DetailFragmentDirections.actionDetailFragmentToAvatarFragment(
                Objects.requireNonNull(viewModel.getAvatarLiveData().getValue()))));

    }

    private void disableFields() {
        b.layoutProfile.lblName.setEnabled(false);
        b.layoutProfile.txtName.setError(getString(R.string.main_invalid_data));
        b.layoutProfile.lblEmail.setEnabled(false);
        b.layoutProfile.txtEmail.setError(getString(R.string.main_invalid_data));
        b.layoutProfile.imgEmail.setEnabled(false);
        b.layoutProfile.lblPhonenumber.setEnabled(false);
        b.layoutProfile.txtPhonenumber.setError(getString(R.string.main_invalid_data));
        b.layoutProfile.imgPhonenumber.setEnabled(false);
        b.layoutProfile.lblAddress.setEnabled(false);
        b.layoutProfile.txtAddress.setError(getString(R.string.main_invalid_data));
        b.layoutProfile.lblAddress.setEnabled(false);
        b.layoutProfile.lblWeb.setEnabled(false);
        b.layoutProfile.txtWeb.setError(getString(R.string.main_invalid_data));
        b.layoutProfile.lblWeb.setEnabled(false);
    }

    private void implicitIntentOfProfile() {
        b.layoutProfile.imgEmail.setOnClickListener(v -> {
            try {
                startActivity(IntentUtils.intentEmail(b.layoutProfile.txtEmail.getText().toString()));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        b.layoutProfile.imgPhonenumber.setOnClickListener(v -> {
            try {
                startActivity(IntentUtils.intentPhone(b.layoutProfile.txtPhonenumber.getText().toString()));
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        });

        b.layoutProfile.imgAddress.setOnClickListener(v -> {
            try {
                startActivity(IntentUtils.intentMapByAddress(b.layoutProfile.txtAddress.getText().toString()));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        b.layoutProfile.imgWeb.setOnClickListener(v -> {
            try {
                startActivity(IntentUtils.intentSearchWeb(b.layoutProfile.txtWeb.getText().toString()));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void focusOfProfile() {
        //setOnFocusChangeListener change the labels typeface if it lose the focus
        b.layoutProfile.txtName.setOnFocusChangeListener((v, hasFocus) -> changeFontOnFocus(hasFocus, b.layoutProfile.lblName));
        b.layoutProfile.txtEmail.setOnFocusChangeListener((v, hasFocus) -> changeFontOnFocus(hasFocus, b.layoutProfile.lblEmail));
        b.layoutProfile.txtPhonenumber.setOnFocusChangeListener((v, hasFocus) -> changeFontOnFocus(hasFocus, b.layoutProfile.lblPhonenumber));
        b.layoutProfile.txtAddress.setOnFocusChangeListener((v, hasFocus) -> changeFontOnFocus(hasFocus, b.layoutProfile.lblAddress));
        b.layoutProfile.txtWeb.setOnFocusChangeListener((v, hasFocus) -> changeFontOnFocus(hasFocus, b.layoutProfile.lblWeb));
    }

    private void changeFontOnFocus(boolean hasFocus, TextView lbl) {
        if (hasFocus) {
            lbl.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            lbl.setTypeface(Typeface.DEFAULT);
        }
    }

    private void validateFields(TextView lbl, ImageView img, EditText txt, boolean val) {
        if (!val) {
            lbl.setEnabled(false);
            txt.setError(getString(R.string.main_invalid_data));
            img.setEnabled(false);
        } else {
            lbl.setEnabled(true);
            img.setEnabled(true);
        }

    }

    private void viewsStateOfProfile() {
        b.layoutProfile.txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (b.layoutProfile.txtName.getText().toString().equals("")) {
                    b.layoutProfile.lblName.setEnabled(false);
                    b.layoutProfile.txtName.setError(getString(R.string.main_invalid_data));

                } else {
                    b.layoutProfile.lblName.setEnabled(true);
                }
            }
        });
        b.layoutProfile.txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields(b.layoutProfile.lblEmail,
                        b.layoutProfile.imgEmail,
                        b.layoutProfile.txtEmail,
                        ValidationUtils.isValidEmail(b.layoutProfile.txtEmail.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        b.layoutProfile.txtPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields(b.layoutProfile.lblPhonenumber,
                        b.layoutProfile.imgPhonenumber,
                        b.layoutProfile.txtPhonenumber,
                        ValidationUtils.isValidPhone(b.layoutProfile.txtPhonenumber.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        b.layoutProfile.txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields(b.layoutProfile.lblAddress,
                        b.layoutProfile.imgAddress,
                        b.layoutProfile.txtAddress,
                        !b.layoutProfile.txtAddress.getText().toString().equals(""));
            }
        });
        b.layoutProfile.txtWeb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields(b.layoutProfile.lblWeb,
                        b.layoutProfile.imgWeb,
                        b.layoutProfile.txtWeb,
                        ValidationUtils.isValidUrl(b.layoutProfile.txtWeb.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void save() {
        EditText[] editTexts = new EditText[NUMBER_OF_EDIT_TEXT];
        Boolean[] fieldControl = new Boolean[editTexts.length];
        Boolean[] booleanComparator = new Boolean[fieldControl.length];
        editTexts[0] = b.layoutProfile.txtName;
        editTexts[1] = b.layoutProfile.txtEmail;
        editTexts[2] = b.layoutProfile.txtPhonenumber;
        editTexts[3] = b.layoutProfile.txtAddress;
        editTexts[4] = b.layoutProfile.txtWeb;

        for (int i = 0; i < editTexts.length; i++) {
            fieldControl[i] = !TextUtils.isEmpty(editTexts[i].getText().toString());
            booleanComparator[i] = false;
        }

        if (Arrays.asList(fieldControl).contains(false)) {
            Snackbar.make(b.layoutProfile.lblWeb, getString(R.string.main_error_saving), Snackbar.LENGTH_LONG).show();
        } else if (Arrays.equals(fieldControl, booleanComparator)) {
            Snackbar.make(b.layoutProfile.lblWeb, getString(R.string.main_error_saving), Snackbar.LENGTH_LONG).show();
            disableFields();
        } else {
            if (userObtainedFromNavigation != null) {
                editUser();
            } else {
                saveNewUser();
            }
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }

    private void saveNewUser() {
        viewModel.addUser(new User(b.layoutProfile.txtName.getText().toString(),
                b.layoutProfile.txtEmail.getText().toString(),
                b.layoutProfile.txtAddress.getText().toString(),
                b.layoutProfile.txtWeb.getText().toString(),
                Integer.parseInt(b.layoutProfile.txtPhonenumber.getText().toString()),
                viewModel.getAvatarLiveData().getValue()));
    }

    private void editUser() {
        viewModel.editUser(userObtainedFromNavigation, b.layoutProfile.txtName.getText().toString(),
                b.layoutProfile.txtEmail.getText().toString(),
                b.layoutProfile.txtAddress.getText().toString(),
                b.layoutProfile.txtWeb.getText().toString(),
                Integer.parseInt(b.layoutProfile.txtPhonenumber.getText().toString()),
                viewModel.getAvatarLiveData().getValue());
    }

    private void setLogicOfFragment() {
        implicitIntentOfProfile();
        focusOfProfile();
        viewsStateOfProfile();
        navigateToAvatarFragment();
        b.layoutProfile.txtWeb.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftKeyboard(requireActivity());
                save();
                return true;
            }
            return false;
        });
    }

    private void observeAvatar() {
        viewModel.getAvatarLiveData().observe(this, avatar -> {
            b.imgAvatar.setImageResource(avatar.getImageResId());
            b.lblAvatar.setText(avatar.getName());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRemoving()) {
            viewModel.getAvatarLiveData().setValue(null);
        }
    }
}
