package com.example.pr06_version2.ui.avatar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pr06_version2.R;
import com.example.pr06_version2.data.local.Database;
import com.example.pr06_version2.data.model.Avatar;
import com.example.pr06_version2.databinding.FragmentAvatarv2Binding;
import com.example.pr06_version2.ui.main.MainActivityViewModel;
import com.example.pr06_version2.ui.main.MainActivityViewModelFactory;
import com.example.pr06_version2.utils.ResourcesUtils;

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

public class AvatarFragment extends Fragment {

    private FragmentAvatarv2Binding b;
    private NavController navController;
    private MainActivityViewModel viewModel;
    private Avatar avatarFromUser;
    private ImageView[] imgCats;
    private static final int NUMBER_OF_CATS = 6;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_avatarv2, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupNavigation();
        obtainArguments();
        viewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory(Database.getInstance())).get(MainActivityViewModel.class);
        setupAvatars();
        viewModel.tintSavedImageV2(avatarFromUser, viewModel.queryAvatars(), Arrays.asList(imgCats));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.avatar_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuSelect) {
//            viewModel.setUserAvatar(avatarFromUser);
            viewModel.setAvatarToObserve(avatarFromUser);
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigation() {
        navController = NavHostFragment.findNavController(this);
        setupAppBar();
    }

    private void setupAppBar() {
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController, appBarConfiguration);
    }

    private void obtainArguments() {
        Objects.requireNonNull(getArguments());
        AvatarFragmentArgs avatarFragmentArgs = AvatarFragmentArgs.fromBundle(getArguments());
        avatarFromUser = avatarFragmentArgs.getCurrentAvatar();
    }

    private void setupAvatars() {
        imgCats = new ImageView[NUMBER_OF_CATS];
        imgCats[0] = b.imgCat1;
        imgCats[1] = b.imgCat2;
        imgCats[2] = b.imgCat3;
        imgCats[3] = b.imgCat4;
        imgCats[4] = b.imgCat5;
        imgCats[5] = b.imgCat6;

        for (int i = 0; i < imgCats.length; i++) {
            imgCats[i].setImageResource(viewModel.queryAvatars().get(i).getImageResId());
        }

        imgCats[0].setOnClickListener(v -> tintSelectedAvatar(imgCats[0]));
        imgCats[1].setOnClickListener(v -> tintSelectedAvatar(imgCats[1]));
        imgCats[2].setOnClickListener(v -> tintSelectedAvatar(imgCats[2]));
        imgCats[3].setOnClickListener(v -> tintSelectedAvatar(imgCats[3]));
        imgCats[4].setOnClickListener(v -> tintSelectedAvatar(imgCats[4]));
        imgCats[5].setOnClickListener(v -> tintSelectedAvatar(imgCats[5]));
    }

    private void selectImageView(ImageView imageView, boolean isDefault) {
        if (isDefault) {
            imageView.setAlpha(ResourcesUtils.getFloat(requireContext(), R.dimen.default_image_alpha));
        } else {
            imageView.setAlpha(ResourcesUtils.getFloat(requireContext(), R.dimen.selected_image_alpha));
        }
    }

    private void tintSelectedAvatar(ImageView imgCat) {
        for (ImageView arrayCat : this.imgCats) {
            selectImageView(arrayCat, true);
        }
        selectImageView(imgCat, false);
        for (int i = 0; i < this.imgCats.length; i++) {
            if (this.imgCats[i].getAlpha() != 1f) {
                avatarFromUser = viewModel.queryAvatars().get(i);
                break;
            }
        }
    }


}
