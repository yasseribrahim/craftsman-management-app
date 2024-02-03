package com.craftsman.management.app.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.craftsman.management.app.Constants;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ActivityServiceBinding;
import com.craftsman.management.app.models.Category;
import com.craftsman.management.app.models.ChatId;
import com.craftsman.management.app.models.Notification;
import com.craftsman.management.app.models.Price;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.models.User;
import com.craftsman.management.app.persenters.notification.NotificationsCallback;
import com.craftsman.management.app.persenters.notification.NotificationsPresenter;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.persenters.user.UsersCallback;
import com.craftsman.management.app.persenters.user.UsersPresenter;
import com.craftsman.management.app.ui.adptres.PricesAdapter;
import com.craftsman.management.app.ui.fragments.CategorySelectorBottomSheetDialog;
import com.craftsman.management.app.ui.fragments.PriceBottomSheetDialog;
import com.craftsman.management.app.ui.fragments.ProgressDialogFragment;
import com.craftsman.management.app.utilities.DatesUtils;
import com.craftsman.management.app.utilities.ToastUtils;
import com.craftsman.management.app.utilities.helpers.LocaleHelper;
import com.craftsman.management.app.utilities.helpers.StorageHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class ServiceActivity extends BaseActivity implements ServicesCallback, PriceBottomSheetDialog.OnPriceChangedCallback, PricesAdapter.OnPricesClickListener, CategorySelectorBottomSheetDialog.OnCategorySelectedCallback, UsersCallback, NotificationsCallback, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private static final int REQUEST_CODE_CAMERA_AND_STORAGE = 100;
    private static final String TAG = ServiceActivity.class.getSimpleName();
    private ActivityServiceBinding binding;
    private ServicesPresenter servicesPresenter;
    private NotificationsPresenter notificationsPresenter;
    private UsersPresenter usersPresenter;
    private PricesAdapter adapter;
    private Service service;
    private List<User> users;
    private User currentUser;
    private Category selectedCategory;
    private boolean canEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        servicesPresenter = new ServicesPresenter(this);
        notificationsPresenter = new NotificationsPresenter(this);
        usersPresenter = new UsersPresenter(this);

        service = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        currentUser = StorageHelper.getCurrentUser();
        canEdit = currentUser.getUsername().equalsIgnoreCase(service.getCreatedBy());

        if (service.getPrices() == null) {
            service.setPrices(new ArrayList<>());
        }

        var canAccepted = canEdit;
        var canChat = canEdit;
        adapter = new PricesAdapter(service.getPrices(), this, canChat, canAccepted);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        bind();

        users = new ArrayList<>();

        if (canEdit) {
            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    methodRequiresTwoPermission();
                }
            });

            binding.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategorySelectorBottomSheetDialog dialog = CategorySelectorBottomSheetDialog.newInstance(selectedCategory);
                    dialog.show(getSupportFragmentManager(), "");
                }
            });

            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!users.isEmpty()) {
                        String title = binding.title.getText().toString().trim();
                        String description = binding.description.getText().toString().trim();

                        if (selectedCategory == null) {
                            binding.category.setError(getString(R.string.str_category_hint));
                            return;
                        }

                        if (title.isEmpty()) {
                            binding.title.setError(getString(R.string.str_title_hint));
                            binding.title.requestFocus();
                            return;
                        }
                        if (description.isEmpty()) {
                            binding.description.setError(getString(R.string.str_description_hint));
                            binding.description.requestFocus();
                            return;
                        }

                        service.setTitle(title);
                        service.setDescription(binding.description.getText().toString());
                        if (service.getId() == null) {
                            service.setCreatedBy(StorageHelper.getCurrentUser().getUsername());
                        }

                        servicesPresenter.save(service);
                    } else {
                        ToastUtils.longToast("Please Wait!!!");
                    }
                }
            });
        } else {
            binding.btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user1 = null;
                    User user2 = currentUser;
                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(service.getCreatedBy())) {
                            user1 = user;
                            break;
                        }
                    }

                    if (user1 != null) {
                        ChatId id = new ChatId(user1, user2);
                        Intent intent = new Intent(ServiceActivity.this, MessagingActivity.class);
                        intent.putExtra(Constants.ARG_OBJECT, id);
                        startActivity(intent);
                    }
                }
            });

            binding.btnAddPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Price price = new Price("", currentUser.getUsername(), 0, Calendar.getInstance().getTime(), false);
                    int index = service.getPrices() != null ? service.getPrices().indexOf(price) : -1;
                    if (index != -1) {
                        price = service.getPrices().get(index);
                    }
                    PriceBottomSheetDialog dialog = PriceBottomSheetDialog.newInstance(service, price);
                    dialog.show(getSupportFragmentManager(), "");
                }
            });
        }
    }

    @Override
    public void onPriceChangedCallback(Service service) {
        this.service = service;
        bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersPresenter.getUsers(0);
    }

    @Override
    public void onGetUsersComplete(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
    }

    @Override
    public void onSaveServiceComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        Notification notification = new Notification();
        notification.setMessage(getString(R.string.str_notification_message, service.getTitle(), DatesUtils.formatDate(service.getDate())));
        notification.setEventId(service.getId());
        if (StorageHelper.getCurrentUser().isAdmin()) {
            notificationsPresenter.save(notification, users);
        } else {
            finish();
        }
    }

    @Override
    public void onSaveNotificationComplete() {
        finish();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    private void takePhoto() {
        if (hasCameraPermission() && hasStoragePermission()) {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
        } else {
            requestPermissions();
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }

    private boolean hasStoragePermission() {
        String[] permissions = getPermissionsStorage();
        return EasyPermissions.hasPermissions(this, permissions);
    }

    private void requestPermissions() {
        if (!hasCameraPermission()) {
            requestCameraPermission();
        } else {
            requestStoragePermission();
        }
    }

    private void requestCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA};
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, REQUEST_CODE_CAMERA_AND_STORAGE, permissions)
                        .setRationale(R.string.str_message_request_camera_and_storage)
                        .setPositiveButtonText(R.string.str_ok)
                        .setNegativeButtonText(R.string.str_cancel)
                        .build());
    }

    private String[] getPermissionsStorage() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        return permissions;
    }

    private void requestStoragePermission() {
        String[] permissions = getPermissionsStorage();
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, REQUEST_CODE_CAMERA_AND_STORAGE, permissions)
                        .setRationale(R.string.str_message_request_camera_and_storage)
                        .setPositiveButtonText(R.string.str_ok)
                        .setNegativeButtonText(R.string.str_cancel)
                        .build());
    }

    @AfterPermissionGranted(REQUEST_CODE_CAMERA_AND_STORAGE)
    private void methodRequiresTwoPermission() {
        if (hasCameraPermission() && hasStoragePermission()) {
            takePhoto();
        } else {
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                if (CropImage.isReadExternalStoragePermissionsRequired(this, uri)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    upload(uri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ToastUtils.longToast(error.getMessage());
            }
        }
    }

    private void upload(Uri uri) {
        ProgressDialogFragment.show(getSupportFragmentManager());
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(Constants.NODE_NAME_IMAGES + "/" + Calendar.getInstance().getTimeInMillis());
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ToastUtils.longToast(R.string.str_message_updated_successfully);
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;

                        service.setImageUrl(downloadUrl.toString());
                        bind();
                        ProgressDialogFragment.hide(getSupportFragmentManager());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ToastUtils.longToast("Error: " + e.getMessage());
                ProgressDialogFragment.hide(getSupportFragmentManager());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.i("ProfileEditActivity", "Uploaded  " + (int) progress + "%");
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        takePhoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }

    private void bind() {
        binding.title.setEnabled(canEdit);
        binding.description.setEnabled(canEdit);
        binding.btnSave.setVisibility(canEdit ? View.VISIBLE : View.GONE);
        binding.btnAddPrice.setVisibility(currentUser.isCraftsman() ? View.VISIBLE : View.GONE);
        binding.btnChat.setVisibility(currentUser.isCraftsman() ? View.VISIBLE : View.GONE);

        binding.username.setText(service.getCreatedBy());
        binding.title.setText(service.getTitle());
        binding.description.setText(service.getDescription());
        binding.category.setText(service.getCategoryName());
        binding.date.setText(DatesUtils.formatDate(service.getDate()));
        Glide.with(this).load(service.getImageUrl()).placeholder(R.drawable.default_image).into(binding.image);
        if (service.getId() != null) {
            selectedCategory = new Category(service.getCategoryId(), service.getCategoryName());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCategorySelectedCallback(Category category) {
        selectedCategory = category;
        service.setCategoryId(category.getId());
        service.setCategoryName(category.getName());
        binding.category.setText(service.getCategoryName());
    }

    @Override
    public void onPriceEditListener(Price price) {

    }

    @Override
    public void onPriceDeleteListener(Price price) {

    }

    @Override
    public void onPriceChatListener(Price price) {
        var user1 = currentUser;
        User user2 = null;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(price.getCraftsmanId())) {
                user2 = user;
                break;
            }
        }

        if (user2 != null) {
            ChatId id = new ChatId(user1, user2);
            Intent intent = new Intent(this, MessagingActivity.class);
            intent.putExtra(Constants.ARG_OBJECT, id);
            startActivity(intent);
        }
    }
}