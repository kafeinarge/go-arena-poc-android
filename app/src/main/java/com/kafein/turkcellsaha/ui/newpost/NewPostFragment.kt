package com.kafein.turkcellsaha.ui.newpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kafein.turkcellsaha.R
import com.kafein.turkcellsaha.data.model.WallContent
import com.kafein.turkcellsaha.databinding.FragmentNewPostBinding
import com.kafein.turkcellsaha.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.*
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class NewPostFragment : BaseFragment<FragmentNewPostBinding>() {

    override val layoutRes: Int = R.layout.fragment_new_post

    private val viewModel: NewPostViewModel by viewModels()
    private lateinit var easyImage: EasyImage
    private var selectedPhoto: MediaFile? = null
    private var selectedMode: Int? = 0
    private var content: WallContent? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedMode = arguments?.getInt(KEY_MODE, KEY_VIEW_MODE)
        content = arguments?.getSerializable(KEY_CONTENT) as WallContent?


        content?.let {
            binding.etPost.setText(content?.text)
            Glide.with(binding.ivSelected.context).load(Base64.decode(content?.preview, Base64.DEFAULT))
                .into(binding.ivSelected)
        }


        easyImage = EasyImage.Builder(requireContext())
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(false)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("EasyImage sample")
            .allowMultiple(false)
            .build();

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivNewPost.setOnClickListener {
            sendPost()
        }

        binding.ivGallery.setOnClickListener {
            val necessaryPermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (arePermissionsGranted(necessaryPermissions)) {
                easyImage.openGallery(this)
            } else {
                requestPermissionsCompat(necessaryPermissions, GALLERY_REQUEST_CODE)
            }
        }

        binding.ivCamera.setOnClickListener {
            val necessaryPermissions = arrayOf(Manifest.permission.CAMERA)
            if (arePermissionsGranted(necessaryPermissions)) {
                easyImage.openCameraForImage(this)
            } else {
                requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE)
            }
        }


        viewModel.newPostLiveData_.observe(viewLifecycleOwner, Observer {
            activity?.onBackPressed()
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(this)
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(this)
        } else if (requestCode == CAMERA_VIDEO_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForVideo(this)
        } else if (requestCode == GALLERY_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openGallery(this)
        } else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    onPhotosReturned(imageFiles)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })

    }

    private fun sendPost() {
        selectedMode?.let {
            when (it) {
                KEY_VIEW_MODE -> {
                    if (binding.etPost.text.toString()
                            .isNullOrEmpty() && selectedPhoto?.file == null
                    ) {
                        binding.etPost.error = getString(R.string.warning_new_post)
                    } else {
                        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                        val bitmap: Bitmap? =
                            (binding.ivSelected.drawable as BitmapDrawable?)?.bitmap
                        val bos = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 30, bos)
                        builder.addFormDataPart(
                            "file", selectedPhoto?.file?.name, RequestBody.create(
                                MultipartBody.FORM,
                                bos.toByteArray()
                            )
                        )

                        viewModel.submitPost(builder.build(), binding.etPost.text.toString())
                    }
                }

                KEY_EDIT_MODE -> {
                    if (binding.etPost.text.toString()
                            .isNullOrEmpty() && selectedPhoto?.file == null
                    ) {
                        binding.etPost.error = getString(R.string.warning_new_post)
                    } else {
                        content?.id?.let { it1 -> viewModel.updatePost(it1, binding.etPost.text.toString()) }
                    }

                }

                else -> {

                }

            }
        }

    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    private fun requestPermissionsCompat(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
    }

    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        selectedPhoto = returnedPhotos[0]
        Glide.with(binding.ivSelected.context).load(selectedPhoto!!.file).into(binding.ivSelected)
    }


    companion object {

        const val KEY_MODE = "extra_mode"
        const val KEY_CONTENT = "content"
        const val KEY_VIEW_MODE = 100
        const val KEY_EDIT_MODE = 101

        private const val PHOTOS_KEY = "easy_image_photos_list"
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
        private const val CAMERA_REQUEST_CODE = 7500
        private const val CAMERA_VIDEO_REQUEST_CODE = 7501
        private const val GALLERY_REQUEST_CODE = 7502
        private const val DOCUMENTS_REQUEST_CODE = 7503

        fun newInstance(content: WallContent, mode: Int): NewPostFragment {
            val fragment = NewPostFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(KEY_CONTENT, content)
                putInt(KEY_MODE, mode)
            }
            return fragment
        }
    }
}